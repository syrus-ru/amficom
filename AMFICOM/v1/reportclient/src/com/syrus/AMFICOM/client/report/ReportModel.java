package com.syrus.AMFICOM.client.report;

import java.util.Collection;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;

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
	public static String REPORT_NAME_DIVIDER = " : ";
	
	public static enum ReportType {SCHEMA,GRAPH,TABLE}
	
	/**
	 *
	 * @param element Элемент шаблона для реализации
	 * @return Панель с реализованным отчётом
	 * @throws CreateReportException в случае, если нет данных для
	 * заполнения отчёта или данные заданы в неправильном формате
*/
	public abstract RenderingComponent createReport(
		DataStorableElement element,
		Object data,
		ApplicationContext aContext) throws CreateReportException;

	/**
	 * Название модели (список значений лежит в DestinationModules)
	 */
	public abstract String getName();
	
	/**
	 * @return возвращает локализованное полное название модели
	 * (типа "Шаблоны по модулю "Анализ"")
	 */
	public String getLocalizedName() {
		return LangModelReport.getString(this.getName());
	}

	/**
	 * @return возвращает локализованное короткое название модели
	 * (типа "Анализ")
	 */
	public String getLocalizedShortName() {
		return LangModelReport.getString(
				DestinationModules.getShortName(this.getName()));
	}
	
	/**
	 * @param reportName Название элемента шаблона
	 * @return Возвращает локализованное название элемента шаблона или null,
	 * если такого отчёта в модели не предусмотрено
	 */
	public abstract String getReportElementName(String reportName);

	/**
	 * @param reportName Название элемента шаблона
	 * @return Возвращает локализованное (короткое) название модели
	 * отчётов + локализованное название элемента шаблона
	 */
	public String getReportElementFullName(String reportName) {
		return this.getLocalizedShortName()
			+ ReportModel.REPORT_NAME_DIVIDER
			+ this.getReportElementName(reportName);
	}

	/**
	 * @param reportName Название элемента шаблона
	 * @return Возвращает локализованное название модели отчётов +
	 * локализованное название элемента шаблона +
	 * информация о том, каким образом будет построен отчёт
	 */
	public String getReportElementDetailName(String reportName){
		//TODO Подумать, как представить дополнительную информацию
		return this.getReportElementFullName(reportName);
	}
	
	/**
	 *
	 * @param reportName Название элемента шаблона
	 * @return возвращает тип элемента отчёта для элемента шаблона
	 */
	public abstract ReportType getReportKind(String reportName);

	/**
	 * Возвращает список элементов ОТЧЁТА, доступных для данного модуля
	 */
	public abstract Collection<String> getReportElementNames();

	/**
	 * Возвращает список элементов ШАБЛОНА ОТЧЁТА, доступных для данного модуля
	 */
	public abstract Collection<String> getTemplateElementNames();

	public ReportModel()
	{
	}
}
