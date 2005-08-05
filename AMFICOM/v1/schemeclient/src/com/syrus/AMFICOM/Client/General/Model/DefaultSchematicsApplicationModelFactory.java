package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class DefaultSchematicsApplicationModelFactory
		extends SchematicsApplicationModelFactory
{
	public DefaultSchematicsApplicationModelFactory()
	{
		//Maybe nothing
	}

	@Override
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible("menuSessionSave", false);
		aModel.setVisible("menuSessionUndo", false);
		aModel.setVisible("menuSessionOptions", false);
//    aModel.setUsable("menuRead", true);

		return aModel;
	}
}
