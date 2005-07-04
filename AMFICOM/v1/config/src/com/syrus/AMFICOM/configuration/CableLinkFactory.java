/*-
 * $Id: CableLinkFactory.java,v 1.3 2005/07/04 13:00:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.configuration.corba.IdlCableLink;
import com.syrus.AMFICOM.general.StorableObjectFactory;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/07/04 13:00:53 $
 * @module config_v1
 */
final class CableLinkFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlCableLink[] allocateArrayOfTransferables(final int length) {
		return new IdlCableLink[length];
	}
}
