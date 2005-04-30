/*
 * $Id: TestEventType.java,v 1.2 2005/04/30 14:18:45 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.event.corba.EventType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/30 14:18:45 $
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

	/**
	 * Test pools
	 *
	 */
//	public void testPools() {
//		EventStorableObjectPool.getStorableObject();
//	}

	/**
	 * Create new instance
	 * @throws ApplicationException 
	 */
	public void testCreateInstance() throws ApplicationException {
		Identifier creatorId = new Identifier("Users_58");
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
		Set parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(cc, true);

		EventType eventType = EventType.createInstance(creatorId,
				codename,
				description,
				parameterTypes,
				new HashSet());

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

		assertEquals(eventType.getParameterTypes().size(), eventType1.getParameterTypes().size());
		Iterator eventTypeParIt = eventType.getParameterTypes().iterator();
		Iterator eventType1ParIt = eventType1.getParameterTypes().iterator();
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

		EventDatabaseContext.getEventTypeDatabase().update(eventType, creatorId, StorableObjectDatabase.UPDATE_FORCE);
	}

//	public void testDelete() throws ApplicationException {
//		TypicalCondition tc = new TypicalCondition(EventType.CODENAME_MEASUREMENT_ALARM,
//				OperationSort.OPERATION_EQUALS,
//				new Short(ObjectEntities.EVENTTYPE_ENTITY_CODE),
//				StorableObjectWrapper.COLUMN_CODENAME);
//		List eventTypes = EventStorableObjectPool.getStorableObjectsByCondition(tc, true);
//
//		EventType eventType;
//		List parameterTypes;
//		ParameterType parameterType;
//		for (Iterator it = eventTypes.iterator(); it.hasNext();) {
//			eventType = (EventType) it.next();
//			System.out.println("Event Type id: '" + eventType.getId() + "' codename: '" + eventType.getCodename() + "'");
//			parameterTypes = eventType.getParameterTypes();
//			for (Iterator it1 = parameterTypes.iterator(); it1.hasNext();) {
//				parameterType = (ParameterType) it1.next();
//				System.out.println("\tParameter Type id: '" + parameterType.getId() + "' codename: '" + parameterType.getCodename() + "'");
//			}
//		}
//		EventStorableObjectPool.delete(eventTypes);
//	}
}
