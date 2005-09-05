package com.syrus.AMFICOM.client.report;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;

import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

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
	 * @return Панель с реализованным отчётом
	 * @throws CreateReportException в случае, если нет данных для
	 * заполнения отчёта или данные заданы в неправильном формате
*/
	public abstract RenderingComponent createReport(
		StorableElement element,
		Object data) throws CreateReportException;

	/**
	 * @return возвращает локализованное название модели
	 */
	public abstract String getName();

	/**
	 * @param reportName Название элемента шаблона
	 * @return Возвращает локализованное название элемента шаблона или null,
	 * если такого отчёта в модели не предусмотрено
	 */
	public abstract String getReportElementName(String reportName);

	/**
	 * @param reportName Название элемента шаблона
	 * @return Возвращает локализованное название модели отчётов +
	 * локализованное название элемента шаблона
	 */
	public String getReportElementFullName(String reportName) {
		return this.getName()
			+ ReportModel.REPORT_NAME_DIVIDER
			+ LangModelReport.getString(reportName);
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
