package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class ReflectometryAnalyseApplicationModelFactory
		extends AnalyseApplicationModelFactory
{

	public ReflectometryAnalyseApplicationModelFactory()
	{
	}

	@Override
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

//		aModel.setVisible("menuSessionSave", false);
//		aModel.setVisible("menuSessionUndo", false);
//		aModel.setVisible("menuSessionOptions", false);

//		aModel.setVisible("menuTraceCloseEtalon", false);
		aModel.setVisible("menuTraceSavePES", false);
				
		aModel.setEnabled("menuFileSave", false);
		aModel.setEnabled("menuFileSaveAll", false);
		aModel.setEnabled("menuFileSaveAs", false);
		aModel.setEnabled("menuFileSaveAsText", false);
		aModel.setEnabled("menuFileClose", false);

		//aModel.setVisible("menuReport", false);
		//aModel.setVisible("menuReportCreate", false);

		aModel.setEnabled("menuTrace", false);
		aModel.setEnabled("menuTraceRemoveCompare", false);
//		aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
//		aModel.setEnabled("menuTraceCurrent", false);
//		aModel.setEnabled("menuTraceThreshold", false);
//		aModel.setEnabled("menuTraceThresholdReload", false);
//		aModel.setEnabled("menuTraceThresholdMakeReference", false);

		return aModel;
	}
}
