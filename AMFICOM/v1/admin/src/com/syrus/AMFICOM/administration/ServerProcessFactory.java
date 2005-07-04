/*-
 * $Id: ServerProcessFactory.java,v 1.6 2005/07/04 13:00:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.administration.corba.IdlServerProcess;
import com.syrus.AMFICOM.general.StorableObjectFactory;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/07/04 13:00:54 $
 * @module admin_v1
 */
final class ServerProcessFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlServerProcess[] allocateArrayOfTransferables(final int length) {
		return new IdlServerProcess[length];
	}
}
