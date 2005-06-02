/*
 * $Id: TestEventSource.java,v 1.3 2005/06/02 14:31:02 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.event.corba.EventSource_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class TestEventSource extends CommonTest {

	public TestEventSource(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestEventSource.class);
	}

	public void testCreateInstance() throws ApplicationException {
		Identifier sourceEntityId;
		List eventSources = new LinkedList();

		sourceEntityId = new Identifier("MCM_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = new Identifier("Port_38");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = new Identifier("Equipment_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		sourceEntityId = new Identifier("TransmissionPath_19");
		eventSources.add(createAndTestEventSource(sourceEntityId));

		EventSource eventSource;
		for (Iterator it = eventSources.iterator(); it.hasNext();) {
			eventSource = (EventSource) it.next();
			System.out.println("id: "+ eventSource.getId());
		}
		StorableObjectPool.flush(ObjectEntities.EVENTSOURCE_ENTITY_CODE, false);
	}

	private EventSource createAndTestEventSource(Identifier sourceEntityId) throws ApplicationException {
		EventSource eventSource = EventSource.createInstance(creatorUser.getId(), sourceEntityId);

		EventSource_Transferable est = (EventSource_Transferable) eventSource.getTransferable();

		EventSource eventSource1 = new EventSource(est);
		assertEquals(eventSource.getId(), eventSource1.getId());
		assertEquals(eventSource.getCreated(), eventSource1.getCreated());
		assertEquals(eventSource.getModified(), eventSource1.getModified());
		assertEquals(eventSource.getCreatorId(), eventSource1.getCreatorId());
		assertEquals(eventSource.getModifierId(), eventSource1.getModifierId());
		assertEquals(eventSource.getVersion(), eventSource1.getVersion());
		assertEquals(eventSource.getSourceEntityId(), eventSource1.getSourceEntityId());

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
