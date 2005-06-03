/*-
 * $Id: DatabaseMapViewObjectLoader.java,v 1.1 2005/06/03 14:53:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/03 14:53:14 $
 * @module csbridge_v1
 */
public class DatabaseMapViewObjectLoader extends DatabaseObjectLoader implements MapViewObjectLoader {
	public final Set loadMapViews(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public final Set loadMapViewsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids); 
	}

	public final void saveMapViews(final Set objects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}
}
