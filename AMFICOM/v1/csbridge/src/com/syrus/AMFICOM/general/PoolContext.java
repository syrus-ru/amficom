/*-
 * $Id: PoolContext.java,v 1.9 2005/10/11 14:19:53 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.io.LRUSaver;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/11 14:19:53 $
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

	public abstract LRUSaver<Identifier, StorableObject> getLRUSaver();
}
