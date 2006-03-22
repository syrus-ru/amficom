package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

public abstract class AnalyseApplicationModelFactory
{

	public ApplicationModel create()
	{
		ApplicationModel aModel = new AnalyseApplicationModel();
		return aModel;
	}
}
