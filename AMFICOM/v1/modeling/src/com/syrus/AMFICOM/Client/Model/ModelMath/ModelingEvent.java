package com.syrus.AMFICOM.Client.Model.ModelMath;

public class ModelingEvent
{
	public static final int CONNECTOR = 3;
	public static final int LINEAR = 1;
	public static final int SPLICE = 2;

	public static final double wl_1310 = 49.0;
	public static final double wl_1550 = 52.0;
	public static final double wl_1625 = 52.8;

	public int type = 0;

	public double length; //km
	public double attenuation; // dB/km
	public double loss; //dB
	public double splash;  //dB
	private double connectorSigma = wl_1550;

	public void setConnector(double reflection, double loss)
	{
		type = CONNECTOR;
		this.loss = loss;
		this.splash = getSplash(reflection);
	}

	public void setWeld(double loss)
	{
		type = SPLICE;
		if(Math.random() > .9) // setting of the loss energy sign
			loss = -loss;
		this.loss = loss;
	}

	public void setLinear(double attenuation, double length)
	{
		type = LINEAR;
		this.attenuation = attenuation;
		this.length = length;
	}

	private double getSplash(double reflection)
	{
		reflection = -Math.abs(reflection);

		double ret = (reflection + connectorSigma) / 10.;
		ret = Math.pow(10, ret) + 1;
		ret = Math.log(ret) / Math.log(10);
		ret *= 5d;
		return ret;
	}
}