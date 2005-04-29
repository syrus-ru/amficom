/*
 * $Id: BaseSessionEnvironment.java,v 1.1 2005/04/29 12:13:14 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/29 12:13:14 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class BaseSessionEnvironment {
	protected BaseConnectionManager baseConnectionManager;
	private PoolContext poolContext;

	public BaseSessionEnvironment(BaseConnectionManager baseConnectionManager, PoolContext poolContext) {
		this.baseConnectionManager = baseConnectionManager;
		this.poolContext = poolContext;

		LoginManager.init(this.baseConnectionManager);
		IdentifierPool.init(this.baseConnectionManager);
		this.poolContext.init();
	}

	public BaseConnectionManager getConnectionManager() {
		return this.baseConnectionManager;
	}

	public void login(String login, String password) throws CommunicationException, LoginException {
		LoginManager.login(login, password);
		this.poolContext.deserialize();
	}

	public void logout() throws CommunicationException, LoginException {
		LoginManager.logout();
		this.poolContext.deserialize();
	}
}
