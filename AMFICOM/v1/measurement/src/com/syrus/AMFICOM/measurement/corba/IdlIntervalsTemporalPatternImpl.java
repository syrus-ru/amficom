/*-
 * $Id: IdlIntervalsTemporalPatternImpl.java,v 1.4 2005/10/30 14:49:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlIntervalsTemporalPatternPackage.IntervalDuration;
import com.syrus.AMFICOM.measurement.corba.IdlIntervalsTemporalPatternPackage.IntervalTemporalPatternId;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/10/30 14:49:06 $
 * @module measurement
 */
final class IdlIntervalsTemporalPatternImpl extends IdlIntervalsTemporalPattern {
	private static final long serialVersionUID = 9068825033523102821L;

	IdlIntervalsTemporalPatternImpl() {
		// empty
	}

	IdlIntervalsTemporalPatternImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IntervalTemporalPatternId intervalsTemporalPatternId[],
			final IntervalDuration intervalsDuration[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.intervalsTemporalPatternId = intervalsTemporalPatternId;
		this.intervalsDuration = intervalsDuration;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public IntervalsTemporalPattern getNative() throws IdlCreateObjectException {
		try {
			return new IntervalsTemporalPattern(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
