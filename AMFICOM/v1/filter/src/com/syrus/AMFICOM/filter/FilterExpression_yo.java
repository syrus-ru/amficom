/*
 * $Id: FilterExpression_yo.java,v 1.3 2004/06/11 10:33:33 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.Vector;

/**
 * @version $Revision: 1.3 $, $Date: 2004/06/11 10:33:33 $
 * @module filter_v1
 */
public class FilterExpression_yo implements FilterExpressionInterface
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
	protected Vector vec = new Vector();
	protected boolean is_template = false;

	protected int listID = 0;

	public String getName()
	{
		return col_name;
	}

	public String getId()
	{
		return col_id;
	}

	public Vector getVec()
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
		col_name = n;
	}

	public void setId(String i)
	{
		col_id = i;
	}

	public void setVec(Vector v)
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
		Vector vec1 = vec;
		out.writeObject(col_name);
		out.writeObject(col_id);
//		String type = (String )vec.get(0);
//		if (type.equals(LIST_EXPRESSION))
//		{
//			TreeModelClone tree = (TreeModelClone )vec.get(1);
//			vec1.setElementAt(tree.getHash(), 1);
//		}
		out.writeObject(vec1);

		out.writeInt(listID);
	}

	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		col_name = (String )in.readObject();
		col_id = (String )in.readObject();
		Vector vec1 = (Vector )in.readObject();
//		String type = (String )vec1.get(0);
//		if (type.equals(LIST_EXPRESSION))
//		{
//			Hashtable h = (Hashtable )vec.get(1);
//			vec1.setElementAt(tree.getHash(), 1);
//		}
		/*
		 * ???: the same: assignment WILL NOT happen if readObject() fails.
		 */
		vec = vec1;

		listID = in.readInt();
	}
}
