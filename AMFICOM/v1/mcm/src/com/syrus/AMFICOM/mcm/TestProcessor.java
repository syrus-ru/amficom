/*
 * $Id: TestProcessor.java,v 1.15 2004/08/13 17:43:52 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.15 $, $Date: 2004/08/13 17:43:52 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class TestProcessor extends SleepButWorkThread {
	private static final int TICK_TIME = 5;

	Test test;
	boolean running;
	Transceiver transceiver;
	
	protected Map measurementResultQueue;//Map <Measurement, Result>

	public TestProcessor(Test test) {
		super(ApplicationProperties.getInt("TickTime", TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", MAX_FALLS));

		this.test = test;
		this.running = true;
		
		this.measurementResultQueue = Collections.synchronizedMap(new HashMap());

		MeasurementPort mp = (MeasurementPort)ConfigurationStorableObjectPool.getStorableObject(this.test.getMonitoredElement().getMeasurementPortId(), true);
		Identifier kisId = mp.getKISId();
		this.transceiver = (Transceiver)MeasurementControlModule.transceivers.get(kisId);
		if (this.transceiver == null) {
			Log.errorMessage("Cannot find transceiver for kis '" + kisId.toString() + "'");
			this.shutdown();
		}
	}
	
	protected void setMeasurementResult(Measurement measurement, Result result) {
		this.measurementResultQueue.put(measurement, result);
	}

	public abstract void run();
	
	protected void shutdown() {
		this.running = false;
		try {
			this.test.setStatus(TestStatus.TEST_STATUS_ABORTED, MeasurementControlModule.iAm.getUserId());
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
		}
		this.cleanup();
	}

	protected void processError() {
		
	}

	void cleanup() {
		this.test = null;
	}
}
