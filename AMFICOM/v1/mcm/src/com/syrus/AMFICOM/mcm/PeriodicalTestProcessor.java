package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.util.Log;

public class PeriodicalTestProcessor extends TestProcessor {
	private static final long FRAME = 24*60*60*1000;//ms
	private static final int STATUS_NEW_MEASUREMENT = 0;
	private static final int STATUS_MEASUREMENT_IS_WAITING = 1;
	private static final int STATUS_NEW_FRAME = 2;
	private static final int STATUS_LAST_MEASUREMENT_GONE = 3;

	private TemporalPattern temporalPattern;
	private int status;

	public PeriodicalTestProcessor(Test test) {
		super(test);
		try {
			this.temporalPattern = new TemporalPattern(test.getTemporalPatternId());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			super.abort();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			super.abort();
		}
		this.status = STATUS_NEW_FRAME;
	}

	public void run() {
		Measurement measurement = null;
		while (super.running) {
			try {
				sleep(super.tickTime);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

			switch (this.status) {
				case STATUS_NEW_FRAME:
					break;
				case STATUS_NEW_MEASUREMENT:
					break;
				case STATUS_MEASUREMENT_IS_WAITING:
					break;
				case STATUS_LAST_MEASUREMENT_GONE:
					break;
			}//switch

			if (this.status != STATUS_LAST_MEASUREMENT_GONE || super.nMeasurements < super.nReports)
				super.checkMeasurementResults();
			else
				break;
		}//while

		super.cleanup();
	}//run
}