/*-
 * $Id: ObjectLoader.java,v 1.7 2005/09/14 18:51:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface ObjectLoader {

	<T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException;

	<T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException;

	Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException;

	void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException;

	Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException;

	void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException;

}
