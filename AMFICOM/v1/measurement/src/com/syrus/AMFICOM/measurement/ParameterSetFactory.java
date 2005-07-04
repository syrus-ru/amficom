/*-
 * $Id: ParameterSetFactory.java,v 1.4 2005/07/04 13:00:49 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSet;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/04 13:00:49 $
 * @module measurement_v1
 */
final class ParameterSetFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlParameterSet[] allocateArrayOfTransferables(final int length) {
		return new IdlParameterSet[length];
	}
}
