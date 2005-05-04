/*
 * $Id: MServerSessionEnvironment.java,v 1.2 2005/05/04 11:37:07 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/04 11:37:07 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
final class MServerSessionEnvironment extends BaseSessionEnvironment {
	private static MServerSessionEnvironment instance;

	private MServerSessionEnvironment(MServerServantManager mServerServantManager, MServerPoolContext mServerPoolContext) {
		super(mServerServantManager, mServerPoolContext, new MeasurementServer.MServerLoginRestorer());
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
