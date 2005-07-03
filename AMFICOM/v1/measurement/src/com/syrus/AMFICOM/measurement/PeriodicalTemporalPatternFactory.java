/*-
 * $Id: PeriodicalTemporalPatternFactory.java,v 1.4 2005/07/03 19:16:30 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlPeriodicalTemporalPattern;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/03 19:16:30 $
 * @module measurement_v1
 */
final class PeriodicalTemporalPatternFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlPeriodicalTemporalPattern[] allocateArrayOfTransferables(final int length) {
		return new IdlPeriodicalTemporalPattern[length];
	}
}
