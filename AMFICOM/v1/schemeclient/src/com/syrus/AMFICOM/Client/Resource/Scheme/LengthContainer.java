package com.syrus.AMFICOM.Client.Resource.Scheme;

public class LengthContainer
{
	public double physicalLength;
	public double opticalLength;
	public double Ku;
	public Object obj;
	public String objTyp = "";

	public LengthContainer(Object obj, double physicalLength, double opticalLength, String objTyp)
	{
		this.objTyp = objTyp;
		this.obj = obj;
		this.physicalLength = physicalLength;
		this.opticalLength = opticalLength;
		if(physicalLength > 0.)
			Ku = opticalLength / physicalLength;
		else
			Ku = 1.;
	}

	public LengthContainer(Object obj, double physicalLength, double opticalLength)
	{
		this.obj = obj;
		this.physicalLength = physicalLength;
		this.opticalLength = opticalLength;
		if(physicalLength > 0.)
			Ku = opticalLength / physicalLength;
		else
			Ku = 1.;
	}

}
