/*-
 * $Id: TestProtoEquipment.java,v 1.1 2005/09/28 13:24:51 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Iterator;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.1 $, $Date: 2005/09/28 13:24:51 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestProtoEquipment extends TestCase {

	public TestProtoEquipment(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestProtoEquipment.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
		final Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final EquipmentType equipmentType = (EquipmentType) it.next();

		final ProtoEquipment protoEquipment = ProtoEquipment.createInstance(DatabaseCommonTest.getSysUser().getId(),
				equipmentType,
				"Нэт-тэст",
				"Рефлектометр с синей лампочкой на морде",
				"Кооператив Нэт-тэст",
				"15A30");
		StorableObjectPool.flush(protoEquipment, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
