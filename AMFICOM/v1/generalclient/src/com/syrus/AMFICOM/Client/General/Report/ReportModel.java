package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.JComponent;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

/**
 * <p>Title: </p>
 * <p>Description: ������� ����� ������, �������� ������� ��
 * ���������� ������� �� ���������� ��������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

abstract public class ReportModel
{
	static public String rt_tableReport = "rt_tableReport";

	/**
	 *
	 * @param rp ������� ������� ��� ����������
	 * @param divisionsNumber ���������� ��������� (��� �������)
	 * @param rt ������
	 * @param aContext �������� ����������
	 * @param fromAnotherModule ���� ������ �� ������� ������
	 * @return ������ � ������������� �������
	 * @throws CreateReportException � ������, ���� ��� ������ ���
	 * ���������� ������ ��� ������ ������ � ������������ �������
*/
	abstract public JComponent createReport(
		ObjectsReport rp,
		int divisionsNumber,
		ReportTemplate rt,
		ApplicationContext aContext,
		boolean fromAnotherModule) throws CreateReportException;

	/**
	 *
	 * @return ���������� ��������� ��� ������
	 */
	abstract public String getName();

	/**
	 *
	 * @return ���������� ������������� �������� ��������,
	 * ������ �� ������� ������������ ������ �������
	 */
	abstract public String getObjectsName();

	/**
	 *
	 * @param rp ������� �������
	 * @return ���������� �������������� ������ ��������
	 * �������� �������
	 */
	abstract public String getReportsName(ObjectsReport rp);

	/**
	 *
	 * @param rp ������� �������
	 * @return ���������� ����� ��������, ����������
	 * ������������� ��� ����������� ������
	 * @throws CreateReportException � ������, ���� ������
	 * ������ � ������������ �������
	 */
	abstract public String getReportsReserveName(ObjectsReport rp) throws
		CreateReportException;

	/**
	 *
	 * @param rp ������� �������
	 * @return ����������
	 *  1, ���� ��� ���������� ���������
	 * ������� �������� �������,
	 *  0, ���� ������,
	 *  -1, ���� �����
	 */
	abstract public int getReportKind(ObjectsReport rp);

	/**
	 * ��������� ������, ������ �������� �����������
	 * ������ �������, ����������� �� ������� ������
	 * @param rt ������
	 * @param data ���������� ��� ����������
	 */
	abstract public void setData(ReportTemplate rt,AMTReport data);

	public ReportModel()
	{
	}
}