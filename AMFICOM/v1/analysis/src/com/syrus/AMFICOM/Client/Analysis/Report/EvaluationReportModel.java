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

public class EvaluationReportModel extends ESAPEReportModel
{
	public EvaluationReportModel()
	{
	}

	public String getName()
	{
		return "evaluationreportmodel";
	}

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repEvaluationResults");
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
		result.add(ESAPEReportModel.mask_type);
		result.add(ESAPEReportModel.mask_view);

		return result;
	}
}