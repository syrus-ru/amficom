/*
 * $Id: ContinuousTestProcessor.java,v 1.6 2004/07/21 18:43:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2004/07/21 18:43:32 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class ContinuousTestProcessor extends TestProcessor {
	private static final int STATUS_NEW_MEASUREMENT = 0;
	private static final int STATUS_MEASUREMENT_IS_WAITING = 1;
	private static final int STATUS_LAST_MEASUREMENT_GONE = 3;

	private long[] ti;
	private int status;

	public ContinuousTestProcessor(Test test) {
		super(test);
	}

	public void run() {
		Measurement measurement = null;
		while (super.running) {
			try {
				sleep(super.tickTime);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

			switch (this.status) {
				case STATUS_NEW_MEASUREMENT:
					break;
				case STATUS_MEASUREMENT_IS_WAITING:
					break;
				case STATUS_LAST_MEASUREMENT_GONE:
					break;
			}//switch

			if (this.status != STATUS_LAST_MEASUREMENT_GONE || super.nMeasurements < super.nReports)
				super.checkMeasurementResults();
			else
				break;
		}//while

		super.cleanup();
	}//run
}
