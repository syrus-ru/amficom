/*
 * $Id: OperatorGroupModel.java,v 1.3 2004/08/19 10:47:23 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2004/08/19 10:47:23 $
 * @module generalclient_v1
 */
public class OperatorGroupModel extends ObjectResourceModel
{
	public OperatorGroup group;

	public OperatorGroupModel(OperatorGroup group)
	{
		this.group = group;
	}

	public Collection getChildren(String key)
	{
		if(key.equals(User.typ))
			return group.users.values();

		return new Vector();
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
				s = ((ObjectResource )Pool.get(User.typ, group.owner_id)).getName();
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
