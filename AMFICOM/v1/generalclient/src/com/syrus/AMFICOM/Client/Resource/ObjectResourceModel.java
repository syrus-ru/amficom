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

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;

import java.awt.Component;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ObjectResourceModel extends Object
{
	ObjectResource or;

	public ObjectResourceModel()
	{
	}

	public ObjectResourceModel(ObjectResource or)
	{
		this.or = or;
	}

	public Enumeration getChildren(String key)
	{
//		System.out.println(" ORM: getChildren " + key);
		return new Vector().elements();
	}

	public PropertiesPanel getPropertyPane()
	{
//		System.out.println("  ORM: getPropertyPane");
		return new GeneralPanel();
	}

	public String getColumnValue(String col_id)
	{
//		System.out.println("      ORM: getValue " + col_id);
		return "";
	}

	public void setColumnValue(String col_id, Object val)
	{
//		System.out.println("       ORM: setValue " + col_id + " " + val);
	}
/*	
	public Component getColumnRenderer(String col_id)
    {
//		System.out.println("        ORM: getColumnRenderer " + col_id);
		return null;//new JLabelRenderer(col_id);
	}

	public Component getColumnEditor(String col_id)
    {
//		System.out.println("         ORM: getColumnEditor " + col_id);
		return null;//new TextFieldEditor(col_id);
	}
*/	
	public Vector getPropertyColumns()
	{
//		System.out.println("           ORM: getPropertyColumns");
		return new Vector();
	}

	public String getPropertyName(String col_id)
	{
//		System.out.println("             ORM: getPropertyName " + col_id);
		return "";
	}

	public String getPropertyValue(String col_id)
	{
//		System.out.println("            ORM: getPropertyValue " + col_id);
		return "";
	}
	
	public boolean isPropertyEditable(String col_id)
	{
//		System.out.println("                  ORM: isEditable " + col_id);
		return false;
	}

	public void setPropertyValue(String col_id, Object val)
	{
//		System.out.println("              ORM: setPropertyValue " + col_id + " " + val);
	}
	
	public Component getPropertyRenderer(String col_id)
    {
//		System.out.println("               ORM: getPropertyRenderer " + col_id);
		return null;//getColumnRenderer(col_id);
	}

	public Component getPropertyEditor(String col_id)
    {
//		System.out.println("                ORM: getPropertyEditor " + col_id);
		return null;//getColumnEditor(col_id);
	}
}
