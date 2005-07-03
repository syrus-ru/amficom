/*-
 * $Id: ConnectorDetailedEvent.java,v 1.1 2005/05/05 11:45:28 saa Exp $
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
    protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
        dos.writeDouble(y0);
        dos.writeDouble(y1);
        dos.writeDouble(y2);
//        dos.writeDouble(refl);
        dos.writeDouble(loss);
    }

    protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
        y0 = dis.readDouble();
        y1 = dis.readDouble();
        y2 = dis.readDouble();
//        refl = dis.readDouble();
        loss = dis.readDouble();
    }
}
