package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptySurveyDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDSurveyDataSource;

public class AnalyseApplicationModel extends ApplicationModel
{

	public AnalyseApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuExit");

		add("menuFileOpen");
		add("menuFileOpenAs");
		add("menuFileOpenAsBellcore");
		add("menuFileOpenAsWavetek");
		add("menuFileSave");
		add("menuFileSaveAll");
		add("menuFileSaveAs");
		add("menuFileSaveAsText");
		add("menuFileClose");
		add("menuFileAddCompare");
		add("menuFileRemoveCompare");
		add("menuExit");


		add("menuTrace");
		add("menuTraceAddCompare");
		add("menuTraceRemoveCompare");
		add("menuTraceDownload");
		add("menuTraceDownloadEtalon");
		add("menuTraceCloseEtalon");
		add("menuTraceClose");
		add("menuTraceReferenceClose");

		add("menuCreateTestSetup");
		add("menuSaveTestSetup");
		add("menuSaveTestSetupAs");
		add("menuAnalyseUpload");
		add("menuLoadTestSetup");
		add("menuTestSetup");
		add("menuNetStudy");

		add("menuOptions");
		add("menuOptionsColor");

		add("menuReport");
		add("menuReportCreate");

		add("menuWindow");
		add("menuWindowArrange");
		add("menuWindowTraceSelector");
		add("menuWindowPrimaryParameters");
		add("menuWindowOverallStats");
		add("menuWindowNoiseFrame");
		add("menuWindowFilteredFrame");
		add("menuWindowEvents");
		add("menuWindowDetailedEvents");
		add("menuWindowAnalysis");
		add("menuWindowMarkersInfo");
		add("menuWindowAnalysisSelection");
		add("menuWindowDerivHistoFrame");
		add("menuWindowThresholdsSelection");
		add("menuWindowThresholds");

		add("menuBar");
		add("toolBar");
		add("statusBar");
		add("reflectometryFrame");
	}

	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
			//return new RISDSurveyDataSource(si);
			return new RISDSurveyDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptySurveyDataSource(si);
		return null;
	}
}