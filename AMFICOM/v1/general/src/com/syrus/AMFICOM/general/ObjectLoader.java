/*-
 * $Id: ObjectLoader.java,v 1.2 2005/07/25 20:47:00 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.2 $, $Date: 2005/07/25 20:47:00 $
 * @author $Author: arseniy $
 * @module general
 */
public interface ObjectLoader {

	Set loadStorableObjects(final Set<Identifier> ids) throws ApplicationException;

	Set loadStorableObjectsButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException;

	Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException;

	void saveStorableObjects(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	Set<Identifier> refresh(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException;

	void delete(final Set<? extends Identifiable> identifiables);

}
