package com.syrus.AMFICOM.Client.General.Report;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridBagConstraints;
import java.util.Date;

/*import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;*/

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;

/**
 * <p>Title: </p>
 * <p>����� ������ �������� ������ - �������/�������/�����,
 *    � ����� ���������� ���������� ������ � �������.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public class ReportBuilder
{
	static public String ev_startProgressBar = "ev_startProgressBar";
	static public String ev_stopProgressBar = "ev_stopProgressBar";

	public ReportBuilder()
	{
	}

	/**
	 * ��������� �������� � ������� ��� ��������� �������.
	 * ����� ���������� ����� ������ ��������� ���������� ��� �������
	 * �������� ������� � ���������, ��� �� ����� ��� ����������� ����
	 * �����. ��� ������ ������������ ���� ���.
	 *   @param dsi ��������� ��� �������� ������</p>
	 *   @param rt ������, �������� ����������� �������</p>
	 */
	public static void loadRequiredObjects(
			final ApplicationContext aContext,
			final ReportTemplate rt)
	{
		//���������� ������ � ������� ��������
		for (int i = 0; i < rt.objectResourceFilters.size(); i++)
		{
			ObjectResourceFilter curFilter = (ObjectResourceFilter)rt.objectResourceFilters.get(i);
			curFilter.logicScheme.setUnfilledFilterExpressions(aContext);
		}

//		aContext.getDispatcher().notify(new OperationEvent("",0,ReportBuilder.ev_startProgressBar));

		ReportBuilder.invokeAsynchronously(new Runnable() {
			public void run()
			{
				System.out.println(new Date(System.currentTimeMillis()).toString() +
										 " " + "Getting data from server...");

				for (int i = 0; i < rt.objectRenderers.size(); i++)
				{
					ObjectsReport curReport =
						((RenderingObject) rt.objectRenderers.get(i)).getReportToRender();

					curReport.model.loadRequiredObjects(aContext.getDataSourceInterface(), curReport, rt);
				}

				System.out.println(new Date(System.currentTimeMillis()).toString() +
										 " " + "...done!");

				aContext.getDispatcher().notify(new OperationEvent("",0,ReportBuilder.ev_stopProgressBar));
			}
		},
		"��� ��������. ����������, ���������.");
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
	 *                       ������� ������ = true, ����� = false)</p>
	 *
	 * @exception CreateReportException ���� ����� �� �����-�� ��������
	 * �����������.
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
				final JDialog jDialog = new JDialog();
				jDialog.setModal(true);
				jDialog.setResizable(false);
				jDialog.setTitle(dialogTitle);
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
