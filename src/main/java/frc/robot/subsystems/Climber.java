/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.Constants.climberDirection;

/**
 * Add your docs here.
 */
public class Climber extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  VictorSPX master;
  VictorSPX slave;

  DigitalInput legSwitch;
  DigitalInput homeSwitch;
  DigitalInput endSwitch;

  Counter legCounter;
  Counter homeCounter;
  Counter endCounter;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public boolean initMotors() {
    ErrorCode error = ErrorCode.OK;

    // Define slave drive (SPX) Victors
    master = new VictorSPX(RobotMap.climberMaster); 
    slave = new VictorSPX(RobotMap.climberSlave);

    slave.follow(master);

    legSwitch = new DigitalInput(1);
    homeSwitch = new DigitalInput(2);
    endSwitch = new DigitalInput(3);

    legCounter = new Counter(legSwitch);
    homeCounter = new Counter(homeSwitch);
    endCounter = new Counter(endSwitch);

    return (error == ErrorCode.OK);
  }

  public void climb(climberDirection direction) {
    switch (direction) {
      case IN:
        climbIn();
        break;
      case OUT:
        climbOut();
        break;
    }
  }

  public void climbOut() {
    master.set(ControlMode.PercentOutput, 1.0);
  }

  public void climbIn() {
    master.set(ControlMode.PercentOutput, -1.0);
  }

  public void stop() {
    master.set(ControlMode.PercentOutput, 0.0);
  }

  public boolean hitDestination(climberDirection direction) {
    switch(direction) {
      case IN:
        return hitHome();
      case OUT:
        return hitEnd();
      default:
        return true;
    }
  }

  public boolean hitHome() {
    return homeCounter.get() > 0;
  }

  public boolean hitEnd() {
    return endCounter.get() > 0;
  }

  public void initializeCounters() {
    homeCounter.reset();
    legCounter.reset();
    endCounter.reset();
  }

}
