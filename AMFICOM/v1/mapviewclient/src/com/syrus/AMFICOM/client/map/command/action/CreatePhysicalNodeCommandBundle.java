/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * В данном классе реализуется алгоритм добавления топологического узла на 
 * фрагмент линии. При этом фрагмент линии удаляется, и вместо него создаются 
 * два других фрагмента, разделенные новывм топологичсеским узлом. Команда
 * состоит из последовательности атомарных действий
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreatePhysicalNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	MapNodeLinkElement nodeLink;
	
	Map map;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public CreatePhysicalNodeCommandBundle(
			MapNodeLinkElement nodeLink,
			Point point)
	{
		super();
		this.nodeLink = nodeLink;
		this.point = point;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		Point2D.Double coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		map = logicalNetLayer.getMapView().getMap();

		// получить линию связи, которой принадлежит фрагмент
		MapPhysicalLinkElement physicalLink = 
			map.getPhysicalLink( nodeLink.getPhysicalLinkId());
	
		// создать новый активный топологический узел
		MapPhysicalNodeElement node = createPhysicalNode(coordinatePoint);
		changePhysicalNodeActivity(node, true);

		// взять начальный и конечный узлы фрагмента
		MapNodeElement startNode = nodeLink.getStartNode();
		MapNodeElement endNode = nodeLink.getEndNode();

		// разбить фрагмент на две части - т.е. создать два новых фрагмента
		MapNodeLinkElement link1 = createNodeLink(startNode, node);
		link1.setPhysicalLinkId(physicalLink.getId());
		MapNodeLinkElement link2 = createNodeLink(node, endNode);
		link2.setPhysicalLinkId(physicalLink.getId());

		// удаляется старый фрагмент с карты
		removeNodeLink(nodeLink);	
		
		MapElementState pls = physicalLink.getState();

		// удаляется старый фрагмент из линии
		physicalLink.removeNodeLink(nodeLink);
		// добавляются два новых фрагмента
		physicalLink.addNodeLink(link1);
		physicalLink.addNodeLink(link2);
		
		registerStateChange(physicalLink, pls, physicalLink.getState());

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					node, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(node);
		logicalNetLayer.notifySchemeEvent(node);
		logicalNetLayer.notifyCatalogueEvent(node);

	}
}
