package com.syrus.AMFICOM.Client.Resource.Object;

import java.text.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class OperatorProfileModel extends ObjectResourceModel
{
	public OperatorProfile prof;

	public OperatorProfileModel(OperatorProfile prof)
	{
		this.prof = prof;
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals(OperatorGroup.typ))
		{
			return prof.groups.elements();
		}
		if(key.equals(OperatorCategory.typ))
		{
			return prof.categories.elements();
		}
		return new Vector().elements();
	}
	public Enumeration getChildTypes()
	{
  	         Vector ret = new Vector();
	         ret.add(OperatorCategory.typ);
	         ret.add(OperatorGroup.typ);
  	         return ret.elements();
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


	public PropertiesPanel getPropertyPane()
	{
		return new OperatorProfilePane(prof);
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
//      if(col_id.equals("id"))
//        s = prof.id;
      if(col_id.equals("name"))
        s = prof.name;
      if(col_id.equals("owner_id"))
        s = Pool.getName("user", prof.owner_id);
      if(col_id.equals("modified"))
        s = sdf.format(new Date(prof.modified));
		}
		catch(Exception e)
		{
//			System.out.println("error getting field value - OperatorProfile");
			s = "";
		}
		if(s == null)
			s = "";

		return s;
	}
}