/*
 * $Id: FilterExpressionBase.java,v 1.4 2005/04/13 19:09:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.4 $, $Date: 2005/04/13 19:09:41 $
 * @module filter_v1
 */
public class FilterExpressionBase implements FilterExpressionInterface
{

	/**
	 * Value: {@value}
	 */
	public static final String TYP = "filterexpression";

	protected String colName = "";
	protected String colId = "";

	protected String visualName = "";

	protected List vec = new ArrayList();
	protected boolean isTemplate = false;

	protected int listID = 0;

	public String getName()
	{
		return this.visualName;
	}

	public String getId()
	{
		return this.colId;
	}

	public List getVec()
	{
		return this.vec;
	}

	public int getListID()
	{
		return this.listID;
	}

	public boolean isTemplate()
	{
		return this.isTemplate;
	}

	public void setName(String n)
	{
		this.visualName = n;
	}

	public void setColumnName(String n)
	{
		this.colName = n;
	}

	public String getColumnName()
	{
		return this.colName;
	}

	public void setId(String i)
	{
		this.colId = i;
	}

	public void setVec(List v)
	{
		this.vec = v;
	}

	public void setListID(int l)
	{
		this.listID = l;
	}

	public void setTemplate(boolean ifT)
	{
		this.isTemplate = ifT;
	}

	public void writeObject(ObjectOutputStream out) throws IOException
	{
		/*
		 * ???: what's the need of creating one more object *reference*
		 *      before serialization?
		 */

		out.writeObject(this.visualName);
		out.writeObject(this.colName);
		out.writeObject(this.colId);
//		Vector vec1 = vec;
//		String type = (String )vec.get(0);
//		if (type.equals(LIST_EXPRESSION))
//		{
//			TreeModelClone tree = (TreeModelClone )vec.get(1);
//			vec1.setElementAt(tree.getHash(), 1);
//		}
//		out.writeObject(vec1);
		out.writeObject(this.vec);

		out.writeInt(this.listID);
	}

	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		this.visualName = (String) in.readObject();
		this.colName = (String )in.readObject();
		this.colId = (String )in.readObject();
		this.vec = (List) in.readObject();
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

		this.listID = in.readInt();
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
