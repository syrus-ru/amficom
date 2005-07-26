/*-
 * $Id: ObjectLoader.java,v 1.4 2005/07/26 17:17:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.4 $, $Date: 2005/07/26 17:17:30 $
 * @author $Author: arseniy $
 * @module general
 */
public interface ObjectLoader {

	Set loadStorableObjects(final Set<Identifier> ids) throws ApplicationException;

	Set loadStorableObjectsButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException;

	Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException;

	void saveStorableObjects(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException;

	void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException;

}
