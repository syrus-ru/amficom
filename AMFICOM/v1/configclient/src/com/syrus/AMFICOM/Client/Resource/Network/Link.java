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
// * Название: описание линии связи                                       * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.2                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\Network\Link.java                             * //
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

import java.io.Serializable;
import java.io.IOException;

import java.util.Hashtable;
import java.util.Enumeration;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Configure.UI.LinkPane;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;

import com.syrus.AMFICOM.CORBA.Network.Link_Transferable;
import com.syrus.AMFICOM.CORBA.Network.Characteristic_Transferable;

public class Link extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "link";

	public Link_Transferable transferable;

	public String id = "";
	public String name = "";
	public String type_id = "";
	public String description = "";

	public String inventory_nr = "";
	public String manufacturer = "";
	public String manufacturer_code = "";
	public String supplier = "";
	public String supplier_code = "";

	public String link_class = "";

	public String start_equipment_id = "";
	public String start_port_id = "";
	public String end_equipment_id = "";
	public String end_port_id = "";

	public String image_id = "";
	public String domain_id = "";
	public long modified;

	public double optical_length = 0.0;
	public double physical_length = 0.0;

	public Hashtable characteristics = new Hashtable();

//	public Equipment start_equipment;
//	public Port start_port;
//	public Equipment end_equipment;
//	public Port end_port;
//	public Link holder;
//	public LinkHolder holder_holder;
//	public Hashtable holders;

	public Link()
	{
		transferable = new Link_Transferable();
	}

	public Link(Link_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

//	public Link(MapPhysicalLinkElement mple, Port start_port, Port end_port)
	public Link(LinkType linktype, Port start_port, Port end_port)
	{
//		LinkType linktype = (LinkType )Pool.get(LinkType.typ, mple.link_type_id);

		id = "new_id";
		name = id;
		type_id = linktype.id;
		description = "link " + linktype.name;

		inventory_nr = "";
		manufacturer = linktype.manufacturer;
		manufacturer_code = linktype.manufacturer_code;
		supplier = "";
		supplier_code = "";

		link_class = linktype.link_class;

		if(start_port != null)
		{
			start_equipment_id = start_port.equipment_id;
			start_port_id = start_port.id;
		}

		if(end_port != null)
		{
			end_equipment_id = end_port.equipment_id;
			end_port_id = end_port.id;
		}

		image_id = linktype.image_id;

		transferable = new Link_Transferable();
	}

	public Link(
			String id,
			String name,
			String type_id)
//			Equipment start_equipment,
//			Port start_port,
//			Equipment end_equipment,
//			Port end_port)
	{
		this.id = id;
		this.name = name;
		this.type_id = type_id;
//		this.start_equipment_id = start_equipment_id;
//		this.start_port_id = start_port_id;
//		this.end_equipment_id = end_equipment_id;
//		this.end_port_id = end_port_id;

		transferable = new Link_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		type_id = transferable.type_id;

		description = transferable.description;
		inventory_nr = transferable.inventory_nr;
		manufacturer = transferable.manufacturer;
		manufacturer_code = transferable.manufacturer_code;
		supplier = transferable.supplier;
		supplier_code = transferable.supplier_code;

		link_class = transferable.link_class;

		start_equipment_id = transferable.start_equipment_id;
		start_port_id = transferable.start_port_id;
		end_equipment_id = transferable.end_equipment_id;
		end_port_id = transferable.end_port_id;

		image_id = transferable.image_id;
		domain_id = transferable.domain_id;
		modified = transferable.modified;

		try
		{
			optical_length = Double.parseDouble(transferable.optical_length);
		}
		catch (Exception ex)
		{
			optical_length = 0.0;
		}

		try
		{
			physical_length = Double.parseDouble(transferable.physical_length);
		}
		catch (Exception ex)
		{
			physical_length = 0.0;
		}

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.type_id = type_id;

		transferable.description = description;
		transferable.inventory_nr = inventory_nr;
		transferable.manufacturer = manufacturer;
		transferable.manufacturer_code = manufacturer_code;
		transferable.supplier = supplier;
		transferable.supplier_code = supplier_code;

		transferable.link_class = link_class;

		transferable.start_equipment_id = start_equipment_id;
		transferable.start_port_id = start_port_id;
		transferable.end_equipment_id = end_equipment_id;
		transferable.end_port_id = end_port_id;

		transferable.image_id = image_id;
		transferable.domain_id = domain_id;
		transferable.modified = modified;

		transferable.optical_length = String.valueOf(optical_length);
		transferable.physical_length = String.valueOf(physical_length);

		int l = this.characteristics.size();
		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[l];
		for(Enumeration e = characteristics.elements(); e.hasMoreElements();)
		{
			Characteristic ch = (Characteristic )e.nextElement();
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
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new LinkModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new LinkDisplayModel();
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new LinkPane();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(type_id);
		out.writeObject(inventory_nr);
		out.writeObject(manufacturer);
		out.writeObject(manufacturer_code);
		out.writeObject(supplier);
		out.writeObject(supplier_code);
		out.writeObject(link_class);

		out.writeObject(start_equipment_id);
		out.writeObject(start_port_id);
		out.writeObject(end_equipment_id);
		out.writeObject(end_port_id);
		out.writeObject(image_id);
		out.writeObject(domain_id);
		out.writeDouble(optical_length);
		out.writeDouble(physical_length);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		type_id = (String )in.readObject();
		inventory_nr = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturer_code = (String )in.readObject();
		supplier = (String )in.readObject();
		supplier_code = (String )in.readObject();
		link_class = (String )in.readObject();

		start_equipment_id = (String )in.readObject();
		start_port_id = (String )in.readObject();
		end_equipment_id = (String )in.readObject();
		end_port_id = (String )in.readObject();
		image_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		optical_length = in.readDouble();
		physical_length = in.readDouble();
		modified = in.readLong();
		characteristics = (Hashtable )in.readObject();

		transferable = new Link_Transferable();
		updateLocalFromTransferable();
	}
}
