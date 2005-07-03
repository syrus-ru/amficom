/*
 * $Id: TestVerifiedConnectionManager.java,v 1.2 2005/06/19 18:43:56 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import java.util.Collections;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestVerifiedConnectionManager extends TestCase {
	private static final String KEY_SERVER_HOST_NAME = "ServerHostName";

	private static final String SERVER_HOST_NAME = "localhost";

	private static CORBAServer corbaServer;

	public TestVerifiedConnectionManager(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite(TestVerifiedConnectionManager.class);
		TestSetup testSetup = new TestSetup(testSuite) {
			protected void setUp() {
				oneTimeSetUp();
			}
			protected void tearDown() {
				oneTimeTearDown();
			}
		};
		return testSetup;
	}

	protected static void oneTimeSetUp() {
		System.out.println("********* one time set up **********");
		Application.init(APPLICATION_NAME);

		try {
			final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
			final String contextName = ContextNameFactory.generateContextName(serverHostName);
			corbaServer = new CORBAServer(contextName);
		}
		catch (Exception e) {
			Log.errorException(e);
			fail(e.getMessage());
		}
	}

	protected static void oneTimeTearDown() {
		//Nothing
	}

	public void testCreateAndStart() throws InterruptedException {
		RunnableVerifiedConnectionManager verifiedConnectionManager = new RunnableVerifiedConnectionManager(corbaServer,
				Collections.singleton(ServerProcessWrapper.LOGIN_PROCESS_CODENAME),
				10000L);
		(new Thread(verifiedConnectionManager)).start();
		while (true) {
			Thread.sleep(10000L);
		}
	}
}
