/*
 * $Id: OnetimeTestProcessor.java,v 1.22 2005/03/25 22:21:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.22 $, $Date: 2005/03/25 22:21:50 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class OnetimeTestProcessor extends TestProcessor {
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_CREATE_IDENTIFIER = 1;
	public static final int FALL_CODE_CREATE_MEASUREMENT = 2;

	private Date startTime;

	public OnetimeTestProcessor(Test test) {
		super(test);
		super.lastMeasurementAcquisition = (super.numberOfScheduledMeasurements > 0);

		this.startTime = test.getStartTime();
		Log.debugMessage("Set lastMeasurementAcquisition: " + this.lastMeasurementAcquisition + "; startTime: " + this.startTime + ", current: " + (new Date(System.currentTimeMillis())), Log.DEBUGLEVEL08);
	}

	public void run() {
		Measurement measurement;
		long currentMeasurementStartTime = this.startTime.getTime();
		while (super.running) {
			if (!super.lastMeasurementAcquisition) {
				if (this.startTime.getTime() <= System.currentTimeMillis()) {

					measurement = null;
					try {
						measurement = super.test.createMeasurement(MeasurementControlModule.iAm.getUserId(), this.startTime);
//						currentMeasurementStartTime = this.startTime.getTime();
						super.clearFalls();
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
						if (coe.getCause() instanceof IllegalObjectEntityException)
							super.fallCode = FALL_CODE_CREATE_IDENTIFIER;
						else
							super.fallCode = FALL_CODE_CREATE_MEASUREMENT;
						super.sleepCauseOfFall();
					}

					if (measurement != null) {
						this.transceiver.addMeasurement(measurement, this);
						super.numberOfScheduledMeasurements ++;
						super.lastMeasurementAcquisition = true;
					}

				}
			}

			this.processMeasurementResult();
			Log.debugMessage("numberOfReceivedMResults: " + super.numberOfReceivedMResults 
				   + ", numberOfScheduledMeasurements: " + super.numberOfScheduledMeasurements 
				   + ", lastMeasurementAcquisition: " + this.lastMeasurementAcquisition, Log.DEBUGLEVEL07);
			if (super.numberOfReceivedMResults == super.numberOfScheduledMeasurements && this.lastMeasurementAcquisition)
				this.complete();
			else {
				if (System.currentTimeMillis() - currentMeasurementStartTime > super.forgetFrame) {
					Log.debugMessage("Passed " + super.forgetFrame / 1000 + " sec from last measurement creation. Aborting test '"
							+ super.test.getId() + "'", Log.DEBUGLEVEL03);
					this.abort();
				}
			}

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

		}	//while
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_CREATE_IDENTIFIER:
				this.abort();
				break;
			case FALL_CODE_CREATE_MEASUREMENT:
				this.abort();
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}
}
