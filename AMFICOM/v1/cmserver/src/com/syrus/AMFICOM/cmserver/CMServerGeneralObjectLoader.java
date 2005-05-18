/*
 * $Id: CMServerGeneralObjectLoader.java,v 1.2 2005/05/18 13:11:21 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:11:21 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public class CMServerGeneralObjectLoader extends DatabaseGeneralObjectLoader {

	 /**
	 * refresh timeout
	 */
	private long refreshTimeout;

	public CMServerGeneralObjectLoader(long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
	}

	public Set refresh(Set storableObjects) {
		/**
		 * there is no reason to refresh because configuration entities couldn't change out of cmserver
		 */
		return Collections.EMPTY_SET;
	}
}
