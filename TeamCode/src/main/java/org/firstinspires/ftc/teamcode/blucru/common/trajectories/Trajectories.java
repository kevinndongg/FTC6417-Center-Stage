package org.firstinspires.ftc.teamcode.blucru.common.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.blucru.common.states.Alliance;
import org.firstinspires.ftc.teamcode.blucru.common.states.AutoType;
import org.firstinspires.ftc.teamcode.blucru.common.states.ParkType;
import org.firstinspires.ftc.teamcode.blucru.common.states.Side;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.Robot;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;

public class Trajectories {
    static double reflect = 1;

    ArrayList<TrajectorySequence>[] trajectories = new ArrayList[3];

    ArrayList<TrajectorySequence> trajectoriesFar;
    ArrayList<TrajectorySequence> trajectoriesCenter;
    ArrayList<TrajectorySequence> trajectoriesClose;

    Side side;
    Placements placements;
    PreloadDeposits preloadDeposits;
    Cycles cycles;
    Poses poses;
    Parks parks;

    AutoType autoType;
    ParkType parkType;

    Pose2d cycleEndPose;

    public Trajectories(Alliance alliance, Side side, AutoType autoType, ParkType parkType) {
        trajectoriesFar = new ArrayList<TrajectorySequence>();
        trajectoriesCenter = new ArrayList<TrajectorySequence>();
        trajectoriesClose = new ArrayList<TrajectorySequence>();

        this.side = side;
        this.autoType = autoType;
        this.parkType = parkType;

        if(alliance == Alliance.RED) {
            reflect = 1;
        } else {
            reflect = -1;
        }

        preloadDeposits = new PreloadDeposits(reflect);
        placements = new Placements(reflect);
        cycles = new Cycles(reflect);
        poses = new Poses(reflect);
        parks = new Parks(reflect);
    }

    public ArrayList<TrajectorySequence> buildTrajectoriesFar(Robot robot) {
        trajectoriesFar.clear();

        if(side == Side.CLOSE) {
            // placement and deposit preload
            trajectoriesFar.add(placements.placementBackdropFar(robot));
            trajectoriesFar.add(preloadDeposits.depositFromBackdropFar(robot));

            switch(autoType) {
                case CENTER_CYCLE:
                    trajectoriesFar.add(cycles.cycleCenterFromFar(robot, 3));
                    trajectoriesFar.add(cycles.cycleCenterFromFar(robot, 1));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
                case PERIMETER_CYCLE:
                    trajectoriesFar.add(cycles.cyclePerimeterFromFar(robot, 3));
                    trajectoriesFar.add(cycles.cyclePerimeterFromClose(robot, 1));
                    cycleEndPose = Poses.DEPOSIT_CLOSE_POSE;

                    break;
                default:
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
            }
        } else {
            switch(autoType) {
                case CENTER_CYCLE:
                    trajectoriesFar.add(placements.placementWingFarForCenter(robot));
                    trajectoriesFar.add(preloadDeposits.depositThroughCenterFromWingFar(robot));
                    trajectoriesFar.add(cycles.cycleCenterFromFar(robot, 3));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
                case PERIMETER_CYCLE:
                    trajectoriesFar.add(placements.placementWingFarForPerimeter(robot));
                    trajectoriesFar.add(preloadDeposits.depositThroughPerimeterFromWingFar(robot));
                    trajectoriesFar.add(cycles.cyclePerimeterFromCenter(robot, 2));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
                default:
                    trajectoriesFar.add(placements.placementWingFarForCenter(robot));
                    trajectoriesFar.add(preloadDeposits.depositThroughCenterFromWingFar(robot));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
            }
        }

        switch (parkType) {
            case CENTER:
                trajectoriesFar.add(parks.parkCenter(robot, cycleEndPose));
                break;
            case PERIMETER:
                trajectoriesFar.add(parks.parkClose(robot, cycleEndPose));
                break;
            default:
                trajectoriesFar.add(parks.retract(robot));
                break;
        }

        return trajectoriesFar;
    }

