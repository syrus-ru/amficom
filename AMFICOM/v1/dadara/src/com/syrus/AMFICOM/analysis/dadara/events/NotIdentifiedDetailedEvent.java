/*-
 * $Id: NotIdentifiedDetailedEvent.java,v 1.5 2005/10/06 13:34:02 saa Exp $
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
 * Неидентифицированный участок рефлектограммы.
 * y0 - уровень начала события, дБ (отрицательное значение)
 * y1 - уровень конца события, дБ (отрицательное значение)
 * yMax - максимальное значение рефлектограммы на данном участке, дБ
 * yMin - минимальное значение рефлектограммы на данном участке, дБ
 * maxDev - максимальное отклонение рефлектограммы от аналитической кривой, дБ
 * loss - предполагаемый уровень потерь на событии
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public class NotIdentifiedDetailedEvent extends DetailedEvent {
	private double y0;
	private double y1;
	private double yMin;
	private double yMax;
	private double maxDev;
	private double loss;

	protected NotIdentifiedDetailedEvent() {
		// for DIS reading
	}
	public NotIdentifiedDetailedEvent(SimpleReflectogramEvent sre,
			double y0, double y1, double yMin, double yMax, double maxDev, double loss) {
		super(sre);
		this.y0 = y0;
		this.y1 = y1;
		this.yMin = yMin;
		this.yMax = yMax;
		this.maxDev = maxDev;
		this.loss = loss;
	}

	public double getY0() {
		return this.y0;
	}
	public double getY1() {
		return this.y1;
	}
	public double getYMin() {
		return this.yMin;
	}
	public double getYMax() {
		return this.yMax;
	}
	public double getMaxDev() {
		return this.maxDev;
	}
	public double getLoss() {
		return this.loss;
	}
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
		dos.writeDouble(this.y0);
		dos.writeDouble(this.y1);
		dos.writeDouble(this.loss);
		dos.writeDouble(this.yMin);
		dos.writeDouble(this.yMax);
		dos.writeDouble(this.maxDev);
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
		this.y0 = dis.readDouble();
		this.y1 = dis.readDouble();
		this.loss = dis.readDouble();
		this.yMin = dis.readDouble();
		this.yMax = dis.readDouble();
		this.maxDev = dis.readDouble();
	}
}
