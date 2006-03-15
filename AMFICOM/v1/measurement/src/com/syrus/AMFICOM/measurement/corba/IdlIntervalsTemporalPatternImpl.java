/*-
 * $Id: IdlIntervalsTemporalPatternImpl.java,v 1.6.2.1 2006/03/15 15:50:02 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.6.2.1 $, $Date: 2006/03/15 15:50:02 $
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
			throw coe.getIdlTransferable();
		}
	}
}
