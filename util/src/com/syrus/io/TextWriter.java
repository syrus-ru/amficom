/*
 * $Id: TextWriter.java,v 1.5 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class TextWriter
{
	byte[] data;
	ByteArrayOutputStream baos;
	PrintWriter pw;
	BellcoreStructure bs;

	public byte[] write (BellcoreStructure bs1)
	{
		this.bs = bs1;
		this.baos = new ByteArrayOutputStream();
		this.pw = new PrintWriter(this.baos, true);

		write_data_2();

		this.data = new byte[this.baos.size()];
		this.data = this.baos.toByteArray();
		return this.data;
	}

	int write_data()
	{
		String str=""; //$NON-NLS-1$
		int temp;
		for (int i = 0; i < this.bs.dataPts.TSF; i++)
			{
				for (int j = 0; j < this.bs.dataPts.TPS[i]; j++)
				{
					temp = (65535 - this.bs.dataPts.DSF[i][j])/1000;
					str = Integer.toString(temp);
					str += "."; //$NON-NLS-1$
					str += ((65535 - this.bs.dataPts.DSF[i][j]) - temp*1000);
					this.pw.println(str);
				}
				this.pw.println();
			}
		return 1;
	}

	int write_data_2()
	{
		String str=""; //$NON-NLS-1$
		int temp;
		double step;
		double gi;

		gi = this.bs.fxdParams.GI / 100000d; // коэфф преломления
		this.pw.println("Optical module name: " + this.bs.supParams.OMID); //$NON-NLS-1$
		this.pw.println("Wavelength: " + String.valueOf(this.bs.fxdParams.AW / 10) + " nm"); //$NON-NLS-1$ //$NON-NLS-2$
		this.pw.println("Index of refraction: " + String.valueOf(gi)); //$NON-NLS-1$
		this.pw.println("Pulse width: " + String.valueOf(this.bs.fxdParams.PWU[0]) + " ns"); //$NON-NLS-1$ //$NON-NLS-2$
		this.pw.println("Number of points: " + String.valueOf(this.bs.fxdParams.NPPW[0])); //$NON-NLS-1$
		this.pw.println("Number of averages: " + String.valueOf(this.bs.fxdParams.NAV)); //$NON-NLS-1$
		this.pw.println("Range: " + String.valueOf((int)((this.bs.fxdParams.AR - this.bs.fxdParams.AO) * 0.03d / gi)) //$NON-NLS-1$
			+ " " + this.bs.fxdParams.UD); //$NON-NLS-1$
//    pw.println("Data spacing: " + String.valueOf(2 * ((double)(bs.fxdParams.DS[0]) / 1000000f)));

		for (int i = 0; i < this.bs.dataPts.TSF; i++)
			{
				step = this.bs.fxdParams.DS[0] * 0.03d / gi / 10000d;
				for (int j = 0; j < this.bs.dataPts.TPS[i]; j++)
				{
					if (this.bs.fxdParams.UD.equalsIgnoreCase("km")) //$NON-NLS-1$
						temp = (int)((step * j) * 10000);
					else
						temp = (int)((step * j) * 10);
					str = String.valueOf (temp / 10f);
					str += "\t"; //$NON-NLS-1$
					str += String.valueOf ((65535 - this.bs.dataPts.DSF[i][j])/1000f);
					this.pw.println(str);
				}
				this.pw.println();
			}
		return 1;
	}
}
