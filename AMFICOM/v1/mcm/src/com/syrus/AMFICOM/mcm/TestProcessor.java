/*
 * $Id: TestProcessor.java,v 1.67 2005/09/14 18:02:52 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementWrapper;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.67 $, $Date: 2005/09/14 18:02:52 $
 * @author $Author: arseniy $
 * @module mcm
 */

abstract class TestProcessor extends SleepButWorkThread {
	Test test;
	Transceiver transceiver;
	private int numberOfReceivedMResults;
	boolean lastMeasurementAcquisition;
	private long currentMeasurementStartTime;
	long forgetFrame;
	private List<Result> measurementResultList;
	boolean running;


	public TestProcessor(final Test test) {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, SleepButWorkThread.MAX_FALLS));

		this.test = test;

		this.numberOfReceivedMResults = 0;
		this.lastMeasurementAcquisition = false;
		this.currentMeasurementStartTime = this.test.getStartTime().getTime();
		this.forgetFrame = ApplicationProperties.getInt(MeasurementControlModule.KEY_FORGET_FRAME, MeasurementControlModule.FORGET_FRAME) * 1000;
		this.measurementResultList = Collections.synchronizedList(new LinkedList<Result>());
		this.running = true;

		//	Проверить, не устарел ли этот тест
		final long timePassed = System.currentTimeMillis() - this.test.getStartTime().getTime();
		if (timePassed > this.forgetFrame) {
			Log.debugMessage("Passed " + timePassed / 1000 + " sec (more than " + this.forgetFrame / 1000
					+ " sec) from start time. Aborting test '" + this.test.getId() + "'", Log.DEBUGLEVEL03);
			this.abort();
		}

		if (this.running) {
			// Проверить правильность КИС. Найти приёмопередатчик.
			final Identifier kisId = test.getKISId();
			this.transceiver = MeasurementControlModule.transceivers.get(kisId);
			if (this.transceiver == null) {
				Log.errorMessage("TestProcessor<init> | Cannot find transceiver for kis '" + kisId + "'");
				this.abort();
			}
		}

		if (this.running) {
			// Различные способы обработки теста в зависимости от его статуса
			switch (this.test.getStatus().value()) {
				case TestStatus._TEST_STATUS_SCHEDULED:
					// Нормальная работа
					this.startWithScheduledTest();
					break;
				case TestStatus._TEST_STATUS_PROCESSING:
					// Перезапуск после сбоя
					this.startWithProcessingTest();
					break;
				default:
					Log.errorMessage("Unappropriate status " + this.test.getStatus().value() + " of test '" + this.test.getId() + "'");
					this.abort();
			}
		}
	}

	private void startWithScheduledTest() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_PROCESSING);
	}

	private void startWithProcessingTest() {
		final StorableObjectDatabase<Test> database = DatabaseContext.getDatabase(ObjectEntities.TEST_CODE);
		final TestDatabase testDatabase = (TestDatabase) database;
		Measurement lastMeasurement = null;
		try {
			lastMeasurement = testDatabase.retrieveLastMeasurement(this.test);
			Log.debugMessage("TestProcessor.startWithProcessingTest | Last measurement for test '" + this.test.getId()
					+ "' -- '" + lastMeasurement.getId() + "'", Log.DEBUGLEVEL08);
		} catch (ApplicationException ae) {
			if (ae instanceof ObjectNotFoundException) {
				this.startWithScheduledTest();
				return;
			}
			Log.errorException(ae);
			this.abort();
		}

		Set<Result> results;
		Result measurementResult = null;
		if (lastMeasurement != null) {
			try{
				this.numberOfReceivedMResults = testDatabase.retrieveNumberOfResults(this.test, ResultSort.RESULT_SORT_MEASUREMENT);
				switch (lastMeasurement.getStatus().value()) {
					case MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED:
						this.transceiver.addMeasurement(lastMeasurement, this);
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING:
						this.transceiver.addAcquiringMeasurement(lastMeasurement, this);
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRED:
						results = lastMeasurement.getResults(true);
						if (results != null && !results.isEmpty()) {
							measurementResult = results.iterator().next();
						}
						if (measurementResult != null) {
							this.addMeasurementResult(measurementResult);
						} else {
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");
						}
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ANALYZED:
						results = lastMeasurement.getResults(true);
						Result analysisResult = null;
						if (results != null && !results.isEmpty()) {
							for (final Result result : results) {
								switch (result.getSort().value()) {
									case ResultSort._RESULT_SORT_MEASUREMENT:
										measurementResult = result;
										break;
									case ResultSort._RESULT_SORT_ANALYSIS:
										analysisResult = result;
										break;
								}
							}
						}
						if (measurementResult != null) {
							Log.debugMessage("TestProcessor.startWithProcessingTest | Found measurement result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')", Log.DEBUGLEVEL08);
						} else {
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find measurement result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");
						}
						if (analysisResult != null) {
							Log.debugMessage("TestProcessor.startWithProcessingTest | Found analysis result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')", Log.DEBUGLEVEL08);
						} else {
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find analysis result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");
						}

						lastMeasurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
						try {
							StorableObjectPool.flush(lastMeasurement, LoginManager.getUserId(), false);
						} catch (ApplicationException ae) {
							Log.errorException(ae);
						}

						break;
				}
			}
			catch (DatabaseException de) {
				Log.errorException(de);
				this.abort();
			}
		}
	}

	protected final void addMeasurementResult(final Result result) {
		if (!this.measurementResultList.contains(result)) {
			this.measurementResultList.add(result);
		}
	}

	final void newMeasurementCreation(final Date startTime) throws CreateObjectException {
		final Measurement measurement = this.test.createMeasurement(LoginManager.getUserId(), startTime);
		this.transceiver.addMeasurement(measurement, this);
		this.currentMeasurementStartTime = startTime.getTime();
		try {
			StorableObjectPool.flush(measurement, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	final void checkIfCompletedOrAborted() {
		final int numberOfScheduledMeasurements = this.test.getNumberOfMeasurements();
		Log.debugMessage('\'' + this.test.getId().getIdentifierString()
				 + "' numberOfReceivedMResults: " + this.numberOfReceivedMResults
				 + ", numberOfScheduledMeasurements: " + numberOfScheduledMeasurements
				 + ", lastMeasurementAcquisition: " + this.lastMeasurementAcquisition, Log.DEBUGLEVEL07);
		if (this.numberOfReceivedMResults == numberOfScheduledMeasurements && this.lastMeasurementAcquisition) {
			this.complete();
		} else if (System.currentTimeMillis() - this.currentMeasurementStartTime > this.forgetFrame) {
				Log.debugMessage("Passed " + this.forgetFrame / 1000 + " sec from last measurement creation. Aborting test '"
						+ this.test.getId() + "'", Log.DEBUGLEVEL03);
				this.abort();
		}
	}

	final void processMeasurementResult() {
		if (!this.measurementResultList.isEmpty()) {
			final Result measurementResult = this.measurementResultList.remove(0);
			this.numberOfReceivedMResults++;
			final Measurement measurement = (Measurement) measurementResult.getAction();

			try {
				final Result[] aeResults = AnalysisEvaluationProcessor.analyseEvaluate(measurementResult);
				for (int i = 0; i < aeResults.length; i++) {
					if (aeResults[i] != null) {
						Log.debugMessage("TestProcessor.processMeasurementResult | Result: '" + aeResults[i].getId()
								+ "' of measurement '" + measurement.getId() + "'", Log.DEBUGLEVEL09);
					}
				}

				measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ANALYZED);

				try {
					StorableObjectPool.flush(ObjectEntities.RESULT_CODE, LoginManager.getUserId(), false);
					// - Every action contains in dependencies of it's result
				} catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}
			catch (TestProcessingException tpe) {
				Log.errorException(tpe);
			}

			measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
			try {
				StorableObjectPool.flush(measurement, LoginManager.getUserId(), false);
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
	}

	private final void updateMyTestStatus(final TestStatus status) {
		this.test.setStatus(status);
		try {
			StorableObjectPool.flush(this.test, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	protected void complete() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_COMPLETED);
		this.shutdown();
	}

	protected void stopTest() {
		if (this.transceiver != null) {
			this.transceiver.removeMeasurementsOfTestProcessor(this);
		}

		try {
			final LinkedIdsCondition lic = new LinkedIdsCondition(this.test.getId(), ObjectEntities.MEASUREMENT_CODE);
			final TypicalCondition tc = new TypicalCondition(MeasurementStatus._MEASUREMENT_STATUS_COMPLETED,
					OperationSort.OPERATION_NOT_EQUALS,
					ObjectEntities.MEASUREMENT_CODE,
					MeasurementWrapper.COLUMN_STATUS);
			final CompoundCondition condition = new CompoundCondition(lic, CompoundConditionSort.AND, tc);
			final Set<Measurement> measurements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (final Measurement measurement : measurements) {
				measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ABORTED);
			}
			StorableObjectPool.flush(ObjectEntities.MEASUREMENT_CODE, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		this.shutdown();
	}

	protected void abort() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_ABORTED);
		this.stopTest();
	}

	protected void shutdown() {
		this.running = false;
		this.cleanup();
	}

	void cleanup() {
		this.measurementResultList.clear();
		MeasurementControlModule.testProcessors.remove(this.test.getId());
		this.test = null;
	}

	public Identifier getTestId() {
		return this.test.getId();
	}

}
