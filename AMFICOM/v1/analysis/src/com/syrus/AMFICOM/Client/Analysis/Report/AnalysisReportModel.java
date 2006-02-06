package com.syrus.AMFICOM.Client.Analysis.Report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * Модель отчётов для модуля "Анализ"
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/10/14 12:00:48 $
 * @module reportother
 */
public class AnalysisReportModel extends AESMPReportModel
{
	// Названия элементов шаблона для модуля "Анализ"
	/**
	 * Параметры теста
	 */
	public static String TEST_PARAMETERS = "paramFrame";
	/**
	 * Параметры анализа
	 */
	public static String ANALYSIS_PARAMETERS = "anaSelectFrame";
	/**
	 * Данные по маркерам
	 */
	public static String MARKER_DATA = "mInfoFrame";
	
	public AnalysisReportModel() {
	}

	@Override
	public String getName()	{
		return DestinationModules.ANALYSIS;
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
		
		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		String langReportName = super.getReportElementName(reportName);
		if (langReportName == null){
			if (	reportName.equals(TEST_PARAMETERS)
				||	reportName.equals(ANALYSIS_PARAMETERS)
				||	reportName.equals(MARKER_DATA))
				langReportName = I18N.getString("report.Modules.Analysis." + reportName);
		}
		return langReportName;
	}
}