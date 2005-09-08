/*
 * $Id: AbstractImageResource.java,v 1.23 2005/09/08 18:26:31 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: bass $
 * @version $Revision: 1.23 $, $Date: 2005/09/08 18:26:31 $
 * @module resource
 */
public abstract class AbstractImageResource extends StorableObject {
	static final long serialVersionUID = -730035505208725678L;

	/**
	 * Client-side constructor. Shouldn't be invoked directly but only via
	 * <code>createInstance(...)</code> method (if any).
	 */
	protected AbstractImageResource(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * Middle-tier constructor. Shouldn't be invoked by clients.
	 * @throws CreateObjectException
	 */
	protected AbstractImageResource(final IdlImageResource irt) throws CreateObjectException {
		try {
			this.fromTransferable(irt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}

	public abstract byte[] getImage();	

	abstract ImageResourceSort getSort();

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public abstract IdlImageResource getTransferable(final ORB orb);
}
