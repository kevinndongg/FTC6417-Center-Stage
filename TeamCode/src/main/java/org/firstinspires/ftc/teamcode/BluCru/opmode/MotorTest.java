package org.firstinspires.ftc.teamcode.BluCru.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "motor test", group = "TeleOp")
public class MotorTest extends LinearOpMode {
    DcMotorEx[] motors = {};

    double pos = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx fr = hardwareMap.get(DcMotorEx.class, "frontRight");
        DcMotorEx fl = hardwareMap.get(DcMotorEx.class, "frontLeft");
        DcMotorEx br = hardwareMap.get(DcMotorEx.class, "backRight");
        DcMotorEx bl = hardwareMap.get(DcMotorEx.class, "backLeft");
        motors[0] = fr;
        motors[1] = fl;
        motors[2] = br;
        motors[3] = bl;

        for(DcMotorEx motor : motors) {
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
            motor.setPower(0);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        boolean lastLB1 = false;
        boolean lastRB1 = false;
        int delta = 100;
        int pos = 0;
        double vert;

        waitForStart();
        while(opModeIsActive()) {
            vert = -gamepad1.left_stick_y;



            delta = (int)(-gamepad1.right_stick_y*200) + 200;
            if(gamepad1.right_bumper && !lastRB1) {
                fr.setTargetPosition(fr.getCurrentPosition() + 100);
            }
            if(gamepad1.left_bumper && !lastLB1) {
                fr.setTargetPosition(fr.getCurrentPosition() - 100);
            }
            if(gamepad1.a) {
                pos = fr.getCurrentPosition();
            }
            if(vert > 0.1) {
                fr.setPower(vert);
            } else {
                fr.setPower(0);
            }

            lastLB1 = gamepad1.left_bumper;
            lastRB1 = gamepad1.right_bumper;

            telemetry.addData("target", fr.getTargetPosition());
            telemetry.addData("current", fr.getCurrentPosition());
            telemetry.addData("delta", delta);
            telemetry.addData("power", fr.getPower());
            telemetry.addData("saved pos", pos);
            telemetry.update();
        }
    }
}