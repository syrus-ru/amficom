/*
 * $Id: TestEventSource.java,v 1.3 2005/02/16 13:40:17 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;

import com.syrus.AMFICOM.event.corba.EventSource_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/16 13:40:17 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class TestEventSource extends CommonEventTest {

	public TestEventSource(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestEventSource.class);
	}

//	public void testCreateInstance() throws ApplicationException {
//		Identifier creatorId = new Identifier("Users_58");
//		Identifier sourceEntityId;
//		List eventSources = new LinkedList();
//
//		sourceEntityId = new Identifier("MCM_19");
//		eventSources.add(createAndTestEventSource(creatorId, sourceEntityId));
//
//		sourceEntityId = new Identifier("Port_38");
//		eventSources.add(createAndTestEventSource(creatorId, sourceEntityId));
//
//		sourceEntityId = new Identifier("Equipment_19");
//		eventSources.add(createAndTestEventSource(creatorId, sourceEntityId));
//
//		sourceEntityId = new Identifier("TransmissionPath_19");
//		eventSources.add(createAndTestEventSource(creatorId, sourceEntityId));
//
//		EventSource eventSource;
//		for (Iterator it = eventSources.iterator(); it.hasNext();) {
//			eventSource = (EventSource) it.next();
//			System.out.println("id: "+ eventSource.getId());
//			EventStorableObjectPool.putStorableObject(eventSource);
//		}
//		EventStorableObjectPool.flush(false);
//	}

	private EventSource createAndTestEventSource(Identifier creatorId, Identifier sourceEntityId) throws ApplicationException {
		EventSource eventSource = EventSource.createInstance(creatorId, sourceEntityId);

		EventSource_Transferable est = (EventSource_Transferable) eventSource.getTransferable();

		EventSource eventSource1 = new EventSource(est);
		assertEquals(eventSource.getId(), eventSource1.getId());
		assertEquals(eventSource.getCreated(), eventSource1.getCreated());
		assertEquals(eventSource.getModified(), eventSource1.getModified());
		assertEquals(eventSource.getCreatorId(), eventSource1.getCreatorId());
		assertEquals(eventSource.getModifierId(), eventSource1.getModifierId());
		assertEquals(eventSource.getSourceEntityId(), eventSource1.getSourceEntityId());

		return eventSource;
	}

	public void testDelete() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EVENTSOURCE_ENTITY_CODE);
		Collection eventSources = EventStorableObjectPool.getStorableObjectsByCondition(ec, true);
		EventSource eventSource;
		for (Iterator it = eventSources.iterator(); it.hasNext();) {
			eventSource = (EventSource) it.next();
			System.out.println("Event source: " + eventSource.getId());
		}
		EventStorableObjectPool.delete(eventSources);
		EventStorableObjectPool.flush(true);
	}
}
