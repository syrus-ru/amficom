package com.syrus.AMFICOM.Client.General.Model;

public class PredictionApplicationModelFactory implements ApplicationModelFactory
{

	public PredictionApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new PredictionApplicationModel();
		return aModel;
	}

}