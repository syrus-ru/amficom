/*-
 * $Id: MapViewDatabaseContext.java,v 1.2 2005/04/01 14:35:54 bass Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/04/01 14:35:54 $
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
				return getMapViewDatabase();
			default:
				Log.errorMessage("MapViewDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;
		}
	}

	public static MapViewDatabase getMapViewDatabase() {
		return mapViewDatabase;
	}
}
