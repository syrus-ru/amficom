package com.syrus.AMFICOM.Client.General.Model;

public class DefaultScheduleApplicationModelFactory
		extends ScheduleApplicationModelFactory
{
	public DefaultScheduleApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible("menuSessionSave", false);
		aModel.setVisible("menuSessionUndo", false);
		aModel.setVisible("menuSessionOptions", false);

		return aModel;
	}
}