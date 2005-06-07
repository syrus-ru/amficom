/*
 * $Id: TestProcessor.java,v 1.53 2005/06/07 15:45:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.53 $, $Date: 2005/06/07 15:45:04 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class TestProcessor extends SleepButWorkThread {
	Test test;
	Transceiver transceiver;
	private int numberOfReceivedMResults;
	boolean lastMeasurementAcquisition;
	private long currentMeasurementStartTime;
	long forgetFrame;
	private List measurementResultList;	//List <Result measurementResult>
	boolean running;


	public TestProcessor(Test test) {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000,
					ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, SleepButWorkThread.MAX_FALLS));

		this.test = test;

		this.numberOfReceivedMResults = 0;
		this.lastMeasurementAcquisition = false;
		this.currentMeasurementStartTime = this.test.getStartTime().getTime();
		this.forgetFrame = ApplicationProperties.getInt(MeasurementControlModule.KEY_FORGET_FRAME, MeasurementControlModule.FORGET_FRAME) * 1000;
		this.measurementResultList = Collections.synchronizedList(new LinkedList());
		this.running = true;

		//	Проверить, не устарел ли этот тест
		long timePassed = System.currentTimeMillis() - this.test.getStartTime().getTime();
		if (timePassed > this.forgetFrame) {
			Log.debugMessage("Passed " + timePassed / 1000 + " sec (more than " + this.forgetFrame / 1000
					+ " sec) from start time. Aborting test '" + this.test.getId() + "'", Log.DEBUGLEVEL03);
			this.abort();
		}

		if (this.running) {
			// Проверить правильность КИС. Найти приёмопередатчик.
			Identifier kisId = test.getKISId();
			this.transceiver = (Transceiver) MeasurementControlModule.transceivers.get(kisId);
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
		Measurement lastMeasurement = null;
		try {
			lastMeasurement = this.test.retrieveLastMeasurement();
			Log.debugMessage("TestProcessor.startWithProcessingTest | Last measurement for test '" + this.test.getId()
					+ "' -- '" + lastMeasurement.getId() + "'", Log.DEBUGLEVEL08);
		}
		catch (ApplicationException ae) {
			if (ae instanceof ObjectNotFoundException) {
				this.startWithScheduledTest();
				return;
			}
			Log.errorException(ae);
			this.abort();
		}

		Collection results;
		Result measurementResult = null;
		if (lastMeasurement != null) {
			try{
				this.numberOfReceivedMResults = this.test.retrieveNumberOfResults(ResultSort.RESULT_SORT_MEASUREMENT);
				switch (lastMeasurement.getStatus().value()) {
					case MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED:
						this.transceiver.addMeasurement(lastMeasurement, this);
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING:
						this.transceiver.addAcquiringMeasurement(lastMeasurement, this);
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRED:
						results = lastMeasurement.getResults(true);
						if (results != null && !results.isEmpty())
							measurementResult = (Result) results.iterator().next();
						if (measurementResult != null)
							this.addMeasurementResult(measurementResult);
						else
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ANALYZED_OR_EVALUATED:
						results = lastMeasurement.getResults(true);
						Result analysisResult = null;
						Result evaluationResult = null;
						if (results != null && !results.isEmpty()) {
							Result result;
							for (Iterator it = results.iterator(); it.hasNext();) {
								result = (Result) it.next();
								switch (result.getSort().value()) {
									case ResultSort._RESULT_SORT_MEASUREMENT:
										measurementResult = result;
										break;
									case ResultSort._RESULT_SORT_ANALYSIS:
										analysisResult = result;
										break;
									case ResultSort._RESULT_SORT_EVALUATION:
										evaluationResult = result;
										break;
								}
							}
						}
						if (measurementResult != null)
							MeasurementControlModule.resultList.add(measurementResult);
						else
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");
						if (analysisResult != null)
							MeasurementControlModule.resultList.add(analysisResult);
						else
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find analysis result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");
						if (evaluationResult != null)
							MeasurementControlModule.resultList.add(evaluationResult);
						else
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find evaluation result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");

						lastMeasurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
						try {
							StorableObjectPool.flush(lastMeasurement.getId(), true);
						}
						catch (ApplicationException ae) {
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

	protected final void addMeasurementResult(Result result) {
		if (! this.measurementResultList.contains(result))
			this.measurementResultList.add(result);
	}

	final void newMeasurementCreation(Date startTime) throws CreateObjectException {
		Measurement measurement = this.test.createMeasurement(LoginManager.getUserId(), startTime);
		this.transceiver.addMeasurement(measurement, this);
		this.currentMeasurementStartTime = startTime.getTime();
		try {
			StorableObjectPool.flushGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	final void checkIfCompletedOrAborted() {
		final int numberOfScheduledMeasurements = this.test.getNumberOfMeasurements();
		Log.debugMessage('\'' + this.test.getId().getIdentifierString()
				 + "' numberOfReceivedMResults: " + this.numberOfReceivedMResults
				 + ", numberOfScheduledMeasurements: " + numberOfScheduledMeasurements
				 + ", lastMeasurementAcquisition: " + this.lastMeasurementAcquisition, Log.DEBUGLEVEL07);
		if (this.numberOfReceivedMResults == numberOfScheduledMeasurements && this.lastMeasurementAcquisition)
			this.complete();
		else {
			if (System.currentTimeMillis() - this.currentMeasurementStartTime > this.forgetFrame) {
				Log.debugMessage("Passed " + this.forgetFrame / 1000 + " sec from last measurement creation. Aborting test '"
						+ this.test.getId() + "'", Log.DEBUGLEVEL03);
				this.abort();
			}
		}
	}

	final void processMeasurementResult() {
		Result measurementResult;
		Measurement measurement;
		if (!this.measurementResultList.isEmpty()) {
			measurementResult = (Result) this.measurementResultList.remove(0);
			this.numberOfReceivedMResults++;
			measurement = (Measurement) measurementResult.getAction();

			Result[] aeResults = null;
			try {
				aeResults = AnalysisEvaluationProcessor.analyseEvaluate(measurementResult);
				for (int i = 0; i < aeResults.length; i++)
					if (aeResults[i] != null)
						MeasurementControlModule.resultList.add(aeResults[i]);

				measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_ANALYZED_OR_EVALUATED);
			}
			catch (TestProcessingException tpe) {
				Log.errorException(tpe);
			}

			try {
				StorableObjectPool.flush(measurement.getId(), false);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			MeasurementControlModule.resultList.add(measurementResult);
			measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
			try {
				StorableObjectPool.flush(measurement.getId(), false);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
	}

	private final void updateMyTestStatus(TestStatus status) {
		this.test.setStatus(status);
		try {
			StorableObjectPool.flush(this.test.getId(), true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	protected void complete() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_COMPLETED);
		this.shutdown();
	}

	protected void abort() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_ABORTED);
		if (this.transceiver != null)
			this.transceiver.abortMeasurementsForTestProcessor(this);
		this.shutdown();
	}

	private void shutdown() {
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
