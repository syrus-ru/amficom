package com.syrus.AMFICOM.Client.Resource.Object;

import java.text.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class CommandPermissionAttributesModel extends ObjectResourceModel
{

	public CommandPermissionAttributes cpa;

	public CommandPermissionAttributesModel(CommandPermissionAttributes cpa)
	{
		this.cpa = cpa;
	}

	public Collection getChildren(String key)
	{
		if(key.equals(OperatorCategory.typ))
		{
			return cpa.categories.values();
		}
		return new Vector();
	}

	public Enumeration getChildTypes()
	{
		Vector ret = new Vector();
		ret.add(OperatorCategory.typ);
		return ret.elements();
	}

	public Class getChildClass(String type)
	{
		if(type.equals(OperatorCategory.typ))
			return OperatorCategory.class;

		return ObjectResource.class;
	}


	public PropertiesPanel getPropertyPane()
	{
		return new CommandPermissionAttributesPane(cpa);
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
//      if(col_id.equals("id"))
//        s = cpa.id;
			if(col_id.equals("name"))
				s = cpa.name;
			if(col_id.equals("owner_id"))
				s = ((ObjectResource)Pool.get(User.typ, cpa.owner_id)).getName();
			if(col_id.equals("modified"))
				s = sdf.format(new Date(cpa.modified));
		}
		catch(Exception e)
		{
//      System.out.println("error getting field value - OperatorProfile");
			s = "";
		}
		if(s == null)
			s = "";

		return s;
	}




}
