/*-
 * $Id: LinearDetailedEvent.java,v 1.5 2005/10/06 13:34:02 saa Exp $
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
 * Ћинейный участок рефлектограммы.
 * y0 - уровень начала событи€, дЅ (отрицательное значение)
 * y1 - уровень конца событи€, дЅ (отрицательное значение)
 * rmsDev - среднеквадратическое отклонение рефлектограммы от аналитической кривой, дЅ
 * maxDev - максимальное отклонение рефлектограммы от аналитической кривой, дЅ
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public class LinearDetailedEvent extends DetailedEvent {
	private double y0;
	private double y1;
	private double rmsDev;
	private double maxDev;

	public LinearDetailedEvent(SimpleReflectogramEvent sre, double y0,
			double y1, double rmsDev, double maxDev) {
		//super(begin, end, eventType);
		super(sre);
		this.y0 = y0;
		this.y1 = y1;
		this.rmsDev = rmsDev;
		this.maxDev = maxDev;
	}
	protected LinearDetailedEvent() {
		// empty, for dis reading
	}
	public double getMaxDev() {
		return this.maxDev;
	}
	public double getRmsDev() {
		return this.rmsDev;
	}
	public double getY0() {
		return this.y0;
	}
	public double getY1() {
		return this.y1;
	}
	public double getLoss() {
		return this.y0 - this.y1;
	}
	public double getAttenuation() {
		return (this.y0 - this.y1) / (this.end - this.begin);
	}
	//public LinearDetailedEvent(int begin, int end, int eventType,)
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
		dos.writeDouble(this.y0);
		dos.writeDouble(this.y1);
		dos.writeDouble(this.rmsDev);
		dos.writeDouble(this.maxDev);
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
		this.y0 = dis.readDouble();
		this.y1 = dis.readDouble();
		this.rmsDev = dis.readDouble();
		this.maxDev = dis.readDouble();
	}
}
