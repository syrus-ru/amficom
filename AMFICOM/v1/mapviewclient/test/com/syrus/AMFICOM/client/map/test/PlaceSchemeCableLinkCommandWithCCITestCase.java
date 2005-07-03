/**
 * $Id: PlaceSchemeCableLinkCommandWithCCITestCase.java,v 1.1 2005/07/03 13:56:51 krupenn Exp $
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
import java.util.Set;

import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeSampleData;

public class PlaceSchemeCableLinkCommandWithCCITestCase extends SchemeBindingTestCase {

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
	public void test1() {

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
		
		assertSame(unboundLink.getStartNode(), this.building1); 
		assertSame(unboundLink.getEndNode(), this.building2);
		assertSame(unboundNodeLink.getStartNode(), this.building1); 
		assertSame(unboundNodeLink.getEndNode(), this.building2);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(unboundNodeLink));
		assertSame(unboundNodeLink.getPhysicalLink(), unboundLink);
		
		Set cciSet = SchemeSampleData.scheme1clink0.getCableChannelingItems();
		assertEquals(cciSet.size(), 1);
		CableChannelingItem cci = (CableChannelingItem )cciSet.iterator().next();

		assertSame(cci.getStartSiteNode(), this.building1);
		assertSame(cci.getEndSiteNode(), this.building2);
		assertSame(cci.getPhysicalLink(), unboundLink);

		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.building2);
		assertSame(cablePath.getBinding().getCCI(unboundLink), cci);
		assertEquals(cablePath.getLinks().size(), 1);
		assertTrue(cablePath.getLinks().contains(unboundLink));
		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertNull(cablePath.getStartLastBoundLink());
		assertSame(cablePath.getStartUnboundNode(), this.building1);
		assertNull(cablePath.getEndLastBoundLink());
		assertSame(cablePath.getEndUnboundNode(), this.building2);
	}

}
