/*
 * $Id: TraceReader.java,v 1.4 2004/11/22 14:03:44 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.io;

import com.syrus.util.TraceDataReader;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * @version $Revision: 1.4 $, $Date: 2004/11/22 14:03:44 $
 * @author $Author: stas $
 * @module general_v1
 */
public class TraceReader extends DataReader
{
	public static final int BELLCORE = 3;
	public static final int LP = 6;
	public static final int ANDO = 7;
	public static final int WAVETEK = 8;

	public native int getTrace(String filename);

	private byte[] raw = null;
	private static boolean treader_loaded = false;

	static
	{
		try
		{
			System.loadLibrary("Treader");
			treader_loaded = true;
		}
		catch (UnsatisfiedLinkError ex)
		{
			// do nothing
			;
		}

	}

	public BellcoreStructure getData(byte[] b)
	{
		return null;
	}

	public BellcoreStructure getData(File f)
	{
		BellcoreStructure bs = null;

		if (f.getName().toLowerCase().endsWith(".sor")) // then Bellcore
		{
			filetype = BELLCORE;
			BellcoreReader br = new BellcoreReader();
			bs = br.getData(f);
		}
		else if (f.getName().toLowerCase().endsWith(".tfw")) // then Wavetek
		{
			filetype = WAVETEK;
			WavetekReader wr = new WavetekReader();
			bs = wr.getData(f);
		}
		else if ((raw = new TraceDataReader().getBellcoreData(f.getAbsolutePath())) != null)
		{
			bs = new BellcoreReader().getData(raw);
			raw = null;
		}
		else if (treader_loaded && new TraceReader().getTrace(f.getAbsolutePath()) != 0)
		{
			bs = new BellcoreStructure();
			bs = fill_bellcore(bs);
		}
		else
		{
			System.out.println("Error reading " + f.getAbsolutePath() + " Unknown format");
		}
		return bs;
	}

	int traceInfoAddr;
	static int filetype;
	static short size;
	static double resolution;
	static short pulsewidth;
	static int averages;
	static int actualwavelength;
	static double groupindex;
	static String date;
	static String time;
	static String id;
	static String fibertype;
	static byte[] data;
	static int wavelength;
	static double range;

	private BellcoreStructure fill_bellcore(BellcoreStructure bs)
	{
		bs.addField(2);
		bs.genParams.NW = (short)(actualwavelength/10);

		bs.addField(3);
		bs.supParams.OMID = id;

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
		long dt = 0;
		try
		{
			dt = sdf.parse(date + " " + time).getTime() / 1000;
		}
		catch (java.text.ParseException ex)
		{
			dt = System.currentTimeMillis() / 1000;
		}

		bs.addField(4);
		bs.fxdParams.DTS = dt;
		bs.fxdParams.UD = "mt";
		bs.fxdParams.AW = (short)(actualwavelength);
		bs.fxdParams.TPW = 1;
		bs.fxdParams.PWU = new short[1];
		bs.fxdParams.DS = new int [1];
		bs.fxdParams.NPPW = new int [1];
		bs.fxdParams.PWU[0] = (short)pulsewidth;
		bs.fxdParams.DS[0] = (int)(resolution * groupindex / 3d * 100d * 10000d/*pionts*/ * 1000d/*meters*/);
		bs.fxdParams.NPPW[0] = size;
		bs.fxdParams.GI = (int)(groupindex * 100000);
		bs.fxdParams.NAV = averages;
		bs.fxdParams.AR = (int)(resolution * size * groupindex / 3d * 100d * 1000d/*meters*/);
		ByteArrayConverter bac = new ByteArrayConverter(data);

		bs.addField(7);
		bs.dataPts.TNDP = size;
		bs.dataPts.TSF = 1;
		bs.dataPts.TPS = new int [1];
		bs.dataPts.SF = new short [1];
		bs.dataPts.TPS[0] = size;
		bs.dataPts.SF[0] = 1000;
		bs.dataPts.DSF = new int[1][size];

		try
		{
			for (int i = 0; i < size; i++)
				bs.dataPts.DSF[0][i] = bac.readIUnsignedShort(i*2);
		}
		catch (java.io.IOException e) { e.printStackTrace(); }

		bs.dataPts.DSF[0] = sort (bs.dataPts.DSF[0], filetype);

		bs.addField(9);
		bs.cksum.CSM = 0;

		bs.addField(1);
		bs.map.MRN = 100;
		bs.map.NB = 6;

		bs.map.B_id = new String[6];
		bs.map.B_rev = new int[6];
		bs.map.B_size = new int[6];

		bs.map.B_id[0] = "Map";
		bs.map.B_id[1] = "GenParams";
		bs.map.B_id[2] = "SupParams";
		bs.map.B_id[3] = "FxdParams";
		bs.map.B_id[4] = "DataPts";
		bs.map.B_id[5] = "Cksum";
		for (int i = 1; i < 6; i++)
			bs.map.B_rev[i] = 100;
		bs.map.B_size[1] = bs.genParams.getSize();
		bs.map.B_size[2] = bs.supParams.getSize();
		bs.map.B_size[3] = bs.fxdParams.getSize();
		bs.map.B_size[4] = bs.dataPts.getSize();
		bs.map.B_size[5] = bs.cksum.getSize();
		bs.map.MBS = bs.map.getSize();

		return bs;
	}

	int[] sort (int[] arr, int filetype)
	{
		int min = arr[0];
		int max = arr[0];

		for (int i=0; i<arr.length; i++)
		{
			if (arr[i] > max) max = arr[i];
			else
			if (arr[i] < min) min = arr[i];
		}

		int delta = max - min;
		double d2 = 40000d / (double)delta;
		if ((filetype == LP) && (delta < 40000))
		{
			for (int i=0; i<arr.length; i++)
				arr[i] = (int)((double)arr[i] * d2);
			max = (int)((double)max * d2);
			min = (int)((double)min * d2);
		}
		for (int i=0; i<arr.length; i++)
			arr[i] = max - arr[i];

		return arr;
	}
}
