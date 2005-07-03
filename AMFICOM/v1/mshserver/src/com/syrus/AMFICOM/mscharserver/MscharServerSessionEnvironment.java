/*-
 * $Id: MscharServerSessionEnvironment.java,v 1.2 2005/06/07 17:58:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/06/07 17:58:13 $
 * @module mscharserver_v1
 */
final class MscharServerSessionEnvironment extends BaseSessionEnvironment {
	private static MscharServerSessionEnvironment instance;

	private MscharServerSessionEnvironment(
			final MscharServerServantManager mscharServerServantManager,
			final MscharServerPoolContext mscharServerPoolContext) {
		super(mscharServerServantManager, mscharServerPoolContext, new MapSchemeAdministrationResourceServer.MscharServerLoginRestorer());
	}

	public MscharServerServantManager getMscharServerServantManager() {
		return (MscharServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		instance = new MscharServerSessionEnvironment(
				MscharServerServantManager.createAndStart(serverHostName),
				new MscharServerPoolContext());
	}

	public static MscharServerSessionEnvironment getInstance() {
		return instance;
	}
}
