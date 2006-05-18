/*
 * $Id: AbstractImageResource.java,v 1.32 2006/03/15 14:47:31 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.corba.IdlImageResource;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @author $Author: bass $
 * @version $Revision: 1.32 $, $Date: 2006/03/15 14:47:31 $
 * @module resource
 */
public abstract class AbstractImageResource
		extends StorableObject
		implements IdlTransferableObjectExt<IdlImageResource> {
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
			this.fromIdlTransferable(irt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}

	public abstract byte[] getImage();	

	abstract ImageResourceSort getSort();

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	@Override
	public abstract IdlImageResource getIdlTransferable(final ORB orb);

	/**
	 * @param imageResource
	 * @throws IdlConversionException
	 * @see com.syrus.util.transport.idl.IdlTransferableObjectExt#fromIdlTransferable(org.omg.CORBA.portable.IDLEntity)
	 */
	public synchronized void fromIdlTransferable(final IdlImageResource imageResource)
	throws IdlConversionException {
		super.fromIdlTransferable(imageResource);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected final ImageResourceWrapper getWrapper() {
		return ImageResourceWrapper.getInstance();
	}
}
