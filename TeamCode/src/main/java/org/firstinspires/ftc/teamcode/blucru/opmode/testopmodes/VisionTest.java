package org.firstinspires.ftc.teamcode.blucru.opmode.testopmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.blucru.common.states.Alliance;
import org.firstinspires.ftc.teamcode.blucru.common.util.AprilTagLocalizer;
import org.firstinspires.ftc.teamcode.blucru.common.vision.CVMaster;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@TeleOp(name="vision test", group="Linear Opmode")
public class VisionTest extends LinearOpMode {
    CVMaster cvMaster;
    Alliance alliance = Alliance.BLUE;

    String status;

    boolean lastLB1 = false;

    @Override
    public void runOpMode() {
        cvMaster = new CVMaster(hardwareMap, Alliance.BLUE);

        telemetry.addLine("camera starting");
        telemetry.update();


        cvMaster.detectProp();
        // Init
        while (opModeInInit() && !isStopRequested()) {
            if(gamepad1.left_bumper && !lastLB1) {
                if(alliance == Alliance.BLUE) {
                    alliance = Alliance.RED;
                } else {
                    alliance = Alliance.BLUE;
                }
                cvMaster.propDetector.setAlliance(alliance);
            }
            lastLB1 = gamepad1.left_bumper;

            telemetry.addLine("left bumper to change alliance");
            telemetry.addData("Status", "Initialized");
            telemetry.addData("Alliance", alliance);
            telemetry.addData("Average0", cvMaster.propDetector.average0);
            telemetry.addData("Average1", cvMaster.propDetector.average1);
            telemetry.addData("Average2", cvMaster.propDetector.average2);
            telemetry.addData("position", cvMaster.propDetector.position);
            telemetry.update();
        }
        waitForStart();

        cvMaster.stop();

        // Run
        while (opModeIsActive()) {

            if(gamepad1.b) {
                status = "detecting prop";
                cvMaster.detectProp();
            }
            if(gamepad1.x) {
                status = "detecting tag";
                cvMaster.detectTag();
            }
            if(gamepad1.a) {
                status = "stopped";
                cvMaster.stop();
            }

            switch (status) {
                case "detecting prop":
                    telemetry.addData("Average0", cvMaster.propDetector.average0);
                    telemetry.addData("Average1", cvMaster.propDetector.average1);
                    telemetry.addData("Average2", cvMaster.propDetector.average2);
                    telemetry.addData("position", cvMaster.propDetector.position);
                    break;
                case "detecting tag":
                    List<AprilTagDetection> currentDetections = cvMaster.tagDetector.getDetections();

                    for (AprilTagDetection detection : currentDetections) {
                        if(detection.id == 3) {
                            Pose2d pose3 = AprilTagLocalizer.getRobotPose(3, detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.yaw);
                            telemetry.addLine(String.format("found tag 3, pose estimate (x, y, heading): ", pose3.getX(), pose3.getY(), pose3.getHeading()));
                        }

                        if(detection.id == 4) {
                            Pose2d pose4 = AprilTagLocalizer.getRobotPose(4, detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.yaw);
                            telemetry.addLine(String.format("found tag 4, pose estimate (x, y, heading): ", pose4.getX(), pose4.getY(), pose4.getHeading()));
                        }

                        if (detection.metadata != null) {
                            telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                            telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                            telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                            telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                        } else {
                            telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                            telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
                        }
                    }   // end for() loop
                    break;
            }

            telemetry.addData("Status", status);
            telemetry.update();
        }
    }
}