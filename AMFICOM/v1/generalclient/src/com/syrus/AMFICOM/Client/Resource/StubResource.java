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
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;

public class StubResource implements ObjectResource
{
	protected boolean changed = false;

	public boolean isChanged()
	{
		return this.changed;
	}

	public void setChanged(boolean changed)
	{
		this.changed = changed;
	}

//	public static PropertiesPanel getPropertyPane()
//	{
//		System.out.println("  ObjectResource: getPropertyPane");
//		return new GeneralPanel();
//	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.General.UI.GeneralPanel";
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] {"name"}, new String[] {"Название"});
	}

	public static ObjectResourceDisplayModel getReportDisplayModel()
	{
		return new StubDisplayModel(new String[] {"name"}, new String[] {"Название"});
	}

	public static ObjectResourceFilter getFilter()
	{
		return null;
	}

	public static ObjectResourceSorter getSorter()
	{
		return getDefaultSorter();
	}

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new ObjectResourceNameSorter();
	}

	public ObjectResourceModel getModel()
	{
		return new ObjectResourceModel();
	}

	public long getModified()
	{
		return 0;
	}

	public ObjectPermissionAttributes getPermissionAttributes()
	{
		return null;
	}

	public StubResource()
	{
	}
	public String getTyp()
	{
		throw new UnsupportedOperationException();
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
