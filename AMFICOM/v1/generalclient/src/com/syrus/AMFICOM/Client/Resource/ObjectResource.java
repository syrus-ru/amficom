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
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceNameSorter;

import java.util.Enumeration;
import java.util.Vector;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;

public abstract class ObjectResource extends Object
{
	static final public String typ = "objectresource";	
									// тип ресурса объекта, представляет собой
									// имя класса объекта (например, Port)

//	public boolean loaded = false;
	protected boolean changed = false;
/*
	public ObjectResource()
	{
	}

	public ObjectResource(String typ)
	{
//		this.typ = typ;
		changed = false;
	}
	
	public ObjectResource(
			String typ,
			boolean changed)
	{

		this.changed = changed;
	}
*/
	public abstract String getTyp();
/*
	{
		return typ;
	}
*/	

	public boolean isChanged()
	{
		return changed;
	}
	
	public void setChanged(boolean changed)
	{
		this.changed = changed;
	}

	/**
	 * @deprecated
	 * @see ObjectResourceModel
	 */
	public Enumeration getChildren(String key)
	{
		System.out.println(" ObjectResource: getChildren " + key);
		return new Vector().elements();
	}

	public static PropertiesPanel getPropertyPane()
	{
		System.out.println("  ObjectResource: getPropertyPane");
		return new GeneralPanel();
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
/*	
	public void prepareRemoval()
	{
		System.out.println("                    ObjectResource: prepareRemoval");
	}
*/
	public ObjectResourceModel getModel()
	{
		return new ObjectResourceModel();
	}

	/**
	 * @deprecated
	 * @see ObjectResourceModel
	 */
	public Enumeration getChildTypes()
    {
        return new Vector().elements();
    }
		
	/**
	 * @deprecated
	 * @see ObjectResourceModel
	 */
	public Class getChildClass(String type)
    {
        return ObjectResource.class;
    }

	public long getModified()
	{
		return 0;
	}

	public ObjectPermissionAttributes getPermissionAttributes()
	{
		return null;
	}

	public abstract Object getTransferable();
	public abstract String getName();
	public abstract String getId();
	public abstract String getDomainId();
	public abstract void setLocalFromTransferable();
	public abstract void setTransferableFromLocal();
	public abstract void updateLocalFromTransferable();
}
