/*
 * $Id: CORBACommonTest.java,v 1.6 2005/12/09 14:50:29 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.ClientSessionEnvironment.SessionKind;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6 $, $Date: 2005/12/09 14:50:29 $
 * @author $Author: arseniy $
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