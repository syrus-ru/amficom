/*-
 * $Id: EmptyEventObjectLoader.java,v 1.1 2006/06/15 17:57:19 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/15 17:57:19 $
 * @module event
 */
final class EmptyEventObjectLoader implements ObjectLoader {
	public <T extends StorableObject> Set<T> loadStorableObjects(
			final Set<Identifier> ids)
	throws ApplicationException {
		return Collections.<T> emptySet();
	}

	public <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(
			final Set<Identifier> ids,
			final StorableObjectCondition condition)
	throws ApplicationException { 
		return Collections.<T> emptySet();
	}

	public Map<Identifier, StorableObjectVersion> getRemoteVersions(
			final Set<Identifier> ids)
	throws ApplicationException {
		final Map<Identifier, StorableObjectVersion> remoteVersions
				= new HashMap<Identifier, StorableObjectVersion>();
		for (final Identifier id : ids) {
			remoteVersions.put(id, ILLEGAL_VERSION);
		}
		return remoteVersions;
	}

	public void saveStorableObjects(
			final Set<StorableObject> storableObjects)
	throws ApplicationException {
		// empty
	}

	public void delete(
			final Set<? extends Identifiable> identifiables)
	throws ApplicationException {
		// empty
	}

	public Set<Identifier> loadIdentifiersButIdsByCondition(
			final Set<Identifier> ids,
			final StorableObjectCondition condition)
	throws ApplicationException {
		return Collections.emptySet();
	}
}
