/*
 * $Id: TestDomain.java,v 1.4 2005/08/18 10:40:08 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.administration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/18 10:40:08 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestDomain extends TestCase {

	public TestDomain(String name) {
		super(name);
	}

	public static Test suite() {
		DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();
		databaseCommonTest.addTestSuite(TestDomain.class);
		return databaseCommonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Domain domain = Domain.createInstance(DatabaseCommonTest.getSysUser().getId(),
				Identifier.VOID_IDENTIFIER,
				"�������� �����",
				"������ ����� � ��������");
		StorableObjectPool.flush(domain, DatabaseCommonTest.getSysUser().getId(), true);
	}
}
