/**
 * $Id: CreateNodeLinkCommandBundle.java,v 1.9 2005/01/11 16:43:05 krupenn Exp $
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;

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
 * @version $Revision: 1.9 $, $Date: 2005/01/11 16:43:05 $
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
	AbstractNode startNode;
	
	Map map;
	
	/** Точка, в которой должен быть конечный узел фрагмента */
	Point endPoint;

	/**
	 * 
	 * @param startNode
	 */
	public CreateNodeLinkCommandBundle(AbstractNode startNode)
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

	void createSiteToSite(AbstractNode endNode)
	{
		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		// создается новая физическая линия из одного фрагмента
		
		physicalLink = super.createPhysicalLink(startNode, endNode);

		nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
		nodeLink.setPhysicalLink(physicalLink);

		physicalLink.addNodeLink(nodeLink);
	}

	void createSiteToNewNode(DoublePoint mapEndPoint)
	{
		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		physicalLink = super.createPhysicalLink(startNode, startNode);

		AbstractNode endNode = super.createPhysicalNode(physicalLink, mapEndPoint);
		
		physicalLink.setEndNode(endNode);

		nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
		nodeLink.setPhysicalLink(physicalLink);

		physicalLink.addNodeLink(nodeLink);
	}

	void createSiteToNode(TopologicalNode endNode)
	{
		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		physicalLink = endNode.getPhysicalLink();

		MapElementState pls = physicalLink.getState();

		nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
		nodeLink.setPhysicalLink(physicalLink);

		physicalLink.addNodeLink(nodeLink);

		super.changePhysicalNodeActivity(endNode, true);

		// Коррекция начального и конечного узлов линии
		if(physicalLink.getEndNode().equals(endNode))
			physicalLink.setEndNode(physicalLink.getStartNode());
		physicalLink.setStartNode(startNode);

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}

	void createNodeToSite(SiteNode endNode)
	{
		// существующая физическая линия завершается на узле site

		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		TopologicalNode mpne = (TopologicalNode )startNode;

		physicalLink = mpne.getPhysicalLink();

		MapElementState pls = physicalLink.getState();

		nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
		nodeLink.setPhysicalLink(physicalLink);

		physicalLink.addNodeLink(nodeLink);

		super.changePhysicalNodeActivity(mpne, true);

		// Коррекция начального и конечного узлов линии
		if(physicalLink.getStartNode().equals(startNode))
			physicalLink.setStartNode(physicalLink.getEndNode());
		physicalLink.setEndNode(endNode);

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}

	void createNodeToNewNode(DoublePoint mapEndPoint)
	{
		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		TopologicalNode smpne = (TopologicalNode )startNode;

		physicalLink = smpne.getPhysicalLink();

		MapElementState pls = physicalLink.getState();

		TopologicalNode endNode = (TopologicalNode )super.createPhysicalNode(physicalLink, mapEndPoint);;

		nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
		nodeLink.setPhysicalLink(physicalLink);

		physicalLink.addNodeLink(nodeLink);

		super.changePhysicalNodeActivity(smpne, true);

		// Коррекция начального и конечного узлов линии
		if(physicalLink.getStartNode().equals(startNode))
			physicalLink.setStartNode(physicalLink.getEndNode());
		physicalLink.setEndNode(endNode);

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}

	void createNodeToNode(TopologicalNode endNode)
	{
		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		TopologicalNode smpne = (TopologicalNode )startNode;
		TopologicalNode empne = (TopologicalNode )endNode;

		physicalLink = smpne.getPhysicalLink();
		PhysicalLink emple = empne.getPhysicalLink();

		MapElementState pls = physicalLink.getState();

		nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
		nodeLink.setPhysicalLink(physicalLink);

		physicalLink.addNodeLink(nodeLink);

		super.changePhysicalNodeActivity(smpne, true);
		super.changePhysicalNodeActivity(empne, true);

		// Коррекция начального и конечного узлов линии
		if(physicalLink.getStartNode().equals(startNode))
			physicalLink.setStartNode(physicalLink.getEndNode());
		physicalLink.setEndNode(endNode);

		MapElementState pls2 = emple.getState();

		// отдельный список, поскольку используется операция удаления
		LinkedList nodeLinksToMove = new LinkedList();
		nodeLinksToMove.addAll(emple.getNodeLinks());

		// Перенос фрагментов линии из одной линии в другую
		for(Iterator it = nodeLinksToMove.iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();
			emple.removeNodeLink(mnle);
			physicalLink.addNodeLink(mnle);
			mnle.setPhysicalLink(physicalLink);
		}			
	
		// Коррекция начального и конечного узлов линии
		if(emple.getStartNode().equals(endNode))
			physicalLink.setEndNode(emple.getEndNode());
		else
			physicalLink.setEndNode(emple.getStartNode());

		super.registerStateChange(emple, pls2, emple.getState());

		super.removePhysicalLink(emple);

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
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
		if(curElementAtPoint.equals(startNode))
			return;
	
		this.map = logicalNetLayer.getMapView().getMap();
	
		AbstractNode endNode = null;
		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		DoublePoint mapEndPoint = logicalNetLayer.convertScreenToMap(endPoint);

		// если в конечной точке уже есть элемент, проверяем, какой это узел
		if ( curElementAtPoint != null
			&& curElementAtPoint instanceof AbstractNode)
		{
			endNode = (AbstractNode )curElementAtPoint;

			// конечный элемент - топологический узел
			if(endNode instanceof TopologicalNode)
			{
				TopologicalNode mpne = (TopologicalNode )endNode;
		
				// если он активный, то есть находится в середине другой линии,
				// то в той же точке создается новый
				if(mpne.isActive())
				{
					// node created with fake physicalLink
					// should be later updated (e.g. through call to
					// nodelink.setPhysicalLink or node.setPhysicalLink)
					endNode = null;
//					endNode = super.createPhysicalNode(mpne.getPhysicalLink(), mapEndPoint);
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
			endNode = null;
//			endNode = super.createPhysicalNode(mapEndPoint);
		}

		if(endNode == null)
		{
			if (startNode instanceof SiteNode)
			{
				createSiteToNewNode(mapEndPoint);
			}
			else
			if (startNode instanceof TopologicalNode)
			{
				createNodeToNewNode(mapEndPoint);
			}
		}
		else
		{
			if (startNode instanceof SiteNode 
				&& endNode instanceof SiteNode )
			{
				createSiteToSite(endNode);
			}
			else
			if ( startNode instanceof SiteNode
				&& endNode instanceof TopologicalNode )
			{
				createSiteToNode((TopologicalNode )endNode);
			}
			else
			if ( startNode instanceof TopologicalNode
				&& endNode instanceof SiteNode )
			{
				createNodeToSite((SiteNode )endNode);
			}
			else
			if ( startNode instanceof TopologicalNode 
				&& endNode instanceof TopologicalNode )
			{
				createNodeToNode((TopologicalNode )endNode);
			}
		}
/*
		//Далее в зависимости от того какая комбинация startNode и endNode
		if (startNode instanceof SiteNode 
			&& endNode instanceof SiteNode )
		{
			// создается новая физическая линия из одного фрагмента
			createSiteToSite(endNode);	
//			physicalLink = super.createPhysicalLink(startNode, endNode);
//
//			nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
//			nodeLink.setPhysicalLink(physicalLink);
//
//			physicalLink.addNodeLink( nodeLink);
		}//if ( MapSiteNodeElement && MapSiteNodeElement)
		else
		if ( startNode instanceof SiteNode
			&& endNode instanceof TopologicalNode )
		{
			TopologicalNode mpne = (TopologicalNode)endNode;

			// Если у конечного узла два входящих фрагмента (включая только 
			// что созданный), то есть фрагмент пририсован к существующей линии,
			// то замкнуть эту линию
			if(endNode.getNodeLinks().size() == 2)
			{
				createSiteToNode(mpne);
//				physicalLink = mpne.getPhysicalLink();
//
//				MapElementState pls = physicalLink.getState();
//
//				nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
//				nodeLink.setPhysicalLink(physicalLink);
//
//				physicalLink.addNodeLink(nodeLink);
//
//				// Коррекция начального и конечного узлов линии
//				if(physicalLink.getEndNode().equals(mpne))
//					physicalLink.setEndNode(physicalLink.getStartNode());
//				physicalLink.setStartNode(startNode);
//
//				super.registerStateChange(physicalLink, pls, physicalLink.getState());
			}//if(endNode.getNodeLinks().size() == 2
			//  в противном случае создается новая линия из одного фрагмента
			else
			{
				createSiteToNewNode(mapEndPoint);

//				physicalLink = super.createPhysicalLink(startNode, endNode);
//
//				nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
//				nodeLink.setPhysicalLink(physicalLink);
//
//				physicalLink.addNodeLink( nodeLink);
			}
		}//if ( MapSiteNodeElement && MapPhysicalNodeElement )
		else
		if ( startNode instanceof TopologicalNode
			&& endNode instanceof SiteNode )
		{
			// существующая физическая линия завершается на узле site
			createNodeToSite((SiteNode )endNode);

//			TopologicalNode mpne = (TopologicalNode)startNode;
//
//			physicalLink = mpne.getPhysicalLink();
//
//			MapElementState pls = physicalLink.getState();
//
//			nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
//			nodeLink.setPhysicalLink(physicalLink);
//
//			physicalLink.addNodeLink(nodeLink);
//
//			super.changePhysicalNodeActivity(mpne, true);
//
//			// Коррекция начального и конечного узлов линии
//			if(physicalLink.getStartNode().equals(startNode))
//				physicalLink.setStartNode(physicalLink.getEndNode());
//			physicalLink.setEndNode(endNode);
//
//			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}// if ( MapPhysicalNodeElement && MapSiteNodeElement )
		else
		// Когда соединяются две линии, в одну из линий переносятся фрагменты 
		// другой, которая удаляется
		if ( startNode instanceof TopologicalNode 
			&& endNode instanceof TopologicalNode )
		{
			createNodeToNewNode(mapEndPoint);
//			TopologicalNode smpne = (TopologicalNode)startNode;
			TopologicalNode empne = (TopologicalNode)endNode;
//
//			physicalLink = smpne.getPhysicalLink();
			PhysicalLink emple = empne.getPhysicalLink();
//
//			MapElementState pls = physicalLink.getState();
//
//			nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
//			nodeLink.setPhysicalLink(physicalLink);
//
//			physicalLink.addNodeLink(nodeLink);
//
//			super.changePhysicalNodeActivity(smpne, true);
//
//			// Коррекция начального и конечного узлов линии
//			if(physicalLink.getStartNode().equals(startNode))
//				physicalLink.setStartNode(physicalLink.getEndNode());
//			physicalLink.setEndNode(endNode);

			// перекидываем фрагменты из одной линии в другую и удаляем
			// ненужную линию
			if(emple != null)
				if(!emple.equals(physicalLink))
			{
				createNodeToNode((TopologicalNode )endNode);
//				MapElementState pls2 = emple.getState();
//
//				// отдельный список, поскольку используется операция удаления
//				LinkedList nodeLinksToMove = new LinkedList();
//				nodeLinksToMove.addAll(emple.getNodeLinks());
//
//				// Перенос фрагментов линии из одной линии в другую
//				for(Iterator it = nodeLinksToMove.iterator(); it.hasNext();)
//				{
//					NodeLink mnle = (NodeLink)it.next();
//					emple.removeNodeLink(mnle);
//					physicalLink.addNodeLink(mnle);
//					mnle.setPhysicalLink(physicalLink);
//				}			
//			
//				// Коррекция начального и конечного узлов линии
//				if(emple.getStartNode().equals(endNode))
//					physicalLink.setEndNode(emple.getEndNode());
//				else
//					physicalLink.setEndNode(emple.getStartNode());
//
//				super.registerStateChange(emple, pls2, emple.getState());
//
//				super.removePhysicalLink(emple);
			}

//			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}
*/
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
//		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
//				physicalLink, 
//				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.notifySchemeEvent(physicalLink);
	}
}
