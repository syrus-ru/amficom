/*
 * $Id: Transceiver.java,v 1.19 2004/08/22 19:10:57 arseniy Exp $
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
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.19 $, $Date: 2004/08/22 19:10:57 $
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
		this.testProcessors = Collections.synchronizedMap(new Hashtable());
		this.kisReport = null;

		this.measurementToRemove = null;
	}

	protected void addMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			this.measurementQueue.add(measurement);
			this.testProcessors.put(measurementId, testProcessor);
		}
		else
			Log.errorMessage("Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not SCHEDULED -- cannot add to queue");
	}

	public void run() {
		Measurement measurement;
		Identifier measurementId;
		TestProcessor testProcessor = null;
		Result result;
		
		while (this.running) {
			if (! this.measurementQueue.isEmpty()) {
				measurement = (Measurement)this.measurementQueue.get(0);
				measurementId = measurement.getId();
				if (this.transmit(measurement.getId().toString(),
													measurement.getType().getCodename(),
													measurement.getLocalAddress(),
													measurement.getSetup().getParameterTypeCodenames(),
													measurement.getSetup().getParameterValues())) {
					try {
						measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING,
																		 MeasurementControlModule.iAm.getUserId());
						MeasurementStorableObjectPool.putStorableObject(measurement);
					}
					catch (UpdateObjectException uoe) {
						Log.errorException(uoe);
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
					Log.debugMessage("Transmitted measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
					this.measurementQueue.remove(measurement);
					super.clearFalls();
				}
				else {
					Log.errorMessage("Cannot transmit measurement '" + measurementId + "'");
					super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
					this.measurementToRemove = measurement;
					super.sleepCauseOfFall();
				}
			}	//if (! this.measurementQueue.isEmpty())

			if (this.kisReport == null) {
				this.kisReport = this.receive();
			}
			else {
				measurementId = this.kisReport.getMeasurementId();
				Log.debugMessage("Received report for measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
				measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(measurementId, true);
				if (measurement != null) {
					testProcessor = (TestProcessor)this.testProcessors.remove(measurementId);
					if (testProcessor != null) {
						result = null;
						try {
							result = this.kisReport.createResult();
							measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRED,
																			 MeasurementControlModule.iAm.getUserId());
							super.clearFalls();
						}
						catch (MeasurementException me) {
							if (me.getCause() instanceof AMFICOMRemoteException) {
								Log.debugMessage("Cannot get identifier - trying to wait", Log.DEBUGLEVEL07);
								MeasurementControlModule.resetMServerConnection();
								super.fallCode = FALL_CODE_GENERATE_IDENTIFIER;
								super.sleepCauseOfFall();
							}
							else {
								Log.errorException(me);
								this.throwAwayKISReport();
							}
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
						Log.errorMessage("Cannot find test processor for measurement '" + measurementId + "'; throwing away it's report");
						this.throwAwayKISReport();
					}
				}	//if (measurement != null)
				else {
					Log.errorMessage("Cannot find measurement for id '" + measurementId + "'; throwing away it's report");
					this.throwAwayKISReport();
				}
				
				try {
					sleep(super.initialTimeToSleep);
				}
				catch (InterruptedException ie) {
					Log.errorException(ie);
				}
			}	//else if (this.kisReport == null)

		}	//while
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
			Log.debugMessage("Throwing away kis report for measurement '" + this.kisReport.getMeasurementId() + "'", Log.DEBUGLEVEL07);
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

	//private native boolean transmit(Measurement measurement);
	private native boolean transmit(String measurementId,
																	String measurementTypeCodename,
																	String localAddress,
																	String[] parameterTypeCodenames,
																	byte[][] parameterValues);

	private native KISReport receive();
}
