/*
 * $Id: MServerSessionEnvironment.java,v 1.1 2005/04/29 16:00:54 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.BaseSessionEnvironment;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/29 16:00:54 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
final class MServerSessionEnvironment extends BaseSessionEnvironment {
	private static MServerSessionEnvironment instance;

	private MServerSessionEnvironment(MServerServantManager mServerServantManager, MServerPoolContext mServerPoolContext) {
		super(mServerServantManager, mServerPoolContext);
	}

	public MServerServantManager getMServerServantManager() {
		return (MServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(String serverHostName, Set mcmIds) throws ApplicationException {
		MServerServantManager mServerServantManager = MServerServantManager.createAndStart(serverHostName, mcmIds);
		instance = new MServerSessionEnvironment(mServerServantManager, new MServerPoolContext());
	}

	public static MServerSessionEnvironment getInstance() {
		return instance;
	}
}
