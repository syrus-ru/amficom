/*
 * $Id: TestProcessor.java,v 1.17 2004/08/15 14:40:14 arseniy Exp $
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
 * @version $Revision: 1.17 $, $Date: 2004/08/15 14:40:14 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class TestProcessor extends SleepButWorkThread {
	private static final int TICK_TIME = 5;

	Test test;
	boolean running;
	Transceiver transceiver;
	
	//protected Map measurementResultQueue;	//Map <Identifier measurementId, Result result>
	private List measurementResultList;	//List <Result measurementResult>

	public TestProcessor(Test test) {
		super(ApplicationProperties.getInt("TickTime", TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", MAX_FALLS));

		this.test = test;
		this.running = true;
		
		//this.measurementResultQueue = Collections.synchronizedMap(new HashMap());
		this.measurementResultList = Collections.synchronizedList(new LinkedList());

		MeasurementPort mp = (MeasurementPort)ConfigurationStorableObjectPool.getStorableObject(this.test.getMonitoredElement().getMeasurementPortId(), true);
		Identifier kisId = mp.getKISId();
		this.transceiver = (Transceiver)MeasurementControlModule.transceivers.get(kisId);
		if (this.transceiver == null) {
			Log.errorMessage("Cannot find transceiver for kis '" + kisId.toString() + "'");
			this.shutdown();
		}

		switch (this.test.getStatus().value()) {
			case TestStatus._TEST_STATUS_SCHEDULED:
				//Normal
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
				this.shutdown();
		}
	}
	
	protected void addMeasurementResult(Result result) {
		this.measurementResultList.add(result);
	}

	public abstract void run();

	void processMeasurementResult() {
		Result measurementResult;
		if (! this.measurementResultList.isEmpty()) {
			measurementResult = (Result)this.measurementResultList.remove(0);
			MeasurementControlModule.resultList.add(measurementResult);

			Result[] aeResults = null;
			try {
				aeResults = AnalysisEvaluationProcessor.analyseEvaluate(measurementResult);
			}
			catch (TestProcessingException tpe) {
				Log.errorException(tpe);
			}
			for (int i = 0; i < aeResults.length; i++)
				if (aeResults[i] != null)
					MeasurementControlModule.resultList.add(aeResults[i]);

			try {
				((Measurement)measurementResult.getAction()).updateStatus(MeasurementStatus.MEASUREMENT_STATUS_COMPLETED,
																																	MeasurementControlModule.iAm.getUserId());
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}
		}
	}
	
	protected void shutdown() {
		this.running = false;
		try {
			this.test.updateStatus(TestStatus.TEST_STATUS_ABORTED, MeasurementControlModule.iAm.getUserId());
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
		}
		this.cleanup();
	}

	void cleanup() {
		this.test = null;
		this.measurementResultList.clear();
	}
}
