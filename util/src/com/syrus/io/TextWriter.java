/*
 * $Id: TextWriter.java,v 1.7 2005/03/17 10:12:50 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * @version $Revision: 1.7 $, $Date: 2005/03/17 10:12:50 $
 * @author $Author: bass $
 * @module util
 */
public class TextWriter {
	byte[] data;
	ByteArrayOutputStream baos;
	PrintWriter pw;
	BellcoreStructure bs;

	public byte[] write(BellcoreStructure bs1) {
		this.bs = bs1;
		this.baos = new ByteArrayOutputStream();
		this.pw = new PrintWriter(this.baos, true);

		this.writeData2();

		this.data = new byte[this.baos.size()];
		this.data = this.baos.toByteArray();
		return this.data;
	}
//
//	private int writeData() {
//		String str = "";
//		int temp;
//		for (int i = 0; i < this.bs.dataPts.tsf; i++) {
//			for (int j = 0; j < this.bs.dataPts.tps[i]; j++) {
//				temp = (65535 - this.bs.dataPts.dsf[i][j]) / 1000;
//				str = Integer.toString(temp);
//				str += ".";
//				str += ((65535 - this.bs.dataPts.dsf[i][j]) - temp * 1000);
//				this.pw.println(str);
//			}
//			this.pw.println();
//		}
//		return 1;
//	}

	private int writeData2() {
		String str = ""; //$NON-NLS-1$
		int temp;
		double step;
		double gi;

		gi = this.bs.fxdParams.gi / 100000d; // коэфф преломления
		this.pw.println("Optical module name: " + this.bs.supParams.omid); //$NON-NLS-1$
		this.pw.println("Wavelength: " + String.valueOf(this.bs.fxdParams.aw / 10) + " nm"); //$NON-NLS-1$ //$NON-NLS-2$
		this.pw.println("Index of refraction: " + String.valueOf(gi)); //$NON-NLS-1$
		this.pw.println("Pulse width: " + String.valueOf(this.bs.fxdParams.pwu[0]) + " ns"); //$NON-NLS-1$ //$NON-NLS-2$
		this.pw.println("Number of points: " + String.valueOf(this.bs.fxdParams.nppw[0])); //$NON-NLS-1$
		this.pw.println("Number of averages: " + String.valueOf(this.bs.fxdParams.nav)); //$NON-NLS-1$
		this.pw.println("Range: " //$NON-NLS-1$
				+ String.valueOf((int) ((this.bs.fxdParams.ar - this.bs.fxdParams.ao) * 0.03d / gi))
				+ " " //$NON-NLS-1$
				+ this.bs.fxdParams.ud);
//		pw.println("Data spacing: " + String.valueOf(2 * ((double)(bs.fxdParams.DS[0]) / 1000000f)));

		for (int i = 0; i < this.bs.dataPts.tsf; i++) {
			step = this.bs.fxdParams.ds[0] * 0.03d / gi / 10000d;
			for (int j = 0; j < this.bs.dataPts.tps[i]; j++) {
				if (this.bs.fxdParams.ud.equalsIgnoreCase("km")) //$NON-NLS-1$
					temp = (int) ((step * j) * 10000);
				else
					temp = (int) ((step * j) * 10);
				str = String.valueOf(temp / 10f);
				str += "\t"; //$NON-NLS-1$
				str += String.valueOf((65535 - this.bs.dataPts.dsf[i][j]) / 1000f);
				this.pw.println(str);
			}
			this.pw.println();
		}
		return 1;
	}
}
