// Copyright (c) 2002 Syrus
package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.MapPathProtoElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.io.IOException;
import java.io.Serializable;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
	
public class MapTransmissionPathProtoElement 
		extends MapTransmissionPathElement
		implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "mappathproto";
	public MapPathProtoElement_Transferable transferable;

	long modified;

	public Vector pathtype_ids = new Vector();

	public String domain_id = "";

	public MapTransmissionPathProtoElement()
	{
	}
	
	public MapTransmissionPathProtoElement( MapPathProtoElement_Transferable transferable)
	{
		super("", null, null, null);
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapTransmissionPathProtoElement(
			String myID, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			MapContext myMapContext)
	{
		super(myID, stNode, eNode, myMapContext);
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.owner_id = transferable.owner_id;
		this.modified = transferable.modified;
		this.domain_id = transferable.domain_id;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		this.pathtype_ids = new Vector();
		for (int i = 0; i < transferable.path_type_ids.length; i++)
			this.pathtype_ids.add(transferable.path_type_ids[i]);
	}

//этот класс используетс€ дл€ отпрвки данных в базу
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.owner_id = mapContext.user_id ;
		transferable.modified = modified;
		transferable.domain_id = domain_id;

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Enumeration e = attributes.elements(); e.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )e.nextElement();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}

		transferable.path_type_ids = new String[ this.pathtype_ids.size()];
		for (i = 0; i < transferable.path_type_ids.length; i++)
			transferable.path_type_ids[i] = (String) this.pathtype_ids.get(i);
	}

	public void updateLocalFromTransferable()
	{
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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(owner_id);
		out.writeObject(pathtype_ids);
		out.writeObject(attributes);
		out.writeObject(domain_id);
		out.writeLong(modified);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		owner_id = (String )in.readObject();
		pathtype_ids = (Vector )in.readObject();
		attributes = (Hashtable )in.readObject();
		domain_id = (String )in.readObject();
		modified = in.readLong();

		transferable = new MapPathProtoElement_Transferable();
	}
}

