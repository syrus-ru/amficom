/*
 * $Id: TestIdentifier.java,v 1.4 2005/06/20 15:13:54 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;


/**
 * @version $Revision: 1.4 $, $Date: 2005/06/20 15:13:54 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestIdentifier extends TestCase {

	public TestIdentifier(String name) {
		super(name);
	}

	public static Test suite() {
		final CommonTest commonTest = new CommonTest();
		commonTest.addTestSuite(TestIdentifier.class);
		return commonTest.createTestSetup();
	}

	public void testEquals() {
		final Identifier id1 = new Identifier("Measurement_1");
		Identifier id2 = new Identifier(ObjectEntities.MEASUREMENT_CODE, 1);
		assertEquals(id1, id2);

		id2 = new Identifier(id1.getIdentifierCode());
		assertEquals(id1, id2);

		id2 = new Identifier(id1.getIdentifierString());
		assertEquals(id1, id2);

		id2 = new Identifier(ObjectEntities.EQUIPMENT_CODE, 1);
		assertFalse("Different majors", id1.equals(id2));

		id2 = new Identifier("Measurement_2");
		assertFalse("Different minors", id1.equals(id2));
	}

	public void testIllegalMajor() {
		Identifier id = new Identifier(StorableObjectPool.БАЙАН + "_1");
		System.out.println("major: " + id.getMajor() + ", minor: " + id.getMinor()
				+ ", code: " + id.getIdentifierCode() + ", string: " + id.getIdentifierString());
	}

	public void testIllegalMinor() {
		try {
			new Identifier("Measurement_100000000000000000");
			fail("Illegal minor");
		}
		catch (AssertionError ae) {
			//ok
		}
	}

	public void _testCreateSumIdentifiers() throws ApplicationException {
		final Set<Identifier> ids = new HashSet<Identifier>();

		//1
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		ids.add(new Identifier("Measurement_2494"));
		final Set<Identifiable> identifiables1 = new HashSet<Identifiable>(ids);

		ids.clear();

		//2
		ids.add(new Identifier("Measurement_2741"));
		ids.add(new Identifier("Measurement_2742"));
		ids.add(new Identifier("Measurement_2744"));
		final Set<Identifiable> identifiables2 = new HashSet<Identifiable>(ids);

		//create sum
		System.out.println("Sum:");
		final Set<Identifier> sumIds = Identifier.createSumIdentifiers(identifiables1, identifiables2);
		for (final Iterator it = sumIds.iterator(); it.hasNext();) {
			final Identifier id = (Identifier) it.next();
			System.out.println("Id: '" + id + "'");
		}

		assertTrue("All identifiers from the 1st set", sumIds.containsAll(Identifier.createIdentifiers(identifiables1)));
		assertTrue("All identifiers from the 2nd set", sumIds.containsAll(Identifier.createIdentifiers(identifiables2)));
		assertTrue("Size of sum", sumIds.size() <= identifiables1.size() + identifiables2.size()
				&& sumIds.size() >= identifiables1.size());
	}

	public void _testCreateSubstractionIdentifiers() throws ApplicationException {
		final Set<Identifier> ids = new HashSet<Identifier>();

		//1
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		ids.add(new Identifier("Measurement_2494"));
		final Set<Identifiable> identifiables1 = new HashSet<Identifiable>(ids);

		ids.clear();

		//2
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		final Set<Identifiable> identifiables2 = new HashSet<Identifiable>(ids);

		// Create substraction
		System.out.println("Substraction:");
		final Set<Identifier> subIds = Identifier.createSubtractionIdentifiers(identifiables1, identifiables2);
		for (final Iterator it = subIds.iterator(); it.hasNext();) {
			final Identifier id = (Identifier) it.next();
			System.out.println("Id: '" + id + "'");
		}

		for (final Iterator it = identifiables1.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			final Identifier id = identifiable.getId();
			assertTrue("Identifiers from 1nd set, not from 2nd set",
					(subIds.contains(id) && identifiables1.contains(identifiable) && !identifiables2.contains(identifiable))
							|| (!subIds.contains(id) && !identifiables1.contains(identifiable) && identifiables2.contains(identifiable))
							|| (!subIds.contains(id) && identifiables1.contains(identifiable) && identifiables2.contains(identifiable)));
		}
		for (final Iterator it = identifiables2.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			final Identifier id = identifiable.getId();
			assertFalse("None identifiers from the 2nd set", subIds.contains(id));
		}
		assertTrue("Size of substraction", subIds.size() >= identifiables1.size() - identifiables2.size()
				&& subIds.size() <= identifiables1.size());
	}
}
