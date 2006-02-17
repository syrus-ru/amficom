/*
 * $Id: TestTransmissionPath.java,v 1.8.2.1 2006/02/17 12:28:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.8.2.1 $, $Date: 2006/02/17 12:28:06 $
 * @author $Author: arseniy $
 * @module test
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

	public void testCreateAll() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.TRANSPATH_TYPE_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final TransmissionPathType transmissionPathType = (TransmissionPathType) it.next();

		ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		ec = new EquivalentCondition(ObjectEntities.PORT_CODE);
		final Set<Port> ports = StorableObjectPool.getStorableObjectsByCondition(ec, true);

		Port finishPort = null;
		for (final Port port : ports) {
			if (port.getDescription().equals("finish")) {
				finishPort = port;
				break;
			}
		}
		if (finishPort == null) {
			fail("Cannot find port 'finish'");
		}

		for (final Port port : ports) {
			final String portDescription = port.getDescription();
			if (portDescription.equals("finish")) {
				continue;
			}

			final int p1 = portDescription.indexOf(SEPARATOR);
			final int p2 = portDescription.indexOf(SEPARATOR, p1 + 1);
			final int n = Integer.parseInt(portDescription.substring(p1 + 1, p2));
			final String transmissionPathDescription = TRANSMISSIONPATH + SEPARATOR + n
					+ SEPARATOR
					+ portDescription;
			TransmissionPath.createInstance(userId,
					domain.getId(),
					"Путь передачи данных " + n,
					transmissionPathDescription,
					transmissionPathType,
					port.getId(),
					finishPort.getId());
		}

		StorableObjectPool.flush(ObjectEntities.TRANSMISSIONPATH_CODE, userId, true);
	}
}
