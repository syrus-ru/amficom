package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.LinkType_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

public class LinkType extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "linktype";

	public LinkType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String linkClass = "";
	public String manufacturer = "";
	public String manufacturerCode = "";
	public String imageId = "";
	public long modified;

	public Map characteristics = new HashMap();

	public transient boolean is_modified = false;

	public LinkType()
	{
		transferable = new LinkType_Transferable();
	}

	public LinkType(LinkType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public LinkType(
			String id,
			String name)
	{
		this.id = id;
		this.name = name;

		transferable = new LinkType_Transferable();
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
		transferable.linkClass = linkClass;
		transferable.manufacturer = manufacturer;
		transferable.manufacturerCode = manufacturerCode;
		transferable.imageId = imageId;
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
/*
		links = new Vector();
		for(int i = 0; i < link_ids.size(); i++)
		{
			Link link = (Link )Pool.get("link", (String )link_ids.get(i));
			links.add(link);
		}
*/
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
		return new LinkTypeModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new LinkTypeDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.LinkTypePane";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(linkClass);
		out.writeObject(manufacturer);
		out.writeObject(manufacturerCode);
		out.writeObject(imageId);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		linkClass = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturerCode = (String )in.readObject();
		imageId = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		transferable = new LinkType_Transferable();
	}
}
