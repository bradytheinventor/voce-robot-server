package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
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
	
	final double CM_TO_IN = 1 / 2.54;
	final double M_TO_IN = 100 * CM_TO_IN;
	final double FT_TO_IN = 12;
	final double YD_TO_IN = 3 * FT_TO_IN;
	
	//declare variables
	String prevCommand = "NONE";
	
	//declare sensor objects
	Encoder lEncoder;
	Encoder rEncoder;
	ADXRS450_Gyro gyro;
	
	//declare output device objects
	Talon rTalon;
	Spark rSpark;
	Talon lTalon;
	Spark lSpark;
	
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
		String command = SmartDashboard.getString("Command", "NONE");
		boolean newCommand = SmartDashboard.getBoolean("New Command", false);
		
		//if a new command was sent (client sets newCommand to true)
		if(newCommand && !command.equals(prevCommand)) {
			//reset flag and execute command
			SmartDashboard.putBoolean("New Command", false);
			
			System.out.println(command);
			runCommand(command);
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
	
	//calculate the unit conversion from 'u' to inches
	public double getConversion(String u) {
		switch(u) {
		case "centimeter":
		case "centimeters":
			return CM_TO_IN;
		
		case "meter":
		case "meters":
			return M_TO_IN;
		
		case "foot":
		case "feet":
			return FT_TO_IN;
		
		case "yard":
		case "yards":
			return YD_TO_IN;
		
		default:
			return 1.0;
		}
	}
	
	//parse the command string to determine what the robot should do
	public void runCommand(String command) {
		//stop all movement if a 'disable' command is sent
		if(command.contains("disable")) {
			dControl.reset();
			rControl.reset();
		}
		
		//stop specific movements if a 'stop' command is sent
		else if(command.contains("stop")) {
			//stop everything
			if(command.contains("all") || command.contains("every thing")) {
				dControl.reset();
				rControl.reset();
			}
			
			//stop driving
			else if(command.contains("driving")) {
				dControl.reset();
			}
			
			//stop rotating
			else if(command.contains("rotating") || command.contains("spinning")) {
				rControl.reset();
			}
		}
		
		//drive a particular distance if a 'drive' command is sent
		else if(command.contains("drive")) {
			//calculate the direction multiplier (1.0 is forward) and the distance to drive in inches
			double direction = command.contains("forward") ? 1.0 : -1.0;
			double distance = SmartDashboard.getNumber("Command Number", 0.0) *
								getConversion(SmartDashboard.getString("Command Units", "NONE"));
			
			//send values to PID controller
			dControl.setSetpoint(distance * direction);
			dControl.enable();
		}
		
		//rotate by a particular angle if a 'rotate' command is sent
		else if(command.contains("rotate") || command.contains("turn")) {
			//calculate the direction multiplier (1.0 is clockwise, to the right)
			double direction = command.contains("right") ? 1.0 : -1.0;
			double angle = SmartDashboard.getNumber("Command Number", 0.0);
			
			//send values to PID controller
			rControl.setSetpoint(angle * direction);
			rControl.enable();
		}
	}
}
