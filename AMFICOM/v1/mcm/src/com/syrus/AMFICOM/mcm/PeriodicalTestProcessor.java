/*
 * $Id: PeriodicalTestProcessor.java,v 1.34 2005/03/30 13:12:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.34 $, $Date: 2005/03/30 13:12:55 $
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
		try {
			this.temporalPattern = (TemporalPattern) MeasurementStorableObjectPool.getStorableObject(test.getTemporalPatternId(), true);
		}
		catch (ApplicationException ae) {
			Log.errorMessage("Cannot load temporal pattern '" + test.getTemporalPatternId() + "' for test '" + test.getId() + "'");
			this.abort();
		}

		this.timeStampsList = new LinkedList();
		this.currentTimeStamp = null;
	}

	private Date getCurrentTimeStamp() {
		Date timeStamp = null;
		if (!super.lastMeasurementAcquisition) {
			if (!this.timeStampsList.isEmpty()) {
				timeStamp = (Date) this.timeStampsList.remove(0);
			}
			else {
				long start = System.currentTimeMillis();
				if (start <= this.endTime) {
					List times = this.temporalPattern.getTimes(start, Math.min(start + FRAME, this.endTime));
//--------
					System.out.println("From " + (new Date(start)) + " to " + (new Date(Math.min(start + FRAME, this.endTime))));
					for (Iterator it = times.iterator(); it.hasNext();)
						System.out.println("time: " + it.next());
//--------
					this.timeStampsList.addAll(times);
					if (!this.timeStampsList.isEmpty())
						timeStamp = (Date) this.timeStampsList.remove(0);
				}
				else
					super.lastMeasurementAcquisition = true;
			}
		}
		return timeStamp;
	}

	public void run() {
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
							measurement = super.test.createMeasurement(MeasurementControlModule.iAm.getUserId(), this.currentTimeStamp);
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
							super.transceiver.addMeasurement(measurement, this);
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
				this.complete();
			else
				if (super.lastMeasurementAcquisition && (this.endTime + super.forgetFrame < System.currentTimeMillis())) {
					Log.debugMessage("Passed " + (super.forgetFrame/1000) + " sec since last measurement,"
									 + " forget acquire results for '" + super.test.getId().getIdentifierString() + "'", Log.DEBUGLEVEL03);
					this.abort();
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
	}

	private void continueWithNextMeasurement() {
		this.currentTimeStamp = null;
	}
}
