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

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.awt.Color;

import java.util.Vector;

public class StubPropertyDisplayModel implements ObjectResourceDisplayModel
{
	ObjectResource or;
	
	public StubPropertyDisplayModel()
	{
	}
	
	public StubPropertyDisplayModel(ObjectResource or)
	{
		this.or = or;
	}
	
	public Vector getColumns()
	{
		if(or == null)
			return new Vector();
		ObjectResourceModel mod = or.getModel();
		return mod.getPropertyColumns();
	}
	
	public String getColumnName(String col_id)
	{
		if(or == null)
			return "";
		ObjectResourceModel mod = or.getModel();
		return mod.getPropertyName(col_id);
	}
	
	public int getColumnSize(String col_id)
	{
		return 100;
	}
	
	public boolean isColumnEditable(String col_id)
	{
		if(or == null)
			return false;
		ObjectResourceModel mod = or.getModel();
		return mod.isPropertyEditable(col_id);
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
    {
		if(this.or == null)
			return null;
		ObjectResourceModel mod = this.or.getModel();
		return (PropertyRenderer)mod.getPropertyRenderer(col_id);
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
    {
		if(this.or == null)
			return null;
		ObjectResourceModel mod = this.or.getModel();
		return (PropertyEditor)mod.getPropertyEditor(col_id);
	}

	public boolean isColumnColored(String col_id)
	{
		return false;
	}
	
	public Color getColumnColor(ObjectResource or, String col_id)
	{
		return Color.white;
	}
}
