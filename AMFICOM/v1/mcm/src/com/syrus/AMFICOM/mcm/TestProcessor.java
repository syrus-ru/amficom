/*
 * $Id: TestProcessor.java,v 1.43 2005/03/30 15:46:54 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.43 $, $Date: 2005/03/30 15:46:54 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class TestProcessor extends SleepButWorkThread {
	private static final String KEY_FORGET_FRAME = "ForgetFrame";

	private static final int FORGET_FRAME = 24 * 60 * 60;

	Test test;
	Transceiver transceiver;
	int numberOfScheduledMeasurements;
	int numberOfReceivedMResults;
	boolean lastMeasurementAcquisition;
//	boolean startedWithProcessingTest;
	long forgetFrame;
	private List measurementResultList;	//List <Result measurementResult>
	boolean running;


	public TestProcessor(Test test) {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000,
					ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, SleepButWorkThread.MAX_FALLS));

		this.test = test;

		this.numberOfScheduledMeasurements = this.numberOfReceivedMResults = 0;
		this.lastMeasurementAcquisition = false;
		this.forgetFrame = ApplicationProperties.getInt(KEY_FORGET_FRAME, FORGET_FRAME) * 1000;
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
			// Проверить правильность КИС.
			Identifier kisId = test.getKISId();
			this.transceiver = (Transceiver) MeasurementControlModule.transceivers.get(kisId);
			if (this.transceiver == null) {
				Log.errorMessage("TestProcessor<init> | Cannot find transceiver for kis '" + kisId + "'");
				this.abort();// this.stopInit();
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
					this.abort();// this.stopInit();
			}
		}
	}
//
//	private final void stopInit() {
//		this.updateMyTestStatus(TestStatus.TEST_STATUS_ABORTED);
//		this.running = false;
//		this.measurementResultList.clear();
//		MeasurementControlModule.testProcessors.remove(this.test.getId());
//		this.test = null;
//	}

	private void startWithScheduledTest() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_PROCESSING);

		try {
			MeasurementStorableObjectPool.putStorableObject(this.test);
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
		}
	}

	private void startWithProcessingTest() {
		Measurement lastMeasurement = null;
		try {
			lastMeasurement = this.test.retrieveLastMeasurement();
		}
		catch (DatabaseException de) {
			if (de instanceof ObjectNotFoundException) {
				startWithScheduledTest();
				return;
			}
			Log.errorException(de);
			this.abort();//this.stopInit();
		}

		Collection results;
		Result measurementResult = null;
		if (lastMeasurement != null) {
			try{
				this.numberOfScheduledMeasurements = this.test.retrieveNumberOfMeasurements();
				this.numberOfReceivedMResults = this.test.retrieveNumberOfResults(ResultSort.RESULT_SORT_MEASUREMENT);
				switch (lastMeasurement.getStatus().value()) {
					case MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED:
						this.transceiver.addMeasurement(lastMeasurement, this);
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING:
						this.transceiver.addAcquiringMeasurement(lastMeasurement, this);
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRED:
						results = lastMeasurement.getResults();
						if (results != null && !results.isEmpty())
							measurementResult = (Result) results.iterator().next();
						if (measurementResult != null)
							this.addMeasurementResult(measurementResult);
						else
							Log.errorMessage("TestProcessor.startWithProcessingTest | Cannot find result for measurement '"
									+ lastMeasurement.getId() + "' (last of test '" + this.test.getId() + "')");
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ANALYZED_OR_EVALUATED:
						results = lastMeasurement.getResults();
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
							MeasurementStorableObjectPool.flush(true);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}

						break;
				}
			}
			catch (DatabaseException de) {
				Log.errorException(de);
				this.abort();//this.stopInit();
			}
		}
	}

	protected final void addMeasurementResult(Result result) {
		if (! this.measurementResultList.contains(result))
			this.measurementResultList.add(result);
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
				MeasurementStorableObjectPool.flush(false);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			MeasurementControlModule.resultList.add(measurementResult);
			measurement.setStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED);
			try {
				MeasurementStorableObjectPool.flush(false);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
	}

	private final void updateMyTestStatus(TestStatus status) {
		this.test.setStatus(status);
		try {
			MeasurementStorableObjectPool.putStorableObject(this.test);
			MeasurementStorableObjectPool.flush(true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

//		TestStatusVerifier tsv = new TestStatusVerifier(this.test.getId(), status);
//		tsv.start();
//
//		try {
//			this.test.updateStatus(status, MeasurementControlModule.iAm.getUserId());
//		}
//		catch (UpdateObjectException uoe) {
//			Log.errorException(uoe);
//		}
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


//	private class TestStatusVerifier extends SleepButWorkThread {
//		private Identifier testId;
//		private TestStatus testStatus;
//		private boolean running;
//
//		TestStatusVerifier (Identifier testId, TestStatus testStatus) {
//			super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000,
//						ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, SleepButWorkThread.MAX_FALLS));
//
//			this.testId = testId;
//			this.testStatus = testStatus;
//
//			this.running = true;
//		}
//
//		public void run() {
//			while (this.running) {
//				try {
//					Log.debugMessage("Updating on server status of test '" + this.testId + "' to " + this.testStatus.value(), Log.DEBUGLEVEL07);
//					if (MeasurementControlModule.mServerRef != null) {
//						MeasurementControlModule.mServerRef.updateTestStatus((Identifier_Transferable)this.testId.getTransferable(), this.testStatus);
//						super.clearFalls();
//						this.shutdown();
//					}
//					else {
//						MeasurementControlModule.resetMServerConnection();
//						super.sleepCauseOfFall();
//					}
//				}
//				catch (org.omg.CORBA.SystemException se) {
//					Log.errorException(se);
//					MeasurementControlModule.resetMServerConnection();
//					super.sleepCauseOfFall();
//				}
//			}	//while
//		}
//
//		protected void processFall() {
//			this.shutdown();
//		}
//
//		private void shutdown() {
//			this.running = false;
//		}
//	}
}
