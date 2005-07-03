/*-
 * $Id: AbstractTemporalPattern.java,v 1.4 2005/06/23 11:54:10 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/23 11:54:10 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public abstract class AbstractTemporalPattern extends StorableObject {

	private static final long serialVersionUID = -4278178783985442738L;

	protected SortedSet<Date>	times;

	protected long		startTime	= 0;
	protected long		endTime		= 0;

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	protected AbstractTemporalPattern(final Identifier id) {
		super(id);
	}

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
			final long version) {
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
	public final SortedSet<Date> getTimes(final Date start,
	                                final Date end) {
		return this.getTimes(start.getTime(), end.getTime());
	}

	/**
	 * get times in ms that describes by temporal patterns and between start and
	 * end
	 *
	 * @param start
	 *            long
	 * @param end
	 *            long
	 * @return SortedSet of java.util.Date
	 */
	public final SortedSet<Date> getTimes(final long start,
	                                final long end) {
		if (this.times == null)
			this.times = new TreeSet<Date>();
		if (this.startTime != start)
			this.times.clear();
		this.startTime = start;
		if (this.endTime != end)
			this.times.clear();
		this.endTime = end;

		if (this.times.isEmpty()) {
			this.fillTimes();
		}
		return this.times;
	}

	protected abstract void fillTimes();
}
