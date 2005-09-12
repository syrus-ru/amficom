package com.syrus.AMFICOM.client.analysis.report;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.report.DestinationModules;

/**
 * ������ ������� ��� ������ "������"
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/12 11:36:24 $
 * @module reportother
 */
public class SurveyReportModel extends AnalysisReportModel
{
	// �������� ������ ��� ������������ (������������� � �������)
	/**
	 * ������� ����� ������� �������
	 */
	public static String NOISE_LEVEL = "noiseFrame";
	/**
	 * ����������� ������� �����
	 */
	public static String NOISE_AREA_HISTOGRAMM = "noiseHistogrammFrame";
	/**
	 * ����������� ������� �������
	 */
	public static String WORK_AREA_HISTOGRAMM = "HistogrammFrame";
	
	//TODO ��������, ��� ���������� ���. �������������� event'��
//	/**
//	 * �������������� ��������������
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
		// TODO ������-��, ��� ���������� ������ ��������� �
		// ������ LangModel'�� �, ��������������, ������ ������
		//���� � ������� ������� - �����������
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