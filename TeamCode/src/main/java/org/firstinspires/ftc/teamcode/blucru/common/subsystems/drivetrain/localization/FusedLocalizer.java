package org.firstinspires.ftc.teamcode.blucru.common.subsystems.drivetrain.localization;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Robot;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class FusedLocalizer {
    Localizer deadWheels;
    IMU imu;
    PoseHistory poseHistory;
    long lastFrameTime;

    IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

    double headingOffset = 0;
    double lastImuUpdateMillis = 0;
    public boolean usingIMU = true;

    YawPitchRollAngles ypr;

    public FusedLocalizer(Localizer localizer, HardwareMap hardwareMap) {
        deadWheels = localizer;
        poseHistory = new PoseHistory();

        imu = hardwareMap.get(IMU.class, "e hub imu");
        lastFrameTime = System.nanoTime();
    }

    public void update() {
        // make a copy of the current pose, so that the pose history doesn't get updated with the same object
        deadWheels.update();
        Pose2d currentPose = deadWheels.getPoseEstimate();
        //Log.v("Marker Entry", "Pos" + currentPose);
        poseHistory.add(currentPose, deadWheels.getPoseVelocity());

        if(System.currentTimeMillis() - lastImuUpdateMillis > 100 && usingIMU) {
            lastImuUpdateMillis = System.currentTimeMillis();

            ypr = imu.getRobotYawPitchRollAngles();
            Log.v("FusedLocalizer", "Updating IMU, correction = " + (ypr.getYaw(AngleUnit.RADIANS) + headingOffset - deadWheels.getPoseEstimate().getHeading()));
            Pose2d currentPoseWithHeading = new Pose2d(currentPose.getX(), currentPose.getY(), Angle.norm(ypr.getYaw(AngleUnit.RADIANS) + headingOffset));
            deadWheels.setPoseEstimate(currentPoseWithHeading);
            deadWheels.update();
        }
    }

    public void updateAprilTags(AprilTagProcessor tagProcessor) {
        Pose2d currentPose = deadWheels.getPoseEstimate();
        double heading = Angle.norm(currentPose.getHeading());
        if(heading < Math.PI/2 || heading > 3*Math.PI/2) throw new IllegalArgumentException("Not in the right orientation to update tags");

        ArrayList<AprilTagDetection> detections = tagProcessor.getDetections();
        if(detections.size() < 1) throw new NoSuchElementException("No tags detected");

        // get odo pose at the time of the tag pose
        long timeOfFrame = detections.get(0).frameAcquisitionNanoTime;
        if(timeOfFrame==lastFrameTime) {
            Log.i("FusedLocalizer", "Already updated with this frame");
            return;
        }
        Pose2d odoPoseAtFrame = poseHistory.getPoseAtTime(timeOfFrame);

        long timeSinceFrame = System.nanoTime() - timeOfFrame;
        Log.v("FusedLocalizer", "Time since frame:" + timeSinceFrame);

        // save reference to tag pose
        Pose2d tagPose = AprilTagPoseGetter.getRobotPoseAtTimeOfFrame(detections, odoPoseAtFrame.getHeading());

        Pose2d weightedEstimateAtFrame = tagPose.minus(odoPoseAtFrame).times(getWeight()).plus(odoPoseAtFrame);

        // calculate change from old odo pose to current pose
        Pose2d odoDelta = currentPose.minus(odoPoseAtFrame);

        Log.i("FusedLocalizer", "Updating pose");
        Log.v("FusedLocalizer", "History odo pose:" + odoPoseAtFrame);
        Log.v("FusedLocalizer", "tag pose: " + tagPose);
        Log.v("FusedLocalizer", "current pose: " + currentPose);
        Log.v("FusedLocalizer", "delta: " + odoDelta);

        Pose2d newPose = new Pose2d(weightedEstimateAtFrame.vec().plus(odoDelta.vec()), currentPose.getHeading());
        Log.v("FusedLocalizer", "new pose: " + newPose);

        // set pose estimate to tag pose + delta
        deadWheels.setPoseEstimate(newPose);
        deadWheels.update();
        // add tag - odo to pose history
        Pose2d odoPoseError = weightedEstimateAtFrame.minus(odoPoseAtFrame);
        Log.v("FusedLocalizer", "odoPoseError: " + odoPoseError);
        poseHistory.offset(odoPoseError);
        lastFrameTime = timeOfFrame;
    }

    public void init() {
        imu.resetDeviceConfigurationForOpMode();
        imu.initialize(parameters);

        lastImuUpdateMillis = System.currentTimeMillis();
    }

    public void resetHeading(double newHeading) {
        newHeading = Angle.norm(newHeading);
        headingOffset = newHeading - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        deadWheels.setPoseEstimate(new Pose2d(deadWheels.getPoseEstimate().vec(), newHeading));
        deadWheels.update();
    }

    public double getWeight() {
        Pose2d vel = deadWheels.getPoseVelocity();
        double velocity = vel.vec().norm();

        double weight = Range.clip(-0.5*Math.atan(.3 * velocity-8.0), 0, 1);
        return 1;
    }
}
