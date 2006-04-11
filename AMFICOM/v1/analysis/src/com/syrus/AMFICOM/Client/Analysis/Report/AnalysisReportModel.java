package com.syrus.AMFICOM.Client.Analysis.Report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * Модель отчётов для модуля "Анализ"
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2006/04/11 05:50:31 $
 * @module reportother
 */
public class AnalysisReportModel extends AESMPReportModel {
	@Override
	public String getName()	{
		return DestinationModules.ANALYSIS;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(AnalysisResourceKeys.FRAME_OVERALL_STATS); // AESMPReportModel.COMMON_INFO
		result.add(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN); // AESMPReportModel.REFLECTOGRAMM
		result.add(AnalysisResourceKeys.FRAME_EVENTS); // AESMPReportModel.GENERAL_CHARACTERISTICS
		
		result.add(AnalysisResourceKeys.FRAME_PRIMARY_PARAMETERS); // AnalysisReportModel.TEST_PARAMETERS
		result.add(AnalysisResourceKeys.FRAME_ANALYSIS_SELECTION); // AnalysisReportModel.ANALYSIS_PARAMETERS
		result.add(AnalysisResourceKeys.FRAME_MARKERS_INFO); //AnalysisReportModel.MARKER_DATA
		
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