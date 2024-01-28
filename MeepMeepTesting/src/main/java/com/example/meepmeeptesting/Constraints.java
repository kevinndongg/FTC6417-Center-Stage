package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;

import java.util.Arrays;

public class Constraints {
    public static TrajectoryVelocityConstraint FAST_VELOCITY = new MinVelocityConstraint(Arrays.asList(
            new AngularVelocityConstraint(Math.toRadians(200)),
                new MecanumVelocityConstraint(45, 12.6)));

    public static TrajectoryVelocityConstraint NORMAL_VELOCITY = new MinVelocityConstraint(Arrays.asList(
            new AngularVelocityConstraint(Math.toRadians(180)),
            new MecanumVelocityConstraint(30, 12.6)));

    public static TrajectoryVelocityConstraint SLOW_VELOCITY = new MinVelocityConstraint(Arrays.asList(
            new AngularVelocityConstraint(Math.toRadians(150)),
            new MecanumVelocityConstraint(15, 12.6)));

    public static TrajectoryAccelerationConstraint FAST_ACCELERATION =  new ProfileAccelerationConstraint(40);
    public static TrajectoryAccelerationConstraint NORMAL_ACCELERATION =  new ProfileAccelerationConstraint(30);
    public static TrajectoryAccelerationConstraint SLOW_ACCELERATION =  new ProfileAccelerationConstraint(20);

    public static TrajectoryVelocityConstraint[] velos = {SLOW_VELOCITY, NORMAL_VELOCITY, FAST_VELOCITY};
    public static TrajectoryAccelerationConstraint[] accels = {SLOW_ACCELERATION, NORMAL_ACCELERATION, FAST_ACCELERATION};
}
