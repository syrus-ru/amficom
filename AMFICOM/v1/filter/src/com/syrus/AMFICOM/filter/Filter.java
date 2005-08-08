/*
 * $Id: Filter.java,v 1.3 2005/08/08 11:37:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:37:22 $
 * @module filter
 */
public interface Filter
{
	boolean expression(FilterExpressionInterface expr, Object or);
}
