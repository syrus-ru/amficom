package com.syrus.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class TextWriter
{
	byte[] data;
	ByteArrayOutputStream baos;
	PrintWriter pw;
	BellcoreStructure bs;

	public byte[] write (BellcoreStructure bs)
	{
		this.bs = bs;
		baos = new ByteArrayOutputStream();
		pw = new PrintWriter(baos, true);

		write_data_2();

		data = new byte[baos.size()];
		data = baos.toByteArray();
		return data;
	}

	int write_data()
	{
		String str="";
		int temp;
		for (int i = 0; i < bs.dataPts.TSF; i++)
			{
				for (int j = 0; j < bs.dataPts.TPS[i]; j++)
				{
					temp = (65535 - bs.dataPts.DSF[i][j])/1000;
					str = Integer.toString(temp);
					str += ".";
					str += ((65535 - bs.dataPts.DSF[i][j]) - temp*1000);
					pw.println(str);
				}
				pw.println();
			}
		return 1;
	}

	int write_data_2()
	{
		String str="";
		int temp;
		double step;
		double gi;

		gi = ((double)bs.fxdParams.GI / 100000d); // коэфф преломления
		pw.println("Optical module name: " + bs.supParams.OMID);
		pw.println("Wavelength: " + String.valueOf(bs.fxdParams.AW / 10) + " nm");
		pw.println("Index of refraction: " + String.valueOf(gi));
		pw.println("Pulse width: " + String.valueOf(bs.fxdParams.PWU[0]) + " ns");
		pw.println("Number of points: " + String.valueOf(bs.fxdParams.NPPW[0]));
		pw.println("Number of averages: " + String.valueOf(bs.fxdParams.NAV));
		pw.println("Range: " + String.valueOf((int)((float)(bs.fxdParams.AR - bs.fxdParams.AO) * 0.03d / gi))
			+ " " + bs.fxdParams.UD);
//    pw.println("Data spacing: " + String.valueOf(2 * ((double)(bs.fxdParams.DS[0]) / 1000000f)));

		for (int i = 0; i < bs.dataPts.TSF; i++)
			{
				step = (((double) bs.fxdParams.DS[0] * 0.03d) / gi) / 10000d;
				for (int j = 0; j < bs.dataPts.TPS[i]; j++)
				{
					if (bs.fxdParams.UD.equalsIgnoreCase("km"))
						temp = (int)((step * j) * 10000);
					else
						temp = (int)((step * j) * 10);
					str = String.valueOf ((float)temp / 10f);
					str += "\t";
					str += String.valueOf ((float)(65535 - bs.dataPts.DSF[i][j])/1000f);
					pw.println(str);
				}
				pw.println();
			}
		return 1;
	}
}
