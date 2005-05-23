/*-
 * $Id: MapViewDatabaseContext.java,v 1.4 2005/05/23 18:45:17 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/23 18:45:17 $
 * @author $Author: bass $
 * @module mapview_v1
 */
public final class MapViewDatabaseContext {
	private static MapViewDatabase mapViewDatabase;

	private MapViewDatabaseContext() {
		assert false;
	}

	public static void init(
			final MapViewDatabase mapViewDatabase1) {
		if (mapViewDatabase1 != null)
			mapViewDatabase = mapViewDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				return mapViewDatabase;
			default:
				Log.errorMessage("MapViewDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
