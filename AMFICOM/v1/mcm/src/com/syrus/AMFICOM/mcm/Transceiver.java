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
	private static final int TCP_SOCKET_INVALID = -1;

/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_GENERATE_IDENTIFIER = 1;
	public static final int FALL_CODE_RECEIVE_KIS_REPORT = 2;

	private short tcpPort;
	private int tcpSocket;
	private Map testProcessors;//Map <Identifier measurementId, TestProcessor testProcessor>
	private KISReport kisReport;

	private boolean running;

	static {
		System.loadLibrary("mcmtransceiver");
	}

	public Transceiver() throws CommunicationException {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

		this.tcpPort = MeasurementControlModule.iAm.getTCPPort();
		if (this.tcpPort <= 0)
			this.tcpPort = (short)ApplicationProperties.getInt(MeasurementControlModule.KEY_TCP_PORT, MeasurementControlModule.TCP_PORT);
		this.setupReceiverInterface();

		this.testProcessors = Collections.synchronizedMap(new Hashtable());
		this.kisReport = null;

		this.running = true;
	}

	public void transmitMeasurementToKIS(Measurement measurement, KIS kis, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			Log.debugMessage("Transceiver.transmitMeasurementToKIS | measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			(new MeasurementTransmitter(measurement, kis, testProcessor)).start();
		}
		else
			Log.errorMessage("Transceiver.transmitMeasurementToKIS | Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not SCHEDULED -- cannot add to queue");
	}

	protected void considerAcquiringMeasurement(Measurement measurement, TestProcessor testProcessor) {
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
			if (this.kisReport == null) {
				if (this.receiverInterfaceIsUp()) {
					if (! this.receive()) {
						this.downReceiverInterface();
						super.clearFalls();
						try {
							sleep(super.initialTimeToSleep);
						}
						catch (InterruptedException ie) {
							Log.errorException(ie);
						}
					}
				}
				else {
					Log.debugMessage("Transceiver.run | Receiver interface is down - trying set it up", Log.DEBUGLEVEL07);
					try {
						this.setupReceiverInterface();
					}
					catch (CommunicationException ce) {
						Log.errorException(ce);
						super.fallCode = FALL_CODE_RECEIVE_KIS_REPORT;
						super.sleepCauseOfFall();
					}
				}
			}
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
			case FALL_CODE_RECEIVE_KIS_REPORT:
				Log.errorMessage("Cannot setup receiver interface");
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

	private void setupReceiverInterface() throws CommunicationException {
		this.tcpSocket = this.setupTCPInterface();
		if (this.receiverInterfaceIsUp())
			return;
		else
			throw new CommunicationException("Cannot setup network interface");
	}

	private void downReceiverInterface() {
		if (this.receiverInterfaceIsUp()) {
			this.downTCPInterface();
			this.tcpSocket = TCP_SOCKET_INVALID;
		}
	}

	private boolean receiverInterfaceIsUp() {
		return (this.tcpSocket != TCP_SOCKET_INVALID);
	}

	protected void shutdown() {
		this.running = false;
		this.testProcessors = null;
		this.throwAwayKISReport();
		this.downReceiverInterface();
	}

	/**
	 * Creates listening socket on local machine.
	 * @return socket file descriptor.
	 */
	private native int setupTCPInterface();

	/**
	 * Closes TCP socket
	 */
	private native void downTCPInterface();

	/**
	 * Receives result of measurement from remote KIS.
	 * Writes received report to field kisReport
	 * @return true on success, false on failure.
	 */
	private native boolean receive();


	private class MeasurementTransmitter extends SleepButWorkThread {
		/*	Error codes for method processFall()	*/
		public static final int FALL_CODE_TRANSMIT_MEASUREMENT = 1;

		private Measurement measurement;
		private KIS kis;
		private TestProcessor testProcessor;
		private KISConnection kisConnection;

		private boolean moreTransmissionAttempts;

		MeasurementTransmitter(Measurement measurement, KIS kis, TestProcessor testProcessor) {
			super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
					ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

			this.measurement = measurement;
			this.kis = kis;
			this.testProcessor = testProcessor;
			this.kisConnection = null;

			this.moreTransmissionAttempts = true;
		}

		public void run() {
			try {
				this.kisConnection = MeasurementControlModule.kisConnectionManager.getConnection(this.kis);
			}
			catch (CommunicationException ce) {
				Log.errorException(ce);
				this.abortMeasurement();
				return;
			}

			Identifier measurementId = this.measurement.getId();
			while (this.moreTransmissionAttempts) {
				try {
					this.kisConnection.transmitMeasurement(this.measurement);

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
					MeasurementControlModule.kisConnectionManager.dropConnection(this.kis.getId());
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
