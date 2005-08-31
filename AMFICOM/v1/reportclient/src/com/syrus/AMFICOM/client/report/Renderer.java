/*
 * $Id: Renderer.java,v 1.1 2005/08/31 10:32:55 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import javax.swing.JPanel;

import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * ���������� ��������� ���������� ����� ������� ������ ��� ������
 * ������.
 */
public abstract class Renderer extends JPanel{
	/**
	 * ������ ����� ��������� ��������� ��� ��������� ����������� � ����������� ��
	 * �� "�����"
	 * @param template
	 * @param fromAnotherModule
	 */
	public abstract void buildFromTemplate(ReportTemplate template,boolean fromAnotherModule)
		throws CreateReportException;
}
