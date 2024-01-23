package org.firstinspires.ftc.teamcode.blucru.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.blucru.common.states.Initialization;
import org.firstinspires.ftc.teamcode.blucru.common.states.OuttakeState;
import org.firstinspires.ftc.teamcode.blucru.common.states.RobotState;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Hanger;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Intake;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Robot;


/*
Controls:

release everything: retracted state
a : release cone/intake
b : low height
x : medium height
y : high height
left joystick : strafe (field centric)
right joystick : turn

 */
@Config
@TeleOp(name = "Main TeleOp", group = "1")
public class Duo extends LinearOpMode {
    public static double OUTTAKE_DELAY_SECONDS = 1;

    Robot robot;
    Drivetrain drivetrain;
    Outtake outtake;
    Intake intake;
    Hanger hanger;

    private RobotState robotState;

    private Gamepad lastGamepad1;
    private Gamepad lastGamepad2;
    ElapsedTime totalTimer;
    ElapsedTime outtakeTimer;
    double lastTime, deltaTime;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        while(opModeInInit()) {
            telemetry.addData("PICK UP UR CONTROLELRS", "");
            telemetry.update();
        }

        waitForStart();


        while(opModeIsActive()) {
            deltaTime = totalTimer.milliseconds() - lastTime;
            lastTime = totalTimer.milliseconds();
            // updates states based on gamepad input
            read();

            // loop time: current time - time at start of loop

            // data for feedback
            write();
        }
    }

    public void initialize() {
        robotState = RobotState.RETRACT;
        lastGamepad1 = new Gamepad();
        lastGamepad2 = new Gamepad();
        robot = new Robot(hardwareMap);

        drivetrain = robot.addDrivetrain();
        outtake = robot.addOuttake();
        intake = robot.addIntake();
        hanger = robot.addHanger();

        totalTimer = new ElapsedTime();
        outtakeTimer = new ElapsedTime();

        robot.init();

        // set initial pose from auto
        drivetrain.setPoseEstimate(Initialization.POSE);
    }

    public void read() {
        robot.read();

        // DRIVING
        drivetrain.setDrivePower(robotState, gamepad1);

        double horz = Math.pow(gamepad1.left_stick_x, 3);
        double vert = Math.pow(-gamepad1.left_stick_y, 3);
        double rotate = Math.pow(-gamepad1.right_stick_x, 3);

        // resets heading offset (face forwards)
        if(gamepad1.right_stick_button) {
            drivetrain.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(90)));
            gamepad1.rumble(100);
        }
        if(gamepad1.b) {
            if(gamepad1.left_bumper) {
                drivetrain.driveToDistanceToHeading(horz, vert, Drivetrain.OUTTAKE_DISTANCE, Math.toRadians(180));
            } else {
                drivetrain.driveToHeading(horz, vert, Math.toRadians(180));
            }
        } else if(gamepad1.x) {
            if(gamepad1.left_bumper) {
                drivetrain.driveToDistanceToHeading(horz, vert, Drivetrain.OUTTAKE_DISTANCE, 0);
            } else {
                drivetrain.driveToHeading(horz, vert, 0);
            }
        } else {
            drivetrain.drive(horz, vert, rotate);
        }

        // INTAKE
        if(gamepad1.left_trigger > 0.1) {
            intake.setIntakePower(gamepad1.left_trigger);
            outtake.unlock();
        } else if(gamepad1.right_trigger > 0.1) {
            intake.setIntakePower(-gamepad1.right_trigger);
            outtake.lock();
        } else {
            intake.setIntakePower(0);
            if(gamepad2.left_bumper) {
                outtake.lockBack();
            } else if(gamepad2.right_bumper) {
                outtake.unlock();
            } else {
                outtake.lock();
            }
        }

        // toggle intake wrist
        if(gamepad2.a && outtake.liftIntakeReady()) {
            intake.downIntakeWrist();
        } else {
            intake.retractIntakeWrist();
        }

        switch(robotState) {
            case RETRACT:
                outtake.outtakeState = OuttakeState.RETRACT;

                if(gamepad2.b) {
                    robotState = RobotState.LIFTING;
                    outtake.setTargetHeight(Outtake.LOW_HEIGHT);
                }
                if(gamepad2.x) {
                    robotState = RobotState.LIFTING;
                    outtake.setTargetHeight(Outtake.MED_HEIGHT);
                }
                if(gamepad2.y) {
                    robotState = RobotState.LIFTING;
                    outtake.setTargetHeight(Outtake.HIGH_HEIGHT);
                }
                break;
            case LIFTING:
                outtake.outtakeState = OuttakeState.OUTTAKE;
                if(outtake.lift.getCurrentPos() > Outtake.LIFT_WRIST_CLEAR_POS) {
                    outtake.wristRetracted = false;
                    robotState = RobotState.OUTTAKE;
                    outtakeTimer.reset();
                }

                if(gamepad2.b) {
                    robotState = RobotState.LIFTING;
                    outtake.setTargetHeight(Outtake.LOW_HEIGHT);
                }
                if(gamepad2.x) {
                    robotState = RobotState.LIFTING;
                    outtake.setTargetHeight(Outtake.MED_HEIGHT);
                }
                if(gamepad2.y) {
                    robotState = RobotState.LIFTING;
                    outtake.setTargetHeight(Outtake.HIGH_HEIGHT);
                }

                if(gamepad2.a) {
                    robotState = RobotState.RETRACT;
                    outtake.outtakeState = OuttakeState.RETRACT;
                    outtake.lift.setTargetPos(0);
                }
                break;
            case OUTTAKE:
                outtake.outtakeState = OuttakeState.OUTTAKE;

                if(outtakeTimer.seconds() > OUTTAKE_DELAY_SECONDS) {
                    if(!outtake.wristRetracted) {
                        if (gamepad2.left_trigger > 0.1) {
                            outtake.setTurretAngle(-gamepad2.left_trigger * 60 + 270);
                        } else if (gamepad2.right_trigger > 0.1) {
                            outtake.setTurretAngle(gamepad2.right_trigger * 60 + 270);
                        } else {
                            outtake.setTurretAngle(270);
                        }
                    } else {
                        outtake.setTurretAngle(270);
                    }
                } else {
                    outtake.setTurretAngle(270);
                }

                if(Math.abs(outtake.getTurretAngle() - 270) < 10) {
                    if(gamepad2.dpad_up) {
                        outtake.extendWrist();
                    }
                    if(gamepad2.dpad_down) {
                        outtake.retractWrist();
                    }
                } else {
                    outtake.extendWrist();
                }

                if(gamepad2.b) {
                    outtake.setTargetHeight(Outtake.LOW_HEIGHT);
                }
                if(gamepad2.x) {
                    outtake.setTargetHeight(Outtake.MED_HEIGHT);
                }
                if(gamepad2.y) {
                    outtake.setTargetHeight(Outtake.HIGH_HEIGHT);
                }

                if(gamepad2.a && outtake.wristRetracted) {
                    robotState = RobotState.RETRACT;
                    outtake.outtakeState = OuttakeState.RETRACT;
                    outtake.lift.setTargetPos(0);
                }
                break;
        }

        // MANUAL SLIDE
//        if(Math.abs(gamepad2.right_stick_y) > 0.1) {
//            outtake.lift.liftState = LiftState.MANUAL;
//            outtake.setManualSlidePower(-gamepad2.right_stick_y + Lift.kF);
//        } else {
//            if(!(Math.abs(lastGamepad2.right_stick_y) > 0.1)) {
//                outtake.updateTargetHeight();
//            }
//            outtake.lift.liftState = LiftState.AUTO;
//        }

        // MANUAL HANG
        if(Math.abs(gamepad2.left_stick_y) > 0.1) {
            hanger.setPower(-gamepad2.left_stick_y);
        } else {
            hanger.setPower(0);
        }
    }

    public void write() {
        lastGamepad1.copy(gamepad1);
        lastGamepad2.copy(gamepad2);

        robot.write();

        telemetry.addData("robot state", robotState);
        telemetry.addData("loop time", deltaTime);
        robot.telemetry(telemetry);
        telemetry.update();
    }
}
