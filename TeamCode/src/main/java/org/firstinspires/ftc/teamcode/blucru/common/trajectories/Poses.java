package org.firstinspires.ftc.teamcode.blucru.common.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.blucru.common.states.Alliance;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.intake.Dropdown;

// poses for trajectories
public final class Poses {
    public static double FIELD_OFFSET_X = 0;
    /*
    offset of distance from backdrop to other side
    APPLY ONLY TO WING SIDE COORDINATES
    HIGHER = CLOSER
    */
    public static double START_Y = -61;
    public static double DEPOSIT_X = 51;
    public static double BACKDROP_SETUP_X = 44;
    public static double BACKDROP_Y_DELTA = 3.5;

    public static double DEPOSIT_FAR_Y = -36 + BACKDROP_Y_DELTA;
    public static double DEPOSIT_CLOSE_Y = -36 - BACKDROP_Y_DELTA;

    public static double CENTER_Y = -10;

    public static double STACK_X = -57.5 + FIELD_OFFSET_X;
    public static double STACK_SETUP_X = -55.5 + FIELD_OFFSET_X;

    public static Pose2d BACKDROP_STARTING_POSE;
    public static Pose2d AUDIENCE_STARTING_POSE;

    public static Pose2d BACKDROP_PLACEMENT_FAR_POSE;
    public static Pose2d BACKDROP_PLACEMENT_CLOSE_POSE;
    public static Pose2d BACKDROP_PLACEMENT_CENTER_POSE;

    public static Pose2d WING_PLACEMENT_FAR_FOR_PERIM_POSE;
    public static Pose2d WING_PLACEMENT_FAR_FOR_CENTER_POSE;
    public static Pose2d WING_PLACEMENT_CLOSE_FOR_PERIM_POSE;
    public static Pose2d WING_PLACEMENT_CLOSE_FOR_CENTER_POSE;
    public static Pose2d WING_PLACEMENT_CENTER_POSE;

    public static Pose2d STACK_CLOSE_POSE;

    public static Pose2d DEPOSIT_FAR_POSE;
    public static Pose2d DEPOSIT_CENTER_POSE;
    public static Pose2d DEPOSIT_CLOSE_POSE;

    public static Pose2d PARK_CLOSE_POSE;
    public static Pose2d PARK_FAR_POSE;

    public static double reflect = 1;

    public static void setAlliance(Alliance alliance) {
        if(alliance == Alliance.RED) {
            FIELD_OFFSET_X = 0;
            reflect = 1;
        } else {
            FIELD_OFFSET_X = 0;
            reflect = -1;
        }

        BACKDROP_STARTING_POSE = new Pose2d(12, START_Y * reflect, Math.toRadians(-90 * reflect));
        BACKDROP_PLACEMENT_FAR_POSE = new Pose2d(5, -39 * reflect, Math.toRadians(-45 * reflect));
        BACKDROP_PLACEMENT_CLOSE_POSE = new Pose2d(23.5, -35 * reflect, Math.toRadians(-90 * reflect));
        BACKDROP_PLACEMENT_CENTER_POSE = new Pose2d(15, -31 * reflect, Math.toRadians(-90 * reflect));

        AUDIENCE_STARTING_POSE = new Pose2d(-36 + FIELD_OFFSET_X, START_Y * reflect, Math.toRadians(-90 * reflect));
        WING_PLACEMENT_FAR_FOR_PERIM_POSE = new Pose2d(-55 + FIELD_OFFSET_X, -36 * reflect, Math.toRadians(180));
        WING_PLACEMENT_FAR_FOR_CENTER_POSE = new Pose2d(-52 + FIELD_OFFSET_X, -23 * reflect, Math.toRadians(140 * reflect));
        WING_PLACEMENT_CLOSE_FOR_CENTER_POSE = new Pose2d(-31 + FIELD_OFFSET_X, -34 * reflect, Math.toRadians(180));
        WING_PLACEMENT_CLOSE_FOR_PERIM_POSE = new Pose2d(-30 + FIELD_OFFSET_X, -40 * reflect, Math.toRadians(225 * reflect));
        WING_PLACEMENT_CENTER_POSE = new Pose2d(-48 + FIELD_OFFSET_X, -24.5 * reflect, Math.toRadians(180));

        DEPOSIT_FAR_POSE = new Pose2d(DEPOSIT_X, DEPOSIT_FAR_Y * reflect, Math.toRadians(180));
        DEPOSIT_CENTER_POSE = new Pose2d(DEPOSIT_X, -36 * reflect, Math.toRadians(180));
        DEPOSIT_CLOSE_POSE = new Pose2d(DEPOSIT_X, DEPOSIT_CLOSE_Y * reflect, Math.toRadians(180));

        PARK_CLOSE_POSE = new Pose2d(44, -63 * reflect, Math.toRadians(180));
        PARK_FAR_POSE = new Pose2d(44, -7 * reflect, Math.toRadians(180));

        STACK_CLOSE_POSE = new Pose2d(STACK_X + FIELD_OFFSET_X + 1, -42 * reflect, Math.toRadians(150 * reflect));
    }

//    public static double calculateStackX(int stackHeight) {
//        return Poses.STACK_X - Dropdown.RADIUS + Dropdown.toX(stackHeight);
//    }
}
