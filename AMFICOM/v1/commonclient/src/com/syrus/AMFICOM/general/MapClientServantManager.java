/*-
 * $Id: MapClientServantManager.java,v 1.1.2.1 2006/06/27 15:43:07 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.administration.ServerWrapper.EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_MAP_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_STORABLE_OBJECT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.MAP_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.STORABLE_OBJECT_SERVER_SERVICE_NAME;

import com.syrus.AMFICOM.systemserver.corba.MapServer;
import com.syrus.AMFICOM.systemserver.corba.MapServerHelper;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:43:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public final class MapClientServantManager extends ClientServantManager implements MapServerConnectionManager {
	private final String mapServerServantName;

	public MapClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String identifierGeneratorServerServantName,
			final String storableObjectServerServantName,
			final String mapServerServantName) {
		super(corbaServer,
				loginServerServantName,
				eventServerServantName,
				identifierGeneratorServerServantName,
				storableObjectServerServantName);
		this.mapServerServantName = mapServerServantName;
	}

	public final MapServer getMapServerReference() throws CommunicationException {
		return MapServerHelper.narrow(this.getVerifiableReference(this.mapServerServantName));
	}

	public static MapClientServantManager create() throws CommunicationException {
		final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(KEY_LOGIN_SERVER_SERVICE_NAME,
				LOGIN_SERVER_SERVICE_NAME);
		final String eventServerServantName = ApplicationProperties.getString(KEY_EVENT_SERVER_SERVICE_NAME,
				EVENT_SERVER_SERVICE_NAME);
		final String identifierGeneratorServerServantName = ApplicationProperties.getString(KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME,
				IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME);
		final String storableObjectServerServantName = ApplicationProperties.getString(KEY_STORABLE_OBJECT_SERVER_SERVICE_NAME,
				STORABLE_OBJECT_SERVER_SERVICE_NAME);
		final String mapServerServantName = ApplicationProperties.getString(KEY_MAP_SERVER_SERVICE_NAME, MAP_SERVER_SERVICE_NAME);

		final MapClientServantManager mscharClientServantManager = new MapClientServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				identifierGeneratorServerServantName,
				storableObjectServerServantName,
				mapServerServantName);
		return mscharClientServantManager;
	}
}
