package com.syrus.AMFICOM.Client.General.Report;

import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: �����-�������� ��� ������� �������
 * �� ����������� �������, ����������� ���.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

abstract public class APOReportModel extends ReportModel
{
	public String getName() {return "aporeportmodel";}

	abstract public Vector getAvailableReports();
	abstract public String getLangForField(String field);

	public APOReportModel()
	{
	}
}