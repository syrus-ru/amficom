package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;
import com.syrus.AMFICOM.Client.Resource.*;

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
		add("menuPathAutoCreate");
		add("menuPathDelete");
		add("menuPathCancel");

		add("menuReportCreate");
		add("menuReport");

		add("menuWindow");
		add("menuWindowArrange");
		add("menuWindowTree");
		add("menuWindowScheme");
		add("menuWindowCatalog");
		add("menuWindowUgo");
		add("menuWindowProps");
		add("menuWindowList");
	}

	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
			return new RISDMapDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptyConfigDataSource(si);
		return null;
	}
}
