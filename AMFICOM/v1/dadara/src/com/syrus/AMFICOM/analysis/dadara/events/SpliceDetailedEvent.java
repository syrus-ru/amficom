/*-
 * $Id: SpliceDetailedEvent.java,v 1.1 2005/05/05 11:45:29 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/05/05 11:45:29 $
 * @module
 */
public class SpliceDetailedEvent extends DetailedEvent {
    double y0;
    double y1;
    double loss;

    public SpliceDetailedEvent(SimpleReflectogramEvent sre, double y0,
            double y1, double loss) {
        super(sre);
        this.y0 = y0;
        this.y1 = y1;
        this.loss = loss;
    }
    public SpliceDetailedEvent() {
        // empty, for dis reading
    }

    public double getLoss() {
        return loss;
    }
    public double getY0() {
        return y0;
    }
    public double getY1() {
        return y1;
    }
    protected void writeSpecificToDOS(DataOutputStream dos) throws IOException {
        dos.writeDouble(y0);
        dos.writeDouble(y1);
        dos.writeDouble(loss);
    }

    protected void readSpecificFromDIS(DataInputStream dis) throws IOException {
        y0 = dis.readDouble();
        y1 = dis.readDouble();
        loss = dis.readDouble();
    }
}
