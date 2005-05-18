/*
 * $Id: ARServerResourceObjectLoader.java,v 1.3 2005/05/18 12:56:28 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 12:56:28 $
 * @author $Author: bass $
 * @module arserver_v1
 * @todo add methods.
 */
public class ARServerResourceObjectLoader extends DatabaseResourceObjectLoader {
	 /**
	  * refresh timeout
	  */
	private long refreshTimeout;

	public ARServerResourceObjectLoader(final long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
	}
}
