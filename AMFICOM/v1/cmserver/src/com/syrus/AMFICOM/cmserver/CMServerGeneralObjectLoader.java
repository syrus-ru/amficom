/*
 * $Id: CMServerGeneralObjectLoader.java,v 1.1 2005/04/01 10:38:13 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Collections;
import java.util.Set;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/01 10:38:13 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public class CMServerGeneralObjectLoader {

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
