/*
 * $Id: PeriodicalTestProcessor.java,v 1.14 2004/08/16 10:48:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2004/08/16 10:48:22 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class PeriodicalTestProcessor extends TestProcessor {
	private static final long FRAME = 24*60*60*1000;//ms
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_CREATE_IDENTIFIER = 1;
	public static final int FALL_CODE_CREATE_MEASUREMENT = 2;

	private Date startTime;
	private Date endTime;
	private TemporalPattern temporalPattern;

	private List timeStampsList;	//List <Date timeStamp>
	private Date currentTimeStamp;

	private boolean allMeasurementsAcquired;

	public PeriodicalTestProcessor(Test test) {
		super(test);

		this.startTime = test.getStartTime();
		this.endTime = test.getEndTime();
		try {
			this.temporalPattern = new TemporalPattern(test.getTemporalPatternId());
		}
		catch (Exception e) {
			Log.errorException(e);
			super.shutdown();
		}

		this.timeStampsList = new ArrayList(10);
		this.currentTimeStamp = null;

		this.allMeasurementsAcquired = false;
	}

	private Date getCurrentTimeStamp() {
		Date timeStamp = null;
		if (! this.allMeasurementsAcquired) {
			if (! this.timeStampsList.isEmpty()) {
				timeStamp = (Date)this.timeStampsList.remove(0);
			}
			else {
				long start = System.currentTimeMillis();
				if (start <= this.endTime.getTime())
					this.timeStampsList.addAll(this.temporalPattern.getTimes(start, start + FRAME));
				else
					this.allMeasurementsAcquired = true;
			}
		}
		return timeStamp;
	}

	public void run() {
		Identifier measurementId = null;
		Measurement measurement = null;
		while (super.running) {
			if (! allMeasurementsAcquired) {
				if (this.currentTimeStamp == null) {
					this.currentTimeStamp = this.getCurrentTimeStamp();
				}
				else {
					try {
						measurementId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE, 10);
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
						Log.debugMessage("Aborting test '" + super.test.getId().toString() + "' because cannot create identifier for measurement", Log.DEBUGLEVEL03);
						super.shutdown();
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.value() == ErrorCode._ERROR_ILLEGAL_OBJECT_ENTITY) {
							Log.errorMessage("Server nothing knows about entity '" + ObjectEntities.MEASUREMENT_ENTITY + "', code " + ObjectEntities.MEASUREMENT_ENTITY_CODE);
							super.shutdown();
						}
						else {
							Log.errorMessage("Server cannot generate identifier -- " + are.message + "; sleepeng cause of fall");
							super.fallCode = FALL_CODE_CREATE_IDENTIFIER;
							super.sleepCauseOfFall();
						}
					}	//catch

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
							super.sleepCauseOfFall();
						}
						catch (IllegalObjectEntityException ioee) {
							Log.errorException(ioee);
						}
					}	//if (measurementId != null)

					if (measurement != null) {
						super.transceiver.addMeasurement(measurement, this);
						this.currentTimeStamp = null;
					}
				}	//if (this.currentTimeStamp == null)
			}	//if (! allMeasurementsAcquired)

			super.processMeasurementResult();
			if (super.numberOfReceivedMResults == super.numberOfScheduledMeasurements && this.allMeasurementsAcquired)
				super.shutdown();
		}
	}

	protected void cleanup() {
		super.cleanup();
		this.timeStampsList.clear();
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_CREATE_IDENTIFIER:
				this.continueWithNextMeasurement();
				break;
			case FALL_CODE_CREATE_MEASUREMENT:
				this.continueWithNextMeasurement();
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}

	private void continueWithNextMeasurement() {
		this.currentTimeStamp = null;
	}
}
