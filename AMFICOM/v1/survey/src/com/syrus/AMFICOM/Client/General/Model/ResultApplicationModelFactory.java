package com.syrus.AMFICOM.Client.General.Model;

public class ResultApplicationModelFactory
		implements ApplicationModelFactory
{
	public ResultApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new ResultApplicationModel();
		return aModel;
	}
}