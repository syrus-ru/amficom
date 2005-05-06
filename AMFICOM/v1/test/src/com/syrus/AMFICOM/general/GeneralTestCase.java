/*
 * $Id: GeneralTestCase.java,v 1.3 2005/05/06 16:07:40 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.util.Application;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/06 16:07:40 $
 * @author $Author: bob $
 * @module tools
 */
public class GeneralTestCase extends TestCase {

	public static Identifier	domainId;

	protected static Identifier	creatorId;
	
	public static final String APPLICATION_NAME = "cmserver";
	
	public GeneralTestCase(String name) {
		super(name);
	}

	public static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() throws CommunicationException, LoginException {
				oneTimeSetUp();
			}

			protected void tearDown() {
				oneTimeTearDown();
			}
		};
		return wrapper;
	}

	static void oneTimeSetUp() throws CommunicationException, LoginException {
		Application.init("scheduler");
		final String login = "sys";
		final String password = "sys";
		LoginRestorer loginRestorer = new LoginRestorer() {

			/* TODO just dummy login restorer */
			public String getLogin() {
				return login;
			}

			public String getPassword() {
				return password;
			}

			public boolean restoreLogin() {
				return true;
			}
		};

		ClientSessionEnvironment.createInstance(ClientSessionEnvironment.SESSION_KIND_MEASUREMENT,
			loginRestorer);
		
		final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();
		clientSessionEnvironment.login(login, password);
		Set availableDomains = LoginManager.getAvailableDomains();
		creatorId = LoginManager.getUserId();
		Domain domain = (Domain) availableDomains.iterator().next();
		domainId = domain.getId();
		LoginManager.selectDomain(domainId);
		
	}

	static void oneTimeTearDown() {
		try {
			GeneralStorableObjectPool.flush(true);
			AdministrationStorableObjectPool.flush(true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseConnection.closeConnection();
	}
	
}
