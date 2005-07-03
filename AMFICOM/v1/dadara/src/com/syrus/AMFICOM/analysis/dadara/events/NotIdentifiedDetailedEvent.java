/*-
 * $Id: NotIdentifiedDetailedEvent.java,v 1.1 2005/05/05 11:45:28 saa Exp $
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
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/05/05 11:45:28 $
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
        return y0;
    }
    public double getY1() {
        return y1;
    }
    public double getYMin() {
        return yMin;
    }
    public double getYMax() {
        return yMax;
    }
    public double getMaxDev() {
        return maxDev;
    }
    public double getLoss() {
        return loss;
    }
    protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
        dos.writeDouble(y0);
        dos.writeDouble(y1);
        dos.writeDouble(loss);
        dos.writeDouble(yMin);
        dos.writeDouble(yMax);
        dos.writeDouble(maxDev);
    }

    protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
        y0 = dis.readDouble();
        y1 = dis.readDouble();
        loss = dis.readDouble();
        yMin = dis.readDouble();
        yMax = dis.readDouble();
        maxDev = dis.readDouble();
    }
}
