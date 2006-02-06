/**
 * $Id: CreateCollectorCommandAtomicTestCase.java,v 1.2 2005/09/16 14:53:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateCollectorCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.RemoveCollectorCommandAtomic;
import com.syrus.AMFICOM.map.Collector;

public class CreateCollectorCommandAtomicTestCase extends TestCase {


	public CreateCollectorCommandAtomicTestCase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearMap();
	}

	public void testExecute() throws Exception {
		Collector collector = null;
		CreateCollectorCommandAtomic command = new CreateCollectorCommandAtomic("sample"); //$NON-NLS-1$
		command.setLogicalNetLayer(METS.logicalNetLayer);
		command.execute();
		collector = command.getCollector();
		
		assertTrue("Map does not contain newly inserted Collector", METS.map.getCollectors().contains(collector)); //$NON-NLS-1$

		RemoveCollectorCommandAtomic command2 = new RemoveCollectorCommandAtomic(collector);
		command2.setLogicalNetLayer(METS.logicalNetLayer);
		command2.execute();
		
		assertFalse("Map contains removed Collector", METS.map.getCollectors().contains(collector)); //$NON-NLS-1$

		collector = null;
	}

}
