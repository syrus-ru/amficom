package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class ResultApplicationModel extends ApplicationModel 
{
	public ResultApplicationModel()
	{
		super();
		
		add("menuRead");
		add("menuAcknowledge");
		add("menuAssign");
		add("menuFix");
		add("menuDelete");
	}

	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
//			return new EmptySurveyDataSource(si);
			return new RISDSurveyDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptySurveyDataSource(si);
		return null;
	}
}