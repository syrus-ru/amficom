/*-
 * $Id: PeriodicalTestProcessor.java,v 1.51 2005/10/15 17:45:22 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.51 $, $Date: 2005/10/15 17:45:22 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class PeriodicalTestProcessor extends TestProcessor {
	private static final long FRAME = 24*60*60*1000;//ms

	private static final String ABORT_REASON_TEMPORAL_PATTERN = "Failed to load temporal pattern";

	private Date endTime;
	private AbstractTemporalPattern temporalPattern;
	private SortedSet<Date> timeStampsList;

	public PeriodicalTestProcessor(Test test) {
		super(test);

		this.endTime = test.getEndTime();
		try {
			this.temporalPattern = StorableObjectPool.getStorableObject(test.getTemporalPatternId(), true);
		} catch (ApplicationException ae) {
			Log.errorMessage("Cannot load temporal pattern '" + test.getTemporalPatternId() + "' for test '" + test.getId() + "'");
			this.abort(ABORT_REASON_TEMPORAL_PATTERN);
		}
		this.timeStampsList = new TreeSet<Date>();
	}

	@Override
	Date getNextMeasurementStartTime(final Date startDate, final boolean includeFromDate) {
		if (this.timeStampsList.isEmpty()) {
			if (startDate.before(this.endTime)) {
				final long currentDateLong = System.currentTimeMillis();
				long fromDateLong = startDate.getTime();
				while (fromDateLong + FRAME < currentDateLong) {
					fromDateLong += FRAME;
				}
				final long toDateLong = Math.min(fromDateLong + FRAME, this.endTime.getTime());
				final SortedSet<Date> timeStamps = this.temporalPattern.getTimes(fromDateLong, toDateLong);
				if (!includeFromDate) {
					if (timeStamps.remove(startDate)) {
						Log.debugMessage("PeriodicalTestProcessor.getNextMeasurementStartTime | Removed from set of time stamps date: " + startDate,
								Log.DEBUGLEVEL10);
					} else {
						Log.debugMessage("PeriodicalTestProcessor.getNextMeasurementStartTime | Date: " + startDate + " not found in set of time stamps",
								Log.DEBUGLEVEL10);
					}
				}

				//--------
				Log.debugMessage("PeriodicalTestProcessor.getCurrentTimeStamp | From " + startDate
						+ " to " + (new Date(toDateLong))
						+ ", include fromDate: " + includeFromDate, Log.DEBUGLEVEL09);
				for (final Date date : timeStamps) {
					Log.debugMessage("date: " + date, Log.DEBUGLEVEL09);
				}
				//--------

				if (!timeStamps.isEmpty()) {
					final Date pastExcludeDate = new Date(System.currentTimeMillis() - PAST_MEASUREMENT_TIMEOUT);
					this.timeStampsList.addAll(timeStamps.tailSet(pastExcludeDate));
				}

			}
		}

		if (!this.timeStampsList.isEmpty()) {
			final Date measurementStartTime = this.timeStampsList.first();
			this.timeStampsList.remove(measurementStartTime);
			return measurementStartTime;
		}
		return null;
	}

}
