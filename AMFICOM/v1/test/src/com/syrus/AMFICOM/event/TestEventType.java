/*
 * $Id: TestEventType.java,v 1.8.2.1 2006/02/17 12:28:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.AlertKind;
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
 * @version $Revision: 1.8.2.1 $, $Date: 2006/02/17 12:28:06 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestEventType extends TestCase {

	public TestEventType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestEventType.class);
		return commonTest.createTestSetup();
	}

	/**
	 * Create new instance
	 * @throws ApplicationException 
	 */
	public void tes1tCreateInstance() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final String codename = EventType.CODENAME_MEASUREMENT_ALARM;
		final String description = "Measurement alarms";

		final EnumSet<ParameterType> parameterTypes = EnumSet.of(ParameterType.DADARA_ALARMS, ParameterType.DADARA_ANALYSIS_RESULT);

		final EventType eventType = EventType.createInstance(userId,
				codename,
				description,
				parameterTypes,
				new HashMap<Identifier, Set<AlertKind>>());

		StorableObjectPool.flush(eventType, userId, false);
	}

	public void testChangeUserAlertKinds() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final TypicalCondition tc = new TypicalCondition(EventType.CODENAME_MEASUREMENT_ALARM,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.EVENT_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final EventType eventType = (EventType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		System.out.println("Event type: '" + eventType.getId() + "'");

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SYSTEMUSER_CODE);
		final Set<SystemUser> users = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final SystemUser systemUser : users) {
			final Identifier userId1 = systemUser.getId();
			eventType.removeAlertKindsFromUser(userId1);
		}

		eventType.printUserAlertKinds();
		StorableObjectPool.flush(ObjectEntities.EVENT_TYPE_CODE, userId, false);
	}

}
