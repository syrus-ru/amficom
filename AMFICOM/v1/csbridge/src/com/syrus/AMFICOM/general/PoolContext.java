/*-
 * $Id: PoolContext.java,v 1.7 2005/09/07 13:02:52 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.io.LRUSaver;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/07 13:02:52 $
 * @author $Author: bob $
 * @module csbridge
 */
public interface PoolContext {

	void init();

	LRUSaver<Identifier, StorableObject> getLRUSaver();
}
