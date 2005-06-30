/*
 * $Id: TestTransmissionPath.java,v 1.5 2005/06/30 16:14:58 arseniy Exp $
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

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/30 16:14:58 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestTransmissionPath extends TestCase {

	public TestTransmissionPath(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestTransmissionPath.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.TRANSPATH_TYPE_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final TransmissionPathType transmissionPathType = (TransmissionPathType) it.next();

		ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		ec = new EquivalentCondition(ObjectEntities.PORT_CODE);
		Set ports = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		it = ports.iterator();
		final Port startPort = (Port) it.next();
		final Port finishPort = (Port) it.next();
		
		final TransmissionPath transmissionPath = TransmissionPath.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				"A test transmission path",
				"Created for tests",
				transmissionPathType,
				startPort.getId(),
				finishPort.getId());

		StorableObjectPool.flush(transmissionPath, true);
	}
}
