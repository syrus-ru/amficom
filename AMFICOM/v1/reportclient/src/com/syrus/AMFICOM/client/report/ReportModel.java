package com.syrus.AMFICOM.client.report;

import java.util.Collection;
import java.util.Map;

import com.syrus.AMFICOM.report.StorableElement;
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
	
	private static String TABLE_REPORT_PATH = "images/table_report";
	private static String SCHEME_REPORT_PATH = "images/scheme";
	private static String GRAPH_REPORT_PATH = "images/graph_report";	
	
	public static enum ReportType {SCHEMA,GRAPH,TABLE}
	
	public static String getIconForReportType(ReportType type){
		if (type.equals(ReportType.TABLE))
			return TABLE_REPORT_PATH;

		if (type.equals(ReportType.SCHEMA))
			return SCHEME_REPORT_PATH;

		if (type.equals(ReportType.GRAPH))
			return GRAPH_REPORT_PATH;

		throw new AssertionError ("Unknown type of report");
	}

	/**
	 *
	 * @param element Элемент шаблона для реализации
	 * @param fromAnotherModule Флаг вызова из другого модуля
	 * @return Панель с реализованным отчётом
	 * @throws CreateReportException в случае, если нет данных для
	 * заполнения отчёта или данные заданы в неправильном формате
*/
	abstract public RenderingComponent createReport(
		StorableElement element,
		boolean fromAnotherModule) throws CreateReportException;

	/**
	 * @return возвращает локализованное название модели
	 */
	abstract public String getName();

	/**
	 * @param element Элемент шаблона
	 * @return Возвращает локализованное название элемента шаблона
	 */
	abstract public String getReportElementName(StorableElement element);

	/**
	 * @param element Элемент шаблона
	 * @return Возвращает локализованное название модели отчётов +
	 * локализованное название элемента шаблона
	 */
	abstract public String getReportElementFullName(StorableElement element);

	/**
	 * @param element Элемент шаблона
	 * @return Возвращает локализованное название модели отчётов +
	 * локализованное название элемента шаблона +
	 * информация о том, по каким данным будет построен отчёт
	 */
	abstract public String getReportElementDetailName(StorableElement element);
	
	/**
	 *
	 * @param reportName Название элемента шаблона
	 * @return возвращает тип элемента отчёта для элемента шаблона
	 */
	abstract public ReportType getReportKind(String reportName);

	/**
	 * Заполняет шаблон, отчёты которого описываются
	 * данной моделью, информацией из другого модуля
	 * @param rt Шаблон
	 * @param data Информация для заполнения
	 */
	abstract public void installDataIntoReport(ReportTemplate rt,Map<String,Object> data);
	
	/**
	 * Возвращает список элементов ОТЧЁТА, доступных для данного модуля
	 */
	abstract public Collection<String> getReportElementNames();

	/**
	 * Возвращает список элементов ШАБЛОНА ОТЧЁТА, доступных для данного модуля
	 */
	abstract public Collection<String> getTemplateElementNames();

	public ReportModel()
	{
	}
}
