/*
 * $Id: CommandPermissionAttributesModel.java,v 1.5 2004/09/27 15:46:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.CommandPermissionAttributesPane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 15:46:18 $
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
