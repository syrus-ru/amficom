package com.syrus.AMFICOM.client.report;

import java.util.Collection;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
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
	 * @throws CreateModelException 
*/
	public abstract RenderingComponent createReport(
		AbstractDataStorableElement<?> element,
		Object data,
		ApplicationContext aContext)
		throws CreateReportException, CreateModelException;

	/**
	 * Название модели (список значений лежит в DestinationModules)
	 */
	public abstract String getName();
	
	/**
	 * @return возвращает локализованное полное название модели
	 * (типа "Шаблоны по модулю "Анализ"")
	 */
	public String getLocalizedName() {
		return I18N.getString(this.getName());
	}

	/**
	 * @return возвращает локализованное короткое название модели
	 * (типа "Анализ")
	 */
	public String getLocalizedShortName() {
		return I18N.getString(
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
	 * Возвращает список элементов ШАБЛОНА ОТЧЁТА, доступных для данного модуля
	 */
	public abstract Collection<String> getTemplateElementNames();
}
