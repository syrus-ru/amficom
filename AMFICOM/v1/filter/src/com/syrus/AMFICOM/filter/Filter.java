package com.syrus.AMFICOM.filter;

public interface Filter 
{
	public abstract boolean expression(FilterExpressionInterface expr, Object or);
}