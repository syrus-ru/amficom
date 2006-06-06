/*
 * $Id: TestEventSource.java,v 1.9 2006/06/06 15:52:38 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.9 $, $Date: 2006/06/06 15:52:38 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestEventSource extends TestCase {

	public TestEventSource(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestEventSource.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		Identifier sourceEntityId;
		final List<EventSource> eventSources = new LinkedList<EventSource>();

		sourceEntityId = Identifier.valueOf("MCM_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = Identifier.valueOf("Port_38");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = Identifier.valueOf("Equipment_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = Identifier.valueOf("TransmissionPath_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		System.out.println("Event Sources: "+ Identifier.toString(eventSources));
		StorableObjectPool.flush(ObjectEntities.EVENTSOURCE_CODE, userId, false);
	}

	private EventSource createAndTestEventSource(final Identifier sourceEntityId) throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		EventSource eventSource = EventSource.createInstance(userId, sourceEntityId);
		return eventSource;
	}
//
//	public void testDelete() throws ApplicationException {
//		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EVENTSOURCE_ENTITY_CODE);
//		Collection eventSources = EventStorableObjectPool.getStorableObjectsByCondition(ec, true);
//		EventSource eventSource;
//		for (Iterator it = eventSources.iterator(); it.hasNext();) {
//			eventSource = (EventSource) it.next();
//			System.out.println("Event source: " + eventSource.getId());
//		}
//		EventStorableObjectPool.delete(eventSources);
//		EventStorableObjectPool.flush(true);
//	}
}
