package org.firstinspires.ftc.teamcode.blucru.common.trajectories;

import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// Constraints for trajectories
public class Constraints {
    public static TrajectoryVelocityConstraint FAST_VEL = SampleMecanumDrive.getVelocityConstraint(44, Math.toRadians(220), DriveConstants.TRACK_WIDTH);
    public static TrajectoryVelocityConstraint NORMAL_VEL = SampleMecanumDrive.getVelocityConstraint(41, Math.toRadians(180), DriveConstants.TRACK_WIDTH);
    public static TrajectoryVelocityConstraint SLOW_VEL = SampleMecanumDrive.getVelocityConstraint(35, Math.toRadians(150), DriveConstants.TRACK_WIDTH);

    public static TrajectoryAccelerationConstraint FAST_ACCEL = SampleMecanumDrive.getAccelerationConstraint(40);
    public static TrajectoryAccelerationConstraint NORMAL_ACCEL = SampleMecanumDrive.getAccelerationConstraint(40);
    public static TrajectoryAccelerationConstraint SLOW_ACCEL = SampleMecanumDrive.getAccelerationConstraint(35);

    public static TrajectoryVelocityConstraint[] velos = {SLOW_VEL, NORMAL_VEL, FAST_VEL};
    public static TrajectoryAccelerationConstraint[] accels = {SLOW_ACCEL, NORMAL_ACCEL, FAST_ACCEL};
}