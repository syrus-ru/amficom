/*
 * $Id: ResourceDatabaseContext.java,v 1.1 2004/12/01 15:42:35 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/01 15:42:35 $
 * @module resource_v1
 */
final class ResourceDatabaseContext {
	private static StorableObjectDatabase imageResourceDatabase;

	private ResourceDatabaseContext() {
	}

	static void init(final StorableObjectDatabase newImageResourceDatabase) {
		imageResourceDatabase = newImageResourceDatabase;
	}

	static StorableObjectDatabase getImageResourceDatabase() {
		return imageResourceDatabase;
	}
}
