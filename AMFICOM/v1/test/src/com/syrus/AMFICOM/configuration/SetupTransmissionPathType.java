/*
 * $Id: SetupTransmissionPathType.java,v 1.1.2.1 2006/02/22 15:00:58 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.setup.I18N;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/22 15:00:58 $
 * @author $Author: arseniy $
 * @module test
 */
public class SetupTransmissionPathType extends TestCase {
	private static String KEY_NAME_ROOT = "Name.TransmissionPathType.";
	private static String KEY_DESCRIPTION_ROOT = "Description.TransmissionPathType.";

	public SetupTransmissionPathType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupTransmissionPathType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final Set<TransmissionPathType> transmissionPathTypes = new HashSet<TransmissionPathType>();
		for (final TransmissionPathTypeCodename transmissionPathTypeCodename : TransmissionPathTypeCodename.values()) {
			final String codename = transmissionPathTypeCodename.stringValue();
			transmissionPathTypes.add(TransmissionPathType.createInstance(creatorId,
					codename,
					I18N.getString(KEY_DESCRIPTION_ROOT + codename),
					I18N.getString(KEY_NAME_ROOT + codename)));
		}
		StorableObjectPool.flush(transmissionPathTypes, creatorId, true);
	}

}
