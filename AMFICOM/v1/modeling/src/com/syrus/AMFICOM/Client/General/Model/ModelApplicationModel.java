package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
//import com.syrus.AMFICOM.Client.Resource.RISDSurveyDataSource;

public class ModelApplicationModel extends ApplicationModel
{
	public ModelApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionOpen");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuExit");

		add("menuView");
		add("menuViewMapOpen");
		add("menuViewMapEdit");
		add("menuViewMapClose");
		add("menuViewSchemeOpen");
		add("menuViewSchemeEdit");
		add("menuViewSchemeClose");
		add("menuViewPerformModeling");
		add("menuViewModelSave");
		add("menuViewModelLoad");

		add("menuFileOpen");
		add("menuFileOpenAs");
		add("menuFileOpenAsBellcore");
		add("menuFileOpenAsWavetek");
		add("menuFileSave");
		add("menuFileSaveAll");
		add("menuFileSaveAs");
		add("menuFileSaveAsText");
		add("menuFileClose");
		add("menuFileAddCompare");
		add("menuFileRemoveCompare");
		add("menuExit");

		add("menuTrace");
		add("menuTraceAddCompare");
		add("menuTraceRemoveCompare");
		add("menuTraceClose");

		add("menuReport");
		add("menuReportCreate");

		add("menuHelp");
		add("menuHelpContents");
		add("menuHelpFind");
		add("menuHelpTips");
		add("menuHelpStart");
		add("menuHelpCourse");
		add("menuHelpHelp");
		add("menuHelpSupport");
		add("menuHelpLicense");
		add("menuHelpAbout");
	}
/*
	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
			return new RISDSurveyDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptySurveyDataSource(si);
		return null;
	}*/

}