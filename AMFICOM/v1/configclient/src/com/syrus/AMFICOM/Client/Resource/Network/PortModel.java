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
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.2                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Configure\ISM\KIS.java                                 * //
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

package com.syrus.AMFICOM.Client.Resource.Network;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class PortModel extends CatalogElementModel
{
	private Port port;
	public PortModel(Port port)
	{
		super(port);
		this.port = port;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return port.id;
		if(col_id.equals("name"))
			return port.name;
		if(col_id.equals("description"))
			return port.description;
		if(col_id.equals("type_id"))
			return port.type_id;
		if(col_id.equals("equipment_id"))
			return port.equipment_id;
		if(col_id.equals("interface_id"))
			return port.interface_id;
		if(col_id.equals("address_id"))
			return port.address_id;
		if(col_id.equals("domain_id"))
			return port.domain_id;

		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return port.characteristics;
	}
}

