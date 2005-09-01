package com.syrus.AMFICOM.client.report;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

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
	 * @return ������ � ������������� �������
	 * @throws CreateReportException � ������, ���� ��� ������ ���
	 * ���������� ������ ��� ������ ������ � ������������ �������
*/
	public abstract RenderingComponent createReport(
		StorableElement element,
		Object data) throws CreateReportException;

	/**
	 * @return ���������� �������������� �������� ������
	 */
	public abstract String getName();

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� �������� �������
	 */
	public abstract String getReportElementName(DataStorableElement element);

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� �������
	 */
	public abstract String getReportElementFullName(DataStorableElement element);

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� ������� +
	 * ���������� � ���, ����� ������� ����� �������� �����
	 */
	public String getReportElementDetailName(DataStorableElement element){
		//TODO ��������, ��� ����������� �������������� ����������
		return this.getReportElementFullName(element);
	}
	
	/**
	 *
	 * @param reportName �������� �������� �������
	 * @return ���������� ��� �������� ������ ��� �������� �������
	 */
	public abstract ReportType getReportKind(String reportName);

	/**
	 * ���������� ������ ��������� ��ר��, ��������� ��� ������� ������
	 */
	public abstract Collection<String> getReportElementNames();

	/**
	 * ���������� ������ ��������� ������� ��ר��, ��������� ��� ������� ������
	 */
	public abstract Collection<String> getTemplateElementNames();

	public ReportModel()
	{
	}
}
