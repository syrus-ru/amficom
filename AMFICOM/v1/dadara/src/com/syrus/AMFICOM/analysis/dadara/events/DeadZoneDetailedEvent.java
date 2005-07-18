/*-
 * $Id: DeadZoneDetailedEvent.java,v 1.3 2005/07/18 14:24:42 saa Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/07/18 14:24:42 $
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
        return adz;
    }
    public int getEdz() {
        return edz;
    }
    public double getPo() {
        return po;
    }
    public double getY1() {
        return y1;
    }
    @Override
	protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
        dos.writeDouble(po);
        dos.writeDouble(y1);
        dos.writeInt(edz);
        dos.writeInt(adz);
    }

    @Override
	protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
        po = dis.readDouble();
        y1 = dis.readDouble();
        edz = dis.readInt();
        adz = dis.readInt();
    }

}
