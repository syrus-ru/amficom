/*-
 * $Id: TestSchemeProtoGroup.java,v 1.3 2005/12/15 13:59:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.3 $, $Date: 2005/12/15 13:59:17 $
 * @author $Author: bass $
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
		final LinkedIdsCondition lic = new LinkedIdsCondition(Identifier.VOID_IDENTIFIER,ObjectEntities.SCHEMEPROTOGROUP_CODE);
		final Set<Identifier> butIds = new HashSet<Identifier>();
		butIds.add(new Identifier("SchemeProtoGroup_91"));
		//butIds.add(new Identifier("SchemeProtoGroup_93"));
		butIds.add(Identifier.VOID_IDENTIFIER);
		//final Set<SchemeProtoGroup> set = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		final Set<SchemeProtoGroup> set = StorableObjectPool.getStorableObjectsButIdsByCondition(butIds, lic, true);
		for (SchemeProtoGroup schemeProtoGroup : set) {
			System.out.println("group: " + schemeProtoGroup.getName());
		}
	}
}
