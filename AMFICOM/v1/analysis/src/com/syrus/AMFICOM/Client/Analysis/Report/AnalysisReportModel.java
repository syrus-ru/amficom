package com.syrus.AMFICOM.Client.Analysis.Report;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AnalysisReportModel extends ESAPEReportModel
{
	public AnalysisReportModel()
	{
	}

	public String getName()
	{
		return "analysisreportmodel";
	}

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repAnalysisResults");
	}

	public String getLangForField(String field)
	{
		return LangModelAnalyse.getString(field);
	}

	public List getAvailableReports()
	{
		List result = new ArrayList();

		result.add(ESAPEReportModel.testParams);
		result.add(ESAPEReportModel.commonInfo);
		result.add(ESAPEReportModel.reflectogram);
		result.add(ESAPEReportModel.analysisParams);
		result.add(ESAPEReportModel.commonChars);

		return result;
	}

}