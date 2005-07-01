/**
 * $Id: PlaceSchemeElementCommandTestCase.java,v 1.1 2005/07/01 16:07:55 krupenn Exp $
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
		assertTrue(siteNodes.contains(this.site1));
		assertTrue(siteNodes.contains(this.site2));
		assertTrue(siteNodes.contains(this.site3));
		assertTrue(siteNodes.contains(this.site4));
		assertTrue(siteNodes.contains(this.site5));

		siteNodes.remove(this.site1);
		siteNodes.remove(this.site2);
		siteNodes.remove(this.site3);
		siteNodes.remove(this.site4);
		siteNodes.remove(this.site5);

		SiteNode site = (SiteNode )siteNodes.iterator().next();
		
		assertTrue(site instanceof UnboundNode);
		UnboundNode unbound = (UnboundNode )site;
		assertSame(unbound.getSchemeElement(), SchemeSampleData.scheme1element1);
		assertNull(SchemeSampleData.scheme1element1.getSiteNode());
	}

	public void testPlaceToSite() {
		Point location = new Point(20, 20);
		PlaceSchemeElementCommand command = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, location);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();
		
		Collection siteNodes = METS.map.getSiteNodes();
		assertEquals(siteNodes.size(), 5);
		assertTrue(siteNodes.contains(this.site1));
		assertTrue(siteNodes.contains(this.site2));
		assertTrue(siteNodes.contains(this.site3));
		assertTrue(siteNodes.contains(this.site4));
		assertTrue(siteNodes.contains(this.site5));

		assertSame(this.site1, SchemeSampleData.scheme1element1.getSiteNode());
	}
}
