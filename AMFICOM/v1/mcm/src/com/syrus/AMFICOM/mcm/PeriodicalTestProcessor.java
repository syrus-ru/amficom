/*
 * $Id: PeriodicalTestProcessor.java,v 1.12 2004/08/12 13:35:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2004/08/12 13:35:08 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class PeriodicalTestProcessor extends TestProcessor {
	private static final long FRAME = 24*60*60*1000;//ms

	private TemporalPattern temporalPattern;
	private List timeStampsList;//List <Date>

	public PeriodicalTestProcessor(Test test) {
		super(test);

		int testStatus = test.getStatus().value();
		switch (testStatus) {
			case TestStatus._TEST_STATUS_SCHEDULED:
				//Normal
				break;
			case TestStatus._TEST_STATUS_PROCESSING:
				try {
					this.completeLastMeasurement();
				}
				catch (TestProcessingException tpe) {
					super.shutdown();
				}
				break;
			default:
				Log.errorMessage("Inappropriate status: " + testStatus + " of test: '" + test.getId().toString() + "'");
				super.shutdown();
		}

		try {
			this.temporalPattern = new TemporalPattern(test.getTemporalPatternId());
		}
		catch (Exception e) {
			Log.errorException(e);
			super.shutdown();
		}
		
		this.timeStampsList = Collections.synchronizedList(new ArrayList(0));
	}
	
	private void completeLastMeasurement() throws TestProcessingException {
		Measurement measurement;
		try {
			measurement = super.test.retrieveLastMeasurement();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			return;
		}
		catch (Exception e) {
			Log.errorException(e);
			throw new TestProcessingException("Cannot retrieve last measurement for test '" + super.test.getId().toString() + "'", e);
		}

		int measurementStatus = measurement.getStatus().value();
		switch (measurementStatus) {
			case MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED:
			case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING:
				//process measurement
				break;
			case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRED:
				//analyse and/or evaluate
				break;
			case MeasurementStatus._MEASUREMENT_STATUS_ANALYZED_OR_EVALUATED:
				//all results of the measurement must go to server
				break;
			case MeasurementStatus._MEASUREMENT_STATUS_COMPLETED:
				//do next
				break;
			case MeasurementStatus._MEASUREMENT_STATUS_ABORTED:
				//do next (?)
				break;
		}
	}

	public void run() {
		Date nextTimeStamp = null;
		Identifier measurementId = null;
		Measurement measurement = null;
		while (super.running) {
			if (nextTimeStamp != null) {
				if (nextTimeStamp.getTime() <= System.currentTimeMillis()) {
					try {
						measurementId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE, 10);
						super.clearFalls();
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
						Log.errorMessage("Aborted test '" + super.test.getId().toString() + "' because cannot create identifier for measurement");
						super.shutdown();
						continue;
					}
					catch (AMFICOMRemoteException are) {
						Log.errorException(are);
						super.sleepCauseOfFall();
						continue;
					}
					try {
						measurement = super.test.createMeasurement(measurementId,
																											 MeasurementControlModule.iAm.getUserId(),
																											 nextTimeStamp);
						super.clearFalls();
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
						super.sleepCauseOfFall();
						continue;
					}
					
					if (measurement != null)
						super.transceiver.addMeasurement(measurement, this);
					
				}
				

				//after all
				measurement = null;
				nextTimeStamp = null;
			}
			else {
				if (! this.timeStampsList.isEmpty())
					nextTimeStamp = (Date)this.timeStampsList.remove(0);
				else
					this.fillTimeStampsList();
			}

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}	//while
	}

	private void fillTimeStampsList() {
		long start = System.currentTimeMillis();
		this.timeStampsList.addAll(this.temporalPattern.getTimes(start, start + FRAME));
	}
	
	protected void cleanup() {
		super.cleanup();
		this.timeStampsList.clear();
	}
}
