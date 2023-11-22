package org.firstinspires.ftc.teamcode.BluCru.subsystems;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BluCru.Constants;
import org.firstinspires.ftc.teamcode.BluCru.states.LiftState;

public class Lift implements Subsystem{
    public LiftState liftState;
    private DcMotorEx liftMotor, liftMotor2;
    private PIDController liftPID;

    private double PID;
    private double ff = Constants.sliderF;

    public int targetPos = 0;
    private int currentPos;

    private ElapsedTime sliderStallTimer;

    public Lift(HardwareMap hardwareMap, Telemetry telemetry) {
        // declares motors
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift1");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "lift2");
        // set direction
        liftMotor.setDirection(DcMotorEx.Direction.FORWARD);
        liftMotor2.setDirection(DcMotorEx.Direction.REVERSE);

        liftState = LiftState.RETRACT;
    }

    public void init() {
        setTargetPos(0);
        liftPID = new PIDController(Constants.sliderP, Constants.sliderI, Constants.sliderD);

        //set all motors to zero power
        liftMotor.setPower(0);
        liftMotor2.setPower(0);

        //set brake behavior
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // reset motor encoders
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        sliderStallTimer = new ElapsedTime();
    }

    public void update() {
        targetPos = Range.clip(targetPos, Constants.sliderMinPos, Constants.sliderMaxPos);
        currentPos = getCurrentPos();
        PID = liftPID.calculate(currentPos, targetPos);

        if(targetPos == 0 && currentPos < 10) {
            setPower(0);
        } else if(Math.abs(targetPos - currentPos) < 10) {
            setPower(ff);
        } else {
            setPower(PID);
        }
    }

    public void setPower(double power) {
        power = Range.clip(power, -1, 1);
        liftMotor.setPower(power);
        liftMotor2.setPower(power);
    }

    public void setTargetPos(int pos) {
        targetPos = pos;
    }

    public void resetSliderStallTimer() {
        sliderStallTimer.reset();
    }

    public int getTargetPos() {
        return targetPos;
    }

    public int getCurrentPos() {
        return liftMotor.getCurrentPosition();
    }
}
