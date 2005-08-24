/*-
 * $Id: Filter.java,v 1.4 2005/08/24 15:00:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/24 15:00:29 $
 * @module filter
 */
public interface Filter
{
	boolean expression(FilterExpressionInterface expr, Object or);
}
