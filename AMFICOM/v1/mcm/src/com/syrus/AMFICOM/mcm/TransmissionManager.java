package com.syrus.AMFICOM.mcm;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

public class TransmissionManager extends SleepButWorkThread {
	public static final String KEY_KIS_TICK_TIME = "KISTickTime";
	public static final String KEY_KIS_MAX_FALLS = "KISMaxFalls";
	public static final String KEY_KIS_TCP_PORT = "KISTCPPort";

	public static final int KIS_TICK_TIME = 1;
	public static final int KIS_MAX_FALLS = 10;
	public static final short KIS_TCP_PORT = 7500;
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_GENERATE_IDENTIFIER = 2;

	private boolean running;
//	private List measurementQueue;//List <Measurement measurement>
	private Map testProcessors;//Map <Identifier measurementId, TestProcessor testProcessor>

	public TransmissionManager() {
		super(ApplicationProperties.getInt(KEY_KIS_TICK_TIME, KIS_TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_KIS_MAX_FALLS, KIS_MAX_FALLS));

		this.testProcessors = Collections.synchronizedMap(new Hashtable());
		this.running = true;
	}

	protected synchronized void addMeasurement(Measurement measurement, KIS kis, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			Log.debugMessage("TransmissionManager.addMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			(new Transmitter(measurement, kis, testProcessor)).start();
		}
		else
			Log.errorMessage("TransmissionManager.addMeasurement | Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not SCHEDULED -- cannot add to queue");
	}
//
//	protected void addAcquiringMeasurement(Measurement measurement, TestProcessor testProcessor) {
//		Identifier measurementId = measurement.getId();
//		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING) {
//			Log.debugMessage("TransmissionManager.addAcquiringMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
//			this.testProcessors.put(measurementId, testProcessor);
//			try {
//				MeasurementStorableObjectPool.putStorableObject(measurement);
//			}
//			catch (IllegalObjectEntityException ioee) {
//				Log.errorException(ioee);
//			}
//		}
//		else
//			Log.errorMessage("TransmissionManager.addAcquiringMeasurement | Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not ACQUIRING -- cannot add to queue");
//	}

	public void run() {
		Measurement measurement;
		Identifier measurementId;

		while (this.running) {
//			if (! this.measurementQueue.isEmpty()) {
//				measurement = (Measurement)this.measurementQueue.get(0);
//				measurementId = measurement.getId();
//				if (this.transmit(measurement)) {
//					
//				}
//			}	//if (! this.measurementQueue.isEmpty())
		}	//while
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
		default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}


	private class Transmitter extends SleepButWorkThread {
		/*	Error codes for method processFall()	*/
		public static final int FALL_CODE_TRANSMIT_MEASUREMENT = 1;

		private Measurement measurement;
		private KIS kis;
		private TestProcessor testProcessor;
		private boolean running;

		Transmitter(Measurement measurement, KIS kis, TestProcessor testProcessor) {
			super(ApplicationProperties.getInt(KEY_KIS_TICK_TIME, KIS_TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_KIS_MAX_FALLS, KIS_MAX_FALLS));

			this.measurement = measurement;
			this.kis = kis;
			this.testProcessor = testProcessor;
			this.running = true;
		}

		public void run() {
			Identifier measurementId = this.measurement.getId();
			while (this.running) {
				Log.debugMessage("TransmissionManager.Transmitter.run | Transmitting measurement: '" + measurementId + "' to KIS '" + this.kis.getId() + "'", Log.DEBUGLEVEL07);
				short kisTCPPort = kis.getPort();
				if (kisTCPPort <= 0)
					kisTCPPort = (short)ApplicationProperties.getInt(KEY_KIS_TCP_PORT, KIS_TCP_PORT);
				if (this.transmitMeasurement(measurementId.toString(),
																		 measurement.getType().getCodename(),
																		 measurement.getLocalAddress(),
																		 measurement.getSetup().getParameterTypeCodenames(),
																		 measurement.getSetup().getParameterValues(),
																		 kis.getHostName(),
																		 kisTCPPort)) {
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
					TransmissionManager.this.testProcessors.put(measurementId, this.testProcessor);
					Log.debugMessage("TransmissionManager.Transmitter.run | Transmitted measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
					this.shutdown();
				}
				else {
					Log.errorMessage("TransmissionManager.Transmitter.run | Cannot transmit measurement '" + measurementId + "'");
					super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
					super.sleepCauseOfFall();
				}
			}
		}

		protected void processFall() {
			switch (super.fallCode) {
				case FALL_CODE_NO_ERROR:
					break;
				case FALL_CODE_TRANSMIT_MEASUREMENT:
					this.abortMeasurement();
					break;
				default:
					Log.errorMessage("processFall | Unknown error code: " + super.fallCode);
			}
		}

		private void abortMeasurement() {
			try {
				this.measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ABORTED,
																			MeasurementControlModule.iAm.getUserId());
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}
			this.shutdown();
		}

		private void shutdown() {
			this.running = false;
		}

		private native boolean transmitMeasurement(String measurementId,
																							 String measurementTypeCodename,
																							 String localAddress,
																							 String[] parameterTypeCodenames,
																							 byte[][] parameterValues,
																							 String hostname,
																							 short port);
	}

}
