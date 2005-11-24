package com.syrus.AMFICOM.Client.Model.Report;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.Client.Analysis.Report.AESMPReportModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModelingReportModel extends AESMPReportModel {
	@Override
	public List<String> getTemplateElementNames() {
		List<String> result = new ArrayList<String>(3);

//		result.add(ESAPEReportModel.testParams);
//		result.add(ESAPEReportModel.model_params);
		result.add(AESMPReportModel.REFLECTOGRAMM);

		return result;
	}
	@Override
	public String getName() {
		return "";
		//LangModelReport.getString("label_repModelingResults");
	}

	public String getLangForField(String field)
	{
		String strToReturn = "";
		try
		{
//			if (field.equals(ESAPEReportModel.model_params))
//				strToReturn = LangModelModel.getString(field);
//			else
				strToReturn = LangModelAnalyse.getString(field);
		}
		catch(Exception exc)
		{
			strToReturn = field;
		}

		return strToReturn;

	}
}