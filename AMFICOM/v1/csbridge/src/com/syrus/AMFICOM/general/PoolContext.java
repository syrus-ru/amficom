/*-
 * $Id: PoolContext.java,v 1.3 2005/05/16 16:04:19 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/16 16:04:19 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class PoolContext {

	public abstract void init();

	public abstract void deserialize();

	public abstract void serialize();
}
