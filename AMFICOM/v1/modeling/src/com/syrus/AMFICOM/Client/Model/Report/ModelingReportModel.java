package com.syrus.AMFICOM.Client.Model.Report;

import java.util.*;

import com.syrus.AMFICOM.Client.Analysis.Report.ESAPEReportModel;
import com.syrus.AMFICOM.Client.General.Lang.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModelingReportModel extends ESAPEReportModel
{
	public ModelingReportModel()
	{
	}
	public List getAvailableReports()
	{
		List result = new ArrayList(3);

		result.add(ESAPEReportModel.testParams);
		result.add(ESAPEReportModel.model_params);
		result.add(ESAPEReportModel.reflectogram);

		return result;
	}
	public String getObjectsName()
	{
		return LangModelReport.getString("label_repModelingResults");
	}

	public String getLangForField(String field)
	{
		String strToReturn = "";
		try
		{
			if (field.equals(ESAPEReportModel.model_params))
				strToReturn = LangModelModel.getString(field);
			else
				strToReturn = LangModelAnalyse.getString(field);
		}
		catch(Exception exc)
		{
			strToReturn = field;
		}

		return strToReturn;

	}
}