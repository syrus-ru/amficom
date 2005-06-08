/*
 * $Id: TraceReader.java,v 1.10 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.File;
import java.text.SimpleDateFormat;

import com.syrus.util.TraceDataReader;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/08 13:49:06 $
 * @author $Author: bass $
 * @module util
 */
public final class TraceReader extends DataReader {
	public static final int BELLCORE = 3;
	public static final int LP = 6;
	public static final int ANDO = 7;
	public static final int WAVETEK = 8;

	public native int getTrace(String filename);

	private byte[] raw = null;
	private static boolean treaderLoaded = false;

	static {
		try {
			System.loadLibrary("Treader");
			treaderLoaded = true;
		}
		catch (UnsatisfiedLinkError ule) {
			System.err.println("Cannot load library for TraceReader -- " + ule.getMessage());
			ule.printStackTrace();
		}

	}

	public BellcoreStructure getData(byte[] b) {
		throw new UnsupportedOperationException("Method not implemented");
		//return null;
	}

	public BellcoreStructure getData(File f) {
		BellcoreStructure bs = null;
		// then Bellcore
		if (f.getName().toLowerCase().endsWith(".sor")) {
			filetype = BELLCORE;
			BellcoreReader br = new BellcoreReader();
			bs = br.getData(f);
		}
		else
			// then Wavetek
			if (f.getName().toLowerCase().endsWith(".tfw")) {
				filetype = WAVETEK;
				WavetekReader wr = new WavetekReader();
				bs = wr.getData(f);
			}
			else
				if ((this.raw = new TraceDataReader().getBellcoreData(f.getAbsolutePath())) != null) {
					bs = new BellcoreReader().getData(this.raw);
					this.raw = null;
				}
				else
					if (treaderLoaded && new TraceReader().getTrace(f.getAbsolutePath()) != 0) {
						bs = new BellcoreStructure();
						bs = fillBellcore(bs);
					}
					else {
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

	private BellcoreStructure fillBellcore(BellcoreStructure bs)
	{
		bs.addField(2);
		bs.genParams.nw = (short)(actualwavelength/10);

		bs.addField(3);
		bs.supParams.omid = id;

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
		bs.fxdParams.dts = dt;
		bs.fxdParams.ud = "mt";
		bs.fxdParams.aw = (short) (actualwavelength);
		bs.fxdParams.tpw = 1;
		bs.fxdParams.pwu = new short[1];
		bs.fxdParams.ds = new int[1];
		bs.fxdParams.nppw = new int[1];
		bs.fxdParams.pwu[0] = pulsewidth;
		bs.fxdParams.ds[0] = (int) (resolution * groupindex / 3d * 100d * 10000d/* pionts */* 1000d/* meters */);
		bs.fxdParams.nppw[0] = size;
		bs.fxdParams.gi = (int) (groupindex * 100000);
		bs.fxdParams.nav = averages;
		bs.fxdParams.ar = (int) (resolution * size * groupindex / 3d * 100d * 1000d/* meters */);
		ByteArrayConverter bac = new ByteArrayConverter(data);

		bs.addField(7);
		bs.dataPts.tndp = size;
		bs.dataPts.tsf = 1;
		bs.dataPts.tps = new int[1];
		bs.dataPts.sf = new short[1];
		bs.dataPts.tps[0] = size;
		bs.dataPts.sf[0] = 1000;
		bs.dataPts.dsf = new int[1][size];

		for (int i = 0; i < size; i++)
			bs.dataPts.dsf[0][i] = bac.readIUnsignedShort(i * 2);

		bs.dataPts.dsf[0] = sort(bs.dataPts.dsf[0], filetype);

		bs.addField(9);
		bs.cksum.csm = 0;

		bs.addField(1);
		bs.map.mrn = 100;
		bs.map.nb = 6;

		bs.map.bId = new String[6];
		bs.map.bRev = new int[6];
		bs.map.bSize = new int[6];

		bs.map.bId[0] = "Map";
		bs.map.bId[1] = "GenParams";
		bs.map.bId[2] = "SupParams";
		bs.map.bId[3] = "FxdParams";
		bs.map.bId[4] = "DataPts";
		bs.map.bId[5] = "Cksum";
		for (int i = 1; i < 6; i++)
			bs.map.bRev[i] = 100;
		bs.map.bSize[1] = bs.genParams.getSize();
		bs.map.bSize[2] = bs.supParams.getSize();
		bs.map.bSize[3] = bs.fxdParams.getSize();
		bs.map.bSize[4] = bs.dataPts.getSize();
		bs.map.bSize[5] = bs.cksum.getSize();
		bs.map.mbs = bs.map.getSize();

		return bs;
	}

	int[] sort(int[] arr, int filetype1) {
		int min = arr[0];
		int max = arr[0];

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max)
				max = arr[i];
			else
				if (arr[i] < min)
					min = arr[i];
		}

		int delta = max - min;
		double d2 = 40000d / delta;
		if ((filetype1 == LP) && (delta < 40000)) {
			for (int i = 0; i < arr.length; i++)
				arr[i] = (int) (arr[i] * d2);
			max = (int) (max * d2);
			min = (int) (min * d2);
		}
		for (int i = 0; i < arr.length; i++)
			arr[i] = max - arr[i];

		return arr;
	}
}
