/*-
 * $Id: EvaluationPerEventResultImpl.java,v 1.4 2005/10/17 13:45:11 saa Exp $
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
 * immutable реализация {@link EvaluationPerEventResult}
 * с поддержкой {@link DataStreamable}
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.4 $, $Date: 2005/10/17 13:45:11 $
 * @module dadara
 */
public class EvaluationPerEventResultImpl
implements EvaluationPerEventResult, DataStreamable {
	private static final long SIGNATURE = 72314905101200L;
	private static DataStreamable.Reader dsReader = null;

	private boolean[] hasQk;
	private boolean[] isModified;
	private double[] q;
	private double[] k;

	/**
	 * @see EvaluationPerEventResult#getNEvents()
	 */
	public int getNEvents() {
		return this.hasQk.length;
	}

	/**
	 * @see EvaluationPerEventResult#hasQK(int)
	 */
	public boolean hasQK(int i) {
		return this.hasQk[i];
	}

	/**
	 * @see EvaluationPerEventResult#isModified(int)
	 */
	public boolean isModified(int i) {
		if (!hasQK(i)) {
			return this.isModified[i];
		}
		throw new IllegalStateException();
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

	/**
	 * copy-constructor
	 */
	public EvaluationPerEventResultImpl(EvaluationPerEventResult that) {
		int size = that.getNEvents();
		this.hasQk = new boolean[size];
		this.isModified = new boolean[size];
		this.q = new double[size];
		this.k = new double[size];
		for (int i = 0; i < size; i++) {
			this.hasQk[i] = that.hasQK(i);
			if (this.hasQk[i]) {
				this.k[i] = that.getK(i);
				this.q[i] = that.getQ(i);
			} else {
				this.k[i] = 0.0; // will not be used
				this.q[i] = 0.0; // will not be used
				this.isModified[i] = that.isModified(i);
			}
		}
	}

	/**
	 * @see com.syrus.AMFICOM.analysis.dadara.DataStreamable#writeToDOS(java.io.DataOutputStream)
	 */
	public void writeToDOS(DataOutputStream dos) throws IOException {
		// XXX: тратится довольно много места. разработать более компактный формат
		dos.writeLong(SIGNATURE);
		dos.writeInt(this.hasQk.length);
		for (int i = 0; i < this.hasQk.length; i++) {
//			dos.writeBoolean(this.hasQk[i]);
			dos.writeByte(
					(this.hasQk[i] ? 1 : 0)
					| (this.hasQk[i] ? 2 : 0));
			if (this.hasQk[i]) {
				dos.writeDouble(this.q[i]);
				dos.writeDouble(this.k[i]);
			}
		}
	}

	/**
	 * Для DSReader'а
	 */
	protected EvaluationPerEventResultImpl(DataInputStream dis)
	throws SignatureMismatchException, IOException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		final int size = dis.readInt();
		this.hasQk = new boolean[size];
		this.isModified = new boolean[size];
		this.q = new double[size];
		this.k = new double[size];
		for (int i = 0; i < size; i++) {
			final byte b = dis.readByte();
			this.hasQk[i] = (b & 1) != 0;
			if (this.hasQk[i]) {
				this.q[i] = dis.readDouble();
				this.k[i] = dis.readDouble();
			} else {
				this.isModified[i] = (b & 2) != 0;
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
