/*-
 * $Id: CorbaTestContext.java,v 1.1.2.1 2006/04/18 10:30:29 saa Exp $
 * 
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import junit.framework.Assert;

import com.syrus.AMFICOM.general.ClientSessionEnvironment.SessionKind;
import com.syrus.util.Log;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/18 10:30:29 $
 * @module
 */
public class CorbaTestContext implements TestContext {
	// @todo: add ctor or factory

	private static void login() {
		try {
			ClientSessionEnvironment.createInstance(SessionKind.ALL, null);
			ClientSessionEnvironment.getInstance().login("sys", "sys", new Identifier("Domain_1"));
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

	public void setUp() {
		login();
	}

	public void tearDown() {
		logout();
	}
}
