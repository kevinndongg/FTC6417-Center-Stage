package org.firstinspires.ftc.teamcode.BluCru.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

import org.firstinspires.ftc.teamcode.BluCru.Constants;

@Config
@TeleOp(name = "servo test", group = "TeleOp")
public class ServoTest extends LinearOpMode {
    public static double position = 0.5;
    public static String name = "outtake wrist";
    @Override
    public void runOpMode() throws InterruptedException {
        Servo test = hardwareMap.get(Servo.class, name);
        ServoControllerEx controller = (ServoControllerEx) test.getController();
        controller.setServoPwmDisable(test.getPortNumber());
        waitForStart();
        while(opModeIsActive()) {
            if(gamepad1.a) {
                controller.setServoPwmEnable(test.getPortNumber());
                test.setPosition(position);
            } else {
                controller.setServoPwmDisable(test.getPortNumber());
            }

            telemetry.addData("name", name);
            telemetry.addData("position", test.getPosition());
            telemetry.update();
        }
    }
}