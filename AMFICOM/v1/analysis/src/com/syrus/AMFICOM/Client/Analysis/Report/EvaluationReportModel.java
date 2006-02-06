package com.syrus.AMFICOM.Client.Analysis.Report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * Модель отчётов для модуля "Оценка"
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/10/14 12:00:48 $
 * @module reportother
 */
public class EvaluationReportModel extends AnalysisReportModel
{
	// Названия таблиц для измерений (дополнительно к анализу)
	/**
	 * Тип и параметры маски
	 */
	public static String MASK_TYPE_AND_PARAMETERS = "thresholdsSelectionFrame";
	/**
	 * Вид маски
	 */
	public static String MASK_VIEW = "thresholdsFrame";
	//TODO Здесь тоже есть дополнительная информация по event'ам.
	
	public EvaluationReportModel() {
	}

	@Override
	public String getName() {
		return DestinationModules.EVALUATION;
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = super.getReportKind(reportName);
		if (reportName.equals(EvaluationReportModel.MASK_VIEW))
			result = ReportType.GRAPH;
		return result;
	}
	
	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();
		
		result.add(AESMPReportModel.COMMON_INFO);
		result.add(AESMPReportModel.REFLECTOGRAMM);
		result.add(AESMPReportModel.GENERAL_CHARACTERISTICS);
		
		result.add(AnalysisReportModel.TEST_PARAMETERS);
		result.add(AnalysisReportModel.ANALYSIS_PARAMETERS);
		result.add(AnalysisReportModel.MARKER_DATA);		
		
		result.add(EvaluationReportModel.MASK_TYPE_AND_PARAMETERS);
		result.add(EvaluationReportModel.MASK_VIEW);

		return result;
	}

	@Override
	public String getReportElementName(String reportName) {
		String langReportName = super.getReportElementName(reportName);
		if (langReportName == null){
			if (	reportName.equals(MASK_TYPE_AND_PARAMETERS)
				||	reportName.equals(MASK_VIEW))
				langReportName = I18N.getString("report.Modules.Evaluation." + reportName);
		}
		return langReportName;
	}
}