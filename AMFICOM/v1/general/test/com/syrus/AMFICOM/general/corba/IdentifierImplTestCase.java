/*
 * $Id: IdentifierImplTestCase.java,v 1.2 2004/12/08 08:49:35 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.ObjectEntities;
import java.io.*;
import junit.framework.TestCase;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/12/08 08:49:35 $
 * @module general_v1
 */
public class IdentifierImplTestCase extends TestCase {
	public static void main(String[] args) {
		junit.awtui.TestRunner.run(IdentifierImplTestCase.class);
	}

	public final void testHashCode() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, minor);
		int hashCode1 = id1.hashCode();
		int hashCode2 = id2.hashCode();
		System.err.println(hashCode1);
		System.err.println(hashCode2);
		assertEquals(hashCode1, hashCode2);
	}

	public final void testMajor() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, minor);
		short id1Major = id1.major();
		short id2Major = id2.major();
		System.err.println(ObjectEntities.codeToString(id1Major));
		System.err.println(ObjectEntities.codeToString(id2Major));
		assertEquals(id1Major, id2Major);
	}

	public final void testMinor() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, minor);
		long id1Minor = id1.minor();
		long id2Minor = id2.minor();
		System.err.println(id1Minor);
		System.err.println(id2Minor);
		assertEquals(id1Minor, id2Minor);
	}

	public final void testIdentifierString() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, minor);
		String id1IdentifierString = id1.identifierString();
		String id2IdentifierString = id2.identifierString();
		assertEquals(id1IdentifierString, id2IdentifierString);
	}

	/**
	 * Class under test for void IdentifierImpl(String)
	 */
	public final void testIdentifierImplString() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		System.err.println(id);
	}

	/**
	 * Class under test for void IdentifierImpl(short, long)
	 */
	public final void testIdentifierImplshortlong() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, minor);
		System.err.println(id);
	}

	/**
	 * Class under test for Identifier cloneInstance()
	 */
	public final void testCloneInstance() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = id1.cloneInstance();
		assertEquals(id1 == id2, false);
		assertEquals(id1, id2);
	}

	public final void testCompareTo() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + ++minor);
		assertEquals(((IdentifierImpl) id1).compareTo(id1), 0);
		assertEquals(((IdentifierImpl) id1).compareTo(id2), -1);
		assertEquals(((IdentifierImpl) id2).compareTo(id1), 1);
		assertEquals(((IdentifierImpl) id2).compareTo(id2), 0);
	}

	/**
	 * Class under test for boolean equals(Object)
	 */
	public final void testEqualsObject() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, minor);
		assertEquals(id1, id1);
		assertEquals(id1, id2);
		assertEquals(id2, id1);
		assertEquals(id2, id2);
	}

	/**
	 * Class under test for String toString()
	 */
	public final void testToString() {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, minor);
		System.err.println(id1);
		System.err.println(id2);
	}

	public final void testSerializable() throws ClassNotFoundException, IOException {
		IdentifierDefaultFactory factory = new IdentifierDefaultFactory();
		long minor = 1L;
		Identifier id1 = factory.newInstanceFromString(ObjectEntities.ANALYSIS_ENTITY + IIdentifier.SEPARATOR + minor);
		Identifier id2 = factory.newInstanceFromPrimitive(ObjectEntities.ANALYSIS_ENTITY_CODE, ++minor);

		File f = File.createTempFile("idImpl", null);
		System.err.println(f.getCanonicalPath());

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(id1);
		out.writeObject(id2);
		out.flush();
		out.close();

		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		Identifier newId1 = (Identifier) in.readObject();
		Identifier newId2 = (Identifier) in.readObject();
		in.close();

		assertEquals(id1, newId1);
		assertEquals(id2, newId2);
	}
}
