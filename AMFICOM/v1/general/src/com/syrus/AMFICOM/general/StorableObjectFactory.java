/*-
 * $Id: StorableObjectFactory.java,v 1.4 2005/07/03 19:16:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * As soon as migration to valuetypes is complete, this class and its
 * descendants will be merged with valuetyped implementations.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/03 19:16:25 $
 * @module general_v1
 */
public abstract class StorableObjectFactory {
	protected abstract IdlStorableObject[] allocateArrayOfTransferables(final int length);
}
