package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CablePortType_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

public class CablePortType extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "cableporttype";

	public CablePortType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String interfaceId = "";
	public String pClass = "";
	public long modified = 0;

	public transient boolean is_modified = false;

	public Map characteristics = new HashMap();

	public CablePortType()
	{
		transferable = new CablePortType_Transferable();
	}

	public CablePortType(CablePortType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public CablePortType(
			String id,
			String name,
			String description,
			String interface_id)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		transferable.interfaceId = interface_id;

		transferable = new CablePortType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		interfaceId = transferable.interfaceId;
		modified = transferable.modified;
		pClass = transferable.pClass;

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
		transferable.modified = modified;
		transferable.pClass = pClass;

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
		return new CablePortTypeModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new CablePortTypeDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.CablePortTypePane";
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

		transferable = new CablePortType_Transferable();
	}
}
