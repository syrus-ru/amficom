/*
 * $Id: OperatorProfileModel.java,v 1.2 2004/08/17 15:02:51 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import java.text.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2004/08/17 15:02:51 $
 * @module generalclient_v1
 */
public class OperatorProfileModel extends ObjectResourceModel
{
	public OperatorProfile prof;

	public OperatorProfileModel(OperatorProfile prof)
	{
		this.prof = prof;
	}

	public Collection getChildren(String key)
	{
		if(key.equals(OperatorGroup.typ))
		{
			return prof.groups.values();
		}
		if(key.equals(OperatorCategory.typ))
		{
			return prof.categories.values();
		}
		return new Vector();
	}

	Vector childrenTypes = new Vector();
	{
	         childrenTypes.add(OperatorCategory.typ);
	         childrenTypes.add(OperatorGroup.typ);
	}

	public Enumeration getChildTypes()
	{
  	         return childrenTypes.elements();
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
