package com.syrus.AMFICOM.Client.General.Model;

public class ModelApplicationModelFactory
		implements ApplicationModelFactory
{
	public ModelApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new ModelApplicationModel();
		return aModel;
	}
}