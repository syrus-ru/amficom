package com.syrus.AMFICOM.mcm;

import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

public class TransmissionManager extends SleepButWorkThread {
	public static final String KEY_TCP_PORT = "TCPPort";
	public static final String KEY_KIS_HOST_NAME = "KISHostName";
	public static final String KEY_KIS_TCP_PORT = "KISTCPPort";

	public static final short TCP_PORT = 7500;
	public static final String KIS_HOST_NAME = "127.0.0.1";
	public static final short KIS_TCP_PORT = 7501;
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_GENERATE_IDENTIFIER = 1;

	private Map testProcessors;//Map <Identifier measurementId, TestProcessor testProcessor>
//	private Map kisSockets;//Map <Identifier kisId, Integer socket>
	private KISReport kisReport;

	private boolean running;

	public TransmissionManager() throws CommunicationException {
		super(ApplicationProperties.getInt(KEY_KIS_TICK_TIME, KIS_TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_KIS_MAX_FALLS, KIS_MAX_FALLS));

		short tcpPort = MeasurementControlModule.iAm.getTCPPort();
		if (tcpPort <= 0)
			tcpPort = (short)ApplicationProperties.getInt(KEY_TCP_PORT, TCP_PORT);
		if (this.setupTCPInterface(tcpPort)) {
			this.testProcessors = Collections.synchronizedMap(new Hashtable());
			this.kisReport = null;
			this.running = true;
		}
		else 
			throw new CommunicationException("Cannot setup network interface");
	}


	private native int getKISTCPSocket(String kisHostName, short kisTCPPort);

	private native boolean transmitMeasurement(String measurementId,
																						 String measurementTypeCodename,
																						 String localAddress,
																						 String[] parameterTypeCodenames,
																						 byte[][] parameterValues,
																						 int kisTCPSocket);

	private native boolean setupTCPInterface(short tcpPort);

	private native KISReport receive();


	private class MeasurementTransmitter extends SleepButWorkThread {
		/*	Error codes for method processFall()	*/
		public static final int FALL_CODE_TRANSMIT_MEASUREMENT = 1;

		private Measurement measurement;
		private Identifier kisId;
		private int kisTCPSocket;
		private TestProcessor testProcessor;
		private boolean running;

		MeasurementTransmitter(Measurement measurement, Identifier kisId, int kisTCPSocket, TestProcessor testProcessor) {
			super(ApplicationProperties.getInt(KEY_KIS_TICK_TIME, KIS_TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_KIS_MAX_FALLS, KIS_MAX_FALLS));

			this.measurement = measurement;
			this.kisId = kisId;
			this.kisTCPSocket = kisTCPSocket;
			this.testProcessor = testProcessor;

			this.running = true;
		}

