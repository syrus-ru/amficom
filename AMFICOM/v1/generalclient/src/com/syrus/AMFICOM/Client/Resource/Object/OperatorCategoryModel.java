/*
 * $Id: OperatorCategoryModel.java,v 1.3 2004/09/27 15:59:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.OperatorCategoryPanel;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.awt.Component;
import java.util.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 15:59:53 $
 * @module generalclient_v1
 */
public class OperatorCategoryModel extends ObjectResourceModel
{
	public OperatorCategory cat;

	public OperatorCategoryModel(OperatorCategory cat)
	{
		this.cat = cat;
	}

	public Collection getChildren(String key)
	{
		if(key.equals(User.typ))
		{
			return cat.users.values();
		}
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

	public ObjectResourcePropertiesPane getPropertyPane() {
		OperatorCategoryPanel operatorCategoryPanel = OperatorCategoryPanel.getInstance();
		operatorCategoryPanel.setObjectResource(cat);
		return operatorCategoryPanel;
	}

	public String getColumnValue(String col_id)
	{
		String s = "";
		try
		{
		if(col_id.equals("id"))
			s = cat.id;
		if(col_id.equals("name"))
			s = cat.name;
//		if(col_id.equals("codename"))
//			s = cat.codename;
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - OperatorCategory");
			s = "";
		}

		return s;
	}

	List vec = new LinkedList();
	{
//		vec.add("id");
		vec.add("name");
		vec.add("user");
	}

	public List getPropertyColumns()
	{
		return vec;
	}

	public String getPropertyName(String col_id)
	{
		if(col_id.equals("id"))
			return "ID";
		if(col_id.equals("name"))
			return "NAME";
		return "leer";
	}

	/**
	 * @todo Consider refactoring: for "user" takes the first arbitrary user
	 *       (or even throws an exception).
	 */
	public String getPropertyValue(final String columnId)
	{
		if (columnId.equals("id"))
			return cat.getId();
		else if (columnId.equals("name"))
			return cat.getName();
		else if (columnId.equals("user"))
			return ((User) (Pool.getMap("user").values().iterator().next())).getId();
		return "val";
	}

	/**
	 * @todo Consider refactoring: for "user" takes the first arbitrary user
	 *       (or even throws an exception).
	 */
	public Component getPropertyEditor(final String columnId)
	{
		if (columnId.equals("user"))
			return new ObjectResourceComboBox("user", Pool.getMap("user").values().iterator().next());
		return super.getPropertyEditor(columnId);
	}

	/**
	 * @todo Consider refactoring: for "user" takes the first arbitrary
	 *       user.
	 */
	public Component getPropertyRenderer(final String columnId)
	{
		if (columnId.equals("user"))
		{
			Map map = Pool.getMap("user");
			if (map != null)
				return new JLabelRenderer(((User) (map.values().iterator().next())).getName());
			return null;
		}
		return super.getPropertyRenderer(columnId);
	}

	public boolean isPropertyEditable(String col_id)
	{
		if(col_id.equals("name"))
			return true;
		if(col_id.equals("user"))
			return true;
		return false;
	}

	public void setPropertyValue(String col_id, Object val)
	{
		if(col_id.equals("name"))
			cat.name = (String )val;
	}
}
