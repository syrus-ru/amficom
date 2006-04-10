/*
 * $Id: Transceiver.java,v 1.80.2.6 2006/04/10 17:06:34 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_KIS_CONNECTION_TIMEOUT;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_KIS_MAX_FALLS;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_KIS_TICK_TIME;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KIS_CONNECTION_TIMEOUT;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KIS_MAX_FALLS;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KIS_TICK_TIME;
import static com.syrus.AMFICOM.measurement.Action.ActionStatus.ACTION_STATUS_ABORTED;
import static com.syrus.AMFICOM.measurement.Action.ActionStatus.ACTION_STATUS_COMPLETED;
import static com.syrus.AMFICOM.measurement.Action.ActionStatus.ACTION_STATUS_NEW;
import static com.syrus.AMFICOM.measurement.Action.ActionStatus.ACTION_STATUS_RUNNING;
import static com.syrus.util.Log.DEBUGLEVEL07;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;
import com.syrus.AMFICOM.measurement.Action.ActionStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.80.2.6 $, $Date: 2006/04/10 17:06:34 $
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

	/**
	 * Идентификатор КИС, который обслуживает данный приёмопередатчик.
	 */
	private final Identifier kisId;

	/**
	 * Соединение с КИС.
	 */
	private final KISConnection kisConnection;

	/**
	 * Список новых измерений. Состояние каждого из них должно быть НОВЫЙ.
	 */
	private final List<Measurement> newMeasurements;

	/**
	 * Набор идентификаторов выполняемых измерений. Состояние каждого из них
	 * должно быть ВЫПОЛНЯЕТСЯ.
	 */
	private final Set<Identifier> runningMeasurementIds;

	/**
	 * Ответ КИС, который приходит с КИС по завершении измерения.
	 */
	private KISReport kisReport;

	/**
	 * Измерение, которое не удалось начать. Его необходимо перевести в
	 * состояние ПРЕРВАН. См. {@link #removeMeasurement()} и
	 * {@link #abortMeasurementAndReport()}
	 */
	private Measurement measurementToRemove;

	/**
	 * Знак работы главного цикла.
	 */
	private boolean running;

	public Transceiver(final Identifier kisId) throws KISException {
		super(ApplicationProperties.getInt(KEY_KIS_TICK_TIME, KIS_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_KIS_MAX_FALLS, KIS_MAX_FALLS));
		super.setName("Transceiver " + kisId);

		assert kisId != null : NON_NULL_EXPECTED;
		assert kisId.getMajor() == KIS_CODE : ILLEGAL_ENTITY_CODE;
		this.kisId = kisId;

		this.kisConnection = MeasurementControlModule.getInstance().getKISConnection(this.kisId);
		this.newMeasurements = Collections.synchronizedList(new LinkedList<Measurement>());
		this.runningMeasurementIds = Collections.synchronizedSet(new HashSet<Identifier>());

		this.running = true;
	}

	/**
	 * Добавить новое измерение для отправки на КИС. Состояние измерения должно
	 * быть НОВЫЙ.
	 * 
	 * @param measurement
	 *        Новое измерение для отправки на КИС
	 */
	synchronized void addMeasurement(final Measurement measurement) {
		assert measurement != null : NON_NULL_EXPECTED;

		final Identifier measurementId = measurement.getId();
		final ActionStatus measurementStatus = measurement.getStatus();
		if (measurementStatus != ACTION_STATUS_NEW) {
			Log.errorMessage("Status: " + measurementStatus
					+ " of measurement '" + measurementId + "' not NEW -- cannot add to queue");
			return;
		}
		if (this.newMeasurements.contains(measurement)) {
			Log.errorMessage("Measurement '" + measurementId + "' of status " + measurementStatus
					+ " already presents in the list of new measurements");
			return;
		}
		if (this.runningMeasurementIds.contains(measurementId)) {
			Log.errorMessage("Measurement '" + measurementId + "' of status " + measurementStatus
					+ " already presents in the set of running measurements");
			return;
		}

		Log.debugMessage("Adding measurement '" + measurementId + "'", DEBUGLEVEL07);
		this.newMeasurements.add(measurement);

		this.notifyAll();
	}

	/**
	 * Добавить выполняемое измерение, которое уже проводится на КИС. Состояние
	 * измерения должно быть ВЫПОЛНЯЕТСЯ. Этот метод нужен для восстановления
	 * состояния приёмопередатчика после перезапуска модуля.
	 * 
	 * @param measurement
	 *        Измерение, проводимое на КИС.
	 */
	synchronized void addRunningMeasurement(final Measurement measurement) {
		assert measurement != null : NON_NULL_EXPECTED;

		final Identifier measurementId = measurement.getId();
		final ActionStatus measurementStatus = measurement.getStatus();
		if (measurementStatus != ACTION_STATUS_RUNNING) {
			Log.errorMessage("Status: " + measurementStatus
					+ " of measurement '" + measurementId + "' not RUNNING -- cannot add to queue");
			return;
		}
		if (this.newMeasurements.contains(measurement)) {
			Log.errorMessage("Measurement '" + measurementId + "' of status " + measurementStatus
					+ " already presents in the list of new measurements");
			return;
		}
		if (this.runningMeasurementIds.contains(measurementId)) {
			Log.errorMessage("Measurement '" + measurementId + "' of status " + measurementStatus
					+ " already presents in the set of running measurements");
			return;
		}

		Log.debugMessage("Adding measurement '" + measurementId + "'", DEBUGLEVEL07);
		this.runningMeasurementIds.add(measurementId);

		this.notifyAll();
	}

	/**
	 * Отменить проведение измерения. Этот метод не менят состояние измерения,
	 * он лишь удаляет измерение из очереди.
	 * 
	 * @todo Реализовать прерывание измерения на КИС.
	 * @param measurementId
	 *        Идентификатор измерения
	 */
	void cancelMeasurement(final Identifier measurementId) {
		assert measurementId != null : NON_NULL_EXPECTED;
		assert measurementId.getMajor() == MEASUREMENT_CODE : ILLEGAL_ENTITY_CODE;

		if (this.newMeasurements.remove(measurementId)) {
			Log.debugMessage("Removed measurement '" + measurementId + "' from list of new measurements", DEBUGLEVEL07);
		} else {
			if (this.runningMeasurementIds.contains(measurementId)) {
				/* @todo Послать команду прерывания измерения на КИС. */
				this.runningMeasurementIds.remove(measurementId);
				Log.debugMessage("Cancelled running measurement '" + measurementId + "'", DEBUGLEVEL07);
			}
		}
	}

	/**
	 * Главный цикл.
	 */
	@Override
	public void run() {
		while (this.running) {

			synchronized (this) {
				while (this.newMeasurements.isEmpty() && this.runningMeasurementIds.isEmpty() && this.running) {
					try {
						this.wait(super.initialTimeToSleep);
					} catch (InterruptedException ie) {
						Log.debugMessage(this.getName() + " -- interrupted", DEBUGLEVEL07);
					}
				}
			}

			if (this.kisConnection.isEstablished()) {

				if (!this.newMeasurements.isEmpty()) {
					final Measurement measurement = this.newMeasurements.get(0);
					final Identifier measurementId = measurement.getId();
					try {
						this.kisConnection.transmitMeasurement(measurement, super.initialTimeToSleep);

						Log.debugMessage("Successfully transferred measurement '" + measurementId + "'", DEBUGLEVEL07);
						this.newMeasurements.remove(measurement);
						this.runningMeasurementIds.add(measurementId);
						measurement.setStatus(ACTION_STATUS_RUNNING);
						StorableObjectPool.flush(measurementId, LoginManager.getUserId(), false);

						super.clearFalls();
					} catch (CommunicationException ce) {
						Log.errorMessage(ce);
						this.kisConnection.drop();
						super.fallCode = FALL_CODE_TRANSMIT_MEASUREMENT;
						this.measurementToRemove = measurement;
						super.sleepCauseOfFall();
						continue;
					} catch (ApplicationException ae) {
						Log.errorMessage(ae);
					}
				}// if (! this.scheduledMeasurements.isEmpty())

				if (this.kisReport == null) {
					try {
						this.kisReport = this.kisConnection.receiveKISReport(super.initialTimeToSleep);
						super.clearFalls();
					} catch (CommunicationException ce) {
						Log.errorMessage(ce);
						this.kisConnection.drop();
						super.fallCode = FALL_CODE_RECEIVE_KIS_REPORT;
						super.sleepCauseOfFall();
					}
				} else {// if (this.kisReport == null)
					final Identifier measurementId = this.kisReport.getMeasurementId();
					Log.debugMessage("Received report for measurement '" + measurementId + "'", DEBUGLEVEL07);
					if (!this.runningMeasurementIds.remove(measurementId)) {
						Log.errorMessage("Measurement '" + measurementId + "' not found in set of running measurements");
					}
					Measurement measurement = null;
					try {
						measurement = StorableObjectPool.getStorableObject(measurementId, true);
					} catch (ApplicationException ae) {
						Log.errorMessage(ae);
					}
					if (measurement != null) {

						Set<MeasurementResultParameter> measurementResultParameters = null;
						try {
							measurementResultParameters = this.kisReport.getResult();
							measurement.setStatus(ACTION_STATUS_COMPLETED);
							final Set<Identifiable> saveObjects = new HashSet<Identifiable>();
							saveObjects.add(measurement);
							saveObjects.addAll(measurementResultParameters);
							StorableObjectPool.flush(saveObjects, LoginManager.getUserId(), false);
						} catch (CreateObjectException coe) {
							Log.errorMessage(coe);
							Log.debugMessage("Cannot create result -- trying to wait", DEBUGLEVEL07);
							try {
								MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
							} catch (CommunicationException ce) {
								Log.errorMessage(ce);
							}
							measurementResultParameters = null;
							super.fallCode = FALL_CODE_CREATE_RESULT;
							this.measurementToRemove = measurement;
							super.sleepCauseOfFall();
						} catch (ApplicationException ae) {
							Log.errorMessage(ae);
						}

						if (measurementResultParameters != null) {
							final Identifier testId = measurement.getTestId();
							final TestProcessor testProcessor = MeasurementControlModule.getInstance().getTestProcessor(testId);
							if (testProcessor != null) {
								assert testProcessor.getTestId().equals(testId) : "Test: '" + testId + "', test processor: '" + testProcessor.getTestId() + "'";
								testProcessor.addMeasurementResultParameters(measurementResultParameters);
								this.kisReport = null;
							} else {// if (testProcessor != null)
								Log.errorMessage("ERROR: Test processor for measurement '" + measurementId + "' + not found");
								this.throwAwayKISReport();
							}// else if (testProcessor != null)
						}

					} else {// if (measurement != null)
						Log.errorMessage("ERROR: Measurement for id '" + measurementId + "' + not found");
						this.throwAwayKISReport();
					}// else if (measurement != null)
				}// else if (this.kisReport == null)

			} else {// if (this.kisConnection.isEstablished())
				final long kisConnectionTimeout = ApplicationProperties.getInt(KEY_KIS_CONNECTION_TIMEOUT, KIS_CONNECTION_TIMEOUT) * 1000;
				try {
					this.kisConnection.establish(kisConnectionTimeout, true);
					super.clearFalls();
				} catch (CommunicationException ce) {
					Log.errorMessage(ce);
					super.fallCode = FALL_CODE_ESTABLISH_CONNECTION;
					super.sleepCauseOfFall();
				}
			}// else if (this.kisConnection.isEstablished())

		}// while
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_ESTABLISH_CONNECTION:
				Log.errorMessage("ERROR: Many errors during establishing connection");
				break;
			case FALL_CODE_TRANSMIT_MEASUREMENT:
				this.cancelAndAbortNewMeasurement();
				break;
			case FALL_CODE_RECEIVE_KIS_REPORT:
				Log.errorMessage("ERROR: Many errors during readig KIS report");
				break;
			case FALL_CODE_CREATE_RESULT:
				this.abortCompletedMeasurementAndReport();
				this.throwAwayKISReport();
				break;
		default:
				Log.errorMessage("ERROR: Unknown error code: " + super.fallCode);
		}
	}

	/**
	 * Измерение, которое не удалось запустить на КИС,
	 * {@link #measurementToRemove} удаляется из списка новых измерений
	 * {@link #newMeasurements}. Его состояние выставляется в ПРЕРВАН. Поле
	 * {@link #measurementToRemove} выставляется в null, чем обеспечивается его
	 * повторное использование.
	 */
	private void cancelAndAbortNewMeasurement() {
		if (this.measurementToRemove != null) {
			Log.debugMessage("Removing measurement '" + this.measurementToRemove.getId() + "' from KIS '" + this.kisId + "'",
					DEBUGLEVEL07);
			this.newMeasurements.remove(this.measurementToRemove);

			this.measurementToRemove.setStatus(ACTION_STATUS_ABORTED);
			try {
				StorableObjectPool.flush(this.measurementToRemove, LoginManager.getUserId(), false);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}

			this.measurementToRemove = null;
		} else {
			Log.errorMessage("ERROR: Measurement to remove is null -- nothing to remove");
		}
	}

	/**
	 * Измерение, для которого не удалось создать результат,
	 * {@link #measurementToRemove} переводится в состояние ПРЕРВАН. Поле
	 * {@link #measurementToRemove} выставляется в null, чем обеспечивается его
	 * повторное использование.
	 */
	private void abortCompletedMeasurementAndReport() {
		Log.errorMessage("ERROR: Cannot create result");
		if (this.measurementToRemove != null) {
			this.measurementToRemove.setStatus(ACTION_STATUS_ABORTED);
			try {
				StorableObjectPool.flush(this.measurementToRemove, LoginManager.getUserId(), true);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
			this.measurementToRemove = null;
		} else {
			Log.errorMessage("ERROR: Measurement to abort is null -- nothing to abort");
		}
	}

	/**
	 * Сбросить последний ответ КИС. Поле {@link #kisReport} сбрасывается в
	 * null, т. е. ответ КИС теряется.
	 */
	private void throwAwayKISReport() {
		if (this.kisReport != null) {
			Log.debugMessage("Throwing away report of measurement '" + this.kisReport.getMeasurementId()
					+ "' from KIS '" + this.kisId + "'", DEBUGLEVEL07);
			this.kisReport = null;
		} else {
			Log.errorMessage("ERROR: KIS report is null -- nothing to throw away");
		}
	}

	/**
	 * Выключение главного цикла.
	 * 
	 * @todo Реализовать прерывание измерения на КИС.
	 */
	synchronized void shutdown() {
		this.running = false;
		/* @todo Послать команду прерывания измерения на КИС. */
		this.kisConnection.drop();
		this.notifyAll();
	}
}
