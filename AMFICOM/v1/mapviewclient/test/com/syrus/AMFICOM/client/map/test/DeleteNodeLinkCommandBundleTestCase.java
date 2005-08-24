/**
 * $Id: DeleteNodeLinkCommandBundleTestCase.java,v 1.5 2005/08/24 10:19:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.DeleteNodeLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;

public class DeleteNodeLinkCommandBundleTestCase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearMap();
	}

	/**
	 * site (-----) site
	 *
	 */
	public void testRemoveNodeLinkBetweenTwoSites() throws Exception {
		//pre-test tasks
		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(type, location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		SiteNode site1 = site1Command.getSite();

		Point location2 = new Point(220, 20);
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
		DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(nodeLink1);
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
		assertEquals(siteNodes.size(), 2);

		assertTrue(siteNodes.contains(site1));
		assertTrue(siteNodes.contains(site2));
	}

	/**
	 * site     node (-----) node
	 *
	 */
	public void testRemoveNodeLinkBetweenInactiveNodes() throws Exception {
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
		DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(nodeLink1);
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
	 * site ----- node (-----) node ----- node
	 * @throws Exception
	 */
	public void testRemoveNodeLinkBetweenActiveNodes() throws Exception {
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
		node2.setActive(true);

		Point location5 = new Point(220, 120);
		DoublePoint doublePoint3 = METS.logicalNetLayer.getConverter().convertScreenToMap(location5);
		CreatePhysicalNodeCommandAtomic node3Command = new CreatePhysicalNodeCommandAtomic(link1, doublePoint3);
		node3Command.setLogicalNetLayer(METS.logicalNetLayer);
		node3Command.execute();
		TopologicalNode node3 = node3Command.getNode();
		link1.setEndNode(node3);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(link1, node1, node2);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		NodeLink nodeLink2 = nodeLink2Command.getNodeLink();

		CreateNodeLinkCommandAtomic nodeLink3Command = new CreateNodeLinkCommandAtomic(link1, node2, node3);
		nodeLink3Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink3Command.execute();
		NodeLink nodeLink3 = nodeLink3Command.getNodeLink();

		//test itself
		DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(nodeLink2);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 2);
		assertEquals(physicalLinks.size(), 2);
		assertEquals(topologicalNodes.size(), 3);
		assertEquals(siteNodes.size(), 1);

		PhysicalLink link2 = null;
		for(Iterator iter = physicalLinks.iterator(); iter.hasNext();) {
			PhysicalLink link = (PhysicalLink )iter.next();
			if(!(link.equals(link1))) {
				link2 = link;
				break;
			}
		}
		assertTrue(nodeLinks.contains(nodeLink1));
		assertTrue(nodeLinks.contains(nodeLink3));
		assertTrue(physicalLinks.contains(link1));
		assertTrue(topologicalNodes.contains(node1));
		assertTrue(topologicalNodes.contains(node2));
		assertTrue(topologicalNodes.contains(node3));
		assertTrue(siteNodes.contains(site1));
		assertFalse(node1.isActive());
		assertFalse(node2.isActive());
		assertFalse(node3.isActive());
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(nodeLink3.getStartNode(), node2); 
		assertSame(nodeLink3.getEndNode(), node3); 
		assertSame(link1.getStartNode(), node2); 
		assertSame(link1.getEndNode(), node3);
		assertSame(link2.getStartNode(), site1); 
		assertSame(link2.getEndNode(), node1);
		assertTrue(link1.getNodeLinks().contains(nodeLink3));
		assertTrue(link2.getNodeLinks().contains(nodeLink1));
	}

	/**
	 * site ----- node (-----) node
	 * @throws Exception
	 */
	public void testRemoveNodeLinkBetweenActiveAndInactive() throws Exception {
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
		DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(nodeLink2);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 1);
		assertEquals(physicalLinks.size(), 1);
		assertEquals(topologicalNodes.size(), 1);
		assertEquals(siteNodes.size(), 1);

		assertTrue(nodeLinks.contains(nodeLink1));
		assertTrue(physicalLinks.contains(link1));
		assertTrue(topologicalNodes.contains(node1));
		assertTrue(siteNodes.contains(site1));
		assertFalse(node1.isActive());
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), node1);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
	}

	/**
	 * site (-----) node ----- node
	 * @throws Exception
	 */
	public void testRemoveNodeLinkBetweenSiteAndActiveNode() throws Exception {
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
		DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(nodeLink1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 1);
		assertEquals(physicalLinks.size(), 1);
		assertEquals(topologicalNodes.size(), 2);
		assertEquals(siteNodes.size(), 1);

		assertTrue(nodeLinks.contains(nodeLink2));
		assertTrue(physicalLinks.contains(link1));
		assertTrue(topologicalNodes.contains(node1));
		assertTrue(topologicalNodes.contains(node2));
		assertTrue(siteNodes.contains(site1));
		assertFalse(node1.isActive());
		assertFalse(node2.isActive());
		assertSame(nodeLink2.getStartNode(), node1); 
		assertSame(nodeLink2.getEndNode(), node2); 
		assertSame(link1.getStartNode(), node1); 
		assertSame(link1.getEndNode(), node2);
		assertTrue(link1.getNodeLinks().contains(nodeLink2));
	}

	/**
	 * site (-----) node
	 *
	 */
	public void testRemoveNodeLinkBetweenSiteAndInactiveNode() throws Exception {
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

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		//test itself
		DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(nodeLink1);
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

		assertTrue(siteNodes.contains(site1));
	}

}
