package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.ArrayList;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.PTTemporalTemplate;
import com.syrus.AMFICOM.util.RetrieveObjectException;
import com.syrus.util.Log;

public class PeriodicalTestProcessor extends TestProcessor {
	private static final long FRAME = 24*60*60*1000;//ms
	private static final int STATUS_NEW_MEASUREMENT = 0;
	private static final int STATUS_MEASUREMENT_IS_WAITING = 1;
	private static final int STATUS_NEW_FRAME = 2;
	private static final int STATUS_LAST_MEASUREMENT_GONE = 3;

	private PTTemporalTemplate pt_template;
	private int status;

	public PeriodicalTestProcessor(Test test) {
		super(test);
		try {
			this.pt_template = new PTTemporalTemplate(test.getPTTemplateId());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			super.abort();
		}
		this.status = STATUS_NEW_FRAME;
	}

	public void run() {
		long start_time_l = super.test.getStartTime().getTime();
		long end_time_l = super.test.getEndTime().getTime();
		ArrayList tt_frame = null;
		long cur_time;
		Date cur_start_time = null;
		Measurement measurement = null;
		while (super.running) {
			try {
				sleep(super.tick_time);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

			switch (this.status) {
				case STATUS_NEW_FRAME:
					cur_time = System.currentTimeMillis();
					if (cur_time <= end_time_l) {
						tt_frame = this.pt_template.getTimeStamps(start_time_l, cur_time, Math.min(end_time_l, cur_time + FRAME));
						this.status = STATUS_NEW_MEASUREMENT;
					}
					else
						this.status = STATUS_LAST_MEASUREMENT_GONE;
					break;
				case STATUS_NEW_MEASUREMENT:
					if (tt_frame != null && ! tt_frame.isEmpty()) {
						cur_start_time = (Date)tt_frame.remove(0);
						measurement = null;
						try {
							measurement = super.test.createMeasurement(MeasurementControlModule.createIdentifier("measurement"),
																												 cur_start_time);
						}
						catch (Exception e) {
							Log.errorException(e);
						}
						if (measurement != null)
							this.status = STATUS_MEASUREMENT_IS_WAITING;
					}
					else
						this.status = STATUS_NEW_FRAME;
					break;
				case STATUS_MEASUREMENT_IS_WAITING:
					if (measurement != null && cur_start_time.getTime() <= System.currentTimeMillis()) {
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

/*
	private int calculateTotalMeasurementsNumber() {
		int n_cycles = (int)((this.end_time - this.start_times[0])/this.period);
		int n1 = n_cycles * this.start_times.length;
		long offset = n_cycles * this.period;
		int n2 = 0;
		while (n2 < this.start_times.length
					 && this.start_times[n2] + offset <= this.end_time)
			n2 ++;

		return (n1 + n2);
	}

	private long calculateNextTime() {
		long tc = System.currentTimeMillis();
		int n_cycles = (int)((tc - this.start_times[0])/this.period);
		long offset = n_cycles * this.period;
		int n = 0;
		while (n < this.start_times.length
					 && this.start_times[n] + offset < tc)
			n ++;

		return (this.start_times[n] + offset);
	}*/
}