/**
 * $Id: DeleteNodeLinkCommandBundle.java,v 1.14 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;

/**
 * В данном классе реализуется алгоритм удаления NodeLink. В зависимости
 * от того, какие конечные точки на концах происходит операция удаления 
 * фрагментов линий, линий, узлов  (и путей). Команда
 * состоит из последовательности атомарных действий
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class DeleteNodeLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый фрагмент
	 */
	NodeLink nodeLink;
	
	/**
	 * Карта
	 */
	Map map;

	public DeleteNodeLinkCommandBundle(NodeLink nodeLink)
	{
		super();
		this.nodeLink = nodeLink;
	}

	/**
	 * удалить фрагмент между двумя сетевыми узлами. поскольку в этом
	 * случае фагмент составляет физическую линию, удалить ее тоже
	 */
	protected void removeNodeLinkBetweenTwoSites(
			NodeLink nodeLink, 
			PhysicalLink physicalLink)
	{
		super.removeNodeLink(nodeLink);
		super.removePhysicalLink(physicalLink);
	}

	/**
	 * удалить фрагмент между двумя неактивными топологиескими узлами. 
	 * поскольоку в этом случае фагмент составляет физическую линию, удалить 
	 * ее, и топологические узлы тоже
	 */
	protected void removeNodeLinkBetweenInactiveNodes(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			TopologicalNode leftNode,
			TopologicalNode rightNode)
	{
		super.removeNode(leftNode);
		super.removeNode(rightNode);
		super.removeNodeLink(nodeLink);
		super.removePhysicalLink(physicalLink);
	}

	/**
	 * удалить фрагмент между двумя неактивными топологиескими узлами.
	 * в этом случае физическая линия делится на две физических линии 
	 */
	protected void removeNodeLinkBetweenActiveNodes(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			TopologicalNode leftNode,
			TopologicalNode rightNode)
	{
		// удалить фрагмент линии
		super.removeNodeLink(nodeLink);

		// при этом его концевые топологические узлы становятся неактивными
		super.changePhysicalNodeActivity(leftNode, false);
		super.changePhysicalNodeActivity(rightNode, false);

		MapElementState pls = physicalLink.getState();

		// убрать фрагмент из физической линии
		physicalLink.removeNodeLink(nodeLink);

		// если разрывается кольцевая линия, содержащая только топологические
		// узлы, то только обновить концевые узлы
		if(physicalLink.getStartNode() == physicalLink.getEndNode()
			&& physicalLink.getStartNode() instanceof TopologicalNode)
		{
			physicalLink.setStartNode(leftNode);
			physicalLink.setEndNode(rightNode);
		}
		else
		// в противном случае разделить линию на две
		{
			// создать вторую физическую линию
			PhysicalLink newPhysicalLink = super.createPhysicalLink(
					physicalLink.getStartNode(),
					null);

			// переносим фрагменты в новую линию пока не наткнемся на
			// один из концевых узлов удаленного фрагмента
			AbstractNode foundNode = super.moveNodeLinks(
					physicalLink,
					newPhysicalLink,
					false,
					leftNode,
					rightNode);

			// если наткнулись на разрыв линии связи, то обновить
			// концевые узлы и закончить
			if(foundNode.equals(leftNode))
			{
				physicalLink.setStartNode(rightNode);
			}
			else
			if(foundNode.equals(rightNode))
			{
				physicalLink.setStartNode(leftNode);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}
	}

	/**
	 * удалить концевой фрагмент линии
	 */
	protected void removeNodeLinkBetweenActiveAndInactive(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			TopologicalNode activeNode,
			TopologicalNode inactiveNode)
	{
		super.removeNodeLink(nodeLink);
		super.removeNode(inactiveNode);
		super.changePhysicalNodeActivity(activeNode, false);

		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink(nodeLink);

		// обновить концевый узлы
		if(physicalLink.getStartNode().equals(inactiveNode))
		{
			physicalLink.setStartNode(activeNode);
		}
		else
		{
			physicalLink.setEndNode(activeNode);
		}

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}
	
	/**
	 * удалить фрагмент между сетевым узлом и топологическим активным узлом
	 */
	protected void removeNodeLinkBetweenSiteAndActiveNode(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			SiteNode site,
			TopologicalNode node)
	{
		super.changePhysicalNodeActivity(node, false);

		super.removeNodeLink(nodeLink);
		
		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink( nodeLink);

		// обновить концевый узлы
		if (physicalLink.getStartNode().equals(site))
		{
			physicalLink.setStartNode(node);
		}
		else
		{
			physicalLink.setEndNode(node);
		}

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}

	/**
	 * удалить фрагмент между сетевым узлом и топологическим неактивным узлом.
	 * поскольоку в этом случае фагмент составляет физическую линию, удалить 
	 * ее и топологический узел тоже
	 */
	protected void removeNodeLinkBetweenSiteAndInactiveNode(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			SiteNode site,
			TopologicalNode node)
	{
		super.removeNode(node);
		super.removeNodeLink(nodeLink);
		super.removePhysicalLink(physicalLink);
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		// фрагмент может быть удален в результате атомарной команды в составе
		// другой команды удаления, в этом случае у него будет выставлен
		// флаг isRemoved
		if(this.nodeLink.isRemoved())
			return;

		this.map = this.logicalNetLayer.getMapView().getMap();

		PhysicalLink physicalLink = 
				this.nodeLink.getPhysicalLink();

		if (this.nodeLink.getStartNode() instanceof SiteNode
			&& this.nodeLink.getEndNode() instanceof SiteNode)
        {
			this.removeNodeLinkBetweenTwoSites(this.nodeLink, physicalLink);
        }//MapSiteNodeElement && MapSiteNodeElement
		else
		if(this.nodeLink.getStartNode() instanceof TopologicalNode
			&& this.nodeLink.getEndNode() instanceof TopologicalNode)
		{
			TopologicalNode leftNode = 
					(TopologicalNode)this.nodeLink.getStartNode();
			TopologicalNode rightNode = 
					(TopologicalNode)this.nodeLink.getEndNode();
			
			if(leftNode.isActive() && rightNode.isActive())
			{
				this.removeNodeLinkBetweenActiveNodes(
						this.nodeLink, 
						physicalLink, 
						leftNode, 
						rightNode);
			}// isActive() && isActive()
			else
			if( !leftNode.isActive() && !rightNode.isActive())
			{
				this.removeNodeLinkBetweenInactiveNodes(
						this.nodeLink, 
						physicalLink, 
						leftNode, 
						rightNode);
			}// ! isActive() && ! isActive()
			else
			{
				TopologicalNode activeNode = 
					(leftNode.isActive() ) 
						? leftNode 
						: rightNode;
				TopologicalNode inactiveNode = 
					(leftNode.isActive() ) 
						? rightNode 
						: leftNode;

				this.removeNodeLinkBetweenActiveAndInactive(
						this.nodeLink, 
						physicalLink, 
						activeNode, 
						inactiveNode);
			}// isActive() && ! isActive()
			
		}//MapPhysicalNodeElement && MapPhysicalNodeElement
		else
		// defaults to MapSiteNodeElement && MapPhysicalNodeElement
		{
			SiteNode site = null;
			TopologicalNode node = null;
			
			if(this.nodeLink.getStartNode() instanceof SiteNode
				&& this.nodeLink.getEndNode() instanceof TopologicalNode)
			{
				site = (SiteNode)this.nodeLink.getStartNode();
				node = (TopologicalNode)this.nodeLink.getEndNode();
	
			}
			else
			if(this.nodeLink.getEndNode() instanceof SiteNode
				&& this.nodeLink.getStartNode() instanceof TopologicalNode)
			{
				site = (SiteNode)this.nodeLink.getEndNode();
				node = (TopologicalNode)this.nodeLink.getStartNode();
			}

			if (node.isActive())
			{
				this.removeNodeLinkBetweenSiteAndActiveNode(
						this.nodeLink, 
						physicalLink, 
						site, 
						node);
			}//if (node.isActive())
			else
			{
				this.removeNodeLinkBetweenSiteAndInactiveNode(
						this.nodeLink, 
						physicalLink, 
						site, 
						node);
			}//if ! (node.isActive())
		}//MapSiteNodeElement && MapPhysicalNodeElement
			
		MapView mapView = this.logicalNetLayer.getMapView();

		if(physicalLink.getStartNode() instanceof SiteNode
			&& physicalLink.getEndNode() instanceof SiteNode)
		{
			for(Iterator it = mapView.getCablePaths(physicalLink).iterator(); it.hasNext();)
			{
				CablePath cpath = (CablePath)it.next();
				cpath.removeLink(physicalLink);
				UnboundLink unbound = 
					super.createUnboundLinkWithNodeLink(
						physicalLink.getStartNode(),
						physicalLink.getEndNode());
				unbound.setCablePath(cpath);
				cpath.addLink(unbound, CableController.generateCCI(unbound));
			}
		}

		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
