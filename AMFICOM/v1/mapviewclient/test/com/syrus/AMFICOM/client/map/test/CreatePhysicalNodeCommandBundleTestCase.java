/**
 * $Id: CreatePhysicalNodeCommandBundleTestCase.java,v 1.2 2005/07/01 16:07:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;

public class CreatePhysicalNodeCommandBundleTestCase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearMap();
	}

	/**
	 * site ---[node]--- site
	 *
	 */
	public void testExecute() throws Exception {
		//pre-test tasks
		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(type, location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		SiteNode site1 = site1Command.getSite();

		Point location2 = new Point(220, 220);
		CreateSiteCommandAtomic site2Command = new CreateSiteCommandAtomic(type, location2);
		site2Command.setLogicalNetLayer(METS.logicalNetLayer);
		site2Command.execute();
		SiteNode site2 = site2Command.getSite();

		CreatePhysicalLinkCommandAtomic link1Command = new CreatePhysicalLinkCommandAtomic(site1, site2);
		link1Command.setLogicalNetLayer(METS.logicalNetLayer);
		link1Command.execute();
		PhysicalLink link1 = link1Command.getLink();

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, site2);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		//test itself
		CreatePhysicalNodeCommandBundle command = new CreatePhysicalNodeCommandBundle(nodeLink1, new Point(120, 120));
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 2);
		assertEquals(physicalLinks.size(), 1);
		assertEquals(topologicalNodes.size(), 1);
		assertEquals(siteNodes.size(), 2);

		NodeLink nodeLink2 = null;
		NodeLink nodeLink3 = null;
		for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
			NodeLink nodeLink = (NodeLink )iter.next();
			if(!(nodeLink.equals(nodeLink1))) {
				if(nodeLink2 == null)
					nodeLink2 = nodeLink;
				else
				if(nodeLink3 == null)
					nodeLink3 = nodeLink;
			}
		}
		
		TopologicalNode node1 = (TopologicalNode)topologicalNodes.iterator().next();

		assertTrue(siteNodes.contains(site1));
		assertTrue(siteNodes.contains(site2));
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), site2); 
		assertSame(nodeLink2.getPhysicalLink(), link1);
		assertSame(nodeLink3.getPhysicalLink(), link1);
		assertEquals(link1.getNodeLinks().size(), 2);
		assertTrue(link1.getNodeLinks().contains(nodeLink2));
		assertTrue(link1.getNodeLinks().contains(nodeLink3));
		assertSame(node1.getPhysicalLink(), link1);
		assertNotSame(nodeLink2.getStartNode(), nodeLink2.getEndNode()); 
		assertNotSame(nodeLink3.getStartNode(), nodeLink3.getEndNode());

		Set nodes = new HashSet();
		nodes.add(nodeLink2.getStartNode());
		nodes.add(nodeLink2.getEndNode());
		nodes.add(nodeLink3.getStartNode());
		nodes.add(nodeLink3.getEndNode());

		assertEquals(nodes.size(), 3);
		
		assertTrue(nodes.contains(site1));
		assertTrue(nodes.contains(site2));
		assertTrue(nodes.contains(node1));
	}

}