		public void run() {
			Identifier measurementId  = this.measurement.getId();

			while (this.running) {
				if (TransmissionManager.this.transmitMeasurement(measurementId.toString(),
																				this.measurement.getType().getCodename(),
																				this.measurement.getLocalAddress(),
																				this.measurement.getSetup().getParameterTypeCodenames(),
																				this.measurement.getSetup().getParameterValues(),
																				this.kisTCPSocket)) {
					try {
						this.measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING, MeasurementControlModule.iAm.getUserId());
						MeasurementStorableObjectPool.putStorableObject(this.measurement);
					}
					catch (UpdateObjectException uoe) {
						Log.errorException(uoe);
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
					TransmissionManager.this.testProcessors.put(measurementId, this.testProcessor);
					Log.debugMessage("TransmissionManager.MeasurementTransmitter.run | Transmitted measurement '" + measurementId + "' to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
					this.shutdown();
				}
				else {
					Log.errorMessage("TransmissionManager.MeasurementTransmitter.run | Cannot transmit measurement '" + measurementId + "' to KIS '" + this.kisId + "'");
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
					MeasurementControlModule.kisConnectionManager.dropConnection(this.kisId);
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

	}


	protected synchronized void addMeasurement(Measurement measurement, KIS kis, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			Log.debugMessage("TransmissionManager.addMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			try {
				int kisTCPSocket = MeasurementControlModule.kisConnectionManager.getConnectedKISTCPSocket(kis);
				(new MeasurementTransmitter(measurement, kisTCPSocket, testProcessor)).start();
			}
			catch (CommunicationException ce) {
				Log.errorException(ce);
				
			}
		}
		else
			Log.errorMessage("TransmissionManager.addMeasurement | Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not SCHEDULED -- cannot add to queue");
	}

	protected void addAcquiringMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING) {
			Log.debugMessage("TransmissionManager.addAcquiringMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			this.testProcessors.put(measurementId, testProcessor);
			try {
				MeasurementStorableObjectPool.putStorableObject(measurement);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}
		}
		else
			Log.errorMessage("TransmissionManager.addAcquiringMeasurement | Status: " + measurement.getStatus().value() + " of measurement '" + measurementId + "' not ACQUIRING -- cannot add to queue");
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
				Log.debugMessage("Received report for measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
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
				}	//if (measurement != null)
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
			Log.debugMessage("Throwing away kis report for measurement '" + this.kisReport.getMeasurementId() + "'", Log.DEBUGLEVEL07);
			this.kisReport = null;
		}
	}

//
//	private class KISConnector extends SleepButWorkThread {
//		/*	Error codes for method processFall()	*/
//		public static final int FALL_CODE_ESTABLISH_CONNECTION = 1;
//
//		private String kisHostName;
//		private short kisTCPPort;
//		private boolean running;
//
//		KISConnector(String kisHostName, short kisTCPPort) {
//			super(ApplicationProperties.getInt(KEY_KIS_TICK_TIME, KIS_TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_KIS_MAX_FALLS, KIS_MAX_FALLS));
//
//			this.kisHostName = kisHostName;
//			this.kisTCPPort = kisTCPPort;
//			this.running = true;
//		}
//
//		public void run() {
//			Log.debugMessage("TransmissionManager.KISConnector.run | Connecting to KIS on host: '" + this.kisHostName + "' to port " + this.kisTCPPort, Log.DEBUGLEVEL07);
//			int kisSocket = -1;
//			while (this.running) {
//				kisSocket = TransmissionManager.this.getKISTCPSocket(this.kisHostName, this.kisTCPPort);
//				if (kisSocket > 0) {
//					
//				}
//			}
//		}
//
//		protected void processFall() {
//			switch (super.fallCode) {
//				case FALL_CODE_NO_ERROR:
//					break;
//				case FALL_CODE_ESTABLISH_CONNECTION:
//					
//					break;
//				default:
//					Log.errorMessage("processFall | Unknown error code: " + super.fallCode);
//			}
//		}
//
//		private void shutdown() {
//			this.running = false;
//		}
//
//	}
//
//	private class Transmitter extends SleepButWorkThread {
//		/*	Error codes for method processFall()	*/
//		public static final int FALL_CODE_TRANSMIT_MEASUREMENT = 1;
//
//		private Measurement measurement;
//		private KIS kis;
//		private TestProcessor testProcessor;
//		private boolean running;
//
//		Transmitter(Measurement measurement, KIS kis, TestProcessor testProcessor) {
//			super(ApplicationProperties.getInt(KEY_KIS_TICK_TIME, KIS_TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_KIS_MAX_FALLS, KIS_MAX_FALLS));
//
//			this.measurement = measurement;
//			this.kis = kis;
//			this.testProcessor = testProcessor;
//			this.running = true;
//		}
//
//		public void run() {
//			Identifier measurementId = this.measurement.getId();
//			Log.debugMessage("TransmissionManager.Transmitter.run | Transmitting measurement: '" + measurementId + "' to KIS '" + this.kis.getId() + "'", Log.DEBUGLEVEL07);
//			while (this.running) {
//				short kisTCPPort = kis.getTCPPort();
//				if (kisTCPPort <= 0)
//					kisTCPPort = (short)ApplicationProperties.getInt(KEY_KIS_TCP_PORT, KIS_TCP_PORT);
//				if (TransmissionManager.this.transmitMeasurement(measurementId.toString(),
//																												 measurement.getType().getCodename(),
//																												 measurement.getLocalAddress(),
//																												 measurement.getSetup().getParameterTypeCodenames(),
//																												 measurement.getSetup().getParameterValues(),
//																												 kis.getHostName(),
//																												 kisTCPPort)) {
//					try {
//						measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING,
//																		 MeasurementControlModule.iAm.getUserId());
//						MeasurementStorableObjectPool.putStorableObject(measurement);
//					}
//					catch (UpdateObjectException uoe) {
//						Log.errorException(uoe);
//					}
//					catch (IllegalObjectEntityException ioee) {
//						Log.errorException(ioee);
//					}
//					TransmissionManager.this.testProcessors.put(measurementId, this.testProcessor);
//					Log.debugMessage("TransmissionManager.Transmitter.run | Transmitted measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
//					this.shutdown();
//				}
//				else {
//					Log.errorMessage("TransmissionManager.Transmitter.run | Cannot transmit measurement '" + measurementId + "'");
//					super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
//					super.sleepCauseOfFall();
//				}
//			}
//		}
//
//		protected void processFall() {
//			switch (super.fallCode) {
//				case FALL_CODE_NO_ERROR:
//					break;
//				case FALL_CODE_TRANSMIT_MEASUREMENT:
//					this.abortMeasurement();
//					break;
//				default:
//					Log.errorMessage("processFall | Unknown error code: " + super.fallCode);
//			}
//		}
//
//		private void abortMeasurement() {
//			try {
//				this.measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ABORTED,
//																			MeasurementControlModule.iAm.getUserId());
//			}
//			catch (UpdateObjectException uoe) {
//				Log.errorException(uoe);
//			}
//			this.shutdown();
//		}
//
//		private void shutdown() {
//			this.running = false;
//		}
//	}

}
