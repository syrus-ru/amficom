package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class MapEditorApplicationModel extends ApplicationModel 
{
	public MapEditorApplicationModel()
	{
		super();
		
		add("mapActionViewProperties");
		add("mapActionEditProperties");

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuExit");

		add("menuView");
		add("menuViewNavigator");
		add("menuViewAttributes");
		add("menuViewElements");
		add("menuViewSetup");
		add("menuViewMap");
		add("menuViewMapScheme");
		add("menuViewAll");

		add("menuMap");
		add("menuMapNew");
		add("menuMapOpen");
		add("menuMapClose");
		add("menuMapSave");
		add("menuMapSaveAs");
		add("menuMapOptions");
		add("menuMapCatalogue");

		add("menuReport");
		add("menuReportOpen");

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
			return new RISDMapDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptyMapDataSource(si);
		return null;
	}
}