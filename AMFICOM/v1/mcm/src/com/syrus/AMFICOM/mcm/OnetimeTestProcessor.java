/*
 * $Id: OnetimeTestProcessor.java,v 1.12 2004/08/23 20:48:29 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2004/08/23 20:48:29 $
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
		Identifier measurementId = null;
		Measurement measurement = null;
		while (super.running) {
			if (! super.lastMeasurementAcquisition) {
				if (this.startTime.getTime() <= System.currentTimeMillis()) {
					try {
						measurementId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE, 10);
						super.clearFalls();
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
						Log.debugMessage("Aborting test '" + super.test.getId() + "' because cannot create identifier for measurement", Log.DEBUGLEVEL07);
						super.abort();
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code == ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY) {
							Log.errorMessage("Server nothing knows about entity '" + ObjectEntities.MEASUREMENT_ENTITY + "', code " + ObjectEntities.MEASUREMENT_ENTITY_CODE);
							super.abort();
						}
						else {
							Log.errorMessage("Server cannot generate identifier -- " + are.message + "; sleepeng cause of fall");
							MeasurementControlModule.resetMServerConnection();
							super.fallCode = FALL_CODE_CREATE_IDENTIFIER;
							super.sleepCauseOfFall();
						}
					}

					if (measurementId != null) {
						try {
							Log.debugMessage("Creating measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
							measurement = super.test.createMeasurement(measurementId,
																												 MeasurementControlModule.iAm.getUserId(),
																												 this.startTime);
							MeasurementStorableObjectPool.putStorableObject(measurement);
							super.clearFalls();
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
							super.fallCode = FALL_CODE_CREATE_MEASUREMENT;
							super.sleepCauseOfFall();
						}
						catch (IllegalObjectEntityException ioee) {
							Log.errorException(ioee);
						}
					}	//if (measurementId != null)

					if (measurement != null) {
						super.transceiver.addMeasurement(measurement, this);
						super.numberOfScheduledMeasurements ++;
						super.lastMeasurementAcquisition = true;
					}

				}	//if (this.startTime.getTime() <= System.currentTimeMillis())
			}	//if (! super.lastMeasurementAcquisition)

			super.processMeasurementResult();
System.out.println("numberOfReceivedMResults: " + super.numberOfReceivedMResults + ", numberOfScheduledMeasurements: " + super.numberOfScheduledMeasurements + ", lastMeasurementAcquisition: " + lastMeasurementAcquisition);
			if (super.numberOfReceivedMResults == super.numberOfScheduledMeasurements && this.lastMeasurementAcquisition)
				super.complete();

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
				super.abort();
				break;
			case FALL_CODE_CREATE_MEASUREMENT:
				super.abort();
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}
}
