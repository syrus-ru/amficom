package com.syrus.AMFICOM.Client.General.Model;

public class AlarmApplicationModelFactory
		implements ApplicationModelFactory
{
	public AlarmApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new AlarmApplicationModel();
		return aModel;
	}
}