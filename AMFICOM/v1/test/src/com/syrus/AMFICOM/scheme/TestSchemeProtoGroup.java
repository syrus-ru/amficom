/*-
 * $Id: TestSchemeProtoGroup.java,v 1.1 2005/07/12 15:53:33 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.1 $, $Date: 2005/07/12 15:53:33 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestSchemeProtoGroup extends TestCase {

	public TestSchemeProtoGroup(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestSchemeProtoGroup.class);
		return commonTest.createTestSetup();
	}

	public void testRetrieve() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
		final Set<SchemeProtoGroup> set = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (SchemeProtoGroup schemeProtoGroup : set) {
			System.out.println("group: " + schemeProtoGroup.getName());
		}
	}
}
