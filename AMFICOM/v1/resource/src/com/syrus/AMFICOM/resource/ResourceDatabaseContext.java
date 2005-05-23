/*-
 * $Id: ResourceDatabaseContext.java,v 1.6 2005/05/23 18:45:11 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/23 18:45:11 $
 * @module resource_v1
 */
public final class ResourceDatabaseContext {
	private static ImageResourceDatabase imageResourceDatabase;

	private ResourceDatabaseContext() {
		assert false;
	}

	public static void init(
			final ImageResourceDatabase imageResourceDatabase1) {
		if (imageResourceDatabase1 != null)
			imageResourceDatabase = imageResourceDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				return imageResourceDatabase;
			default:
				Log.errorMessage("ResourceDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
