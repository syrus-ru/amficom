package com.syrus.AMFICOM.Client.General.Model;

public class ReflectometryAnalyseApplicationModelFactory
																			extends AnalyseApplicationModelFactory
{

	public ReflectometryAnalyseApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible("menuFileSaveAll", false);
		aModel.setVisible("menuSessionSave", false);
		aModel.setVisible("menuSessionUndo", false);
		aModel.setVisible("menuSessionOptions", false);

		aModel.setVisible("menuTraceCloseEtalon", false);

		aModel.setEnabled("menuFileSave", false);
		aModel.setEnabled("menuFileSaveAs", false);
		aModel.setEnabled("menuFileSaveAsText", false);
		aModel.setEnabled("menuFileClose", false);

		//aModel.setVisible("menuReport", false);
		//aModel.setVisible("menuReportCreate", false);

		aModel.setEnabled("menuTrace", false);
		aModel.setEnabled("menuTraceRemoveCompare", false);
		aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
		aModel.setEnabled("menuTraceCurrent", false);
		aModel.setEnabled("menuTraceThreshold", false);
		aModel.setEnabled("menuTraceThresholdReload", false);
		aModel.setEnabled("menuTraceThresholdMakeReference", false);

		return aModel;
	}
}