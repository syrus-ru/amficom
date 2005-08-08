/*-
 * $Id: PoolContext.java,v 1.6 2005/08/08 11:38:11 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:38:11 $
 * @author $Author: arseniy $
 * @module csbridge
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
