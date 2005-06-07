/*-
 * $Id: MClientXMLServantManager.java,v 1.1 2005/06/07 13:28:24 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;



/**
 * @version $Revision: 1.1 $, $Date: 2005/06/07 13:28:24 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
public final class MClientXMLServantManager extends XMLClientServantManager implements BaseConnectionManager {

	private MClientXMLServantManager() {
		// nothing
	}

	public static MClientXMLServantManager create() {
		final MClientXMLServantManager mClientServantManager = new MClientXMLServantManager();
		return mClientServantManager;
	}
	
	public CORBAServer getCORBAServer() {
		// TODO Auto-generated method stub
		return null;
	}	
}
