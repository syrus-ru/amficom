package com.syrus.AMFICOM.Client.General.Model;

public class DefaultPredictionApplicationModelFactory
										 extends PredictionApplicationModelFactory
{

	public DefaultPredictionApplicationModelFactory()
	{
	}

			public ApplicationModel create()
			{
				ApplicationModel aModel = super.create();

				aModel.setUsable("menuSessionSave", false);
				aModel.setUsable("menuSessionUndo", false);
				aModel.setVisible("menuSessionOptions", false);

				aModel.setUsable("menuHelpContents", false);
				aModel.setUsable("menuHelpFind", false);
				aModel.setUsable("menuelpTips", false);
				aModel.setUsable("menuHelpStart", false);
				aModel.setUsable("menuHelpCourse", false);
				aModel.setUsable("menuHelpHelp", false);
				aModel.setUsable("menuHelpSupport", false);
				aModel.setUsable("menuHelpLiecnse", false);

				return aModel;
			}

}