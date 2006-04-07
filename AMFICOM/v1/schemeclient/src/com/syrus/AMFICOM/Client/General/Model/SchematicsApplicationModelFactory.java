package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class SchematicsApplicationModelFactory
{
	public SchematicsApplicationModelFactory()
	{
		//Maybe nothing
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new SchematicsApplicationModel();
		return aModel;
	}
}
