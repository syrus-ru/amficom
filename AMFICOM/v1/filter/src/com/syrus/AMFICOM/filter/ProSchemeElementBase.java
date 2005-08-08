/*
 * $Id: ProSchemeElementBase.java,v 1.2 2005/08/08 11:37:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:37:22 $
 * @module filter
 */
public abstract class ProSchemeElementBase
{
	public abstract String getTyp();
	public int x = 0;
	public int y = 0;

	public boolean selected = false;
}
