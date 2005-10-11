/*-
 * $Id: EvaluationPerEventResultImpl.java,v 1.1 2005/10/11 16:42:01 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/10/11 16:42:01 $
 * @module
 */
public class EvaluationPerEventResultImpl
implements EvaluationPerEventResult, DataStreamable {
	private static DataStreamable.Reader reader = null;

	private boolean[] qkPresent;
	private double[] qi;
	private double[] ki;
	private static final long VERSION = 250669505101000L;

	/**
	 * @see EvaluationPerEventResult#getNEvents()
	 */
	public int getNEvents() {
		return this.qkPresent.length;
	}

	/**
	 * @see EvaluationPerEventResult#hasQK(int)
	 */
	public boolean hasQK(int i) {
		return this.qkPresent[i];
	}
	/**
	 * @see EvaluationPerEventResult#getQ(int)
	 */
	public double getQ(int i) {
		if (this.qkPresent[i])
			return this.qi[i];
		throw new IllegalStateException();
	}
	/**
	 * @see EvaluationPerEventResult#getK(int)
	 */
	public double getK(int i) {
		if (this.qkPresent[i])
			return this.ki[i];
		throw new IllegalStateException();
	}

	/**
	 * Package-visible.
	 * @param qi массив Qi; the caller will never change this array 
	 * @param ki массив Ki; the caller will never change this array
	 * @param presence массив флагов присутствия qi/ki;
	 *   the caller will never change this array
	 */
	EvaluationPerEventResultImpl(boolean[] presence, double[] qi, double[] ki) {
		this.qkPresent = presence;
		this.qi = qi;
		this.ki = ki;
	}

	/**
	 * @param dos
	 * @throws IOException
	 * @see com.syrus.AMFICOM.analysis.dadara.DataStreamable#writeToDOS(java.io.DataOutputStream)
	 */
	public void writeToDOS(DataOutputStream dos)
			throws IOException {
		dos.writeLong(VERSION);
		int n = this.qkPresent.length;
		dos.writeInt(n);
		for (int i = 0; i < n; i++) {
			dos.writeBoolean(this.qkPresent[i]);
		}
		for (int i = 0; i < n; i++) {
			dos.writeDouble(this.qi[i]);
		}
		for (int i = 0; i < n; i++) {
			dos.writeDouble(this.ki[i]);
		}
	}

	protected EvaluationPerEventResultImpl(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		long version = dis.readLong();
		if (version != VERSION) {
			throw new SignatureMismatchException();
		}
		int n = dis.readInt();
		this.qi = new double[n];
		this.ki = new double[n];
		for (int i = 0; i < n; i++) {
			this.qi[i] = dis.readDouble();
		}
		for (int i = 0; i < n; i++) {
			this.ki[i] = dis.readDouble();
		}
	}

	/**
	 * @see DataStreamable
	 */
	public static DataStreamable.Reader getDSReader() {
		if (reader == null) {
			reader = new DataStreamable.Reader() {
				public DataStreamable readFromDIS(DataInputStream dis)
						throws IOException, SignatureMismatchException {
					return new EvaluationPerEventResultImpl(dis);
				}
			};
		}
		return reader;
	}
}
