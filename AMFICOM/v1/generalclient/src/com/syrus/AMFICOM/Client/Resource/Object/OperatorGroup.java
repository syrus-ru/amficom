/*
 * $Id: OperatorGroup.java,v 1.5 2004/09/27 15:57:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.CORBA.Admin.OperatorGroup_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import java.io.*;
import java.util.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 15:57:42 $
 * @module generalclient_v1
 */
public class OperatorGroup extends AdminObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;

	public OperatorGroup_Transferable transferable =
			new OperatorGroup_Transferable();

	public String id="";
	public String name="";
	public String codename="";
	public String owner_id="";
	public String description="";
	public long created;
	public String created_by="";
	public long modified;
	public String modified_by="";

	public List user_ids = new ArrayList();
	public List exec_ids = new ArrayList();

	public Map users = new HashMap();
	public Map execs = new HashMap();


	static final public String typ = "operatorgroup";

	public OperatorGroup()
	{
//    super("operatorgroup");
	}

	public OperatorGroup(OperatorGroup_Transferable transferable)
	{
//    super("operatorgroup");
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public OperatorGroup(
			String id,
			String name,
			String codename,
			String owner_id,
			String description,
			long created,
			String created_by,
			long modified,
			String modified_by,
			List operator_ids,
			List role_ids,
			List privilege_ids,
			List execIds)
	{
//    super("operatorgroup");
		this.id = id;
		this.name = name;
		this.codename = codename;
		this.owner_id = owner_id;
		this.description = description;
		this.created = created;
		this.created_by = created_by;
		this.modified = modified;
		this.modified_by = modified_by;
		this.user_ids = operator_ids;
		this.exec_ids = execIds;

		transferable = new OperatorGroup_Transferable();
	}

	public void setLocalFromTransferable()
	{
		int l;
		int i;
		int count;


		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
		owner_id = transferable.owner_id;
		description = transferable.description;
		created = transferable.created;
		created_by = transferable.created_by;
		modified = transferable.modified;
		modified_by = transferable.modified_by;

		count = transferable.user_ids.length;
		user_ids = new ArrayList();
		for(i = 0; i < count; i++)
			user_ids.add(transferable.user_ids[i]);
	}

	public void setTransferableFromLocal()
	{
		int l;
		int i;
		int count;

		transferable.id = id;
		transferable.name = name;
		transferable.codename = codename;
		transferable.owner_id = owner_id;
		transferable.description = description;
		transferable.created = created;
		transferable.created_by = created_by;
		transferable.modified = modified;
		transferable.modified_by = modified_by;

		count = user_ids.size();
		transferable.user_ids = new String[count];
		for(i = 0; i < count; i++)
			transferable.user_ids[i] = (String)user_ids.get(i);

		count = exec_ids.size();
		transferable.exec_ids = new String[count];
		for(i=0; i<count; i++)
			transferable.exec_ids[i] = (String)exec_ids.get(i);
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

	public void updateLocalFromTransferable()
	{
		int l;
		int i;

		l = user_ids.size();
		users = new HashMap();

		ListIterator lIt = user_ids.listIterator();
		for(; lIt.hasNext();)
		{
			String str = (String)lIt.next();
			Object o = Pool.get(User.typ, str);
			if(o != null)
				users.put(str, o);
		}
	}

	public Collection getChildren(String key)
	{
		if(key.equals(User.typ))
			return users.values();

		return new ArrayList();
	}
	public Collection getChildTypes()
	{
		ArrayList ret = new ArrayList();
		ret.add(User.typ);

		return ret;
	}

	public List getChildIds(String key)
	{
		if(key.equals(User.typ))
			return user_ids;
		return new ArrayList();
	}

	public void addChildId(String key, String id)
	{
		if(key.equals(User.typ))
			user_ids.add(id);
	}
	public void removeChildId(String key, String id)
	{
		if(key.equals(User.typ))
			user_ids.remove(id);
	}


	public static List getChildTypes_()
	{
		ArrayList ret = new ArrayList();
		ret.add(User.typ);
		return ret;
	}


	public Class getChildClass(String type)
	{
		if(type.equals(User.typ))
			return User.class;
		return ObjectResource.class;
	}

	public static String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Administrate.Object.UI.OperatorGroupPane";
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new OperatorGroupModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new OperatorGroupDisplayModel();
	}

	public String getDomainId()
	{
		return null;
	}

	public long getModified()
	{
		return modified;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(owner_id);
		out.writeObject(description);
		out.writeLong(created);
		out.writeObject(created_by);
		out.writeLong(modified);
		out.writeObject(modified_by);
		out.writeObject(user_ids);
		out.writeObject(exec_ids);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		owner_id = (String )in.readObject();
		description = (String )in.readObject();
		created = in.readLong();
		created_by = (String )in.readObject();
		modified = in.readLong();
		modified_by = (String )in.readObject();
		user_ids = (List)in.readObject();
		exec_ids = (List)in.readObject();

		transferable = new OperatorGroup_Transferable();
		updateLocalFromTransferable();
	}

	public String getOwnerId()
	{
		return owner_id;
	}


	public void setOwnerId(String ownerID)
	{
		this.owner_id = ownerID;
	}

	public  void setModificationTime(long time)
	{
		modified = time;
	}

}
