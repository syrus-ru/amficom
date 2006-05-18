/*-
 * $Id: IdlMapLibraryImpl.java,v 1.5 2006/03/14 10:48:01 bass Exp $
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
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.util.Log;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2006/03/14 10:48:01 $
 * @module map
 */

public class IdlMapLibraryImpl extends IdlMapLibrary {
	
	private static final long	serialVersionUID	= -7988225331684079116L;

	IdlMapLibraryImpl() {
		// empty
	}
	
	IdlMapLibraryImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String codename,
			final String description,
			final IdlIdentifier parentMapLibraryId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.codename = codename;
		this.description = description;
		this.parentMapLibraryId = parentMapLibraryId;
	}
	
	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public MapLibrary getNative() throws IdlCreateObjectException {
		try {
			return new MapLibrary(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}		
}
