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
 * <p>Класс создаёт элементы отчёта - таблицы/графики/схемы,
 *    а также оптимально подгружает данные с сервера.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
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
	 * Подгрузка объектов с сервера для заданного шаблона.
	 * Класс определяет какие данные требуется подгрузить для каждого
	 * элемента шаблона и проверяет, нет ли среди них дублирующих друг
	 * друга. Все данные подгружаются один раз.
	 *   @param dsi интерфейс для передачи данных</p>
	 *   @param rt шаблон, которому принадлежит элемент</p>
	 */
	public static void loadRequiredObjects(
			final ApplicationContext aContext,
			final ReportTemplate rt)
	{
		//Подгружаем данные в шаблоны фильтров
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
		"Идёт загрузка. Пожалуйста, подождите.");
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
	 *                       другого модуля = true, иначе = false)</p>
	 *
	 * @exception CreateReportException Если отчёт по каким-то причинам
	 * нереализуем.
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
