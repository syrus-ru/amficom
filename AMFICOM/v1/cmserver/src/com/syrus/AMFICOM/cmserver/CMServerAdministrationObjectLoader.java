/*
 * $Id: CMServerAdministrationObjectLoader.java,v 1.2 2005/05/18 13:11:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:11:21 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public class CMServerAdministrationObjectLoader extends DatabaseAdministrationObjectLoader {

	 /**
	 * refresh timeout
	 */
	private long refreshTimeout;

	public CMServerAdministrationObjectLoader(long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
	}

	public Set refresh(Set storableObjects) {
		/**
		 * there is no reason to refresh because configuration entities couldn't change out of cmserver
		 */
		return Collections.EMPTY_SET;
	}
}
