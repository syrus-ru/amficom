package com.syrus.AMFICOM.mcm;

import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collections;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

public class Transceiver extends Thread {
	public static final int KIS_TICK_TIME = 1;

	private final String taskFileName;
	private final String reportFileName;
	private long kisTickTime;
	private boolean running;
	private List measurementQueue;
	/* key - Measurement, value - TestProcessor*/
	private Hashtable testProcessors;
	/* key - Identifier, value - Measurement*/
	private Hashtable processingMeasurements;

	static {
		System.loadLibrary("mcmtransceiver");
	}
	
	public Transceiver(String kisIdStr) {
		this.taskFileName = "task" + kisIdStr;
    this.reportFileName = "report" + kisIdStr;
		this.kisTickTime = ApplicationProperties.getInt("KISTickTime", KIS_TICK_TIME);
		this.running = true;
		this.measurementQueue = Collections.synchronizedList(new ArrayList());
		this.testProcessors = new Hashtable(Collections.synchronizedMap(new Hashtable()));
		this.processingMeasurements = new Hashtable(Collections.synchronizedMap(new Hashtable()));
	}

	public void run() {
		Measurement measurement;
		KISReport kisReport;
		TestProcessor testProcessor;
		Identifier measurement_id;
		Result result;
		while (this.running) {
			if (!this.measurementQueue.isEmpty()) {
				measurement = (Measurement)this.measurementQueue.get(0);
				measurement_id = measurement.getId();
				if (this.transmit(measurement)) {
					this.measurementQueue.remove(measurement);
					try {
						measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_PROCESSING,
																	MeasurementControlModule.iAm.getUserId());
					}
					catch (Exception e) {
						Log.errorException(e);
					}
					this.processingMeasurements.put(measurement_id, measurement);
					Log.debugMessage("Transmitted measurement '" + measurement_id.toString() + "'", Log.DEBUGLEVEL03);
				}
				else
					Log.errorMessage("Cannot transmit measurement '" + measurement_id.toString() + "'");
			}//if (!this.measurementQueue.isEmpty())

			kisReport = this.receive();
			if (kisReport != null) {
				measurement_id = kisReport.getMeasurementId();
				Log.debugMessage("Received report for measurement '" + measurement_id.toString() + "'", Log.DEBUGLEVEL03);
				measurement = (Measurement)this.processingMeasurements.remove(measurement_id);
				if (measurement != null) {
					testProcessor = (TestProcessor)this.testProcessors.remove(measurement);
					if  (testProcessor != null) {
						result = null;
						try {
							kisReport.createResult(measurement);
							measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_MEASURED,
																		MeasurementControlModule.iAm.getUserId());
						}
						catch (Exception e) {
							Log.errorException(e);
						}
						if (result != null)
							testProcessor.addMeasurementResult(result);
					}//if  (testProcessor != null)
					else
						Log.errorMessage("Cannot find test processor for measurement '" + measurement_id.toString() + "'; throwing away it's report");
				}//if (measurement != null)
				else
					Log.errorMessage("Cannot find measurement for id '" + measurement_id.toString() + "'; throwing away it's report");
			}//if (kisReport != null)

			/*	We need not this as receive() already do it
			try {
				sleep(this.kis_tick_time);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}*/
		}//while

		this.measurementQueue.clear();
		this.testProcessors.clear();
		this.processingMeasurements.clear();
	}

	protected void addMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurement_id = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			this.measurementQueue.add(measurement);
			this.testProcessors.put(measurement, testProcessor);
		}
		else
			Log.errorMessage("Status of measurement '" + measurement_id.toString() + "' not SCHEDULED -- cannot add to queue");
	}

	protected void addProcessingMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurement_id = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_PROCESSING) {
			this.processingMeasurements.put(measurement_id, measurement);
			this.testProcessors.put(measurement, testProcessor);
		}
		else
			Log.errorMessage("Status of measurement '" + measurement_id.toString() + "' not PROCESSING -- cannot add to queue");
	}

	protected void abortMeasurements(TestProcessor testProcessor) {
		if (this.testProcessors.containsValue(testProcessor)) {
			Enumeration tp_enumeration = this.testProcessors.elements();
			Enumeration m_enumeration = this.testProcessors.keys();
			Measurement measurement;
			while (tp_enumeration.hasMoreElements()) {
				measurement = (Measurement)m_enumeration.nextElement();
				if (((TestProcessor)tp_enumeration.nextElement()).equals(testProcessor)) {
					this.measurementQueue.remove(measurement);
					this.testProcessors.remove(measurement);
					this.processingMeasurements.remove(measurement.getId());
					try {
						measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ABORTED,
																	MeasurementControlModule.iAm.getUserId());
					}
					catch (Exception e) {
						Log.errorException(e);
					}
				}
			}
		}
		else
			Log.errorMessage("Test processor doesn't contain in table");
	}

	protected void shutdown() {
		this.running = false;
	}

	private native boolean transmit(Measurement measurement);

	private native KISReport receive();
}