package com.syrus.AMFICOM.Client.General.Model;

public class SchematicsApplicationModelFactory
		implements ApplicationModelFactory
{
	public SchematicsApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new SchematicsApplicationModel();
		return aModel;
	}
}
