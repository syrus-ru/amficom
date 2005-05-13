/*
 * $Id: ARServerResourceObjectLoader.java,v 1.2 2005/05/13 17:41:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/13 17:41:55 $
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
