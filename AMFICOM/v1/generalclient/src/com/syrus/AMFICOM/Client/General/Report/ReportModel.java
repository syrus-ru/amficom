package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import javax.swing.JComponent;

/**
 * <p>Title: </p>
 * <p>Description: Потомки этого класса, содержат функции по
 * построению отчётов по конкретным объектам</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

abstract public class ReportModel
{
	static public String rt_tableReport = "rt_tableReport";

	abstract public void loadRequiredObjects(
		DataSourceInterface dsi,
		ObjectsReport rp,
		ReportTemplate rt);

	/**
	 *
	 * @param rp Элемент шаблона для реализации
	 * @param divisionsNumber Количество разбиений (для таблицы)
	 * @param rt Шаблон
	 * @param aContext Контекст приложения
	 * @param fromAnotherModule Флаг вызова из другого модуля
	 * @return Панель с реализованным отчётом
	 * @throws CreateReportException в случае, если нет данных для
	 * заполнения отчёта или данные заданы в неправильном формате
*/
	abstract public JComponent createReport(
		ObjectsReport rp,
		int divisionsNumber,
		ReportTemplate rt,
		ApplicationContext aContext,
		boolean fromAnotherModule) throws CreateReportException;

	/**
	 *
	 * @return возвращает строковое имя модели
	 */
	abstract public String getName();

	/**
	 *
	 * @return возвращает локализванное название объектов,
	 * отчёты по которым описыаваются данной моделью
	 */
	abstract public String getObjectsName();

	/**
	 *
	 * @param rp Элемент шаблона
	 * @return возвращает локализованное полное название
	 * элемента шаблона
	 */
	abstract public String getReportsName(ObjectsReport rp);

	/**
	 *
	 * @param rp Элемент шаблона
	 * @return возвращает часть названия, получаемую
	 * индивидуально для конкретного отчёта
	 * @throws CreateReportException в случае, если данные
	 * заданы в неправильном формате
	 */
	abstract public String getReportsReserveName(ObjectsReport rp) throws
		CreateReportException;

	/**
	 *
	 * @param rp Элемент шаблона
	 * @return возвращает
	 *  1, если при реализации эелемента
	 * шаблона строится таблица,
	 *  0, если график,
	 *  -1, если схема
	 */
	abstract public int getReportKind(ObjectsReport rp);

	/**
	 * Заполняет шаблон, отчёты которого описываются
	 * данной моделью, информацией из другого модуля
	 * @param rt Шаблон
	 * @param data Информация для заполнения
	 */
	abstract public void setData(ReportTemplate rt,AMTReport data);

	public ReportModel()
	{
	}
}
