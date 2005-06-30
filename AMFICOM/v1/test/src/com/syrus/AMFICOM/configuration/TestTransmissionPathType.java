/*
 * $Id: TestTransmissionPathType.java,v 1.5 2005/06/30 08:00:03 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/30 08:00:03 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestTransmissionPathType extends TestCase {

	public TestTransmissionPathType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestTransmissionPathType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		TransmissionPathType transmissionPathType = TransmissionPathType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				"reflectometry",
				"For tests",
				"tptyp");

		StorableObjectPool.flush(transmissionPathType, true);
	}

	public void _testDelete() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.TRANSPATH_TYPE_CODE);
		final Set transmissionPathTypes = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final Iterator it = transmissionPathTypes.iterator(); it.hasNext();) {
			final TransmissionPathType transmissionPathType = (TransmissionPathType) it.next();
			System.out.println("Event source: " + transmissionPathType.getId());
		}
		StorableObjectPool.delete(transmissionPathTypes);
		StorableObjectPool.flush(ObjectEntities.TRANSPATH_TYPE_CODE, true);
	}
}
