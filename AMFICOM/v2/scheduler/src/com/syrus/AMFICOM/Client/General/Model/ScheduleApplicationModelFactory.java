package com.syrus.AMFICOM.Client.General.Model;

public class ScheduleApplicationModelFactory
		implements ApplicationModelFactory
{
	public ScheduleApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new ScheduleApplicationModel();
		return aModel;
	}
}