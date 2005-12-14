/*-
 * $Id: ObjectLoader.java,v 1.10 2005/12/14 11:06:11 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.10 $, $Date: 2005/12/14 11:06:11 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface ObjectLoader {

	<T extends StorableObject<T>> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException;

	<T extends StorableObject<T>> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException;

	Set<Identifier> loadIdentifiersButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException;

	Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException;

	void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException;

	void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException;

}
