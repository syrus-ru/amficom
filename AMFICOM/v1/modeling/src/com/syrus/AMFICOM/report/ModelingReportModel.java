/*
 * $Id: ModelingReportModel.java,v 1.1 2006/04/03 08:20:30 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.Report.AESMPReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModelingReportModel extends AnalysisReportModel {
	// Названия таблиц для моделирования
	public static String MODELLING_PARAMETERS = "ParamsTitle";
	
	public ModelingReportModel() {
	}
	
	@Override
	public String getName()	{
		return DestinationModules.MODELING;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(AESMPReportModel.COMMON_INFO);
		result.add(AESMPReportModel.REFLECTOGRAMM);
		result.add(AESMPReportModel.GENERAL_CHARACTERISTICS);
		
		result.add(AnalysisReportModel.TEST_PARAMETERS);
		result.add(AnalysisReportModel.ANALYSIS_PARAMETERS);
		
		result.add(ModelingReportModel.MODELLING_PARAMETERS);

		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		String langReportName = super.getReportElementName(reportName);
		if (langReportName == null){
			if (reportName.equals(MODELLING_PARAMETERS))
				langReportName = I18N.getString("report.Modules.Modeling." + reportName);
		}
		return langReportName;
	}
}