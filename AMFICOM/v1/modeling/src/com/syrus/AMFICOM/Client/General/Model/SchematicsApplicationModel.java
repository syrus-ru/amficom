package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyConfigDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDConfigDataSource;

public class SchematicsApplicationModel extends ApplicationModel
{
	public SchematicsApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuExit");

		add("menuBar");
		add("toolBar");
		add("statusBar");

		add("menuScheme");
		add("menuSchemeNew");
		add("menuSchemeLoad");
		add("menuSchemeSave");
		add("menuSchemeSaveAs");
		add("menuInsertToCatalog");
		add("menuSchemeImport");
		add("menuSchemeExport");

		add("menuComponent");
		add("menuComponentSave");
		add("menuComponentNew");

		add("menuPath");
		add("menuPathNew");
		add("menuPathSave");
		add("menuPathEdit");
		add("menuPathAddStart");
		add("menuPathAddEnd");
		add("menuPathAddLink");
		add("menuPathRemoveLink");
		add("menuPathUpdateLink");
		add("menuPathDelete");
		add("menuPathCancel");

		add("menuWindow");
		add("menuWindowArrange");
		add("menuWindowTree");
		add("menuWindowScheme");
		add("menuWindowUgo");
		add("menuWindowProps");
		add("menuWindowList");
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
