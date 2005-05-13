/*-
 * $Id: PoolContext.java,v 1.2 2005/05/13 17:38:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/13 17:38:43 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public abstract class PoolContext {
	public abstract void init();

	public abstract void deserialize();

	public abstract void serialize();
}
