package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Filter.FilterPanel;

import java.util.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.DataSet;

import com.syrus.AMFICOM.filter.*;

public abstract class ObjectResourceFilter implements Filter
{
	
	public static final String COLUMN_FILTER_TYPE_STRING	=  "string";
	public static final String COLUMN_FILTER_TYPE_LIST		=  "list";
	public static final String COLUMN_FILTER_TYPE_TIME		=  "time";
	public static final String COLUMN_FILTER_TYPE_NUMERIC	=  "numeric";
	public static final String COLUMN_FILTER_TYPE_RANGE		=  "range";
	
	public LogicScheme logicScheme = null;
	public String resource_typ = "";
//	public Vector criteria = new Vector();

	String id = "";

	public int lastListID = 1;

	public ObjectResourceFilter()
	{
		this.id = "filter" + System.currentTimeMillis();
		this.logicScheme = new LogicScheme(this);
	}

	public ObjectResourceFilter(LogicScheme ls)
	{
		this.id = "filter" + System.currentTimeMillis();
		this.logicScheme = ls;
	}

	public abstract Vector getFilterColumns();
	public abstract String getFilterColumnName(String col_id);
	public abstract String[] getColumnFilterTypes(String col_id);
	public abstract FilterPanel getColumnFilterPanel(String col_id, String type);
	public abstract boolean expression(FilterExpressionInterface expr, ObjectResource or);
	public abstract Object clone();

	public String getId()
	{
		return this.id;
	}

	public boolean expression(FilterExpressionInterface expr, Object or)
	{
		return expression(expr, (ObjectResource)or);
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

	public void addCriterium(FilterExpressionInterface expr)
	{
		expr.setListID(this.lastListID++);
		this.logicScheme.addFilterExpression(expr);
//		criteria.add(expr);
	}

	public void removeCriterium(FilterExpressionInterface expr)
	{
		this.lastListID--;
		this.logicScheme.removeFilterExpression(expr);
//		criteria.remove(expr);
	}

	public void replaceCriterium(FilterExpressionInterface fe_old, FilterExpressionInterface fe_new)
	{
		this.logicScheme.replaceFilterExpression(fe_old, fe_new);
	}

	public void clearCriteria()
	{
		this.lastListID = 1;
		this.logicScheme.clearFilterExpressions();
//		criteria = new Vector();
	}

	public Vector getCriteria()
	{
		return this.logicScheme.getFilterExpressions();
//		return criteria;
	}

	public List filter(List dataSet)
	{
		List ds = new DataSet(dataSet);
		for(Iterator it = dataSet.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();

			if (this.logicScheme.passesAllConstraints(or) == false)
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

	/**
	 * Filtrate input Collection
	 * @param col source Collection, which will be filtrate
	 */
	public void filtrate(Collection col){		
		for(Iterator it=col.iterator();it.hasNext();){
			ObjectResource or = (ObjectResource)it.next();
			if (!this.logicScheme.passesAllConstraints(or)){
				col.remove(or);
				it=col.iterator();
			}
		}		
	}
	
	/**
	 * filtrate input Collection to output Collection
	 * @param input
	 * @param output
	 */
	public void filtrate(final Collection input, Collection output){
		output.clear();
		for(Iterator it=input.iterator();it.hasNext();){
			ObjectResource or = (ObjectResource)it.next();
			if (this.logicScheme.passesAllConstraints(or)){
				output.add(or);
			}
		}	
	}
	
	/**
	 * Return new Collection of filtered elements of Collection
	 * @param col
	 * @return
	 */
	public Collection filter(final Collection col){
		List resultList = new ArrayList();
		this.filtrate(col, resultList);
		return resultList;
	}
	

	/**
	 * Filtrate input Map
	 * @param map source Map, which will be filtrate
	 */
	public void filtrate(Map map){		
		for(Iterator it=map.keySet().iterator();it.hasNext();){
			Object key = it.next();
			ObjectResource or = (ObjectResource)map.get(key);
			if (!this.logicScheme.passesAllConstraints(or)){
				map.remove(key);
				it=map.keySet().iterator();
			}
		}
	}
	
	/**
	 * filtrate input Map to output Map
	 * @param input
	 * @param output
	 */
	public void filtrate(final Map input, Map output){
		output.clear();		
		for(Iterator it=input.keySet().iterator();it.hasNext();){
			Object key = it.next();
			ObjectResource or = (ObjectResource)input.get(key);
			if (this.logicScheme.passesAllConstraints(or)){
				output.put(key,or);
			}
		}
	}
	
	/**
	 * Return new Map of filtered elements of Map
	 * @param map
	 * @return
	 */
	public Map filter(final Map map){
		Map resultMap = new HashMap();
		this.filtrate(map, resultMap);
		return resultMap;
	}

}
