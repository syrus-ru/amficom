/*
 * $Id: ProSchemeElementBase.java,v 1.1 2004/06/17 10:23:05 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/17 10:23:05 $
 * @module filter_v1
 */
public abstract class ProSchemeElementBase
{
	public abstract String getTyp();
	public int x = 0;
	public int y = 0;

	public boolean selected = false;
}
