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

		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", false);

		aModel.setVisible("menuSessionOptions", false);

		aModel.setVisible("menuSessionSave", false);
		aModel.setVisible("menuSessionUndo", false);

		aModel.setVisible("menuView", false);

		aModel.setVisible("menuEvaluateTrack", false);

		aModel.setUsable("menuVisualizeISM", false);
		aModel.setUsable("menuVisualizeISMGIS", false);

		aModel.setUsable("menuMaintainAlert", false);
		aModel.setUsable("menuMaintainCall", false);
		aModel.setUsable("menuMaintainEvent", false);

		aModel.setVisible("menuReport", false);
		aModel.setUsable("menuReportHistogramm", false);
		aModel.setUsable("menuReportGraph", false);
		aModel.setUsable("menuReportComplex", false);
		aModel.setUsable("menuReportReport", false);

		aModel.setInstalled("menuTools", false);

		aModel.setVisible("menuWindow", false);

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