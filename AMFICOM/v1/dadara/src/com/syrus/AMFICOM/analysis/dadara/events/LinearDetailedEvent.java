/*-
 * $Id: LinearDetailedEvent.java,v 1.1 2005/05/05 11:45:28 saa Exp $
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
        return maxDev;
    }
    public double getRmsDev() {
        return rmsDev;
    }
    public double getY0() {
        return y0;
    }
    public double getY1() {
        return y1;
    }
    public double getLoss() {
        return y0 - y1;
    }
    public double getAttenuation() {
        return (y0 - y1) / (end - begin);
    }
    //public LinearDetailedEvent(int begin, int end, int eventType,)
    protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
        dos.writeDouble(y0);
        dos.writeDouble(y1);
        dos.writeDouble(rmsDev);
        dos.writeDouble(maxDev);
    }

    protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
        y0 = dis.readDouble();
        y1 = dis.readDouble();
        rmsDev = dis.readDouble();
        maxDev = dis.readDouble();
    }
}
