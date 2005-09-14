/*-
 * $Id: PoolContext.java,v 1.8 2005/09/14 18:21:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.io.LRUSaver;

/**
 * @version $Revision: 1.8 $, $Date: 2005/09/14 18:21:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface PoolContext {

	void init();

	LRUSaver<Identifier, StorableObject> getLRUSaver();
}
