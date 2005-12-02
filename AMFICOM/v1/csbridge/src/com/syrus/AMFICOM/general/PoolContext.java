/*-
 * $Id: PoolContext.java,v 1.10 2005/12/02 15:19:42 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.10 $, $Date: 2005/12/02 15:19:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public abstract class PoolContext {
	protected ObjectLoader objectLoader;

	protected PoolContext(final ObjectLoader objectLoader) {
		this.objectLoader = objectLoader;
	}

	public abstract void init();

	public abstract LRUMapSaver<Identifier, StorableObject> getLRUMapSaver();
}
