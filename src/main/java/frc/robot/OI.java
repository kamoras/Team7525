/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Constants.armPosition;
import frc.robot.Constants.intakeMode;
import frc.robot.Constants.liftPosition;
import frc.robot.commands.MoveArm;
import frc.robot.commands.MoveLift;
import frc.robot.commands.OperateIntake;
import frc.robot.commands.ReleaseHatch;
import frc.robot.commands.ShiftDown;
import frc.robot.commands.ShiftUp;
import frc.robot.commands.TakeHatch;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());

  // Controllers
  Joystick driverStick = new Joystick(RobotMap.driverStickPort); // Drive joystick
  Joystick operatorStick = new Joystick(RobotMap.operatorStickPort); // Operator controller
  
  // Buttons
  Button shift = new JoystickButton(driverStick, 1); // TODO Change the "1" to desired button
  Button liftRocketLow = new JoystickButton(operatorStick, 1);
  Button liftRocketMiddle = new JoystickButton(operatorStick, 2);
  Button liftRocketHigh = new JoystickButton(operatorStick, 3);
  Button liftCargoRocketLow = new JoystickButton(operatorStick, 4);
  Button liftCargoRocketHigh = new JoystickButton(operatorStick, 5);
  Button liftCargoLow = new JoystickButton(operatorStick, 6);
  Button liftCargoHigh = new JoystickButton(operatorStick, 7);
  Button liftDefault = new JoystickButton(operatorStick, 8);
  Button intakeBall = new JoystickButton(operatorStick, 9);
  Button releaseBall = new JoystickButton(operatorStick, 10);
  Button intakeHatch = new JoystickButton(operatorStick, 11);
  Button releaseHatch = new JoystickButton(operatorStick, 12);
  Button armToHome = new JoystickButton(operatorStick, 13);
  Button armToHatchIn = new JoystickButton(operatorStick, 14);
  Button armToHatchOut = new JoystickButton(operatorStick, 15);
  Button armToBallIn = new JoystickButton(operatorStick, 16);
  Button armToBallOut = new JoystickButton(operatorStick, 17);

  public Joystick getDriverStick() {
    return driverStick;
  }

  public Joystick getOperatorStick() {
    return operatorStick;
  }

  public void initButtons() {
    // When you push the shift button the robot will go into high gear. 
    // It will go back down to low gear when the button is released.
    shift.whenPressed(new ShiftUp());
    shift.whenReleased(new ShiftDown());
    // Lift buttons
    liftRocketLow.whenPressed(new MoveLift(liftPosition.ROCKET_LOW));
    liftRocketMiddle.whenPressed(new MoveLift(liftPosition.ROCKET_MIDDLE));
    liftRocketHigh.whenPressed(new MoveLift(liftPosition.ROCKET_HIGH));
    liftCargoRocketLow.whenPressed(new MoveLift(liftPosition.CARGO_ROCKET_LOW));
    liftCargoRocketHigh.whenPressed(new MoveLift(liftPosition.CARGO_ROCKET_HIGH));
    liftCargoLow.whenPressed(new MoveLift(liftPosition.CARGO_SHIP_LOW));
    liftCargoHigh.whenPressed(new MoveLift(liftPosition.CARGO_SHIP_HIGH));
    liftDefault.whenPressed(new MoveLift(liftPosition.DEFAULT));
    // Intake buttons
    intakeBall.whenPressed(new OperateIntake(intakeMode.BALL_IN));
    releaseBall.whenPressed(new OperateIntake(intakeMode.BALL_OUT));
    intakeHatch.whenPressed(new TakeHatch());
    releaseHatch.whenPressed(new ReleaseHatch());
    // Arm buttons
    armToHome.whenPressed(new MoveArm(armPosition.HOME));
    armToBallIn.whenPressed(new MoveArm(armPosition.BALL_IN));
    armToBallOut.whenPressed(new MoveArm(armPosition.BALL_OUT));
    armToHatchIn.whenPressed(new MoveArm(armPosition.HATCH_IN));
    armToHatchOut.whenPressed(new MoveArm(armPosition.HATCH_OUT));
  }
}
