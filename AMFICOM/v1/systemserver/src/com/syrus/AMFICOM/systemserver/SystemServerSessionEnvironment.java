/*-
 * $Id: SystemServerSessionEnvironment.java,v 1.1.1.1 2006/06/26 10:49:41 cvsadmin Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;

import com.syrus.AMFICOM.general.AbstractSessionEnvironment;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CORBASystemUserImpl;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.systemserver.corba.CORBASystemUser;
import com.syrus.AMFICOM.systemserver.corba.CORBASystemUserPOA;
import com.syrus.AMFICOM.systemserver.corba.CORBASystemUserPOATie;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2006/06/26 10:49:41 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class SystemServerSessionEnvironment extends AbstractSessionEnvironment<SystemServerServantManager> {
	private static SystemServerSessionEnvironment instance;

	private SystemServerSessionEnvironment(final CORBASystemUser corbaSystemUser,
			final SystemServerLoginRestorer systemServerLoginRestorer) {
		super(SystemServerServantManager.getInstance(),
				SystemServerPoolContext.getInstance(),
				corbaSystemUser,
				systemServerLoginRestorer);
	}

	public static SystemServerSessionEnvironment getInstance() {
		if (instance != null) {
			return instance;
		}
		throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
	}

	public static void createInstance(final String serverHostName, final SystemServerLoginRestorer systemServerLoginRestorer)
			throws CommunicationException {
		SystemServerServantManager.createInstance(serverHostName);

		final CORBAServer corbaServer = SystemServerServantManager.getInstance().getCORBAServer();
		final CORBASystemUserPOA userServant = new CORBASystemUserPOATie(new CORBASystemUserImpl(), corbaServer.getPoa());

		instance = new SystemServerSessionEnvironment(userServant._this(corbaServer.getOrb()), systemServerLoginRestorer);
	}

	interface SystemServerLoginRestorer extends LoginRestorer {
		// Ничего нового
	}

}
