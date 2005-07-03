/*
 * $Id: TestMServer.java,v 1.4 2005/06/28 15:28:25 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.corba.IdlKIS;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/28 15:28:25 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestMServer extends TestCase {

	public TestMServer(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestMServer.class);
		return commonTest.createTestSetup();
	}

	public void testTransmitKISsButIdsByCondition() throws AMFICOMRemoteException { 
		final Set<Identifier> ids = new HashSet<Identifier>();
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final LinkedIdsCondition lic = new LinkedIdsCondition(new Identifier("MCM_28"), ObjectEntities.KIS_CODE);
		final IdlStorableObjectCondition conditionT = lic.getTransferable();
		final IdlKIS[] kiss = CORBACommonTest.getMServerRef().transmitKISsButIdsByCondition(idsT,
				conditionT,
				CORBACommonTest.getIdlSessionKey());
		for (int i = 0; i < kiss.length; i++) {
			System.out.println("Loaded: " + kiss[i].name);
		}
	}

	public void _testTransmitMeasurements() throws AMFICOMRemoteException {
		final Set<Identifier> ids = new HashSet<Identifier>();
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		ids.add(new Identifier("Measurement_2494"));
		ids.add(new Identifier("Measurement_2754"));
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final IdlMeasurement[] measurementsT = CORBACommonTest.getMServerRef().transmitMeasurements(idsT,
				CORBACommonTest.getIdlSessionKey());
		for (int i = 0; i < measurementsT.length; i++) {
			final Identifier id = new Identifier(measurementsT[i].header.id);
			Log.debugMessage("Loaded: " + id, Log.DEBUGLEVEL02);
		}
	}

	public void _testTransmitMeasurementsButIdsByCondition() throws AMFICOMRemoteException {
		final Set<Identifier> ids = new HashSet<Identifier>();
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		ids.add(new Identifier("Measurement_2494"));
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_CODE);
		final IdlStorableObjectCondition conditionT = ec.getTransferable();
		final IdlMeasurement[] measurementsT = CORBACommonTest.getMServerRef().transmitMeasurementsButIdsByCondition(idsT,
				conditionT,
				CORBACommonTest.getIdlSessionKey());
		for (int i = 0; i < measurementsT.length; i++) {
			final Identifier id = new Identifier(measurementsT[i].header.id);
			Log.debugMessage("Loaded: " + id, Log.DEBUGLEVEL02);
		}
	}

}
