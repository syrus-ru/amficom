/*-
 * $Id: SystemUserFactory.java,v 1.4 2005/07/03 19:16:34 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.administration.corba.IdlSystemUser;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/03 19:16:34 $
 * @module admin_v1
 */
final class SystemUserFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlSystemUser[] allocateArrayOfTransferables(final int length) {
		return new IdlSystemUser[length];
	}
}
