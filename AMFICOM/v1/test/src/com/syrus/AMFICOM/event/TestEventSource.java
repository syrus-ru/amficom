/*
 * $Id: TestEventSource.java,v 1.6 2005/12/15 14:16:36 arseniy Exp $
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.6 $, $Date: 2005/12/15 14:16:36 $
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
		Identifier sourceEntityId;
		final List<EventSource> eventSources = new LinkedList<EventSource>();

		sourceEntityId = new Identifier("MCM_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = new Identifier("Port_38");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = new Identifier("Equipment_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = new Identifier("TransmissionPath_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		System.out.println("Event Sources: "+ Identifier.toString(eventSources));
		StorableObjectPool.flush(ObjectEntities.EVENTSOURCE_CODE, DatabaseCommonTest.getSysUser().getId(), false);
	}

	private EventSource createAndTestEventSource(final Identifier sourceEntityId) throws ApplicationException {
		EventSource eventSource = EventSource.createInstance(DatabaseCommonTest.getSysUser().getId(), sourceEntityId);
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
