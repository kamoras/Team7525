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

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.Constants.liftDirection;
import frc.robot.Constants.liftPosition;

/**
 * Add your docs here.
 */
public class Lift extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  TalonSRX master;
  
  VictorSPX slave1;
  VictorSPX slave2;

  DigitalInput limitSwitch;
  Counter counter;

  liftDirection direction;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public boolean initMotors() {
    ErrorCode error = ErrorCode.OK;

    // define master drive (SRX) Talons
    master = new TalonSRX(RobotMap.liftMaster); // master
    // Define slave drive (SPX) Victors
    slave1 = new VictorSPX(RobotMap.liftSlave1); // slave 1
    slave2 = new VictorSPX(RobotMap.liftSlave2); // slave 2

    // Define limit switch
    limitSwitch = new DigitalInput(1); // TODO verify channel
    counter = new Counter(limitSwitch);

    // Set drive slaves to follow drive masters
    slave1.follow(master);
    slave2.follow(master);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be
     * neutral within this range. See Table here for units to use: 
     * https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
     */
    error = master.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);

    /* Config closed loop gains for Primary closed loop (Current) */
    // TODO: Check error codes
    master.config_kP(0, Constants.lP, Constants.kTimeoutMs);
    master.config_kI(0, Constants.lI, Constants.kTimeoutMs);
    master.config_kD(0, Constants.lD, Constants.kTimeoutMs);
    master.config_kF(0, Constants.lF, Constants.kTimeoutMs);

    // Initalizes encoders
    // TODO: Check error codes
    master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimeoutMs);

    // Ensures motor output and encoder velocity are prorightional to each other
    // If they become inverted, set these to true
    master.setSensorPhase(false);

    // Zeroes encoders
    // TODO: Check error codes
    master.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);

    direction = liftDirection.NONE;

    return (error == ErrorCode.OK);
  }

  public boolean isSwitchSet() {
    return counter.get() > 0;
  }

  public void moveUp() {
    master.set(ControlMode.PercentOutput, 0.5);
    direction = liftDirection.UP;
  }

  public void moveDown() {
    master.set(ControlMode.PercentOutput, -0.5);
    direction = liftDirection.DOWN;
  }

  public void initializeCounter() {
    counter.reset();
    direction = liftDirection.NONE;
  }

  public void stop() {
    master.set(ControlMode.PercentOutput, 0.0);
    direction = liftDirection.NONE;
  }

  public boolean atTop() {
    return getPosition() > Constants.lift_top;
  }

  public int getPosition() {
    return master.getSelectedSensorPosition();
  }

  public liftDirection getDirection() {
    return direction;
  }

  public boolean hasReachedPosition(liftPosition position) {
    if (getDirection() == liftDirection.UP)
    {
      return getPosition() >= getIntFromPosition(position);
    }
    else if (getDirection() == liftDirection.DOWN) {
      return getPosition() <= getIntFromPosition(position);
    }
    else {
      return getPosition() == getIntFromPosition(position);
    }
  }

  public void moveToPosition(liftPosition position) {
    int desiredPosition = getIntFromPosition(position);
    if(getPosition() < desiredPosition) {
      moveUp();
    }
    else {
      moveDown();
    }
  }

  private int getIntFromPosition(liftPosition position) {
    int desiredPosition = 0;
    switch(position)
    {
      case ROCKET_LOW:
        desiredPosition = Constants.rocket_low;
        break;
      case ROCKET_MIDDLE:
        desiredPosition = Constants.rocket_middle;
        break;
      case ROCKET_HIGH:
        desiredPosition = Constants.rocket_high;
        break;
      case CARGO_ROCKET_LOW:
        desiredPosition = Constants.cargo_rocket_low;
        break;
      case CARGO_ROCKET_HIGH:
        desiredPosition = Constants.cargo_rocket_high;
        break;
      case CARGO_SHIP_LOW:
        desiredPosition = Constants.cargo_low;
        break;
      case CARGO_SHIP_HIGH:
        desiredPosition = Constants.cargo_high;
        break;
      case DEFAULT:
      default:
        desiredPosition = Constants.lift_default;
    }

    return desiredPosition;
  }
}
