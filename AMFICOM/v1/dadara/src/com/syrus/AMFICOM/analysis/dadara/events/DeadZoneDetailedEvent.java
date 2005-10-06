/*-
 * $Id: DeadZoneDetailedEvent.java,v 1.5 2005/10/06 13:34:02 saa Exp $
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
 * Параметры мертвой зоны
 * po - экстраполированный (по смежному лин. участку) уровень для начала волокна (отрицательное значение)
 * adz - ширина ADZ, в точках
 * edz - ширина EDZ, в точках
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/10/06 13:34:02 $
 * @module
 */
public class DeadZoneDetailedEvent extends DetailedEvent {
	private double po;
	private double y1;
	private int edz;
	private int adz;

	public DeadZoneDetailedEvent(SimpleReflectogramEvent sre,
			double po, double y1, int edz, int adz) {
		super(sre);
		this.po = po;
		this.y1 = y1;
		this.edz = edz;
		this.adz = adz;
	}
	protected DeadZoneDetailedEvent() {
		// for DIS reading
	}

	public int getAdz() {
		return this.adz;
	}
	public int getEdz() {
		return this.edz;
	}
	public double getPo() {
		return this.po;
	}
	public double getY1() {
		return this.y1;
	}
	@Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
		dos.writeDouble(this.po);
		dos.writeDouble(this.y1);
		dos.writeInt(this.edz);
		dos.writeInt(this.adz);
	}

	@Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
		this.po = dis.readDouble();
		this.y1 = dis.readDouble();
		this.edz = dis.readInt();
		this.adz = dis.readInt();
	}

}
