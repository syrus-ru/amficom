package com.syrus.AMFICOM.Client.General.Filter;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

public class FilterExpressionModel extends ObjectResourceModel
{	
	FilterExpression expr;
	
	public FilterExpressionModel(FilterExpression expr)
	{
		super();
		this.expr = expr;
	}

	public String getColumnValue(String col_id)
	{
		return "expr " + expr.getName();
	}
}