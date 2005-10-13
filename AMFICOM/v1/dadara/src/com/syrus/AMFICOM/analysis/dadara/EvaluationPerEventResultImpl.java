/*-
 * $Id: EvaluationPerEventResultImpl.java,v 1.2 2005/10/13 11:23:32 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.io.SignatureMismatchException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/10/13 11:23:32 $
 * @module
 */
public class EvaluationPerEventResultImpl
implements EvaluationPerEventResult, DataStreamable {
	private static final long SIGNATURE = 72314905101200L;
	private static DataStreamable.Reader dsReader = null;

	private boolean[] qk;
	private double[] q;
	private double[] k;

	/**
	 * @see EvaluationPerEventResult#getNEvents()
	 */
	public int getNEvents() {
		return this.qk.length;
	}

	/**
	 * @see EvaluationPerEventResult#hasQK(int)
	 */
	public boolean hasQK(int i) {
		return this.qk[i];
	}

	/**
	 * @see EvaluationPerEventResult#getQ(int)
	 */
	public double getQ(int i) {
		if (hasQK(i)) {
			return this.q[i];
		}
		throw new IllegalStateException();
	}

	/**
	 * @see EvaluationPerEventResult#getK(int)
	 */
	public double getK(int i) {
		if (hasQK(i)) {
			return this.k[i];
		}
		throw new IllegalStateException();
	}

	public EvaluationPerEventResultImpl(EvaluationPerEventResult that) {
		int size = that.getNEvents();
		this.qk = new boolean[size];
		this.q = new double[size];
		this.k = new double[size];
		for (int i = 0; i < size; i++) {
			this.qk[i] = that.hasQK(i);
			if (this.qk[i]) {
				this.k[i] = that.getK(i);
				this.q[i] = that.getQ(i);
			} else {
				this.k[i] = 0.0; // will not be used
				this.q[i] = 0.0; // will not be used
			}
		}
	}

	public void writeToDOS(DataOutputStream dos) throws IOException {
		// XXX: тратится довольно много места. разработать более компактный формат
		dos.writeLong(SIGNATURE);
		dos.writeInt(this.qk.length);
		for (int i = 0; i < this.qk.length; i++) {
			dos.writeBoolean(this.qk[i]);
			if (this.qk[i]) {
				dos.writeDouble(this.q[i]);
				dos.writeDouble(this.k[i]);
			}
		}
	}

	protected EvaluationPerEventResultImpl(DataInputStream dis)
	throws SignatureMismatchException, IOException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		final int size = dis.readInt();
		this.qk = new boolean[size];
		this.q = new double[size];
		this.k = new double[size];
		for (int i = 0; i < size; i++) {
			this.qk[i] = dis.readBoolean();
			if (this.qk[i]) {
				this.q[i] = dis.readDouble();
				this.k[i] = dis.readDouble();
			}
		}
	}

	/**
	 * @see DataStreamable
	 */
	public static DataStreamable.Reader getDSReader() {
		if (dsReader == null) {
			dsReader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
						throws IOException, SignatureMismatchException {
					return new EvaluationPerEventResultImpl(dis);
				}
			};
		}
		return dsReader;
	}
}
