/*-
 * $Id: EmptySchemeObjectLoader.java,v 1.7 2005/12/14 11:35:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
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
 * @version $Revision: 1.7 $, $Date: 2005/12/14 11:35:46 $
 * @module scheme
 */
final class EmptySchemeObjectLoader implements ObjectLoader {
	public <T extends StorableObject<T>> Set<T> loadStorableObjects(Set<Identifier> ids) throws ApplicationException {
		return Collections.<T>emptySet();
	}

	public <T extends StorableObject<T>> Set<T> loadStorableObjectsButIdsByCondition(Set<Identifier> ids, StorableObjectCondition condition) throws ApplicationException {
		return Collections.<T>emptySet();
	}

	public Map<Identifier, StorableObjectVersion> getRemoteVersions(Set<Identifier> ids) throws ApplicationException {
		return Collections.emptyMap();
	}

	public void saveStorableObjects(Set<StorableObject> storableObjects) throws ApplicationException {
		// empty
	}

	public void delete(Set<? extends Identifiable> identifiables) throws ApplicationException {
		// empty
	}

	public Set<Identifier> loadIdentifiersButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition)
	throws ApplicationException {
		return Collections.emptySet();
	}
}
