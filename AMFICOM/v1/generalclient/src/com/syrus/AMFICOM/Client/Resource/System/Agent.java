/*
 * $Id: Agent.java,v 1.3 2004/09/10 14:13:35 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.System;

import java.io.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2004/09/10 14:13:35 $
 * @module generalclient_v1
 */
public class Agent extends AdminObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "agent";
	Agent_Transferable transferable = new Agent_Transferable();

	public String id = "";
	public String name = "";
	public String version = "";
	public String licence_id = "";
	public String location = "";
	public String contact = "";
	public String hostname = "";
	public long created = 0;
	public long modified = 0;

	public Agent()
	{
	}

	public Agent(Agent_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Agent(
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

		transferable = new Agent_Transferable();
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

	public List getChildIds(String key)
	{
		return new ArrayList();
	}

	public void addChildId(String key, String id)
	{
	}
	public void removeChildId(String key, String id)
	{
	}


	public Collection getChildren(String key)
	{
		return new ArrayList();
	}

	public Collection getChildTypes()
	{
		return new ArrayList();
	}

	public static Collection getChildTypes_()
	{
		return new ArrayList();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new AgentDisplayModel();
	}

	public ObjectResourceModel getModel()
	{
		return new AgentModel(this);
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Administrate.Object.UI.AgentPane";
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

		transferable = new Agent_Transferable();
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
