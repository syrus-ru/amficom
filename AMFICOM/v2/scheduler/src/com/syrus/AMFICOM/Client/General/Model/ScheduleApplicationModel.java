package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptySurveyDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDSurveyDataSource;

public class ScheduleApplicationModel extends ApplicationModel
{
	public ScheduleApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuSessionDomain");
		add("menuExit");
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