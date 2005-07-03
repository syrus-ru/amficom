/*
 * $Id: CMServerAdministrationObjectLoader.java,v 1.3 2005/05/27 11:13:49 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/05/27 11:13:49 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public class CMServerAdministrationObjectLoader extends DatabaseAdministrationObjectLoader {
	public Set refresh(Set storableObjects) {
		/**
		 * there is no reason to refresh because configuration entities couldn't change out of cmserver
		 */
		return Collections.EMPTY_SET;
	}
}
