package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CableLinkType_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CableTypeThread_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CableLinkType extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "cablelinktype";

	public CableLinkType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String linkClass = "";
	public String manufacturer = "";
	public String manufacturerCode = "";

//	public transient boolean is_modified = false;

	public String imageId = "";
	public long modified;

	public Map characteristics = new HashMap();
	public List cableThreads = new ArrayList();

	public CableLinkType()
	{
		transferable = new CableLinkType_Transferable();
	}

	public CableLinkType(CableLinkType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public CableLinkType(
			String id,
			String name)
	{
		this.id = id;
		this.name = name;

		transferable = new CableLinkType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		linkClass = transferable.linkClass;
		manufacturer = transferable.manufacturer;
		manufacturerCode = transferable.manufacturerCode;
		imageId = transferable.imageId;
		modified = transferable.modified;

		for (int i = 0; i < transferable.cableThreads.length; i++)
			cableThreads.add(new CableTypeThread(transferable.cableThreads[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.linkClass = linkClass;
		transferable.manufacturer = manufacturer;
		transferable.manufacturerCode = manufacturerCode;
		transferable.imageId = imageId;
		transferable.modified = modified;

		transferable.cableThreads = new CableTypeThread_Transferable[cableThreads.size()];

		int counter = 0;
		for (Iterator it = cableThreads.iterator(); it.hasNext();)
		{
			CableTypeThread thread = (CableTypeThread)it.next();
			thread.setTransferableFromLocal();
			transferable.cableThreads[counter++] = (CableTypeThread_Transferable)thread.getTransferable();
		}

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

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new CableLinkTypeDisplayModel();
	}

	public ObjectResourceModel getModel()
	{
		return new CableLinkTypeModel(this);
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.CableLinkTypePane";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(linkClass);
		out.writeObject(manufacturer);
		out.writeObject(manufacturerCode);
		out.writeObject(imageId);
		out.writeLong(modified);
		out.writeObject(characteristics);

		out.writeObject(cableThreads);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		linkClass = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturerCode = (String )in.readObject();
		imageId = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		cableThreads = (List )in.readObject();

		transferable = new CableLinkType_Transferable();
	}
}
