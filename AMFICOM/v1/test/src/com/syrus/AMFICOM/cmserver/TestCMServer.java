/*
 * $Id: TestCMServer.java,v 1.8 2005/07/11 10:28:45 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/11 10:28:45 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestCMServer extends TestCase {

	public TestCMServer(final String name) {
		super(name);
	}

	public static Test suite() {
		CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestCMServer.class);
		return commonTest.createTestSetup();
	}

	public void testTransmitAvailableDomains() throws AMFICOMRemoteException {
		final LoginServer loginServerRef = CORBACommonTest.getLoginServerRef();
		IdlDomain[] idlDomains = loginServerRef.transmitAvailableDomains(CORBACommonTest.getIdlSessionKey());
		for (int i = 0; i < idlDomains.length; i++) {
			System.out.println("Domain: " + idlDomains[i].name);
		}
	}

	public void _testTransmit() {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_TYPE_CODE);
		final CMServer cmServerRef = CORBACommonTest.getCMServerRef();
		try {
			final IdlMeasurementType[] idlMeasurementTypes = cmServerRef.transmitMeasurementTypesButIdsByCondition(new IdlIdentifier[0],
					ec.getTransferable(),
					CORBACommonTest.getIdlSessionKey());
			for (int i = 0; i < idlMeasurementTypes.length; i++) {
				System.out.println("Loaded: " + idlMeasurementTypes[i].codename);
			}
		}
		catch (AMFICOMRemoteException are) {
			System.out.println("AMFICOMRemoteException: " + are.message);
		}
	}
}
