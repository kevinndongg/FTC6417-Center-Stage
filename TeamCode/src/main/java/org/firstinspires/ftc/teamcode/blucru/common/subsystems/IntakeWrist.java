package org.firstinspires.ftc.teamcode.blucru.common.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class IntakeWrist implements Subsystem{
    public static double PARALLEL_POS = 0.42;
    public static double RADIUS = 4.7244;

    public static double RETRACT_HEIGHT = 4.7; // inches
    public static double GROUND_HEIGHT = -1.8; // inches for intaking at ground level
    public static double STACK1_HEIGHT = -1.2; // inches for intaking at one pixel height level
    public static double AUTO_MID_HEIGHT = 2.5; // inches for position to be ready to auto stack

    Servo wrist;

    public double targetAngleDeg; // degrees
    private double position;
    public IntakeWrist (HardwareMap hardwareMap) {
        wrist = hardwareMap.get(Servo.class, "intake wrist");
        targetAngleDeg = 90;
        position = toTicks(targetAngleDeg);
    }

    public void init() {
        wrist.setPosition(position);
    }

    public void read() {
        position = toTicks(targetAngleDeg);
    }

    public void write() {
        if(wrist.getPosition() != position) {
            wrist.setPosition(position);
        }
    }

    public double toTicks(double targetAngle) {
        double rawTicks = (-(targetAngle) / 270.0) + PARALLEL_POS;
        return Range.clip(rawTicks, 0.0, 1.0);
    }

    public static double toDeg(double height) {
        return Math.toDegrees(Math.asin(height / RADIUS));
    }

    public static double toX(double height) {
        return RADIUS * Math.cos(Math.asin(height / RADIUS));
    }

    public void dropToStack(int stackHeight) {
        targetAngleDeg = toDeg(getTargetHeight(stackHeight));
    }

    public double getTargetHeight(int stackHeight) {
        if(stackHeight == 0) {
            return GROUND_HEIGHT; // drop to ground
        } else {
            stackHeight = Math.max(0, Math.min(4, stackHeight));
            return STACK1_HEIGHT + (stackHeight - 1) * 0.5;
        }
    }

    public void dropToGround() {
        targetAngleDeg = toDeg(GROUND_HEIGHT);
    }

    public void retract() {
        targetAngleDeg = toDeg(RETRACT_HEIGHT);
    }

    public void dropToAutoMidPos() {
        targetAngleDeg = toDeg(AUTO_MID_HEIGHT);
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("intake wrist pos", position);
    }
}
