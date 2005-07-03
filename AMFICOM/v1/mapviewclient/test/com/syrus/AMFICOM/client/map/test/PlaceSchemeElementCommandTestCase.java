/**
 * $Id: PlaceSchemeElementCommandTestCase.java,v 1.2 2005/07/03 13:56:51 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeSampleData;

public class PlaceSchemeElementCommandTestCase extends SchemeBindingTestCase {

	public void testPlaceSeparate() {
		Point location = new Point(120, 120);
		PlaceSchemeElementCommand command = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, location);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();
		
		Collection siteNodes = new ArrayList(METS.map.getSiteNodes());
		assertEquals(siteNodes.size(), 6);
		assertTrue(siteNodes.contains(this.building1));
		assertTrue(siteNodes.contains(this.well1));
		assertTrue(siteNodes.contains(this.well2));
		assertTrue(siteNodes.contains(this.well3));
		assertTrue(siteNodes.contains(this.building2));

		siteNodes.remove(this.building1);
		siteNodes.remove(this.well1);
		siteNodes.remove(this.well2);
		siteNodes.remove(this.well3);
		siteNodes.remove(this.building2);

		SiteNode site = (SiteNode )siteNodes.iterator().next();
		
		assertTrue(site instanceof UnboundNode);
		UnboundNode unbound = (UnboundNode )site;
		assertSame(unbound.getSchemeElement(), SchemeSampleData.scheme1element1);
		assertNull(SchemeSampleData.scheme1element1.getSiteNode());
	}

	public void testPlaceToSite() {
		Point location = this.building1location;
		PlaceSchemeElementCommand command = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, location);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();
		
		Collection siteNodes = METS.map.getSiteNodes();
		assertEquals(siteNodes.size(), 5);
		assertTrue(siteNodes.contains(this.building1));
		assertTrue(siteNodes.contains(this.well1));
		assertTrue(siteNodes.contains(this.well2));
		assertTrue(siteNodes.contains(this.well3));
		assertTrue(siteNodes.contains(this.building2));

		assertSame(this.building1, SchemeSampleData.scheme1element1.getSiteNode());
	}
}
