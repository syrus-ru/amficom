/*
 * $Id: OperatorProfile.java,v 1.5 2004/09/27 15:41:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.CORBA.Admin.OperatorProfile_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.io.Rewriter;
import java.io.*;
import java.util.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 15:41:49 $
 * @module generalclient_v1
 */
public class OperatorProfile extends AdminObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;

	public OperatorProfile_Transferable transferable =
				 new OperatorProfile_Transferable();

// begin of the privilegies fields
	public String id="";
	public String name="";
	public String codename="";
	public String user_id="";
	public String owner_id="";
	public String description="";
	public long created;
	public String created_by="";
	public long modified;
	public String modified_by="";

	public String login="";
	public String password="";

	public long last_login;
	public String status="";
	public String state="";


	public long disabled;
	public String disabled_comments="";

	public String priority="";
	public String logfile="";


	public String first_name="";
	public String second_name="";
	public String last_name="";
	public String phone_work="";
	public String phone_home="";
	public String phone_mobile="";
	public String phone_emergency="";

	public String pager_phone="";
	public String pager_number="";
	public String sms_number="";

	public String address="";
	public String language="";
	public String organization="";
	public String e_mail="";

// End of the privelegies fields
	public List category_ids = new ArrayList();
	public List group_ids = new ArrayList();

	public Map categories = new HashMap();
	public Map groups = new HashMap();

	static final public String typ = "operatorprofile";

	public OperatorProfile()
	{
//		super("operatorprofile");
	}

	public OperatorProfile(OperatorProfile_Transferable transferable)
	{
//		super("operatorprofile");
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public OperatorProfile(
			String id,
			String name,
			String codename,
			String user_id,
			String description,
			long created,
			String created_by,
			long modified,
			String modified_by)
	{
//		super("operatorprofile");

		this.id = id;
		this.name = name;
		this.codename = codename;
		this.user_id = user_id;
		this.description = description;
		this.created = created;
		this.created_by = created_by;
		this.modified = modified;
		this.modified_by = modified_by;

		transferable = new OperatorProfile_Transferable();
	}

	public void setLocalFromTransferable()
	{
		int l;
		int i;
		int count;

		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
		user_id = transferable.user_id;
		owner_id = transferable.owner_id;
		description = transferable.description;
		created = transferable.created;
		created_by = transferable.created_by;
		modified = transferable.modified;
		modified_by = transferable.modified_by;

		login = transferable.login;
		password = Rewriter.read(transferable.password);

		last_login = transferable.last_login;
		status = transferable.status;
		state = transferable.state;
		disabled = transferable.disabled;
		disabled_comments = transferable.disabled_comments;
		priority = transferable.priority;
		logfile = transferable.logfile;
		first_name = transferable.first_name;
		second_name = transferable.second_name;
		last_name = transferable.last_name;
		phone_work = transferable.phone_work;
		phone_home = transferable.phone_home;
		phone_mobile = transferable.phone_mobile;
		phone_emergency = transferable.phone_emergency;
		pager_phone = transferable.pager_phone;
		pager_number = transferable.pager_number;
		sms_number = transferable.sms_number;
		address = transferable.address;
		language = transferable.language;
		organization = transferable.organization;
		e_mail = transferable.e_mail;

		count = transferable.category_ids.length;
		category_ids = new ArrayList();
		for(i = 0; i < count; i++)
			category_ids.add(transferable.category_ids[i]);


		count = transferable.group_ids.length;
		group_ids = new ArrayList();
		for(i = 0; i < count; i++)
			group_ids.add(transferable.group_ids[i]);
	}

	public void setTransferableFromLocal()
	{
		int l;
		int i;
		int count;

		transferable.id = id;
		transferable.name = name;
		transferable.codename = codename;
		transferable.user_id = user_id;
		transferable.owner_id = owner_id;
		transferable.description = description;
		transferable.created = created;
		transferable.created_by = created_by;
		transferable.modified = modified;
		transferable.modified_by = modified_by;

		transferable.login = login;
		transferable.password = Rewriter.write(password);
		transferable.last_login = last_login;
		transferable.status = status;
		transferable.state = state;
		transferable.disabled = disabled;
		transferable.disabled_comments = disabled_comments;
		transferable.priority = priority;
		transferable.logfile = logfile;
		transferable.first_name = first_name;
		transferable.second_name = second_name;
		transferable.last_name = last_name;
		transferable.phone_work = phone_work;
		transferable.phone_home = phone_home;
		transferable.phone_mobile = phone_mobile;
		transferable.phone_emergency = phone_emergency;
		transferable.pager_phone = pager_phone;
		transferable.pager_number = pager_number;
		transferable.sms_number = sms_number;
		transferable.address = address;
		transferable.language = language;
		transferable.organization = organization;
		transferable.e_mail = e_mail;

		count = category_ids.size();
		transferable.category_ids = new String[count];
		for(i = 0; i < count; i++)
			transferable.category_ids[i] = (String)category_ids.get(i);

								count = group_ids.size();
		transferable.group_ids = new String[count];
		for(i=0; i<count; i++)
		{
			transferable.group_ids[i] = (String)group_ids.get(i);
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

	public void updateLocalFromTransferable()
	{
		categories = new HashMap();
		for(Iterator it = category_ids.iterator();it.hasNext();)
		{
			String str = (String)it.next();
			Object o = Pool.get(OperatorCategory.typ, str);
			if(o != null)
				categories.put(str, o);
		}

		groups = new HashMap();
		for(Iterator it = group_ids.iterator();it.hasNext();)
		{
			String str = (String)it.next();
			Object o = Pool.get(OperatorGroup.typ, str);
			if(o != null)
				groups.put(str, o);
		}
	}

	public List getChildIds(String key)
	{
		if(key.equals(OperatorGroup.typ))
			return group_ids;
		if(key.equals(OperatorCategory.typ))
			return category_ids;
		return new ArrayList();
	}

	public void addChildId(String key, String id)
	{
		if(key.equals(OperatorGroup.typ))
			group_ids.add(id);
		if(key.equals(OperatorCategory.typ))
			category_ids.add(id);
	}
	public void removeChildId(String key, String id)
	{
		if(key.equals(OperatorGroup.typ))
			group_ids.remove(id);
		if(key.equals(OperatorCategory.typ))
			category_ids.remove(id);
	}


	public Collection getChildren(String key)
	{
		if(key.equals(OperatorGroup.typ))
		{
			return groups.values();
		}
		if(key.equals(OperatorCategory.typ))
		{
			return categories.values();
		}
		return new ArrayList();
	}

	public Collection getChildTypes()
	{
		List ret = new ArrayList();
		ret.add(OperatorCategory.typ);
		ret.add(OperatorGroup.typ);
		return ret;
	}

	public static Collection getChildTypes_()
	{
		List ret = new ArrayList();
		ret.add(OperatorCategory.typ);
		ret.add(OperatorGroup.typ);
		return ret;
	}


	public Class getChildClass(String type)
	{
		if(type.equals(OperatorCategory.typ))
		{
			return OperatorCategory.class;
		}
		if(type.equals(OperatorGroup.typ))
		{
			return OperatorGroup.class;
		}
			return ObjectResource.class;
	}

	public static String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Administrate.Object.UI.OperatorProfilePane";
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
				{
					return new OperatorProfileModel(this);
				}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
			 {
				return new OperatorProfileDisplayModel();
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
		out.writeObject(user_id);
		out.writeObject(owner_id);
		out.writeObject(description);
		out.writeLong(created);
		out.writeObject(created_by);
		out.writeLong(modified);
		out.writeObject(modified_by);

		out.writeObject(login);
		out.writeObject(password);
		out.writeLong(last_login);
		out.writeObject(status);
		out.writeObject(state);
		out.writeLong(disabled);
		out.writeObject(disabled_comments);
		out.writeObject(priority);
		out.writeObject(logfile);
		out.writeObject(first_name);
		out.writeObject(second_name);
		out.writeObject(last_name);
		out.writeObject(phone_work);
		out.writeObject(phone_home);
		out.writeObject(phone_mobile);
		out.writeObject(phone_emergency);
		out.writeObject(pager_phone);
		out.writeObject(pager_number);
		out.writeObject(sms_number);
		out.writeObject(address);
		out.writeObject(language);
		out.writeObject(organization);
		out.writeObject(e_mail);

		out.writeObject(category_ids);
		out.writeObject(group_ids);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		user_id = (String )in.readObject();
		owner_id = (String )in.readObject();
		description = (String )in.readObject();
		created = in.readLong();
		created_by = (String )in.readObject();
		modified = in.readLong();
		modified_by = (String )in.readObject();

		login = (String )in.readObject();
		password = (String )in.readObject();
		last_login = in.readLong();
		status = (String )in.readObject();
		state = (String )in.readObject();
		disabled = in.readLong();
		disabled_comments = (String )in.readObject();
		priority = (String )in.readObject();
		logfile = (String )in.readObject();
		first_name = (String )in.readObject();
		second_name = (String )in.readObject();
		last_name = (String )in.readObject();
		phone_work = (String )in.readObject();
		phone_home = (String )in.readObject();
		phone_mobile = (String )in.readObject();
		phone_emergency = (String )in.readObject();
		pager_phone = (String )in.readObject();
		pager_number = (String )in.readObject();
		sms_number = (String )in.readObject();
		address = (String )in.readObject();
		language = (String )in.readObject();
		organization = (String )in.readObject();
		e_mail = (String )in.readObject();

		category_ids = (List)in.readObject();
		group_ids = (List)in.readObject();

		transferable = new OperatorProfile_Transferable();
		updateLocalFromTransferable();
	}

	public String getOwnerId()
	{
		return owner_id;
	}

	public  void setModificationTime(long time)
	{
		modified = time;
	}

	public void setOwnerId(String ownerID)
	{
		this.owner_id = ownerID;
	}


}
