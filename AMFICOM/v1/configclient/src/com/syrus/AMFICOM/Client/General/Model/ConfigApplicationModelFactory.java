package com.syrus.AMFICOM.Client.General.Model;

public class ConfigApplicationModelFactory
		implements ApplicationModelFactory
{
	public ConfigApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new ConfigApplicationModel();
		return aModel;
	}
}