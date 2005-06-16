/*
 * $Id: TestEventType.java,v 1.5 2005/06/16 13:26:44 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.event.corba.AlertKind;
import com.syrus.AMFICOM.event.corba.EventType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/16 13:26:44 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class TestEventType extends CommonTest {

	public TestEventType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestEventType.class);
	}

	public void tes1tAlertKind() {
		AlertKind alertKind1 = AlertKind.ALERT_KIND_EMAIL;
		AlertKind alertKind2 = AlertKind.ALERT_KIND_SMS;
		System.out.println("Email: " + alertKind1.hashCode() + ", SMS: " + alertKind2.hashCode() + ", " + alertKind1.equals(alertKind2));
	}

	/**
	 * Create new instance
	 * @throws ApplicationException 
	 */
	public void tes1tCreateInstance() throws ApplicationException {
		String codename = EventType.CODENAME_MEASUREMENT_ALARM;
		String description = "Measurement alarms";

		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodenames.ALARM_STATUS,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		TypicalCondition tc2 = new TypicalCondition(ParameterTypeCodenames.HZ_CHO,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		CompoundCondition cc = new CompoundCondition(tc1, CompoundConditionSort.OR, tc2);
		Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(cc, true);

		EventType eventType = EventType.createInstance(creatorUser.getId(),
				codename,
				description,
				parameterTypes,
				new HashMap());

		Identifier id = eventType.getId();
		assertEquals(ObjectEntities.EVENTTYPE_ENTITY_CODE, id.getMajor());
		EventType_Transferable ett = (EventType_Transferable) eventType.getTransferable();
		assertEquals(ett.codename, eventType.getCodename());
		EventType eventType1 = new EventType(ett);

		assertEquals(eventType.getId(), eventType1.getId());
		assertEquals(eventType.getCreated(), eventType1.getCreated());
		assertEquals(eventType.getModified(), eventType1.getModified());
		assertEquals(eventType.getCreatorId(), eventType1.getCreatorId());
		assertEquals(eventType.getModifierId(), eventType1.getModifierId());
		assertEquals(eventType.getCodename(), eventType1.getCodename());
		assertEquals(eventType.getDescription(), eventType1.getDescription());

		assertEquals(eventType.getParameterTypeIds().size(), eventType1.getParameterTypeIds().size());
		Iterator eventTypeParIt = eventType.getParameterTypeIds().iterator();
		Iterator eventType1ParIt = eventType1.getParameterTypeIds().iterator();
		ParameterType parameterType1, parameterType2;
		while (eventTypeParIt.hasNext() && eventType1ParIt.hasNext()) {
			parameterType1 = (ParameterType) eventTypeParIt.next();
			parameterType2 = (ParameterType) eventType1ParIt.next();
			assertEquals("identifier of parameter type for event type '" + eventType.getId() + "'", parameterType1.getId(), parameterType2.getId());
			assertEquals("created of parameter type for event type '" + eventType.getId() + "'", parameterType1.getCreated(), parameterType2.getCreated());
			assertEquals("modified of parameter type for event type '" + eventType.getId() + "'", parameterType1.getModified(), parameterType2.getModified());
			assertEquals("creator id of parameter type for event type '" + eventType.getId() + "'", parameterType1.getCreatorId(), parameterType2.getCreatorId());
			assertEquals("modifier id of parameter type for event type '" + eventType.getId() + "'", parameterType1.getModifierId(), parameterType2.getModifierId());
			assertEquals("codename of parameter type for event type '" + eventType.getId() + "'", parameterType1.getCodename(), parameterType2.getCodename());
			assertEquals("name of parameter type for event type '" + eventType.getId() + "'", parameterType1.getName(), parameterType2.getName());
			assertEquals("description of parameter type for event type '" + eventType.getId() + "'", parameterType1.getDescription(), parameterType2.getDescription());
			assertEquals("data type of parameter type for event type '" + eventType.getId() + "'", parameterType1.getDataType(), parameterType2.getDataType());
		}

		EventTypeDatabase eventTypeDatabase = (EventTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.EVENTTYPE_ENTITY_CODE);
		eventTypeDatabase.update(eventType, creatorUser.getId(), StorableObjectDatabase.UPDATE_FORCE);
	}

	public void testChangeUserAlertKinds() throws ApplicationException {
		TypicalCondition tc = new TypicalCondition(EventType.CODENAME_MEASUREMENT_ALARM,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.EVENTTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		EventType eventType = (EventType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		System.out.println("Event type: '" + eventType.getId() + "'");

		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.SYSTEM_USER_ENTITY_CODE);
		Set users = StorableObjectPool.getStorableObjectsByCondition(ec, true);

		for (final Iterator it = users.iterator(); it.hasNext();) {
			final SystemUser user = (SystemUser) it.next();
			final Identifier userId = user.getId();
			eventType.removeAlertKindsFromUser(userId);
		}

		eventType.printUserAlertKinds();
		StorableObjectPool.flush(ObjectEntities.EVENTTYPE_ENTITY_CODE, false);
	}

	public void testTransferable() throws ApplicationException {
		TypicalCondition tc = new TypicalCondition(EventType.CODENAME_MEASUREMENT_ALARM,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.EVENTTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		EventType eventType = (EventType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		System.out.println("Event type: '" + eventType.getId() + "'");

		EventType_Transferable ett = (EventType_Transferable) eventType.getTransferable();
		EventType eventType1 = (EventType) StorableObjectPool.fromTransferable(ObjectEntities.EVENTTYPE_ENTITY_CODE, ett);
		eventType1.printUserAlertKinds();
	}
}
