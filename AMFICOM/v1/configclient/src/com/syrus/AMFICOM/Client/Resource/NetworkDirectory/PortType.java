package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.PortType_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PortType extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "porttype";

	public PortType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String interfaceId = "";
	public String pClass = "";
	public long modified;

	public transient boolean is_modified = false;

	public Map characteristics = new HashMap();

	public PortType()
	{
		transferable = new PortType_Transferable();
	}

	public PortType(PortType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public PortType(
			String id,
			String name,
			String codename,
			String description,
			String interfaceId)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		transferable.interfaceId = interfaceId;

		transferable = new PortType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		interfaceId = transferable.interfaceId;
		pClass = transferable.pClass;
		modified = transferable.modified;

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
		transferable.interfaceId = interfaceId;
		transferable.pClass = pClass;
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
		return "sysdomain";
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public long getModified()
	{
		return modified;
	}

	public ObjectResourceModel getModel()
	{
		return new PortTypeModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new PortTypeDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.PortTypePane";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(interfaceId);
		out.writeObject(pClass);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		interfaceId = (String )in.readObject();
		pClass = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		transferable = new PortType_Transferable();
	}
}
