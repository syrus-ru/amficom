package com.syrus.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
		bs = new BellcoreStructure();
		bais = new ByteArrayInputStream(raw_data);
		idis = new IntelDataInputStream(bais);
		bac = new ByteArrayConverter(raw_data);
		//filesize = raw_data.length;

		search(); //1st
			read_params();
		search(); //2nd
			read_traceinfo();
		search(); //3rd
		search(); //4th
			read_trace();
		fill_bellcore();
		return bs;
	}

	int read_params()
	{
		// pos + 0x29 - (DOUBLE) длина трассы в км
		// pos + 0x33 - (short) длина волны
		// pos + 0x31 - (short) длительность импульса
		// pos + 0x35 - (dword) число усреднений
		// pos + 0x3d - (double) групповой индекс
		// pos + 0x4d - (long) время и дата
		// pos + 0x61 - (double) разрешение
		// pos + 0x71 - (double) ?
		// pos + 0x51 - (double) ?
		// pos + 0x79 - (string) имя оптического модуля
		try
		{
			averages = bac.readIInt(pos + 0x35);
			wavelength = bac.readIShort(pos + 0x33);
			actualwavelength = wavelength * 10;
			groupindex = bac.readIDouble(pos + 0x3D);
			datetime = bac.readIUnsignedInt(pos + 0x4d);
			pulsewidth = bac.readIShort(pos + 0x31);
			range = bac.readIDouble(pos + 0x29);
			id = bac.readIString(pos + 0x79);
		}
		catch (IOException e) { e.printStackTrace(); }
		return 1;
		}

	int read_traceinfo()
	{
		try
		{
			size = bac.readIUnsignedShort(pos + 0x6);
			resolution = range / (double)size;
		}
		catch (IOException e) { e.printStackTrace(); }
		return 1;
	}

	int read_trace()
	{
		bs.addField(7);
		try
		{
			bs.dataPts.TNDP = size;
			bs.dataPts.TSF = 1;
			bs.dataPts.TPS = new int [1];
			bs.dataPts.SF = new short [1];
			bs.dataPts.TPS[0] = size;
			bs.dataPts.SF[0] = 1000;
			bs.dataPts.DSF = new int[1][size];
			for (i = 0; i < size; i++)
				bs.dataPts.DSF[0][i] = 65535 - idis.readIUnsignedShort();
		}
		catch (IOException e) { e.printStackTrace(); }
		return 1;
	}

	int fill_bellcore()
	{
		bs.addField(BellcoreStructure.GENPARAMS);
		bs.genParams.NW = wavelength;

		bs.addField(3);
		bs.supParams.OMID = id;

		bs.addField(4);
		bs.fxdParams.DTS = datetime;
		bs.fxdParams.UD = "mt";
		bs.fxdParams.AW = (short)(actualwavelength);
		bs.fxdParams.TPW = 1;
		bs.fxdParams.PWU = new short[1];
		bs.fxdParams.DS = new int [1];
		bs.fxdParams.NPPW = new int [1];
		bs.fxdParams.PWU[0] = (short)pulsewidth;
		bs.fxdParams.DS[0] = (int)(resolution * groupindex / 3d * 100d * 10000d/*points*/ * 1000d/*meters*/);
		bs.fxdParams.NPPW[0] = size;
		bs.fxdParams.GI = (int)(groupindex * 100000);
		bs.fxdParams.NAV = averages;
		bs.fxdParams.AR = (int)(resolution * size * groupindex / 3d * 100d * 1000d/*meters*/);

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

		return 1;
	}

	private void search ()
	{
		try
		{
			flag = false;
			while (!flag)
			{
				if (idis.readIChar() == 167)
					if (idis.readIChar() == 40)
						if (idis.readIChar() == 2)
							if (idis.readIChar() == 21)
								flag = true;
				pos++;
			}
			pos += 3;
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
