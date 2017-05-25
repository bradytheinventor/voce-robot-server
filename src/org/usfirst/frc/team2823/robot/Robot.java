package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	//declare constants
	final double ENCODER_RESOLUTION = 2048.0;
	final double DRIVE_RATIO = 1.432 / 3.826;
	final double FUDGE_FACTOR = 194.0 / 196.0;
	final double WHEEL_RADIUS = 5.875 * FUDGE_FACTOR;
	
	final double ENC_TO_IN = 2 * Math.PI * WHEEL_RADIUS * DRIVE_RATIO / ENCODER_RESOLUTION;
	final double IN_TO_ENC = 1 / ENC_TO_IN;
	
	//declare variables
	String prevCommand = "FAIL";
	
	//declare sensor objects
	Encoder lEncoder;
	Encoder rEncoder;
	ADXRS450_Gyro gyro;
	
	//declare output device objects
	Talon rTalon;
	Spark rSpark;
	Talon lTalon;
	Spark lSpark;
	
	Servo servo;
	
	//declare PID controller objects
	AverageEncoderPIDSource dSource;
	
	AdvancedPIDController dControl;
	AdvancedPIDController rControl;
	
	DrivePIDOutput dOutput;
	RotationPIDOutput rOutput;
	
	@Override
	public void robotInit() {
		
		//create sensor objects
		lEncoder = new Encoder(2, 3);
		rEncoder = new Encoder(0, 1);
		gyro = new ADXRS450_Gyro();
		
		//create output device objects
		rTalon = new Talon(0);
		rSpark = new Spark(1);
		lTalon = new Talon(2);
		lSpark = new Spark(3);
		
		servo = new Servo(9);
		servo.set(0.0);
		
		//create PID controller objects
		dSource = new AverageEncoderPIDSource(lEncoder, rEncoder);
		
		dControl = new AdvancedPIDController(0.001, 0.0000025, 0.01, dSource, dOutput, 0.01);
		rControl = new AdvancedPIDController(0, 0, 0, gyro, rOutput, 0.01);
		
		dOutput = new DrivePIDOutput(this);
		rOutput = new RotationPIDOutput(this);
		
		//put initial SmartDashboard values
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
	
	//drive the left half of the drivetrain
	public void driveLeft(double p) {
		lTalon.set(p);
		lSpark.set(p);
	}
	
	//drive the right half of the drivetrain
	public void driveRight(double p) {
		rTalon.set(-p);
		rSpark.set(-p);
	}
}
