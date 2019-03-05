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
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.Constants.armDirection;
import frc.robot.Constants.armPosition;

/**
 * Add your docs here.
 */
public class Arm extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  TalonSRX motor;

  DigitalInput limitSwitch;
  Counter counter;

  armDirection direction;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public boolean initMotors() {
    ErrorCode error = ErrorCode.OK;

    // define master drive (SRX) Talons
    motor = new TalonSRX(RobotMap.liftMaster); 
   
    // Define limit switch
    limitSwitch = new DigitalInput(1); // TODO verify channel
    counter = new Counter(limitSwitch);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be
     * neutral within this range. See Table here for units to use: 
     * https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
     */
    error = motor.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);

    /* Config closed loop gains for Primary closed loop (Current) */
    // TO DO: Check error codes
    motor.config_kP(0, Constants.aP, Constants.kTimeoutMs);
    motor.config_kI(0, Constants.aI, Constants.kTimeoutMs);
    motor.config_kD(0, Constants.aD, Constants.kTimeoutMs);
    motor.config_kF(0, Constants.aF, Constants.kTimeoutMs);

    motor.configNominalOutputForward(0,Constants.kTimeoutMs);
    motor.configNominalOutputReverse(0,Constants.kTimeoutMs);
    motor.configPeakOutputForward(1,Constants.kTimeoutMs);
    motor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    // Initalizes encoders
    // TO DO: Check error codes and add soft limits
    motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimeoutMs);

    // Ensures motor output and encoder velocity are prorightional to each other
    // If they become inverted, set these to true
    motor.setSensorPhase(false);
    motor.setInverted(false);

    // Set relevant frame periods to be at least as fast as periodic rate
    motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);

    // Zeroes encoders
    // TO DO: Check error codes
    motor.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);

    direction = armDirection.NONE;

    return (error == ErrorCode.OK);
  }

  public boolean isSwitchSet() {
    return counter.get() > 0;
  }

  public void initializeCounter() {
    counter.reset();
    direction = armDirection.NONE;
  }

  public void moveUp() {
    motor.set(ControlMode.PercentOutput, 0.5);
    direction = armDirection.ARM_UP;
  }

  public void moveDown() {
    motor.set(ControlMode.PercentOutput, -0.5);
    direction = armDirection.ARM_DOWN;
  }

  public void stop() {
    motor.set(ControlMode.PercentOutput, 0.0);
    direction = armDirection.NONE;
  }

  public int getPosition() {
    return motor.getSelectedSensorPosition();
  }

  private int getIntFromPosition(armPosition position) {
    int desiredPosition = 0;
    switch(position) 
    {
      case HATCH_OUT:
        desiredPosition = Constants.armHatchOut;
        break;
      case BALL_OUT:
        desiredPosition = Constants.armBallOut;
        break;
      case HATCH_IN:
        desiredPosition = Constants.armHatchIn;
        break;
      case BALL_IN:
        desiredPosition = Constants.armBallIn;
        break;
      case HOME:
      default:
        desiredPosition = Constants.armHome;
        break;
    }
    return desiredPosition; 
  }

  public boolean isAtPosition(armPosition position) {
    if (direction == armDirection.ARM_UP) {
      return getPosition() >= getIntFromPosition(position);
    }
    else if (direction == armDirection.ARM_DOWN) {
      return getPosition() <= getIntFromPosition(position);
    }
    else {
      return getPosition() == getIntFromPosition(position);
    }
  }

  public void moveToPosition(armPosition position) {
    int desiredPosition = getIntFromPosition(position);
    if (getPosition() < desiredPosition) {
      moveUp();
    }
    else {
      moveDown();
    }
  }
}
