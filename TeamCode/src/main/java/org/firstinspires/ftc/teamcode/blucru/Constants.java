package org.firstinspires.ftc.teamcode.blucru;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Constants {
    public static double sens                     = 0.1;
    public static double triggerSens              = 0.5;

// drive variables
    public static double driveSpeedIntake         = 0.5;
    public static double driveSpeedRetract = 0.75;
    public static double driveSpeedLifting     = 0.6;
    public static double driveSpeedOuttake        = 0.4;
    public static double driveSpeedEject          = 0.4;

// slider variables
    public static int sliderRetractPos = 0;
    public static int sliderIntakePos             = 50;
    public static int sliderLowPos                = 1500;
    public static int sliderMedPos                = 1800;
    public static int sliderHighPos               = 2100;
    public static int sliderIntakeDelta           = 50;
    public static int sliderMaxPos                = 2100;
    public static int sliderMinPos                = 0;
    public static int sliderWristClearPos         = 1000;
    public static int sliderAutoPos               = 1150;

// hanger motor variables
    public static double hangerP = 0.005;
    public static double hangerI = 0;
    public static double hangerD = 0;
    public static int hangerUpPos = 4000;
    public static int hangerHangPos = 2500;

// outtake servo variables
    public static double outtakeRollersIntakePower = 1;
    public static double outtakeRollersOuttakePower = -1;
    public static double outtakeRollersOuttakeAutoPower = -0.7;

// intake rollers variables
    public static double intakeRollersIntakePower = 0.75;
    public static double intakeRollersOuttakePower = -0.75;

// outtake wrist variables
    public static double outtakeWristRetractPos   = 0.27;
    public static double outtakeWristOuttakePos   = 0.57;
    public static double outtakeWristIntakePos    = 0.30;

// plane variables
    public static double planeRetractPos = 0.7;
    public static double planeLaunchPos = 0.9;

    /* timer variables */
    public static double farAutoDelay             = 10000; // seconds
    public static double slideDownDelay           = 1000;
    public static double slideStallDelay          = 3000; //milliseconds
}