package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class MainApplicationModel extends ApplicationModel 
{
	public MainApplicationModel()
	{
		super();
		
		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuExit");

		add("menuView");
		add("menuViewPanel");

		add("menuTools");
		add("menuToolsAdmin");
		add("menuToolsConfig");
		add("menuToolsComponents");
		add("menuToolsScheme");
		add("menuToolsMap");
		add("menuToolsTrace");
		add("menuToolsSchedule");
		add("menuToolsSurvey");
		add("menuToolsModel");
		add("menuToolsMonitor");
		add("menuToolsAnalyse");
		add("menuToolsNorms");
		add("menuToolsPrognosis");
		add("menuToolsMaintain");
		add("menuToolsReportBuilder");

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
			return new RISDDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptyDataSource(si);
		return null;
	}
}