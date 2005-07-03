package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class SchematicsApplicationModelFactory
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
