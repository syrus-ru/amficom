/*-
 * $Id: ResourceDatabaseContext.java,v 1.4 2005/04/01 11:11:38 bass Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/04/01 11:11:38 $
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
				return getImageResourceDatabase();
			default:
				Log.errorMessage("ResourceDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;
		}
	}

	public static ImageResourceDatabase getImageResourceDatabase() {
		return imageResourceDatabase;
	}
}
