/*
 * $Id: OnetimeTestProcessor.java,v 1.10 2004/08/16 10:48:22 arseniy Exp $
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
 * @version $Revision: 1.10 $, $Date: 2004/08/16 10:48:22 $
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
	}

	public void run() {
		Identifier measurementId = null;
		Measurement measurement = null;
		while (super.running) {
			if (this.startTime.getTime() <= System.currentTimeMillis()) {
				try {
					measurementId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE, 10);
					super.clearFalls();
				}
				catch (IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
					Log.debugMessage("Aborting test '" + super.test.getId().toString() + "' because cannot create identifier for measurement", Log.DEBUGLEVEL03);
					super.shutdown();
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code == ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY) {
						Log.errorMessage("Server nothing knows about entity '" + ObjectEntities.MEASUREMENT_ENTITY + "', code " + ObjectEntities.MEASUREMENT_ENTITY_CODE);
						super.shutdown();
					}
					else {
						Log.errorMessage("Server cannot generate identifier -- " + are.message + "; sleepeng cause of fall");
						super.fallCode = FALL_CODE_CREATE_IDENTIFIER;
						super.sleepCauseOfFall();
					}
				}

				if (measurementId != null) {
					try {
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

				if (measurement != null)
					super.transceiver.addMeasurement(measurement, this);

			}	//if (this.startTime.getTime() <= System.currentTimeMillis())

			super.processMeasurementResult();

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
				super.shutdown();
				break;
			case FALL_CODE_CREATE_MEASUREMENT:
				super.shutdown();
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}
}
