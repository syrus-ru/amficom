package com.syrus.AMFICOM.Client.General.Model;

public class DefaultSurveyApplicationModelFactory
		extends SurveyApplicationModelFactory 
{
	public DefaultSurveyApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible("menuHelpContents", false);
		aModel.setVisible("menuHelpFind", false);
		aModel.setVisible("menuHelpTips", false);
		aModel.setVisible("menuHelpStart", false);
		aModel.setVisible("menuHelpCourse", false);
		aModel.setVisible("menuHelpHelp", false);
		aModel.setVisible("menuHelpSupport", false);
		aModel.setVisible("menuHelpLicense", false);

		return aModel;
	}
}

