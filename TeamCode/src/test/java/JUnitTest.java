import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.util.Angle;

import org.firstinspires.ftc.robotcore.internal.system.Assert;

import org.firstinspires.ftc.teamcode.blucru.common.subsystems.drivetrain.localization.PoseHistory;
import org.firstinspires.ftc.teamcode.blucru.common.subsystems.drivetrain.localization.PoseMarker;
import org.junit.*;

public class JUnitTest {
    @Test
    public void testCar() {
        Assert.assertEquals(1, 0);
    }

    @Test
    public void testPoseHistory() {
        PoseHistory poseHistory = new PoseHistory();
        poseHistory.add(new Pose2d(0, 0, 0));
        long time1 = System.nanoTime();

        poseHistory.add(new Pose2d(1, 1, 1));
        long time2 = System.nanoTime();

        poseHistory.add(new Pose2d(2, 2, 2));
        long time3 = System.nanoTime();

        poseHistory.offset(new Pose2d(1, 1, 1));

        Assert.assertTrue(poseHistory.getPoseAtTime(time2).equals(new Pose2d(2, 2, 1)));
    }

    @Test
    public void testAsymmetricDecel() {
        double tangentialDecel = 1;
        double strafeDecel = 3;
        double headingDecel = 10;

        Pose2d startPose = new Pose2d(0, 0, 0);
        Pose2d startVelocity = new Pose2d(4, 4, 3);


    }

    public Pose2d getAssymetricStopPose(Pose2d startPose, Pose2d startVelocity, double tangentialDecel, double strafeDecel, double headingDecel) {
        // vf^2 = vi^2 + 2ad
        // d = (vf^2 - vi^2) / 2a
        // d = vi^2 / 2a
        Pose2d robotVel = new Pose2d(startVelocity.vec().rotated(-startPose.getHeading()), startVelocity.getHeading());
        double strafeDist = Math.signum(robotVel.getY()) * robotVel.vec().getY() * robotVel.vec().getY() / (2 * strafeDecel);
        double forwardDist = Math.signum(robotVel.getX()) * robotVel.vec().getX() * robotVel.vec().getX() / (2 * tangentialDecel);
        double headingDist = Math.signum(robotVel.getHeading()) * robotVel.getHeading() * robotVel.getHeading() / (2 * headingDecel);

        Vector2d globalDist = new Vector2d(forwardDist, strafeDist).rotated(startPose.getHeading());
        
        Vector2d finalVec = startPose.vec().plus(globalDist);
        double finalHeading = Angle.norm(startPose.getHeading() + headingDist);
        return new Pose2d(finalVec, finalHeading);
    }
}
