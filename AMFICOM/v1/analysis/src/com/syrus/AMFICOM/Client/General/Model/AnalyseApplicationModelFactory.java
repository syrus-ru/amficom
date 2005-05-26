package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public abstract class AnalyseApplicationModelFactory
{

	public ApplicationModel create()
	{
		ApplicationModel aModel = new AnalyseApplicationModel();
		return aModel;
	}
}
