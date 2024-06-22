package org.firstinspires.ftc.teamcode.blucru.opmode.testopmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.blucru.common.states.RobotState;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Robot;
import org.firstinspires.ftc.teamcode.blucru.opmode.BCLinearOpMode;

@TeleOp(name = "intake test", group = "test")
public class IntakeTest extends BCLinearOpMode {
    public static int stackHeight = 4;
    double horz, vert, rotate;

    RobotState robotState;

    public void initialize() {
        addDrivetrain(true);
        addIntake();
    }

    public void periodic() {
//        vert = -gamepad1.left_stick_y;
//        horz = gamepad1.left_stick_x;
//        rotate = -gamepad1.right_stick_x;

//        if(gamepad1.right_stick_button) {
//            drivetrain.resetHeading(Math.toRadians(90));
//            gamepad1.rumble(150);
//        }

//        if(gamepad1.b) drivetrain.driveToHeadingScaled(horz, vert, Math.toRadians(180));
//        else if (gamepad1.x) drivetrain.driveToHeadingScaled(horz, vert, 0);
//        else drivetrain.driveScaled(horz, vert, rotate);


        if(gamepad1.left_trigger > 0.1) intake.setIntakePower(gamepad1.left_trigger);
        else if (gamepad1.right_trigger > 0.1) intake.setIntakePower(-gamepad1.right_trigger);
        else intake.setIntakePower(0);

        if(gamepad1.a) intake.dropToGround();
        else if(gamepad1.y) intake.dropToStack(stackHeight);
        else intake.retractIntakeWrist();

        if(stickyG1.dpad_up) {
            stackHeight = Range.clip(stackHeight+1, 0, 5);
        }

        if(stickyG1.dpad_down) {
            stackHeight = Range.clip(stackHeight-1, 0, 5);
        }
    }

    public void telemetry() {
        telemetry.addData("Stack height", stackHeight);
    }
}
