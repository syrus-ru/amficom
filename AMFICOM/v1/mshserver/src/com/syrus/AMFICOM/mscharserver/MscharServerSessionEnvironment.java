/*-
 * $Id: MscharServerSessionEnvironment.java,v 1.8 2006/05/11 11:46:32 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerPOA;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerPOATie;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/05/11 11:46:32 $
 * @module mscharserver
 */
final class MscharServerSessionEnvironment extends BaseSessionEnvironment {
	private static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";
	private static final String KEY_REFRESH_TIMEOUT = "RefreshPoolTimeout";

	private static final String DATABASE_LOADER_ONLY = "false";
	private static final int REFRESH_TIMEOUT = 30; //sec

	private static MscharServerSessionEnvironment instance;

	private MscharServerSessionEnvironment(final MscharServerServantManager mscharServerServantManager,
			final MscharServerPoolContext mscharServerPoolContext,
			final MscharServer mscharServer) {
		super(mscharServerServantManager,
				mscharServerPoolContext,
				mscharServer,
				new MapSchemeAdministrationResourceServer.MscharServerLoginRestorer());
	}

	public MscharServerServantManager getMscharServerServantManager() {
		return (MscharServerServantManager) this.getConnectionManager();
	}

	public static void createInstance(final String serverHostName, final String servantName) throws CommunicationException {
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

		final CORBAServer corbaServer = mscharServerServantManager.getCORBAServer();
		final MscharServerPOA servant = new MscharServerPOATie(new MscharServerImpl(mscharServerServantManager), corbaServer.getPoa());
		corbaServer.activateServant(servant, servantName);
		corbaServer.printNamingContext();

		instance = new MscharServerSessionEnvironment(mscharServerServantManager,
				mscharServerPoolContext,
				servant._this(corbaServer.getOrb()));
	}

	public static MscharServerSessionEnvironment getInstance() {
		return instance;
	}
}
