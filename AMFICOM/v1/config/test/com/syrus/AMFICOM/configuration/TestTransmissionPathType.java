/*
 * $Id: TestTransmissionPathType.java,v 1.1 2005/02/16 21:25:52 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/16 21:25:52 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestTransmissionPathType extends CommonConfigurationTest {

	public TestTransmissionPathType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestTransmissionPathType.class);
	}

	public void testCreateInstance() throws ApplicationException {
		TransmissionPathType transmissionPathType = TransmissionPathType.createInstance(SessionContext.getAccessIdentity().getUserId(),
				"tptyp", "For tests", "tptyp");

		TransmissionPathType_Transferable tptt = (TransmissionPathType_Transferable) transmissionPathType.getTransferable();

		TransmissionPathType transmissionPathType1 = new TransmissionPathType(tptt);
		assertEquals(transmissionPathType.getId(), transmissionPathType1.getId());
		assertEquals(transmissionPathType.getCreated(), transmissionPathType1.getCreated());
		assertEquals(transmissionPathType.getModified(), transmissionPathType1.getModified());
		assertEquals(transmissionPathType.getCreatorId(), transmissionPathType1.getCreatorId());
		assertEquals(transmissionPathType.getModifierId(), transmissionPathType1.getModifierId());
		assertEquals(transmissionPathType.getVersion(), transmissionPathType1.getVersion());
		assertEquals(transmissionPathType.getCodename(), transmissionPathType1.getCodename());
		assertEquals(transmissionPathType.getDescription(), transmissionPathType1.getDescription());
		assertEquals(transmissionPathType.getName(), transmissionPathType1.getName());

		ConfigurationStorableObjectPool.putStorableObject(transmissionPathType);
		ConfigurationStorableObjectPool.flush(true);
	}

//	public void testDelete() throws ApplicationException {
//		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
//		Collection transmissionPathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(ec, true);
//		TransmissionPathType transmissionPathType;
//		for (Iterator it = transmissionPathTypes.iterator(); it.hasNext();) {
//			transmissionPathType = (TransmissionPathType) it.next();
//			System.out.println("Event source: " + transmissionPathType.getId());
//		}
//		ConfigurationStorableObjectPool.delete(transmissionPathTypes);
//		ConfigurationStorableObjectPool.flush(true);
//	}
}
