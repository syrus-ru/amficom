package com.syrus.io;

public class BellcoreModelWriter
{
	private BellcoreStructure bs;

	public BellcoreModelWriter(BellcoreStructure bs)
	{
		this.bs = bs;
	}

	public void setDataPoints(double[] data)
	{
		bs.dataPts.TPS[0] = data.length;
		bs.dataPts.TNDP = data.length;

		bs.dataPts.DSF = new int[1][data.length];
		for (int i = 0; i < data.length; i++)
			bs.dataPts.DSF[0][i] = (int)(65535 - 1000d * data[i]);
	}

	public void setTime(long time)
	{
		bs.fxdParams.DTS = time;
	}

	public void setWavelength(int wavelength)
	{
		bs.addField(BellcoreStructure.GENPARAMS);
		bs.genParams.NW = (short)wavelength;
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.AW = (short)(10 * wavelength);
	}

	public void setPulseWidth(int pulsewidth)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.TPW = 1;
		bs.fxdParams.PWU = new short[1];
		bs.fxdParams.PWU[0] = (short)pulsewidth;
	}

	public void setAverages(int averages)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.NAV = averages;
	}

	public void setOpticalModule(String omid)
	{
		bs.addField(BellcoreStructure.SUPPARAMS);
		bs.supParams.OMID = omid;
	}

	public void setPathId(String pathId)
	{
		bs.addField(BellcoreStructure.SUPPARAMS);
		bs.supParams.OT = pathId;
	}

	public void setUnits(String units)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.UD = units;
	}

	public void setData(double[] data)
	{
		bs.addField(BellcoreStructure.DATAPOINTS);
		bs.dataPts.TNDP = data.length;
		bs.dataPts.TSF = 1;
		bs.dataPts.TPS = new int [1];
		bs.dataPts.SF = new short [1];
		bs.dataPts.TPS[0] = data.length;
		bs.dataPts.SF[0] = 1000;
		bs.fxdParams.NPPW = new int[1];
		bs.fxdParams.NPPW[0] = data.length;
		bs.dataPts.DSF = new int[1][data.length];
		for (int i = 0; i < data.length; i++)
			bs.dataPts.DSF[0][i] = 65535 - (int)(data[i]*1000);
	}

	public void setRangeParameters(double groupIndex, double resolution, double range)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.GI = (int)(groupIndex * 100000);
		bs.fxdParams.DS = new int[1];
		bs.fxdParams.DS[0] = (int)(resolution * groupIndex / 3d * 100d * 10000d * 1000d);
		bs.fxdParams.AR = (int)(range * groupIndex / 3d * 100d * 1000d);
	}

	public void finalizeChanges()
	{
		bs.addField(BellcoreStructure.CKSUM);
		bs.cksum.CSM = 0;

		bs.addField(BellcoreStructure.MAP);
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
	}
}