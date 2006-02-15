/*-
 * $Id: IdlActionParameterImpl.java,v 1.1.2.3 2006/02/15 19:38:11 arseniy Exp $
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
 * @version $Revision: 1.1.2.3 $, $Date: 2006/02/15 19:38:11 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class IdlActionParameterImpl extends IdlActionParameter {
	private static final long serialVersionUID = -6410625174852985525L;

	IdlActionParameterImpl() {
		// empty
	}

	IdlActionParameterImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier bindingId,
			final byte[] value,
			final IdlParameterValueKind valueKind,
			final String typeCodename) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.bindingId = bindingId;
		this.value = value;

		this.valueKind = valueKind;
		this.typeCodename = typeCodename;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new ActionParameter(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}

}
