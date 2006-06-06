/*-
 * $Id: TestSchemeProtoGroup.java,v 1.5 2006/06/06 15:52:38 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.5 $, $Date: 2006/06/06 15:52:38 $
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
		//final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
		final LinkedIdsCondition lic = new LinkedIdsCondition(Identifier.VOID_IDENTIFIER,ObjectEntities.SCHEMEPROTOGROUP_CODE);
		final Set<Identifier> butIds = new HashSet<Identifier>();
		butIds.add(Identifier.valueOf("SchemeProtoGroup_91"));
		//butIds.add(Identifier.valueOf("SchemeProtoGroup_93"));
		butIds.add(Identifier.VOID_IDENTIFIER);
		//final Set<SchemeProtoGroup> set = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		final Set<SchemeProtoGroup> set = StorableObjectPool.getStorableObjectsButIdsByCondition(butIds, lic, true);
		for (SchemeProtoGroup schemeProtoGroup : set) {
			System.out.println("group: " + schemeProtoGroup.getName());
		}
	}
}
