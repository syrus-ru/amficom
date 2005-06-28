/*-
 * $Id: ReliabilitySimpleReflectogramEventImpl.java,v 1.6 2005/06/28 13:00:42 saa Exp $
 * 
 * Copyright c 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/06/28 13:00:42 $
 * @module
 */
public class ReliabilitySimpleReflectogramEventImpl
extends SimpleReflectogramEventImpl
implements ReliabilitySimpleReflectogramEvent {
	// точность представления nSigma
	protected static final double SIGMA_PREC = 0.1;
	protected static final int NSIGMA_MAX = 100; // must be <= 127

	// параметр достоверности
	// -1 - не определена
	// 0..NSIGMA_MAX - определена, достоверность (в станд. отклонениях) ~ nSigma * SIGMA_PREC
	// изменяется из native-кода
	protected int nSigma;

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
        this.nSigma = -1;
    }

    public ReliabilitySimpleReflectogramEventImpl(DataInputStream dis)
    throws IOException{
        super(dis);
        this.nSigma = (int)dis.readByte(); // sign-extendive conversion
    }

    public void writeToDOS(DataOutputStream dos)
    throws IOException {
    	super.writeToDOS(dos);
    	dos.writeByte(this.nSigma);
    }

    public double getReliability() {
    	if (hasReliability()) {
    		double tau = this.nSigma * SIGMA_PREC;
    		// use 2*erf(tau) approx.
    		double prob = 1.0 -
    				Math.exp(-tau*tau/2) /
    						(0.82*tau+Math.sqrt(0.19*tau*tau+1.0));
    		return prob;
    	}
    	else {
    		throw new IllegalArgumentException("getReliability() requested on event that has no probability");
    	}
    }

    public boolean hasReliability() {
        return nSigma >= 0;
    }

    public String toString() {
        return "RSE("
            + "T=" + getEventType()
            + ",B=" + getBegin() + ",E=" + getEnd()
            + ",R=" + (hasReliability() ? "" + getReliability() : "<no>") + ")";
    }
}
