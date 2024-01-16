package org.firstinspires.ftc.teamcode.blucru.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.blucru.Constants;
import org.firstinspires.ftc.teamcode.blucru.states.Alliance;
import org.firstinspires.ftc.teamcode.blucru.states.AutoState;
import org.firstinspires.ftc.teamcode.blucru.states.Side;
import org.firstinspires.ftc.teamcode.blucru.subsystems.Robot;
import org.firstinspires.ftc.teamcode.blucru.trajectories.Trajectories;
import org.firstinspires.ftc.teamcode.blucru.vision.CVMaster;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Blue Right PARK Auto", group = "BluCru")
public class BlueRightParkAuto extends LinearOpMode {
    Robot robot;
    Trajectories trajectories;
    CVMaster cvMaster;
    AutoState autoState;
    ElapsedTime runtime;

    int position;

    TrajectorySequence placementFar;
    TrajectorySequence placementClose;
    TrajectorySequence placementCenter;

    TrajectorySequence parkFar;
    TrajectorySequence parkCenter;
    TrajectorySequence parkClose;

    TrajectorySequence placement;
    TrajectorySequence park;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(telemetry, hardwareMap);
        trajectories = new Trajectories(Alliance.BLUE, Side.FAR);
        cvMaster = new CVMaster(hardwareMap, Alliance.BLUE);
        autoState = AutoState.PLACEMENT;
        runtime = new ElapsedTime();

        robot.init();

        telemetry.addData("robot init", "complete");
        telemetry.update();

        placementFar = trajectories.placementFar(robot);
        placementClose = trajectories.placementClose(robot);
        placementCenter = trajectories.placementCenter(robot);

        parkFar = trajectories.parkFarFromFar(robot);
        parkCenter = trajectories.parkCenterFromFar(robot);
        parkClose = trajectories.parkCloseFromFar(robot);

        cvMaster.detectProp();

        while(!isStopRequested() && opModeInInit()) {
            position = cvMaster.pipeline.position;

            telemetry.addData("build trajectories", "complete");
            telemetry.addData("average0", cvMaster.pipeline.average0);
            telemetry.addData("average1", cvMaster.pipeline.average1);
            telemetry.addData("average2", cvMaster.pipeline.average2);
            telemetry.addData("position", position);
            telemetry.update();
        }

        switch(position) {
            case 0:
                placement = placementClose;
                park = parkClose;
                break;
            case 1:
                placement = placementCenter;
                park = parkCenter;
                break;
            case 2:
                placement = placementFar;
                park = parkFar;
                break;
        }

        waitForStart();

        sleep((long) Constants.farAutoDelay);

        robot.drivetrain.setPoseEstimate(trajectories.getStartPose());
        cvMaster.stopCamera();
        runtime.reset();

        robot.drivetrain.followTrajectorySequenceAsync(placement);

        while(!isStopRequested() && opModeIsActive()) {
            switch(autoState) {
                case PLACEMENT:
                    if(!robot.drivetrain.isBusy()) {
                        robot.drivetrain.followTrajectorySequenceAsync(park);
                        autoState = AutoState.PARK;
                    }
                    break;
                case PARK:
                    if(!robot.drivetrain.isBusy()) {
                        // auto is finished
                    }
                    break;
            }

            robot.write();
            robot.drivetrain.updateTrajectory();

            telemetry.addData("poseEstimate: ", robot.drivetrain.getPoseEstimate());
            telemetry.addData("path:", autoState);
            telemetry.addData("runtime:", runtime.seconds());
            telemetry.update();
        }
    }
}