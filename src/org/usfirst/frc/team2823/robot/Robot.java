package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	String prevCommand = "FAIL";
	Servo servo;
	
	Talon rTalon;
	Spark rSpark;
	Talon lTalon;
	Spark lSpark;
	
	@Override
	public void robotInit() {
		servo = new Servo(9);
		servo.set(0.0);
		
		rTalon = new Talon(0);
		rSpark = new Spark(1);
		lTalon = new Talon(2);
		lSpark = new Spark(3);
		
		SmartDashboard.putBoolean("New Command", false);
	}
	
	@Override
	public void autonomousInit() {
		
	}
	
	@Override
	public void autonomousPeriodic() {
		
	}
	
	@Override
	public void teleopPeriodic() {
		//read voice commands from SmartDashboard
		String command = SmartDashboard.getString("Command", "FAIL");
		boolean newCommand = SmartDashboard.getBoolean("New Command", false);
		
		//if a new command was sent (client sets newCommand to true)
		if(newCommand && !command.equals(prevCommand)) {
			//reset flag and execute command
			SmartDashboard.putBoolean("New Command", false);
			System.out.println(command);
			
			if(command.contains("servo")) {
				if(command.contains("on")) {
					servo.set(1.0);
				} else if(command.contains("off")) {
					servo.set(0.0);
				}
			}
		}
		
		prevCommand = command;
	}
	
	@Override
	public void testPeriodic() {
		
	}
	
	public void driveLeft(double p) {
		lTalon.set(p);
		lSpark.set(p);
	}
	
	public void driveRight(double p) {
		rTalon.set(-p);
		rSpark.set(-p);
	}
}
