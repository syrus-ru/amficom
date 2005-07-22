/*-
 * $Id: ConnectorDetailedEvent.java,v 1.4 2005/07/22 06:39:51 saa Exp $
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
 * Параметры коннектора (любого отражения, кроме начала и конца волокна)
 * y0 - уровень начала события, дБ (отрицательное значение)
 * y1 - уровень конца события, дБ (отрицательное значение)
 * y2 - уровень в максимуме события, дБ (отрицательное значение или ноль)
 * loss - уровень потерь на событии (это не совсем то же, что и разница уровня на конце и в начале)
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/07/22 06:39:51 $
 * @module
 */
public class ConnectorDetailedEvent extends DetailedEvent {
	private double y0;
	private double y1;
	private double y2;
//    private double refl;
	private double loss;

	protected ConnectorDetailedEvent() {
		// for DIS reading
	}
	public ConnectorDetailedEvent(SimpleReflectogramEvent ev, double y0,
			double y1, double y2, double loss) {
		super(ev);
		this.y0 = y0;
		this.y1 = y1;
		this.y2 = y2;
//        this.refl = refl;
		this.loss = loss;
	}

	public double getLoss() {
		return loss;
	}
//    public double getRefl() {
//        return refl;
//    }
	public double getY0() {
		return y0;
	}
	public double getY1() {
		return y1;
	}
	public double getY2() {
		return y2;
	}
	public double getAmpl() {
		return y2 - y0;
	}
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
		dos.writeDouble(y0);
		dos.writeDouble(y1);
		dos.writeDouble(y2);
//        dos.writeDouble(refl);
		dos.writeDouble(loss);
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
		y0 = dis.readDouble();
		y1 = dis.readDouble();
		y2 = dis.readDouble();
//        refl = dis.readDouble();
		loss = dis.readDouble();
	}
}
