/*
 * $Id: TestEvent.java,v 1.6.2.2 2006/03/22 08:32:04 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ALARMS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ANALYSIS_RESULT;

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
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.6.2.2 $, $Date: 2006/03/22 08:32:04 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestEvent extends TestCase {

	public TestEvent(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestEvent.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final TypicalCondition tc = new TypicalCondition(EventType.CODENAME_MEASUREMENT_ALARM,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.EVENT_TYPE_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set<EventType> eventTypes = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		System.out.println("###################### Retrieved: " + eventTypes.size() + " event types");
		final EventType eventType = eventTypes.iterator().next();

		final String description = "test event";

		final Set<EventParameter> eventParameters = new HashSet<EventParameter>(2);
		eventParameters.add(EventParameter.createInstance(ParameterType.valueOf(DADARA_ALARMS.stringValue()).getId(), Integer.toString(0)));
		eventParameters.add(EventParameter.createInstance(ParameterType.valueOf(DADARA_ANALYSIS_RESULT.stringValue()).getId(), "1, 2, 3, 4, 5, 6, 7, 8"));

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EVENTSOURCE_CODE);
		final Set<EventSource> eventSources = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		System.out.println("###################### Retrieved: " + eventSources.size() + " event sources");
		final Set<Identifier> eventSourceIds = Identifier.createIdentifiers(eventSources);

		Event.createInstance(userId, eventType, description, eventParameters, eventSourceIds);

		StorableObjectPool.flush(ObjectEntities.EVENT_CODE, userId, false);
	}

}
