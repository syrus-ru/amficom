package com.syrus.AMFICOM.report;

import com.syrus.AMFICOM.general.Identifier;

/**
 * <p>Title: </p>
 * <p>Description: Элемент шаблона</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public abstract class DataStorableElement extends StorableElement
{
	/**
	 * Название отображаемого отчёта.
	 * По этому имени он будет отображаться Renderer'ом с помощью ReportModel.
	 */
	protected String reportName = null;

	/**
	 * Полное имя класса модели, которая "знает" как строить этот отчёт.
	 */
	protected String modelClassName = null;
	
	/**
	 * Несмотря на то, что объекты этого класса сохраняются только в рамках
	 * шаблона отчёта, им нужны идентификаторы, поскольку на них ссылаются
	 * привязанные надписи
	 */
	protected Identifier id = Identifier.VOID_IDENTIFIER;

	public String getReportName() {
		return this.reportName;
	}

	public Identifier getId() {
		return this.id;
	}

	public String getModelClassName() {
		return this.modelClassName;
	}

	public DataStorableElement(String reportName, String modelClassName)
	{
		this.reportName = reportName;
		this.modelClassName = modelClassName;
		//TODO Здесь должно быть получение ID
		this.id = Identifier.VOID_IDENTIFIER;
	}
}
