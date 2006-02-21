/*
 * $Id: SetupPortType.java,v 1.1.2.1 2006/02/21 15:53:40 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

public final class SetupPortType extends TestCase {
	private static String KEY_NAME_ROOT = "Name.PortType.";
	private static String KEY_DESCRIPTION_ROOT = "Description.PortType.";

	public SetupPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupPortType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final Set<PortType> portTypes = new HashSet<PortType>();
		for (final PortTypeCodename portTypeCodename : PortTypeCodename.values()) {
			final String codename = portTypeCodename.stringValue();
			portTypes.add(PortType.createInstance(creatorId,
					codename,
					I18N.getString(KEY_DESCRIPTION_ROOT + codename),
					I18N.getString(KEY_NAME_ROOT + codename),
					PortTypeSort.PORTTYPESORT_OPTICAL,
					PortTypeKind.PORT_KIND_SIMPLE));
		}
		StorableObjectPool.flush(portTypes, creatorId, false);
	}
}
