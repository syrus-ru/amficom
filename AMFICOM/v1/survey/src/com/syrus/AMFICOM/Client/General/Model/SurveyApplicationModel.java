package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SurveyApplicationModel extends ApplicationModel
{
	public SurveyApplicationModel()
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
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuExit");

		add("menuView");
		add("menuViewNavigator");
		add("menuViewMessages");
		add("menuViewToolbar");
		add("menuViewRefresh");
		add("menuViewCatalogue");
		add("menuViewAll");

		add("menuEvaluate");
		add("menuEvaluateRequest");
		add("menuEvaluateScheduler");
		add("menuEvaluateArchive");
		add("menuEvaluateTrack");
		add("menuEvaluateTrackRequest");
		add("menuEvaluateTrackTask");
		add("menuEvaluateResult");
		add("menuEvaluateAnalize");
		add("menuEvaluateAnalizeExt");
		add("menuEvaluateNorms");
		add("menuEvaluateModeling");
		add("menuEvaluatePrognosis");
		add("menuEvaluateViewAll");

		add("menuVisualize");
		add("menuVisualizeNet");
		add("menuVisualizeNetGIS");
		add("menuVisualizeISM");
		add("menuVisualizeISMGIS");
		add("menuVisualizeMapEdit");
		add("menuVisualizeMapClose");
		add("menuVisualizeSchemeEdit");
		add("menuVisualizeSchemeClose");

		add("menuViewMapSetup");

		add("menuMaintain");
		add("menuMaintainAlarm");
		add("menuMaintainAlert");
		add("menuMaintainCall");
		add("menuMaintainEvent");

		add("menuReport");
		add("menuReportTable");
		add("menuReportHistogramm");
		add("menuReportGraph");
		add("menuReportComplex");
		add("menuReportReport");

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
			return new RISDSurveyDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptySurveyDataSource(si);
		return null;
	}
}