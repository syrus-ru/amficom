package com.syrus.AMFICOM.mcm;

import java.util.Date;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.Log;

public class ContinuousTestProcessor extends TestProcessor {
	private static final int STATUS_NEW_MEASUREMENT = 0;
	private static final int STATUS_MEASUREMENT_IS_WAITING = 1;
	private static final int STATUS_LAST_MEASUREMENT_GONE = 3;

	private long[] ti;
	private int status;

	public ContinuousTestProcessor(Test test) {
		super(test);
		Date[] dti = super.test.getTTTimestamps();
		this.ti = new long[dti.length];
		for (int i = 0; i < this.ti.length; i++)
			this.ti[i] = dti[i].getTime();
		this.status = STATUS_NEW_MEASUREMENT;
	}

	public void run() {
		Measurement measurement = null;
		while (super.running) {
			try {
				sleep(super.tick_time);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

			switch (this.status) {
				case STATUS_NEW_MEASUREMENT:
					if (super.n_measurements < this.ti.length) {
						measurement = null;
						try {
							measurement = super.test.createMeasurement(MeasurementControlModule.createIdentifier("measurement"),
																												 new Date(ti[super.n_measurements]));
							if (measurement != null)
								this.status = STATUS_MEASUREMENT_IS_WAITING;
						}
						catch (Exception e) {
							Log.errorException(e);
						}
					}
					else
						this.status = STATUS_LAST_MEASUREMENT_GONE;
					break;
				case STATUS_MEASUREMENT_IS_WAITING:
					if (measurement != null && ti[super.n_measurements] <= System.currentTimeMillis()) {
						super.transceiver.addMeasurement(measurement, this);
						super.n_measurements ++;
						this.status = STATUS_NEW_MEASUREMENT;
					}
					break;
				case STATUS_LAST_MEASUREMENT_GONE:
					break;
			}//switch

			if (this.status != STATUS_LAST_MEASUREMENT_GONE || super.n_measurements < super.n_reports)
				super.checkMeasurementResults();
			else
				break;
		}//while

		super.cleanup();
	}//run
}