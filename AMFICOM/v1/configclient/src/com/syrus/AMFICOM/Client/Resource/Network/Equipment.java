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
// * Название: описание оборудования                                      * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.2                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\Network\Equipment.java                        * //
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
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;

public class Equipment extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "equipment";

	public Equipment_Transferable transferable;

	public String id = ""; //
	public String name = "";//
	public String description = "";//
	public String type_id = "";//

	public String longitude = "";
	public String latitude = "";

	public String hw_serial = "";
	public String sw_serial = "";
	public String hw_version = "";
	public String sw_version = "";
	public String inventory_nr = "";
	public String manufacturer = "";
	public String manufacturer_code = "";
	public String supplier = "";
	public String supplier_code = "";

	public String eq_class = "";
	public boolean is_kis;
	public String agent_id = "";
	public String domain_id = "";

	public String image_id = "";

	public long modified;

	public Collection port_ids = new ArrayList();
	public Collection cport_ids = new ArrayList();
	public Collection s_port_ids = new ArrayList();

	public Collection ports = new ArrayList();
	public Collection cports = new ArrayList();
	public Collection test_ports = new ArrayList();

	public Map characteristics = new HashMap();

	public Equipment()
	{
		transferable = new Equipment_Transferable();
	}

	public Equipment(Equipment_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Equipment(EquipmentType eq_type)
	{
		id = "new_id";
		name = eq_type.name;
		type_id = eq_type.id;
		longitude = "0.0";
		latitude = "0.0";

		hw_serial = "";
		sw_serial = "";
		hw_version = "";
		sw_version = "";
		description = "equipment " + eq_type.name;
		inventory_nr = "";
		manufacturer = eq_type.manufacturer;
		manufacturer_code = "";
		supplier = "";
		supplier_code = "";

		eq_class = eq_type.eq_class;

		image_id = eq_type.image_id;

		ports = new ArrayList();
		cports = new ArrayList();
		test_ports = new ArrayList();

		characteristics = new HashMap();

		transferable = new Equipment_Transferable();
	}

	public Equipment(
			String id,
			String name,
			String type_id,
			String longitude,
			String latitude,

			String hw_serial,
			String sw_serial,
			String hw_version,
			String sw_version,
			String description,
			String inventory_nr,
			String manufacturer,
			String manufacturer_code,
			String supplier,
			String supplier_code,

			Map characteristics,
			boolean is_holder,

			String holder_id,
			String holder_slot_id)
	{
		this.id = id;
		this.name = name;
		this.type_id = type_id;
		this.longitude = longitude;
		this.latitude = latitude;

		this.hw_serial = hw_serial;
		this.sw_serial = sw_serial;
		this.hw_version = hw_version;
		this.sw_version = sw_version;
		this.description = description;
		this.inventory_nr = inventory_nr;
		this.manufacturer = manufacturer;
		this.manufacturer_code = manufacturer_code;
		this.supplier = supplier;
		this.supplier_code = supplier_code;

		this.eq_class = eq_class;

//		this.ports = ports;
//		this.characteristics = characteristics;
//		this.test_ports = test_ports;
//		this.slots = slots;
//		this.holder = holder;
//		this.holder_slot = holder_slot;

		transferable = new Equipment_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		type_id = transferable.type_id;
		longitude = transferable.longitude;
		latitude = transferable.latitude;

		hw_serial = transferable.hw_serial;
		sw_serial = transferable.sw_serial;
		hw_version = transferable.hw_version;
		sw_version = transferable.sw_version;
		description = transferable.description;
		inventory_nr = transferable.inventory_nr;
		manufacturer = transferable.manufacturer;
		manufacturer_code = transferable.manufacturer_code;
		supplier = transferable.supplier;
		supplier_code = transferable.supplier_code;

		eq_class = transferable.eq_class;
		is_kis = transferable.is_kis;
		agent_id = transferable.agent_id;
		domain_id = transferable.domain_id;

		image_id = transferable.image_id;

		modified = transferable.modified;

		MiscUtil.addToCollection(port_ids, transferable.port_ids);
		MiscUtil.addToCollection(cport_ids, transferable.cport_ids);
		MiscUtil.addToCollection(s_port_ids, transferable.s_port_ids);

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
		transferable.longitude = longitude;
		transferable.latitude = latitude;

		transferable.hw_serial = hw_serial;
		transferable.sw_serial = sw_serial;
		transferable.hw_version = hw_version;
		transferable.sw_version = sw_version;
		transferable.description = description;
		transferable.inventory_nr = inventory_nr;
		transferable.manufacturer = manufacturer;
		transferable.manufacturer_code = manufacturer_code;
		transferable.supplier = supplier;
		transferable.supplier_code = supplier_code;

		transferable.eq_class = eq_class;
		transferable.is_kis = is_kis;
		transferable.agent_id = agent_id;
		transferable.domain_id = domain_id;

		transferable.image_id = image_id;

		transferable.modified = modified;

		transferable.port_ids = (String[])port_ids.toArray(new String[port_ids.size()]);
		transferable.cport_ids = (String[])cport_ids.toArray(new String[cport_ids.size()]);
		transferable.s_port_ids = (String[])s_port_ids.toArray(new String[s_port_ids.size()]);

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

	public long getModified()
	{
		return modified;
	}

	public void updateLocalFromTransferable()
	{
		ports = new ArrayList();
		cports = new ArrayList();
		test_ports = new ArrayList();

		for(Iterator it = port_ids.iterator(); it.hasNext();)
			ports.add( Pool.get(Port.typ, (String)it.next()) );
		for(Iterator it = cport_ids.iterator(); it.hasNext();)
			cports.add( Pool.get(CablePort.typ, (String)it.next()) );
		for(Iterator it = s_port_ids.iterator(); it.hasNext();)
			test_ports.add( Pool.get(TestPort.typ, (String)it.next()) );
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new EquipmentModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new EquipmentDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.EquipmentPane";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(type_id);
		out.writeObject(longitude);
		out.writeObject(latitude);
		out.writeObject(hw_serial);
		out.writeObject(sw_serial);
		out.writeObject(hw_version);
		out.writeObject(sw_version);
		out.writeObject(inventory_nr);
		out.writeObject(manufacturer);
		out.writeObject(manufacturer_code);
		out.writeObject(supplier);
		out.writeObject(supplier_code);
		out.writeObject(eq_class);
		out.writeBoolean(is_kis);
		out.writeObject(agent_id);
		out.writeObject(domain_id);
		out.writeObject(image_id);
		out.writeLong(modified);
		out.writeObject(port_ids);
		out.writeObject(cport_ids);
		out.writeObject(s_port_ids);
		out.writeObject(ports);
		out.writeObject(cports);
		if(this instanceof KIS)
			out.writeObject(((KIS )this).access_ports);
		else
			out.writeObject(test_ports);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		type_id = (String )in.readObject();
		longitude = (String )in.readObject();
		latitude = (String )in.readObject();
		hw_serial = (String )in.readObject();
		sw_serial = (String )in.readObject();
		hw_version = (String )in.readObject();
		sw_version = (String )in.readObject();
		inventory_nr = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturer_code = (String )in.readObject();
		supplier = (String )in.readObject();
		supplier_code = (String )in.readObject();
		eq_class = (String )in.readObject();
		is_kis = in.readBoolean();
		agent_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		image_id = (String )in.readObject();
		modified = in.readLong();
		port_ids = (Collection)in.readObject();
		cport_ids = (Collection)in.readObject();
		s_port_ids = (Collection)in.readObject();
		ports = (Collection)in.readObject();
		cports = (Collection)in.readObject();
		if(this instanceof KIS)
			((KIS )this).access_ports = (Collection )in.readObject();
		else
			test_ports = (Collection )in.readObject();
		characteristics = (Map )in.readObject();

		for(Iterator it = ports.iterator(); it.hasNext();)
		{
			Port port = (Port)it.next();
			Pool.put(Port.typ, port.getId(), port);
		}

		for(Iterator it = cports.iterator(); it.hasNext();)
		{
			CablePort port = (CablePort )it.next();
			Pool.put(CablePort.typ, port.getId(), port);
		}

		if(this instanceof KIS)
			for(Iterator it = ((KIS )this).access_ports.iterator(); it.hasNext();)
			{
				AccessPort port = (AccessPort)it.next();
				Pool.put(AccessPort.typ, port.getId(), port);
			}
		else
			for(Iterator it = test_ports.iterator(); it.hasNext();)
			{
				TestPort port = (TestPort)it.next();
				Pool.put(TestPort.typ, port.getId(), port);
			}

		transferable = new Equipment_Transferable();

		updateLocalFromTransferable();
	}

}
