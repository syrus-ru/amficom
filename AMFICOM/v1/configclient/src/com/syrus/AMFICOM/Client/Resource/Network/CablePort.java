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
// * Название: Описание порта оборудования                                * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.2                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\Network\Port.java                             * //
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
import com.syrus.AMFICOM.Client.Configure.UI.CablePortPane;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;

public class CablePort extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "cableport";

	public CablePort_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String interface_id = "";
	public String address_id = "";
	public String local_id = "";
	public String type_id = "";
	public String equipment_id = "";

	public String domain_id = "";

	public Map characteristics = new HashMap();

	public CablePort()
	{
		transferable = new CablePort_Transferable();
	}

	public CablePort(CablePort_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public CablePort(CablePortType portType, Equipment eq)
	{
		type_id = portType.id;
		name = "noname";
		description = "port " + portType.codename;
		equipment_id = eq.id;
		interface_id = portType.interface_id;
		id = eq.id + "." + "new_id";

		transferable = new CablePort_Transferable();
	}

/*
	public Port(MapConnectionPoint mcpe, Equipment eq)
	{

		PortType portType = (PortType )Pool.get("porttype", mcpe.port_type_id);
		id = eq.id + "." + "new_id";
		this.name = mcpe.getName();
		this.description = "port " + portType.codename;;
//		this.interface_id = mcpe.interface_id;
		interface_id = portType.interface_id;
		this.address_id = "";
		this.local_id = "";
		this.type_id = mcpe.port_type_id;
		this.equipment_id = eq.id;
		this.link_id = "";

		transferable = new Port_Transferable();
	}

	public Port(MapConnectionPoint mcpe, KIS kis)
	{
	PortType portType = (PortType )Pool.get("porttype", mcpe.port_type_id);
		id = kis.id + "." + "new_id";
		this.name = this.id;
		this.description = "port " + portType.codename;;
//		this.interface_id = mcpe.interface_id;
		interface_id = portType.interface_id;
		this.address_id = "";
		this.local_id = "";
		this.type_id = mcpe.port_type_id;
		this.equipment_id = kis.id;
		this.link_id = "";

		transferable = new Port_Transferable();
	}
*/
	public CablePort(
			String id,
			String name,
			String description,
			String interface_id,
			String address_id,
			String local_id,
			String type_id,
			String equipment_id)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.interface_id = interface_id;
		this.address_id = address_id;
		this.local_id = local_id;
		this.type_id = type_id;
		this.equipment_id = equipment_id;

		transferable = new CablePort_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		interface_id = transferable.interface_id;
		address_id = transferable.address_id;
		local_id = transferable.local_id;
		type_id = transferable.type_id;
		equipment_id = transferable.equipment_id;
		domain_id = transferable.domain_id;

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.interface_id = interface_id;
		transferable.address_id = address_id;
		transferable.local_id = local_id;
		transferable.type_id = type_id;
		transferable.equipment_id = equipment_id;
		transferable.domain_id = domain_id;

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
		return domain_id;
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new CablePortModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new CablePortDisplayModel();
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new CablePortPane();
//		return new CablePortGeneralPanel();
	}

	public Object clone()
	{
		return new CablePort(
			id,
			name,
			description,
			interface_id,
			address_id,
			local_id,
			type_id,
			equipment_id);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(interface_id);
		out.writeObject(address_id);
		out.writeObject(local_id);
		out.writeObject(type_id);
		out.writeObject(equipment_id);
		out.writeObject(domain_id);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		interface_id = (String )in.readObject();
		address_id = (String )in.readObject();
		local_id = (String )in.readObject();
		type_id = (String )in.readObject();
		equipment_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		characteristics = (Map )in.readObject();

		transferable = new CablePort_Transferable();
		updateLocalFromTransferable();
	}

}
