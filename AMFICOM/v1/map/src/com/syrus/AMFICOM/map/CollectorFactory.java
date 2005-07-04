/*-
 * $Id: CollectorFactory.java,v 1.6 2005/07/04 13:00:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.map.corba.IdlCollector;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/07/04 13:00:48 $
 * @module map_v1
 */
final class CollectorFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlCollector[] allocateArrayOfTransferables(final int length) {
		return new IdlCollector[length];
	}
}
