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
		bs.dataPts.tps[0] = data.length;
		bs.dataPts.tndp = data.length;

		bs.dataPts.dsf = new int[1][data.length];
		for (int i = 0; i < data.length; i++)
			bs.dataPts.dsf[0][i] = (int)(65535 - 1000d * data[i]);
	}

	public void setTime(long time)
	{
		bs.fxdParams.dts = time;
	}

	public void setWavelength(int wavelength)
	{
		bs.addField(BellcoreStructure.GENPARAMS);
		bs.genParams.nw = (short)wavelength;
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.aw = (short)(10 * wavelength);
	}

	public void setPulseWidth(int pulsewidth)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.tpw = 1;
		bs.fxdParams.pwu = new short[1];
		bs.fxdParams.pwu[0] = (short)pulsewidth;
	}

	public void setAverages(int averages)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.nav = averages;
	}

	public void setOpticalModule(String omid)
	{
		bs.addField(BellcoreStructure.SUPPARAMS);
		bs.supParams.omid = omid;
	}

	public void setPathId(String pathId)
	{
		bs.addField(BellcoreStructure.SUPPARAMS);
		bs.supParams.ot = pathId;
	}

	public void setUnits(String units)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.ud = units;
	}

	public void setData(double[] data)
	{
		bs.addField(BellcoreStructure.DATAPOINTS);
		bs.dataPts.tndp = data.length;
		bs.dataPts.tsf = 1;
		bs.dataPts.tps = new int [1];
		bs.dataPts.sf = new short [1];
		bs.dataPts.tps[0] = data.length;
		bs.dataPts.sf[0] = 1000;
		bs.fxdParams.nppw = new int[1];
		bs.fxdParams.nppw[0] = data.length;
		bs.dataPts.dsf = new int[1][data.length];
		for (int i = 0; i < data.length; i++)
			bs.dataPts.dsf[0][i] = 65535 - (int)(data[i]*1000);
	}

	/**
	 * @param groupIndex
	 * @param resolution разрешение, выраженное в километрах (!) 
	 * @param range дистанция, которая, по-видимому, должна определяться как
	 *   resulution * data.length
	 *   (по-видимому, другие значения чреваты появлением внутренне противоречивой BellcoreStructure) 
	 */
	public void setRangeParameters(double groupIndex, double resolution, double range)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.gi = (int)(groupIndex * 100000);
		bs.fxdParams.ds = new int[1];
		bs.fxdParams.ds[0] = (int)(resolution * groupIndex / 3d * 100d * 10000d * 1000d);
		bs.fxdParams.ar = (int)(range * groupIndex / 3d * 100d * 1000d);
	}

	public void finalizeChanges()
	{
		bs.addField(BellcoreStructure.CKSUM);
		bs.cksum.csm = 0;

		bs.addField(BellcoreStructure.MAP);
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
	}
}