/*-
 * $Id: CMServerSessionEnvironment.java,v 1.8 2005/10/11 14:33:36 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.8 $, $Date: 2005/10/11 14:33:36 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module cmserver
 */
final class CMServerSessionEnvironment extends BaseSessionEnvironment {
	private static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";
	private static final String KEY_REFRESH_TIMEOUT = "RefreshPoolTimeout";

	private static final String DATABASE_LOADER_ONLY = "false";
	private static final int REFRESH_TIMEOUT = 30; //sec

	private static CMServerSessionEnvironment instance;

	private CMServerSessionEnvironment(final CMServerServantManager cmServerServantManager,
			final CMServerPoolContext cmServerPoolContext) {
		super(cmServerServantManager, cmServerPoolContext);
	}

	public CMServerServantManager getCMServerServantManager() {
		return (CMServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName, final LoginRestorer loginRestorer) throws CommunicationException {
		final CMServerServantManager cmServerServantManager = CMServerServantManager.createAndStart(serverHostName);

		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY,
				DATABASE_LOADER_ONLY)).booleanValue();
		ObjectLoader objectLoader;
		if (!databaseLoaderOnly) {
			final long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, REFRESH_TIMEOUT) * 1000L;
			objectLoader = new CMServerObjectLoader(cmServerServantManager, refreshTimeout, loginRestorer);
		} else {
			objectLoader = new DatabaseObjectLoader();
		}
		final CMServerPoolContext cmServerPoolContext = new CMServerPoolContext(objectLoader);

		instance = new CMServerSessionEnvironment(cmServerServantManager, cmServerPoolContext);
	}

	public static CMServerSessionEnvironment getInstance() {
		return instance;
	}

}
