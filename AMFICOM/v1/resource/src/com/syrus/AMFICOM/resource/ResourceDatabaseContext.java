/*
 * $Id: ResourceDatabaseContext.java,v 1.2 2005/01/17 17:15:55 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author $Author: max $
 * @version $Revision: 1.2 $, $Date: 2005/01/17 17:15:55 $
 * @module resource_v1
 */
public final class ResourceDatabaseContext {
	private static StorableObjectDatabase imageResourceDatabase;

	private ResourceDatabaseContext() {
	}

	public static void init(final StorableObjectDatabase newImageResourceDatabase) {
		imageResourceDatabase = newImageResourceDatabase;
	}

	public static StorableObjectDatabase getImageResourceDatabase() {
		return imageResourceDatabase;
	}
}
