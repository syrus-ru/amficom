package com.syrus.AMFICOM.Client.General.Model;

public abstract class AnalyseApplicationModelFactory implements ApplicationModelFactory
{

	public ApplicationModel create()
	{
		ApplicationModel aModel = new AnalyseApplicationModel();
		return aModel;
	}
}
