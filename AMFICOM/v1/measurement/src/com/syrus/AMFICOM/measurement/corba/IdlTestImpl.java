/*-
 * $Id: IdlTestImpl.java,v 1.1 2005/07/06 19:10:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement.corba;

import static com.syrus.util.Log.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/06 19:10:54 $
 * @module measurement_v1
 */
final class IdlTestImpl extends IdlTest {
	private static final long serialVersionUID = 8900736807034094131L;

	IdlTestImpl() {
		// empty
	}

	IdlTestImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlTestTimeStamps timeStamps,
			final IdlIdentifier measurementTypeId,
			final IdlIdentifier analysisTypeId,
			final IdlIdentifier evaluationTypeId,
			final IdlIdentifier groupTestId,
			final TestStatus status,
			final IdlIdentifier monitoredElementId,
			final TestReturnType returnType,
			final String description,
			final int numberOfMeasurements,
			final IdlIdentifier measurementSetupIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.timeStamps = timeStamps;
		this.measurementTypeId = measurementTypeId;
		this.analysisTypeId = analysisTypeId;
		this.evaluationTypeId = evaluationTypeId;
		this.groupTestId = groupTestId;
		this.status = status;
		this.monitoredElementId = monitoredElementId;
		this.returnType = returnType;
		this.description = description;
		this.numberOfMeasurements = numberOfMeasurements;
		this.measurementSetupIds = measurementSetupIds;
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
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
