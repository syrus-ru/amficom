/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.1 2004/10/09 13:33:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.awt.Point;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.1 $, $Date: 2004/10/09 13:33:40 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	MapSiteNodeElement startNode = null;
	MapSiteNodeElement endNode = null;

	MapCablePathElement cablePath = null;
	MapUnboundLinkElement unbound = null;
	MapNodeLinkElement nodeLink;

	SchemeCableLink scl = null;
	String prevSiteId;
	
	Map map;
	MapView mapView;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public PlaceSchemeCableLinkCommand(SchemeCableLink scl)
	{
		super();
		this.scl = scl;
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		MapSiteNodeElement[] mne = mapView.getSideNodes(scl);
		
		startNode = mne[0];
		endNode = mne[1];
		
		cablePath = mapView.findCablePath(scl);
		if(cablePath == null)
			cablePath = super.createCablePath(scl, startNode, endNode);
		else
			super.removeCablePathLinks(cablePath);

		scl.channelingItems = sortCCIS(startNode, endNode, scl.channelingItems);
		List ccis = (List )scl.channelingItems;

		MapSiteNodeElement bufferStartSite = startNode;

		for(Iterator it = ccis.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			MapSiteNodeElement smsne = map.getMapSiteNodeElement(cci.startSiteId);
			MapSiteNodeElement emsne = map.getMapSiteNodeElement(cci.endSiteId);
			if(smsne == null
				|| emsne == null)
			{
				continue;
			}

			MapPhysicalLinkElement link = map.getPhysicalLink(cci.physicalLinkId);
			if(link == null)
			{
				unbound = super.createUnboundLink(bufferStartSite, smsne);
				cablePath.addLink(unbound);
				unbound.setCablePath(cablePath);

				nodeLink = super.createNodeLink(bufferStartSite, endNode);
				unbound.addNodeLink(nodeLink);
				nodeLink.setPhysicalLinkId(unbound.getId());
			}

			link.getBinding().add(scl);
			if(cci.row_x != -1
				&& cci.place_y != -1)
				link.getBinding().bind(scl, cci.row_x, cci.place_y);

			cablePath.addLink(link);

			bufferStartSite = emsne;
		}

		if(endNode != bufferStartSite)
		{
			unbound = super.createUnboundLink(bufferStartSite, endNode);
			cablePath.addLink(unbound);
			unbound.setCablePath(cablePath);

			nodeLink = super.createNodeLink(bufferStartSite, endNode);
			unbound.addNodeLink(nodeLink);
			nodeLink.setPhysicalLinkId(unbound.getId());
		}

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				cablePath, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(cablePath);
		logicalNetLayer.notifySchemeEvent(cablePath);
		logicalNetLayer.notifyCatalogueEvent(cablePath);
	}
	
	protected List sortCCIS(MapSiteNodeElement start, MapSiteNodeElement end, Collection ccis)
	{
		MapSiteNodeElement bufferSite = start;
		
		List retCCIs = new LinkedList();
		
		int count = ccis.size();

		for (int i = 0; i < count; i++) 
		{
			for(Iterator it = ccis.iterator(); it.hasNext();)
			{
				CableChannelingItem cci = (CableChannelingItem )it.next();
				if(cci.startSiteId.equals(bufferSite.getId()))
				{
					retCCIs.add(cci);
					bufferSite = map.getMapSiteNodeElement(cci.endSiteId);
					break;
				}
			}
		}
		return retCCIs;
	}
}
