/*
 * $Id: OnetimeTestProcessor.java,v 1.25 2005/04/13 17:32:58 arseniy Exp $
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.25 $, $Date: 2005/04/13 17:32:58 $
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
		super.lastMeasurementAcquisition = (super.test.getNumberOfMeasurements() > 0);

		this.startTime = test.getStartTime();
		Log.debugMessage("Set lastMeasurementAcquisition: " + this.lastMeasurementAcquisition + "; startTime: " + this.startTime + ", current: " + (new Date(System.currentTimeMillis())), Log.DEBUGLEVEL08);
	}

	public void run() {
		while (super.running) {
			if (!super.lastMeasurementAcquisition) {
				if (this.startTime.getTime() <= System.currentTimeMillis()) {

					try {
						super.newMeasurementCreation(this.startTime);
						super.lastMeasurementAcquisition = true;
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

				}
			}

			super.processMeasurementResult();
			super.checkIfCompletedOrAborted();

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
