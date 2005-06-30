/*
 * $Id: CORBACommonTest.java,v 1.4 2005/06/30 07:54:03 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.cmserver.corba.CMServerHelper;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/06/30 07:54:03 $
 * @author $Author: arseniy $
 * @module test
 */
public class CORBACommonTest extends CommonTest {
	public static final String KEY_SERVER_HOST_NAME = "ServerHostName";
	private static final String KEY_LOGIN = "Login";
	private static final String KEY_PASSWORD = "Password";

	public static final String SERVER_HOST_NAME = "localhost";
	private static final String LOGIN = "cmserver";
	private static final String PASSWORD = "CMServer";

	private static LoginServer loginServerRef;
	private static IdlSessionKey idlSessionKey;
	private static MServer mServerRef;
	private static CMServer cmServerRef;

	public static IdlSessionKey getIdlSessionKey() {
		return idlSessionKey;
	}

	public static MServer getMServerRef() {
		return mServerRef;
	}

	public static CMServer getCMServerRef() {
		return cmServerRef;
	}

	void oneTimeSetUp() {
		super.oneTimeSetUp();

		try {

			final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
			final String contextName = ContextNameFactory.generateContextName(serverHostName);
			final CORBAServer corbaServer = new CORBAServer(contextName);

			loginServerRef = (LoginServer) corbaServer.resolveReference(ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
			final String login = ApplicationProperties.getString(KEY_LOGIN, LOGIN);
			final String password = ApplicationProperties.getString(KEY_PASSWORD, PASSWORD);
			final IdlIdentifierHolder userIdH = new IdlIdentifierHolder();
			idlSessionKey = loginServerRef.login(login, password, userIdH);

			mServerRef = MServerHelper.narrow(corbaServer.resolveReference(ServerProcessWrapper.MSERVER_PROCESS_CODENAME));

			cmServerRef = CMServerHelper.narrow(corbaServer.resolveReference(ServerProcessWrapper.CMSERVER_PROCESS_CODENAME));
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(0);
		}
	}

	void oneTimeTearDown() {
		try {
			loginServerRef.logout(idlSessionKey);
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		super.oneTimeTearDown();
	}
}
