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
// * Название: тип характеристики оборудования                            * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\NetworkDirectory\CharacteristicType.java      * //
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

package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import com.syrus.AMFICOM.CORBA.NetworkDirectory.CharacteristicType_Transferable;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.io.IOException;
import java.io.Serializable;

public class CharacteristicType extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "characteristictype";

	public CharacteristicType_Transferable transferable;

	public String id = "";
	public String ch_class = "";
	public String name = "";
	public String description = "";
	public String value_type_id = "";
	public long modified = 0;
	public boolean editable = true;
	public boolean visible = true;

	public CharacteristicType()
	{
		transferable = new CharacteristicType_Transferable();
	}

	public CharacteristicType(CharacteristicType_Transferable transferable)
	{
		this.transferable = transferable; 
		setLocalFromTransferable();
	}

	public CharacteristicType(
			String id,
			String ch_class,
			String name,
			String value_type_id)
	{
		this.id = id;
		this.ch_class = ch_class;
		this.name = name;
		this.description = description;
		this.value_type_id = value_type_id;
		this.modified = System.currentTimeMillis();

		transferable = new CharacteristicType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		ch_class = transferable.ch_class;
		name = transferable.name;
		description = transferable.description;
		value_type_id = transferable.value_type_id;
		modified = transferable.modified;
		editable = transferable.editable;
		visible = transferable.visible;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.ch_class = ch_class;
		transferable.name = name;
		transferable.description = description;
		transferable.value_type_id = value_type_id;
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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(ch_class);
		out.writeObject(description);
		out.writeObject(value_type_id);
		out.writeLong(modified);
		out.writeBoolean(editable);
		out.writeBoolean(visible);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		ch_class = (String )in.readObject();
		description = (String )in.readObject();
		value_type_id = (String )in.readObject();
		modified = in.readLong();
		editable = in.readBoolean();
		visible = in.readBoolean();

		transferable = new CharacteristicType_Transferable();
	}
}
