/*-
 * $Id: MeasurementTypeFactory.java,v 1.5 2005/07/04 13:00:49 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/07/04 13:00:49 $
 * @module measurement_v1
 */
final class MeasurementTypeFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlMeasurementType[] allocateArrayOfTransferables(final int length) {
		return new IdlMeasurementType[length];
	}
}
