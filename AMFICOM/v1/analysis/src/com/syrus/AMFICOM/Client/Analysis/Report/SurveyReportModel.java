package com.syrus.AMFICOM.Client.Analysis.Report;

import java.util.Vector;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SurveyReportModel extends ESAPEReportModel
{
	public SurveyReportModel()
	{
	}

	public String getName()
	{
		return "surveyreportmodel";
	}

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repSurveyResults");
	}

	public String getLangForField(String field)
	{
		return LangModelAnalyse.getString(field);
	}

	public Vector getAvailableReports()
	{
		Vector result = new Vector();

		result.add(ESAPEReportModel.testParams);
		result.add(ESAPEReportModel.commonInfo);
		result.add(ESAPEReportModel.reflectogram);
		result.add(ESAPEReportModel.analysisParams);
		result.add(ESAPEReportModel.commonChars);
		result.add(ESAPEReportModel.noise_level);
		result.add(ESAPEReportModel.filtered_reflect);
		result.add(ESAPEReportModel.histogram);

		return result;
	}
}