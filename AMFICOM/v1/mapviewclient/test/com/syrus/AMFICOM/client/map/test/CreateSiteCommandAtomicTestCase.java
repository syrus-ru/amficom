/**
 * $Id: CreateSiteCommandAtomicTestCase.java,v 1.5 2005/09/16 14:53:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.resource.DoublePoint;

public class CreateSiteCommandAtomicTestCase extends TestCase {

	public CreateSiteCommandAtomicTestCase(String arg0) {
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
		SiteNode node = null;

		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);
		CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(type, location);
		command.setLogicalNetLayer(METS.logicalNetLayer);
		command.execute();
		node = command.getSite();
		DoublePoint geoloc = node.getLocation();
		Point loc = METS.logicalNetLayer.getConverter().convertMapToScreen(geoloc);
		
		assertTrue("Map does not contain newly inserted SiteNode", METS.map.getSiteNodes().contains(node)); //$NON-NLS-1$
		assertEquals("SiteNode created with wrong coordinates", location, loc); //$NON-NLS-1$

		RemoveNodeCommandAtomic command2 = new RemoveNodeCommandAtomic(node);
		command2.setLogicalNetLayer(METS.logicalNetLayer);
		command2.execute();
		
		assertFalse("Map contains removed SiteNode", METS.map.getSiteNodes().contains(node)); //$NON-NLS-1$

		node = null;
	}

}
