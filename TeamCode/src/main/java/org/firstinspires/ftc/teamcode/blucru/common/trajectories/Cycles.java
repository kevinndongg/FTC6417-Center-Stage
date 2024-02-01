package org.firstinspires.ftc.teamcode.blucru.common.trajectories;

import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Intake;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Lift;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Robot;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class Cycles {
    public static double CENTER_TURRET_TIME = 0;
    public static double WRIST_RETRACT_TIME = 0.3;
    public static double LIFT_RETRACT_TIME = 0.4;
    public static double LIFT_TIME = -1;
    public static double WRIST_EXTEND_TIME = -0.75;

    public static double DROP_INTAKE_TIME = -0.5;
    public static double START_INTAKE_TIME = -0.3;

    public static double TOTAL_DEPOSIT_TIME = 0.3;
    public static double INTAKE_TIME = 1.5;

    double reflect = 1;

    public Cycles(double reflect) {
        this.reflect = reflect;
    }

    public TrajectorySequence cyclePerimeterFromFar(Robot robot, int stackHeight) {
        return robot.drivetrain.trajectorySequenceBuilder(Poses.DEPOSIT_FAR_POSE)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .setTangent(Math.toRadians(225 * reflect))

                // retract turret
                .UNSTABLE_addTemporalMarkerOffset(CENTER_TURRET_TIME, () -> robot.outtake.centerTurret())
                // retract wrist
                .UNSTABLE_addTemporalMarkerOffset(WRIST_RETRACT_TIME, () -> robot.outtake.retractWrist())
                // retract lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_RETRACT_TIME, () -> robot.outtake.lift.setMotionProfileTargetPos(0))

                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52 + Poses.FIELD_OFFSET_X, -42 * reflect), Math.toRadians(150))

                // drop intake
                .UNSTABLE_addTemporalMarkerOffset(DROP_INTAKE_TIME, () -> robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT))
                // start and lower intake
                .UNSTABLE_addTemporalMarkerOffset(START_INTAKE_TIME, () -> {
                    robot.intake.dropToStack(stackHeight);
                    robot.intake.setIntakePower(1);
                })

                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
                .splineToSplineHeading(Poses.STACK_CLOSE_POSE, Math.toRadians(150))
                .waitSeconds(INTAKE_TIME)

                // lock and raise intake, start outtaking
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.lock();
                    robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT);
                    robot.intake.setIntakePower(-0.7);
                })
                // stop outtaking and retract wrist
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robot.intake.setIntakePower(0);
                    robot.intake.retractIntakeWrist();
                })

                .setTangent(Math.toRadians(-45 * reflect))
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .splineToConstantHeading(new Vector2d(-30 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))

                // lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_TIME, () -> {
                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CYCLE_POS);
                })
                // wrist back
                .UNSTABLE_addTemporalMarkerOffset(WRIST_EXTEND_TIME, () -> {
                    robot.outtake.extendWrist();
                })
                .setVelConstraint(Constraints.NORMAL_VEL)
                .splineToConstantHeading(new Vector2d(Poses.DEPOSIT_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))
                // release pixel
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.unlock();
                })

                .waitSeconds(TOTAL_DEPOSIT_TIME)
                .build();
    }

    public TrajectorySequence cyclePerimeterFromCenter(Robot robot, int stackHeight) {
        return robot.drivetrain.trajectorySequenceBuilder(Poses.DEPOSIT_CENTER_POSE)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .setTangent(Math.toRadians(225 * reflect))

                // retract turret
                .UNSTABLE_addTemporalMarkerOffset(CENTER_TURRET_TIME, () -> robot.outtake.centerTurret())
                // retract wrist
                .UNSTABLE_addTemporalMarkerOffset(WRIST_RETRACT_TIME, () -> robot.outtake.retractWrist())
                // retract lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_RETRACT_TIME, () -> robot.outtake.lift.setMotionProfileTargetPos(0))

                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52 + Poses.FIELD_OFFSET_X, -42 * reflect), Math.toRadians(150))

                // drop intake
                .UNSTABLE_addTemporalMarkerOffset(DROP_INTAKE_TIME, () -> robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT))
                // start and lower intake
                .UNSTABLE_addTemporalMarkerOffset(START_INTAKE_TIME, () -> {
                    robot.intake.dropToStack(stackHeight);
                    robot.intake.setIntakePower(1);
                })

                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
                .splineToSplineHeading(Poses.STACK_CLOSE_POSE, Math.toRadians(150))
                .waitSeconds(INTAKE_TIME)

                // lock and raise intake, start outtaking
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.lock();
                    robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT);
                    robot.intake.setIntakePower(-0.7);
                })
                // stop outtaking and retract wrist
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robot.intake.setIntakePower(0);
                    robot.intake.retractIntakeWrist();
                })

                .setTangent(Math.toRadians(-45 * reflect))
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .splineToConstantHeading(new Vector2d(-30 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_CLOSE_Y * reflect), Math.toRadians(0))

                // lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_TIME, () -> {
                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CYCLE_POS);
                })
                // wrist back
                .UNSTABLE_addTemporalMarkerOffset(WRIST_EXTEND_TIME, () -> {
                    robot.outtake.extendWrist();
                })

                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
                .splineToConstantHeading(new Vector2d(Poses.DEPOSIT_X, Poses.DEPOSIT_CLOSE_Y * reflect), Math.toRadians(0))

                // release pixel
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.unlock();
                })
                .waitSeconds(TOTAL_DEPOSIT_TIME)
                .build();
    }

    public TrajectorySequence cyclePerimeterFromClose(Robot robot, int stackHeight) {
        return robot.drivetrain.trajectorySequenceBuilder(Poses.DEPOSIT_CLOSE_POSE)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .setTangent(Math.toRadians(225 * reflect))

                // retract turret
                .UNSTABLE_addTemporalMarkerOffset(CENTER_TURRET_TIME, () -> robot.outtake.centerTurret())
                // retract wrist
                .UNSTABLE_addTemporalMarkerOffset(WRIST_RETRACT_TIME, () -> robot.outtake.retractWrist())
                // retract lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_RETRACT_TIME, () -> robot.outtake.lift.setMotionProfileTargetPos(0))

                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(180))
                .setVelConstraint(Constraints.NORMAL_VEL)
                .splineToConstantHeading(new Vector2d(-30 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52 + Poses.FIELD_OFFSET_X, -42 * reflect), Math.toRadians(150))

                // drop intake
                .UNSTABLE_addTemporalMarkerOffset(DROP_INTAKE_TIME, () -> robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT))
                // start and lower intake
                .UNSTABLE_addTemporalMarkerOffset(START_INTAKE_TIME, () -> {
                    robot.intake.dropToStack(stackHeight);
                    robot.intake.setIntakePower(1);
                })

                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
                .splineToSplineHeading(Poses.STACK_CLOSE_POSE, Math.toRadians(150))
                .waitSeconds(INTAKE_TIME)

                // lock and raise intake, start outtaking
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.lock();
                    robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT);
                    robot.intake.setIntakePower(-0.7);
                })
                // stop outtaking and retract wrist
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robot.intake.setIntakePower(0);
                    robot.intake.retractIntakeWrist();
                })

                .setTangent(Math.toRadians(-45 * reflect))
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .splineToConstantHeading(new Vector2d(-30 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_CLOSE_Y * reflect), Math.toRadians(0))

                // lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_TIME, () -> {
                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CYCLE_POS);
                })
                // wrist back
                .UNSTABLE_addTemporalMarkerOffset(WRIST_EXTEND_TIME, () -> {
                    robot.outtake.extendWrist();
                })

                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
                .splineToConstantHeading(new Vector2d(Poses.DEPOSIT_X, Poses.DEPOSIT_CLOSE_Y * reflect), Math.toRadians(0))

                // release pixel
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.unlock();
                })
                .waitSeconds(TOTAL_DEPOSIT_TIME)
                .build();
    }

    public TrajectorySequence cycleCenterFromClose(Robot robot, int stackHeight) {
        return robot.drivetrain.trajectorySequenceBuilder(Poses.DEPOSIT_CLOSE_POSE)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .setTangent(Math.toRadians(135 * reflect))
                // retract turret
                .UNSTABLE_addTemporalMarkerOffset(CENTER_TURRET_TIME, () -> robot.outtake.centerTurret())
                // retract wrist
                .UNSTABLE_addTemporalMarkerOffset(WRIST_RETRACT_TIME, () -> robot.outtake.retractWrist())
                // retract lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_RETRACT_TIME, () -> robot.outtake.lift.setMotionProfileTargetPos(0))
                .splineToConstantHeading(new Vector2d(30, -12 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(Poses.STACK_SETUP_X, -12 * reflect), Math.toRadians(180))
                // drop intake
                .UNSTABLE_addTemporalMarkerOffset(DROP_INTAKE_TIME, () -> robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT))
                // start and lower intake
                .UNSTABLE_addTemporalMarkerOffset(START_INTAKE_TIME, () -> {
                    robot.intake.dropToStack(stackHeight);
                    robot.intake.setIntakePower(1);
                })
                .setVelConstraint(Constraints.NORMAL_VEL)
                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -12 * reflect), Math.toRadians(180))
                .waitSeconds(INTAKE_TIME)

                // lock and raise intake, start outtaking
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.lock();
                    robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT);
                    robot.intake.setIntakePower(-0.7);
                })
                // stop outtaking and retract wrist
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robot.intake.setIntakePower(0);
                    robot.intake.retractIntakeWrist();
                })

                .setTangent(0)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .splineToConstantHeading(new Vector2d(30, -12 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))

                // lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_TIME, () -> {
                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CYCLE_POS);
                })
                // wrist back
                .UNSTABLE_addTemporalMarkerOffset(WRIST_EXTEND_TIME, () -> {
                    robot.outtake.extendWrist();
                })

                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
                .splineToConstantHeading(new Vector2d(Poses.DEPOSIT_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))

                // release pixel
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.unlock();
                })

                .waitSeconds(TOTAL_DEPOSIT_TIME)
                .build();
    }

    public TrajectorySequence cycleCenterFromCenter(Robot robot, int stackHeight) {
        return robot.drivetrain.trajectorySequenceBuilder(Poses.DEPOSIT_CENTER_POSE)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .setTangent(Math.toRadians(135 * reflect))

                // retract turret
                .UNSTABLE_addTemporalMarkerOffset(CENTER_TURRET_TIME, () -> robot.outtake.centerTurret())
                // retract wrist
                .UNSTABLE_addTemporalMarkerOffset(WRIST_RETRACT_TIME, () -> robot.outtake.retractWrist())
                // retract lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_RETRACT_TIME, () -> robot.outtake.lift.setMotionProfileTargetPos(0))

                .splineToConstantHeading(new Vector2d(30, -12 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(Poses.STACK_SETUP_X, -12 * reflect), Math.toRadians(180))

                // drop intake
                .UNSTABLE_addTemporalMarkerOffset(DROP_INTAKE_TIME, () -> robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT))
                // start and lower intake
                .UNSTABLE_addTemporalMarkerOffset(START_INTAKE_TIME, () -> {
                    robot.intake.dropToStack(stackHeight);
                    robot.intake.setIntakePower(1);
                })

                .setVelConstraint(Constraints.NORMAL_VEL)
                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -12 * reflect), Math.toRadians(180))
                .waitSeconds(INTAKE_TIME)

                // lock and raise intake, start outtaking
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.lock();
                    robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT);
                    robot.intake.setIntakePower(-0.7);
                })
                // stop outtaking and retract wrist
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robot.intake.setIntakePower(0);
                    robot.intake.retractIntakeWrist();
                })

                .setTangent(0)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .splineToConstantHeading(new Vector2d(30, -12 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))

                // lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_TIME, () -> {
                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CYCLE_POS);
                })
                // wrist back
                .UNSTABLE_addTemporalMarkerOffset(WRIST_EXTEND_TIME, () -> {
                    robot.outtake.extendWrist();
                })

                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
                .splineToConstantHeading(new Vector2d(Poses.DEPOSIT_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))

                // release pixel
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.unlock();
                })

                .waitSeconds(TOTAL_DEPOSIT_TIME)
                .build();
    }

    public TrajectorySequence cycleCenterFromFar(Robot robot, int stackHeight) {
        return robot.drivetrain.trajectorySequenceBuilder(Poses.DEPOSIT_FAR_POSE)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .setTangent(Math.toRadians(135 * reflect))

                // retract turret
                .UNSTABLE_addTemporalMarkerOffset(CENTER_TURRET_TIME, () -> robot.outtake.centerTurret())
                // retract wrist
                .UNSTABLE_addTemporalMarkerOffset(WRIST_RETRACT_TIME, () -> robot.outtake.retractWrist())
                // retract lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_RETRACT_TIME, () -> robot.outtake.lift.setMotionProfileTargetPos(0))

                .splineToConstantHeading(new Vector2d(30, -12 * reflect), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(Poses.STACK_SETUP_X, -12 * reflect), Math.toRadians(180))

                // drop intake
                .UNSTABLE_addTemporalMarkerOffset(DROP_INTAKE_TIME, () -> robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT))
                // start and lower intake
                .UNSTABLE_addTemporalMarkerOffset(START_INTAKE_TIME, () -> {
                    robot.intake.dropToStack(stackHeight);
                    robot.intake.setIntakePower(1);
                })

                .setVelConstraint(Constraints.NORMAL_VEL)
                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -12 * reflect), Math.toRadians(180))
                .waitSeconds(INTAKE_TIME)

                // lock and raise intake, start outtaking
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.lock();
                    robot.intake.setIntakeWristTargetHeight(Intake.WRIST_AUTO_READY_HEIGHT);
                    robot.intake.setIntakePower(-0.7);
                })
                // stop outtaking and retract wrist
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                    robot.intake.setIntakePower(0);
                    robot.outtake.retractWrist();
                })

                .setTangent(0)
                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
                .splineToConstantHeading(new Vector2d(30, -12 * reflect), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))

                // lift
                .UNSTABLE_addTemporalMarkerOffset(LIFT_TIME, () -> {
                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CYCLE_POS);
                })
                // wrist back
                .UNSTABLE_addTemporalMarkerOffset(WRIST_EXTEND_TIME, () -> {
                    robot.outtake.extendWrist();
                })


                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
                .splineToConstantHeading(new Vector2d(Poses.DEPOSIT_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))

                // release pixel
                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
                    robot.outtake.unlock();
                })

                .waitSeconds(TOTAL_DEPOSIT_TIME)
                .build();
