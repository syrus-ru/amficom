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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ObjectResourceModel{
	
	public static final String	COLUMN_ALARM_TYPE_NAME	= "alarm_type_name";
	public static final String	COLUMN_GENERATED		= "generated";
	public static final String	COLUMN_KIS_ID			= "kis_id";
	public static final String	COLUMN_LOCAL_ID			= "local_id";
	public static final String	COLUMN_ME_ID			= "monitored_element_id";
	public static final String	COLUMN_PORT_ID			= "port_id";
	public static final String	COLUMN_SOURCE_NAME		= "source_name";
	public static final String	COLUMN_START_TIME		= "start_time";
	public static final String	COLUMN_STATUS			= "status";
	public static final String	COLUMN_TEMPORAL_TYPE	= "temporal_type";
	public static final String	COLUMN_TEST_TYPE_ID		= "test_type_id";
	
	public static final String 	COLUMN_TYPE_LIST		= "list";
	public static final String	COLUMN_TYPE_LONG		= "long";
	public static final String 	COLUMN_TYPE_NUMERIC		= "numeric";
	public static final String 	COLUMN_TYPE_RANGE		= "range";
	public static final String	COLUMN_TYPE_STRING		= "string";
	public static final String	COLUMN_TYPE_TIME		= "time";	
	
	protected ObjectResource or;
	private LinkedList children = new LinkedList();
	private PropertiesPanel panel = new GeneralPanel();

	private LinkedList propertyColumns = new LinkedList();

	public ObjectResourceModel()
	{
	}

	public ObjectResourceModel(ObjectResource or)
	{
		this.or = or;
	}

	public Collection getChildren(String key)
	{
//		System.out.println(" ORM: getChildren " + key);
		return children;
	}

	public String getColumnValue(String col_id)
	{
//		System.out.println("      ORM: getValue " + col_id);
		return "";
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
	public List getPropertyColumns()
	{
//		System.out.println("           ORM: getPropertyColumns");
		return propertyColumns;
	}

	public Component getPropertyEditor(String col_id)
    {
//		System.out.println("                ORM: getPropertyEditor " + col_id);
		return null;//getColumnEditor(col_id);
	}

	public String getPropertyName(String col_id)
	{
//		System.out.println("             ORM: getPropertyName " + col_id);
		return "";
	}

	public PropertiesPanel getPropertyPane()
	{
//		System.out.println("  ORM: getPropertyPane");
		return panel;
	}
	
	public Component getPropertyRenderer(String col_id)
    {
//		System.out.println("               ORM: getPropertyRenderer " + col_id);
		return null;//getColumnRenderer(col_id);
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

	public void setColumnValue(String col_id, Object val)
	{
//		System.out.println("       ORM: setValue " + col_id + " " + val);
	}

	public void setPropertyValue(String col_id, Object val)
	{
//		System.out.println("              ORM: setPropertyValue " + col_id + " " + val);
	}
}
