/**
 * $Id: CreateNodeLinkCommandBundleTestCase.java,v 1.5 2005/08/24 10:19:20 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;

public class CreateNodeLinkCommandBundleTestCase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearMap();
	}

	/**
	 * site ---->
	 * @throws Exception
	 */
	public void testCreateSiteToNewNode() throws Exception {
		//pre-test tasks
		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(type, location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		SiteNode site1 = site1Command.getSite();

		//test itself
		CreateNodeLinkCommandBundle command = new CreateNodeLinkCommandBundle(site1);
		command.setParameter(CreateNodeLinkCommandBundle.END_POINT, new Point(120, 20));
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
		
		TopologicalNode node1 = (TopologicalNode)topologicalNodes.iterator().next();
		NodeLink nodeLink1 = (NodeLink)nodeLinks.iterator().next();
		PhysicalLink link1 = (PhysicalLink)physicalLinks.iterator().next();
		
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), node1); 
		assertSame(nodeLink1.getPhysicalLink(), link1);
		assertEquals(link1.getNodeLinks().size(), 1);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
		assertSame(node1.getPhysicalLink(), link1);
	}

	/**
	 * site -----> site
	 * @throws Exception
	 */
	public void testCreateSiteToSite() throws Exception {
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

		//test itself
		CreateNodeLinkCommandBundle command = new CreateNodeLinkCommandBundle(site1);
		command.setParameter(CreateNodeLinkCommandBundle.END_POINT, new Point(220, 20));
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 1);
		assertEquals(physicalLinks.size(), 1);
		assertEquals(topologicalNodes.size(), 0);
		assertEquals(siteNodes.size(), 2);
		
		NodeLink nodeLink1 = (NodeLink)nodeLinks.iterator().next();
		PhysicalLink link1 = (PhysicalLink)physicalLinks.iterator().next();
		
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), site2); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), site2); 
		assertSame(nodeLink1.getPhysicalLink(), link1);
		assertEquals(link1.getNodeLinks().size(), 1);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
	}

	/**
	 * site ----- node ----->
	 * @throws Exception
	 */
	public void testCreateNodeToNewNode() throws Exception {
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
		link1.setEndNode(node1);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		//test itself
		CreateNodeLinkCommandBundle command = new CreateNodeLinkCommandBundle(node1);
		command.setParameter(CreateNodeLinkCommandBundle.END_POINT, new Point(120, 120));
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 2);
		assertEquals(physicalLinks.size(), 1);
		assertEquals(topologicalNodes.size(), 2);
		assertEquals(siteNodes.size(), 1);

		TopologicalNode node2 = null;
		for(Iterator iter = topologicalNodes.iterator(); iter.hasNext();) {
			TopologicalNode node = (TopologicalNode )iter.next();
			if(!(node.equals(node1))) {
				node2 = node;
				break;
			}
		}
		NodeLink nodeLink2 = null;
		for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
			NodeLink nodeLink = (NodeLink )iter.next();
			if(!(nodeLink.equals(nodeLink1))) {
				nodeLink2 = nodeLink;
				break;
			}
		}
		
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(nodeLink2.getStartNode(), node1); 
		assertSame(nodeLink2.getEndNode(), node2); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), node2); 
		assertSame(nodeLink1.getPhysicalLink(), link1);
		assertSame(nodeLink2.getPhysicalLink(), link1);
		assertEquals(link1.getNodeLinks().size(), 2);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
		assertTrue(link1.getNodeLinks().contains(nodeLink2));
		assertSame(node1.getPhysicalLink(), link1);
		assertSame(node2.getPhysicalLink(), link1);
	}

	/**
	 * site ----- node -----> site
	 * @throws Exception
	 */
	public void testCreateNodeToSite() throws Exception {
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
		link1.setEndNode(node1);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		Point location3 = new Point(220, 20);
		CreateSiteCommandAtomic site2Command = new CreateSiteCommandAtomic(type, location3);
		site2Command.setLogicalNetLayer(METS.logicalNetLayer);
		site2Command.execute();
		SiteNode site2 = site2Command.getSite();

		//test itself
		CreateNodeLinkCommandBundle command = new CreateNodeLinkCommandBundle(node1);
		command.setParameter(CreateNodeLinkCommandBundle.END_POINT, new Point(220, 20));
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
		for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
			NodeLink nodeLink = (NodeLink )iter.next();
			if(!(nodeLink.equals(nodeLink1))) {
				nodeLink2  = nodeLink;
				break;
			}
		}
		
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(nodeLink2.getStartNode(), node1); 
		assertSame(nodeLink2.getEndNode(), site2); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), site2); 
		assertSame(nodeLink1.getPhysicalLink(), link1);
		assertSame(nodeLink2.getPhysicalLink(), link1);
		assertEquals(link1.getNodeLinks().size(), 2);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
		assertTrue(link1.getNodeLinks().contains(nodeLink2));
		assertSame(node1.getPhysicalLink(), link1);
	}

	/**
	 * site ----- node <----- site
	 * @throws Exception
	 */
	public void testCreateSiteToNode() throws Exception {
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
		link1.setEndNode(node1);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		Point location3 = new Point(220, 20);
		CreateSiteCommandAtomic site2Command = new CreateSiteCommandAtomic(type, location3);
		site2Command.setLogicalNetLayer(METS.logicalNetLayer);
		site2Command.execute();
		SiteNode site2 = site2Command.getSite();

		//test itself
		CreateNodeLinkCommandBundle command = new CreateNodeLinkCommandBundle(site2);
		command.setParameter(CreateNodeLinkCommandBundle.END_POINT, new Point(120, 20));
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
		for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
			NodeLink nodeLink = (NodeLink )iter.next();
			if(!(nodeLink.equals(nodeLink1))) {
				nodeLink2 = nodeLink;
				break;
			}
		}
		
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(nodeLink2.getStartNode(), site2); 
		assertSame(nodeLink2.getEndNode(), node1); 
		assertSame(link1.getStartNode(), site2); 
		assertSame(link1.getEndNode(), site1); 
		assertSame(nodeLink1.getPhysicalLink(), link1);
		assertSame(nodeLink2.getPhysicalLink(), link1);
		assertEquals(link1.getNodeLinks().size(), 2);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
		assertTrue(link1.getNodeLinks().contains(nodeLink2));
		assertSame(node1.getPhysicalLink(), link1);
	}

	/**
	 * site1 ----- node1 -----> node2 ----- site2
	 * @throws Exception
	 */
	public void testCreateNodeToNode() throws Exception {
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
		link1.setEndNode(node1);

		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(link1, site1, node1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		NodeLink nodeLink1 = nodeLink1Command.getNodeLink();

		Point location3 = new Point(220, 20);
		CreateSiteCommandAtomic site2Command = new CreateSiteCommandAtomic(type, location3);
		site2Command.setLogicalNetLayer(METS.logicalNetLayer);
		site2Command.execute();
		SiteNode site2 = site2Command.getSite();

		CreatePhysicalLinkCommandAtomic link2Command = new CreatePhysicalLinkCommandAtomic(site2, site2);
		link2Command.setLogicalNetLayer(METS.logicalNetLayer);
		link2Command.execute();
		PhysicalLink link2 = link2Command.getLink();

		Point location4 = new Point(120, 120);
		DoublePoint doublePoint2 = METS.logicalNetLayer.getConverter().convertScreenToMap(location4);
		CreatePhysicalNodeCommandAtomic node2Command = new CreatePhysicalNodeCommandAtomic(link2, doublePoint2);
		node2Command.setLogicalNetLayer(METS.logicalNetLayer);
		node2Command.execute();
		TopologicalNode node2 = node2Command.getNode();
		link2.setEndNode(node2);

		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(link2, site2, node2);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		NodeLink nodeLink2 = nodeLink2Command.getNodeLink();

		//test itself
		CreateNodeLinkCommandBundle command = new CreateNodeLinkCommandBundle(node1);
		command.setParameter(CreateNodeLinkCommandBundle.END_POINT, location4);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		
		assertEquals(nodeLinks.size(), 3);
		assertEquals(physicalLinks.size(), 1);
		assertEquals(topologicalNodes.size(), 2);
		assertEquals(siteNodes.size(), 2);

		NodeLink nodeLink3 = null;
		for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
			NodeLink nodeLink = (NodeLink )iter.next();
			if(!(nodeLink.equals(nodeLink1))
					&& !(nodeLink.equals(nodeLink2))) {
				nodeLink3 = nodeLink;
				break;
			}
		}
		
		assertSame(nodeLink1.getStartNode(), site1); 
		assertSame(nodeLink1.getEndNode(), node1); 
		assertSame(nodeLink2.getStartNode(), site2); 
		assertSame(nodeLink2.getEndNode(), node2); 
		assertSame(nodeLink3.getStartNode(), node1); 
		assertSame(nodeLink3.getEndNode(), node2); 
		assertSame(link1.getStartNode(), site1); 
		assertSame(link1.getEndNode(), site2); 
		assertSame(nodeLink1.getPhysicalLink(), link1);
		assertSame(nodeLink2.getPhysicalLink(), link1);
		assertSame(nodeLink3.getPhysicalLink(), link1);
		assertEquals(link1.getNodeLinks().size(), 3);
		assertTrue(link1.getNodeLinks().contains(nodeLink1));
		assertTrue(link1.getNodeLinks().contains(nodeLink2));
		assertTrue(link1.getNodeLinks().contains(nodeLink3));
		assertSame(node1.getPhysicalLink(), link1);
		assertSame(node2.getPhysicalLink(), link1);
	}

}
