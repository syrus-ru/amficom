package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Filter.FilterPanel;

import java.io.*;
import java.util.*;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.DataSet;

import com.syrus.AMFICOM.filter.*;

public abstract class ObjectResourceFilter implements Filter
{
	public LogicScheme logicScheme = null;
	public String resource_typ = "";
//	public Vector criteria = new Vector();

	String id = "";

	public int lastListID = 1;

	public ObjectResourceFilter()
	{
		id = "filter" + System.currentTimeMillis();
		this.logicScheme = new LogicScheme(this);
	}

	public ObjectResourceFilter(LogicScheme ls)
	{
		id = "filter" + System.currentTimeMillis();
		this.logicScheme = ls;
	}

	public abstract Vector getFilterColumns();
	public abstract String getFilterColumnName(String col_id);
	public abstract String[] getColumnFilterTypes(String col_id);
	public abstract FilterPanel getColumnFilterPanel(String col_id, String type);
	public abstract boolean expression(FilterExpression expr, ObjectResource or);
	public abstract Object clone();

	public String getId()
	{
		return id;
	}

	public boolean expression(FilterExpressionInterface expr, Object or)
	{
		return expression((FilterExpression )expr, (ObjectResource )or);
	}

	public boolean SearchSubstring (String text, String substring)
	{
		boolean res = false;
		if(text.indexOf(substring) >= 0)
			return true;
/*
		if (substring.length() <= text.length())
		{
			for (int i = 0; i <= text.length() - substring.length(); i++)
			{
				if (text.substring(i,i+substring.length()).equals(substring))
				{
					res = true;
				}
			}
		}
*/
		return res;
	}

	public void addCriterium(FilterExpression expr)
	{
		expr.setListID(lastListID++);
		logicScheme.addFilterExpression(expr);
//		criteria.add(expr);
	}

	public void removeCriterium(FilterExpression expr)
	{
		lastListID--;
		logicScheme.removeFilterExpression(expr);
//		criteria.remove(expr);
	}

	public void replaceCriterium(FilterExpression fe_old, FilterExpression fe_new)
	{
		logicScheme.replaceFilterExpression(fe_old, fe_new);
	}

	public void clearCriteria()
	{
		lastListID = 1;
		logicScheme.clearFilterExpressions();
//		criteria = new Vector();
	}

	public Vector getCriteria()
	{
		return logicScheme.getFilterExpressions();
//		return criteria;
	}

	public DataSet filter(DataSet dataSet)
	{
		DataSet ds = new DataSet(dataSet.elements());
		for(Enumeration e = dataSet.elements(); e.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )e.nextElement();
			if (logicScheme.passesAllConstraints(or) == false)
				ds.remove(or);
/*
			for(Enumeration e2 = criteria.elements(); e2.hasMoreElements();)
			{
				FilterExpression expr = (FilterExpression)e2.nextElement();
				if(expression(expr, or) == false)
					ds.remove(or);
			}
*/
		}
		return ds;
	}
}
