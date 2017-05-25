package org.usfirst.frc.team2823.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AverageEncoderPIDSource implements PIDSource {
	Encoder l;
	Encoder r;
	
	public AverageEncoderPIDSource(Encoder l, Encoder r) {
		this.l = l;
		this.r = r;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		//return the average of the two encoder values
		return (l.getDistance() + r.getDistance()) / 2;
	}
}
