/*
 * $Id: Transceiver.java,v 1.68 2005/09/22 12:54:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.68 $, $Date: 2005/09/22 12:54:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class Transceiver extends SleepButWorkThread {
	/*	Error codes for method processFall()	*/
	private static final int FALL_CODE_ESTABLISH_CONNECTION = 1;
	private static final int FALL_CODE_TRANSMIT_MEASUREMENT = 2;
	private static final int FALL_CODE_RECEIVE_KIS_REPORT = 3;
	private static final int FALL_CODE_CREATE_RESULT = 4;

	private KIS kis;
	private KISConnection kisConnection;
	private List<Measurement> scheduledMeasurements;
	private Map<Identifier, TestProcessor> testProcessors;

	private KISReport kisReport;
	private Measurement measurementToRemove;

	private boolean running;

	public Transceiver(final KIS kis) {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));
		super.setName("Transceiver " + kis.getId());

		this.kis = kis;

		this.scheduledMeasurements = Collections.synchronizedList(new ArrayList<Measurement>());
		this.testProcessors = Collections.synchronizedMap(new HashMap<Identifier, TestProcessor>());

		try {
			this.kisConnection = MeasurementControlModule.kisConnectionManager.getConnection(this.kis);
		} catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		this.running = true;
	}

	void addMeasurement(final Measurement measurement, final TestProcessor testProcessor) {
		final Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED) {
			Log.debugMessage("Transceiver.addMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			this.scheduledMeasurements.add(measurement);
			this.testProcessors.put(measurementId, testProcessor);

			this.interrupt();
		} else {
			Log.errorMessage("Transceiver.transmitMeasurementToKIS | ERROR: Status: " + measurement.getStatus().value()
					+ " of measurement '" + measurementId + "' not SCHEDULED -- cannot add to queue");
		}
	}

	void addAcquiringMeasurement(final Measurement measurement, final TestProcessor testProcessor) {
		final Identifier measurementId = measurement.getId();
		if (measurement.getStatus().value() == MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING) {
			Log.debugMessage("Transceiver.addAcquiringMeasurement | Adding measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
			this.testProcessors.put(measurementId, testProcessor);

			this.interrupt();
		} else {
			Log.errorMessage("Transceiver.addAcquiringMeasurement | ERROR: Status: " + measurement.getStatus().value()
					+ " of measurement '" + measurementId + "' not ACQUIRING -- cannot add to queue");
		}
	}

	void removeMeasurementsOfTestProcessor(final TestProcessor testProcessor) {
		synchronized (this.testProcessors) {
			for (final Iterator<Identifier> it = this.testProcessors.keySet().iterator(); it.hasNext();) {
				final Identifier measurementId = it.next();
				final TestProcessor tProcessor = this.testProcessors.get(measurementId);
				if (tProcessor.getTestId().equals(testProcessor.getTestId())) {
					try {
						final Measurement measurement = StorableObjectPool.getStorableObject(measurementId, true);
						this.scheduledMeasurements.remove(measurement);
					} catch (ApplicationException ae) {
						Log.errorException(ae);
					}
					it.remove();
				}
			}
		}
	}

	@Override
	public void run() {
		while (this.running) {

			if (this.testProcessors.isEmpty()) {
				try {
					sleep(10000);
					continue;
				} catch (InterruptedException ie) {
					Log.debugMessage(this.getName() + " -- interrupted", Log.DEBUGLEVEL07);
				}
			}

			if (this.kisConnection != null) {
				if (this.kisConnection.isEstablished()) {

					if (!this.scheduledMeasurements.isEmpty()) {
						final Measurement measurement = this.scheduledMeasurements.get(0);
						final Identifier measurementId = measurement.getId();
						try {
							this.kisConnection.transmitMeasurement(measurement, super.initialTimeToSleep);

							Log.debugMessage("Transceiver.run | Successfully transferred measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
							this.scheduledMeasurements.remove(measurement);
							measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRING);
							StorableObjectPool.flush(measurementId, LoginManager.getUserId(), false);
							super.clearFalls();
						} catch (CommunicationException ce) {
							Log.errorException(ce);
							this.kisConnection.drop();
							super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
							this.measurementToRemove = measurement;
							super.sleepCauseOfFall();
						} catch (ApplicationException ae) {
							Log.errorException(ae);
						}
					}// if (! this.scheduledMeasurements.isEmpty())

					if (this.kisReport == null) {
						try {
							this.kisReport = this.kisConnection.receiveKISReport(super.initialTimeToSleep);
							super.clearFalls();
						} catch (CommunicationException ce) {
							Log.errorException(ce);
							this.kisConnection.drop();
							super.fallCode = FALL_CODE_RECEIVE_KIS_REPORT;
							super.sleepCauseOfFall();
						}
					} else {// if (this.kisReport == null)
						final Identifier measurementId = this.kisReport.getMeasurementId();
						Log.debugMessage("Transceiver.run | Received report for measurement '" + measurementId + "'", Log.DEBUGLEVEL07);
						Measurement measurement = null;
						try {
							measurement = (Measurement) StorableObjectPool.getStorableObject(measurementId, true);
						} catch (ApplicationException ae) {
							Log.errorException(ae);
						}
						if (measurement != null) {

							if (measurement.getStatus() != MeasurementStatus.MEASUREMENT_STATUS_ACQUIRED) {
								measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ACQUIRED);
								try {
									StorableObjectPool.flush(measurementId, LoginManager.getUserId(), false);
								} catch (ApplicationException ae) {
									Log.errorException(ae);
								}
							}

							Result result = null;
							try {
								result = this.kisReport.createResult();
							} catch (MeasurementException me) {
								final int code = me.getCode();
								switch (code) {
									case MeasurementException.IDENTIFIER_GENERATION_FAILED_CODE:
									case MeasurementException.COMMUNICATION_FAILED_CODE:
										Log.debugMessage("Transceiver.run | Cannot create result -- trying to wait", Log.DEBUGLEVEL07);
										try {
											MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
										} catch (CommunicationException ce) {
											Log.errorException(ce);
										}
									default:
										super.fallCode = FALL_CODE_CREATE_RESULT;
										super.sleepCauseOfFall();
								}
							}

							TestProcessor testProcessor = this.testProcessors.remove(measurementId);
							if (testProcessor == null) {
								Log.errorMessage("Transceiver.run | WARNING: Cannot find test processor for measurement '" + measurementId
										+ "'; searching in global MCM map");
								testProcessor = MeasurementControlModule.testProcessors.get(measurement.getTestId());
							}
							if (testProcessor != null) {
								if (result != null) {
									testProcessor.addMeasurementResult(result);
									this.kisReport = null;
								}
							} else {// if (testProcessor != null)
								Log.errorMessage("Transceiver.run | ERROR: Test processor for measurement '" + measurementId + "' + not found");
								this.throwAwayKISReport();
							}// else if (testProcessor != null)

						} else {// if (measurement != null)
							Log.errorMessage("Transceiver.run | ERROR: Measurement for id '" + measurementId + "' + not found");
							this.throwAwayKISReport();
						}// else if (measurement != null)
					}// else if (this.kisReport == null)

				} else {// if (this.kisConnection.isEstablished())
					final long kisConnectionTimeout = ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_CONNECTION_TIMEOUT, MeasurementControlModule.KIS_CONNECTION_TIMEOUT) * 1000;
					try {
						this.kisConnection.establish(kisConnectionTimeout, true);
						super.clearFalls();
					} catch (CommunicationException ce) {
						Log.errorException(ce);
						super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
						super.sleepCauseOfFall();
					}
				}// else if (this.kisConnection.isEstablished())
			} else {// if (this.kisConnection != null)
				try {
					this.kisConnection = MeasurementControlModule.kisConnectionManager.getConnection(this.kis);
					super.clearFalls();
				} catch (CommunicationException ce) {
					Log.errorException(ce);
					super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
					super.sleepCauseOfFall();
				}
			}// else if (this.kisConnection != null)

		}// while
	}

	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_ESTABLISH_CONNECTION:
				Log.errorMessage("Transceiver.processFall | ERROR: Many errors while establishing connection");
				break;
			case FALL_CODE_TRANSMIT_MEASUREMENT:
				this.removeMeasurement();
				break;
			case FALL_CODE_RECEIVE_KIS_REPORT:
				Log.errorMessage("Transceiver.processFall | ERROR: Many errors while readig KIS report");
				break;
			case FALL_CODE_CREATE_RESULT:
				Log.errorMessage("Transceiver.processFall | ERROR: Cannot create result");
				this.throwAwayKISReport();
				break;
		default:
				Log.errorMessage("processError | ERROR: Unknown error code: " + super.fallCode);
		}
	}

	private void removeMeasurement() {
		if (this.measurementToRemove != null) {
			Log.debugMessage("Transceiver.throwAwayKISReport | removing measurement '" + this.measurementToRemove.getId() + "' from KIS '" + this.kis.getId() + "'", Log.DEBUGLEVEL07);
			this.scheduledMeasurements.remove(this.measurementToRemove);
			this.testProcessors.remove(this.measurementToRemove.getId());

			this.measurementToRemove.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ABORTED);
			try {
				StorableObjectPool.flush(this.measurementToRemove, LoginManager.getUserId(), false);
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			this.measurementToRemove = null;
		} else {
			Log.errorMessage("Transceiver.removeMeasurement | ERROR: Measurement to remove is null -- nothing to remove");
		}
	}

	private void throwAwayKISReport() {
		if (this.kisReport != null) {
			Log.debugMessage("Transceiver.throwAwayKISReport | Throwing away report of measurement '"
					+ this.kisReport.getMeasurementId() + "' from KIS '" + this.kis.getId() + "'", Log.DEBUGLEVEL07);
			this.kisReport = null;
		} else {
			Log.errorMessage("Transceiver.throwAwayKISReport | ERROR: KIS report is null -- nothing to throw away");
		}
	}

	void shutdown() {
		this.scheduledMeasurements.clear();
		this.running = false;
		this.kisConnection.drop();
	}
}
