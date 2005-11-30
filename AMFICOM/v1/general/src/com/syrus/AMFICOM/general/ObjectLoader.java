/*-
 * $Id: ObjectLoader.java,v 1.9 2005/11/30 15:37:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.9 $, $Date: 2005/11/30 15:37:14 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface ObjectLoader {

	<T extends StorableObject<T>> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException;

	<T extends StorableObject<T>> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException;

	Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException;

	void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException;

	void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException;

}
