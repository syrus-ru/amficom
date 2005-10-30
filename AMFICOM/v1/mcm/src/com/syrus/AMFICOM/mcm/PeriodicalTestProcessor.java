/*-
 * $Id: PeriodicalTestProcessor.java,v 1.53 2005/10/30 15:20:17 bass Exp $
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
 * @version $Revision: 1.53 $, $Date: 2005/10/30 15:20:17 $
 * @author $Author: bass $
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
			assert Log.errorMessage("Cannot load temporal pattern '" + test.getTemporalPatternId() + "' for test '" + test.getId() + "'");
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
						assert Log.debugMessage("Removed from set of time stamps date: " + startDate, Log.DEBUGLEVEL10);
					} else {
						assert Log.debugMessage("Date: " + startDate + " not found in set of time stamps", Log.DEBUGLEVEL10);
					}
				}

				//--------
				final StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("From ");
				stringBuffer.append(startDate);
				stringBuffer.append(" to ");
				stringBuffer.append(new Date(toDateLong));
				stringBuffer.append(", include fromDate: ");
				stringBuffer.append(includeFromDate);
				stringBuffer.append("\n");
				for (final Date date : timeStamps) {
					stringBuffer.append("\t date: ");
					stringBuffer.append(date);
					stringBuffer.append("\n");
				}
				assert Log.debugMessage(stringBuffer.toString(), Log.DEBUGLEVEL09);
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
