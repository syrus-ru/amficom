/*-
 * $Id: EndOfTraceDetailedEvent.java,v 1.5 2005/10/06 13:34:02 saa Exp $
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
 * Конец волокна
 * y0 - уровень начала события, дБ (отрицательное значение)
 * y2 - уровень максимума, дБ (отрицательное значение или ноль)
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public class EndOfTraceDetailedEvent extends DetailedEvent {
	private double y0;
	private double y2;
	//private double refl;

	protected EndOfTraceDetailedEvent() {
		// for DIS reading
	}
	public EndOfTraceDetailedEvent(SimpleReflectogramEvent sre,
			double y0, double y2) {
		super(sre);
		this.y0 = y0;
		this.y2 = y2;
//        this.refl = refl;
	}

//    public double getRefl() {
//        return refl;
//    }
//    public void setRefl(double refl) {
//        this.refl = refl;
//    }
	public double getAmpl() {
		return this.y2 - this.y0;
	}
	public double getY0() {
		return this.y0;
	}
	public double getY2() {
		return this.y2;
	}
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
		dos.writeDouble(this.y0);
		dos.writeDouble(this.y2);
//        dos.writeDouble(this.refl);
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
		this.y0 = dis.readDouble();
		this.y2 = dis.readDouble();
//        this.refl = dis.readDouble();
	}
}
