package com.syrus.io;

public class BellcorePredictionWriter
{
	private BellcoreStructure bs;

	public BellcorePredictionWriter(BellcoreStructure bs)
	{
		this.bs = bs;
	}

	public void setDataPoints(double[] data)
	{
		bs.addField(BellcoreStructure.DATAPOINTS);
		bs.dataPts.TPS[0] = data.length;
		bs.dataPts.TNDP = data.length;

		bs.dataPts.DSF = new int[1][data.length];
		for (int i = 0; i < data.length; i++)
			bs.dataPts.DSF[0][i] = (int)(65535 - 1000d * data[i]);
	}

	public void setTime(long time)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.DTS = time;
	}
}