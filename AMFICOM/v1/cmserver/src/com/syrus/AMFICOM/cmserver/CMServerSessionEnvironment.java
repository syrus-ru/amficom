/*
 * $Id: CMServerSessionEnvironment.java,v 1.1 2005/05/01 17:23:16 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/01 17:23:16 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
final class CMServerSessionEnvironment extends BaseSessionEnvironment {
	private static CMServerSessionEnvironment instance;

	private CMServerSessionEnvironment(CMServerServantManager cmServerServantManager, CMServerPoolContext cmServerPoolContext) {
		super(cmServerServantManager, cmServerPoolContext);
	}

	public CMServerServantManager getCMServerServantManager() {
		return (CMServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(String serverHostName) throws CommunicationException {
		CMServerServantManager cmServerServantManager = CMServerServantManager.createAndStart(serverHostName);
		instance = new CMServerSessionEnvironment(cmServerServantManager, new CMServerPoolContext());
	}

	public static CMServerSessionEnvironment getInstance() {
		return instance;
	}

}
