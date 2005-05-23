/*-
 * $Id: PoolContext.java,v 1.5 2005/05/23 08:34:38 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/23 08:34:38 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public abstract class PoolContext {

	public abstract void init();

	public final void deserialize() {
		StorableObjectPool.deserialize();
	}

	public final void serialize() {
		StorableObjectPool.serialize();
	}
}
