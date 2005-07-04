/*-
 * $Id: ImageResourceFactory.java,v 1.6 2005/07/04 13:00:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/07/04 13:00:47 $
 * @module resource_v1
 */
final class ImageResourceFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlImageResource[] allocateArrayOfTransferables(final int length) {
		return new IdlImageResource[length];
	}
}
