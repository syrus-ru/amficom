/*
 * $Id: OperatorCategoryModel.java,v 1.2 2004/08/17 15:02:51 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import java.awt.*;
import java.util.*;


import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.List;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2004/08/17 15:02:51 $
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

	public PropertiesPanel getPropertyPane()
	{
		return new OperatorCategoryPanel(cat);
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

	public String getPropertyValue(String col_id)
	{
		if(col_id.equals("id"))
			return cat.id;
		if(col_id.equals("name"))
			return cat.name;
		if(col_id.equals("user"))
		{
			Hashtable h = Pool.getHash("user");
			User user = (User )h.elements().nextElement();
			return user.id;
		}
		return "val";
	}

	public Component getPropertyEditor(String col_id)
	{
		if(col_id.equals("user"))
		{
			Hashtable h = Pool.getHash("user");
			User user = (User )h.elements().nextElement();
			return new ObjectResourceComboBox("user", user);
		}
		return super.getPropertyEditor(col_id);
	}

	public Component getPropertyRenderer(String col_id)
	{
		if(col_id.equals("user"))
		{
			Hashtable h = Pool.getHash("user");
			if(h == null)
				return null;
			User user = (User )h.elements().nextElement();
			return new JLabelRenderer(user.getName());
		}
		return super.getPropertyRenderer(col_id);
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
