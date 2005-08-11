package com.syrus.AMFICOM.client.general.report;

import java.util.Map;

import com.syrus.AMFICOM.report.RenderingElement;
import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * <p>Title: </p>
 * <p>Description: Потомки этого класса, содержат функции по
 * построению отчётов по конкретным объектам</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public abstract class ReportModel
{
	public static char REPORT_NAME_DIVIDER = ':';
	
	public static enum ReportType {SCHEMA,GRAPH,TABLE}

	/**
	 *
	 * @param element Элемент шаблона для реализации
	 * @param fromAnotherModule Флаг вызова из другого модуля
	 * @return Панель с реализованным отчётом
	 * @throws CreateReportException в случае, если нет данных для
	 * заполнения отчёта или данные заданы в неправильном формате
*/
	abstract public ReportComponent createReport(
		RenderingElement element,
		boolean fromAnotherModule) throws CreateReportException;

	/**
	 * @return возвращает локализованное название модели
	 */
	abstract public String getName();

	/**
	 * @param element Элемент шаблона
	 * @return Возвращает локализованное название элемента шаблона
	 * @throws CreateReportException в случае, если данные
	 * заданы в неправильном формате
	 */
	abstract public String getReportElementName(RenderingElement element);

	/**
	 * @param element Элемент шаблона
	 * @return Возвращает локализованное название модели отчётов +
	 * локализованное название элемента шаблона
	 * @throws CreateReportException в случае, если данные
	 * заданы в неправильном формате
	 */
	abstract public String getReportElementFullName(RenderingElement element);

	/**
	 * @param element Элемент шаблона
	 * @return Возвращает локализованное название модели отчётов +
	 * локализованное название элемента шаблона +
	 * информация о том, по каким данным будет построен отчёт
	 * @throws CreateReportException в случае, если данные
	 * заданы в неправильном формате
	 */
	abstract public String getReportElementDetailName(RenderingElement element);
	
	/**
	 *
	 * @param element Элемент шаблона
	 * @return возвращает тип элемента отчёта для элемента шаблона
	 */
	abstract public ReportType getReportKind(RenderingElement element);

	/**
	 * Заполняет шаблон, отчёты которого описываются
	 * данной моделью, информацией из другого модуля
	 * @param rt Шаблон
	 * @param data Информация для заполнения
	 */
	abstract public void installDataIntoReport(ReportTemplate rt,Map data);

	public ReportModel()
	{
	}
}
