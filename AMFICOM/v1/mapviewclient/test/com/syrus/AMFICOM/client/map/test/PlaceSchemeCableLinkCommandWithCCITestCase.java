/**
 * $Id: PlaceSchemeCableLinkCommandWithCCITestCase.java,v 1.8 2006/06/16 10:19:56 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

/**
 * building(start) ----- well1 ........... building(end)
 * building(start) ----- well1 ........... well3 ----- building(end)
 * building(start) ----- well1 ..... well2 ----- well3 ..... building(end)
 * building(start) ..... well1 ----- well2 ----- well3 ..... building(end)
 * building(start) ----- well1 ----- well2 ----- well1 ----- well2 ----- well3 ----- building(end)
 * building(start) ..... well1 ........... building(end)
 * building(start) ..... well1 ........... well3 ..... building(end)
 *
 */
public class PlaceSchemeCableLinkCommandWithCCITestCase extends SchemeBindingTestCase {

	/**
	 * building(start) ----- well1 ........... building(end)
	 *
	 */
	public void testSingleCCI() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1, 
				this.link1);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

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

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink.getStartNode(), this.well1); 
		assertSame(unboundLink.getEndNode(), this.building2);
		assertSame(unboundNodeLink.getStartNode(), this.well1); 
		assertSame(unboundNodeLink.getEndNode(), this.building2);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(unboundNodeLink));
		assertSame(unboundNodeLink.getPhysicalLink(), unboundLink);
		
		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 2);
		cciList.remove(cci1);
		CableChannelingItem cci2 = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.building2);
		assertNull(cci2.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(this.link1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink), cci2);
		assertEquals(cablePath.getLinks().size(), 2);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci1);
		assertSame(cablePath.getStartUnboundNode(), this.well1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}

	/**
	 * building(start) ----- well1 ........... building(end)
	 *
	 */
	public void testDoubleCCISinglePhysical() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1, 
				this.link1);

		CableChannelingItem cci2 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well1, 
				this.building2);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

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

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink.getStartNode(), this.well1); 
		assertSame(unboundLink.getEndNode(), this.building2);
		assertSame(unboundNodeLink.getStartNode(), this.well1); 
		assertSame(unboundNodeLink.getEndNode(), this.building2);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(unboundNodeLink));
		assertSame(unboundNodeLink.getPhysicalLink(), unboundLink);
		
		Set cciSet = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciSet.size(), 2);
		assertTrue(cciSet.contains(cci1));
		assertTrue(cciSet.contains(cci2));

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.building2);
		assertNull(cci2.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(this.link1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink), cci2);
		assertEquals(cablePath.getLinks().size(), 2);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci1);
		assertSame(cablePath.getStartUnboundNode(), this.well1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}

	/**
	 * building(start) ----- well1 ........... well3 ----- building(end)
	 *
	 */
	public void testDoubleCCI() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1, 
				this.link1);

		CableChannelingItem cci3 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well3, 
				this.building2, 
				this.link4);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

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

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink.getStartNode(), this.well1); 
		assertSame(unboundLink.getEndNode(), this.well3);
		assertSame(unboundNodeLink.getStartNode(), this.well1); 
		assertSame(unboundNodeLink.getEndNode(), this.well3);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(unboundNodeLink));
		assertSame(unboundNodeLink.getPhysicalLink(), unboundLink);
		
		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 3);
		cciList.remove(cci1);
		cciList.remove(cci3);
		CableChannelingItem cci2 = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.well3);
		assertNull(cci2.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(this.link1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink), cci2);
		assertSame(cablePath.getFirstCCI(this.link4), cci3);
		assertEquals(cablePath.getLinks().size(), 3);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertTrue(cablePath.getLinks().contains(this.link4));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci1);
		assertSame(cablePath.getStartUnboundNode(), this.well1);
		assertSame(cablePath.getEndLastBoundLink(), cci3);
		assertSame(cablePath.getEndUnboundNode(), this.well3);
	}

	/**
	 * building(start) ----- well1 ........... well3 ----- building(end)
	 *
	 */
	public void testTripleCCISingleUnbound() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1, 
				this.link1);

		CableChannelingItem cci2 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well1, 
				this.well3);

		CableChannelingItem cci3 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well3, 
				this.building2, 
				this.link4);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

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

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink.getStartNode(), this.well1); 
		assertSame(unboundLink.getEndNode(), this.well3);
		assertSame(unboundNodeLink.getStartNode(), this.well1); 
		assertSame(unboundNodeLink.getEndNode(), this.well3);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(unboundNodeLink));
		assertSame(unboundNodeLink.getPhysicalLink(), unboundLink);
		
		Set cciSet = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciSet.size(), 3);
		assertTrue(cciSet.contains(cci1));
		assertTrue(cciSet.contains(cci2));
		assertTrue(cciSet.contains(cci3));

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.well3);
		assertNull(cci2.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(this.link1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink), cci2);
		assertSame(cablePath.getFirstCCI(this.link4), cci3);
		assertEquals(cablePath.getLinks().size(), 3);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertTrue(cablePath.getLinks().contains(this.link4));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci1);
		assertSame(cablePath.getStartUnboundNode(), this.well1);
		assertSame(cablePath.getEndLastBoundLink(), cci3);
		assertSame(cablePath.getEndUnboundNode(), this.well3);
	}

	/**
	 * building(start) ----- well1 ..... well2 ----- well3 ..... building(end)
	 */
	public void testDoubleCCISingleHanging() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1, 
				this.link1);

		CableChannelingItem cci3 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well2, 
				this.well3, 
				this.link3);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 6);
		assertEquals(physicalLinks.size(), 6);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		nodeLinks.remove(this.nodeLink1);
		nodeLinks.remove(this.nodeLink2);
		nodeLinks.remove(this.nodeLink3);
		nodeLinks.remove(this.nodeLink4);
		
		Iterator nodeLinkIterator = nodeLinks.iterator();
		NodeLink unboundNodeLink1;
		NodeLink unboundNodeLink2;

		unboundNodeLink1 = (NodeLink )nodeLinkIterator.next();
		if(unboundNodeLink1.getStartNode().equals(this.well1))
			unboundNodeLink2 = (NodeLink )nodeLinkIterator.next();
		else {
			unboundNodeLink2 = unboundNodeLink1;
			unboundNodeLink1 = (NodeLink )nodeLinkIterator.next();
		}
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		Iterator linksIterator = physicalLinks.iterator();
		PhysicalLink newlink1 = (PhysicalLink )linksIterator.next();
		PhysicalLink newlink2 = (PhysicalLink )linksIterator.next();
		assertNotSame(newlink1, newlink2);
		
		assertTrue(newlink1 instanceof UnboundLink);
		assertTrue(newlink2 instanceof UnboundLink);
		
		UnboundLink unboundLink1 = (UnboundLink)unboundNodeLink1.getPhysicalLink();
		UnboundLink unboundLink2 = (UnboundLink)unboundNodeLink2.getPhysicalLink();

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink1.getStartNode(), this.well1); 
		assertSame(unboundLink1.getEndNode(), this.well2);
		assertSame(unboundNodeLink1.getStartNode(), this.well1); 
		assertSame(unboundNodeLink1.getEndNode(), this.well2);
		assertEquals(unboundLink1.getNodeLinks().size(), 1);
		assertTrue(unboundLink1.getNodeLinks().contains(unboundNodeLink1));
		assertSame(unboundNodeLink1.getPhysicalLink(), unboundLink1);
		
		assertSame(unboundLink2.getStartNode(), this.well3); 
		assertSame(unboundLink2.getEndNode(), this.building2);
		assertSame(unboundNodeLink2.getStartNode(), this.well3); 
		assertSame(unboundNodeLink2.getEndNode(), this.building2);
		assertEquals(unboundLink2.getNodeLinks().size(), 1);
		assertTrue(unboundLink2.getNodeLinks().contains(unboundNodeLink2));
		assertSame(unboundNodeLink2.getPhysicalLink(), unboundLink2);

		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 4);
		cciList.remove(cci1);
		cciList.remove(cci3);

		CableChannelingItem cci2 = cablePath.getFirstCCI(unboundLink1);
		CableChannelingItem cci4 = cablePath.getFirstCCI(unboundLink2);

		assertTrue(cciList.contains(cci2));
		assertTrue(cciList.contains(cci4));

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.well2);
		assertNull(cci2.getPhysicalLink());

		assertSame(cci4.getStartSiteNode(), this.well3);
		assertSame(cci4.getEndSiteNode(), this.building2);
		assertNull(cci4.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(this.link1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink1), cci2);
		assertSame(cablePath.getFirstCCI(this.link3), cci3);
		assertSame(cablePath.getFirstCCI(unboundLink2), cci4);
		assertEquals(cablePath.getLinks().size(), 4);
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertTrue(cablePath.getLinks().contains(unboundLink1));
		assertTrue(cablePath.getLinks().contains(this.link3));
		assertTrue(cablePath.getLinks().contains(unboundLink2));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci1);
		assertSame(cablePath.getStartUnboundNode(), this.well1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}

	/**
	 * building(start) ..... well1 ----- well2 ----- well3 ..... building(end)
	 */
	public void testDoubleCCIBothHanging() throws Exception {
		CableChannelingItem cci2 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well1, 
				this.well2, 
				this.link2);

		CableChannelingItem cci3 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well2, 
				this.well3, 
				this.link3);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 6);
		assertEquals(physicalLinks.size(), 6);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		nodeLinks.remove(this.nodeLink1);
		nodeLinks.remove(this.nodeLink2);
		nodeLinks.remove(this.nodeLink3);
		nodeLinks.remove(this.nodeLink4);
		
		Iterator nodeLinkIterator = nodeLinks.iterator();
		NodeLink unboundNodeLink1;
		NodeLink unboundNodeLink2;

		unboundNodeLink1 = (NodeLink )nodeLinkIterator.next();
		if(unboundNodeLink1.getStartNode().equals(this.building1))
			unboundNodeLink2 = (NodeLink )nodeLinkIterator.next();
		else {
			unboundNodeLink2 = unboundNodeLink1;
			unboundNodeLink1 = (NodeLink )nodeLinkIterator.next();
		}
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		Iterator linksIterator = physicalLinks.iterator();
		PhysicalLink newlink1 = (PhysicalLink )linksIterator.next();
		PhysicalLink newlink2 = (PhysicalLink )linksIterator.next();
		assertNotSame(newlink1, newlink2);
		
		assertTrue(newlink1 instanceof UnboundLink);
		assertTrue(newlink2 instanceof UnboundLink);
		
		UnboundLink unboundLink1 = (UnboundLink)unboundNodeLink1.getPhysicalLink();
		UnboundLink unboundLink2 = (UnboundLink)unboundNodeLink2.getPhysicalLink();

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink1.getStartNode(), this.building1); 
		assertSame(unboundLink1.getEndNode(), this.well1);
		assertSame(unboundNodeLink1.getStartNode(), this.building1); 
		assertSame(unboundNodeLink1.getEndNode(), this.well1);
		assertEquals(unboundLink1.getNodeLinks().size(), 1);
		assertTrue(unboundLink1.getNodeLinks().contains(unboundNodeLink1));
		assertSame(unboundNodeLink1.getPhysicalLink(), unboundLink1);
		
		assertSame(unboundLink2.getStartNode(), this.well3); 
		assertSame(unboundLink2.getEndNode(), this.building2);
		assertSame(unboundNodeLink2.getStartNode(), this.well3); 
		assertSame(unboundNodeLink2.getEndNode(), this.building2);
		assertEquals(unboundLink2.getNodeLinks().size(), 1);
		assertTrue(unboundLink2.getNodeLinks().contains(unboundNodeLink2));
		assertSame(unboundNodeLink2.getPhysicalLink(), unboundLink2);

		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 4);
		cciList.remove(cci2);
		cciList.remove(cci3);

		CableChannelingItem cci1 = cablePath.getFirstCCI(unboundLink1);
		CableChannelingItem cci4 = cablePath.getFirstCCI(unboundLink2);

		assertTrue(cciList.contains(cci1));
		assertTrue(cciList.contains(cci4));

		assertSame(cci1.getStartSiteNode(), this.building1);
		assertSame(cci1.getEndSiteNode(), this.well1);
		assertNull(cci1.getPhysicalLink());

		assertSame(cci4.getStartSiteNode(), this.well3);
		assertSame(cci4.getEndSiteNode(), this.building2);
		assertNull(cci4.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(unboundLink1), cci1);
		assertSame(cablePath.getFirstCCI(this.link2), cci2);
		assertSame(cablePath.getFirstCCI(this.link3), cci3);
		assertSame(cablePath.getFirstCCI(unboundLink2), cci4);
		assertEquals(cablePath.getLinks().size(), 4);
		assertTrue(cablePath.getLinks().contains(unboundLink1));
		assertTrue(cablePath.getLinks().contains(this.link2));
		assertTrue(cablePath.getLinks().contains(this.link3));
		assertTrue(cablePath.getLinks().contains(unboundLink2));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertNull(cablePath.getStartLastBoundLink());
		assertSame(cablePath.getStartUnboundNode(), this.building1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}

	/**
	 * building(start) ----- well1 ----- well2 ----- well1 ----- well2 ----- well3 ----- building(end)
	 */
	public void testSixCCI() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1, 
				this.link1);

		CableChannelingItem cci2 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well1, 
				this.well2, 
				this.link2);

		CableChannelingItem cci3 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well2, 
				this.well1, 
				this.link2);

		CableChannelingItem cci4 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well1, 
				this.well2, 
				this.link2);

		CableChannelingItem cci5 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well2, 
				this.well3, 
				this.link3);

		CableChannelingItem cci6 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well3, 
				this.building2, 
				this.link4);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 4);
		assertEquals(physicalLinks.size(), 4);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		Set<CableChannelingItem> cciList = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciList.size(), 6);

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertEquals(cablePath.getLinks().size(), 6);
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci6);
		assertSame(cablePath.getStartUnboundNode(), this.building2);
		assertSame(cablePath.getEndLastBoundLink(), cci1);
		assertSame(cablePath.getEndUnboundNode(), this.building1);
	}

	/**
	 * building(start) ..... well1 ........... building(end)
	 *
	 */
	public void testSingleCCIUnbound() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 6);
		assertEquals(physicalLinks.size(), 6);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		nodeLinks.remove(this.nodeLink1);
		nodeLinks.remove(this.nodeLink2);
		nodeLinks.remove(this.nodeLink3);
		nodeLinks.remove(this.nodeLink4);
		
		Iterator nodeLinkIterator = nodeLinks.iterator();
		NodeLink unboundNodeLink1;
		NodeLink unboundNodeLink2;

		unboundNodeLink1 = (NodeLink )nodeLinkIterator.next();
		if(unboundNodeLink1.getStartNode().equals(this.building1))
			unboundNodeLink2 = (NodeLink )nodeLinkIterator.next();
		else {
			unboundNodeLink2 = unboundNodeLink1;
			unboundNodeLink1 = (NodeLink )nodeLinkIterator.next();
		}
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		Iterator linksIterator = physicalLinks.iterator();
		PhysicalLink newlink1 = (PhysicalLink )linksIterator.next();
		PhysicalLink newlink2 = (PhysicalLink )linksIterator.next();
		assertNotSame(newlink1, newlink2);
		
		assertTrue(newlink1 instanceof UnboundLink);
		assertTrue(newlink2 instanceof UnboundLink);
		
		UnboundLink unboundLink1 = (UnboundLink)unboundNodeLink1.getPhysicalLink();
		UnboundLink unboundLink2 = (UnboundLink)unboundNodeLink2.getPhysicalLink();

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink1.getStartNode(), this.building1); 
		assertSame(unboundLink1.getEndNode(), this.well1);
		assertSame(unboundNodeLink1.getStartNode(), this.building1); 
		assertSame(unboundNodeLink1.getEndNode(), this.well1);
		assertEquals(unboundLink1.getNodeLinks().size(), 1);
		assertTrue(unboundLink1.getNodeLinks().contains(unboundNodeLink1));
		assertSame(unboundNodeLink1.getPhysicalLink(), unboundLink1);
		
		assertSame(unboundLink2.getStartNode(), this.well1); 
		assertSame(unboundLink2.getEndNode(), this.building2);
		assertSame(unboundNodeLink2.getStartNode(), this.well1); 
		assertSame(unboundNodeLink2.getEndNode(), this.building2);
		assertEquals(unboundLink2.getNodeLinks().size(), 1);
		assertTrue(unboundLink2.getNodeLinks().contains(unboundNodeLink2));
		assertSame(unboundNodeLink2.getPhysicalLink(), unboundLink2);
		
		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 2);
		cciList.remove(cci1);
		CableChannelingItem cci2 = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.building2);
		assertNull(cci2.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(unboundLink1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink2), cci2);
		assertEquals(cablePath.getLinks().size(), 2);
		assertTrue(cablePath.getLinks().contains(unboundLink1));
		assertTrue(cablePath.getLinks().contains(unboundLink2));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertNull(cablePath.getStartLastBoundLink());
		assertSame(cablePath.getStartUnboundNode(), this.building1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}

	/**
	 * building(start) ..... well1 ........... well3 ..... building(end)
	 *
	 */
	public void testDoubleCCIUnbound() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1);

		CableChannelingItem cci3 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.well3, 
				this.building2);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 7);
		assertEquals(physicalLinks.size(), 7);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 1);

		nodeLinks.remove(this.nodeLink1);
		nodeLinks.remove(this.nodeLink2);
		nodeLinks.remove(this.nodeLink3);
		nodeLinks.remove(this.nodeLink4);
		
		NodeLink unboundNodeLink1 = null;
		NodeLink unboundNodeLink2 = null;
		NodeLink unboundNodeLink3 = null;

		for(Iterator nodeLinkIterator = nodeLinks.iterator();nodeLinkIterator.hasNext();) {
			NodeLink nl = (NodeLink )nodeLinkIterator.next();
			if(nl.getStartNode().equals(this.building1))
				unboundNodeLink1 = nl;
			if(nl.getStartNode().equals(this.well1))
				unboundNodeLink2 = nl;
			if(nl.getStartNode().equals(this.well3))
				unboundNodeLink3 = nl;
		}
		assertNotNull(unboundNodeLink1);
		assertNotNull(unboundNodeLink2);
		assertNotNull(unboundNodeLink3);
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		Iterator linksIterator = physicalLinks.iterator();
		PhysicalLink newlink1 = (PhysicalLink )linksIterator.next();
		PhysicalLink newlink2 = (PhysicalLink )linksIterator.next();
		PhysicalLink newlink3 = (PhysicalLink )linksIterator.next();
		assertNotSame(newlink1, newlink2);
		assertNotSame(newlink2, newlink3);
		assertNotSame(newlink3, newlink1);
		
		assertTrue(newlink1 instanceof UnboundLink);
		assertTrue(newlink2 instanceof UnboundLink);
		assertTrue(newlink3 instanceof UnboundLink);
		
		UnboundLink unboundLink1 = (UnboundLink)unboundNodeLink1.getPhysicalLink();
		UnboundLink unboundLink2 = (UnboundLink)unboundNodeLink2.getPhysicalLink();
		UnboundLink unboundLink3 = (UnboundLink)unboundNodeLink3.getPhysicalLink();

		CablePath cablePath = (CablePath)cablePaths.iterator().next();
		
		assertSame(unboundLink1.getStartNode(), this.building1); 
		assertSame(unboundLink1.getEndNode(), this.well1);
		assertSame(unboundNodeLink1.getStartNode(), this.building1); 
		assertSame(unboundNodeLink1.getEndNode(), this.well1);
		assertEquals(unboundLink1.getNodeLinks().size(), 1);
		assertTrue(unboundLink1.getNodeLinks().contains(unboundNodeLink1));
		assertSame(unboundNodeLink1.getPhysicalLink(), unboundLink1);
		
		assertSame(unboundLink2.getStartNode(), this.well1); 
		assertSame(unboundLink2.getEndNode(), this.well3);
		assertSame(unboundNodeLink2.getStartNode(), this.well1); 
		assertSame(unboundNodeLink2.getEndNode(), this.well3);
		assertEquals(unboundLink2.getNodeLinks().size(), 1);
		assertTrue(unboundLink2.getNodeLinks().contains(unboundNodeLink2));
		assertSame(unboundNodeLink2.getPhysicalLink(), unboundLink2);
		
		assertSame(unboundLink3.getStartNode(), this.well3); 
		assertSame(unboundLink3.getEndNode(), this.building2);
		assertSame(unboundNodeLink3.getStartNode(), this.well3); 
		assertSame(unboundNodeLink3.getEndNode(), this.building2);
		assertEquals(unboundLink3.getNodeLinks().size(), 1);
		assertTrue(unboundLink3.getNodeLinks().contains(unboundNodeLink3));
		assertSame(unboundNodeLink3.getPhysicalLink(), unboundLink3);
		
		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 3);
		cciList.remove(cci1);
		cciList.remove(cci3);
		CableChannelingItem cci2 = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.well3);
		assertNull(cci2.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(unboundLink1), cci1);
		assertSame(cablePath.getFirstCCI(unboundLink2), cci2);
		assertSame(cablePath.getFirstCCI(unboundLink3), cci3);
		assertEquals(cablePath.getLinks().size(), 3);
		assertTrue(cablePath.getLinks().contains(unboundLink1));
		assertTrue(cablePath.getLinks().contains(unboundLink2));
		assertTrue(cablePath.getLinks().contains(unboundLink3));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);

		assertNull(cablePath.getStartLastBoundLink());
		assertSame(cablePath.getStartUnboundNode(), this.building1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}
}
