package com.syrus.AMFICOM.Client.General.Model;

public class OptimizeApplicationModelFactory
		implements ApplicationModelFactory
{
	public OptimizeApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new OptimizeApplicationModel();
		return aModel;
	}
}