package com.syrus.AMFICOM.mcm;

import java.util.Collections;
import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

public class Transceiver extends SleepButWorkThread {
/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_GENERATE_IDENTIFIER = 1;

	private Map testProcessors;//Map <Identifier measurementId, TestProcessor testProcessor>
	private KISReport kisReport;

	private boolean running;

	public Transceiver() throws CommunicationException {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

		this.setupReceiverInterface();

		this.testProcessors = Collections.synchronizedMap(new Hashtable());
		this.kisReport = null;

		this.running = true;
	}

	private void setupReceiverInterface() throws CommunicationException {
		short tcpPort = MeasurementControlModule.iAm.getTCPPort();
		if (tcpPort <= 0)
			tcpPort = (short)ApplicationProperties.getInt(MeasurementControlModule.KEY_TCP_PORT, MeasurementControlModule.TCP_PORT);

		if (this.setupTCPInterface(tcpPort))
			return;
		else
			throw new CommunicationException("Cannot setup network interface");
	}

	private native boolean setupTCPInterface(short tcpPort);

	private native KISReport receive();

	public void transmitMeasurementToKIS(Measurement measurement, KIS kis, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			Log.debugMessage("Transceiver.transmitMeasurementToKIS | measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			(new MeasurementTransmitter(measurement, kis, testProcessor)).start();
		}
		else
			Log.errorMessage("Transceiver.transmitMeasurementToKIS | Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not SCHEDULED -- cannot add to queue");
	}

	protected void addAcquiringMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING) {
			Log.debugMessage("Transceiver.addAcquiringMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			this.testProcessors.put(measurementId, testProcessor);
			try {
				MeasurementStorableObjectPool.putStorableObject(measurement);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}
		}
		else
			Log.errorMessage("Transceiver.addAcquiringMeasurement | Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not ACQUIRING -- cannot add to queue");
	}

	public void run() {
		Measurement measurement;
		Identifier measurementId;
		TestProcessor testProcessor = null;
		Result result;

		while (this.running) {
			if (this.kisReport == null)
				this.kisReport = this.receive();
			else {
				measurementId = this.kisReport.getMeasurementId();
				Log.debugMessage("Transceiver.run | Received report for measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
				measurement = null;
				try {
					measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(measurementId, true);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				if (measurement != null) {
					testProcessor = (TestProcessor)this.testProcessors.remove(measurementId);
					if (testProcessor != null) {
						result = null;

						try {
							result = this.kisReport.createResult();
							measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRED, MeasurementControlModule.iAm.getUserId());
							super.clearFalls();
						}
						catch (MeasurementException me) {
							if (me.getCause() instanceof AMFICOMRemoteException) {
								Log.debugMessage("Transceiver.run | Cannot get identifier - trying to wait", Log.DEBUGLEVEL07);
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
					else
						Log.errorMessage("Transceiver.run | Cannot find test processor for measurement '" + measurementId + "'");
				}	//if (measurement != null)
				else
					Log.errorMessage("Transceiver.run | Cannot find measurement for id '" + measurementId + "'");
			}	//else if (this.kisReport == null)

//	Not need as receive already do it
//			try {
//				sleep(super.initialTimeToSleep);
//			}
//			catch (InterruptedException ie) {
//				Log.errorException(ie);
//			}

		}	//while
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_GENERATE_IDENTIFIER:
				this.throwAwayKISReport();
				break;
		default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

	private void throwAwayKISReport() {
		if (this.kisReport != null) {
			Log.debugMessage("Transceiver.throwAwayKISReport | Throwing away kis report for measurement '" + this.kisReport.getMeasurementId() + "'", Log.DEBUGLEVEL07);
			this.kisReport = null;
		}
	}


	private class MeasurementTransmitter extends SleepButWorkThread {
		/*	Error codes for method processFall()	*/
		public static final int FALL_CODE_TRANSMIT_MEASUREMENT = 1;

		private Measurement measurement;
		private KIS kis;
		private TestProcessor testProcessor;

		private boolean moreTransmissionAttempts;

		MeasurementTransmitter(Measurement measurement, KIS kis, TestProcessor testProcessor) {
			super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
					ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

			this.measurement = measurement;
			this.kis = kis;
			this.testProcessor = testProcessor;

			this.moreTransmissionAttempts = true;
		}

		public void run() {
			KISConnection kisConnection = null;
			try {
				kisConnection = MeasurementControlModule.kisConnectionManager.getConnection(kis);
			}
			catch (CommunicationException ce) {
				Log.errorException(ce);
				this.abortMeasurement();
				return;
			}

			Identifier measurementId = this.measurement.getId();
			while (this.moreTransmissionAttempts) {
				try {
					kisConnection.transmitMeasurement(this.measurement);

					this.moreTransmissionAttempts = false;
					this.measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING, MeasurementControlModule.iAm.getUserId());
					MeasurementStorableObjectPool.putStorableObject(this.measurement);
					Transceiver.this.testProcessors.put(measurementId, this.testProcessor);
					Log.debugMessage("TransmissionManager.MeasurementTransmitter.run | Transmitted measurement '" + measurementId + "' to KIS '" + this.kis.getId() + "'", Log.DEBUGLEVEL07);
					super.clearFalls();
				}
				catch (CommunicationException ce) {
					Log.errorException(ce);
					super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
					super.sleepCauseOfFall();
				}
				catch (UpdateObjectException uoe) {
					Log.errorException(uoe);
				}
				catch (IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
				}
			}
		}
		
		protected void processFall() {
			switch (super.fallCode) {
				case FALL_CODE_NO_ERROR:
					break;
				case FALL_CODE_TRANSMIT_MEASUREMENT:
					this.abortMeasurement();
					this.moreTransmissionAttempts = false;
					break;
				default:
					Log.errorMessage("processError | Unknown error code: " + super.fallCode);
			}
		}

		private void abortMeasurement() {
			Log.debugMessage("Aborting measurement '" + this.measurement.getId() + "'", Log.DEBUGLEVEL07);
			try {
				this.measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ABORTED, MeasurementControlModule.iAm.getUserId());
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}
		}
	}

}
