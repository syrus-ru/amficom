/**
 * $Id: DeleteNodeLinkCommandBundle.java,v 1.4 2004/10/14 15:39:05 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;

/**
 * В данном классе реализуется алгоритм удаления NodeLink. В зависимости
 * от того, какие конечные точки на концах происходит операция удаления 
 * фрагментов линий, линий, узлов  (и путей). Команда
 * состоит из последовательности атомарных действий
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/14 15:39:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeleteNodeLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый фрагмент
	 */
	MapNodeLinkElement nodeLink;
	
	/**
	 * Карта
	 */
	Map map;

	public DeleteNodeLinkCommandBundle(MapNodeLinkElement nodeLink)
	{
		super();
		this.nodeLink = nodeLink;
	}

	/**
	 * удалить фрагмент между двумя сетевыми узлами. поскольку в этом
	 * случае фагмент составляет физическую линию, удалить ее тоже
	 */
	protected void removeNodeLinkBetweenTwoSites(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink)
	{
		removeNodeLink(nodeLink);
		removePhysicalLink(physicalLink);
	}

	/**
	 * удалить фрагмент между двумя неактивными топологиескими узлами. 
	 * поскольоку в этом случае фагмент составляет физическую линию, удалить 
	 * ее, и топологические узлы тоже
	 */
	protected void removeNodeLinkBetweenInactiveNodes(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapPhysicalNodeElement leftNode,
			MapPhysicalNodeElement rightNode)
	{
		removeNode(leftNode);
		removeNode(rightNode);
		removeNodeLink(nodeLink);
		removePhysicalLink(physicalLink);
	}

	/**
	 * удалить фрагмент между двумя неактивными топологиескими узлами.
	 * в этом случае физическая линия делится на две физических линии 
	 */
	protected void removeNodeLinkBetweenActiveNodes(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapPhysicalNodeElement leftNode,
			MapPhysicalNodeElement rightNode)
	{
		// удалить фрагмент линии
		removeNodeLink(nodeLink);

		// при этом его концевые топологические узлы становятся неактивными
		leftNode.setActive(false);
		rightNode.setActive(false);

		MapElementState pls = physicalLink.getState();

		// убрать фрагмент из физической линии
		physicalLink.removeNodeLink(nodeLink);

		// если разрывается кольцевая линия, содержащая только топологические
		// узлы, то только обновить концевые узлы
		if(physicalLink.getStartNode() == physicalLink.getEndNode()
			&& physicalLink.getStartNode() instanceof MapPhysicalNodeElement)
		{
			physicalLink.setStartNode(leftNode);
			physicalLink.setEndNode(rightNode);
		}
		else
		// в противном случае разделить линию на две
		{
			// создать вторую физическую линию
			MapPhysicalLinkElement newPhysicalLink = createPhysicalLink(
					physicalLink.getStartNode(),
					null);
		
			// получить все фрагменты первой филической линии
			java.util.List nodelinks = physicalLink.getNodeLinks();
		
			// определить начальный узел и начальный фрагмент физической линии
			MapNodeLinkElement startNodeLink = null;
			MapNodeElement startNode = physicalLink.getStartNode();
			for(Iterator it = nodelinks.iterator(); it.hasNext();)
			{
				startNodeLink = (MapNodeLinkElement )it.next();
				if(startNodeLink.getStartNode() == physicalLink.getStartNode()
					|| startNodeLink.getEndNode() == physicalLink.getStartNode())
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
				physicalLink.removeNodeLink(startNodeLink);
				newPhysicalLink.addNodeLink(startNodeLink);
				
				MapElementState nls = startNodeLink.getState();
				
				startNodeLink.setPhysicalLinkId(newPhysicalLink.getId());
				
				registerStateChange(startNodeLink, nls, startNodeLink.getState());
				
				// перейти к следующему узлу
				startNode = startNodeLink.getOtherNode(startNode);
				
				// если наткнулись на разрыв линии связи, то обновить
				// концевые узлы и закончить
				if(startNode == leftNode)
				{
					newPhysicalLink.setEndNode(leftNode);
					physicalLink.setStartNode(rightNode);
					break;
				}
				else
				if(startNode == rightNode)
				{
					newPhysicalLink.setEndNode(rightNode);
					physicalLink.setStartNode(leftNode);
					break;
				}
	
				// перейти к следующему фрагменту
				for(Iterator it = startNode.getNodeLinks().iterator(); it.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
					if(startNodeLink != mnle)
						startNodeLink = mnle;
				}
			}//for(;;)
			registerStateChange(physicalLink, pls, physicalLink.getState());
		}
	}

	/**
	 * удалить концевой фрагмент линии
	 */
	protected void removeNodeLinkBetweenActiveAndInactive(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapPhysicalNodeElement activeNode,
			MapPhysicalNodeElement inactiveNode)
	{
		removeNodeLink(nodeLink);
		removeNode(inactiveNode);
		changePhysicalNodeActivity(activeNode, false);

		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink(nodeLink);

		// обновить концевый узлы
		if(physicalLink.getStartNode() == inactiveNode)
		{
			physicalLink.setStartNode(activeNode);
		}
		else
		{
			physicalLink.setEndNode(activeNode);
		}

		registerStateChange(physicalLink, pls, physicalLink.getState());

		// проверка избыточна?, поскольку топологический узел был активен
		// а значит в линии есть второй фрагмент
		if(physicalLink.getNodeLinks().size() == 0)
		{
			removeNode(activeNode);
			removePhysicalLink( physicalLink);
		}
	}
	
	/**
	 * удалить фрагмент между сетевым узлом и топологическим активным узлом
	 */
	protected void removeNodeLinkBetweenSiteAndActiveNode(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapSiteNodeElement site,
			MapPhysicalNodeElement node)
	{
		changePhysicalNodeActivity(node, false);

		removeNodeLink(nodeLink);
		
		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink( nodeLink);

		// обновить концевый узлы
		if (physicalLink.getStartNode() == site)
		{
			physicalLink.setStartNode(node);
		}
		else
		{
			physicalLink.setEndNode(node);
		}

		registerStateChange(physicalLink, pls, physicalLink.getState());

		// проверка избыточна?, поскольку топологический узел был активен
		// а значит в линии есть второй фрагмент
		if(physicalLink.getNodeLinks().size() == 0)
		{
			removeNode(node);
			removePhysicalLink( physicalLink);
		}
	}

	/**
	 * удалить фрагмент между сетевым узлом и топологическим неактивным узлом.
	 * поскольоку в этом случае фагмент составляет физическую линию, удалить 
	 * ее и топологический узел тоже
	 */
	protected void removeNodeLinkBetweenSiteAndInactiveNode(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapSiteNodeElement site,
			MapPhysicalNodeElement node)
	{
		removeNode(node);
		removeNodeLink(nodeLink);
		removePhysicalLink(physicalLink);
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");
		
		// фрагмент может быть удален в результате атомарной команды в составе
		// другой команды удаления, в этом случае у него будет выставлен
		// флаг isRemoved
		if(nodeLink.isRemoved())
			return;

		map = logicalNetLayer.getMapView().getMap();

		MapPhysicalLinkElement physicalLink = map.getPhysicalLink(nodeLink.getPhysicalLinkId());

		if (nodeLink.getStartNode() instanceof MapSiteNodeElement
			&& nodeLink.getEndNode() instanceof MapSiteNodeElement)
        {
			this.removeNodeLinkBetweenTwoSites(nodeLink, physicalLink);
        }//MapSiteNodeElement && MapSiteNodeElement
		else
		if(nodeLink.getStartNode() instanceof MapPhysicalNodeElement
			&& nodeLink.getEndNode() instanceof MapPhysicalNodeElement)
		{
			MapPhysicalNodeElement leftNode = (MapPhysicalNodeElement )nodeLink.getStartNode();
			MapPhysicalNodeElement rightNode = (MapPhysicalNodeElement )nodeLink.getEndNode();
			
			if(leftNode.isActive() && rightNode.isActive())
			{
				this.removeNodeLinkBetweenActiveNodes(nodeLink, physicalLink, leftNode, rightNode);
			}// isActive() && isActive()
			else
			if( !leftNode.isActive() && !rightNode.isActive())
			{
				this.removeNodeLinkBetweenInactiveNodes(nodeLink, physicalLink, leftNode, rightNode);
			}// ! isActive() && ! isActive()
			else
			{
				MapPhysicalNodeElement activeNode = (leftNode.isActive() ) ? leftNode : rightNode;
				MapPhysicalNodeElement inactiveNode = (leftNode.isActive() ) ? rightNode : leftNode;

				this.removeNodeLinkBetweenActiveAndInactive(nodeLink, physicalLink, activeNode, inactiveNode);
			}// isActive() && ! isActive()
			
		}//MapPhysicalNodeElement && MapPhysicalNodeElement
		else
		// defaults to MapSiteNodeElement && MapPhysicalNodeElement
		{
			MapSiteNodeElement site = null;
			MapPhysicalNodeElement node = null;
			
			if(nodeLink.getStartNode() instanceof MapSiteNodeElement
				&& nodeLink.getEndNode() instanceof MapPhysicalNodeElement)
			{
				site = (MapSiteNodeElement )nodeLink.getStartNode();
				node = (MapPhysicalNodeElement )nodeLink.getEndNode();
	
			}
			else
			if(nodeLink.getEndNode() instanceof MapSiteNodeElement
				&& nodeLink.getStartNode() instanceof MapPhysicalNodeElement)
			{
				site = (MapSiteNodeElement )nodeLink.getEndNode();
				node = (MapPhysicalNodeElement )nodeLink.getStartNode();
			}

			if (node.isActive())
			{
				this.removeNodeLinkBetweenSiteAndActiveNode(nodeLink, physicalLink, site, node);
			}//if (node.isActive())
			else
			{
				this.removeNodeLinkBetweenSiteAndInactiveNode(nodeLink, physicalLink, site, node);
			}//if ! (node.isActive())
		}//MapSiteNodeElement && MapPhysicalNodeElement
			
		MapView mapView = logicalNetLayer.getMapView();

		if(physicalLink.getStartNode() instanceof MapSiteNodeElement
			&& physicalLink.getEndNode() instanceof MapSiteNodeElement)
		{
			for(Iterator it = mapView.getCablePaths(physicalLink).iterator(); it.hasNext();)
			{
				MapCablePathElement cpath = (MapCablePathElement )it.next();
				cpath.removeLink(physicalLink);
				MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(
					physicalLink.getStartNode(),
					physicalLink.getEndNode());
				unbound.setCablePath(cpath);
				cpath.addLink(unbound);
			}
		}
//		logicalNetLayer.getMapView().removePath( mapPhysicalLinkElement.getId() );

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
