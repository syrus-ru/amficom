/*-
 * $Id: SpliceDetailedEvent.java,v 1.5 2005/10/06 13:34:02 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * ”силение или потери. Ќа сварки, на изгибе или на усилителе.
 * y0 - уровень начала событи€, дЅ (отрицательное значение)
 * y1 - уровень конца событи€, дЅ (отрицательное значение)
 * loss - уровень потерь на событии (это не совсем то же, что и разница уровн€ на конце и в начале)
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public class SpliceDetailedEvent extends DetailedEvent {
	double y0;
	double y1;
	double loss;

	public SpliceDetailedEvent(SimpleReflectogramEvent sre, double y0,
			double y1, double loss) {
		super(sre);
		this.y0 = y0;
		this.y1 = y1;
		this.loss = loss;
	}
	public SpliceDetailedEvent() {
		// empty, for dis reading
	}

	public double getLoss() {
		return this.loss;
	}
	public double getY0() {
		return this.y0;
	}
	public double getY1() {
		return this.y1;
	}
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
		dos.writeDouble(this.y0);
		dos.writeDouble(this.y1);
		dos.writeDouble(this.loss);
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
		this.y0 = dis.readDouble();
		this.y1 = dis.readDouble();
		this.loss = dis.readDouble();
	}
}
