/*
 * $Id: TestProcessor.java,v 1.19 2004/08/22 19:10:57 arseniy Exp $
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
import com.syrus.AMFICOM.general.SleepButWorkThread;
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
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.19 $, $Date: 2004/08/22 19:10:57 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class TestProcessor extends SleepButWorkThread {
	private static final int TICK_TIME = 5;

	Test test;
	boolean running;
	Transceiver transceiver;
	int numberOfScheduledMeasurements;
	int numberOfReceivedMResults;
	boolean lastMeasurement;

	private List measurementResultList;	//List <Result measurementResult>

	public TestProcessor(Test test) {
		super(ApplicationProperties.getInt("TickTime", TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", MAX_FALLS));

		this.test = test;
		this.running = true;

		this.measurementResultList = Collections.synchronizedList(new LinkedList());

		MeasurementPort mp = (MeasurementPort)ConfigurationStorableObjectPool.getStorableObject(this.test.getMonitoredElement().getMeasurementPortId(), true);
		Identifier kisId = mp.getKISId();
		this.transceiver = (Transceiver)MeasurementControlModule.transceivers.get(kisId);
		if (this.transceiver == null) {
			Log.errorMessage("Cannot find transceiver for kis '" + kisId.toString() + "'");
			this.abort();
		}

		this.numberOfScheduledMeasurements = this.numberOfReceivedMResults = 0;
		this.lastMeasurement = false;

		switch (this.test.getStatus().value()) {
			case TestStatus._TEST_STATUS_SCHEDULED:
				//Normal
				TestStatusVerifier tsv = new TestStatusVerifier(this.test.getId(), TestStatus.TEST_STATUS_PROCESSING);
				tsv.start();

				try {
					this.test.updateStatus(TestStatus.TEST_STATUS_PROCESSING, MeasurementControlModule.iAm.getUserId());
				}
				catch (UpdateObjectException uoe) {
					Log.errorException(uoe);
				}

				try {
					MeasurementStorableObjectPool.putStorableObject(this.test);
				}
				catch (IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
				}
				break;
			case TestStatus._TEST_STATUS_PROCESSING:
				//Continue process this test -- return later !!
				break;
			default:
				Log.errorMessage("Unappropriate status " + this.test.getStatus().value() + " of test '" + this.test.getId() + "'");
				this.abort();
		}
	}

	protected final void addMeasurementResult(Result result) {
		this.measurementResultList.add(result);
	}

	void processMeasurementResult() {
		Result measurementResult;
		if (! this.measurementResultList.isEmpty()) {
			measurementResult = (Result)this.measurementResultList.remove(0);
			MeasurementControlModule.resultList.add(measurementResult);
			this.numberOfReceivedMResults ++;

			Result[] aeResults = null;
			try {
				aeResults = AnalysisEvaluationProcessor.analyseEvaluate(measurementResult);
				for (int i = 0; i < aeResults.length; i++)
					if (aeResults[i] != null)
						MeasurementControlModule.resultList.add(aeResults[i]);
			}
			catch (TestProcessingException tpe) {
				Log.errorException(tpe);
			}

			try {
				((Measurement)measurementResult.getAction()).updateStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED,
																																	MeasurementControlModule.iAm.getUserId());
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}
		}
	}

	protected void complete() {
		TestStatusVerifier tsv = new TestStatusVerifier(this.test.getId(), TestStatus.TEST_STATUS_COMPLETED);
		tsv.start();

		try {
			this.test.updateStatus(TestStatus.TEST_STATUS_COMPLETED, MeasurementControlModule.iAm.getUserId());
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
		}
		this.shutdown();
	}

	protected void abort() {
		TestStatusVerifier tsv = new TestStatusVerifier(this.test.getId(), TestStatus.TEST_STATUS_ABORTED);
		tsv.start();

		try {
			this.test.updateStatus(TestStatus.TEST_STATUS_ABORTED, MeasurementControlModule.iAm.getUserId());
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
		}
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
					MeasurementControlModule.mServerRef.updateTestStatus((Identifier_Transferable)this.testId.getTransferable(), this.testStatus);
					super.clearFalls();
					this.shutdown();
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					super.sleepCauseOfFall();
				}
			}
		}

		protected void processFall() {
			this.shutdown();
		}

		private void shutdown() {
			this.running = false;
		}
	}
}
