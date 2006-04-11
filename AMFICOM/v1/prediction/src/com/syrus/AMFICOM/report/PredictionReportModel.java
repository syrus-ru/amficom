/*
 * $Id: PredictionReportModel.java,v 1.2 2006/04/11 05:53:57 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.resource.PredictionResourceKeys.FRAME_TIME_DEPENDANCE;
import static com.syrus.AMFICOM.resource.PredictionResourceKeys.FRAME_TIME_DEPENDANCE_TABLE;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.Report.AESMPReportModel;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.I18N;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PredictionReportModel extends AESMPReportModel {
	@Override
	public String getName()	{
		return DestinationModules.PREDICTION;
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = super.getReportKind(reportName);
		if (reportName.equals(FRAME_TIME_DEPENDANCE))
			result = ReportType.GRAPH;
		return result;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(AnalysisResourceKeys.FRAME_OVERALL_STATS);
		result.add(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN);
		result.add(AnalysisResourceKeys.FRAME_EVENTS);
		
		result.add(FRAME_TIME_DEPENDANCE);
		result.add(FRAME_TIME_DEPENDANCE_TABLE);

		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		String langReportName = super.getReportElementName(reportName);
		if (langReportName == null){
			langReportName = I18N.getString(reportName);
		}
		return langReportName;
	}
}