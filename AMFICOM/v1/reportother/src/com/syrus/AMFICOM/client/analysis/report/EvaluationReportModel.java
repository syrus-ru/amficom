package com.syrus.AMFICOM.client.analysis.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * Модель отчётов для модуля "Оценка"
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/12 11:36:24 $
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
		// TODO Вообще-то, эта информация должна храниться в
		// других LangModel'ах и, соответственно, методы должны
		//быть в моделях отчётов - наследницах
		String langReportName = super.getReportElementName(reportName);
		if (langReportName == null){
			if (	reportName.equals(MASK_TYPE_AND_PARAMETERS)
				||	reportName.equals(MASK_VIEW))
				langReportName = LangModelReport.getString("report.Modules.Evaluation." + reportName);
		}
		return langReportName;
	}
}