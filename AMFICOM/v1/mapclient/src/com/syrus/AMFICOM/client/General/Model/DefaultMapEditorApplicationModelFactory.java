package com.syrus.AMFICOM.Client.General.Model;

public class DefaultMapEditorApplicationModelFactory
		extends MapEditorApplicationModelFactory 
{
	public DefaultMapEditorApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible("menuSessionOptions", false);

		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", true);

//		aModel.setVisible("menuMapSaveAs", false);
		aModel.setVisible("menuMapOptions", false);
		aModel.setVisible("menuMapCatalogue", false);

//		aModel.setVisible("menuViewAll", false);

		aModel.setUsable("menuHelpContents", false);
		aModel.setUsable("menuHelpFind", false);
		aModel.setUsable("menuHelpTips", false);
		aModel.setUsable("menuHelpStart", false);
		aModel.setUsable("menuHelpCourse", false);
		aModel.setUsable("menuHelpHelp", false);
		aModel.setUsable("menuHelpSupport", false);
		aModel.setUsable("menuHelpLicense", false);

		return aModel;
	}
}