package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class OptimizeApplicationModel extends ApplicationModel
{
	public OptimizeApplicationModel()
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

		add("menuScheme");
		add("menuMapOpen");
		add("menuSchemeOpen");
		add("menuSchemeSave");
		add("menuSchemeSaveAs");
    add("menuLoadSm");
    add("menuClearScheme");

		add("menuView");
		add("menuViewMap");
		add("menuViewMapElProperties");
    add("menuViewSchElProperties");
		add("menuViewScheme");
		add("menuViewKIS");
		add("menuViewGraph");
		add("menuViewSolution");
		add("menuViewParams");
		add("menuViewMode");
		add("menuViewShowall");

		add("menuOptimize");
    add("menuOptimizeCriteria");
    add("menuOptimizeCriteriaPrice");
    add("menuOptimizeCriteriaPriceLoad");
    add("menuOptimizeCriteriaPriceSave");
    add("menuOptimizeCriteriaPriceSaveas");
    add("menuOptimizeCriteriaPriceClose");
    add("menuOptimizeMode");
    add("menuOptimizeModeUnidirect");
    add("menuOptimizeModeBidirect");
		add("menuOptimizeStart");
		add("menuOptimizeStop");

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

    add("mapActionViewProperties");// добавлено в соответствии с
    add("mapActionEditProperties");// письмом јндре€ от 24 сент€бр€ 2003

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