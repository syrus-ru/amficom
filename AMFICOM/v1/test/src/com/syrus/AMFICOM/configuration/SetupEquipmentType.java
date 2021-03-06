/*-
 * $Id: SetupEquipmentType.java,v 1.3 2006/04/26 09:35:14 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

/**
 * @version $Revision: 1.3 $, $Date: 2006/04/26 09:35:14 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupEquipmentType extends TestCase {
	private static String KEY_DESCRIPTION_ROOT = "Description.EquipmentType.";

	public SetupEquipmentType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupEquipmentType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final Set<EquipmentType> equipmentTypes = new HashSet<EquipmentType>();
		for (final EquipmentTypeCodename equipmentTypeCodename : EquipmentTypeCodename.values()) {
			final String codename = equipmentTypeCodename.stringValue();
			equipmentTypes.add(EquipmentType.createInstance(creatorId,
					codename,
					I18N.getString(KEY_DESCRIPTION_ROOT + codename)));
		}

		StorableObjectPool.flush(equipmentTypes, creatorId, false);
	}
}
