package com.syrus.AMFICOM.Client.General.Model;

public abstract class AnalyseApplicationModelFactory implements ApplicationModelFactory
{

	public AnalyseApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new AnalyseApplicationModel();
		return aModel;
	}
}