package com.syrus.AMFICOM.Client.Resource.Network;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.Network.Link_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;

public class Link extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "link";

	public Link_Transferable transferable;

	public String id = "";
	public String name = "";
	public String typeId = "";
	public String description = "";

	public String inventoryNr = "";
	public String manufacturer = "";
	public String manufacturerCode = "";
	public String supplier = "";
	public String supplierCode = "";

	public String domainId = "";
	public long modified;

	public Map characteristics = new HashMap();

	public Link()
	{
		transferable = new Link_Transferable();
	}

	public Link(Link_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Link(LinkType linktype)
	{
		id = "new_id";
		name = id;
		typeId = linktype.getId();
		description = "link " + linktype.name;

		manufacturer = linktype.manufacturer;
		manufacturerCode = linktype.manufacturerCode;

		transferable = new Link_Transferable();
	}

	public Link(
			String id,
			String name,
			String typeId)
	{
		this.id = id;
		this.name = name;
		this.typeId = typeId;

		transferable = new Link_Transferable();
	}

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
		return new LinkModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new LinkDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.LinkPane";
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
		domainId = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		transferable = new Link_Transferable();
		updateLocalFromTransferable();
	}
}
