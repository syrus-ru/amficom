package com.syrus.AMFICOM.Client.General.Model;

public class DefaultSchematicsApplicationModelFactory
		extends SchematicsApplicationModelFactory
{
	public DefaultSchematicsApplicationModelFactory()
	{
	}

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
