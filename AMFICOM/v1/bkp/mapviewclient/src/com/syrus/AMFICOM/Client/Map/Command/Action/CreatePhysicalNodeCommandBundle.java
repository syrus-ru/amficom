/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.7 2004/12/23 16:57:59 krupenn Exp $
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * В данном классе реализуется алгоритм добавления топологического узла на 
 * фрагмент линии. При этом фрагмент линии удаляется, и вместо него создаются 
 * два других фрагмента, разделенные новывм топологичсеским узлом. Команда
 * состоит из последовательности атомарных действий
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/23 16:57:59 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreatePhysicalNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	NodeLink nodeLink;
	
	Map map;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public CreatePhysicalNodeCommandBundle(
			NodeLink nodeLink,
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
		
		DoublePoint coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		map = logicalNetLayer.getMapView().getMap();

		// получить линию связи, которой принадлежит фрагмент
		PhysicalLink physicalLink = nodeLink.getPhysicalLink();
	
		// создать новый активный топологический узел
		TopologicalNode node = createPhysicalNode(coordinatePoint);
		changePhysicalNodeActivity(node, true);

		// взять начальный и конечный узлы фрагмента
		AbstractNode startNode = nodeLink.getStartNode();
		AbstractNode endNode = nodeLink.getEndNode();

		// разбить фрагмент на две части - т.е. создать два новых фрагмента
		NodeLink link1 = createNodeLink(physicalLink, startNode, node);
		link1.setPhysicalLink(physicalLink);
		NodeLink link2 = createNodeLink(physicalLink, node, endNode);
		link2.setPhysicalLink(physicalLink);

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

	}
}
