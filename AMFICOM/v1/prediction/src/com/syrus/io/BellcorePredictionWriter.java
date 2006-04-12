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
		bs.dataPts.tps[0] = data.length;
		bs.dataPts.tndp = data.length;

		bs.dataPts.dsf = new int[1][data.length];
		for (int i = 0; i < data.length; i++) {
			bs.dataPts.dsf[0][i] = (int)(- 1000d * data[i]);
			if (bs.dataPts.dsf[0][i] < 0) {
				System.err.println("bs.dataPts.dsf is " + bs.dataPts.dsf[0][i] + "; set to 0");
				bs.dataPts.dsf[0][i] = 0; 
			}
		}
	}

	public void setTime(long time)
	{
		bs.addField(BellcoreStructure.FXDPARAMS);
		bs.fxdParams.dts = time;
	}
}