package com.syrus.AMFICOM.client.general.report;

import java.util.Map;

import com.syrus.AMFICOM.report.RenderingElement;
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
	
	public static enum ReportType {SCHEMA,GRAPH,TABLE}

	/**
	 *
	 * @param element ������� ������� ��� ����������
	 * @param fromAnotherModule ���� ������ �� ������� ������
	 * @return ������ � ������������� �������
	 * @throws CreateReportException � ������, ���� ��� ������ ���
	 * ���������� ������ ��� ������ ������ � ������������ �������
*/
	abstract public ReportComponent createReport(
		RenderingElement element,
		boolean fromAnotherModule) throws CreateReportException;

	/**
	 * @return ���������� �������������� �������� ������
	 */
	abstract public String getName();

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� �������� �������
	 * @throws CreateReportException � ������, ���� ������
	 * ������ � ������������ �������
	 */
	abstract public String getReportElementName(RenderingElement element);

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� �������
	 * @throws CreateReportException � ������, ���� ������
	 * ������ � ������������ �������
	 */
	abstract public String getReportElementFullName(RenderingElement element);

	/**
	 * @param element ������� �������
	 * @return ���������� �������������� �������� ������ ������� +
	 * �������������� �������� �������� ������� +
	 * ���������� � ���, �� ����� ������ ����� �������� �����
	 * @throws CreateReportException � ������, ���� ������
	 * ������ � ������������ �������
	 */
	abstract public String getReportElementDetailName(RenderingElement element);
	
	/**
	 *
	 * @param element ������� �������
	 * @return ���������� ��� �������� ������ ��� �������� �������
	 */
	abstract public ReportType getReportKind(RenderingElement element);

	/**
	 * ��������� ������, ������ �������� �����������
	 * ������ �������, ����������� �� ������� ������
	 * @param rt ������
	 * @param data ���������� ��� ����������
	 */
	abstract public void installDataIntoReport(ReportTemplate rt,Map data);

	public ReportModel()
	{
	}
}
