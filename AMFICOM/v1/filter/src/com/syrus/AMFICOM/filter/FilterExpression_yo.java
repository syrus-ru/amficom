package com.syrus.AMFICOM.filter;

import java.util.Vector;
import java.util.Hashtable;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class FilterExpression_yo implements FilterExpressionInterface
{
	private static final long serialVersionUID = 01L;

	public final String typ = "filterexpression";
	protected String col_name = "";
	protected String col_id = "";
	protected Vector vec = new Vector();

	protected int listID = 0;

	public FilterExpression_yo()
	{
	}

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
	
	public void writeObject(java.io.ObjectOutputStream out)
		throws IOException
	{
		Vector vec1 = vec;
		out.writeObject(col_name);
		out.writeObject(col_id);
		String type = (String )vec.get(0);
		if(type.equals(LIST_EXPRESSION))
		{
//			TreeModelClone tree = (TreeModelClone )vec.get(1);
//			vec1.setElementAt(tree.getHash(), 1);
		}
		out.writeObject(vec1);

		out.writeInt(listID);
	}

	public void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		col_name = (String )in.readObject();
		col_id = (String )in.readObject();
		Vector vec1 = (Vector )in.readObject();
		String type = (String )vec1.get(0);
		if(type.equals(LIST_EXPRESSION))
		{
//			Hashtable h = (Hashtable )vec.get(1);
			// ÿ¨øôºüðª¹ òþ¸¸ªðýþòûõýøõ ¯øû¹ª¨ð ÿþ ¸ÿø¸úº ø÷ +-
			//vec1.setElementAt(tree.getHash(), 1);
		}
		vec = vec1;
	
		listID = in.readInt();
	}
}