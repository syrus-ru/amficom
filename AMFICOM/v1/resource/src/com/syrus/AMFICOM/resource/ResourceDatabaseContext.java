/*
 * $Id: ResourceDatabaseContext.java,v 1.3 2005/02/08 10:24:54 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/02/08 10:24:54 $
 * @module resource_v1
 */
public final class ResourceDatabaseContext {
	private static StorableObjectDatabase imageResourceDatabase;

	private ResourceDatabaseContext() {
		// singleton
	}

	public static void init(final StorableObjectDatabase newImageResourceDatabase) {
		imageResourceDatabase = newImageResourceDatabase;
	}

	public static StorableObjectDatabase getImageResourceDatabase() {
		return imageResourceDatabase;
	}
}
