/*-
 * $Id: TestSchemeProtoGroup.java,v 1.6 2006/06/16 10:56:55 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
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

/**
 * @version $Revision: 1.6 $, $Date: 2006/06/16 10:56:55 $
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
		//final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SCHEMEPROTOGROUP_CODE);
		final Set<Identifier> butIds = new HashSet<Identifier>();
		butIds.add(Identifier.valueOf("SchemeProtoGroup_91"));
		//butIds.add(Identifier.valueOf("SchemeProtoGroup_93"));
		butIds.add(Identifier.VOID_IDENTIFIER);
//		final Set<SchemeProtoGroup> schemeProtoGroups = SchemeUtilities.getRootSchemeProtoGroups();
		final Set<SchemeProtoGroup> schemeProtoGroups = SchemeUtilities.getRootSchemeProtoGroupsButIds(butIds);
		for (final SchemeProtoGroup schemeProtoGroup : schemeProtoGroups) {
			System.out.println("group: " + schemeProtoGroup.getName());
		}
	}
}
