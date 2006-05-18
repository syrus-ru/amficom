/**
 * $Id: CreateUnboundLinkCommandBundleTestCase.java,v 1.1 2005/07/08 06:39:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.UnboundLink;

public class CreateUnboundLinkCommandBundleTestCase extends SchemeBindingTestCase {

	public void testExecute() {
		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(this.building1, this.building2);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();
		
		Collection nodeLinks = new ArrayList(METS.map.getNodeLinks());
		Collection physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();

		assertEquals(nodeLinks.size(), 5);
		assertEquals(physicalLinks.size(), 5);
		assertEquals(siteNodes.size(), 5);
		assertEquals(cablePaths.size(), 0);

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

		assertSame(unboundLink.getStartNode(), this.building1); 
		assertSame(unboundLink.getEndNode(), this.building2);
		assertSame(unboundNodeLink.getStartNode(), this.building1); 
		assertSame(unboundNodeLink.getEndNode(), this.building2);
		assertEquals(unboundLink.getNodeLinks().size(), 1);
		assertTrue(unboundLink.getNodeLinks().contains(unboundNodeLink));
		assertSame(unboundNodeLink.getPhysicalLink(), unboundLink);
	}

}
