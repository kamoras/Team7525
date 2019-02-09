/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Constants.liftPosition;

public class MoveLift extends Command {
  private liftPosition position;

  public MoveLift() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this(liftPosition.DEFAULT);
  }

  public MoveLift(liftPosition position)
  {
    this.position = position;
    requires(Robot.m_lift);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.m_lift.initializeCounter();
    Robot.m_lift.moveToPosition(position);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.m_lift.isSwitchSet() || Robot.m_lift.hasReachedPosition(position);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.m_lift.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
