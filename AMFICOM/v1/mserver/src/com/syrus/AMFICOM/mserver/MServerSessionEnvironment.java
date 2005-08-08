/*-
 * $Id: MServerSessionEnvironment.java,v 1.6 2005/08/08 11:43:53 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:43:53 $
 * @author $Author: arseniy $
 * @module mserver
 */
final class MServerSessionEnvironment extends BaseSessionEnvironment {
	private static MServerSessionEnvironment instance;

	private MServerSessionEnvironment(final MServerServantManager mServerServantManager, final MServerPoolContext mServerPoolContext) {
		super(mServerServantManager, mServerPoolContext, new MeasurementServer.MServerLoginRestorer());
	}

	public MServerServantManager getMServerServantManager() {
		return (MServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName, final Set<Identifier> mcmIds) throws ApplicationException {
		final MServerServantManager mServerServantManager = MServerServantManager.createAndStart(serverHostName, mcmIds);
		instance = new MServerSessionEnvironment(mServerServantManager, new MServerPoolContext());
	}

	public static MServerSessionEnvironment getInstance() {
		return instance;
	}
}
