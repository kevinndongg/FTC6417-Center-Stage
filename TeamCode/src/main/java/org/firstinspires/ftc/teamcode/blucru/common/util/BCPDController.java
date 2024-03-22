package org.firstinspires.ftc.teamcode.blucru.common.util;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.controller.PIDController;

public class BCPDController extends PIDController {
    Vector2d k;
    public BCPDController(double kP, double kD) {
        super(kP, 0, kD);
        k = new Vector2d(kP, kD);
    }

    public double calculate(double currentPos, double targetPos, double currentVelocity, double targetVelocity) {
        Vector2d p = new Vector2d(currentPos, currentVelocity);
        Vector2d target = new Vector2d(targetPos, targetVelocity);
        Vector2d error = target.minus(p);

        return error.dot(k);
    }
}