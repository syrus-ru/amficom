package com.syrus.AMFICOM.Client.Resource.Object;

import java.text.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class OperatorGroupModel extends ObjectResourceModel
{
	public OperatorGroup group;

	public OperatorGroupModel(OperatorGroup group)
	{
		this.group = group;
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals(User.typ))
			return group.users.elements();

		return new Vector().elements();
	}
	public Enumeration getChildTypes()
	{
	  Vector ret = new Vector();
	  ret.add(User.typ);
	  return ret.elements();
	}

	public Class getChildClass(String type)
	{
	  if(type.equals(User.typ))
	     return User.class;
	  return ObjectResource.class;
	}


	public PropertiesPanel getPropertyPane()
	{
		return new OperatorGroupPane(group);
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
//      if(col_id.equals("id"))
//        s = group.id;
      if(col_id.equals("name"))
        s = group.name;
      if(col_id.equals("owner_id"))
        s = Pool.getName(User.typ, group.owner_id);
      if(col_id.equals("modified"))
        s = sdf.format(new Date(group.modified));
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - OperatorGroup");
			s = "";
		}
		if(s == null)
			s = "";

		return s;
	}
}