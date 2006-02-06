/**
 * $Id: DeleteNodeCommandBundleTestCase.java,v 1.5 2005/08/24 10:19:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.DeleteNodeCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;

public class DeleteNodeCommandBundleTestCase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearMap();
	}

	/**
	 * (site)
	 * @throws Exception
	 */
	public void testDeleteSingleSite() throws Exception {
		//pre-test tasks
		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(type, location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		SiteNode site1 = site1Command.getSite();

		//test itself
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(site1);
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
		assertEquals(siteNodes.size(), 0);
	}

	/**
	 * site ----- (site)
	 * @throws Exception
	 */
	public void testDeleteSiteToSite() throws Exception {
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
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(site2);
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

	/**
	 * (site) ===== node
	 * @throws Exception
	 */
	public void testDeleteSiteDoubleToNode() throws Exception {
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

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(link1, node1, site1);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		NodeLink nodeLink2 = nodeLink2Command.getNodeLink();

		//test itself
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(site1);
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
		assertEquals(siteNodes.size(), 0);
	}

	/**
	 * (site) ----- node ----- node
	 * @throws Exception
	 */
	public void testDeleteSiteToActiveNode() throws Exception {
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
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(site1);
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
		assertEquals(siteNodes.size(), 0);

		assertTrue(physicalLinks.contains(link1));
		assertTrue(nodeLinks.contains(nodeLink2));
		assertTrue(topologicalNodes.contains(node1));
		assertTrue(topologicalNodes.contains(node2));
		assertFalse(node1.isActive());
		assertFalse(node2.isActive());
		assertSame(nodeLink2.getStartNode(), node1); 
		assertSame(nodeLink2.getEndNode(), node2); 
		assertSame(link1.getStartNode(), node1); 
		assertSame(link1.getEndNode(), node2);
		assertTrue(link1.getNodeLinks().contains(nodeLink2));
	}

	/**
	 * site     (node) ----- node
	 *
	 */
	public void testDeleteNodeToNode() throws Exception {
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
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node1);
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
	 * site ----- (node) ----- node
	 *
	 */
	public void testDeleteActiveNode() throws Exception {
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
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node1);
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
		assertFalse(node2.isActive());

		NodeLink nodeLink = (NodeLink )nodeLinks.iterator().next(); 
		
		assertTrue(physicalLinks.contains(link1));
		assertTrue(nodeLinks.contains(nodeLink));
		assertTrue(siteNodes.contains(site1));
		assertTrue(topologicalNodes.contains(node2));
		assertFalse(node2.isActive());
		ArrayList nodes = new ArrayList(2);
		nodes.add(site1);
		nodes.add(node2);
		assertTrue(nodes.contains(nodeLink.getStartNode())); 
		assertTrue(nodes.contains(nodeLink.getEndNode())); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), node2);
		assertEquals(link1.getNodeLinks().size(), 1);
		assertTrue(link1.getNodeLinks().contains(nodeLink));
	}

	/**
	 * site ----- (node)
	 * @throws Exception
	 */
	public void testDeleteNodeFromSite() throws Exception {
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
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node1);
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

	/**
	 * site ----- node ----- (node)
	 *
	 */
	public void testDeleteNonActiveNode() throws Exception {
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
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node2);
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

		assertTrue(physicalLinks.contains(link1));
		assertTrue(nodeLinks.contains(nodeLink1));
		assertTrue(siteNodes.contains(site1));
		assertTrue(topologicalNodes.contains(node1));
		assertFalse(node1.isActive());
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), node1);
		assertEquals(link1.getNodeLinks().size(), 1);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
	}

	/**
	 * site ===== (node)
	 * @throws Exception
	 */
	public void testDeleteNodeDoubleFromSite() throws Exception {
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

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(link1, node1, site1);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		NodeLink nodeLink2 = nodeLink2Command.getNodeLink();

		//test itself
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node1);
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
	 * site     (node) ===== (node)
	 *
	 */
	public void testDeleteNodeDoubleToNode() throws Exception {
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

		Point location4 = new Point(120, 120);
		DoublePoint doublePoint2 = METS.logicalNetLayer.getConverter().convertScreenToMap(location4);
		CreatePhysicalNodeCommandAtomic node2Command = new CreatePhysicalNodeCommandAtomic(link1, doublePoint2);
		node2Command.setLogicalNetLayer(METS.logicalNetLayer);
		node2Command.execute();
		TopologicalNode node2 = node2Command.getNode();
		link1.setStartNode(node1);
		link1.setEndNode(node1);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, node1, node2);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(link1, node1, node2);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		NodeLink nodeLink2 = nodeLink2Command.getNodeLink();

		//test itself
		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node1);
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

	public void testDeleteUnbound() {
	}

	public void testDeleteMark() {
	}

	public void testDeleteMarker() {
	}

}
