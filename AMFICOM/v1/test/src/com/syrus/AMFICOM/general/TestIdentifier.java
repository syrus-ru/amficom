/*
 * $Id: TestIdentifier.java,v 1.1 2005/06/10 15:17:44 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @version $Revision: 1.1 $, $Date: 2005/06/10 15:17:44 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestIdentifier extends CommonTest {

	public TestIdentifier(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite(TestIdentifier.class);
		TestSetup testSetup = new TestSetup(testSuite) {
			protected void setUp() {
				oneTimeSetUp();
			}
			protected void tearDown() {
				oneTimeTearDown();
			}
		};
		return testSetup;
	}

	protected static void oneTimeSetUp() {
		//Nothing
	}

	protected static void oneTimeTearDown() {
		//Nothing
	}

	public void testCreateSumIdentifiers() throws ApplicationException {
		final Set ids = new HashSet();

		//1
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		ids.add(new Identifier("Measurement_2494"));
		final Set identifiables1 = new HashSet(ids);

		ids.clear();

		//2
		ids.add(new Identifier("Measurement_2741"));
		ids.add(new Identifier("Measurement_2742"));
		ids.add(new Identifier("Measurement_2744"));
		final Set identifiables2 = new HashSet(ids);

		//create sum
		System.out.println("Sum:");
		final Set sumIds = Identifier.createSumIdentifiers(identifiables1, identifiables2);
		for (final Iterator it = sumIds.iterator(); it.hasNext();) {
			final Identifier id = (Identifier) it.next();
			System.out.println("Id: '" + id + "'");
		}

		assertTrue("All identifiers from the 1st set", sumIds.containsAll(Identifier.createIdentifiers(identifiables1)));
		assertTrue("All identifiers from the 2nd set", sumIds.containsAll(Identifier.createIdentifiers(identifiables2)));
		assertTrue("Size of sum", sumIds.size() <= identifiables1.size() + identifiables2.size()
				&& sumIds.size() >= identifiables1.size());
	}

	public void testCreateSubstractionIdentifiers() throws ApplicationException {
		final Set ids = new HashSet();

		//1
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		ids.add(new Identifier("Measurement_2494"));
		final Set identifiables1 = new HashSet(ids);

		ids.clear();

		//2
		ids.add(new Identifier("Measurement_2491"));
		ids.add(new Identifier("Measurement_2492"));
		ids.add(new Identifier("Measurement_2493"));
		final Set identifiables2 = new HashSet(ids);

		// Create substraction
		System.out.println("Substraction:");
		final Set subIds = Identifier.createSubstractionIdentifiers(identifiables1, identifiables2);
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
