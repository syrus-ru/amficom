/*
 * $Id: WavetekReader.java,v 1.6 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.6 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class WavetekReader extends DataReader
{
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

	// еще не реализованы
	static String date;
	static String time;
	static String fibertype;


	private boolean flag = false;
	private int pos = 0;
	int i;

	public BellcoreStructure getData (byte[] raw_data)
	{
		if (raw_data.length < 50)
			 return null;
		this.bs = new BellcoreStructure();
		this.bais = new ByteArrayInputStream(raw_data);
		this.idis = new IntelDataInputStream(this.bais);
		this.bac = new ByteArrayConverter(raw_data);
		//filesize = raw_data.length;

		search(); //1st
			read_params();
		search(); //2nd
			read_traceinfo();
		search(); //3rd
		search(); //4th
			read_trace();
		fill_bellcore();
		return this.bs;
	}

	int read_params()
	{
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

	int read_traceinfo()
	{
		size = this.bac.readIUnsignedShort(this.pos + 0x6);
		resolution = range / size;
		return 1;
	}

	int read_trace()
	{
		this.bs.addField(7);
		try
		{
			this.bs.dataPts.TNDP = size;
			this.bs.dataPts.TSF = 1;
			this.bs.dataPts.TPS = new int [1];
			this.bs.dataPts.SF = new short [1];
			this.bs.dataPts.TPS[0] = size;
			this.bs.dataPts.SF[0] = 1000;
			this.bs.dataPts.DSF = new int[1][size];
			for (this.i = 0; this.i < size; this.i++)
				this.bs.dataPts.DSF[0][this.i] = 65535 - this.idis.readIUnsignedShort();
		}
		catch (IOException e) { e.printStackTrace(); }
		return 1;
	}

	int fill_bellcore()
	{
		this.bs.addField(BellcoreStructure.GENPARAMS);
		this.bs.genParams.NW = wavelength;

		this.bs.addField(3);
		this.bs.supParams.OMID = id;

		this.bs.addField(4);
		this.bs.fxdParams.DTS = datetime;
		this.bs.fxdParams.UD = "mt"; //$NON-NLS-1$
		this.bs.fxdParams.AW = (short)(actualwavelength);
		this.bs.fxdParams.TPW = 1;
		this.bs.fxdParams.PWU = new short[1];
		this.bs.fxdParams.DS = new int [1];
		this.bs.fxdParams.NPPW = new int [1];
		this.bs.fxdParams.PWU[0] = (short)pulsewidth;
		this.bs.fxdParams.DS[0] = (int)(resolution * groupindex / 3d * 100d * 10000d/*points*/ * 1000d/*meters*/);
		this.bs.fxdParams.NPPW[0] = size;
		this.bs.fxdParams.GI = (int)(groupindex * 100000);
		this.bs.fxdParams.NAV = averages;
		this.bs.fxdParams.AR = (int)(resolution * size * groupindex / 3d * 100d * 1000d/*meters*/);

		this.bs.addField(9);
		this.bs.cksum.CSM = 0;

		this.bs.addField(1);
		this.bs.map.MRN = 100;
		this.bs.map.NB = 6;

		this.bs.map.B_id = new String[6];
		this.bs.map.B_rev = new int[6];
		this.bs.map.B_size = new int[6];

		this.bs.map.B_id[0] = "Map"; //$NON-NLS-1$
		this.bs.map.B_id[1] = "GenParams"; //$NON-NLS-1$
		this.bs.map.B_id[2] = "SupParams"; //$NON-NLS-1$
		this.bs.map.B_id[3] = "FxdParams"; //$NON-NLS-1$
		this.bs.map.B_id[4] = "DataPts"; //$NON-NLS-1$
		this.bs.map.B_id[5] = "Cksum"; //$NON-NLS-1$

		for (int i1 = 1; i1 < 6; i1++)
			this.bs.map.B_rev[i1] = 100;

		this.bs.map.B_size[1] = this.bs.genParams.getSize();
		this.bs.map.B_size[2] = this.bs.supParams.getSize();
		this.bs.map.B_size[3] = this.bs.fxdParams.getSize();
		this.bs.map.B_size[4] = this.bs.dataPts.getSize();
		this.bs.map.B_size[5] = this.bs.cksum.getSize();
		this.bs.map.MBS = this.bs.map.getSize();

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
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
