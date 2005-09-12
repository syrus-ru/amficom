package com.syrus.AMFICOM.client.analysis.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * Модель отчётов для модуля "Анализ"
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/12 11:36:24 $
 * @module reportother
 */
public class SurveyReportModel extends AnalysisReportModel
{
	// Названия таблиц для исследований (дополнительно к анализу)
	/**
	 * Уровень шумов рабочей области
	 */
	public static String NOISE_LEVEL = "noiseFrame";
	/**
	 * Гистограмма области шумов
	 */
	public static String NOISE_AREA_HISTOGRAMM = "noiseHistogrammFrame";
	/**
	 * Гистограмма рабочей области
	 */
	public static String WORK_AREA_HISTOGRAMM = "HistogrammFrame";
	
	//TODO Подумать, как передавать доп. характеристики event'ов
//	/**
//	 * Дополнительные характеристики
//	 */
//	public static String ADDITIONAL_CHARACTERISTICS = "eventDetailedTableTitle";
	
	public SurveyReportModel() {
	}

	@Override
	public String getName()	{
		return DestinationModules.SURVEY;
	}

	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = super.getReportKind(reportName);
		if (reportName.equals(NOISE_LEVEL)
			|| reportName.equals(NOISE_AREA_HISTOGRAMM)
			|| reportName.equals(WORK_AREA_HISTOGRAMM))
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
		
		result.add(SurveyReportModel.NOISE_LEVEL);
		result.add(SurveyReportModel.NOISE_AREA_HISTOGRAMM);
		result.add(SurveyReportModel.WORK_AREA_HISTOGRAMM);

		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		// TODO Вообще-то, эта информация должна храниться в
		// других LangModel'ах и, соответственно, методы должны
		//быть в моделях отчётов - наследницах
		String langReportName = super.getReportElementName(reportName);
		if (langReportName == null){
			if (	reportName.equals(NOISE_LEVEL)
				||	reportName.equals(NOISE_AREA_HISTOGRAMM)
				||	reportName.equals(WORK_AREA_HISTOGRAMM))
				langReportName = LangModelReport.getString("report.Modules.Survey." + reportName);
		}
		return langReportName;
	}
}