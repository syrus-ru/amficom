/**
 * $Id: DropSchemeCableLinkCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
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
		
		cablePath = createCablePath(scl, startNode, endNode);

		unbound = createUnboundLink(startNode, endNode);
		
		nodeLink = createNodeLink(startNode, endNode);

		cablePath.addLink(unbound);
		
		unbound.addNodeLink(nodeLink);
		nodeLink.setPhysicalLinkId(unbound.getId());
				
//		sort();
//
//		for(Iterator it = scl.channeling_items.iterator(); it.hasNext();)
//		{
//			CableChannelingItem cci = (CableChannelingItem )it.next();
//			MapSiteNodeElement smsne = map.getMapSiteNodeElement(cci.start_site_id);
//			MapSiteNodeElement emsne = map.getMapSiteNodeElement(cci.end_site_id);
//			MapPhysicalLinkElement link = getMap().getPhysicalLink(cci.physical_link_id);
//			if(link.)
//		}

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				cablePath, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(cablePath);
		logicalNetLayer.notifySchemeEvent(cablePath);
		logicalNetLayer.notifyCatalogueEvent(cablePath);
	}
	
}
