/**
 * $Id: CreatePhysicalNodeCommandAtomicTestCase.java,v 1.7 2005/09/16 14:53:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;

public class CreatePhysicalNodeCommandAtomicTestCase extends TestCase {

	public CreatePhysicalNodeCommandAtomicTestCase(String arg0) {
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
		AbstractNode startnode = null;
		AbstractNode endnode = null;
		PhysicalLink link = null;
		TopologicalNode node = null;

		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic command1 = new CreateSiteCommandAtomic(type, location);
		command1.setLogicalNetLayer(METS.logicalNetLayer);
		command1.execute();
		startnode = command1.getSite();

		Point location2 = new Point(120, 120);
		CreateSiteCommandAtomic command2 = new CreateSiteCommandAtomic(type, location2);
		command2.setLogicalNetLayer(METS.logicalNetLayer);
		command2.execute();
		endnode = command2.getSite();

		CreatePhysicalLinkCommandAtomic command3 = new CreatePhysicalLinkCommandAtomic(startnode, endnode);
		command3.setLogicalNetLayer(METS.logicalNetLayer);
		command3.execute();
		link = command3.getLink();

		Point location3 = new Point(100, 50);
		DoublePoint doublePoint = METS.logicalNetLayer.getConverter().convertScreenToMap(location3);
		CreatePhysicalNodeCommandAtomic command = new CreatePhysicalNodeCommandAtomic(link, doublePoint);
		command.setLogicalNetLayer(METS.logicalNetLayer);
		command.execute();
		node = command.getNode();
		
		assertTrue("Map does not contain newly inserted TopologicalNode", METS.map.getTopologicalNodes().contains(node)); //$NON-NLS-1$
//		assertSame("wrong physicallink", node.getPhysicalLink(), link);

		RemoveNodeCommandAtomic command4 = new RemoveNodeCommandAtomic(node);
		command4.setLogicalNetLayer(METS.logicalNetLayer);
		command4.execute();

		assertFalse("Map contains removed TopologicalNode", METS.map.getTopologicalNodes().contains(node)); //$NON-NLS-1$

		node = null;
	}

}
