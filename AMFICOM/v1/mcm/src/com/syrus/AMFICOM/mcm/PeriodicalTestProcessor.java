/*
 * $Id: PeriodicalTestProcessor.java,v 1.25 2004/11/17 08:22:23 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
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
 * @version $Revision: 1.25 $, $Date: 2004/11/17 08:22:23 $
 * @author $Author: bob $
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

		this.timeStampsList = new LinkedList();
		this.currentTimeStamp = null;
	}

	private Date getCurrentTimeStamp() {
		Date timeStamp = null;
		if (! super.lastMeasurementAcquisition) {
			if (! this.timeStampsList.isEmpty()) {
				timeStamp = (Date)this.timeStampsList.remove(0);
			}
			else {
				long start = System.currentTimeMillis();
				if (start <= this.endTime) {
					List times = this.temporalPattern.getTimes(start, Math.min(start + FRAME, this.endTime));
					System.out.println("From " + (new Date(start)) + " to " + (new Date(Math.min(start + FRAME, this.endTime))));
					for (Iterator it = times.iterator(); it.hasNext();)
						System.out.println("time: " + it.next());
					this.timeStampsList.addAll(times);
					if (! this.timeStampsList.isEmpty())
						timeStamp = (Date)this.timeStampsList.remove(0);
				}
				else
					super.lastMeasurementAcquisition = true;
			}
		}
		return timeStamp;
	}

	public void run() {
		Identifier measurementId = null;
		Measurement measurement = null;
		while (super.running) {
			if (! super.lastMeasurementAcquisition) {
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
							MeasurementControlModule.transmissionManager.addMeasurement(measurement, super.kis, this);
							super.numberOfScheduledMeasurements ++;
							this.currentTimeStamp = null;
						}

					}	//if (this.currentTimeStamp.getTime() <= System.currentTimeMillis())
				}	//if (this.currentTimeStamp == null)
			}	//if (! super.lastMeasurementAcquisition)

			super.processMeasurementResult();
			Log.debugMessage('\'' + super.test.getId().getIdentifierString() 
							 + "' numberOfReceivedMResults: " + super.numberOfReceivedMResults 
							 + ", numberOfScheduledMeasurements: " + super.numberOfScheduledMeasurements 
							 + ", lastMeasurementAcquisition: " + super.lastMeasurementAcquisition, Log.DEBUGLEVEL07);
			if (super.numberOfReceivedMResults == super.numberOfScheduledMeasurements && super.lastMeasurementAcquisition)
				super.complete();
			else if (super.lastMeasurementAcquisition && (this.endTime + super.forgetFrame < System.currentTimeMillis())){
					Log.debugMessage("Past " + (super.forgetFrame/1000) + " sec since last measurement,"
									 + " forget acquire results for '" + super.test.getId().getIdentifierString() + "'", Log.DEBUGLEVEL03);
					super.abort();
				}

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
		if (this.timeStampsList != null)
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