    public ArrayList<TrajectorySequence> buildTrajectoriesCenter(Robot robot) {
        trajectoriesCenter.clear();

        if(side == Side.CLOSE) {
            // placement and deposit preload


            switch(autoType) {
                case CENTER_CYCLE:
                    trajectoriesCenter.add(placements.placementBackdropCenter(robot));
                    trajectoriesCenter.add(preloadDeposits.depositFromBackdropCenter(robot));
                    trajectoriesCenter.add(cycles.cycleCenterFromCenter(robot, 3));
                    trajectoriesCenter.add(cycles.cycleCenterFromFar(robot, 1));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;

                    break;
                case PERIMETER_CYCLE:
                    trajectoriesCenter.add(placements.placementBackdropCenter(robot));
                    trajectoriesCenter.add(preloadDeposits.depositFromBackdropCenter(robot));
                    trajectoriesCenter.add(cycles.cyclePerimeterFromCenter(robot, 3));
                    trajectoriesCenter.add(cycles.cyclePerimeterFromCenter(robot, 1));
                    cycleEndPose = Poses.DEPOSIT_CLOSE_POSE;
                    break;
                default:
                    trajectoriesCenter.add(placements.placementBackdropCenter(robot));
                    trajectoriesCenter.add(preloadDeposits.depositFromBackdropCenter(robot));
                    cycleEndPose = Poses.DEPOSIT_CENTER_POSE;
                    break;
            }
        } else {
            switch(autoType) {
                case CENTER_CYCLE:
                    trajectoriesCenter.add(placements.placementWingCenter(robot));
                    trajectoriesCenter.add(preloadDeposits.depositThroughCenterFromWingCenter(robot));
                    trajectoriesCenter.add(cycles.cycleCenterFromFar(robot, 3));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
                case PERIMETER_CYCLE:
                    trajectoriesCenter.add(placements.placementWingCenter(robot));
                    trajectoriesCenter.add(preloadDeposits.depositThroughPerimeterFromWingCenter(robot));
                    trajectoriesCenter.add(cycles.cyclePerimeterFromCenter(robot, 3));
                    cycleEndPose = Poses.DEPOSIT_CLOSE_POSE;
                    break;
                default:
                    trajectoriesCenter.add(placements.placementWingCenter(robot));
                    trajectoriesCenter.add(preloadDeposits.depositThroughCenterFromWingCenter(robot));
                    cycleEndPose = Poses.DEPOSIT_CENTER_POSE;
                    break;
            }
        }

        switch (parkType) {
            case CENTER:
                trajectoriesCenter.add(parks.parkCenter(robot, cycleEndPose));
                break;
            case PERIMETER:
                trajectoriesCenter.add(parks.parkClose(robot, cycleEndPose));
                break;
            default:
                trajectoriesCenter.add(parks.retract(robot));
                break;
        }

        return trajectoriesCenter;
    }

    public ArrayList<TrajectorySequence> buildTrajectoriesClose(Robot robot) {
        trajectoriesClose.clear();

        if(side == Side.CLOSE) {
            // placement and deposit preload
            trajectoriesClose.add(placements.placementBackdropClose(robot));
            trajectoriesClose.add(preloadDeposits.depositFromBackdropClose(robot));

            switch(autoType) {
                case CENTER_CYCLE:
                    trajectoriesClose.add(cycles.cycleCenterFromClose(robot, 3));
                    trajectoriesClose.add(cycles.cycleCenterFromFar(robot, 1));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
                case PERIMETER_CYCLE:
                    trajectoriesClose.add(cycles.cyclePerimeterFromClose(robot, 3));
                    trajectoriesClose.add(cycles.cyclePerimeterFromClose(robot, 1));
                    cycleEndPose = Poses.DEPOSIT_CLOSE_POSE;
                    break;
                default:
                    cycleEndPose = Poses.DEPOSIT_CLOSE_POSE;
                    break;
            }
        } else {
            switch(autoType) {
                case CENTER_CYCLE:
                    trajectoriesClose.add(placements.placementWingCloseForCenter(robot));
                    trajectoriesClose.add(preloadDeposits.depositThroughCenterFromWingClose(robot));
                    trajectoriesClose.add(cycles.cycleCenterFromFar(robot, 2));
                    cycleEndPose = Poses.DEPOSIT_FAR_POSE;
                    break;
                case PERIMETER_CYCLE:
                    trajectoriesClose.add(placements.placementWingCloseForPerimeter(robot));
                    trajectoriesClose.add(preloadDeposits.depositThroughPerimeterFromWingClose(robot));
                    trajectoriesClose.add(cycles.cyclePerimeterFromClose(robot, 2));
                    cycleEndPose = Poses.DEPOSIT_CLOSE_POSE;
                    break;
                default:
                    trajectoriesClose.add(placements.placementWingCloseForPerimeter(robot));
                    trajectoriesClose.add(preloadDeposits.depositThroughPerimeterFromWingClose(robot));
                    cycleEndPose = Poses.DEPOSIT_CLOSE_POSE;
                    break;
            }
        }

        switch (parkType) {
            case CENTER:
                trajectoriesClose.add(parks.parkCenter(robot, cycleEndPose));
                break;
            case PERIMETER:
                trajectoriesClose.add(parks.parkClose(robot, cycleEndPose));
                break;
            default:
                trajectoriesClose.add(parks.retract(robot));
                break;
        }

        return trajectoriesClose;
    }

    public Pose2d getStartPose() {
        switch (side) {
            case CLOSE:
                return Poses.BACKDROP_STARTING_POSE;
            case FAR:
                return Poses.WING_STARTING_POSE;
        }
        return null;
    }
}