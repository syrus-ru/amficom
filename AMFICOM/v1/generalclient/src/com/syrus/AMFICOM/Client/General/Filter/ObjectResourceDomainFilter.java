package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Filter.FilterPanel;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;
import com.syrus.AMFICOM.filter.FilterExpressionInterface;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;


public class ObjectResourceDomainFilter extends ObjectResourceFilter
{
	String domain_id = "";

	public ObjectResourceDomainFilter(String domain_id)
	{
		super();
		this.domain_id = domain_id;
		FilterExpression fe = new FilterExpression();
		fe.setId("domain_id");
		fe.getVec().add(domain_id);
		this.addCriterium(fe);
	}

	public Vector getFilterColumns()
	{
		Vector vec = new Vector();
		vec.add("domain_id");
		return vec;
	}

	public String getFilterColumnName(String col_id)
	{
		return "Домен";
	}

	public String[] getColumnFilterTypes(String col_id)
	{
		return new String[] { "string" };
	}

	public FilterPanel getColumnFilterPanel(String col_id, String type)
	{
		return null;
	}

	public boolean expression(FilterExpressionInterface expr, ObjectResource or)
	{
		String s = (String )expr.getVec().get(0);
		return or.getDomainId().equals(s);
	}

	public Object clone()
	{
		return new ObjectResourceDomainFilter(domain_id);
	}

}
