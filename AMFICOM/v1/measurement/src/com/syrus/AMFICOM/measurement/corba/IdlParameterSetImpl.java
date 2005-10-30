/*-
 * $Id: IdlParameterSetImpl.java,v 1.5 2005/10/30 15:20:39 bass Exp $
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
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/10/30 15:20:39 $
 * @module measurement
 */
final class IdlParameterSetImpl extends IdlParameterSet {
	private static final long serialVersionUID = -7843619861375356402L;

	IdlParameterSetImpl() {
		// empty
	}

	IdlParameterSetImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final ParameterSetSort sort,
			final String description,
			final IdlParameter parameters[],
			final IdlIdentifier monitoredElementIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.sort = sort;
		this.description = description;
		this.parameters = parameters;
		this.monitoredElementIds = monitoredElementIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public ParameterSet getNative() throws IdlCreateObjectException {
		try {
			return new ParameterSet(this);
		} catch (final CreateObjectException coe) {
			assert Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
