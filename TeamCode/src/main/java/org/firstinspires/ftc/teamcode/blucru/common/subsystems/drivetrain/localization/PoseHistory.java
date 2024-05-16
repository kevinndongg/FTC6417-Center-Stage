package org.firstinspires.ftc.teamcode.blucru.common.subsystems.drivetrain.localization;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import java.util.LinkedList;
import java.util.ListIterator;

public class PoseHistory {
    static double STORAGE_NANOSECONDS = 1.0 * Math.pow(10.0, 9.0);

    static class PoseMarker {
        long nanoTime;
        Pose2d pose;

        PoseMarker(Pose2d pose) {
            nanoTime = System.nanoTime();
            this.pose = pose;
        }
    }

    LinkedList<PoseMarker> poseList;

    /*
        Linked list of poses and their timestamps
        New poses are stored at the front of the linked list
        Timestamps are in nanoseconds

        The reason for using a linked list is because we need
        to add and remove elements from the front
        and back of the list, which is faster with a
        linked list than an arraylist
     */

    public PoseHistory() {
        // initialize pose history
        poseList = new LinkedList<>();
    }

    public void add(Pose2d pose) {
        poseList.addFirst(new PoseMarker(pose)); // add current pose to front of list

        long currentTime = System.nanoTime();

        // remove old poses
        while (poseList.size() > 0 && currentTime - poseList.getLast().nanoTime > STORAGE_NANOSECONDS) {
            poseList.removeLast(); // remove oldest pose from back of list until we have less than STORAGE_NANOSECONDS of poses
        }
    }

    public Pose2d getPoseAtTime(long targetNanoTime) {
        ListIterator<PoseMarker> iterator = poseList.listIterator();
        PoseMarker poseMarker = iterator.next();

        while(iterator.hasNext()) {
            if (poseMarker.nanoTime < targetNanoTime) {
                Log.i("PoseHistory", "found: " + poseMarker.pose);
                return poseMarker.pose;
            }
            Log.v("PoseHistory", "iterated: " + poseMarker.pose);
            poseMarker = iterator.next();
        }

        Log.e("PoseHistory", "No pose found at time " + targetNanoTime);
        return null;
    }

    public void offset(Pose2d poseDelta) {
        for (PoseMarker marker : poseList) {
            marker.pose = new Pose2d(marker.pose.vec().plus(poseDelta.vec()), marker.pose.getHeading());
        }
    }
}
