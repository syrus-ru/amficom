/*-
 * $Id: LinearDetailedEvent.java,v 1.6 2005/10/11 13:26:14 saa Exp $
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
 * Линейный участок рефлектограммы.
 * y0 - уровень начала события, дБ (отрицательное значение)
 * y1 - уровень конца события, дБ (отрицательное значение)
 * rmsDev - среднеквадратическое отклонение рефлектограммы от аналитической кривой, дБ
 * maxDev - максимальное отклонение рефлектограммы от аналитической кривой, дБ
 * FIXME: loss для лин. участка не совсем соответствует физическому смыслу
 *   потерь на этом участке. При этом loss зависит от длины этого лин. участка,
 *   полученной в результата распознавания в IA (хотя attenuation определяется
 *   более-менее точно и воспроизводимо). Это может приводить к неправильному
 *   определению (завышению) K- и Q- параметров для лин. участков.
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/10/11 13:26:14 $
 * @module
 */
public class LinearDetailedEvent extends DetailedEvent
implements HavingY0, HavingLoss {
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
