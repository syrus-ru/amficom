package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.CORBA.Scheme.MapProtoGroup_Transferable;

public class MapProtoGroup extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	MapProtoGroup_Transferable transferable;
	
	public static final String typ = "mapprotogroup";

	public String id = "";
	public String name = "";
	public String parent_id = "";
	public Vector group_ids = new Vector();
	public Vector mapproto_ids = new Vector();
	public long modified = 0L;

	public MapProtoGroup()
	{
		transferable = new MapProtoGroup_Transferable();
	}

	public MapProtoGroup(MapProtoGroup_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
        this.id = transferable.id;
        this.name = transferable.name;
        this.parent_id = transferable.parent_id;
		this.modified = transferable.modified;

		this.group_ids = new Vector();
		for (int i = 0; i < transferable.group_ids.length; i++)
			this.group_ids.add( transferable.group_ids[i]);

		this.mapproto_ids = new Vector();
		for (int i = 0; i < transferable.mapproto_ids.length; i++)
			this.mapproto_ids.add( transferable.mapproto_ids[i]);
	}

	public void setTransferableFromLocal()
	{
		int l;
		int i;
		
        transferable.id = id;
        transferable.name = name;
		transferable.parent_id = parent_id;
		transferable.modified = modified;

        l = this.group_ids.size();
		transferable.group_ids = new String[l];
		for (i = 0; i < group_ids.size(); i++)
			transferable.group_ids[i] = (String )group_ids.get(i);

        l = this.mapproto_ids.size();
		transferable.mapproto_ids = new String[l];
		for (i = 0; i < mapproto_ids.size(); i++)
			transferable.mapproto_ids[i] = (String )mapproto_ids.get(i);
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getTyp()
	{
		return typ;
	}

	public long getModified()
	{
		return modified;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}
	
	public Object getTransferable()
	{
		return transferable;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(parent_id);
		out.writeLong(modified);
		out.writeObject(group_ids);
		out.writeObject(mapproto_ids);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		parent_id = (String )in.readObject();

		modified = in.readLong();
		group_ids = (Vector )in.readObject();
		mapproto_ids = (Vector )in.readObject();

		transferable = new MapProtoGroup_Transferable();
	}

}