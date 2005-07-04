/*-
 * $Id: ParameterTypeFactory.java,v 1.5 2005/07/04 13:00:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdlParameterType;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/07/04 13:00:51 $
 * @module general_v1
 */
final class ParameterTypeFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlParameterType[] allocateArrayOfTransferables(final int length) {
		return new IdlParameterType[length];
	}
}
