/*
 * $Id: PeriodicalTestProcessor.java,v 1.38 2005/04/22 16:05:41 arseniy Exp $
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
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.CronTemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.38 $, $Date: 2005/04/22 16:05:41 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class PeriodicalTestProcessor extends TestProcessor {
	private static final long FRAME = 24*60*60*1000;//ms	
	
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_CREATE_IDENTIFIER = 1;
	public static final int FALL_CODE_CREATE_MEASUREMENT = 2;

	private long endTime;
	private CronTemporalPattern temporalPattern;

	private List timeStampsList;	//List <Date timeStamp>
	private Date currentTimeStamp;

	public PeriodicalTestProcessor(Test test) {
		super(test);

		this.endTime = test.getEndTime().getTime();
		try {
			this.temporalPattern = (CronTemporalPattern) MeasurementStorableObjectPool.getStorableObject(test.getTemporalPatternId(), true);
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
					Set times = this.temporalPattern.getTimes(start, Math.min(start + FRAME, this.endTime));
//--------
					Log.debugMessage("PeriodicalTestProcessor.getCurrentTimeStamp | From " + (new Date(start))
							+ " to " + (new Date(Math.min(start + FRAME, this.endTime))), Log.DEBUGLEVEL09);
					for (Iterator it = times.iterator(); it.hasNext();)
						Log.debugMessage("time: " + it.next(), Log.DEBUGLEVEL09);
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
		while (super.running) {
			if (! super.lastMeasurementAcquisition) {
				if (this.currentTimeStamp == null) {
					this.currentTimeStamp = this.getCurrentTimeStamp();
					Log.debugMessage("Next measurement at: " + this.currentTimeStamp, Log.DEBUGLEVEL07);
				}
				else {
					if (this.currentTimeStamp.getTime() <= System.currentTimeMillis()) {

						try {
							super.newMeasurementCreation(this.currentTimeStamp);
							this.currentTimeStamp = null;
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

					}	//if (this.currentTimeStamp.getTime() <= System.currentTimeMillis())
				}	//if (this.currentTimeStamp == null)
			}	//if (! super.lastMeasurementAcquisition)

			super.processMeasurementResult();
			super.checkIfCompletedOrAborted();

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
