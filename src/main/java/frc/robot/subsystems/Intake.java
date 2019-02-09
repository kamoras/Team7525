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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.Constants.intakeMode;

/**
 * Add your docs here.
 */
public class Intake extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  VictorSPX topMotor;
  VictorSPX bottomMotor;

  DigitalInput ballSensor;
  DigitalInput hatchSensor1;
  DigitalInput hatchSensor2;

  Counter ballSensorCounter;
  Counter hatchSensorCounter1;
  Counter hatchSensorCounter2;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public boolean initMotors() {
    ErrorCode error = ErrorCode.OK;

    // Define slave drive (SPX) Victors
    topMotor = new VictorSPX(RobotMap.intakeMotor1); 
    bottomMotor = new VictorSPX(RobotMap.intakeMotor2);

    ballSensor = new DigitalInput(1);
    hatchSensor1 = new DigitalInput(2);
    hatchSensor2 = new DigitalInput(3);

    ballSensorCounter = new Counter(ballSensor);
    hatchSensorCounter1 = new Counter(hatchSensor1);
    hatchSensorCounter2 = new Counter(hatchSensor2);

    return (error == ErrorCode.OK);
  }

  public void operateIntake(intakeMode mode)
  {
    switch(mode)
    {
      case BALL_IN:
        ballIn();
        break;
      case BALL_OUT:
        ballOut();
        break;
      case HATCH_IN:
        hatchIn();
        break;
      case HATCH_OUT:
        hatchOut();
        break;
    }
  }

  public boolean isDone(intakeMode mode) {
    if (mode == intakeMode.BALL_IN || mode == intakeMode.BALL_OUT) {
      Timer.delay(0.2);
    }

    if (mode == intakeMode.HATCH_IN || mode == intakeMode.HATCH_OUT) {
      Timer.delay(0.1);
    }

    return true;
  }

  public void stop() {
    topMotor.set(ControlMode.PercentOutput, 0.0);
    bottomMotor.set(ControlMode.PercentOutput, 0.0);
  }

  public boolean hasBall() {
    return ballSensorCounter.get() > 0;
  }

  public boolean hasHatch() {
    return hatchSensorCounter1.get() > 0 || hatchSensorCounter2.get() > 0;
  }

  public void ballIn() {
    topMotor.set(ControlMode.PercentOutput, 1.0);
    bottomMotor.set(ControlMode.PercentOutput, -1.0);
  }

  public void ballOut() {
    topMotor.set(ControlMode.PercentOutput, -1.0);
    bottomMotor.set(ControlMode.PercentOutput, 1.0);
  }

  public void hatchIn() {
    bottomMotor.set(ControlMode.PercentOutput, 1.0);
  }

  public void hatchOut() {
    bottomMotor.set(ControlMode.PercentOutput, -1.0);
  }
}
