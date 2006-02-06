/*
 * $Id: TestPortType.java,v 1.3 2005/08/19 15:55:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestPortType extends TestCase {

	public TestPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestPortType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final PortType portType = PortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				PortTypeCodename.FC_PC.stringValue(),
				"FC/PC",
				"FC/PC",
				PortTypeSort.PORTTYPESORT_OPTICAL,
				PortTypeKind.PORT_KIND_SIMPLE);
		StorableObjectPool.flush(portType, DatabaseCommonTest.getSysUser().getId(), false);
	}

}