//    }
//
//    public TrajectorySequence depositFromBackdropClose(Robot robot) {
//        double liftTime = 2; // time into trajectory to lift
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.BACKDROP_PLACEMENT_CLOSE_POSE)
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(Math.toRadians(270 * reflect))
//
//                // lift
//                .UNSTABLE_addTemporalMarkerOffset(liftTime, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.YELLOW_POS);
//                })
//                // wrist back
//                .UNSTABLE_addTemporalMarkerOffset(liftTime + WRIST_EXTEND_AFTER_LIFT_TIME, () -> {
//                    robot.outtake.extendWrist();
//                })
//                // turn turret
//                .UNSTABLE_addTemporalMarkerOffset(liftTime + WRIST_EXTEND_AFTER_LIFT_TIME + TURRET_TURN_AFTER_WRIST_TIME, () -> {
//                    robot.outtake.setTurretAngle(270 + 50 * reflect);
//                })
//
//                .splineToConstantHeading(new Vector2d(30, -44 * reflect), 0)
//                .splineToSplineHeading(new Pose2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_CLOSE_Y * reflect, Math.toRadians(180)), 0)
//
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_CLOSE_POSE.vec(), 0)
//
//                // release
//                .UNSTABLE_addTemporalMarkerOffset(RELEASE_TIME, () -> {
//                    robot.outtake.unlock();
//                })
//                // lift clear
//                .UNSTABLE_addTemporalMarkerOffset(LIFT_CLEAR_TIME, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CLEAR_POS);
//                    robot.outtake.centerTurret();
//                })
//
//                .waitSeconds(TOTAL_CLOSE_DEPOSIT_TIME)
//                .build();
//    }
//
//    public TrajectorySequence depositFromBackdropCenter(Robot robot) {
//        double liftTime = 1.5; // time into trajectory to lift
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.BACKDROP_PLACEMENT_CENTER_POSE)
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(Math.toRadians(-90 * reflect))
//                // lift
//                .UNSTABLE_addTemporalMarkerOffset(liftTime, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.YELLOW_POS);
//                })
//                // wrist back
//                .UNSTABLE_addTemporalMarkerOffset(liftTime + WRIST_EXTEND_AFTER_LIFT_TIME, () -> {
//                    robot.outtake.extendWrist();
//                })
//                .splineToConstantHeading(new Vector2d(20, -36 * reflect), 0)
//                .splineToSplineHeading(new Pose2d(Poses.BACKDROP_SETUP_X, -36*reflect, Math.toRadians(180)), 0)
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_CENTER_POSE.vec(), Math.toRadians(0))
//                // release
//                .UNSTABLE_addTemporalMarkerOffset(RELEASE_TIME, () -> {
//                    robot.outtake.unlock();
//                })
//                // lift clear
//                .UNSTABLE_addTemporalMarkerOffset(LIFT_CLEAR_TIME, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CLEAR_POS);
//                })
//                .waitSeconds(TOTAL_CLOSE_DEPOSIT_TIME)
//                .build();
//    }
//
//    public TrajectorySequence depositFromBackdropFar(Robot robot) {
//        double liftTime = 2.5; // time into trajectory to lift
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.BACKDROP_PLACEMENT_FAR_POSE)
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(-45*reflect)
//
//                // lift
//                .UNSTABLE_addTemporalMarkerOffset(liftTime, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.YELLOW_POS);
//                })
//                // wrist back
//                .UNSTABLE_addTemporalMarkerOffset(liftTime + WRIST_EXTEND_AFTER_LIFT_TIME, () -> {
//                    robot.outtake.extendWrist();
//                })
//                // turn turret
//                .UNSTABLE_addTemporalMarkerOffset(liftTime + WRIST_EXTEND_AFTER_LIFT_TIME + TURRET_TURN_AFTER_WRIST_TIME, () -> {
//                    robot.outtake.setTurretAngle(270 - 50 * reflect);
//                })
//
//                .splineToConstantHeading(new Vector2d(8, -42 * reflect), 0)
//                .splineToConstantHeading(new Vector2d(10, -42*reflect), 0)
//                .splineToSplineHeading(new Pose2d(30, -35 * reflect, Math.toRadians(180)), Math.toRadians(45*reflect))
//                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_FAR_Y *reflect), 0)
//
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_FAR_POSE.vec(), Math.toRadians(0))
//
//                // release
//                .UNSTABLE_addTemporalMarkerOffset(RELEASE_TIME, () -> {
//                    robot.outtake.unlock();
//                })
//                // lift clear
//                .UNSTABLE_addTemporalMarkerOffset(LIFT_CLEAR_TIME, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CLEAR_POS);
//                    robot.outtake.centerTurret();
//                })
//                .waitSeconds(TOTAL_CLOSE_DEPOSIT_TIME)
//                .build();
//    }
//
//    public TrajectorySequence depositThroughCenterFromWingClose(Robot robot) {
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.WING_PLACEMENT_CLOSE_FOR_CENTER_POSE)
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .setTangent(Math.toRadians(180 * reflect))
//                // drop down, start intake, unlock
//                .UNSTABLE_addTemporalMarkerOffset(0.9, () -> {
//                    robot.intake.dropToStack(4);
//                    robot.intake.setIntakePower(1);
//                    robot.outtake.unlock();
//                })
//                .splineToConstantHeading(new Vector2d(-53 + Poses.FIELD_OFFSET_X, -24*reflect), Math.toRadians(180))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -24 * reflect), Math.toRadians(180))
//                .waitSeconds(INTAKE_TIME)
//                // lock and start outtake
//                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
//                    robot.outtake.lock();
//                    robot.intake.setIntakePower(-1);
//                })
//                .UNSTABLE_addTemporalMarkerOffset(1, () -> {
//                    robot.intake.setIntakePower(0);
//                    robot.intake.retractIntakeWrist();
//                })
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(0)
//                .splineToConstantHeading(new Vector2d(-40 + Poses.FIELD_OFFSET_X, Poses.CENTER_Y * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .splineToConstantHeading(new Vector2d(30, Poses.CENTER_Y * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
////                // lift
////                .UNSTABLE_addTemporalMarkerOffset(1, () -> {
////                    robot.outtake.lift.setMotionProfileTargetPos(Lift.YELLOW_POS);
////                })
////                // wrist back
////                .UNSTABLE_addTemporalMarkerOffset(1 + WRIST_EXTEND_AFTER_LIFT_TIME, () -> {
////                    robot.outtake.extendWrist();
////                })
////                // turn turret
////                .UNSTABLE_addTemporalMarkerOffset(1 + WRIST_EXTEND_AFTER_LIFT_TIME + TURRET_TURN_AFTER_WRIST_TIME, () -> {
////                    robot.outtake.setTurretAngle(270 - 20 * reflect);
////                })
//                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, -36 * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_CENTER_POSE.vec(), Math.toRadians(0))
////                // release white pixel
////                .UNSTABLE_addTemporalMarkerOffset(RELEASE_TIME, () -> {
////                    robot.outtake.lockBack();
////                })
////                // turn turret
////                .UNSTABLE_addTemporalMarkerOffset(0.3, () -> {
////                    robot.outtake.setTurretAngle(270 + 20 * reflect);
////                })
////                // release yellow pixel
////                .UNSTABLE_addTemporalMarkerOffset(0.7, () -> {
////                    robot.outtake.unlock();
////                })
////                .UNSTABLE_addTemporalMarkerOffset(0.9, () -> {
////                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CLEAR_POS);
////                    robot.outtake.centerTurret();
////                })
//                .waitSeconds(1.3)
//                .build();
//    }
//
//    public TrajectorySequence depositThroughCenterFromWingCenter(Robot robot) {
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.WING_PLACEMENT_CENTER_POSE)
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(Math.toRadians(180 * reflect))
//                // drop to intake, start intake, unlock
//                .UNSTABLE_addTemporalMarkerOffset(0.2, () -> {
//                    robot.intake.dropToStack(4);
//                    robot.intake.setIntakePower(1);
//                    robot.outtake.unlock();
//                })
//                .splineToConstantHeading(new Vector2d(-53 + Poses.FIELD_OFFSET_X, -24*reflect), Math.toRadians(180))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -24 * reflect), Math.toRadians(180))
//                .waitSeconds(INTAKE_TIME)
//                // lock and start outtaking
//                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
//                    robot.outtake.lock();
//                    robot.intake.setIntakePower(-1);
//                })
//                // stop outtake
//                .UNSTABLE_addTemporalMarkerOffset(1, () -> {
//                    robot.intake.setIntakePower(0);
//                    robot.intake.retractIntakeWrist();
//                })
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(Math.toRadians(45 * reflect))
//                .splineToConstantHeading(new Vector2d(-45 + Poses.FIELD_OFFSET_X, Poses.CENTER_Y * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .splineToConstantHeading(new Vector2d(30, Poses.CENTER_Y * reflect), Math.toRadians(0))
//                // lift
//                .UNSTABLE_addTemporalMarkerOffset(1, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.YELLOW_POS);
//                })
//                // wrist back
//                .UNSTABLE_addTemporalMarkerOffset(1 + WRIST_EXTEND_AFTER_LIFT_TIME, () -> {
//                    robot.outtake.extendWrist();
//                })
//                // turn turret
//                .UNSTABLE_addTemporalMarkerOffset(1 + WRIST_EXTEND_AFTER_LIFT_TIME + TURRET_TURN_AFTER_WRIST_TIME, () -> {
//                    robot.outtake.setTurretAngle(270 - 20 * reflect);
//                })
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_FAR_POSE.vec(), Math.toRadians(0))
//                // release white pixel
//                .UNSTABLE_addTemporalMarkerOffset(RELEASE_TIME, () -> {
//                    robot.outtake.lockBack();
//                })
//                // turn turret
//                .UNSTABLE_addTemporalMarkerOffset(0.3, () -> {
//                    robot.outtake.setTurretAngle(270 + 20 * reflect);
//                })
//                // release yellow pixel
//                .UNSTABLE_addTemporalMarkerOffset(0.7, () -> {
//                    robot.outtake.unlock();
//                })
//                .UNSTABLE_addTemporalMarkerOffset(0.9, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CLEAR_POS);
//                    robot.outtake.centerTurret();
//                })
//                .waitSeconds(1.3)
//                .build();
//    }
//
//    public TrajectorySequence depositThroughCenterFromWingFar(Robot robot) {
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.WING_PLACEMENT_FAR_FOR_CENTER_POSE)
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(Math.toRadians(120 * reflect))
//                // drop down, start intake, unlock
//                .UNSTABLE_addTemporalMarkerOffset(0.2, () -> {
//                    robot.intake.dropToStack(4);
//                    robot.intake.setIntakePower(1);
//                    robot.outtake.unlock();
//                })
//                .splineToLinearHeading(new Pose2d(Poses.STACK_X, -12 * reflect, Math.toRadians(180)), Math.toRadians(180))
//                .waitSeconds(INTAKE_TIME)
//                // lock and start outtaking
//                .UNSTABLE_addTemporalMarkerOffset(0, () -> {
//                    robot.outtake.lock();
//                    robot.intake.setIntakePower(-1);
//                })
//                // stop outtake
//                .UNSTABLE_addTemporalMarkerOffset(1, () -> {
//                    robot.intake.setIntakePower(0);
//                    robot.intake.retractIntakeWrist();
//                })
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .setTangent(Math.toRadians(0 * reflect))
//                .splineToConstantHeading(new Vector2d(-45 + Poses.FIELD_OFFSET_X, Poses.CENTER_Y * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .splineToConstantHeading(new Vector2d(30, Poses.CENTER_Y * reflect), Math.toRadians(0))
//                // lift
//                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.YELLOW_POS);
//                })
//                // wrist back
//                .UNSTABLE_addTemporalMarkerOffset(0.5 + WRIST_EXTEND_AFTER_LIFT_TIME, () -> {
//                    robot.outtake.extendWrist();
//                })
//                // turn turret
//                .UNSTABLE_addTemporalMarkerOffset(0.5 + WRIST_EXTEND_AFTER_LIFT_TIME + TURRET_TURN_AFTER_WRIST_TIME, () -> {
//                    robot.outtake.setTurretAngle(270 + 50 * reflect);
//                })
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_FAR_Y * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_FAR_POSE.vec(), Math.toRadians(0))
//                // release white pixel
//                .UNSTABLE_addTemporalMarkerOffset(RELEASE_TIME, () -> {
//                    robot.outtake.lockBack();
//                })
//                // turn turret
//                .UNSTABLE_addTemporalMarkerOffset(0.3, () -> {
//                    robot.outtake.setTurretAngle(270 - 20 * reflect);
//                })
//                // release yellow pixel
//                .UNSTABLE_addTemporalMarkerOffset(0.8, () -> {
//                    robot.outtake.unlock();
//                })
//                .UNSTABLE_addTemporalMarkerOffset(1.2, () -> {
//                    robot.outtake.lift.setMotionProfileTargetPos(Lift.CLEAR_POS);
//                    robot.outtake.centerTurret();
//                })
//                .waitSeconds(1.3)
//                .build();
//    }
//
//    public TrajectorySequence depositThroughPerimeterFromWingClose(Robot robot) {
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.WING_PLACEMENT_CLOSE_FOR_PERIM_POSE)
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .setTangent(Math.toRadians(180 * reflect))
//                .splineToSplineHeading(new Pose2d(Poses.STACK_SETUP_X, -36*reflect, Math.toRadians(180)), Math.toRadians(180))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                // drop down, start intake, unlock
//
//                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -36 * reflect), Math.toRadians(180))
//                .waitSeconds(INTAKE_TIME)
//                // lock and start outtaking
//
//                // stop outtake
//
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .setTangent(Math.toRadians(-45 * reflect))
//                .splineToConstantHeading(new Vector2d(-45 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_CLOSE_Y * reflect), Math.toRadians(0))
//                // lift
//
//                // wrist back
//
//                // turn turret
//
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_CLOSE_POSE.vec(), Math.toRadians(0))
//                // release white pixel
//
//                // turn turret
//
//                // release yellow pixel
//
//                .waitSeconds(1.3)
//                .build();
//    }
//
//    public TrajectorySequence depositThroughPerimeterFromWingCenter(Robot robot) {
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.WING_PLACEMENT_CENTER_POSE)
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .setTangent(Math.toRadians(225 * reflect))
//                .splineToConstantHeading(new Vector2d(Poses.STACK_SETUP_X, -24 * reflect), Math.toRadians(180))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                // drop down, start intake, unlock
//
//                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -24 * reflect), Math.toRadians(180))
//                .waitSeconds(INTAKE_TIME)
//                // lock and start outtaking
//
//                // stop outtake
//
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .setTangent(Math.toRadians(-45 * reflect))
//                .splineToConstantHeading(new Vector2d(-45 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, Poses.DEPOSIT_CLOSE_Y * reflect), Math.toRadians(0))
//                // lift
//
//                // wrist back
//
//                // turn turret
//
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_CLOSE_POSE.vec(), Math.toRadians(0))
//                // release white pixel
//
//                // turn turret
//
//                // release yellow pixel
//
//                .waitSeconds(TOTAL_CLOSE_DEPOSIT_TIME)
//                .build();
//    }
//
//    public TrajectorySequence depositThroughPerimeterFromWingFar(Robot robot) {
//        return robot.drivetrain.trajectorySequenceBuilder(Poses.WING_PLACEMENT_FAR_FOR_PERIM_POSE)
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .setTangent(Math.toRadians(180 * reflect))
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                // drop down, start intake, unlock
//
//                .splineToConstantHeading(new Vector2d(Poses.STACK_X, -36 * reflect), Math.toRadians(180))
//                .waitSeconds(INTAKE_TIME)
//                // lock and start outtaking
//
//                // stop outtake
//
//                .setConstraints(Constraints.FAST_VEL, Constraints.FAST_ACCEL)
//                .setTangent(Math.toRadians(-45 * reflect))
//                .splineToConstantHeading(new Vector2d(-45 + Poses.FIELD_OFFSET_X, -60 * reflect), Math.toRadians(0))
//                .setConstraints(Constraints.NORMAL_VEL, Constraints.NORMAL_ACCEL)
//                .splineToConstantHeading(new Vector2d(30, -60 * reflect), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(Poses.BACKDROP_SETUP_X, -36 * reflect), Math.toRadians(0))
//                // lift
//
//                // wrist back
//
//                // turn turret
//
//                .setConstraints(Constraints.SLOW_VEL, Constraints.SLOW_ACCEL)
//                .splineToConstantHeading(Poses.DEPOSIT_CENTER_POSE.vec(), Math.toRadians(0))
//                // release white pixel
//
//                // turn turret
//
//                // release yellow pixel
//
//                .waitSeconds(1.3)
//                .build();
    }
}
