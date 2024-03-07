package org.firstinspires.ftc.teamcode.blucru.common.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.blucru.common.states.Alliance;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Hanger;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Intake;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.IntakeWrist;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Lift;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Locks;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Plane;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.PurplePixelHolder;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Robot;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Turret;

public class BCLinearOpMode extends LinearOpMode {
    public Alliance alliance;
    Robot robot;
    public Drivetrain drivetrain;
    public Outtake outtake;
    public Lift lift;
    public Turret turret;
    public Intake intake;
    public IntakeWrist intakeWrist;
    public Plane plane;
    public Hanger hanger;
    public PurplePixelHolder purplePixelHolder;
    public Locks locks;

    ElapsedTime runtime;
    double lastTime;
    double loopTimeSum;
    int loopTimeCount;
    double lastTelemetryTime;

    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap);
        robot.init();
        initialize();
        while(opModeInInit()) {
            initLoop();
            telemetry.update();
        }
        waitForStart();
        runtime = new ElapsedTime(); // start timer
        while (opModeIsActive()) {
            read();
            robot.read();

            write();
            robot.write();

            // calculate average loop time
            loopTimeSum += runtime.milliseconds() - lastTime;
            lastTime = runtime.milliseconds();
            loopTimeCount++;

            if(timeSinceMillis(lastTelemetryTime) > 100) { // update telemetry every 0.1 seconds
                lastTelemetryTime = runtime.milliseconds();
                telemetry();
                robot.telemetry(telemetry);

                double loopTimeAvg = loopTimeSum / loopTimeCount;
                resetLoopTime();
                telemetry.addData("Loop Time", loopTimeAvg);
                telemetry.update();
            }
        }
    }

    // methods to be overriden
    public void initialize() {}
    public void initLoop() {}
    public void read() {}
    public void write() {}
    public void telemetry() {}

    public void addDrivetrain(boolean isTeleOp) {drivetrain = robot.addDrivetrain(isTeleOp);}

    public void addOuttake() {outtake = robot.addOuttake();}

    public void addIntake() {intake = robot.addIntake();}

    public void addIntakeWrist() {intakeWrist = robot.addIntakeWrist();}

    public void addPlane() {plane = robot.addPlane();}

    public void addHanger() {hanger = robot.addHanger();}

    public void addPurplePixelHolder() {purplePixelHolder = robot.addPurplePixelHolder();}

    public void addLocks() {locks = robot.addLocks();}

    public double timeSinceSecs(double timeSecs) {
        return runtime.seconds() - timeSecs;
    }

    private double timeSinceMillis(double timeMillis) {
        return runtime.milliseconds() - timeMillis;
    }

    private void resetLoopTime() {
        loopTimeSum = 0;
        loopTimeCount = 0;
    }
}
