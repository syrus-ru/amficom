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
// *        Client\Resource\Network\TestPort.java                        * //
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

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.Client.Resource.StubResource;

public class TestPort extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "testport";

	public TestPort_Transferable transferable;

	public String id = "";
	public String name = "";
	public String type_id = "";
	public String equipment_id = "";
	public String port_id = "";

	public Map characteristics = new HashMap();

	public TestPort()
	{
		transferable = new TestPort_Transferable();
	}

	public TestPort(TestPort_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}
/*
	public TestPort(MapConnectionPoint mcpe, Equipment equipment, Port port)
	{
		TestPortType testPortType = (TestPortType )Pool.get("testporttype", mcpe.test_port_type_id);
//		TestPort testPort = (TestPort )Pool.get("testport", mcpe.test_port_id);

		this.id = equipment.id + "." + testPortType.id;
		this.name = testPortType.getName();
		this.type_id = mcpe.test_port_type_id;
		this.port_id = port.id;
		this.equipment_id = equipment.id;

		transferable = new TestPort_Transferable();
	}
*/
	public TestPort(
			String id,
			String name,
			String type_id,
			String equipment_id,
			String port_id,
			Hashtable characteristics)
	{
		this.id = id;
		this.name = name;
		this.type_id = type_id;
		this.equipment_id = equipment_id;
		this.port_id = port_id;
		this.characteristics = characteristics;

		transferable = new TestPort_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		type_id = transferable.type_id;
		equipment_id = transferable.equipment_id;
		port_id = transferable.port_id;

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.type_id = type_id;
		transferable.equipment_id = equipment_id;
		transferable.port_id = port_id;

		int l = this.characteristics.size();
		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[l];
		for(Iterator it = characteristics.values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			ch.setTransferableFromLocal();
			transferable.characteristics[i++] = ch.transferable;
		}
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(type_id);
		out.writeObject(equipment_id);
		out.writeObject(port_id);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		type_id = (String )in.readObject();
		equipment_id = (String )in.readObject();
		port_id = (String )in.readObject();
		characteristics = (Map )in.readObject();

		transferable = new TestPort_Transferable();
		updateLocalFromTransferable();
	}

}
