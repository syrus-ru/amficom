/**
 * $Id: BindUnboundNodeToSiteCommandBundleTestCase.java,v 1.5 2006/06/16 10:19:56 bass Exp $
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
import java.util.LinkedList;
import java.util.Set;

import com.syrus.AMFICOM.client.map.command.action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

public class BindUnboundNodeToSiteCommandBundleTestCase extends SchemeBindingTestCase {

	public void test1() {
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, new Point(120, 120));
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		Collection siteNodes = new LinkedList(METS.map.getSiteNodes());
		assertEquals(siteNodes.size(), 6);
		siteNodes.remove(this.building1);
		siteNodes.remove(this.well1);
		siteNodes.remove(this.well2);
		siteNodes.remove(this.well3);
		siteNodes.remove(this.building2);
		
		UnboundNode unbound = (UnboundNode )siteNodes.iterator().next(); 
		
		BindUnboundNodeToSiteCommandBundle command = new BindUnboundNodeToSiteCommandBundle(unbound, this.building1); 
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 4);
		assertEquals(physicalLinks.size(), 4);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 0);
	}

	public void test2() throws Exception {
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

		Collection nodeLinks = METS.map.getNodeLinks();
		Collection physicalLinks = METS.map.getPhysicalLinks();
		Collection siteNodes = new ArrayList(METS.map.getSiteNodes());
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 7);
		assertEquals(cablePaths.size(), 1);

		siteNodes.remove(this.building1);
		siteNodes.remove(this.well1);
		siteNodes.remove(this.well2);
		siteNodes.remove(this.well3);
		siteNodes.remove(this.building2);

		Iterator iterator = siteNodes.iterator();
		SiteNode site1 = (SiteNode )iterator.next();
		SiteNode site2 = (SiteNode )iterator.next();
		
		assertTrue(site1 instanceof UnboundNode);
		assertTrue(site2 instanceof UnboundNode);
		UnboundNode unboundSite1;
		UnboundNode unboundSite2;

		Point loc = METS.logicalNetLayer.getConverter().convertMapToScreen(site1.getLocation());
		if(loc.equals(unbound1location)) {
			unboundSite1 = (UnboundNode )site1;
			unboundSite2 = (UnboundNode )site2;
		} else {
			unboundSite1 = (UnboundNode )site2;
			unboundSite2 = (UnboundNode )site1;
		}
		
		BindUnboundNodeToSiteCommandBundle command = new BindUnboundNodeToSiteCommandBundle(unboundSite1, this.building1); 
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		siteNodes = new ArrayList(METS.map.getSiteNodes());
		cablePaths = METS.mapView.getCablePaths();

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
		UnboundNode unboundSite = (UnboundNode )site;

		assertSame(unboundSite, unboundSite2);
		
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
		
		assertSame(unboundLink.getStartNode(), this.building1); 
		assertSame(unboundLink.getEndNode(), unboundSite);
		assertSame(unboundNodeLink.getStartNode(), this.building1); 
		assertSame(unboundNodeLink.getEndNode(), unboundSite);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(unboundNodeLink));
		assertSame(unboundNodeLink.getPhysicalLink(), unboundLink);
		
		Set cciSet = SchemeSampleData.scheme1clink0.getPathMembers();
		assertEquals(cciSet.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciSet.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), unboundSite);
		assertNull(cci.getPhysicalLink());

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), unboundSite);
		assertSame(cablePath.getFirstCCI(unboundLink), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertNull(cablePath.getStartLastBoundLink());
		assertSame(cablePath.getStartUnboundNode(), this.building1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), unboundSite);
	}
}
