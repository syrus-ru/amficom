/*-
 * $Id: MscharServerSessionEnvironment.java,v 1.6 2005/10/21 12:04:23 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/10/21 12:04:23 $
 * @module mscharserver
 */
final class MscharServerSessionEnvironment extends BaseSessionEnvironment {
	private static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";
	private static final String KEY_REFRESH_TIMEOUT = "RefreshPoolTimeout";

	private static final String DATABASE_LOADER_ONLY = "false";
	private static final int REFRESH_TIMEOUT = 30; //sec

	private static MscharServerSessionEnvironment instance;

	private MscharServerSessionEnvironment(final MscharServerServantManager mscharServerServantManager,
			final MscharServerPoolContext mscharServerPoolContext) {
		super(mscharServerServantManager,
				mscharServerPoolContext,
				new MapSchemeAdministrationResourceServer.MscharServerLoginRestorer());
	}

	public MscharServerServantManager getMscharServerServantManager() {
		return (MscharServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final MscharServerServantManager mscharServerServantManager = MscharServerServantManager.createAndStart(serverHostName);

		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY,
				DATABASE_LOADER_ONLY)).booleanValue();
		ObjectLoader objectLoader;
		if (!databaseLoaderOnly) {
			final long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, REFRESH_TIMEOUT) * 1000L;
			objectLoader = new MscharServerObjectLoader(refreshTimeout);
		} else {
			objectLoader = new DatabaseObjectLoader();
		}
		final MscharServerPoolContext mscharServerPoolContext = new MscharServerPoolContext(objectLoader);

		instance = new MscharServerSessionEnvironment(mscharServerServantManager, mscharServerPoolContext);
	}

	public static MscharServerSessionEnvironment getInstance() {
		return instance;
	}
}
