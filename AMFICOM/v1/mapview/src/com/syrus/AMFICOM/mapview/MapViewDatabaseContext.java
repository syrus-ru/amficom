/*
* $Id: MapViewDatabaseContext.java,v 1.1 2004/12/22 15:21:52 cvsadmin Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/


package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/22 15:21:52 $
 * @author $Author: cvsadmin $
 * @module mapview_v1
 */
public final class MapViewDatabaseContext {

	private static StorableObjectDatabase	mapViewDatabase;

	private MapViewDatabaseContext() {
		// empty singleton constructor
	}

	public static void init(final StorableObjectDatabase mapViewDatabase) {
		MapViewDatabaseContext.mapViewDatabase = mapViewDatabase;
	}

	public static StorableObjectDatabase getMapViewDatabase() {
		return mapViewDatabase;
	}
}
