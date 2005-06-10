/*
 * $Id: TestStorableObjectPool.java,v 1.1 2005/06/10 15:17:44 arseniy Exp $
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

import com.syrus.AMFICOM.measurement.Measurement;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/10 15:17:44 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestStorableObjectPool extends CommonTest {

	public TestStorableObjectPool(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestStorableObjectPool.class);
	}

	public void _testStorableObjectResizableLRUMap() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_ENTITY_CODE);

		final Set set = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final Iterator it = set.iterator(); it.hasNext();) {
			final Measurement measurement = (Measurement) it.next();
			System.out.println("Measurement: '" + measurement.getId() + "'");
		}

		final int SIZE = 4;
		final StorableObjectResizableLRUMap storableObjectResizableLRUMap = new StorableObjectResizableLRUMap(SIZE);
		System.out.println("Size: " + storableObjectResizableLRUMap.size());
		int i = 0;
		for (final Iterator it = set.iterator(); it.hasNext() && i < SIZE; i++) {
			final Measurement measurement = (Measurement) it.next();
			final Identifier id = measurement.getId();
			System.out.println("Putting measurement: '" + id + "'");
			storableObjectResizableLRUMap.put(id, measurement);
			System.out.println("Size: " + storableObjectResizableLRUMap.size());
		}

		for (final Iterator it = storableObjectResizableLRUMap.iterator(); it.hasNext();) {
			final Measurement measurement = (Measurement) it.next();
			System.out.println("In pool: '" + measurement.getId() + "'");
		}
	}

	public void testGetStorableObjectsByConditionButIds() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_ENTITY_CODE);

		final Set set = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final Iterator it = set.iterator(); it.hasNext();) {
			final Measurement measurement = (Measurement) it.next();
			System.out.println("Measurement: '" + measurement.getId() + "'");
		}

		final Set ids = new HashSet();
		ids.add(new Identifier("Measurement_2742"));
		ids.add(new Identifier("Measurement_2701"));
		final Set set1 = StorableObjectPool.getStorableObjectsByConditionButIds(ids, ec, true);
		for (final Iterator it = set1.iterator(); it.hasNext();) {
			final Measurement measurement = (Measurement) it.next();
			System.out.println("Measurement: '" + measurement.getId() + "'");
		}
	}

}
