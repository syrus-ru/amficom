/*-
 * $Id: IdlTestImpl.java,v 1.12.2.5 2006/03/15 15:50:02 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestStops;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStamps;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.12.2.5 $, $Date: 2006/03/15 15:50:02 $
 * @module measurement
 */
final class IdlTestImpl extends IdlTest {
	private static final long serialVersionUID = 4694177549880713460L;

	IdlTestImpl() {
		// empty
	}

	IdlTestImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String description,
			final IdlIdentifier groupTestId,
			final IdlIdentifier monitoredElementId,
			final IdlTestStatus status,
			final IdlTestTimeStamps timeStamps,
			final IdlIdentifier[] measurementSetupIds,
			final IdlIdentifier measurementTypeId,
			final int numberOfMeasurements,
			final IdlIdentifier analysisTypeId,
			final IdlTestStops[] stops) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.description = description;
		this.groupTestId = groupTestId;
		this.monitoredElementId = monitoredElementId;
		this.status = status;
		this.timeStamps = timeStamps;
		this.measurementSetupIds = measurementSetupIds;
		this.measurementTypeId = measurementTypeId;
		this.numberOfMeasurements = numberOfMeasurements;
		this.analysisTypeId = analysisTypeId;
		this.stops = stops;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Test getNative() throws IdlCreateObjectException {
		try {
			return new Test(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
