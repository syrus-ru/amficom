/**
 * $Id: GenerateUnboundLinkCablingCommandBundleTestCase.java,v 1.8 2005/10/25 08:02:45 krupenn Exp $
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

import com.syrus.AMFICOM.client.map.command.action.GenerateUnboundLinkCablingCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeSampleData;

public class GenerateUnboundLinkCablingCommandBundleTestCase extends SchemeBindingTestCase {

	public void testUnboundBetweenTwoUnbound() {
		Point unbound1location = new Point(120, 120);
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, unbound1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		Point unbound2location = new Point(120, 220);
		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, unbound2location);
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

		GenerateUnboundLinkCablingCommandBundle command = new GenerateUnboundLinkCablingCommandBundle(unboundLink);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		assertEquals(command.getResult(), Command.RESULT_NO);
		
		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 7);
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

		link = (PhysicalLink )physicalLinks.iterator().next();

		assertTrue(link instanceof UnboundLink);
		assertSame(link, unboundLink);
		assertTrue(link.getNodeLinks().contains(nodeLink));
		assertSame(nodeLink.getPhysicalLink(), link);
	}

	public void testUnboundBetweenUnboundAndSite1() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1, 
				this.link1);

		Point unbound1location = new Point(120, 120);
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, unbound1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		Point unbound2location = new Point(120, 220);
		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, unbound2location);
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
		
		Iterator linkIterator = physicalLinks.iterator();
		PhysicalLink newLink1 = (PhysicalLink )linkIterator.next();
		PhysicalLink newLink2 = (PhysicalLink )linkIterator.next();

		assertTrue(newLink1 instanceof UnboundLink);
		assertTrue(newLink2 instanceof UnboundLink);
		
		UnboundLink unboundLink1 = (UnboundLink)newLink1;
		UnboundLink unboundLink2 = (UnboundLink)newLink2;

		GenerateUnboundLinkCablingCommandBundle command = new GenerateUnboundLinkCablingCommandBundle(unboundLink1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		assertEquals(command.getResult(), Command.RESULT_NO);
		
		Collection nodeLinks = METS.map.getNodeLinks();
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 6);
		assertEquals(physicalLinks.size(), 6);
		assertEquals(siteNodes.size(), 7);
		assertEquals(cablePaths.size(), 1);

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);

		assertTrue(physicalLinks.contains(unboundLink1));
		assertTrue(physicalLinks.contains(unboundLink2));
	}

	public void testUnboundBetweenUnboundAndSite2() throws Exception {
		CableChannelingItem cci1 = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1);

		Point unbound1location = new Point(120, 120);
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, unbound1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		Point unbound2location = new Point(120, 220);
		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, unbound2location);
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
		
		CablePath cablePath = METS.mapView.getCablePaths().iterator().next();
		UnboundLink unboundLink = (UnboundLink)cablePath.getBinding().get(cci1);
		physicalLinks.remove(unboundLink);

		Iterator linkIterator = physicalLinks.iterator();
		PhysicalLink newLink1 = (PhysicalLink )linkIterator.next();
		PhysicalLink newLink2 = (PhysicalLink )linkIterator.next();

		assertTrue(newLink1 instanceof UnboundLink);
		assertTrue(newLink2 instanceof UnboundLink);
		
		UnboundLink unboundLink1 = (UnboundLink)newLink1;
		UnboundLink unboundLink2 = (UnboundLink)newLink2;

		GenerateUnboundLinkCablingCommandBundle command = new GenerateUnboundLinkCablingCommandBundle(unboundLink1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		assertEquals(command.getResult(), Command.RESULT_NO);
		
		Collection nodeLinks = METS.map.getNodeLinks();
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 7);
		assertEquals(physicalLinks.size(), 7);
		assertEquals(siteNodes.size(), 7);
		assertEquals(cablePaths.size(), 1);

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);

		assertTrue(physicalLinks.contains(unboundLink1));
		assertTrue(physicalLinks.contains(unboundLink2));
		assertTrue(physicalLinks.contains(unboundLink));
	}

	public void testUnboundBetweenBoundAndSite() throws Exception {
		CableChannelingItem cci = METS.generateCCI(
				SchemeSampleData.scheme1clink0, 
				this.building1, 
				this.well1);

		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		Point unbound2location = new Point(120, 220);
		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, unbound2location);
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
		
		Iterator linkIterator = physicalLinks.iterator();
		PhysicalLink newLink1 = (PhysicalLink )linkIterator.next();
		PhysicalLink newLink2 = (PhysicalLink )linkIterator.next();

		assertTrue(newLink1 instanceof UnboundLink);
		assertTrue(newLink2 instanceof UnboundLink);
		
		UnboundLink unboundLink1;
		UnboundLink unboundLink2;
		
		
		if(newLink1.getStartNode().equals(this.building1)) {
			unboundLink1 = (UnboundLink)newLink1;
			unboundLink2 = (UnboundLink)newLink2;
		}
		else {
			unboundLink1 = (UnboundLink)newLink2;
			unboundLink2 = (UnboundLink)newLink1;
		}

		GenerateUnboundLinkCablingCommandBundle command = new GenerateUnboundLinkCablingCommandBundle(unboundLink1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		assertEquals(command.getResult(), Command.RESULT_OK);
		
		Collection nodeLinks = METS.map.getNodeLinks();
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 6);
		assertEquals(physicalLinks.size(), 6);
		assertEquals(siteNodes.size(), 6);
		assertEquals(cablePaths.size(), 1);

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		physicalLinks.remove(unboundLink2);
		
		PhysicalLink greneratedLink = (PhysicalLink)physicalLinks.iterator().next();

		assertFalse(greneratedLink instanceof UnboundLink);
		assertSame(greneratedLink.getStartNode(), this.building1);
		assertSame(greneratedLink.getEndNode(), this.well1);

		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 2);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		cci = cablePath.getFirstCCI(unboundLink1);
		CableChannelingItem cci1 = cablePath.getFirstCCI(greneratedLink);
		CableChannelingItem cci2 = cablePath.getFirstCCI(unboundLink2);

		assertNull(cci);
		assertTrue(cciList.contains(cci1));
		assertTrue(cciList.contains(cci2));

		SiteNode unbound = (SiteNode )METS.logicalNetLayer.getVisibleMapElementAtPoint(unbound2location, METS.netMapViewer.getVisibleBounds());
		
		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), unbound);

		assertEquals(cablePath.getLinks().size(), 2);
		assertTrue(cablePath.getLinks().contains(greneratedLink));
		assertTrue(cablePath.getLinks().contains(unboundLink2));
		assertSame(cablePath.getStartLastBoundLink(), cci1);
		assertSame(cablePath.getStartUnboundNode(), this.well1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), unbound);
	}

	public void testUnboundBetweenTwoBound() throws ApplicationException {
		long t1 = System.currentTimeMillis();
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		long t2 = System.currentTimeMillis();

		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		
		assertTrue(link instanceof UnboundLink);
		
		UnboundLink unboundLink = (UnboundLink)link;

		long t3 = System.currentTimeMillis();

		GenerateUnboundLinkCablingCommandBundle command = new GenerateUnboundLinkCablingCommandBundle(unboundLink);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		long t4 = System.currentTimeMillis();

		assertEquals(command.getResult(), Command.RESULT_OK);

		nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
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
		
		NodeLink generatedNodeLink = (NodeLink )nodeLinks.iterator().next();
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);

		PhysicalLink generatedLink = (PhysicalLink )physicalLinks.iterator().next();
		assertFalse(generatedLink instanceof UnboundLink);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		assertSame(generatedLink.getStartNode(), this.building1); 
		assertSame(generatedLink.getEndNode(), this.building2);
		assertSame(generatedNodeLink.getStartNode(), this.building1); 
		assertSame(generatedNodeLink.getEndNode(), this.building2);
		assertEquals(generatedLink.getNodeLinks().size(), 1);
		assertTrue(generatedLink.getNodeLinks().contains(generatedNodeLink));
		assertSame(generatedNodeLink.getPhysicalLink(), generatedLink);
		
		long t5 = System.currentTimeMillis();

		Set cciSet = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciSet.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciSet.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), this.building2);
		assertSame(cci.getPhysicalLink(), generatedLink);

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(generatedLink), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(generatedLink));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci);
		assertSame(cablePath.getStartUnboundNode(), this.building2);
		assertSame(cablePath.getEndLastBoundLink(), cci);
		assertSame(cablePath.getEndUnboundNode(), this.building1);
		long t6 = System.currentTimeMillis();
		System.out.println("place commands " + (t2 - t1) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("place assertions " + (t3 - t2) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("generate command " + (t4 - t3) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("map assertions " + (t5 - t4) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println("cci assertions " + (t6 - t5) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void testUnboundBetweenTwoSites() throws Exception {
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

		PlaceSchemeCableLinkCommand cablecommand = new PlaceSchemeCableLinkCommand(SchemeSampleData.scheme1clink0);
		cablecommand.setNetMapViewer(METS.netMapViewer);
		cablecommand.execute();

		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);

		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);
		
		PhysicalLink link = (PhysicalLink )physicalLinks.iterator().next();
		
		assertTrue(link instanceof UnboundLink);
		
		UnboundLink unboundLink = (UnboundLink)link;

		GenerateUnboundLinkCablingCommandBundle command = new GenerateUnboundLinkCablingCommandBundle(unboundLink);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		assertEquals(command.getResult(), Command.RESULT_OK);

		nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
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
		
		NodeLink generatedNodeLink = (NodeLink )nodeLinks.iterator().next();
		
		physicalLinks.remove(this.link1);
		physicalLinks.remove(this.link2);
		physicalLinks.remove(this.link3);
		physicalLinks.remove(this.link4);

		PhysicalLink generatedLink = (PhysicalLink )physicalLinks.iterator().next();
		assertFalse(generatedLink instanceof UnboundLink);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		assertSame(generatedLink.getStartNode(), this.well1); 
		assertSame(generatedLink.getEndNode(), this.well3);
		assertSame(generatedNodeLink.getStartNode(), this.well1); 
		assertSame(generatedNodeLink.getEndNode(), this.well3);
		assertEquals(generatedLink.getNodeLinks().size(), 1);
		assertTrue(generatedLink.getNodeLinks().contains(generatedNodeLink));
		assertSame(generatedNodeLink.getPhysicalLink(), generatedLink);
		
		List cciList = new ArrayList(SchemeSampleData.scheme1clink0.getPathMembers());
		assertEquals(cciList.size(), 3);
		cciList.remove(cci1);
		cciList.remove(cci3);
		CableChannelingItem cci2 = (CableChannelingItem )cciList.iterator().next();

		assertSame(cci2.getStartSiteNode(), this.well1);
		assertSame(cci2.getEndSiteNode(), this.well3);
		assertSame(cci2.getPhysicalLink(), generatedLink);

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getFirstCCI(this.link1), cci1);
		assertSame(cablePath.getFirstCCI(generatedLink), cci2);
		assertSame(cablePath.getFirstCCI(this.link4), cci3);
		assertEquals(cablePath.getLinks().size(), 3);
		assertTrue(cablePath.getLinks().contains(generatedLink));
		assertTrue(cablePath.getLinks().contains(this.link1));
		assertTrue(cablePath.getLinks().contains(this.link4));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath.getStartLastBoundLink(), cci3);
		assertSame(cablePath.getStartUnboundNode(), this.building2);
		assertSame(cablePath.getEndLastBoundLink(), cci1);
		assertSame(cablePath.getEndUnboundNode(), this.building1);
	}

}


