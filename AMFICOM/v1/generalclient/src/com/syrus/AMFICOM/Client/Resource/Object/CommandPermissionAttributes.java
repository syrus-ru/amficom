/*
 * $Id: CommandPermissionAttributes.java,v 1.7 2004/09/27 15:45:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.CORBA.Admin.CommandPermissionAttributes_Transferable;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/09/27 15:45:16 $
 * @module generalclient_v1
 */
public class CommandPermissionAttributes extends AdminObjectResource implements AdminObjectRWXresource
{
	static final public String typ = "comm_perm_attrib";
	public String filterGroup = "other";

	public String id = "";               //1 -- Identificator of the object, for which
	//     we set the attributes.
	public boolean userR=false;     //2 -- permittion to read for owner of the object
	public boolean userW=false;     //3 --    -|- to write   -|-
	public boolean userX=false;     //4 --    -|- to execute -|-

	public boolean groupR=false;    //5 ... for group
	public boolean groupW=false;    //6 ... for group
	public boolean groupX=false;    //7 ... for group

	public boolean otherR=false;    //8  ... for other
	public boolean otherW=false;    //9  ... for other
	public boolean otherX=false;    //10 ... for other

	public List category_ids = new ArrayList();
	public List group_ids = new ArrayList();

	public Map categories = new HashMap();
	public Map groups = new HashMap();

	public String owner_id="";
	public String name="";
	public String codename="";
	public long created;
	public String created_by="";
	public long modified;
	public String modified_by="";
	public CommandPermissionAttributes_Transferable transferable =
			new CommandPermissionAttributes_Transferable();

	public String whyRejected = "Ошибка доступа!";

	public CommandPermissionAttributes() // Simplest case. When the object is created
			// for the first time. All attributes MUST be
	// using appropriate functions.
	{
//    super("comm_perm_attrib");
		this.id = "";
		this.owner_id = "";
		this.name = "";
	}
	public CommandPermissionAttributes(CommandPermissionAttributes_Transferable transferable)
	{
//    super("comm_perm_attrib");
		this.transferable = transferable;
		setLocalFromTransferable();
	}


	public void SetUserR(boolean t)
	{
		userR=t;
	}
	public void SetUserW(boolean t)
	{
		userW=t;
	}
	public void SetUserX(boolean t)
	{
		userX=t;
	}
	public void SetGroupR(boolean t)
	{
		groupR=t;
	}
	public void SetGroupW(boolean t)
	{
		groupW=t;
	}
	public void SetGroupX(boolean t)
	{
		groupX=t;
	}
	public void SetOtherR(boolean t)
	{
		otherR=t;
	}
	public void SetOtherW(boolean t)
	{
		otherW=t;
	}
	public void SetOtherX(boolean t)
	{
		otherX=t;
	}

	public void SetAllR(boolean t)
	{
		userR=t;
		groupR=t;
		otherR=t;
	}
	public void SetAllW(boolean t)
	{
		userW=t;
		groupW=t;
		otherW=t;
	}
	public void SetAllX(boolean t)
	{
		userX=t;
		groupX=t;
		otherX=t;
	}
	public void SetRWX(boolean t)
	{
		userR=t;
		groupR=t;
		otherR=t;
		userW=t;
		groupW=t;
		otherW=t;
		userX=t;
		groupX=t;
		otherX=t;
	}

	public boolean getUserR()
	{
		return userR;
	}

	public boolean getUserW()
	{
		return userW;
	}

	public boolean getUserX()
	{
		return userX;
	}

	public boolean getGroupR()
	{
		return groupR;
	}

	public boolean getGroupW()
	{
		return groupW;
	}

	public boolean getGroupX()
	{
		return groupX;
	}

	public boolean getOtherR()
	{
		return otherR;
	}

	public boolean getOtherW()
	{
		return otherW;
	}

	public boolean getOtherX()
	{
		return otherX;
	}



	public void SetOwner(String owner_id)
	{
		this.owner_id = owner_id;
	}
	public void SetID(String ID)
	{
		this.id = ID;
	}
	public void SetName(String name)
	{
		this.name = name;
	}
	public void SetCodename(String codename)
	{
		this.codename = codename;
	}

//////////////////////////
	public  Object getTransferable()
	{
		return transferable;
	}

	public  String getName()
	{
		return this.name;
	}

	public  String getId()
	{
		return this.id;
	}

