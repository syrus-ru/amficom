/*
 * $Id: TestEvent.java,v 1.4 2005/04/01 15:20:22 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.4 $, $Date: 2005/04/01 15:20:22 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class TestEvent extends CommonEventTest {

	public TestEvent(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestEvent.class);
	}

	public void testCreateInstance() throws ApplicationException {
		Identifier creatorId = new Identifier("Users_58");

		TypicalCondition tc = new TypicalCondition(EventType.CODENAME_MEASUREMENT_ALARM,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.EVENTTYPE_ENTITY_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		Set eventTypes = EventStorableObjectPool.getStorableObjectsByCondition(tc, true);
		System.out.println("###################### Retrieved: " + eventTypes.size() + " event types");
		EventType eventType = (EventType) eventTypes.iterator().next();

		String description = "test event";

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
		System.out.println("###################### Retrieved: " + parameterTypes.size() + " parameter types");
		ParameterType pt, pt1 = null, pt2 = null;
		for (Iterator it = parameterTypes.iterator(); it.hasNext();) {
			pt = (ParameterType) it.next();
			if (pt.getCodename().equals(ParameterTypeCodenames.ALARM_STATUS))
				pt1 = pt;
			else
				if (pt.getCodename().equals(ParameterTypeCodenames.HZ_CHO))
					pt2 = pt;
				else
					fail("Unknown codename '" + pt.getCodename() + "' of parameter type '" + pt.getId() + "'");
		}
		if (pt1 == null)
			fail("Cannot find parameter type for codename '" + ParameterTypeCodenames.ALARM_STATUS + "'");
		if (pt2 == null)
			fail("Cannot find parameter type for codename '" + ParameterTypeCodenames.HZ_CHO + "'");
		Set eventParameters = new HashSet(2);
		eventParameters.add(EventParameter.createInstance(pt1, Integer.toString(0)));
		eventParameters.add(EventParameter.createInstance(pt2, "1, 2, 3, 4, 5, 6, 7, 8"));

		//LinkedIdsCondition lic = new LinkedIdsCondition()
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EVENTSOURCE_ENTITY_CODE);
		Set eventSources = EventStorableObjectPool.getStorableObjectsByCondition(ec, true);
		System.out.println("###################### Retrieved: " + eventSources.size() + " event sources");
		Set eventSourceIds = new HashSet(eventSources.size());
		for (Iterator it = eventSources.iterator(); it.hasNext();)
			eventSourceIds.add(((EventSource) it.next()).getId()); 

		Event event = Event.createInstance(creatorId, eventType, description, eventParameters, eventSourceIds);

		EventStorableObjectPool.putStorableObject(event);
		EventStorableObjectPool.flush(false);
	}

}
