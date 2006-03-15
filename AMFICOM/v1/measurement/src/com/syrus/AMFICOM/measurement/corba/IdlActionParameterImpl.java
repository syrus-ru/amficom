/*-
 * $Id: IdlActionParameterImpl.java,v 1.1.2.6 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.ActionParameter;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.6 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class IdlActionParameterImpl extends IdlActionParameter {
	private static final long serialVersionUID = 6070258509252618584L;

	IdlActionParameterImpl() {
		// empty
	}

	IdlActionParameterImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final byte[] value,
			final IdlIdentifier bindingId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.value = value;
		this.bindingId = bindingId;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new ActionParameter(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}

}
