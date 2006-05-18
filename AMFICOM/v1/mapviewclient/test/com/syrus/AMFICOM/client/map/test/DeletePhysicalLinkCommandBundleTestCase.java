/**
 * $Id: DeletePhysicalLinkCommandBundleTestCase.java,v 1.5 2005/08/24 10:19:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;
import java.util.Collection;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.DeletePhysicalLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;

public class DeletePhysicalLinkCommandBundleTestCase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearMap();
	}

	/**
	 * site     node (-----) node
	 *
	 */
	public void test1() throws Exception {
		//pre-test tasks
		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(type, location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		SiteNode site1 = site1Command.getSite();

		CreatePhysicalLinkCommandAtomic link1Command = new CreatePhysicalLinkCommandAtomic(site1, site1);
		link1Command.setLogicalNetLayer(METS.logicalNetLayer);
		link1Command.execute();
		PhysicalLink link1 = link1Command.getLink();

		Point location2 = new Point(120, 20);
		DoublePoint doublePoint = METS.logicalNetLayer.getConverter().convertScreenToMap(location2);
		CreatePhysicalNodeCommandAtomic node1Command = new CreatePhysicalNodeCommandAtomic(link1, doublePoint);
		node1Command.setLogicalNetLayer(METS.logicalNetLayer);
		node1Command.execute();
		TopologicalNode node1 = node1Command.getNode();
		link1.setStartNode(node1);

		Point location4 = new Point(120, 120);
		DoublePoint doublePoint2 = METS.logicalNetLayer.getConverter().convertScreenToMap(location4);
		CreatePhysicalNodeCommandAtomic node2Command = new CreatePhysicalNodeCommandAtomic(link1, doublePoint2);
		node2Command.setLogicalNetLayer(METS.logicalNetLayer);
		node2Command.execute();
		TopologicalNode node2 = node2Command.getNode();
		link1.setEndNode(node2);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, node1, node2);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		//test itself
		DeletePhysicalLinkCommandBundle command = new DeletePhysicalLinkCommandBundle(link1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 0);
		assertEquals(physicalLinks.size(), 0);
		assertEquals(topologicalNodes.size(), 0);
		assertEquals(siteNodes.size(), 1);
	}

	/**
	 * site (----- node -----) node
	 *
	 */
	public void test2() throws Exception {
		//pre-test tasks
		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(type, location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		SiteNode site1 = site1Command.getSite();

		CreatePhysicalLinkCommandAtomic link1Command = new CreatePhysicalLinkCommandAtomic(site1, site1);
		link1Command.setLogicalNetLayer(METS.logicalNetLayer);
		link1Command.execute();
		PhysicalLink link1 = link1Command.getLink();

		Point location2 = new Point(120, 20);
		DoublePoint doublePoint = METS.logicalNetLayer.getConverter().convertScreenToMap(location2);
		CreatePhysicalNodeCommandAtomic node1Command = new CreatePhysicalNodeCommandAtomic(link1, doublePoint);
		node1Command.setLogicalNetLayer(METS.logicalNetLayer);
		node1Command.execute();
		TopologicalNode node1 = node1Command.getNode();
		node1.setActive(true);

		Point location4 = new Point(120, 120);
		DoublePoint doublePoint2 = METS.logicalNetLayer.getConverter().convertScreenToMap(location4);
		CreatePhysicalNodeCommandAtomic node2Command = new CreatePhysicalNodeCommandAtomic(link1, doublePoint2);
		node2Command.setLogicalNetLayer(METS.logicalNetLayer);
		node2Command.execute();
		TopologicalNode node2 = node2Command.getNode();
		link1.setEndNode(node2);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(link1, node1, node2);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		NodeLink nodeLink2 = nodeLink2Command.getNodeLink();

		//test itself
		DeletePhysicalLinkCommandBundle command = new DeletePhysicalLinkCommandBundle(link1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 0);
		assertEquals(physicalLinks.size(), 0);
		assertEquals(topologicalNodes.size(), 0);
		assertEquals(siteNodes.size(), 1);
	}

}
