/*
 * $Id: TestParameterSet.java,v 1.3 2006/02/17 12:04:55 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.3 $, $Date: 2006/02/17 12:04:55 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestParameterSet extends TestCase {

	public TestParameterSet(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestParameterSet.class);
		return commonTest.createTestSetup();
	}

	public void testAttachToMonitoredElement() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final StorableObjectCondition meCondition = new EquivalentCondition(ObjectEntities.MONITOREDELEMENT_CODE);
		final Set<MonitoredElement> monitoredElements = StorableObjectPool.getStorableObjectsByCondition(meCondition, true);
		final MonitoredElement monitoredElement = monitoredElements.iterator().next();
		System.out.println("MonitoredElement: " + monitoredElement);

		final StorableObjectCondition condition = new EquivalentCondition(ObjectEntities.PARAMETERSET_CODE);

		final Set<ParameterSet> parameterSets = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		final Set<ParameterSet> updateParameterSets = new HashSet<ParameterSet>();
		for (final ParameterSet parameterSet : parameterSets) {
			System.out.println("Parameter Set: " + parameterSet.getId() + ", " + parameterSet.getDescription() + ", valid: " + parameterSet.isValid());
			if (!parameterSet.isValid()) {
				System.out.println("Setting monitored element " + monitoredElement + " to parameter set " + parameterSet);
				parameterSet.attachToMonitoredElement(monitoredElement.getId());
				updateParameterSets.add(parameterSet);
				System.out.println("Parameter set " + parameterSet + ", valid: " + parameterSet.isValid());
			}
		}
		StorableObjectPool.flush(updateParameterSets, userId, false);
	}
}
