package com.syrus.AMFICOM.Client.Resource.Network;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.Network.Port_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;

public class Port extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "port";

	public Port_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String interfaceId = "";
	public String addressId = "";
	public String localId = "";
	public String typeId = "";
	public String equipmentId = "";
	public String domainId = "";
	public long modified;

	public Map characteristics = new HashMap();

	public Port()
	{
		transferable = new Port_Transferable();
	}

	public Port(Port_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}
/*
	public Port(PortType portType, Equipment eq)
	{
		typeId = portType.getId();
		name = "noname";
		equipmentId = eq.getId();
		interfaceId = portType.interfaceId;
		id = eq.getId() + "." + "new_id";

		transferable = new Port_Transferable();
	}

	public Port(PortType portType, KIS kis)
	{
		type_id = portType.id;
		name = "noname";
		description = "port " + portType.codename;
		equipment_id = kis.id;
		interface_id = portType.interface_id;
		id = kis.id + "." + "new_id";

		transferable = new Port_Transferable();
	}*/
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
	public Port(
			String id,
			String name,
			String description,
			String interfaceId,
			String addressId,
			String localId,
			String typeId,
			String equipmentId)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.interfaceId = interfaceId;
		this.addressId = addressId;
		this.localId = localId;
		this.typeId = typeId;
		this.equipmentId = equipmentId;

		transferable = new Port_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		interfaceId = transferable.interfaceId;
		addressId = transferable.addressId;
		localId = transferable.localId;
		typeId = transferable._typeId;
		equipmentId = transferable.equipmentId;
		domainId = transferable.domainId;

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.interfaceId = interfaceId;
		transferable.addressId = addressId;
		transferable.localId = localId;
		transferable._typeId = typeId;
		transferable.equipmentId = equipmentId;
		transferable.domainId = domainId;

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

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new PortModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new PortDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.PortPane";
	}

	public Object clone()
	{
		return new Port(
			id,
			name,
			description,
			interfaceId,
			addressId,
			localId,
			typ,
			equipmentId);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(interfaceId);
		out.writeObject(addressId);
		out.writeObject(localId);
		out.writeObject(typeId);
		out.writeObject(equipmentId);
		out.writeObject(domainId);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		interfaceId = (String )in.readObject();
		addressId = (String )in.readObject();
		localId = (String )in.readObject();
		typeId = (String )in.readObject();
		equipmentId = (String )in.readObject();
		domainId = (String )in.readObject();
		characteristics = (Map )in.readObject();

		transferable = new Port_Transferable();
		updateLocalFromTransferable();
	}

}
