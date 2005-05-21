/*-
 * $Id: PoolContext.java,v 1.4 2005/05/21 19:55:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/21 19:55:49 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class PoolContext {

	public abstract void init();

	public void deserialize() {
		StorableObjectPool.deserialize();
	}

	public void serialize() {
		StorableObjectPool.serialize();
	}
}
