package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class SurveyApplicationModelFactory
{
	public SurveyApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		new SchematicsApplicationModel();
		new AnalyseApplicationModel();
		ApplicationModel aModel = new SurveyApplicationModel();
		return aModel;
	}
}

