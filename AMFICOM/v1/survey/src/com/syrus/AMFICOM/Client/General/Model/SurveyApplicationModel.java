package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SurveyApplicationModel extends ApplicationModel
{
	public SurveyApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionChangePassword");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionDomain");
		add("menuExit");

		add("menuStart");
		add("menuStartScheduler");
		add("menuStartAnalize");
		add("menuStartAnalizeExt");
		add("menuStartEvaluation");
		add("menuStartPrognosis");

		add("menuView");
		add("menuViewMapOpen");
		add("menuViewMapEditor");
		add("menuViewMapClose");
		add("menuViewSchemeOpen");
		add("menuViewSchemeEditor");
		add("menuViewSchemeClose");
		add("menuViewMeasurements");
		add("menuViewResults");
		add("menuViewAlarms");
		add("menuViewMapSetup");
		add("menuViewAll");

		add("menuReport");
		add("menuTemplateReport");

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

	private static DataSourceInterface dataSource = null;
	
	public DataSourceInterface getDataSource(final SessionInterface session) 
	{
		String connection = Environment.getConnectionType();
        if ((this.session == null) || (!this.session.equals(session)))
			synchronized (this) 
			{
					if ((this.session == null) || (!this.session.equals(session))) 
					{
						this.session = session;
						if(connection.equalsIgnoreCase(Environment.CONNECTION_RISD))
							this.dataSource = new RISDSurveyDataSource(this.session);
						else
						if(connection.equalsIgnoreCase(Environment.CONNECTION_EMPTY))
							this.dataSource = new EmptySurveyDataSource(this.session);
					}
			}
        return this.dataSource;
	}
}

