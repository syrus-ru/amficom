package com.syrus.AMFICOM.Client.Model.Report;

import java.util.Vector;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.Analysis.Report.*;

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
	public Vector getAvailableReports()
	{
		Vector result = new Vector();

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