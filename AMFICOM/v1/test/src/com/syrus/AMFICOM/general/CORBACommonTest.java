/*
 * $Id: CORBACommonTest.java,v 1.7 2006/02/17 09:09:58 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.ClientSessionEnvironment.SessionKind;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.7 $, $Date: 2006/02/17 09:09:58 $
 * @author $Author: bob $
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
			System.exit(0);
		}
	}

	private static void logout() {
		try {
			ClientSessionEnvironment.getInstance().logout();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			System.exit(0);
		}
	}
}
