package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public abstract class AnalyseApplicationModelFactory extends ApplicationModel
{

	public ApplicationModel create()
	{
		ApplicationModel aModel = new AnalyseApplicationModel();
		return aModel;
	}
}
