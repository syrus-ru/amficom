/*-
 * $Id: TestProtoEquipment.java,v 1.2 2005/12/15 13:53:08 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/12/15 13:53:08 $
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
		final EquipmentType equipmentType = EquipmentType.REFLECTOMETER;

		final ProtoEquipment protoEquipment = ProtoEquipment.createInstance(DatabaseCommonTest.getSysUser().getId(),
				equipmentType,
				"Нэт-тэст",
				"Рефлектометр с синей лампочкой на морде",
				"Кооператив Нэт-тэст",
				"15A30");
		StorableObjectPool.flush(protoEquipment, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
