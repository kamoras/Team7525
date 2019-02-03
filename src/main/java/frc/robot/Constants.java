/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * These are all constants defined for the robot.
 * All should be defined as static and final.
 * Please add a note to each constant to explain what it is for. 
 * Also add relevant info such as units.
 */
public class Constants {
    // Timeout in ms for drive motors
    public static final int kTimeoutMs = 30;

    // P constant for drive motors PID
    public static final double kP = 0.05;
    // I constant for drive motors PID
    public static final double kI = 0.0;
    // D constant for drive motors PID
    public static final double kD = 0.0;
    // F constant for drive motors PID
    public static final double kF = 0.03103;

    // Speed dampener for DriveTrain while driving
    public static final int speedDampener = 1;
}
