/*
 * $Id: ARServerResourceObjectLoader.java,v 1.1 2005/01/17 16:34:05 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/17 16:34:05 $
 * @author $Author: max $
 * @module arserver_v1
 */
public class ARServerResourceObjectLoader extends DatabaseResourceObjectLoader {
	
	 /**
     * refresh timeout 
     */
    private long refreshTimeout;    
    
	public ARServerResourceObjectLoader(long refreshTimeout) {
    	this.refreshTimeout = refreshTimeout;
    }
	
	// TODO: add methods
}
