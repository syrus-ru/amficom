/*
 * $Id: TestProcessor.java,v 1.21 2004/08/25 11:37:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.21 $, $Date: 2004/08/25 11:37:37 $
 * @author $Author: bob $
 * @module mcm_v1
 */

public abstract class TestProcessor extends SleepButWorkThread {
	private static final int TICK_TIME = 5;

	Test test;
	boolean running;
	Transceiver transceiver;
	int numberOfScheduledMeasurements;
	int numberOfReceivedMResults;
	boolean lastMeasurementAcquisition;
//	boolean startedWithProcessingTest;

	private List measurementResultList;	//List <Result measurementResult>

	public TestProcessor(Test test) {
		super(ApplicationProperties.getInt("TickTime", TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", MAX_FALLS));

		this.test = test;
		this.running = true;

		this.measurementResultList = Collections.synchronizedList(new LinkedList());

		try {
			MeasurementPort mp = (MeasurementPort)ConfigurationStorableObjectPool.getStorableObject(this.test.getMonitoredElement().getMeasurementPortId(), true);
			Identifier kisId = mp.getKISId();
			this.transceiver = (Transceiver)MeasurementControlModule.transceivers.get(kisId);
			if (this.transceiver == null) {
				Log.errorMessage("Cannot find transceiver for kis '" + kisId.toString() + "'");
				this.abort();
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			this.abort();
		}
		

		this.numberOfScheduledMeasurements = this.numberOfReceivedMResults = 0;
		this.lastMeasurementAcquisition = false;

		switch (this.test.getStatus().value()) {
			case TestStatus._TEST_STATUS_SCHEDULED:
				//Normally
				this.startWithScheduledTest();
				break;
			case TestStatus._TEST_STATUS_PROCESSING:
				this.startWithProcessingTest();
				break;
			default:
				Log.errorMessage("Unappropriate status " + this.test.getStatus().value() + " of test '" + this.test.getId() + "'");
				this.abort();
		}
	}

	private void startWithScheduledTest() {
//		this.startedWithProcessingTest = false;
		this.updateMyTestStatus(TestStatus.TEST_STATUS_PROCESSING);

		try {
			MeasurementStorableObjectPool.putStorableObject(this.test);
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
		}
	}

	private void startWithProcessingTest() {
//		this.startedWithProcessingTest = true;
		Measurement lastMeasurement = null;
		Result measurementResult = null;
		try {
			lastMeasurement = this.test.retrieveLastMeasurement();
		}
		catch (ObjectNotFoundException onfe){
			Log.errorException(onfe);
		}
		catch (RetrieveObjectException onfe){
			Log.errorException(onfe);
			this.abort();
		}
		if (lastMeasurement != null){
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
						measurementResult = lastMeasurement.retrieveResult(ResultSort.RESULT_SORT_MEASUREMENT);
						this.addMeasurementResult(measurementResult);
						break;
					case MeasurementStatus._MEASUREMENT_STATUS_ANALYZED_OR_EVALUATED:
						measurementResult = lastMeasurement.retrieveResult(ResultSort.RESULT_SORT_MEASUREMENT);
						Result analysisResult = lastMeasurement.retrieveResult(ResultSort.RESULT_SORT_ANALYSIS);
						Result evaluationResult = lastMeasurement.retrieveResult(ResultSort.RESULT_SORT_EVALUATION);
						MeasurementControlModule.resultList.add(measurementResult);
						if (analysisResult != null)
							MeasurementControlModule.resultList.add(analysisResult);
						if (evaluationResult != null)
							MeasurementControlModule.resultList.add(evaluationResult);
						lastMeasurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED,
																				 MeasurementControlModule.iAm.getUserId());
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
		this.measurementResultList.add(result);
	}

	void processMeasurementResult() {
		Result measurementResult;
		Measurement measurement;
		if (! this.measurementResultList.isEmpty()) {
			measurementResult = (Result)this.measurementResultList.remove(0);
			this.numberOfReceivedMResults ++;
			measurement = (Measurement)measurementResult.getAction();

			Result[] aeResults = null;
			try {
				aeResults = AnalysisEvaluationProcessor.analyseEvaluate(measurementResult);
				for (int i = 0; i < aeResults.length; i++)
					if (aeResults[i] != null)
						MeasurementControlModule.resultList.add(aeResults[i]);
				measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_ANALYZED_OR_EVALUATED,
																 MeasurementControlModule.iAm.getUserId());
			}
			catch (TestProcessingException tpe) {
				Log.errorException(tpe);
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}

			MeasurementControlModule.resultList.add(measurementResult);
			try {
				measurement.updateStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED,
																 MeasurementControlModule.iAm.getUserId());
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}
		}
	}

	private void updateMyTestStatus(TestStatus status) {
		TestStatusVerifier tsv = new TestStatusVerifier(this.test.getId(), status);
		tsv.start();

		try {
			this.test.updateStatus(status, MeasurementControlModule.iAm.getUserId());
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
		}
	}

	protected void complete() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_COMPLETED);
		this.shutdown();
	}

	protected void abort() {
		this.updateMyTestStatus(TestStatus.TEST_STATUS_ABORTED);
		this.shutdown();
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

	private class TestStatusVerifier extends SleepButWorkThread {
		private Identifier testId;
		private TestStatus testStatus;
		private boolean running;

		TestStatusVerifier (Identifier testId, TestStatus testStatus) {
			super(ApplicationProperties.getInt("TickTime", TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", MAX_FALLS));

			this.testId = testId;
			this.testStatus = testStatus;

			this.running = true;
		}

		public void run() {
			while (this.running) {
				try {
					Log.debugMessage("Updating on server status of test '" + this.testId + "' to " + this.testStatus.value(), Log.DEBUGLEVEL07);
					if (MeasurementControlModule.mServerRef != null) {
						MeasurementControlModule.mServerRef.updateTestStatus((Identifier_Transferable)this.testId.getTransferable(), this.testStatus);
						super.clearFalls();
						this.shutdown();
					}
					else {
						MeasurementControlModule.resetMServerConnection();
						super.sleepCauseOfFall();
					}
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					super.sleepCauseOfFall();
				}
			}	//while
		}

		protected void processFall() {
			this.shutdown();
		}

		private void shutdown() {
			this.running = false;
		}
	}
}
