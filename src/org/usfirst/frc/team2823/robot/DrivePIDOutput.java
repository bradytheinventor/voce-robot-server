package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class DrivePIDOutput implements PIDOutput {
	Robot r;
	
	public DrivePIDOutput(Robot r) {
		this.r = r;
	}

	@Override
	public void pidWrite(double output) {
		r.driveLeft(output);
		r.driveRight(output);
	}
}
