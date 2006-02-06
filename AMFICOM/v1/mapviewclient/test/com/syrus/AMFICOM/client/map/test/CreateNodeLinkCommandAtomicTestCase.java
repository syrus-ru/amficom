/**
 * $Id: CreateNodeLinkCommandAtomicTestCase.java,v 1.3 2005/09/16 14:53:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.RemoveNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNodeType;

public class CreateNodeLinkCommandAtomicTestCase extends TestCase {

	public CreateNodeLinkCommandAtomicTestCase(String arg0) {
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
		NodeLink nodeLink = null;

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

		CreateNodeLinkCommandAtomic command = new CreateNodeLinkCommandAtomic(link, startnode, endnode);
		command.setLogicalNetLayer(METS.logicalNetLayer);
		command.execute();
		nodeLink = command.getNodeLink();
		
		assertTrue("Map does not contain newly inserted NodeLink", METS.map.getNodeLinks().contains(nodeLink)); //$NON-NLS-1$
		assertSame("start node not correct", nodeLink.getStartNode(), startnode); //$NON-NLS-1$
		assertSame("end node not correct", nodeLink.getEndNode(), endnode); //$NON-NLS-1$
		assertSame("link not correct", nodeLink.getPhysicalLink(), link); //$NON-NLS-1$
		assertEquals("topological length not correct", nodeLink.getLengthLt(), link.getLengthLt(), 0.0001); //$NON-NLS-1$

		RemoveNodeLinkCommandAtomic command4 = new RemoveNodeLinkCommandAtomic(nodeLink);
		command4.setLogicalNetLayer(METS.logicalNetLayer);
		command4.execute();

		assertFalse("Map contains removed NodeLink", METS.map.getNodeLinks().contains(nodeLink)); //$NON-NLS-1$
		
		nodeLink = null;
	}

}
