package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class ConfigApplicationModel extends ApplicationModel 
{
	public ConfigApplicationModel()
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
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuSessionDomain");
		add("menuExit");

		add("menuView");
		add("menuViewNavigator");
		add("menuViewMessages");
		add("menuViewToolbar");
		add("menuViewAttributes");
		add("menuViewElements");
		add("menuViewMapScheme");
		add("menuViewRefresh");
		add("menuViewCatalogue");
		add("menuViewAll");

		add("menuScheme");
		add("menuSchemeMap");
		add("menuSchemeMapBitmaps");
		add("menuSchemeMapIcons");
		add("menuSchemeMapStyle");
		add("menuSchemeMapGIS");
		add("menuSchemeMapCoord");
		add("menuSchemeNet");
		add("menuSchemeNetScheme");
		add("menuSchemeNetAttribute");
		add("menuSchemeNetElType");
		add("menuSchemeNetElement");
		add("menuSchemeNetCatalogue");
		add("menuSchemeNetOpen");
		add("menuSchemeNetOpenScheme");
		add("menuSchemeNetView");
		add("menuSchemeJ");
		add("menuSchemeJScheme");
		add("menuSchemeJAttribute");
		add("menuSchemeJElType");
		add("menuSchemeJElement");
		add("menuSchemeJLayout");
		add("menuSchemeJCatalogue");
		add("menuSchemeJOpen");

		add("menuObject");
	    add("menuObjectNavigator");
		add("menuObjectDomain");
		add("menuObjectNetDir");
		add("menuObjectNetCat");
		add("menuObjectJDir");
		add("menuObjectJCat");

		add("menuNet");
		add("menuNetDir");
		add("menuNetDirAddress");
		add("menuNetDirResource");
		add("menuNetDirEquipment");
		add("menuNetDirProtocol");
		add("menuNetDirLink");
		add("menuNetDirTechnology");
		add("menuNetDirInterface");
		add("menuNetDirPort");
		add("menuNetDirStack");
		add("menuNetCat");
		add("menuNetCatEquipment");
		add("menuNetCatLink");
		add("menuNetCatCable");
		add("menuNetCatResource");
		add("menuNetCatTPGroup");
		add("menuNetCatTestPoint");
		add("menuNetLocation");

		add("menuJ");
		add("menuJDir");
		add("menuJDirKIS");
		add("menuJDirAccessPoint");
		add("menuJDirLink");
		add("menuJCat");
		add("menuJCatKIS");
		add("menuJCatAccessPoint");
		add("menuJCatPath");
		add("menuJCatResource");
		add("menuJInstall");

		add("menuMaintain");
		add("menuMaintainType");
		add("menuMaintainEvent");
		add("menuMaintainAlarmRule");
		add("menuMaintainMessageRule");
		add("menuMaintainAlertRule");
		add("menuMaintainReactRule");
		add("menuMaintainRule");
		add("menuMaintainCorrectRule");

		add("menuWindow");
		add("menuWindowClose");
		add("menuWindowCloseAll");
		add("menuWindowTileHorizontal");
		add("menuWindowTileVertical");
		add("menuWindowCascade");
		add("menuWindowArrange");
		add("menuWindowArrangeIcons");
		add("menuWindowMinimizeAll");
		add("menuWindowRestoreAll");
		add("menuWindowList");

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
			return new RISDConfigDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptyConfigDataSource(si);
		return null;
	}
}