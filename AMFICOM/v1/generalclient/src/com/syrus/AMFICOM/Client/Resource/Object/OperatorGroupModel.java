/*
 * $Id: OperatorGroupModel.java,v 1.5 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.OperatorGroupPane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.StorableObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/13 19:05:47 $
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
		if(key.equals(User.class.getName()))
			return group.users.values();

		return new Vector();
	}
	public Enumeration getChildTypes()
	{
		Vector ret = new Vector();
		ret.add(User.class.getName());
		return ret.elements();
	}

	public Class getChildClass(String type)
	{
		if(type.equals(User.class.getName()))
			 return User.class;
		return StorableObject.class;
	}

	public ObjectResourcePropertiesPane getPropertyPane() {
		OperatorGroupPane operatorGroupPane = OperatorGroupPane.getInstance();
		operatorGroupPane.setObjectResource(group);
		return operatorGroupPane;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
//      if(col_id.equals("id"))
//        s = group.getId();
			if(col_id.equals("name"))
				s = group.name;
			if(col_id.equals("owner_id"))
				s = ((StorableObject )Pool.get(User.class.getName(), group.owner_id)).getName();
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
