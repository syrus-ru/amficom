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
public class SurveyReportModel extends AnalysisReportModel {
	//TODO Подумать, как передавать доп. характеристики event'ов
//	/**
//	 * Дополнительные характеристики
//	 */
//	public static String ADDITIONAL_CHARACTERISTICS = "eventDetailedTableTitle";

	@Override
	public String getName()	{
		return DestinationModules.SURVEY;
	}

	@Override
	public ReportType getReportKind(String reportName){
		if (reportName.equals(AnalysisResourceKeys.FRAME_NOISE)
			|| reportName.equals(AnalysisResourceKeys.FRAME_HISTOGRAMM)
			|| reportName.equals(AnalysisResourceKeys.FRAME_NOISE_HISTOGRAMM))
			return ReportType.GRAPH;
		return super.getReportKind(reportName);
	}
	
	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(AnalysisResourceKeys.FRAME_OVERALL_STATS);
		result.add(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN);
		result.add(AnalysisResourceKeys.FRAME_EVENTS);
		
		result.add(AnalysisResourceKeys.FRAME_PRIMARY_PARAMETERS);
		result.add(AnalysisResourceKeys.FRAME_ANALYSIS_SELECTION);
		result.add(AnalysisResourceKeys.FRAME_MARKERS_INFO);
		
		result.add(AnalysisResourceKeys.FRAME_NOISE);
		result.add(AnalysisResourceKeys.FRAME_HISTOGRAMM);
		result.add(AnalysisResourceKeys.FRAME_NOISE_HISTOGRAMM);

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