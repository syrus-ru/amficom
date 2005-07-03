/*
 * $Id: OperatorProfileModel.java,v 1.5 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.OperatorProfilePane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.*;
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
public class OperatorProfileModel extends ObjectResourceModel
{
	public OperatorProfile prof;

	public OperatorProfileModel(OperatorProfile prof)
	{
		this.prof = prof;
	}

	public Collection getChildren(String key)
	{
		if(key.equals(OperatorGroup.class.getName()))
		{
			return prof.groups.values();
		}
		if(key.equals(OperatorCategory.class.getName()))
		{
			return prof.categories.values();
		}
		return new Vector();
	}

	Vector childrenTypes = new Vector();
	{
					 childrenTypes.add(OperatorCategory.class.getName());
					 childrenTypes.add(OperatorGroup.class.getName());
	}

	public Enumeration getChildTypes()
	{
						 return childrenTypes.elements();
	}

	public Class getChildClass(String type)
	{
		if(type.equals(OperatorCategory.class.getName()))
		{
			return OperatorCategory.class;
		}
		if(type.equals(OperatorGroup.class.getName()))
		{
			return OperatorGroup.class;
		}
			return StorableObject.class;
	}

	public ObjectResourcePropertiesPane getPropertyPane() {
		OperatorProfilePane operatorProfilePane = OperatorProfilePane.getInstance();
		operatorProfilePane.setObjectResource(prof);
		return operatorProfilePane;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
//      if(col_id.equals("id"))
//        s = prof.getId();
			if(col_id.equals("name"))
				s = prof.name;
			if(col_id.equals("owner_id"))
				s = ((StorableObject )Pool.get("user", prof.owner_id)).getName();
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
