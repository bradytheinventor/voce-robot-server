package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class RotationPIDOutput implements PIDOutput {
	Robot r;
	
	public RotationPIDOutput(Robot r) {
		this.r = r;
	}

	@Override
	public void pidWrite(double output) {
		r.driveLeft(output);
		r.driveRight(-output);
	}
	
	

}
