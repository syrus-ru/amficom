/*
 * $Id: OnetimeTestProcessor.java,v 1.21 2004/12/15 14:09:13 arseniy Exp $
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
 * @version $Revision: 1.21 $, $Date: 2004/12/15 14:09:13 $
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
		this.startTime = test.getStartTime();
		super.lastMeasurementAcquisition = (this.startTime.getTime() < System.currentTimeMillis());
		Log.debugMessage("Set lastMeasurementAcquisition: " + this.lastMeasurementAcquisition + "; startTime: " + this.startTime + ", current: " + (new Date(System.currentTimeMillis())), Log.DEBUGLEVEL08);
	}

	public void run() {
		Measurement measurement = null;
		while (super.running) {
			long time = this.startTime.getTime();
			if (! super.lastMeasurementAcquisition) {				
				if ( time <= System.currentTimeMillis()) {

					try {
						measurement = super.test.createMeasurement(MeasurementControlModule.iAm.getUserId(), this.startTime);
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

				}	//if (this.startTime.getTime() <= System.currentTimeMillis())
			}	//if (! super.lastMeasurementAcquisition)

			this.processMeasurementResult();
			Log.debugMessage("numberOfReceivedMResults: " + super.numberOfReceivedMResults 
							   + ", numberOfScheduledMeasurements: " + super.numberOfScheduledMeasurements 
							   + ", lastMeasurementAcquisition: " + this.lastMeasurementAcquisition, Log.DEBUGLEVEL07);
			if (super.numberOfReceivedMResults == super.numberOfScheduledMeasurements && this.lastMeasurementAcquisition)
				this.complete();
			else if (super.lastMeasurementAcquisition && (time + super.forgetFrame < System.currentTimeMillis())){
				Log.debugMessage("Past " + (super.forgetFrame/1000) + " sec since last measurement,"
								 + " forget acquire results for '" + super.test.getId().getIdentifierString() + "'", Log.DEBUGLEVEL03);
				this.abort();
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
