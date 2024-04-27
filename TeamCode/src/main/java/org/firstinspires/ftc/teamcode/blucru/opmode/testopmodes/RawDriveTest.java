package org.firstinspires.ftc.teamcode.blucru.opmode.testopmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.blucru.opmode.BCLinearOpMode;

@TeleOp(name = "raw drive test", group = "test")
public class RawDriveTest extends BCLinearOpMode {
    double vert, horz, rotate;
    @Override
    public void initialize() {
        addDrivetrain(true);
        addIntakeWrist();
    }

    @Override
    public void periodic() {
        vert = -gamepad1.left_stick_y;
        horz = gamepad1.left_stick_x;
        rotate = -gamepad1.right_stick_x;

        drivetrain.driveScaled(horz ,vert, rotate);
    }

    @Override
    public void telemetry() {
        drivetrain.testTelemetry(telemetry);
    }
}
