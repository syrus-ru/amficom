/**
 * $Id: DropSchemeCableLinkCommand.java,v 1.4 2004/10/01 16:35:50 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/01 16:35:50 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class DropSchemeCableLinkCommand extends MapActionCommandBundle
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
	String prev_site_id;
	
	Map map;
	MapView mapView;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public DropSchemeCableLinkCommand(SchemeCableLink scl)
	{
		super();
		this.scl = scl;
	}
/*
	private void sort()
	{
		CableChannelingItem[] ccis = new CableChannelingItem[scl.channeling_items.size()];
		for(Iterator it = scl.channeling_items.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			ccis[cci.n] = cci;
		}
		List list = new ArrayList();
		for(int i = 0; i < ccis.length; i++)
			list.add(ccis[i]);
			
		scl.channeling_items = list;
	}
*/
	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
	
		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		MapSiteNodeElement[] mne = mapView.getSideNodes(scl);
		
		startNode = mne[0];
		endNode = mne[1];
		
		cablePath = mapView.findCablePath(scl);
		if(cablePath == null)
			cablePath = createCablePath(scl, startNode, endNode);

		scl.channelingItems = sortCCIS(startNode, endNode, scl.channelingItems);
		List ccis = (List )scl.channelingItems;

		MapSiteNodeElement bufferStartSite = startNode;
		MapSiteNodeElement bufferEndSite;

		for(Iterator it = ccis.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			MapSiteNodeElement smsne = map.getMapSiteNodeElement(cci.startSiteId);
			MapSiteNodeElement emsne = map.getMapSiteNodeElement(cci.endSiteId);
			MapPhysicalLinkElement link = map.getPhysicalLink(cci.physicalLinkId);
			if(smsne == null
				|| emsne == null
				|| link == null)
			{
				break;
			}
			
			if(smsne == bufferStartSite)
				bufferEndSite = emsne;
			else
				bufferEndSite = smsne;

			cablePath.addLink(link);

			bufferStartSite = bufferEndSite;
		}

		if(endNode != bufferStartSite)
		{
			unbound = createUnboundLink(bufferStartSite, endNode);
			unbound.setCablePath(cablePath);
			
			nodeLink = createNodeLink(bufferStartSite, endNode);
	
			cablePath.addLink(unbound);
			
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
