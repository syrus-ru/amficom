/*
 * $Id: MCMSessionEnvironment.java,v 1.3 2005/05/04 11:38:44 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/04 11:38:44 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMSessionEnvironment extends BaseSessionEnvironment {
	private static MCMSessionEnvironment instance;

	private MCMSessionEnvironment(MCMServantManager mcmServantManager, MCMPoolContext mcmPoolContext) {
		super(mcmServantManager, mcmPoolContext, new MeasurementControlModule.MCMLoginRestorer());
	}

	public MCMServantManager getMCMServantManager() {
		return (MCMServantManager) super.baseConnectionManager;
	}

	public static void createInstance(String serverHostName) throws CommunicationException {
		MCMServantManager mcmServantManager = MCMServantManager.createAndStart(serverHostName);
		instance = new MCMSessionEnvironment(mcmServantManager, new MCMPoolContext());
	}

	public static MCMSessionEnvironment getInstance() {
		return instance;
	}
}
