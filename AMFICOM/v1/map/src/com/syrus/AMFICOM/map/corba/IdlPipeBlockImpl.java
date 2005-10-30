/*-
 * $Id: IdlPipeBlockImpl.java,v 1.2 2005/10/30 14:49:02 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.util.Log;

/**
 * @author Andrei Kroupennikov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/30 14:49:02 $
 * @module map
 */
final class IdlPipeBlockImpl extends IdlPipeBlock {

	IdlPipeBlockImpl() {
		// empty
	}

	IdlPipeBlockImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final int number,
			final int dimensionX,
			final int dimensionY,
			final boolean leftToRight,
			final boolean topToBottom,
			final boolean horizontalVertical) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.number = number;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;
		this.horizontalVertical = horizontalVertical;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public PipeBlock getNative() throws IdlCreateObjectException {
		try {
			return new PipeBlock(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
