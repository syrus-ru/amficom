/*-
 * $Id: ReliabilitySimpleReflectogramEventImpl.java,v 1.5 2005/06/28 11:18:36 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/06/28 11:18:36 $
 * @module
 */
public class ReliabilitySimpleReflectogramEventImpl
extends SimpleReflectogramEventImpl
implements ReliabilitySimpleReflectogramEvent {

    double reliability;

    protected ReliabilitySimpleReflectogramEventImpl()
    { // for native use
    }

    /**
     * reliability устанавливается в состояние "не определено"
     * @param begin начало события
     * @param end конец события
     * @param eventType тип события {@link SimpleReflectogramEvent}
     */
    public ReliabilitySimpleReflectogramEventImpl(int begin, int end,
            int eventType) {
        super(begin, end, eventType);
        this.reliability = -1;
    }

    public ReliabilitySimpleReflectogramEventImpl(DataInputStream dis)
    throws IOException{
        super(dis);
        this.reliability = dis.readBoolean() ? dis.readDouble() : -1;
    }

    public void writeToDOS(DataOutputStream dos)
    throws IOException {
        super.writeToDOS(dos);
        dos.writeBoolean(hasReliability());
        if (hasReliability())
            dos.writeDouble(getReliability());
    }

    public double getReliability() {
        if (hasReliability())
            return reliability;
        else
            throw new IllegalArgumentException("getReliability() requested on event that has no probability");
    }

    public boolean hasReliability() {
        return reliability >= 0;
    }

    public String toString() {
        return "RSE("
            + "T=" + getEventType()
            + ",B=" + getBegin() + ",E=" + getEnd()
            + ",R=" + (hasReliability() ? "" + getReliability() : "<no>") + ")";
    }
}
