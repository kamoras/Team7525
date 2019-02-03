/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.commands.MoveRobot;

/**
 * This represents the drivetrain of the robot.
 * The robot has 2 Talon SRX motors each with a slave Victor SPX.
 */
public class DriveTrain extends Subsystem {
  TalonSRX leftMaster;
  TalonSRX rightMaster;

  VictorSPX leftSlave;
  VictorSPX rightSlave;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());

    // Default is to manually drive robot with sticks
    setDefaultCommand(new MoveRobot());
  }

  public boolean initMotors() {
    ErrorCode error = ErrorCode.OK;

    // define master drive (SRX) Talons
    leftMaster = new TalonSRX(RobotMap.leftMasterPort); // right master
    rightMaster = new TalonSRX(RobotMap.rightMasterPort); // left master
    // Define slave drive (SPX) Victors
    leftSlave = new VictorSPX(RobotMap.leftSlavePort); // left slave 1
    rightSlave = new VictorSPX(RobotMap.rightSlavePort); // right slave 1

    // Set drive slaves to follow drive masters
    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be
     * neutral within this range. See Table here for units to use: 
     * https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
     */
    error = leftMaster.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
    if (error == ErrorCode.OK){
      error = rightMaster.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
    }

    /* Config closed loop gains for Primary closed loop (Current) */
    // TODO: Check error codes
    leftMaster.config_kP(0, Constants.kP, Constants.kTimeoutMs);
    leftMaster.config_kI(0, Constants.kI, Constants.kTimeoutMs);
    leftMaster.config_kD(0, Constants.kD, Constants.kTimeoutMs);
    leftMaster.config_kF(0, Constants.kF, Constants.kTimeoutMs);
    
    rightMaster.config_kP(0, Constants.kP, Constants.kTimeoutMs);
    rightMaster.config_kI(0, Constants.kI, Constants.kTimeoutMs);
    rightMaster.config_kD(0, Constants.kD, Constants.kTimeoutMs);
    rightMaster.config_kF(0, Constants.kF, Constants.kTimeoutMs);

    // Initalizes encoders
    // TODO: Check error codes
    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimeoutMs);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimeoutMs);

    // Ensures motor output and encoder velocity are prorightional to each other
    // If they become inverted, set these to true
    leftMaster.setSensorPhase(false);
    rightMaster.setSensorPhase(false);

    // Zeroes encoders
    // TODO: Check error codes
    leftMaster.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);
    rightMaster.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);

    return (error == ErrorCode.OK);
  }

  /** Drive using two talons and a joystick **/
  public void stickDrive(Joystick driverStick, Solenoid gearShift){
    double thro = driverStick.getRawAxis(1); //Populate the double thro with the raw axis 1
    double yaw = driverStick.getRawAxis(2); //Populate the double yaw with the raw axis 2

    if(gearShift.get()){ // If bot is in high gear
      leftMaster.set(ControlMode.PercentOutput, (Constants.speedDampener * -1 * thro) - yaw); // Subtract the steerage for arcade drive
      rightMaster.set(ControlMode.PercentOutput, (Constants.speedDampener * thro) - yaw); // Subtract the steerage for arcade drive, reverse
    }else{ // If bot is in low gear
      leftMaster.set(ControlMode.PercentOutput, (-1 * thro) - yaw); // Drive normally
      rightMaster.set(ControlMode.PercentOutput, thro - yaw); // Drive normally
    }
  }

}
