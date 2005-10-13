/*
 * $Id: PredictionReportModel.java,v 1.2 2005/10/13 06:21:41 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.prediction.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.analysis.report.AESMPReportModel;
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

public class PredictionReportModel extends AESMPReportModel
{
	// �������� ������ ��� ��������
	public static String TIME_DISTRIBUTION = "TimedGraphTitle";

	public static String STATISTICAL_DATA = "TimedTableTitle";
	
	public PredictionReportModel(){
	}

	@Override
	public String getName()	{
		return DestinationModules.PREDICTION;
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = super.getReportKind(reportName);
		if (reportName.equals(TIME_DISTRIBUTION))
			result = ReportType.GRAPH;
		return result;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(AESMPReportModel.COMMON_INFO);
		result.add(AESMPReportModel.REFLECTOGRAMM);
		result.add(AESMPReportModel.GENERAL_CHARACTERISTICS);
		
		result.add(PredictionReportModel.TIME_DISTRIBUTION);
		result.add(PredictionReportModel.STATISTICAL_DATA);

		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		String langReportName = super.getReportElementName(reportName);
		if (langReportName == null){
			if (	reportName.equals(TIME_DISTRIBUTION)
				||	reportName.equals(STATISTICAL_DATA))
				langReportName = I18N.getString("report.Modules.Prediction." + reportName);
		}
		return langReportName;
	}
}