/**
 * $Id: CreateNodeLinkCommandBundle.java,v 1.3 2004/11/01 15:40:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * создание фрагмента линии, которое строится из 
 * последовательности атомарных действий. соответствует рисованию фрагмента
 * пользователем: пользователь нажимает на узле левой кнопкой, драгает
 * курсор в нужное место и отпускает. В момент отпускания выполняется 
 * данная команда
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/11/01 15:40:10 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateNodeLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * константа используется для передачи в команду параметра (конечной
	 * точки для фрагмента)
	 */
	public static final String END_POINT = "endpoint";

	/** Начальный узел фрагмента */
	MapNodeElement startNode;
	
	Map map;
	
	/** Точка, в которой должен быть конечный узел фрагмента */
	Point endPoint;

	/**
	 * 
	 * @param startNode
	 */
	public CreateNodeLinkCommandBundle(MapNodeElement startNode)
	{
		super();
		this.startNode = startNode;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals(END_POINT))
		{
			endPoint = (Point )value;
		}
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		// анализируется элемент в точке, в которой отпущена мышка		
		MapElement curElementAtPoint = logicalNetLayer.getMapElementAtPoint(endPoint);

		// если мышка отпущена на том же элементе, то линию не рисовать
		if(curElementAtPoint == startNode)
			return;
	
		Map map = logicalNetLayer.getMapView().getMap();
	
		MapNodeElement endNode = null;
		MapPhysicalLinkElement physicalLink = null;
		MapNodeLinkElement nodeLink = null;

		Point2D.Double mapEndPoint = logicalNetLayer.convertScreenToMap(endPoint);

		// если в конечной точке уже есть элемент, проверяем, какой это узел
		if ( curElementAtPoint != null
			&& curElementAtPoint instanceof MapNodeElement)
		{
			endNode = (MapNodeElement )curElementAtPoint;

			// конечный элемент - топологический узел
			if(endNode instanceof MapPhysicalNodeElement)
			{
				MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )endNode;
		
				// если он активный, то есть находится в середине другой линии,
				// то в той же точке создается новый
				if(mpne.isActive())
				{
					endNode = super.createPhysicalNode(mapEndPoint);
				}
				else
				// если он - концевой для линии, то замкнуть новый фрагмент
				// на него (дорисовка существующей линии или объелинение
				// физических линий)
				{
					super.changePhysicalNodeActivity(mpne, true);
				}
			}
		}
		else
		// Если в конечной точке нет элемента, то создаем новый конечный 
		// топологический узел
		{
			endNode = super.createPhysicalNode(mapEndPoint);
		}

		nodeLink = super.createNodeLink(startNode, endNode);

		//Далее в зависимости от того какая комбинация startNode и endNode
		if (startNode instanceof MapSiteNodeElement 
			&& endNode instanceof MapSiteNodeElement )
		{
			// создается новая физическая линия из одного фрагмента
			
			physicalLink = super.createPhysicalLink(startNode, endNode);
			physicalLink.addNodeLink( nodeLink);
			nodeLink.setPhysicalLinkId(physicalLink.getId());
		}//if ( MapSiteNodeElement && MapSiteNodeElement)
		else
		if ( startNode instanceof MapSiteNodeElement
			&& endNode instanceof MapPhysicalNodeElement )
		{
			MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )endNode;

			// Если у конечного узла два входящих фрагмента (включая только 
			// что созданный), то есть фрагмент пририсован к существующей линии,
			// то замкнуть эту линию
			if(endNode.getNodeLinks().size() == 2)
			{
				physicalLink = map.getPhysicalLink(mpne.getPhysicalLinkId());

				MapElementState pls = physicalLink.getState();

				physicalLink.addNodeLink(nodeLink);
				nodeLink.setPhysicalLinkId(physicalLink.getId());

				// Коррекция начального и конечного узлов линии
				if(physicalLink.getEndNode() == startNode)
					physicalLink.setEndNode(physicalLink.getStartNode());
				physicalLink.setStartNode(startNode);

				super.registerStateChange(physicalLink, pls, physicalLink.getState());
			}//if(endNode.getNodeLinks().size() == 2
			//  в противном случае создается новая линия из одного фрагмента
			else
			{
				physicalLink = super.createPhysicalLink(startNode, endNode);
				physicalLink.addNodeLink( nodeLink);
				nodeLink.setPhysicalLinkId(physicalLink.getId());
			}
		}//if ( MapSiteNodeElement && MapPhysicalNodeElement )
		else
		if ( startNode instanceof MapPhysicalNodeElement
			&& endNode instanceof MapSiteNodeElement )
		{
			// существующая физическая линия завершается на узле site

			MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )startNode;

			physicalLink = map.getPhysicalLink(mpne.getPhysicalLinkId());

			MapElementState pls = physicalLink.getState();

			physicalLink.addNodeLink(nodeLink);
			nodeLink.setPhysicalLinkId(physicalLink.getId());

			super.changePhysicalNodeActivity(mpne, true);

			// Коррекция начального и конечного узлов линии
			if(physicalLink.getStartNode() == startNode)
				physicalLink.setStartNode(physicalLink.getEndNode());
			physicalLink.setEndNode(endNode);

			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}// if ( MapPhysicalNodeElement && MapSiteNodeElement )
		else
		// Когда соединяются две линии, в одну из линий переносятся фрагменты 
		// другой, которая удаляется
		if ( startNode instanceof MapPhysicalNodeElement 
			&& endNode instanceof MapPhysicalNodeElement )
		{
			MapPhysicalNodeElement smpne = (MapPhysicalNodeElement )startNode;
			MapPhysicalNodeElement empne = (MapPhysicalNodeElement )endNode;

			physicalLink = map.getPhysicalLink(smpne.getPhysicalLinkId());
			MapPhysicalLinkElement emple = map.getPhysicalLink(empne.getPhysicalLinkId());

			MapElementState pls = physicalLink.getState();

			physicalLink.addNodeLink(nodeLink);
			nodeLink.setPhysicalLinkId(physicalLink.getId());

			super.changePhysicalNodeActivity(smpne, true);

			// Коррекция начального и конечного узлов линии
			if(physicalLink.getStartNode() == startNode)
				physicalLink.setStartNode(physicalLink.getEndNode());
			physicalLink.setEndNode(endNode);

			// перекидываем фрагменты из одной линии в другую и удаляем
			// ненужную линию
			if(emple != null)
				if(emple != physicalLink)
			{
				MapElementState pls2 = emple.getState();

				// отдельный список, поскольку используется операция удаления
				LinkedList nodeLinksToMove = new LinkedList();
				nodeLinksToMove.addAll(emple.getNodeLinks());

				// Перенос фрагментов линии из одной линии в другую
				for(Iterator it = nodeLinksToMove.iterator(); it.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
					emple.removeNodeLink(mnle);
					physicalLink.addNodeLink(mnle);
					mnle.setPhysicalLinkId(physicalLink.getId());
				}			
			
				// Коррекция начального и конечного узлов линии
				if(emple.getStartNode() == endNode)
					physicalLink.setEndNode(emple.getEndNode());
				else
					physicalLink.setEndNode(emple.getStartNode());

				super.registerStateChange(emple, pls2, emple.getState());

				super.removePhysicalLink(emple);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				physicalLink, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.notifySchemeEvent(physicalLink);
	}
}
