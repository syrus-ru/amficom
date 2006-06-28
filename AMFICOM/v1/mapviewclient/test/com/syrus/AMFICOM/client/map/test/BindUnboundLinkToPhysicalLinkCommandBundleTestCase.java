/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundleTestCase.java,v 1.6 2006/06/16 10:19:56 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.client.map.command.action.BindPhysicalNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.BindUnboundLinkToPhysicalLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

public class BindUnboundLinkToPhysicalLinkCommandBundleTestCase extends SchemeBindingTestCase {

	/**
	 *                  _____ 
	 * building1(start) ----- well1(end) ----- well2 ----- well3 ----- building2
	 * @throws ApplicationException 
	 */
	public void testSingleUnboundToPhysical() throws ApplicationException {
		//pre-test tasks
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		assertTrue(link instanceof UnboundLink);
		UnboundLink unboundLink = (UnboundLink)link;
		
		//test itself
		BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unboundLink, this.link1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();
		
		assertEquals(nodeLinks.size(), 4);
		assertEquals(physicalLinks.size(), 4);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		Set cciList = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciList.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), this.well1);
		assertSame(cci.getPhysicalLink(), this.link1);

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.well1);
		assertSame(cablePath.getFirstCCI(this.link1), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
	}

	/**
	 *                  ___ node ___ 
	 * building1(start) ------------ well1(end) ----- well2 ----- well3 ----- building2
	 * @throws ApplicationException 
	 */
	public void testSingleUnboundWithNodeToPhysical() throws ApplicationException {
		//pre-test tasks
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		nodeLinks.remove(this.nodeLink1);
		nodeLinks.remove(this.nodeLink2);
		nodeLinks.remove(this.nodeLink3);
		nodeLinks.remove(this.nodeLink4);
		
		NodeLink unboundNodeLink = (NodeLink )nodeLinks.iterator().next();
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		assertTrue(link instanceof UnboundLink);
		UnboundLink unboundLink = (UnboundLink)link;

		CreatePhysicalNodeCommandBundle nodecommand = new CreatePhysicalNodeCommandBundle(unboundNodeLink, new Point(60, 20));
		nodecommand.setNetMapViewer(METS.netMapViewer);
		nodecommand.execute();
		
		//test itself
		BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unboundLink, this.link1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();
		
		assertEquals(nodeLinks.size(), 4);
		assertEquals(physicalLinks.size(), 4);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		Set cciList = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciList.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), this.well1);
		assertSame(cci.getPhysicalLink(), this.link1);

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.well1);
		assertSame(cablePath.getFirstCCI(this.link1), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
	}

	/**
	 *                  ____________ 
	 * building1(start) --- node --- well1(end) ----- well2 ----- well3 ----- building2
	 * @throws ApplicationException 
	 */
	public void testSingleUnboundToPhysicalWithNode() throws ApplicationException {
		//pre-test tasks
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		assertTrue(link instanceof UnboundLink);
		UnboundLink unboundLink = (UnboundLink)link;

		CreatePhysicalNodeCommandBundle nodecommand = new CreatePhysicalNodeCommandBundle(this.nodeLink1, new Point(60, 20));
		nodecommand.setNetMapViewer(METS.netMapViewer);
		nodecommand.execute();
		
		//test itself
		BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unboundLink, this.link1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();
		
		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 4);
		assertEquals(topologicalNodes.size(), 1);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		Set cciList = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciList.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), this.well1);
		assertSame(cci.getPhysicalLink(), this.link1);

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.well1);
		assertSame(cablePath.getFirstCCI(this.link1), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
	}

	/**
	 *                  _____ unbound(end)
	 * building1(start) ----- well1 ----- well2 ----- well3 ----- building2
	 * @throws ApplicationException 
	 */
	public void testSingleUnboundSite() throws ApplicationException {
		//pre-test tasks
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, new Point(120, 120));
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		assertTrue(link instanceof UnboundLink);
		UnboundLink unboundLink = (UnboundLink)link;

		//test itself
		BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unboundLink, this.link1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		assertEquals(command.getResult(), Command.RESULT_NO);
		
		//assertions
		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = new ArrayList(METS.map.getSiteNodes());
		Collection cablePaths = METS.mapView.getCablePaths();
		
		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 6);
		assertEquals(cablePaths.size(), 1);

		siteNodes.remove(this.building1);
		siteNodes.remove(this.well1);
		siteNodes.remove(this.well2);
		siteNodes.remove(this.well3);
		siteNodes.remove(this.building2);
		
		SiteNode site = (SiteNode )siteNodes.iterator().next();
		
		assertTrue(site instanceof UnboundNode);
		
		UnboundNode unboundSite = (UnboundNode)site;
		
		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		Set cciList = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciList.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), unboundSite);
		assertNull(cci.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), unboundSite);
		assertSame(cablePath.getFirstCCI(unboundLink), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
	}

	/**
	 *                  _____ 
	 * building1(start) ----- well1(end) ----- well2 ----- well3 ----- building2
	 * @throws ApplicationException 
	 */
	public void testSingleUnboundToInconsequent() throws ApplicationException {
		//pre-test tasks
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		assertTrue(link instanceof UnboundLink);
		UnboundLink unboundLink = (UnboundLink)link;
		
		//test itself
		BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unboundLink, this.link2);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		assertEquals(command.getResult(), Command.RESULT_NO);

		//assertions
		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();
		
		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		Set cciList = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciList.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), this.well1);
		assertNull(cci.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.well1);
		assertSame(cablePath.getFirstCCI(unboundLink), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
	}

	/**
	 *                  _____                _____________________________ 
	 * building1(start) ----- well1(interim) ----- well2 ----- well3 ----- building2(end)
	 * @throws ApplicationException 
	 */
	public void testDoubleUnboundToPhysical() throws ApplicationException {
		//pre-test tasks
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		nodeLinks.remove(this.nodeLink1);
		nodeLinks.remove(this.nodeLink2);
		nodeLinks.remove(this.nodeLink3);
		nodeLinks.remove(this.nodeLink4);
		
		NodeLink unboundNodeLink = (NodeLink )nodeLinks.iterator().next();
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		assertTrue(link instanceof UnboundLink);

		CreatePhysicalNodeCommandBundle nodecommand = new CreatePhysicalNodeCommandBundle(unboundNodeLink, new Point(120, 120));
		nodecommand.setNetMapViewer(METS.netMapViewer);
		nodecommand.execute();

		Collection topologicalNodes = METS.map.getTopologicalNodes();
		assertEquals(topologicalNodes.size(), 1);

		TopologicalNode node = (TopologicalNode)topologicalNodes.iterator().next();
		assertSame(node.getPhysicalLink(), link);

		BindPhysicalNodeToSiteCommandBundle bindcommand = new BindPhysicalNodeToSiteCommandBundle(node, this.well1);
		bindcommand.setNetMapViewer(METS.netMapViewer);
		bindcommand.execute();

		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		Iterator linksIterator = physicalLinks.iterator();
		PhysicalLink newlink1 = (PhysicalLink )linksIterator.next();
		PhysicalLink newlink2 = (PhysicalLink )linksIterator.next();
		
		UnboundLink unboundLink1;
		UnboundLink unboundLink2;
		
		if(newlink1.getStartNode().equals(this.building1)) {
			unboundLink1 = (UnboundLink )newlink1;
			unboundLink2 = (UnboundLink )newlink2;
		}
		else {
			unboundLink1 = (UnboundLink )newlink2;
			unboundLink2 = (UnboundLink )newlink1;
		}

		//test itself
		BindUnboundLinkToPhysicalLinkCommandBundle command = new BindUnboundLinkToPhysicalLinkCommandBundle(unboundLink1, this.link1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();
		
		//assertions
		nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();
		
		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(topologicalNodes.size(), 0);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		nodeLinks.remove(this.nodeLink1);
		nodeLinks.remove(this.nodeLink2);
		nodeLinks.remove(this.nodeLink3);
		nodeLinks.remove(this.nodeLink4);
		
		NodeLink nodeLink = (NodeLink )nodeLinks.iterator().next();
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink newlink = (PhysicalLink )physicalLinks.iterator().next();
		assertTrue(newlink2 instanceof UnboundLink);
		
		UnboundLink unboundLink = (UnboundLink)newlink;

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink.getStartNode(), this.well1); 
		assertSame(unboundLink.getEndNode(), this.building2);
		assertSame(nodeLink.getStartNode(), this.well1); 
		assertSame(nodeLink.getEndNode(), this.building2);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(nodeLink));
		assertSame(nodeLink.getPhysicalLink(), unboundLink);
		
		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 2);

		Iterator cciIterator = cciList.iterator();
		CableChannelingItem cci1;
		CableChannelingItem cci2;
		cci1 = (CableChannelingItem )cciIterator.next();
		if(cci1.getStartSiteNode().equals(this.building1))
			cci2 = (CableChannelingItem )cciIterator.next();
		else {
			cci2 = cci1;
			cci1 = (CableChannelingItem )cciIterator.next();
		}

		assertSame(cci1.getStartSiteNode(), this.building1);
		assertSame(cci1.getEndSiteNode(), this.well1);
		assertSame(cci1.getPhysicalLink(), this.link1);

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.building2);
		assertNull(cci2.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(this.link1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink2), cci2);
		assertEquals(cablePath.getLinks().size(), 2);
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertTrue(cablePath.getLinks().contains(unboundLink2));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci1);
		assertSame(cablePath.getStartUnboundNode(), this.well1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}

}
