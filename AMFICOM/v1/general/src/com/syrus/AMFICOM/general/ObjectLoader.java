/*-
 * $Id: ObjectLoader.java,v 1.5 2005/07/26 18:09:34 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.5 $, $Date: 2005/07/26 18:09:34 $
 * @author $Author: bass $
 * @module general
 */
public interface ObjectLoader {

	<T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException;

	<T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException;

	Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException;

	void saveStorableObjects(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException;

	void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException;

}
