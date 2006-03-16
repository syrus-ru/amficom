/*-
 * $Id: OnetimeTestProcessor.java,v 1.34.2.1 2006/03/16 12:01:54 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import java.util.Date;

import com.syrus.AMFICOM.measurement.Test;

/**
 * @version $Revision: 1.34.2.1 $, $Date: 2006/03/16 12:01:54 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class OnetimeTestProcessor extends TestProcessor {
	private boolean first;

	public OnetimeTestProcessor(final Test test) throws TestProcessingException {
		super(test);

		this.first = true;
	}

	@Override
	Date getNextMeasurementStartTime(final Date fromDate, final boolean includeFromDate) {
		if (this.first) {
			this.first = false;
			return (includeFromDate && System.currentTimeMillis() - fromDate.getTime() <= PAST_MEASUREMENT_TIMEOUT) ? fromDate : null;
		}
		return null;
	}

}
