/*-
 * $Id: ReliabilitySimpleReflectogramEventImpl.java,v 1.9 2005/07/22 06:39:51 saa Exp $
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
 * @version $Revision: 1.9 $, $Date: 2005/07/22 06:39:51 $
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

	protected void readSpecificFromDIS(DataInputStream dis)
	throws IOException{
		this.nSigma = (int)dis.readByte(); // sign-extendive conversion
	}

	protected void writeSpecificToDOS(DataOutputStream dos)
	throws IOException {
		dos.writeByte(this.nSigma);
	}

	public static void writeArrayToDOS(
			ReliabilitySimpleReflectogramEventImpl[] se,
			DataOutputStream dos) throws IOException {
		dos.writeInt(se.length);
		writeArrayBaseToDOS(se, dos);
		for (int i = 0; i < se.length; i++) {
			se[i].writeSpecificToDOS(dos);
		}
	}

	public static ReliabilitySimpleReflectogramEventImpl[]readArrayFromDIS(
			DataInputStream dis) throws IOException {
		int len = dis.readInt();
		ReliabilitySimpleReflectogramEventImpl[] se =
			new ReliabilitySimpleReflectogramEventImpl[len];
		for (int i = 0; i < se.length; i++) {
			se[i] = new ReliabilitySimpleReflectogramEventImpl();
		}
		readArrayBaseFromDIS(se, dis);
		for (int i = 0; i < se.length; i++) {
			se[i].readSpecificFromDIS(dis);
		}
		return se;
	}

	public double getReliability() {
		if (hasReliability()) {
			double tau = this.nSigma * SIGMA_PREC;
			// use approximation for 1.0 - 2.0 * erf(tau)
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

	@Override
	public String toString() {
		return "RSE("
			+ "T=" + getEventType()
			+ ",B=" + getBegin() + ",E=" + getEnd()
			+ ",R=" + (hasReliability() ? "" + getReliability() : "<no>") + ")";
	}
}
