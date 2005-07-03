/*-
 * $Id: DatabaseMapViewObjectLoader.java,v 1.4 2005/06/22 19:29:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/06/22 19:29:32 $
 * @module csbridge_v1
 */
public class DatabaseMapViewObjectLoader extends DatabaseObjectLoader implements MapViewObjectLoader {
	public final Set loadMapViews(final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public final Set loadMapViewsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids); 
	}

	public final void saveMapViews(final Set<MapView> objects, final boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
