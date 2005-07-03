/*
 * $Id: ReportBuilder.java,v 1.11 2005/05/18 14:01:19 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.Model.*;
import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>����� ������ �������� ������ - �������/�������/�����,
 *    � ����� ���������� ���������� ������ � �������.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 *
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/05/18 14:01:19 $
 * @module generalclient_v1
 */
public class ReportBuilder
{
	static public String ev_startProgressBar = "ev_startProgressBar";
	static public String ev_stopProgressBar = "ev_stopProgressBar";

	public ReportBuilder()
	{
	}

	/**
   * ����������� �������� ������. ������ ��������� �������� �� �������������
   * ���������. �����, �� ���� ��������� <code>ObjectsReport </code> ��������
   * �����, ��������� ��� ���������� ������� ��������, ������� � ���� ������ �
   * �������� �� ���� ������ � �����������. ��� ������ �� ����� ��
   * <code> TitledPanel </code> (������ � ����������).
   *
   * @param rp ����������� ������� �������</p>
   * @param divisionsNumber ���������� ��������� ��� ������� (����� 1)</p>
   * @param rt ������, �������� ����������� �������</p>
   * @param aContext �������� ����������</p>
   * @param fromAnotherModule ���� ������ �� ������� ������ (��� ������ ��
   *                        ������� ������ = true, ����� = false)</p>
   *
   * @exception CreateReportException ���� ����� �� �����-�� ��������
   *  �����������.
   * @return
   */

	public static TitledPanel createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule)

			throws CreateReportException
	{
		if ( (!rp.isToBeFilledImmediately) && (!fromAnotherModule))
			throw new CreateReportException(rp.getName(),
																			CreateReportException.templatePiece);

		//������� ����
		JComponent returnValue = rp.model.createReport(rp,divisionsNumber,rt,aContext,fromAnotherModule);
		if (returnValue == null)
			throw new CreateReportException(rp.getName(),
																			CreateReportException.cantImplement);

		RenderingObject reportsRO = rt.findROforReport(rp);
		TitledPanel containPanel = new TitledPanel(reportsRO.getReportToRender().getName(),
																							 returnValue);

		//containPanel.add(returnValue,BorderLayout.CENTER);

		return containPanel;
	}

	static public void invokeAsynchronously(final Runnable doRun, final String dialogTitle) {
		Thread thread = new Thread()
		{
			public void run()
			{
				/**
				 * @todo Set dialog's parent depending on app context.
				 */
				final JDialog jDialog = new JDialog(Environment.getActiveWindow(),dialogTitle,true);
				jDialog.setResizable(false);

				((JPanel) (jDialog.getContentPane())).setPreferredSize(new Dimension(400, 0));
				jDialog.pack();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Dimension dialogSize = jDialog.getSize();
				jDialog.setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);

				Thread targetThread = new Thread()
				{
					public void run()
					{
						doRun.run();
					}
				};
				targetThread.start();

				Thread dialogThread = new Thread()
				{
					public void run()
					{
						jDialog.setVisible(true);
					}
				};
				dialogThread.start();

				try
				{
					targetThread.join();
				} catch (InterruptedException ie)
				{
					ie.printStackTrace();
				}
				jDialog.dispose();
			}
		};
		thread.start();
	}

}
