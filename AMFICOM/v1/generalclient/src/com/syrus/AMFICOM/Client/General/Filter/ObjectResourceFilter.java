/*
 * $Id: ObjectResourceFilter.java,v 1.14 2004/09/27 10:40:41 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.filter.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2004/09/27 10:40:41 $
 * @module generalclient_v1
 */
public abstract class ObjectResourceFilter implements Filter
{
	public LogicScheme logicScheme = null;
	public String resource_typ = "";

	private String id = "";

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

	public abstract List getFilterColumns();
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

	public List getCriteria()
	{
		return this.logicScheme.getFilterExpressions();
//		return criteria;
	}

	public List filter(List list)
	{
		List ds = new ArrayList(list);
		for(Iterator it = list.iterator(); it.hasNext();){
			ObjectResource or = (ObjectResource )it.next();
			if (!this.logicScheme.passesAllConstraints(or))
				ds.remove(or);
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
				it.remove();				
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
				it.remove();				
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
