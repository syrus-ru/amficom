package com.syrus.AMFICOM.Client.General.Filter;

import java.util.Vector;

import java.io.IOException;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import com.syrus.AMFICOM.filter.*;

public class FilterExpression extends StubResource implements FilterExpressionInterface
{
	static public final String typ = "filterexpression";
	private static final long serialVersionUID = 01L;

	public static final String NUMERIC_EXPRESSION = "numeric";
	public static final String STRING_EXPRESSION = "string";
	public static final String RANGE_EXPRESSION = "range";
	public static final String TIME_EXPRESSION = "time";
	public static final String LIST_EXPRESSION = "list";

	private FilterExpressionBase fe;

	public FilterExpression()
	{
		fe = new FilterExpressionBase();
	}

	public FilterExpression(FilterExpressionBase feBase)
	{
		fe = feBase;
	}


	public String getName()
	{
		return "Условие " + Integer.toString(getListID()) + ": " + fe.getName();
	}

	public String getId()
	{
		return fe.getId();
	}

	public Vector getVec()
	{
		return fe.getVec();
	}

	public int getListID()
	{
		return fe.getListID();
	}

	public boolean isTemplate()
	{
		return fe.isTemplate();
	}

	public void setName(String n)
	{
		fe.setName(n);
	}

	public void setId(String i)
	{
		fe.setId(i);
	}

	public void setVec(Vector v)
	{
		fe.setVec(v);
	}

	public void setListID(int l)
	{
		fe.setListID(l);
	}

	public void setTemplate(boolean ifT)
	{
		fe.setTemplate(ifT);
	}

	public void setColumnName(String n)
	{
		fe.setColumnName(n);
	}

	public String getColumnName()
	{
		return fe.getColumnName();
	}


	public Object clone()
	{
		FilterExpression fe = new FilterExpression();
		fe.setName(getName());
		fe.setId(getId());
		fe.setVec((Vector )getVec().clone());

		fe.setListID(getListID());

		return fe;
	}

	public void writeObject(java.io.ObjectOutputStream out)
		throws IOException
	{
		Vector vec1 = getVec();
		Vector vec = (Vector )vec1.clone();
		String type = (String )vec.get(0);
		if(type.equals(LIST_EXPRESSION))
		{
			TreeModelClone tree = (TreeModelClone )vec.get(1);
			vec.setElementAt(tree.getHash(), 1);
		}
		fe.setVec(vec);
		fe.writeObject(out);
		fe.setVec(vec1);
	}

	public void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		fe.readObject(in);
	}
}
