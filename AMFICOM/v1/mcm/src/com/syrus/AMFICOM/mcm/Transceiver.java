/*
 * $Id: Transceiver.java,v 1.17 2004/08/15 14:40:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.Collections;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2004/08/15 14:40:14 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class Transceiver extends SleepButWorkThread {
	public static final int KIS_TICK_TIME = 1;
	public static final int KIS_MAX_FALLS = 10;
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_TRANSMIT_MEASUREMENT = 1;
	public static final int FALL_CODE_GENERATE_IDENTIFIER = 2;

	private final String taskFileName;
	private final String reportFileName;
	private boolean running;

	private List measurementQueue;//List <Measurement measurement>
	private Map testProcessors;//Map <Identifier measurementId, TestProcessor testProcessor>
	private Map processingMeasurements;//Map <Identifier measurementId, Measurement measurement>
	private KISReport kisReport;

	/*	Variables for method processFall()	*/
	private Measurement measurementToRemove;

	static {
		System.loadLibrary("mcmtransceiver");
	}
	
	public Transceiver(Identifier kisId) {
		super(ApplicationProperties.getInt("KISTickTime", KIS_TICK_TIME) * 1000, ApplicationProperties.getInt("KISMaxFalls", KIS_MAX_FALLS));

		String kisIdStr = kisId.toString();
		this.taskFileName = "task" + kisIdStr;
    this.reportFileName = "report" + kisIdStr;
		this.running = true;
		
		this.measurementQueue = Collections.synchronizedList(new ArrayList());
		this.processingMeasurements = Collections.synchronizedMap(new Hashtable());
		this.testProcessors = Collections.synchronizedMap(new Hashtable());

		this.measurementToRemove = null;
	}

	protected void addMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			this.measurementQueue.add(measurement);
			this.testProcessors.put(measurementId, testProcessor);
		}
		else
			Log.errorMessage("Status: " + measurement.getStatus().value() + " of measurement '" + measurementId.toString() + "' not SCHEDULED -- cannot add to queue");
	}

	public void run() {
		Measurement measurement = null;
		Identifier measurementId = null;
		TestProcessor testProcessor = null;
		Result result;
		while (this.running) {
			if (measurement == null) {
				if (! this.measurementQueue.isEmpty()) {
					measurement = (Measurement)this.measurementQueue.get(0);
					measurementId = measurement.getId();
				}
			}
			else {
				if (this.transmit(measurement)) {
					try {
						measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING,
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
					super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
					this.measurementToRemove = measurement;
					super.sleepCauseOfFall();
				}
			}	//else if (measurement == null)

			if (this.kisReport == null) {
				this.kisReport = this.receive();
			}
			else {
				measurementId = this.kisReport.getMeasurementId();
				Log.debugMessage("Received report for measurement '" + measurementId.toString() + "'", Log.DEBUGLEVEL03);
				measurement = (Measurement)this.processingMeasurements.remove(measurementId);
				if (measurement != null) {
					testProcessor = (TestProcessor)this.testProcessors.remove(measurementId);
					if (testProcessor != null) {
						result = null;
						try {
							result = this.kisReport.createResult(measurement);
							measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRED,
																			 MeasurementControlModule.iAm.getUserId());
							super.clearFalls();
						}
						catch (IllegalDataException ide) {
							Log.errorException(ide);
							this.throwAwayKISReport();
						}
						catch (MeasurementException me) {
							if (me.getCause() instanceof AMFICOMRemoteException) {
								Log.debugMessage("Cannot get identifier - trying to wait", Log.DEBUGLEVEL03);
								super.fallCode = FALL_CODE_GENERATE_IDENTIFIER;
								super.sleepCauseOfFall();
							}
							else
								this.throwAwayKISReport();
						}
						catch (UpdateObjectException uoe) {
							Log.errorException(uoe);
						}

						if (result != null) {
							testProcessor.addMeasurementResult(result);
							this.kisReport = null;
						}
					}	//if (testProcessor != null)
					else {
						Log.errorMessage("Cannot find test processor for measurement '" + measurementId.toString() + "'; throwing away it's report");
						this.throwAwayKISReport();
					}
				}	//if (measurement != null)
				else {
					Log.errorMessage("Cannot find measurement for id '" + measurementId.toString() + "'; throwing away it's report");
					this.throwAwayKISReport();
				}

				try {
					sleep(super.initialTimeToSleep);
				}
				catch (InterruptedException ie) {
					Log.errorException(ie);
				}
			}	//else if (this.kisReport != null)
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_TRANSMIT_MEASUREMENT:
				this.removeMeasurement();
				break;
			case FALL_CODE_GENERATE_IDENTIFIER:
				this.throwAwayKISReport();
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}
	
	private void removeMeasurement() {
		if (this.measurementToRemove != null) {
			this.measurementQueue.remove(this.measurementToRemove);
			this.testProcessors.remove(this.measurementToRemove.getId());
			this.measurementToRemove = null;
		}
	}
	
	private void throwAwayKISReport() {
		if (this.kisReport != null) {
			Log.debugMessage("Throwing away kis report for measurement '" + this.kisReport.getMeasurementId() + "'", Log.DEBUGLEVEL03);
			this.kisReport = null;
		}
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
