/*
 * $Id: CORBACommonTest.java,v 1.8 2006/04/18 06:42:57 saa Exp $
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
 * @version $Revision: 1.8 $, $Date: 2006/04/18 06:42:57 $
 * @author $Author: saa $
 * @module test
 */
public class CORBACommonTest extends CommonTest {

	@Override
	public void oneTimeSetUp() {
		super.oneTimeSetUp();
		login();
	}

	@Override
	public void oneTimeTearDown() {
		logout();
		super.oneTimeTearDown();
	}

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
}
