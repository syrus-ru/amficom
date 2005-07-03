/*
 * $Id: ReportBuilder.java,v 1.11 2005/05/18 14:01:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.Model.*;
import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>Класс создаёт элементы отчёта - таблицы/графики/схемы,
 *    а также оптимально подгружает данные с сервера.</p>
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
   * Построитель элемента отчёта. Делает первичную проверку на реализуемость
   * эелемента. Затем, на базе структуры <code>ObjectsReport </code> выбирает
   * класс, требуемый для реализации данного элемента, передаёт в него данные и
   * получает из него панель с результатом. Эту панель он кладёт на
   * <code> TitledPanel </code> (панель с заголовком).
   *
   * @param rp реализуемый элемент шаблона</p>
   * @param divisionsNumber количество разбиений для таблицы (иначе 1)</p>
   * @param rt шаблон, которому принадлежит элемент</p>
   * @param aContext контекст приложения</p>
   * @param fromAnotherModule флаг вызова из другого модуля (при вызове из
   *                        другого модуля = true, иначе = false)</p>
   *
   * @exception CreateReportException Если отчёт по каким-то причинам
   *  нереализуем.
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

		//Генерим окно
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
