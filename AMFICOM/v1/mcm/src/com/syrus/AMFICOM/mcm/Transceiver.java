/*
 * $Id: Transceiver.java,v 1.13 2004/07/30 14:31:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.Collections;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2004/07/30 14:31:21 $
 * @author $Author: bob $
 * @module mcm_v1
 */

public class Transceiver extends SleepButWorkThread {
	public static final int KIS_TICK_TIME = 1;
	public static final int KIS_MAX_FALLS = 10;

	private final String taskFileName;
	private final String reportFileName;
	private boolean running;
	
	private Map measurementQueue;//Map <Measurement, KISReport>
	private Map processingMeasurements;//Map <Identifier, Measurement>
	private Map testProcessors;//Map <Measurement, TestProcessor>

	static {
		System.loadLibrary("mcmtransceiver");
	}
	
	public Transceiver(Identifier kisId) {
		super(ApplicationProperties.getInt("KISTickTime", KIS_TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", KIS_MAX_FALLS));

		String kisIdStr = kisId.toString();
		this.taskFileName = "task" + kisIdStr;
    this.reportFileName = "report" + kisIdStr;

		this.running = true;
		
		this.measurementQueue = Collections.synchronizedMap(new Hashtable());
		this.processingMeasurements = Collections.synchronizedMap(new Hashtable());
		this.testProcessors = Collections.synchronizedMap(new Hashtable());
	}

	protected void addMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			this.measurementQueue.put(measurement, null);
			this.testProcessors.put(measurement, testProcessor);
		}
		else
			Log.errorMessage("Status: " + measurement.getStatus().value() + " of measurement '" + measurementId.toString() + "' not SCHEDULED -- cannot add to queue");
	}
	
	protected KISReport getKISPreport(Measurement measurement){
		return (KISReport) this.measurementQueue.get(measurement);		
	}

	public void run() {
		Measurement measurement = null;
		Identifier measurementId = null;
		KISReport kisReport = null;
		TestProcessor testProcessor = null;
		Result result;
		Iterator measumentIterator = this.measurementQueue.keySet().iterator();
		while (this.running) {
			if (measurement == null) {
				if (! this.measurementQueue.isEmpty()) {
					measurement = (Measurement)measumentIterator.next();
					measurementId = measurement.getId();
				}
			}//if (measurement == null)
			else {
				if (this.transmit(measurement)) {
					try {
						measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING,
																	MeasurementControlModule.iAm.getUserId());
					}
					catch (UpdateObjectException uoe) {
						Log.errorException(uoe);
					}
					this.processingMeasurements.put(measurementId, measurement);
					Log.debugMessage("Transmitted measurement '" + measurementId.toString() + "'", Log.DEBUGLEVEL03);
					this.measurementQueue.remove(measurement);
					measurement = null;
					super.clearFalls();
				}
				else {
					Log.errorMessage("Cannot transmit measurement '" + measurementId.toString() + "'");
					super.sleepCauseOfFall();
				}
			}//else if (measurement == null)
			
			kisReport = this.receive();
			if (kisReport != null) {
				measurementId = kisReport.getMeasurementId();
				Log.debugMessage("Received report for measurement '" + measurementId.toString() + "'", Log.DEBUGLEVEL03);
				measurement = (Measurement)this.processingMeasurements.remove(measurementId);
				if (measurement != null) {
					testProcessor = (TestProcessor)this.testProcessors.remove(measurement);
					if  (testProcessor != null) {
						result = null;
						try {
							result = kisReport.createResult(measurement);
							measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRED,
																		MeasurementControlModule.iAm.getUserId());
							super.clearFalls();
						}
						catch (MeasurementException me) {
							Log.errorException(me);
							super.sleepCauseOfFall();
						}
						catch (IllegalDataException ide) {
							Log.errorException(ide);
						}
						catch (UpdateObjectException uoe) {
							Log.errorException(uoe);
						}
						if (result != null){
							this.measurementQueue.put(measurement, result);
							testProcessor.addMeasurementResult(result);
						}
					}
					else
						Log.errorMessage("Cannot find test processor for measurement '" + measurementId.toString() + "'; throwing away it's report");
				}
				else
					Log.errorMessage("Cannot find measurement for id '" + measurementId.toString() + "'; throwing away it's report");
			}
			
			
///*			We need not this as receive() already do it*/
//			try {
//				sleep(super.initialTimeToSleep);
//			}
//			catch (InterruptedException ie) {
//				Log.errorException(ie);
//			}
		}//while
	}

	protected void shutdown() {
		this.running = false;
		/*What to do with measurements ?!*/
		this.cleanup();
	}
	
	private void cleanup() {
		this.measurementQueue.clear();
		this.processingMeasurements.clear();
		this.testProcessors.clear();
	}

//	protected void addProcessingMeasurement(Measurement measurement, TestProcessor testProcessor) {
//		Identifier measurement_id = measurement.getId();
//		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_PROCESSING) {
//			this.processingMeasurements.put(measurement_id, measurement);
//			this.testProcessors.put(measurement, testProcessor);
//		}
//		else
//			Log.errorMessage("Status of measurement '" + measurement_id.toString() + "' not PROCESSING -- cannot add to queue");
//	}
//
//	protected void abortMeasurements(TestProcessor testProcessor) {
//		if (this.testProcessors.containsValue(testProcessor)) {
//			Enumeration tp_enumeration = this.testProcessors.elements();
//			Enumeration m_enumeration = this.testProcessors.keys();
//			Measurement measurement;
//			while (tp_enumeration.hasMoreElements()) {
//				measurement = (Measurement)m_enumeration.nextElement();
//				if (((TestProcessor)tp_enumeration.nextElement()).equals(testProcessor)) {
//					this.measurementQueue.remove(measurement);
//					this.testProcessors.remove(measurement);
//					this.processingMeasurements.remove(measurement.getId());
//					try {
//						measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ABORTED,
//																	MeasurementControlModule.iAm.getUserId());
//					}
//					catch (Exception e) {
//						Log.errorException(e);
//					}
//				}
//			}
//		}
//		else
//			Log.errorMessage("Test processor doesn't contain in table");
//	}

	private native boolean transmit(Measurement measurement);

	private native KISReport receive();
}
