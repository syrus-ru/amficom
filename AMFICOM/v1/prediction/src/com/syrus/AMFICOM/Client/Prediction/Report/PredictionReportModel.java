package com.syrus.AMFICOM.Client.Prediction.Report;
/*
import java.util.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.Analysis.Report.*;
*/

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

/*
public class PredictionReportModel extends ESAPEReportModel
{
	public PredictionReportModel()
	{
	}
	public List getAvailableReports()
	{
		List result = new ArrayList(7);

		result.add(ESAPEReportModel.testParams);
		result.add(ESAPEReportModel.commonInfo);
		result.add(ESAPEReportModel.reflectogram);
		result.add(ESAPEReportModel.commonChars);
		result.add(ESAPEReportModel.pred_stat_data);
		result.add(ESAPEReportModel.pred_time_distrib);
		result.add(ESAPEReportModel.histogram);

		return result;
	}

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repPredictionResults");
	}

	public String getLangForField(String field)
	{
		String strToReturn = "";
		try
		{
			if (field.equals(ESAPEReportModel.pred_stat_data)
				|| field.equals(ESAPEReportModel.pred_time_distrib))
				strToReturn = LangModelPrediction.getString(field);
			else
				strToReturn = LangModelAnalyse.getString(field);
		}
		catch(Exception exc)
		{
			strToReturn = field;
		}

		return strToReturn;
	}
}*/