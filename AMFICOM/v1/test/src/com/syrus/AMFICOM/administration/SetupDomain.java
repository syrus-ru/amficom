/*
 * $Id: SetupDomain.java,v 1.2 2006/04/25 09:31:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/25 09:31:58 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupDomain extends TestCase {
	private static final String KEY_NAME = "Name.RootDomain";
	private static final String KEY_DESCRIPTION = "Description.RootDomain";

	public SetupDomain(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(SetupDomain.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final Domain domain = Domain.createInstance(creatorId,
				Identifier.VOID_IDENTIFIER,
				I18N.getString(KEY_NAME),
				I18N.getString(KEY_DESCRIPTION));
		StorableObjectPool.flush(domain, creatorId, true);
	}
}
