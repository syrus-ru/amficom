/*
 * $Id: TestTransmissionPath.java,v 1.1 2005/02/16 21:25:52 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/16 21:25:52 $
 * @author $Author: cvsadmin $
 * @module config_v1
 */
public class TestTransmissionPath extends CommonConfigurationTest {

	public TestTransmissionPath(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestTransmissionPath.class);
	}

	public void testCreateInstance() throws ApplicationException {
		AccessIdentity accessIdentity = SessionContext.getAccessIdentity();

		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
		Iterator it = ConfigurationStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		TransmissionPathType transmissionPathType = (TransmissionPathType) it.next();

		ec = new EquivalentCondition(ObjectEntities.PORT_ENTITY_CODE);
		Collection ports = ConfigurationStorableObjectPool.getStorableObjectsByCondition(ec, true);
		it = ports.iterator();
		Port startPort = (Port) it.next();
		Port finishPort = (Port) it.next();
		
		TransmissionPath transmissionPath = TransmissionPath.createInstance(accessIdentity.getUserId(),
				accessIdentity.getDomainId(),
				"A test transmission path",
				"Created for tests",
				transmissionPathType,
				startPort.getId(),
				finishPort.getId());

		TransmissionPath_Transferable tpt = (TransmissionPath_Transferable) transmissionPath.getTransferable();

		TransmissionPath transmissionPath1 = new TransmissionPath(tpt);
		assertEquals(transmissionPath.getId(), transmissionPath1.getId());
		assertEquals(transmissionPath.getCreated(), transmissionPath1.getCreated());
		assertEquals(transmissionPath.getModified(), transmissionPath1.getModified());
		assertEquals(transmissionPath.getCreatorId(), transmissionPath1.getCreatorId());
		assertEquals(transmissionPath.getModifierId(), transmissionPath1.getModifierId());
		assertEquals(transmissionPath.getVersion(), transmissionPath1.getVersion());
		assertEquals(transmissionPath.getDomainId(), transmissionPath1.getDomainId());
		assertEquals(transmissionPath.getName(), transmissionPath1.getName());
		assertEquals(transmissionPath.getDescription(), transmissionPath1.getDescription());
		assertEquals(transmissionPath.getStartPortId(), transmissionPath1.getStartPortId());
		assertEquals(transmissionPath.getFinishPortId(), transmissionPath1.getFinishPortId());

		ConfigurationStorableObjectPool.putStorableObject(transmissionPath);
		ConfigurationStorableObjectPool.flush(true);
	}
}