	public  void setLocalFromTransferable()
	{
		int l;
		int i;
		int count;
		this.codename = transferable.codename;
		this.created = transferable.created;
		this.created_by = transferable.created_by;
		this.id = transferable.id;
		this.modified = transferable.modified;
		this.modified_by = transferable.modified_by;
		this.name = transferable.name;
		this.owner_id = transferable.owner_id;
		this.whyRejected = transferable.whyRejected;
		this.setRWXstateFromString(transferable.rwx);

		count = transferable.category_ids.length;
		this.category_ids = new ArrayList();
		for(i=0; i<count; i++)
			this.category_ids.add(transferable.category_ids[i]);


		if(codename.startsWith(Checker.admin)) // Administrating
		{
			this.filterGroup = Checker.admin;
		}
		else if(codename.startsWith(Checker.ana)) // Analysis
		{
			this.filterGroup = Checker.ana;
		}
		else if(codename.startsWith(Checker.model)) // Modeling
		{
			this.filterGroup = Checker.model;
		}
		else if(codename.startsWith(Checker.plan)) // Planer
		{
			this.filterGroup = Checker.plan;
		}
		else if(codename.startsWith(Checker.optim)) //Optimizer
		{
			this.filterGroup = Checker.optim;
		}
		else if(codename.startsWith(Checker.observ)) // Observing
		{
			this.filterGroup = Checker.observ;
		}
		else if(codename.startsWith(Checker.config)) //Configuring
		{
			this.filterGroup = Checker.config;
		}
		else if(codename.startsWith(Checker.predict)) //Prediction
		{
			this.filterGroup = Checker.predict;
		}
		else
		{
			this.filterGroup = "other";
		}

	}

	public  void setTransferableFromLocal()
	{
		int l;
		int i;
		int count;
		transferable.codename = codename;
		transferable.created = created;
		transferable.created_by = created_by;
		transferable.id = id;
		transferable.modified = modified;
		transferable.modified_by = modified_by;
		transferable.name = name;
		transferable.owner_id = owner_id;
		transferable.whyRejected = whyRejected;
		transferable.rwx = this.returnRWXstateString();

		count = category_ids.size();
		transferable.category_ids = new String[count];
		for(i=0; i<count; i++)
			transferable.category_ids[i] = (String)category_ids.get(i);

		count = group_ids.size();
		transferable.group_ids = new String[count];
		for(i=0; i<count; i++)
			transferable.group_ids[i] = (String)group_ids.get(i);
	}

	public  void updateLocalFromTransferable()
	{
		int l;
		int i;

		l = category_ids.size();
		categories = new HashMap();
		for(i = 0; i < l; i++)
		{
			Object o = Pool.get(OperatorCategory.typ, (String)category_ids.get(i));
			if(o != null)
				categories.put(category_ids.get(i), o);
		}

	}
	public Collection getChildren(String key)
	{
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
		return ret;
	}

	public List getChildIds(String key)
	{
		if(key.equals(OperatorCategory.typ))
			return category_ids;
		return new ArrayList();
	}

	public void addChildId(String key, String id)
	{
		if(key.equals(OperatorCategory.typ))
			category_ids.add(id);
	}
	public void removeChildId(String key, String id)
	{
		if(key.equals(OperatorCategory.typ))
			category_ids.remove(id);
	}

	public static Collection getChildTypes_()
	{
		List ret = new ArrayList();
		ret.add(OperatorCategory.typ);
		return ret;
	}

	public Class getChildClass(String type)
	{
		if(type.equals(OperatorCategory.typ))
			return OperatorCategory.class;

		return ObjectResource.class;
	}

	public ObjectResourceModel getModel()
	{
		return new CommandPermissionAttributesModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new CommandPermissionAttributesDisplayModel();
	}

	public static String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Administrate.Object.UI.CommandPermissionAttributesPane";
	}

	private String returnRWXstateString()
	{
		String ur;
		String uw;
		String ux;
		String gr;
		String gw;
		String gx;
		String or;
		String ow;
		String ox;
		if(this.userR)
			ur = "1";
		else
			ur = "0";
		if(this.userW)
			uw = "1";
		else
			uw = "0";
		if(this.userX)
			ux = "1";
		else
			ux = "0";
		if(this.groupR)
			gr = "1";
		else
			gr = "0";
		if(this.groupW)
			gw = "1";
		else
			gw = "0";
		if(this.groupX)
			gx = "1";
		else
			gx = "0";
		if(this.otherR)
			or = "1";
		else
			or = "0";
		if(this.otherW)
			ow = "1";
		else
			ow = "0";
		if(this.otherX)
			ox = "1";
		else
			ox = "0";
		String s = ur+uw+ux+
							 gr+gw+gx+
							 or+ow+ox;
		return s;
	}

	private void setRWXstateFromString(String s)
	{
		if(s.charAt(0) == '1')
			this.userR = true;
		else
			this.userR = false;
		if(s.charAt(1) == '1')
			this.userW = true;
		else
			this.userW = false;
		if(s.charAt(2) == '1')
			this.userX = true;
		else
			this.userX = false;

		if(s.charAt(3) == '1')
			this.groupR = true;
		else
			this.groupR = false;
		if(s.charAt(4) == '1')
			this.groupW = true;
		else
			this.groupW = false;
		if(s.charAt(5) == '1')
			this.groupX = true;
		else
			this.groupX = false;

		if(s.charAt(6) == '1')
			this.otherR = true;
		else
			this.otherR = false;
		if(s.charAt(7) == '1')
			this.otherW = true;
		else
			this.otherW = false;
		if(s.charAt(8) == '1')
			this.otherX = true;
		else
			this.otherX = false;
	}


	public String getDomainId()
	{
		return null;
	}

	public String getTyp()
	{
		return typ;
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
