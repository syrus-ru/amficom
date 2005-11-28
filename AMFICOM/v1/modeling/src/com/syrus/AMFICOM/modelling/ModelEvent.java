/*-
 * $Id: ModelEvent.java,v 1.5 2005/11/28 11:30:05 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.modelling;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/11/28 11:30:05 $
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

	// FIXME: rename createSlice to createSplice
	public static ModelEvent createSlice(double loss) {
		return new ModelEvent(SPLICE, 0.0, 0.0, loss, 0.0);
	}

	public static ModelEvent createReflective(double loss, double reflection) {
		return new ModelEvent(REFLECTIVE, 0.0, 0.0, loss, reflection);
	}

	/**
	 * Читает из моего убогого формата (сейчас нет ни времени, ни необходимости
	 *  придумывать нормальный формат - он используется только для отладки).
	 * //saa
	 * Создает ModelEvent, добавляет их в данный на входе список,
	 * возвращая длину, которую надо отнести к след. лин. соб.
	 * @param s строка-описатель события (недокументировано)
	 * @param prevLinLen перенесенная длина с предыдущего участка
	 * @return перенесенная длина на следующий участок
	 */
	@Deprecated
	public static double addEventsByString(List<ModelEvent> events, String s,
			double prevLinLen) {
		// LINEAR ?
		Pattern patLinear = Pattern.compile("[LU] ([-0-9.Ee+]+) ([-0-9.Ee+]+)");
		Matcher m = patLinear.matcher(s);
		if (m.matches()) {
			events.add(createLinear(prevLinLen + Double.parseDouble(m.group(1)),
					Double.parseDouble(m.group(2))));
			return 0.0;
		}
		// not linear: immediately insert lin. region of prevLinLen length 
		events.add(createLinear(prevLinLen, 1e-8)); // XXX: 1e-8: затухание 0.0 не допустимо 
		// SPLICE ? 
		Pattern patSplice = Pattern.compile("S ([-0-9.Ee+]+) ([-0-9.Ee+]+)");
		m = patSplice.matcher(s);
		if (m.matches()) {
			events.add(createSlice(Double.parseDouble(m.group(2))));
			return Double.parseDouble(m.group(1));
		}
		// CONNECTOR ?
		Pattern patConn = Pattern.compile("C ([-0-9.Ee+]+) ([-0-9.Ee+]+) ([-0-9.Ee+]+)");
		m = patConn.matcher(s);
		if (m.matches()) {
			events.add(createReflective(Double.parseDouble(m.group(2)),
					Double.parseDouble(m.group(3))));
			return Double.parseDouble(m.group(1));
		}
		// parse error
		throw new IllegalArgumentException(
				"Unrecognized event description string: " + s);
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
		return hasLength();
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
