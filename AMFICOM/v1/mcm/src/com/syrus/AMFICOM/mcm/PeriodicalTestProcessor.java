/*
 * $Id: PeriodicalTestProcessor.java,v 1.15 2004/08/22 19:10:57 arseniy Exp $
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
 * @version $Revision: 1.15 $, $Date: 2004/08/22 19:10:57 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class PeriodicalTestProcessor extends TestProcessor {
	private static final long FRAME = 24*60*60*1000;//ms
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_CREATE_IDENTIFIER = 1;
	public static final int FALL_CODE_CREATE_MEASUREMENT = 2;

	private long endTime;
	private TemporalPattern temporalPattern;

	private List timeStampsList;	//List <Date timeStamp>
	private Date currentTimeStamp;

	public PeriodicalTestProcessor(Test test) {
		super(test);

		this.endTime = test.getEndTime().getTime();
		this.temporalPattern = test.getTemporalPattern();
		if (this.temporalPattern == null) {
			Log.errorMessage("Temporal pattern is NULL");
			super.abort();
		}

		this.timeStampsList = new ArrayList(10);
		this.currentTimeStamp = null;
	}

	private Date getCurrentTimeStamp() {
		Date timeStamp = null;
		if (! super.lastMeasurement) {
			if (! this.timeStampsList.isEmpty()) {
				timeStamp = (Date)this.timeStampsList.remove(0);
			}
			else {
				long start = System.currentTimeMillis();
				if (start <= this.endTime) {
					this.timeStampsList.addAll(this.temporalPattern.getTimes(start, Math.min(start + FRAME, this.endTime)));
					timeStamp = (Date)this.timeStampsList.remove(0);
				}
				else
					super.lastMeasurement = true;
			}
		}
		return timeStamp;
	}

	public void run() {
		Identifier measurementId = null;
		Measurement measurement = null;
		while (super.running) {
			if (! super.lastMeasurement) {
				if (this.currentTimeStamp == null) {
					this.currentTimeStamp = this.getCurrentTimeStamp();
					Log.debugMessage("Next measurement at: " + this.currentTimeStamp, Log.DEBUGLEVEL07);
				}
				else {
					if (this.currentTimeStamp.getTime() <= System.currentTimeMillis()) {
						try {
							measurementId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE, 10);
						}
						catch (IllegalObjectEntityException ioee) {
							Log.errorException(ioee);
							Log.debugMessage("Aborting test '" + super.test.getId().toString() + "' because cannot create identifier for measurement", Log.DEBUGLEVEL03);
							super.abort();
						}
						catch (AMFICOMRemoteException are) {
							if (are.error_code.value() == ErrorCode._ERROR_ILLEGAL_OBJECT_ENTITY) {
								Log.errorMessage("Server nothing knows about entity '" + ObjectEntities.MEASUREMENT_ENTITY + "', code " + ObjectEntities.MEASUREMENT_ENTITY_CODE);
								super.abort();
							}
							else {
								Log.errorMessage("Server cannot generate identifier -- " + are.message + "; sleepeng cause of fall");
								MeasurementControlModule.resetMServerConnection();
								super.fallCode = FALL_CODE_CREATE_IDENTIFIER;
								super.sleepCauseOfFall();
							}
						}	//catch
	
						if (measurementId != null) {
							try {
								measurement = super.test.createMeasurement(measurementId,
																													 MeasurementControlModule.iAm.getUserId(),
																													 this.currentTimeStamp);
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
							super.numberOfScheduledMeasurements ++;
							this.currentTimeStamp = null;
						}

					}	//if (this.currentTimeStamp.getTime() <= System.currentTimeMillis())
				}	//if (this.currentTimeStamp == null)
			}	//if (! super.lastMeasurement)

			super.processMeasurementResult();
System.out.println("numberOfReceivedMResults: " + super.numberOfReceivedMResults + ", numberOfScheduledMeasurements: " + super.numberOfScheduledMeasurements + ", lastMeasurement: " + lastMeasurement);
			if (super.numberOfReceivedMResults == super.numberOfScheduledMeasurements && super.lastMeasurement)
				super.complete();

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
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
