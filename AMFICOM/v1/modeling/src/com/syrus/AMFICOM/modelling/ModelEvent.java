/*-
 * $Id: ModelEvent.java,v 1.1 2005/08/04 09:10:55 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.modelling;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/08/04 09:10:55 $
 * @module
 */
public class ModelEvent {
	private static final int LINEAR = 51;
	//private static final int NONIDENTIFIED = 52;
	private static final int SPLICE = 53;
	private static final int REFLECTIVE = 54;

	private int type;

	// linear & non-id only
	private double length; // [m]

	// linear only
	private double attenuation; // [dB/m]

	// point only
	private double loss; // [dB]; usually positive
	private double reflection; // [dB]; typically -5 .. -50 dB; defined for reflective only

	private ModelEvent(int type, double length, double attenuation,
			double loss, double reflection) {
		this.type = type;
		this.length = length;
		this.attenuation = attenuation;
		this.loss = loss;
		this.reflection = reflection;
	}

	public static ModelEvent createLinear(double length, double attenuation) {
		return new ModelEvent(LINEAR, length, attenuation, 0.0, 0.0);
	}

	public static ModelEvent createSlice(double loss) {
		return new ModelEvent(SPLICE, 0.0, 0.0, loss, 0.0);
	}

	public static ModelEvent createReflective(double loss, double reflection) {
		return new ModelEvent(REFLECTIVE, 0.0, 0.0, loss, reflection);
	}

	public boolean isLinear() {
		return this.type == LINEAR;
	}
	public boolean isSplice() {
		return this.type == SPLICE;
	}
	public boolean isReflective() {
		return this.type == REFLECTIVE;
	}

	public boolean hasLength() {
		return isLinear();
	}
	public double getLength() {
		assert hasLength() : "length undefined";
		return this.length;
	}
	public boolean hasAttenuation() {
		return isLinear();
	}
	public double getAttenuation() {
		assert hasAttenuation() : "attenuation undefined";
		return this.attenuation;
	}

	public boolean hasLoss() {
		return isSplice() || isReflective();
	}
	public double getLoss() {
		assert hasLoss() : "loss undefined";
		return this.loss;
	}
	public boolean hasReflection() {
		return isReflective();
	}
	public double getReflection() {
		assert hasReflection() : "reflection undefined";
		return this.reflection;
	}
}
