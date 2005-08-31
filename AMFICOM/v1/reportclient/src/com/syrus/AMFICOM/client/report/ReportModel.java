package com.syrus.AMFICOM.client.report;

import java.util.Collection;
import java.util.Map;

import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * <p>Title: </p>
 * <p>Description: ������� ����� ������, �������� ������� ��
 * ���������� ������� �� ���������� ��������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public abstract class ReportModel
{
	public static char REPORT_NAME_DIVIDER = ':';
	
	private static String TABLE_REPORT_PATH = "images/table_report";
	private static String SCHEME_REPORT_PATH = "images/scheme";
	private static String GRAPH_REPORT_PATH = "images/graph_report";	
	
	public static enum ReportType {SCHEMA,GRAPH,TABLE}
	
	public static String getIconForReportType(ReportType type){
		if (type.equals(ReportType.TABLE))
			return TABLE_REPORT_PATH;

		if (type.equals(ReportType.SCHEMA))
			return SCHEME_REPORT_PATH;

		if (type.equals(ReportType.GRAPH))
			return GRAPH_REPORT_PATH;

		throw new AssertionError ("Unknown type of report");
	}

	/**
	 *
	 * @param element ������� ������� ��� ����������
	 * @param fromAnotherModule ���� ������ �� ������� ������
	 * @return ������ � ������������� �������
	 * @throws CreateReportException � ������, ���� ��� ������ ���
	 * ���������� ������ ��� ������ ������ � ������������ �������
*/
	abstract public RenderingComponent createReport(
		StorableElement element,
		boolean fromAnotherModule) throws CreateReportException;

	/**
	 * @return ���������� �������������� �������� ������
	 */
	abstract public String getName();

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� �������� �������
	 */
	abstract public String getReportElementName(StorableElement element);

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� �������
	 */
	abstract public String getReportElementFullName(StorableElement element);

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� ������� +
	 * ���������� � ���, �� ����� ������ ����� �������� �����
	 */
	abstract public String getReportElementDetailName(StorableElement element);
	
	/**
	 *
	 * @param reportName �������� �������� �������
	 * @return ���������� ��� �������� ������ ��� �������� �������
	 */
	abstract public ReportType getReportKind(String reportName);

	/**
	 * ��������� ������, ������ �������� �����������
	 * ������ �������, ����������� �� ������� ������
	 * @param rt ������
	 * @param data ���������� ��� ����������
	 */
	abstract public void installDataIntoReport(ReportTemplate rt,Map<String,Object> data);
	
	/**
	 * ���������� ������ ��������� ��ר��, ��������� ��� ������� ������
	 */
	abstract public Collection<String> getReportElementNames();

	/**
	 * ���������� ������ ��������� ������� ��ר��, ��������� ��� ������� ������
	 */
	abstract public Collection<String> getTemplateElementNames();

	public ReportModel()
	{
	}
}
