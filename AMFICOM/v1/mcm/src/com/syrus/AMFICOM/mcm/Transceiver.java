package com.syrus.AMFICOM.mcm;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

public class Transceiver extends SleepButWorkThread {
	/*	Error codes for method processFall()	*/
	public static final int FALL_CODE_ESTABLISH_CONNECTION = 1;
	public static final int FALL_CODE_TRANSMIT_MEASUREMENT = 2;
	public static final int FALL_CODE_RECEIVE_KIS_REPORT = 3;
	public static final int FALL_CODE_GENERATE_IDENTIFIER = 4;

	private KIS kis;
	private KISConnection kisConnection;
	private List scheduledMeasurements;
	private Map testProcessors;//Map <Identifier measurementId, TestProcessor testProcessor>

	private KISReport kisReport;
	private Measurement measurementToRemove;

	private boolean running;

	public Transceiver(KIS kis) {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

		this.kis = kis;
		try {
			this.kisConnection = MeasurementControlModule.kisConnectionManager.getConnection(kis);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		this.scheduledMeasurements = Collections.synchronizedList(new ArrayList());
		this.testProcessors = Collections.synchronizedMap(new HashMap());

		this.running = true;
	}

	public void addMeasurement(Measurement measurement, TestProcessor testProcessor) {
		Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			Log.debugMessage("Transceiver.addMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			this.scheduledMeasurements.add(measurement);
			this.testProcessors.put(measurementId, testProcessor);
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
		TestProcessor testProcessor;
		Result result;

		while (this.running) {

			if (this.kisConnection != null) {
				if (this.kisConnection.isEstablished()) {

					if (! this.scheduledMeasurements.isEmpty()) {
						measurement = (Measurement)this.scheduledMeasurements.get(0);
						measurementId = measurement.getId();
						try {
							this.kisConnection.transmitMeasurement(measurement);

							Log.debugMessage("Transceiver.run | Successfully transferred measurement '" + measurementId + "'", Log.DEBUGLEVEL03);
							this.scheduledMeasurements.remove(measurement);
							measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING, MeasurementControlModule.iAm.getUserId());
							MeasurementStorableObjectPool.putStorableObject(measurement);
							super.clearFalls();
						}
						catch (CommunicationException ce) {
							Log.errorException(ce);
							this.kisConnection.drop();
							super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
							this.measurementToRemove = measurement;
							super.sleepCauseOfFall();
						}
						catch (UpdateObjectException uoe) {
							Log.errorException(uoe);
						}
						catch (IllegalObjectEntityException ioee) {
							Log.errorException(ioee);
						}
					}// if (! this.scheduledMeasurements.isEmpty())

					if (this.kisReport == null) {
						try {
							this.kisReport = this.kisConnection.receiveKISReport(super.initialTimeToSleep);
						}
						catch (CommunicationException ce) {
							Log.errorException(ce);
							this.kisConnection.drop();
							super.fallCode = FALL_CODE_RECEIVE_KIS_REPORT;
							super.sleepCauseOfFall();
						}
					}// if (this.kisReport == null)
					else {
						measurementId = this.kisReport.getMeasurementId();
						Log.debugMessage("Transceiver.run | Received report for measurement '" + measurementId + "'", Log.DEBUGLEVEL03);
						measurement = null;
						try {
							measurement = (Measurement) MeasurementStorableObjectPool.getStorableObject(measurementId, true);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}
						if (measurement != null) {
							testProcessor = (TestProcessor) this.testProcessors.remove(measurementId);
							if (testProcessor != null) {
								result = null;

								try {
									result = this.kisReport.createResult();
									measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRED, MeasurementControlModule.iAm.getUserId());
									super.clearFalls();
								}
								catch (MeasurementException me) {
									if (me.getCode() == MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE) {
										Log.debugMessage("Transceiver.run | Cannot obtain identifier -- trying to wait", Log.DEBUGLEVEL05);
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
							}// if (testProcessor != null)
							else {
								Log.errorMessage("Transceiver.run | Cannot find test processor for measurement '" + measurementId + "'");
								this.throwAwayKISReport();
							}// else if (testProcessor != null)
						}// if (measurement != null)
						else {
							Log.errorMessage("Transceiver.run | Cannot find measurement for id '" + measurementId + "'");
							this.throwAwayKISReport();
						}// else if (measurement != null)
					}// else if (this.kisReport == null)

				}// if (this.kisConnection.isEstablished())
				else {
					long kisConnectionTimeout = ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_CONNECTION_TIMEOUT, MeasurementControlModule.KIS_CONNECTION_TIMEOUT) * 1000;
					try {
						this.kisConnection.establish(kisConnectionTimeout, true);
					}
					catch (CommunicationException ce) {
						Log.errorException(ce);
						super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
						super.sleepCauseOfFall();
					}
				}// else if (this.kisConnection.isEstablished())
			}// if (this.kisConnection != null)
			else {
				try {
					this.kisConnection = MeasurementControlModule.kisConnectionManager.getConnection(this.kis);
				}
				catch (CommunicationException ce) {
					Log.errorException(ce);
					super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
					super.sleepCauseOfFall();
				}
			}// else if (this.kisConnection != null)

		}// while
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_ESTABLISH_CONNECTION:
				Log.errorMessage("Transceiver.processFall | Many errors while establishing connection");
				break;
			case FALL_CODE_TRANSMIT_MEASUREMENT:
				this.removeMeasurement();
				break;
			case FALL_CODE_RECEIVE_KIS_REPORT:
				Log.errorMessage("Transceiver.processFall | Many errors while readig KIS report");
				break;
			case FALL_CODE_GENERATE_IDENTIFIER:
				Log.errorMessage("Transceiver.processFall | Cannot generate identifier");
				this.throwAwayKISReport();
				break;
		default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

	private void removeMeasurement() {
		if (this.measurementToRemove != null) {
			Log.debugMessage("Transceiver.throwAwayKISReport | removing measurement '" + this.measurementToRemove.getId() + "' from KIS '" + this.kis.getId() + "'", Log.DEBUGLEVEL05);
			this.scheduledMeasurements.remove(this.measurementToRemove);
			this.testProcessors.remove(this.measurementToRemove.getId());

			this.measurementToRemove = null;
		}
		else
			Log.errorMessage("Transceiver.removeMeasurement | Measurement to remove is null -- nothing to remove");
	}

	private void throwAwayKISReport() {
		if (this.kisReport != null) {
			Log.debugMessage("Transceiver.throwAwayKISReport | Throwing away report of measurement '" + this.kisReport.getMeasurementId() + "' from KIS '" + this.kis.getId() + "'", Log.DEBUGLEVEL05);
			this.kisReport = null;
		}
		else
			Log.errorMessage("Transceiver.throwAwayKISReport | KIS report is null -- nothing to throw away");
	}

	protected void shutdown() {
		this.scheduledMeasurements.clear();
		this.running = false;
		this.kisConnection.drop();
	}
}
