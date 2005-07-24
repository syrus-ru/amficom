/*-
 * $Id: SchemeOptimizeInfoSwitchFactory.java,v 1.7 2005/07/24 17:10:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoSwitch;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/07/24 17:10:19 $
 * @module scheme
 */
final class SchemeOptimizeInfoSwitchFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlSchemeOptimizeInfoSwitch[] allocateArrayOfTransferables(final int length) {
		return new IdlSchemeOptimizeInfoSwitch[length];
	}
}
