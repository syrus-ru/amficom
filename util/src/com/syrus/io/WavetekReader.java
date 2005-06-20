/*
 * $Id: WavetekReader.java,v 1.11 2005/06/20 14:24:40 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/20 14:24:40 $
 * @author $Author: bass $
 * @module util
 */
public final class WavetekReader extends DataReader {
	BellcoreStructure bs;
	ByteArrayInputStream bais;
	IntelDataInputStream idis;
	ByteArrayConverter bac;

	static int size;
	static int averages;
	static short wavelength;
	static int actualwavelength;
	static double groupindex;
	static double pulsewidth;
	static double range;
	static double resolution;
	static String id;
	static long datetime;

	// Ееще не реализованы
	static String date;
	static String time;
	static String fibertype;

	private boolean flag = false;
	private int pos = 0;
	int i;

	@Override
	public BellcoreStructure getData(byte[] raw_data) {
		if (raw_data.length < 50)
			return null;
		this.bs = new BellcoreStructure();
		this.bais = new ByteArrayInputStream(raw_data);
		this.idis = new IntelDataInputStream(this.bais);
		this.bac = new ByteArrayConverter(raw_data);
		// filesize = raw_data.length;

		this.search(); // 1st
		this.readParams();
		this.search(); // 2nd
		this.readTraceinfo();
		this.search(); // 3rd
		this.search(); // 4th
		this.readTrace();
		this.fillBellcore();
		return this.bs;
	}

	private int readParams() {
		averages = this.bac.readIInt(this.pos + 0x35);
		wavelength = this.bac.readIShort(this.pos + 0x33);
		actualwavelength = wavelength * 10;
		groupindex = this.bac.readIDouble(this.pos + 0x3D);
		datetime = this.bac.readIUnsignedInt(this.pos + 0x4d);
		pulsewidth = this.bac.readIShort(this.pos + 0x31);
		range = this.bac.readIDouble(this.pos + 0x29);
		id = this.bac.readIString(this.pos + 0x79);
		return 1;
	}

	private int readTraceinfo() {
		size = this.bac.readIUnsignedShort(this.pos + 0x6);
		resolution = range / size;
		return 1;
	}

	private int readTrace()
	{
		this.bs.addField(7);
		try
		{
			this.bs.dataPts.tndp = size;
			this.bs.dataPts.tsf = 1;
			this.bs.dataPts.tps = new int [1];
			this.bs.dataPts.sf = new short [1];
			this.bs.dataPts.tps[0] = size;
			this.bs.dataPts.sf[0] = 1000;
			this.bs.dataPts.dsf = new int[1][size];
			for (this.i = 0; this.i < size; this.i++)
				this.bs.dataPts.dsf[0][this.i] = 65535 - this.idis.readIUnsignedShort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 1;
	}

	private int fillBellcore()
	{
		this.bs.addField(BellcoreStructure.GENPARAMS);
		this.bs.genParams.nw = wavelength;

		this.bs.addField(3);
		this.bs.supParams.omid = id;

		this.bs.addField(4);
		this.bs.fxdParams.dts = datetime;
		this.bs.fxdParams.ud = "mt";
		this.bs.fxdParams.aw = (short)(actualwavelength);
		this.bs.fxdParams.tpw = 1;
		this.bs.fxdParams.pwu = new short[1];
		this.bs.fxdParams.ds = new int [1];
		this.bs.fxdParams.nppw = new int [1];
		this.bs.fxdParams.pwu[0] = (short)pulsewidth;
		this.bs.fxdParams.ds[0] = (int)(resolution * groupindex / 3d * 100d * 10000d/*points*/ * 1000d/*meters*/);
		this.bs.fxdParams.nppw[0] = size;
		this.bs.fxdParams.gi = (int)(groupindex * 100000);
		this.bs.fxdParams.nav = averages;
		this.bs.fxdParams.ar = (int)(resolution * size * groupindex / 3d * 100d * 1000d/*meters*/);

		this.bs.addField(9);
		this.bs.cksum.csm = 0;

		this.bs.addField(1);
		this.bs.map.mrn = 100;
		this.bs.map.nb = 6;

		this.bs.map.bId = new String[6];
		this.bs.map.bRev = new int[6];
		this.bs.map.bSize = new int[6];

		this.bs.map.bId[0] = "Map";
		this.bs.map.bId[1] = "GenParams";
		this.bs.map.bId[2] = "SupParams";
		this.bs.map.bId[3] = "FxdParams";
		this.bs.map.bId[4] = "DataPts";
		this.bs.map.bId[5] = "Cksum";

		for (int i1 = 1; i1 < 6; i1++)
			this.bs.map.bRev[i1] = 100;

		this.bs.map.bSize[1] = this.bs.genParams.getSize();
		this.bs.map.bSize[2] = this.bs.supParams.getSize();
		this.bs.map.bSize[3] = this.bs.fxdParams.getSize();
		this.bs.map.bSize[4] = this.bs.dataPts.getSize();
		this.bs.map.bSize[5] = this.bs.cksum.getSize();
		this.bs.map.mbs = this.bs.map.getSize();

		return 1;
	}

	private void search ()
	{
		try
		{
			this.flag = false;
			while (!this.flag)
			{
				if (this.idis.readIChar() == 167)
					if (this.idis.readIChar() == 40)
						if (this.idis.readIChar() == 2)
							if (this.idis.readIChar() == 21)
								this.flag = true;
				this.pos++;
			}
			this.pos += 3;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
