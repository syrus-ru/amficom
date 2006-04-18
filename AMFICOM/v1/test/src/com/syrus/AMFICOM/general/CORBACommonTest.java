/*
 * $Id: CORBACommonTest.java,v 1.6.2.3 2006/04/18 10:29:54 saa Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Assert;

import com.syrus.AMFICOM.general.ClientSessionEnvironment.SessionKind;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6.2.3 $, $Date: 2006/04/18 10:29:54 $
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
