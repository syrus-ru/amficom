/*
 * $Id: SetupTransmissionPath.java,v 1.1.2.1 2006/03/06 19:02:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.configuration.TransmissionPathTypeCodename.OPTICAL;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/03/06 19:02:21 $
 * @author $Author: arseniy $
 * @module test
 */
public class SetupTransmissionPath extends TestCase {

	public SetupTransmissionPath(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupTransmissionPath.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition domainCondition = new EquivalentCondition(DOMAIN_CODE);
		final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true);
		if (domains.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find Domains");
		}
		final Domain domain = domains.iterator().next();

		final TypicalCondition transmissionPathTypeCondition = new TypicalCondition(OPTICAL.stringValue(),
				OPERATION_EQUALS,
				TRANSPATH_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<TransmissionPathType> transmissionPathTypes = StorableObjectPool.getStorableObjectsByCondition(transmissionPathTypeCondition, true);
		assert transmissionPathTypes.size() == 1 : ONLY_ONE_EXPECTED;
		final TransmissionPathType transmissionPathType = transmissionPathTypes.iterator().next();

		final EquivalentCondition portCondition = new EquivalentCondition(PORT_CODE);
		final Set<Port> ports = StorableObjectPool.getStorableObjectsByCondition(portCondition, true);

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

		final Set<TransmissionPath> transmissionPaths = new HashSet<TransmissionPath>();
		for (final Port port : ports) {
			final String portDescription = port.getDescription();
			if (portDescription.equals("finish")) {
				continue;
			}

			final int p1 = portDescription.indexOf(SEPARATOR);
			final int p2 = portDescription.indexOf(SEPARATOR, p1 + 1);
			final int n = Integer.parseInt(portDescription.substring(p1 + 1, p2));
			final String transmissionPathDescription = TRANSMISSIONPATH + SEPARATOR + n + SEPARATOR + port.getEquipment().getDescription();
			transmissionPaths.add(TransmissionPath.createInstance(userId,
					domain.getId(),
					"Путь передачи данных " + n,
					transmissionPathDescription,
					transmissionPathType,
					port.getId(),
					finishPort.getId()));
		}

		StorableObjectPool.flush(transmissionPaths, userId, true);
	}
}
