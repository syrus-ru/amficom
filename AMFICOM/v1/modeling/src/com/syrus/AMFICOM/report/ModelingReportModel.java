/*
 * $Id: ModelingReportModel.java,v 1.2 2006/04/11 05:52:23 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.resource.ModelResourceKeys;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModelingReportModel extends AnalysisReportModel {
	@Override
	public String getName()	{
		return DestinationModules.MODELING;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(AnalysisResourceKeys.FRAME_OVERALL_STATS);
		result.add(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN);
		result.add(AnalysisResourceKeys.FRAME_EVENTS);
		
		result.add(AnalysisResourceKeys.FRAME_PRIMARY_PARAMETERS);
		result.add(AnalysisResourceKeys.FRAME_ANALYSIS_SELECTION);
		
		result.add(ModelResourceKeys.FRAME_TRANS_DATA);

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