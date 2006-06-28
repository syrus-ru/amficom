/*-
 * $Id: MoveSchemeElementCommand.java,v 1.1 2006/06/13 06:44:46 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

public class MoveSchemeElementCommand extends MapActionCommandBundle {
	private SiteNode site; 
	private SiteNode destination;
	private SchemeElement schemeElement;
	
	public MoveSchemeElementCommand(final SchemeElement schemeElement, 
			final SiteNode source, final SiteNode destination) {
		this.site = source;
		this.destination = destination;
		this.schemeElement = schemeElement;
	}
	
	@Override
	public void execute() {
		MapView mapView = this.netMapViewer.getLogicalNetLayer().getMapView();
		List<CablePath> cableElementsDropped = new LinkedList<CablePath>();
		for(CablePath cablePath : mapView.getCablePaths(this.site)) {
			if(cablePath.getStartNode().equals(this.site) || cablePath.getEndNode().equals(this.site)) {
				cableElementsDropped.add(cablePath);
			}
		}

		Collection<SchemeCableLink> linkedSchemeCableLinks = new LinkedList<SchemeCableLink>();
		for(CablePath cablePath : cableElementsDropped) {
			final SchemeCableLink schemeCableLink = cablePath.getSchemeCableLink();
			linkedSchemeCableLinks.add(schemeCableLink);
			
			UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(cablePath);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();
		}
		
		this.schemeElement.setSiteNode(this.destination);
		
		for (SchemeCableLink link : linkedSchemeCableLinks) {
			try {
				final SortedSet<CableChannelingItem> pathMembers = link.getPathMembers();
				if (!pathMembers.isEmpty()) {
					pathMembers.first().setParentPathOwner(null, true);
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			
			final PlaceSchemeCableLinkCommand command = new PlaceSchemeCableLinkCommand(link);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();
		}
		this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}
}
