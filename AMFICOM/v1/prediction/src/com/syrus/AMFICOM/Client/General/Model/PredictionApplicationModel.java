package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptySurveyDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDSurveyDataSource;

public class PredictionApplicationModel extends ApplicationModel
{

	public PredictionApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionOpen");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");

//		add("menuSessionSave");
//		add("menuSessionUndo");
		add("menuExit");

		add("menuView");
		add("menuViewDataLoad");
		add("menuViewCountPrediction");
		add("menuTraceAddCompare");
		add("menuTraceRemoveCompare");
		add("menuViewSavePrediction");
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

	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
			return new RISDSurveyDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptySurveyDataSource(si);
		return null;
	}
}