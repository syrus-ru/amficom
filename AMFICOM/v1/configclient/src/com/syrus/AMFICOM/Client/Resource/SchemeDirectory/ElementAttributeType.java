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
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\SchemeDirectory\ElementAttributeType.java     * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 2.0                              * //
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

package com.syrus.AMFICOM.Client.Resource.SchemeDirectory;

import java.io.*;

import com.syrus.AMFICOM.CORBA.Scheme.ElementAttributeType_Transferable;
import com.syrus.AMFICOM.Client.Resource.StubResource;

public class ElementAttributeType extends StubResource
		implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "elementattributetype";
	public ElementAttributeType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String value_type_id = "";
	public String default_value = "";
	public long modified = 0;
	public boolean editable = true;
	public boolean visible = true;

	public ElementAttributeType()
	{
		transferable = new ElementAttributeType_Transferable();
	}

	public ElementAttributeType(ElementAttributeType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public ElementAttributeType(
			String id,
			String name,
			String value_type_id,
			String default_value)
	{
		this.id = id;
		this.name = name;
		this.value_type_id = value_type_id;
		this.default_value = default_value;
		modified = System.currentTimeMillis();

		transferable = new ElementAttributeType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		value_type_id = transferable.value_type_id;
		default_value = transferable.default_value;
		modified = transferable.modified;
		editable = transferable.editable;
		visible = transferable.visible;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.value_type_id = value_type_id;
		transferable.default_value = default_value;
		transferable.modified = modified;
		transferable.editable = editable;
		transferable.visible = visible;
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getId()
	{
		return id;
	}

	public String getTyp()
	{
		return typ;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public String getName()
	{
		return name;
	}

	public long getModified()
	{
		return modified;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(value_type_id);
		out.writeObject(default_value);
		out.writeLong(modified);
		out.writeBoolean(editable);
		out.writeBoolean(visible);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		value_type_id = (String )in.readObject();
		default_value = (String )in.readObject();
		modified = in.readLong();
		editable = in.readBoolean();
		visible = in.readBoolean();

		transferable = new ElementAttributeType_Transferable();
	}
}
