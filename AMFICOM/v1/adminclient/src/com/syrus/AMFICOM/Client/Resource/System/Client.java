package com.syrus.AMFICOM.Client.Resource.System;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;

public class Client extends AdminObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "client";

	Client_Transferable transferable = new Client_Transferable();

	public String id = "";
	public String name = "";
	public String version = "";
	public String licence_id = "";
	public String location = "";
	public String contact = "";
	public String hostname = "";
	public long created = 0;
	public long modified = 0;

	public Client()
	{
	}

	public Client(Client_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Client(
		String id,
		String name,
		String version,
		String licence_id,
		String location,
		String contact,
		String hostname,
		long created,
		long modified)
	{
		this.id = id;
		this.name = name;
		this.version = version;
		this.licence_id = licence_id;
		this.location = location;
		this.contact = contact;
		this.hostname = hostname;
		this.created = created;
		this.modified = modified;

		transferable = new Client_Transferable();
	}

	public void updateLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.version = version;
		transferable.licence_id = licence_id;
		transferable.location = location;
		transferable.contact = contact;
		transferable.hostname = hostname;
		transferable.created = created;
		transferable.modified = modified;
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.version = transferable.version;
		this.licence_id = transferable.licence_id;
		this.location = transferable.location;
		this.contact = transferable.contact;
		this.hostname = transferable.hostname;
		this.created = transferable.created;
		this.modified = transferable.modified;
	}

	public String getDomainId()
	{
		return null;
	}

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return name;
	}

	public Object getTransferable()
	{
		return transferable;
	}

/*--------------------------------------------------------------------*/

	public Class getChildClass(String type)
	{
		return ObjectResource.class;
	}

  public Vector getChildIds(String key)
  {
    return new Vector();
  }

  public void addChildId(String key, String id)
  {
  }
  public void removeChildId(String key, String id)
  {
  }


	public Enumeration getChildren(String key)
	{
		return new Vector().elements();
	}

	public Enumeration getChildTypes()
	{
		return new Vector().elements();
	}

  public static Vector getChildTypes_()
  {
    return new Vector();
  }

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new ClientDisplayModel();
	}

	public ObjectResourceModel getModel()
	{
		return new ClientModel(this);
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new ClientPane();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(version);
		out.writeObject(licence_id);
		out.writeObject(location);
		out.writeObject(contact);
		out.writeObject(hostname);
		out.writeLong(created);
		out.writeLong(modified);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		version = (String )in.readObject();
		licence_id = (String )in.readObject();
		location = (String )in.readObject();
		contact = (String )in.readObject();
		hostname = (String )in.readObject();
		created = in.readLong();
		modified = in.readLong();

		transferable = new Client_Transferable();
		updateLocalFromTransferable();
	}

  public String getTyp()
  {
    return typ;
  }

  public String getOwnerId()
  {
    return "";
  }

  public void setOwnerId(String ownerID)
  {
  }

  public  void setModificationTime(long time)
  {
    modified = time;
  }

}
