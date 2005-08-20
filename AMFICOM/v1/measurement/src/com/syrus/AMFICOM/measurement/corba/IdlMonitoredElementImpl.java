/*-
 * $Id: IdlMonitoredElementImpl.java,v 1.1 2005/08/20 19:25:23 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElement;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.1 $, $Date: 2005/08/20 19:25:23 $
 * @module config
 */
final class IdlMonitoredElementImpl extends IdlMonitoredElement {
	private static final long serialVersionUID = 7497960841961799987L;

	IdlMonitoredElementImpl() {
		// empty
	}

	IdlMonitoredElementImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier domainId,
			final String name,
			final IdlIdentifier measurementPortId,
			final MonitoredElementSort sort,
			final String localAddress,
			final IdlIdentifier monitoredDomainMemberIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.domainId = domainId;
		this.name = name;
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;
		this.monitoredDomainMemberIds = monitoredDomainMemberIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public MonitoredElement getNative() throws IdlCreateObjectException {
		try {
			return new MonitoredElement(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
