/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.3 2004/10/11 16:48:33 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/11 16:48:33 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	MapPhysicalNodeElement node;
	MapSiteNodeElement site;

	MapNodeElement node1 = null;
	MapNodeElement node2 = null;

	/**
	 * Карта, на которой производится операция
	 */
	MapView mapView;
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(MapPhysicalNodeElement node, MapSiteNodeElement site)
	{
		this.node = node;
		this.site = site;
	}
	
	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		DataSourceInterface dataSource = aContext.getDataSource();

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();

		MapPhysicalLinkElement link = map.getPhysicalLink(node.getPhysicalLinkId());

		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();

			if(node1 == null)
				node1 = mnle.getOtherNode(node);
			else
				node2 = mnle.getOtherNode(node);

			if(mnle.getStartNode() == node)
				mnle.setStartNode(site);
			else
				mnle.setEndNode(site);
		}

		removeNode(node);

		if(link.getStartNode() == node)
			link.setStartNode(site);
		else
		if(link.getEndNode() == node)
			link.setEndNode(site);

		MapUnboundLinkElement link1 = super.createUnboundLink(link.getStartNode(), site);
		link1.setProto(link.getProto());

		// single cpath, as long as link is UnboundLink
		MapCablePathElement cpath = (MapCablePathElement )(mapView.getCablePaths(link).getFirst());
		
		cpath.addLink(link1);
		link1.setCablePath(cpath);

		// получить все фрагменты первой филической линии
		java.util.List nodelinks = link.getNodeLinks();
	
		// определить начальный узел и начальный фрагмент физической линии
		MapNodeLinkElement startNodeLink = null;
		MapNodeElement startNode = link.getStartNode();
		for(Iterator it = nodelinks.iterator(); it.hasNext();)
		{
			startNodeLink = (MapNodeLinkElement )it.next();
			if(startNodeLink.getStartNode() == link.getStartNode()
				|| startNodeLink.getEndNode() == link.getStartNode())
			{
				break;
			}
		}
	
		// неявный цикл по фракментам линии - перекидывать фрагменты в новую 
		// физическую линию. движемся по фрагментам от первого пока не наткнемся
		// на фрагмент, соседний с удаленным
		for(;;)
		{
			// перекинуть фрагмент в новую линию
			link.removeNodeLink(startNodeLink);
			link1.addNodeLink(startNodeLink);
			
			startNodeLink.setPhysicalLinkId(link1.getId());

			// перейти к следующему узлу
			startNode = startNodeLink.getOtherNode(startNode);

			// если наткнулись на разрыв линии связи, то обновить
			// концевые узлы и закончить
			if(startNode == site)
			{
				link1.setEndNode(site);
				link.setStartNode(site);
				break;
			}
			
			// перейти к следующему фрагменту
			for(Iterator it = startNode.getNodeLinks().iterator(); it.hasNext();)
			{
				MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
				if(startNodeLink != mnle)
				{
					startNodeLink = mnle;
					break;
				}
			}
		}//for(;;)

		logicalNetLayer.repaint();
	}
}

