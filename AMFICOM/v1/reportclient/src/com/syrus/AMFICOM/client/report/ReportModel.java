package com.syrus.AMFICOM.client.report;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;

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
	
	public static enum ReportType {SCHEMA,GRAPH,TABLE}
	
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
	 * @param reportName �������� �������� �������
	 * @return ���������� �������������� �������� �������� ������� ��� null,
	 * ���� ������ ������ � ������ �� �������������
	 */
	public abstract String getReportElementName(String reportName);

	/**
	 * @param reportName �������� �������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� �������
	 */
	public String getReportElementFullName(String reportName) {
		return this.getName()
			+ ReportModel.REPORT_NAME_DIVIDER
			+ LangModelReport.getString(reportName);
	}

	/**
	 * @param reportName �������� �������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� ������� +
	 * ���������� � ���, ����� ������� ����� �������� �����
	 */
	public String getReportElementDetailName(String reportName){
		//TODO ��������, ��� ����������� �������������� ����������
		return this.getReportElementFullName(reportName);
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
