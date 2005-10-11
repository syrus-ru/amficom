/*-
 * $Id: MServerSessionEnvironment.java,v 1.8 2005/10/11 14:33:25 arseniy Exp $
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
import com.syrus.AMFICOM.general.LoginRestorer;

/**
 * @version $Revision: 1.8 $, $Date: 2005/10/11 14:33:25 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerSessionEnvironment extends BaseSessionEnvironment {
	private static MServerSessionEnvironment instance;

	private MServerSessionEnvironment(final MServerServantManager mServerServantManager, final MServerPoolContext mServerPoolContext) {
		super(mServerServantManager, mServerPoolContext);
	}

	public MServerServantManager getMServerServantManager() {
		return (MServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName, final Set<Identifier> mcmIds, final LoginRestorer loginRestorer)
			throws ApplicationException {
		final MServerServantManager mServerServantManager = MServerServantManager.createAndStart(serverHostName, mcmIds);

		final MServerObjectLoader mServerObjectLoader = new MServerObjectLoader(loginRestorer);
		final MServerPoolContext mServerPoolContext = new MServerPoolContext(mServerObjectLoader);

		instance = new MServerSessionEnvironment(mServerServantManager, mServerPoolContext);
	}

	public static MServerSessionEnvironment getInstance() {
		return instance;
	}
}
