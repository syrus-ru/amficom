/*-
 * $Id: OnetimeTestProcessor.java,v 1.33 2005/09/20 09:54:05 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import java.util.Date;

import com.syrus.AMFICOM.measurement.Test;

/**
 * @version $Revision: 1.33 $, $Date: 2005/09/20 09:54:05 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class OnetimeTestProcessor extends TestProcessor {
	private boolean first;

	public OnetimeTestProcessor(final Test test) {
		super(test);

		this.first = true;
	}

	@Override
	Date getNextMeasurementStartTime(final Date fromDate, final boolean includeFromDate) {
		if (this.first) {
			this.first = false;
			return (includeFromDate && System.currentTimeMillis() - fromDate.getTime() >= 0) ? fromDate : null;
		}
		return null;
	}

}
