package com.syrus.AMFICOM.Client.General.Model;

public class SurveyApplicationModelFactory
		implements ApplicationModelFactory
{
	public SurveyApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new SurveyApplicationModel();
		return aModel;
	}
}