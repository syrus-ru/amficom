package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class AdminApplicationModel extends ApplicationModel
{
	public AdminApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
//		add("menuSessionSave");
//		add("menuSessionUndo");
		add("menuExit");


    add("menuView");
    add("menuViewNavigator");
    add("menuViewOpenAll");
    add("menuViewWhoAmI");
    add("menuViewOpenObjectsWindow");

    add("menuArchitecture");
    add("menuArchitectureServer");
    add("menuArchitectureAgent");
    add("menuArchitectureClient");

    add("menuUser");
    add("menuUserCategory");
    add("menuUserGroup");
    add("menuUserProfile");


    add("menuAccess");
    add("menuAccessDomain");
    add("menuAccessModul");
    add("menuAccessMaintain");

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
			return new RISDObjectDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptyObjectDataSource(si);
		return null;

	}

}