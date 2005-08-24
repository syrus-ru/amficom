/*-
 * $Id: ProSchemeElementBase.java,v 1.3 2005/08/24 15:00:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/24 15:00:29 $
 * @module filter
 */
public abstract class ProSchemeElementBase
{
	public abstract String getTyp();
	public int x = 0;
	public int y = 0;

	public boolean selected = false;
}
