/*
 * $Id: OnetimeTestProcessor.java,v 1.6 2004/07/21 18:43:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2004/07/21 18:43:32 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

import com.syrus.AMFICOM.general.Identifier;

public class OnetimeTestProcessor extends TestProcessor {

	public OnetimeTestProcessor(Test test) {
		super(test);
	}

	public void run() {
		Measurement measurement = null;
		try {
			/*Ordinary measurement with ordinary setup*/
			Identifier id = MeasurementControlModule.createIdentifier("measurement");
			Log.debugMessage("OnetimeTestProcessor | Measurement id: '" + id.toString() + "'", Log.DEBUGLEVEL06);
			measurement = super.test.createMeasurement(id,
																								 super.test.getStartTime(),
																								 MeasurementControlModule.iAm.getUserId());
/*
				MeasurementControlModule.measurementServer.ping(1);*/
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		if (measurement != null) {
			super.transceiver.addMeasurement(measurement, this);
			super.nMeasurements ++;

			while (super.running) {
				try {
					sleep(super.tickTime);
				}
				catch (InterruptedException ie) {
					Log.errorException(ie);
				}

				if (super.nReports < 1)
					super.checkMeasurementResults();
				else
					break;
			}//while

		}//if (measurement != null)

		super.cleanup();
		
	}//run
}
