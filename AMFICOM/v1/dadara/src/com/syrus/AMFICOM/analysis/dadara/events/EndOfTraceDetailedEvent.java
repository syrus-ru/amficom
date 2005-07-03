/*-
 * $Id: EndOfTraceDetailedEvent.java,v 1.1 2005/05/05 11:45:28 saa Exp $
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
        return y2 - y0;
    }
    public double getY0() {
        return y0;
    }
    public double getY2() {
        return y2;
    }
    protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
        dos.writeDouble(y0);
        dos.writeDouble(y2);
//        dos.writeDouble(refl);
    }

    protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
        y0 = dis.readDouble();
        y2 = dis.readDouble();
//        refl = dis.readDouble();
    }
}
