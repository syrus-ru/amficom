package com.syrus.AMFICOM.Client.General.Model;

public class DefaultAlarmApplicationModelFactory
		extends AlarmApplicationModelFactory 
{
	public DefaultAlarmApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("menuRead", true);
		aModel.setUsable("menuAcknowledge", true);
		aModel.setUsable("menuAssign", true);
		aModel.setUsable("menuFix", true);
		aModel.setUsable("menuDelete", true);

		return aModel;
	}
}

