/*
 * $Id: CommandPermissionAttributesModel.java,v 1.6 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.CommandPermissionAttributesPane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.StorableObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/13 19:05:47 $
 * @module generalclient_v1
 */
public class CommandPermissionAttributesModel extends ObjectResourceModel
{

	public CommandPermissionAttributes cpa;

	public CommandPermissionAttributesModel(CommandPermissionAttributes cpa)
	{
		this.cpa = cpa;
	}

	public Collection getChildren(String key)
	{
		if(key.equals(OperatorCategory.class.getName()))
		{
			return cpa.categories.values();
		}
		return new Vector();
	}

	public Enumeration getChildTypes()
	{
		Vector ret = new Vector();
		ret.add(OperatorCategory.class.getName());
		return ret.elements();
	}

	public Class getChildClass(String type)
	{
		if(type.equals(OperatorCategory.class.getName()))
			return OperatorCategory.class;

		return StorableObject.class;
	}

	public ObjectResourcePropertiesPane getPropertyPane() {
		CommandPermissionAttributesPane commandPermissionAttributesPane = CommandPermissionAttributesPane.getInstance();
		commandPermissionAttributesPane.setObjectResource(cpa);
		return commandPermissionAttributesPane;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
//      if(col_id.equals("id"))
//        s = cpa.getId();
			if(col_id.equals("name"))
				s = cpa.name;
			if(col_id.equals("owner_id"))
				s = ((StorableObject)Pool.get(User.class.getName(), cpa.owner_id)).getName();
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
