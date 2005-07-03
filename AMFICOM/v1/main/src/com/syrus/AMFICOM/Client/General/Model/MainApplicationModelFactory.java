package com.syrus.AMFICOM.Client.General.Model;

public abstract class MainApplicationModelFactory 
		implements ApplicationModelFactory
{
	public MainApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new MainApplicationModel();
		return aModel;
	}
}