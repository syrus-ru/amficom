/**
 * $Id: BindPhysicalNodeToSiteCommandBundleTestCase.java,v 1.7 2006/06/16 10:19:56 bass Exp $
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

import com.syrus.AMFICOM.client.map.command.action.BindPhysicalNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

public class BindPhysicalNodeToSiteCommandBundleTestCase extends SchemeBindingTestCase {

	public void testExecute() throws ApplicationException {
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

		//test itself
		BindPhysicalNodeToSiteCommandBundle command = new BindPhysicalNodeToSiteCommandBundle(node, this.well1);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		//assertions
		nodeLinks = new ArrayList(METS.map.getNodeLinks());
		physicalLinks = new ArrayList(METS.map.getPhysicalLinks());
		topologicalNodes = METS.map.getTopologicalNodes();
		Collection siteNodes = METS.map.getSiteNodes();
		Collection cablePaths = METS.mapView.getCablePaths();
		
		assertEquals(nodeLinks.size(), 6);
		assertEquals(physicalLinks.size(), 6);
		assertEquals(topologicalNodes.size(), 0);
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
		assertNull(cci1.getPhysicalLink());

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

}
