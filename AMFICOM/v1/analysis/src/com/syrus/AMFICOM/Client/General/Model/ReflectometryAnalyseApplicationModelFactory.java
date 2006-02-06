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
		aModel.setVisible(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS, false);
		aModel.setVisible(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH, false);
				
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_ALL, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_AS, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE, false);

		//aModel.setVisible("menuReport", false);
		//aModel.setVisible("menuReportCreate", false);

		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE, false);
		aModel.setEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE, false);
//		aModel.setEnabled("menuTraceReferenceMakeCurrent", false);
//		aModel.setEnabled("menuTraceCurrent", false);
//		aModel.setEnabled("menuTraceThreshold", false);
//		aModel.setEnabled("menuTraceThresholdReload", false);
//		aModel.setEnabled("menuTraceThresholdMakeReference", false);

		return aModel;
	}
}
