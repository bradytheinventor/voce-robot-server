package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Encoder lEncoder;
	Encoder rEncoder;
	ADXRS450_Gyro gyro;
	
	Talon rTalon;
	Spark rSpark;
	Talon lTalon;
	Spark lSpark;
	
	Servo servo;
	
	AverageEncoderPIDSource dSource;
	
	AdvancedPIDController dControl;
	AdvancedPIDController rControl;
	
	DrivePIDOutput dOutput;
	RotationPIDOutput rOutput;
	
	String prevCommand = "FAIL";
	
	@Override
	public void robotInit() {
		
		lEncoder = new Encoder(2, 3);
		rEncoder = new Encoder(0, 1);
		gyro = new ADXRS450_Gyro();
		
		rTalon = new Talon(0);
		rSpark = new Spark(1);
		lTalon = new Talon(2);
		lSpark = new Spark(3);
		
		servo = new Servo(9);
		servo.set(0.0);
		
		dSource = new AverageEncoderPIDSource(lEncoder, rEncoder);
		
		dControl = new AdvancedPIDController(0.001, 0.0000025, 0.01, dSource, dOutput, 0.01);
		rControl = new AdvancedPIDController(0, 0, 0, gyro, rOutput, 0.01);
		
		dOutput = new DrivePIDOutput(this);
		rOutput = new RotationPIDOutput(this);
		
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
