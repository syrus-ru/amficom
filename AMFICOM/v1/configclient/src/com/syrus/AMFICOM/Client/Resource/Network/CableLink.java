package com.syrus.AMFICOM.Client.Resource.Network;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.Network.CableLink_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CableLink extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "cablelink";

	public CableLink_Transferable transferable;

	public String id = "";
	public String name = "";
	public String typeId = "";
	public String description = "";

	public String inventoryNr = "";
	public String manufacturer = "";
	public String manufacturerCode = "";
	public String supplier = "";
	public String supplierCode = "";
	public String constructor = "";
	public String constructorCode = "";

	public String imageId = "";
	public String domainId = "";

	public long modified;

	public Map characteristics = new HashMap();

	public CableLink()
	{
		transferable = new CableLink_Transferable();
	}

	public CableLink(CableLink_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}
/*
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
		is_holder = linktype.is_holder;
		holder_id = "";
		holder_holder_id = "";

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
*/
	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		typeId = transferable._typeId;

		description = transferable.description;
		inventoryNr = transferable.inventoryNr;
		manufacturer = transferable.manufacturer;
		manufacturerCode = transferable.manufacturerCode;
		supplier = transferable.supplier;
		supplierCode = transferable.supplierCode;
		constructor = transferable.constructor;
		constructorCode = transferable.constructorCode;

		imageId = transferable.imageId;
		domainId = transferable.domainId;
		modified = transferable.modified;

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable._typeId = typeId;

		transferable.description = description;
		transferable.inventoryNr = inventoryNr;
		transferable.manufacturer = manufacturer;
		transferable.manufacturerCode = manufacturerCode;
		transferable.supplier = supplier;
		transferable.supplierCode = supplierCode;
		transferable.constructor = constructor;
		transferable.constructorCode = constructorCode;

		transferable.imageId = imageId;
		transferable.domainId = domainId;
		transferable.modified = modified;

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
		return domainId;
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
		return new CableLinkModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new CableLinkDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.CableLinkPane";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(typeId);
		out.writeObject(inventoryNr);
		out.writeObject(manufacturer);
		out.writeObject(manufacturerCode);
		out.writeObject(supplier);
		out.writeObject(supplierCode);

		out.writeObject(imageId);
		out.writeObject(domainId);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		typeId = (String )in.readObject();
		inventoryNr = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturerCode = (String )in.readObject();
		supplier = (String )in.readObject();
		supplierCode = (String )in.readObject();

		imageId = (String )in.readObject();
		domainId = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		transferable = new CableLink_Transferable();
		updateLocalFromTransferable();
	}

}

