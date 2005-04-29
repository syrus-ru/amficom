/*
 * $Id: BaseSessionEnvironment.java,v 1.2 2005/04/29 12:30:31 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/29 12:30:31 $
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
//
//	public PoolContext getPoolContext() {
//		return this.poolContext;
//	}

	public void login(String login, String password) throws CommunicationException, LoginException {
		LoginManager.login(login, password);
		this.poolContext.deserialize();
	}

	public void logout() throws CommunicationException, LoginException {
		LoginManager.logout();
		this.poolContext.serialize();
	}
}
