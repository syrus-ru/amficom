package com.syrus.AMFICOM.client.report;

import java.util.Collection;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;

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
	 * @throws CreateModelException 
*/
	public abstract RenderingComponent createReport(
		AbstractDataStorableElement<?> element,
		Object data,
		ApplicationContext aContext)
		throws CreateReportException, CreateModelException;

	/**
	 * �������� ������ (������ �������� ����� � DestinationModules)
	 */
	public abstract String getName();
	
	/**
	 * @return ���������� �������������� ������ �������� ������
	 * (���� "������� �� ������ "������"")
	 */
	public String getLocalizedName() {
		return I18N.getString(this.getName());
	}

	/**
	 * @return ���������� �������������� �������� �������� ������
	 * (���� "������")
	 */
	public String getLocalizedShortName() {
		return I18N.getString(
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
			+ this.getReportElementName(reportName);
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
	 * ���������� ������ ��������� ������� ��ר��, ��������� ��� ������� ������
	 */
	public abstract Collection<String> getTemplateElementNames();
}
