//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: класс, описывающий свойства, одинаковые для всех объектов, * //
// *           загружаемых в клиентское ПО с РИСД. Каждый объект          * //
// *           содержит экземпляр данного класса и может использовать     * //
// *           из него необходимые члены для хранения информации,         * //
// *           связанной с обменом клиентского ПО с РИСД                  * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\ObjectResource.java                           * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class StubResource extends ObjectResource
{
	static final public String typ = "stub";

	public StubResource()
	{
		super();
	}

	public StubResource(String typ)
	{
		super(typ);
	}
	
	public String getTyp()
	{
		return typ;
	}
	
	public Object getTransferable()
	{
		return null;
	}
	
	public String getName()
	{
		return "";
	}
	
	public String getId()
	{
		return "";
	}

	public String getDomainId()
	{
		return "sysdomain";
	}
	
	public void setLocalFromTransferable()
	{
	}
	
	public void setTransferableFromLocal()
	{
	}
	
	public void updateLocalFromTransferable()
	{
	}

	public String getColumnName(String col_id)
	{
		return "";
	}

	public String getColumnValue(String col_id)
	{
		return "";
	}

	public String getPropertyValue(String col_id)
	{
		return "";
	}
	
	public String getPropertyName(String col_id)
	{
		return "";
	}

}
