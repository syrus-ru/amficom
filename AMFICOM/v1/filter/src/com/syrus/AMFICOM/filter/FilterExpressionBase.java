/*
 * $Id: FilterExpressionBase.java,v 1.3 2004/08/24 12:52:08 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/24 12:52:08 $
 * @module filter_v1
 */
public class FilterExpressionBase implements FilterExpressionInterface
{
	private static final long serialVersionUID = 01L;

	/**
	 * Value: {@value}
	 */
	public static final String TYP = "filterexpression";

	/**
	 * Value: {@value}
	 * @deprecated Use {@link #TYP} instead.
	 */

	public static final String typ = TYP;

	protected String col_name = "";
	protected String col_id = "";

	protected String visualName = "";

	protected List vec = new ArrayList();
	protected boolean is_template = false;

	protected int listID = 0;

	public String getName()
	{
		return visualName;
	}

	public String getId()
	{
		return col_id;
	}

	public List getVec()
	{
		return vec;
	}

	public int getListID()
	{
		return listID;
	}

	public boolean isTemplate()
	{
		return is_template;
	}

	public void setName(String n)
	{
		visualName = n;
	}

	public void setColumnName(String n)
	{
		col_name = n;
	}

	public String getColumnName()
	{
		return col_name;
	}

	public void setId(String i)
	{
		col_id = i;
	}

	public void setVec(List v)
	{
		vec = v;
	}

	public void setListID(int l)
	{
		listID = l;
	}

	public void setTemplate(boolean ifT)
	{
		is_template = ifT;
	}

	public void writeObject(ObjectOutputStream out) throws IOException
	{
		/*
		 * ???: what's the need of creating one more object *reference*
		 *      before serialization?
		 */

		out.writeObject(visualName);
		out.writeObject(col_name);
		out.writeObject(col_id);
//		Vector vec1 = vec;
//		String type = (String )vec.get(0);
//		if (type.equals(LIST_EXPRESSION))
//		{
//			TreeModelClone tree = (TreeModelClone )vec.get(1);
//			vec1.setElementAt(tree.getHash(), 1);
//		}
//		out.writeObject(vec1);
		out.writeObject(vec);

		out.writeInt(listID);
	}

	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		visualName = (String) in.readObject();
		col_name = (String )in.readObject();
		col_id = (String )in.readObject();
		vec = (List) in.readObject();
//		Vector vec1 = (Vector )in.readObject();
//		String type = (String )vec1.get(0);
//		if (type.equals(LIST_EXPRESSION))
//		{
//			Hashtable h = (Hashtable )vec.get(1);
//			vec1.setElementAt(tree.getHash(), 1);
//		}
		/*
		 * ???: the same: assignment WILL NOT happen if readObject() fails.
		 */
//		vec = vec1;

		listID = in.readInt();
	}

	public Object clone()
	{
		FilterExpressionBase fe = new FilterExpressionBase();
		fe.setName(getName());
		fe.setColumnName(getColumnName());
		fe.setId(getId());
    
    List cloneList = new ArrayList();
    cloneList.addAll(getVec());
		fe.setVec(cloneList);

		fe.setListID(getListID());

		return fe;
	}

}
