package org.firstinspires.ftc.teamcode.blucru.opmode.testopmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.blucru.common.states.DrivetrainState;
import org.firstinspires.ftc.teamcode.blucru.common.util.BCLinearOpMode;

@Config
@TeleOp(name="Drive To Position Test", group="test")
public class DriveToPositionTest extends BCLinearOpMode {
    public static double p = 0, i = 0, d = 0;
    public static double targetX = 0, targetY = 0, targetHeading = 0;

    String mode = "driver control";

    public void initialize() {
        addDrivetrain(true);
        addIntakeWrist();
        enableFTCDashboard();

        targetX = drivetrain.getPoseEstimate().getX();
        targetY = drivetrain.getPoseEstimate().getY();
        targetHeading = drivetrain.getPoseEstimate().getHeading();
    }

    public void read() {
        drivetrain.translationPID.setPID(p, i, d);
        drivetrain.setTargetPose(new Pose2d(targetX, targetY, targetHeading));

        if(gamepad1.a) {
            mode = "driver control";
        } else if(gamepad1.b) {
            mode = "drive to position";
        }

        if(mode.equals("driver control")) {
            drivetrain.drivetrainState = DrivetrainState.IDLE;

            if(gamepad1.right_stick_button) {
                drivetrain.resetHeading(Math.toRadians(90));
                gamepad1.rumble(100);
            }

            drivetrain.driveMaintainHeading(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x);
        } else if(mode.equals("drive to position")) {
            drivetrain.drivetrainState = DrivetrainState.DRIVE_TO_POSITION;
        }
    }

    public void telemetry() {
        telemetry.addData("mode", mode);
        telemetry.addData("target x", targetX);
        telemetry.addData("target y", targetY);
        telemetry.addData("target heading", targetHeading);
        telemetry.addData("current x", drivetrain.getPoseEstimate().getX());
        telemetry.addData("current y", drivetrain.getPoseEstimate().getY());
        telemetry.addData("current heading", drivetrain.getPoseEstimate().getHeading());
    }
}