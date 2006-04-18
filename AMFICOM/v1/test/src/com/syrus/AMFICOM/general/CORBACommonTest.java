/*
 * $Id: CORBACommonTest.java,v 1.6.2.2 2006/04/18 06:40:22 saa Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ClientSessionEnvironment.SessionKind;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6.2.2 $, $Date: 2006/04/18 06:40:22 $
 * @author $Author: saa $
 * @module test
 */
public class CORBACommonTest extends CommonTest {

	@Override
	void oneTimeSetUp() {
		super.oneTimeSetUp();
		login();
	}

	@Override
	void oneTimeTearDown() {
		logout();
		super.oneTimeTearDown();
	}

	private static void login() {
		try {
			ClientSessionEnvironment.createInstance(SessionKind.MEASUREMENT, null);
			final Identifier domainId = LoginManager.getAvailableDomains().iterator().next().getId();
			ClientSessionEnvironment.getInstance().login("sys", "sys", domainId);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			Assert.fail(ae.getMessage());
		}
	}

	private static void logout() {
		try {
			ClientSessionEnvironment.getInstance().logout();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			Assert.fail(ae.getMessage());
		}
	}
}
