/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.4 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLinkBinding;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class GenerateCablePathCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	MapCablePathElement path;
	MapNodeProtoElement proto;

	/**
	 * Карта, на которой производится операция
	 */
	MapView mapView;
	Map map;

	public GenerateCablePathCablingCommandBundle(MapCablePathElement path, MapNodeProtoElement proto)
	{
		this.path = path;
		this.proto = proto;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		DataSourceInterface dataSource = getLogicalNetLayer().getContext().getDataSource();

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		MapSiteNodeElement site = (MapSiteNodeElement )path.getStartNode();
		site = checkSite(site);

		List list  = new LinkedList();
		list.addAll(path.getLinks());

		for(Iterator it = list.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

			if(site == link.getEndNode())
				site = (MapSiteNodeElement )link.getStartNode();
			else
				site = (MapSiteNodeElement )link.getEndNode();

			site = checkSite(site);

			if(link instanceof MapUnboundLinkElement)
			{
				path.removeLink(link);
				MapUnboundLinkElement un = (MapUnboundLinkElement )link;
				link = createPhysicalLink(un.getStartNode(), un.getEndNode());
				removePhysicalLink(un);
				for(Iterator it2 = un.getNodeLinks().iterator(); it2.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it2.next();
					mnle.setPhysicalLinkId(link.getId());
					link.addNodeLink(mnle);
				}
				path.addLink(link);
				link.getBinding().add(path);
			}
		}
	}

	private MapSiteNodeElement checkSite(MapSiteNodeElement site)
	{
		MapSiteNodeElement site2 = site;
		if(site instanceof MapUnboundNodeElement)
		{
			site2 = createSite(site.getAnchor(), proto);
			super.removeNode(site);
			((MapUnboundNodeElement )site).getSchemeElement().siteId = site2.getId();
			
			for(Iterator it = path.getLinks().iterator(); it.hasNext();)
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
				if(link.getStartNode() == site)
					link.setStartNode(site2);
				if(link.getEndNode() == site)
					link.setEndNode(site2);
					
				for(Iterator it2 = link.getNodeLinksAt(site).iterator(); it2.hasNext();)
				{
					MapNodeLinkElement nl = (MapNodeLinkElement )it2.next();
					if(nl.getStartNode() == site)
						nl.setStartNode(site2);
					if(nl.getEndNode() == site)
						nl.setEndNode(site2);
				}
			}
			for(Iterator it = mapView.getCablePaths(site).iterator(); it.hasNext();)
			{
				MapCablePathElement cpath = (MapCablePathElement )it.next();
				if(cpath.getStartNode().equals(site))
					cpath.setStartNode(site2);
				else
				if(cpath.getEndNode().equals(site))
					cpath.setEndNode(site2);
			}
		}
		return site2;
	}

}

