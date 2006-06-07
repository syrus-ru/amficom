/*
 * $Id: TestEventType.java,v 1.8.2.3 2006/06/07 10:30:28 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.event.EventType.CODENAME_MEASUREMENT_ALARM;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ALARMS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ANALYSIS_RESULT;

import java.util.HashMap;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.eventv2.DeliveryMethod;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.8.2.3 $, $Date: 2006/06/07 10:30:28 $
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

		final String codename = CODENAME_MEASUREMENT_ALARM;
		final String description = "Measurement alarms";

		final CompoundCondition parameterTypeCondition = new CompoundCondition(new TypicalCondition(DADARA_ALARMS.stringValue(),
						OPERATION_EQUALS,
						PARAMETER_TYPE_CODE,
						COLUMN_CODENAME),
				OR,
				new TypicalCondition(DADARA_ANALYSIS_RESULT.stringValue(),
						OPERATION_EQUALS,
						PARAMETER_TYPE_CODE,
						COLUMN_CODENAME));
		final Set<Identifier> parameterTypeIds = StorableObjectPool.getIdentifiersByCondition(parameterTypeCondition, true);

		final EventType eventType = EventType.createInstance(userId,
				codename,
				description,
				parameterTypeIds,
				new HashMap<Identifier, Set<DeliveryMethod>>());

		StorableObjectPool.flush(eventType, userId, false);
	}

	public void testChangeUserAlertKinds() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final TypicalCondition tc = new TypicalCondition(CODENAME_MEASUREMENT_ALARM,
				OPERATION_EQUALS,
				EVENT_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<EventType> eventTypes = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		final EventType eventType = eventTypes.iterator().next();
		System.out.println("Event type: '" + eventType.getId() + "'");

		final EquivalentCondition ec = new EquivalentCondition(SYSTEMUSER_CODE);
		final Set<SystemUser> users = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final SystemUser systemUser : users) {
			final Identifier userId1 = systemUser.getId();
			eventType.removeAlertKindsFromUser(userId1);
		}

		eventType.printUserAlertKinds();
		StorableObjectPool.flush(EVENT_TYPE_CODE, userId, false);
	}

}
