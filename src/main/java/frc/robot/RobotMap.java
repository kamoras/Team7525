/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;
	

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;

  // Controllers
  // based on USB port
  public static int operatorStickPort = 0; 
  public static int driverStickPort = 1;

  // DriveTrain motors
  public static int leftMasterPort = 0;
  public static int rightMasterPort = 1;
  public static int leftSlavePort = 2;
  public static int rightSlavePort = 3;

  // PCM Ports
  public static int compressorPort = 0; // PCM port for the compressor - should always be zero, why would we have more than one?
  public static int gearShiftPort = 0; // PCM port to shift gears (single solenoid)
}
