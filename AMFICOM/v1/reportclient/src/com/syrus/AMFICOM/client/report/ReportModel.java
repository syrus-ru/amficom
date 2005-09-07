package com.syrus.AMFICOM.client.report;

import java.util.Collection;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.StorableElement;

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
	public static String REPORT_NAME_DIVIDER = " : ";
	
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
	 * �������� ������ (������ �������� ����� � DestinationModules)
	 */
	public abstract String getName();
	
	/**
	 * @return ���������� �������������� ������ �������� ������
	 * (���� "������� �� ������ "������"")
	 */
	public String getLocalizedName() {
		return LangModelReport.getString(this.getName());
	}

	/**
	 * @return ���������� �������������� �������� �������� ������
	 * (���� "������")
	 */
	public String getLocalizedShortName() {
		return LangModelReport.getString(
				DestinationModules.getShortName(this.getName()));
	}
	
	/**
	 * @param reportName �������� �������� �������
	 * @return ���������� �������������� �������� �������� ������� ��� null,
	 * ���� ������ ������ � ������ �� �������������
	 */
	public abstract String getReportElementName(String reportName);

	/**
	 * @param reportName �������� �������� �������
	 * @return ���������� �������������� (��������) �������� ������
	 * ������� + �������������� �������� �������� �������
	 */
	public String getReportElementFullName(String reportName) {
		return this.getLocalizedShortName()
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
