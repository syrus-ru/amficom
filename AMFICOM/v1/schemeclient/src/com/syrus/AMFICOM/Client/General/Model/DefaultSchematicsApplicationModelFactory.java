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
		aModel.setVisible("menuSchemeLoad", false);
					
		return aModel;
	}
}
