/*
 * $Id: ContinuousTestProcessor.java,v 1.12 2004/11/18 19:31:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;

import com.syrus.AMFICOM.measurement.Test;
//import com.syrus.AMFICOM.general.Identifier;
//import com.syrus.AMFICOM.general.ObjectNotFoundException;
//import com.syrus.AMFICOM.measurement.Measurement;
//import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
//import com.syrus.AMFICOM.measurement.corba.TestStatus;
//import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2004/11/18 19:31:10 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class ContinuousTestProcessor extends TestProcessor {
/**
 * This class is not implemented as unuseful
 * */	
	private Date nextTimeStamp;

	public ContinuousTestProcessor(Test test) {
		super(test);

//Not implemented
	}

	public void run() {
//		Identifier measurementId = null;
//		Measurement measurement = null;
//		while (super.running) {
//			if (this.nextTimeStamp != null) {
//				if (this.nextTimeStamp.getTime() <= System.currentTimeMillis()) {
//					try {
//						measurementId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE, 10);
//					}
//					catch (IllegalObjectEntityException ioee) {
//						Log.errorException(ioee);
//						Log.errorMessage("Aborted test '" + super.test.getId().toString() + "' because cannot create identifier for measurement");
//						super.shutdown();
//						continue;
//					}
//					catch (AMFICOMRemoteException are) {
//						Log.errorException(are);
//						super.sleepCauseOfFall();
//						continue;
//					}
//					try {
//						measurement = super.test.createMeasurement(measurementId,
//																											 MeasurementControlModule.iAm.getUserId(),
//																											 this.nextTimeStamp);
//						super.clearFalls();
//					}
//					catch (CreateObjectException coe) {
//						Log.errorException(coe);
//						super.sleepCauseOfFall();
//						continue;
//					}
//					
//					if (measurement != null)
//						super.transceiver.addMeasurement(measurement, this);
//					
//				}
//				
//
//				//after all
//				//measurement = null;
//				this.nextTimeStamp = null;
//			}
//			else {
//				// have got report after measurement ?				
//				Result result = (Result) super.measurementResultQueue.get(measurement);
//				if (result != null)
//					this.nextTimeStamp = new Date(System.currentTimeMillis());
//			}
//
//			try {
//				sleep(super.initialTimeToSleep);
//			}
//			catch (InterruptedException ie) {
//				Log.errorException(ie);
//			}
//		}	//while
	}

	protected void processFall() {
		
	}
}
