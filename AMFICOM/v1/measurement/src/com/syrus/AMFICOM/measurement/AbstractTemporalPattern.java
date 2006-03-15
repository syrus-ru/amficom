/*-
 * $Id: AbstractTemporalPattern.java,v 1.12.2.1 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @version $Revision: 1.12.2.1 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module measurement
 */
public abstract class AbstractTemporalPattern extends StorableObject {

	protected SortedSet<Date> times;

	protected long startTime = 0;
	protected long endTime = 0;

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	protected AbstractTemporalPattern() {
		super();
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	protected AbstractTemporalPattern(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * get times in ms that describes by temporal patterns and between start and
	 * end
	 *
	 * @param start
	 *            Date
	 * @param end
	 *            Date
	 * @return SortedSet of java.util.Date
	 */
	public final SortedSet<Date> getTimes(final Date start, final Date end) {
		return Collections.unmodifiableSortedSet(this.getTimes(start.getTime(), end.getTime()));
	}

	/**
	 * get times in ms that describes by temporal patterns and between start and
	 * end
	 * 
	 * @param start
	 *        long
	 * @param end
	 *        long
	 * @return SortedSet of java.util.Date
	 */
	public final SortedSet<Date> getTimes(final long start, final long end) {
		if (this.startTime != start) {
			this.times = null;
		}
		this.startTime = start;
		if (this.endTime != end) {
			this.times = null;
		}
		this.endTime = end;

		if (this.times == null) {
			this.times = new TreeSet<Date>();
			this.fillTimes();
		}
		return this.times;
	}

	protected abstract void fillTimes();
	
	/**
	 * @param start 
	 * @param end
	 * @param startInterval
	 * @param endInterval
	 * @return subtimes of (start, end) interval of (startInterval, endInterval)
	 */
	public abstract SortedSet<Date> getTimes(final Date start, 
		final Date end,
		final Date startInterval, 
		final Date endInterval); 
}
