/*-
 * $Id: IdlSchemeOptimizeInfoImpl.java,v 1.2.2.1 2006/03/15 15:47:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfo;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @module scheme
 */
final class IdlSchemeOptimizeInfoImpl extends IdlSchemeOptimizeInfo {
	private static final long serialVersionUID = -9084236419408389263L;

	IdlSchemeOptimizeInfoImpl() {
		// empty
	}

	IdlSchemeOptimizeInfoImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final int optimizationMode,
			final int iterations,
			final double price,
			final double waveLength,
			final double lenMargin,
			final double mutationRate,
			final double mutationDegree,
			final double rtuDeleteProb,
			final double rtuCreateProb,
			final double nodesSpliceProb,
			final double nodesCutProb,
			final double survivorRate,
			final IdlIdentifier parentSchemeId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.optimizationMode = optimizationMode;
		this.iterations = iterations;
		this.price = price;
		this.waveLength = waveLength;
		this.lenMargin = lenMargin;
		this.mutationRate = mutationRate;
		this.mutationDegree = mutationDegree;
		this.rtuDeleteProb = rtuDeleteProb;
		this.rtuCreateProb = rtuCreateProb;
		this.nodesSpliceProb = nodesSpliceProb;
		this.nodesCutProb = nodesCutProb;
		this.survivorRate = survivorRate;
		this.parentSchemeId = parentSchemeId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeOptimizeInfo getNative() throws IdlCreateObjectException {
		try {
			return new SchemeOptimizeInfo(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
