/*-
 * $$Id: DeleteNodeLinkCommandBundle.java,v 1.42 2005/10/26 11:07:01 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 * В данном классе реализуется алгоритм удаления NodeLink. В зависимости
 * от того, какие конечные точки на концах происходит операция удаления 
 * фрагментов линий, линий, узлов  (и путей). Команда
 * состоит из последовательности атомарных действий
 * 
 * @version $Revision: 1.42 $, $Date: 2005/10/26 11:07:01 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class DeleteNodeLinkCommandBundle extends MapActionCommandBundle {
	/**
	 * Удаляемый фрагмент
	 */
	NodeLink nodeLink;
	
	/**
	 * Карта
	 */
	Map map;

	public DeleteNodeLinkCommandBundle(NodeLink nodeLink) {
		super();
		this.nodeLink = nodeLink;
	}

	/**
	 * удалить фрагмент между двумя сетевыми узлами. поскольку в этом
	 * случае фагмент составляет физическую линию, удалить ее тоже
	 */
	protected void removeNodeLinkBetweenTwoSites(
			NodeLink nodeLink1, 
			PhysicalLink physicalLink)
			throws Throwable {
		super.removeNodeLink(nodeLink1);
		super.removePhysicalLink(physicalLink);
	}

	/**
	 * удалить фрагмент между двумя неактивными топологиескими узлами. 
	 * поскольоку в этом случае фагмент составляет физическую линию, удалить 
	 * ее, и топологические узлы тоже
	 */
	protected void removeNodeLinkBetweenInactiveNodes(
			NodeLink nodeLink1, 
			PhysicalLink physicalLink,
			TopologicalNode leftNode,
			TopologicalNode rightNode)
			throws Throwable {
		super.removeNode(leftNode);
		super.removeNode(rightNode);
		super.removeNodeLink(nodeLink1);
		super.removePhysicalLink(physicalLink);
	}

	/**
	 * удалить фрагмент между двумя неактивными топологиескими узлами.
	 * в этом случае физическая линия делится на две физических линии 
	 */
	protected void removeNodeLinkBetweenActiveNodes(
			NodeLink nodeLink1, 
			PhysicalLink physicalLink,
			TopologicalNode leftNode,
			TopologicalNode rightNode)
			throws Throwable {
		// удалить фрагмент линии
		super.removeNodeLink(nodeLink1);

		// при этом его концевые топологические узлы становятся неактивными
		super.changePhysicalNodeActivity(leftNode, false);
		super.changePhysicalNodeActivity(rightNode, false);

		MapElementState pls = physicalLink.getState();

		// убрать фрагмент из физической линии
		physicalLink.removeNodeLink(nodeLink1);

		// если разрывается кольцевая линия, содержащая только топологические
		// узлы, то только обновить концевые узлы
		final AbstractNode startNode = physicalLink.getStartNode();
		if (startNode == physicalLink.getEndNode()
			&& startNode instanceof TopologicalNode) {
			physicalLink.setStartNode(leftNode);
			physicalLink.setEndNode(rightNode);
		}
		else {
			// в противном случае разделить линию на две

			// создать вторую физическую линию
			PhysicalLink newPhysicalLink = super.createPhysicalLink(
					startNode,
					startNode);

			// переносим фрагменты в новую линию пока не наткнемся на
			// один из концевых узлов удаленного фрагмента
			AbstractNode foundNode = super.moveNodeLinks(
					this.map,
					physicalLink,
					newPhysicalLink,
					false,
					leftNode,
					rightNode);

			newPhysicalLink.setEndNode(foundNode);

			// если наткнулись на разрыв линии связи, то обновить
			// концевые узлы и закончить
			if(foundNode.equals(leftNode)) {
				physicalLink.setStartNode(rightNode);
			} else if(foundNode.equals(rightNode)) {
				physicalLink.setStartNode(leftNode);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}
	}

	/**
	 * удалить концевой фрагмент линии
	 */
	protected void removeNodeLinkBetweenActiveAndInactive(
			NodeLink nodeLink1, 
			PhysicalLink physicalLink,
			TopologicalNode activeNode,
			TopologicalNode inactiveNode)
			throws Throwable {
		super.removeNodeLink(nodeLink1);
		super.removeNode(inactiveNode);
		super.changePhysicalNodeActivity(activeNode, false);

		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink(nodeLink1);

		// обновить концевый узлы
		if(physicalLink.getStartNode().equals(inactiveNode)) {
			physicalLink.setStartNode(activeNode);
		}
		else {
			physicalLink.setEndNode(activeNode);
		}

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}
	
	/**
	 * удалить фрагмент между сетевым узлом и топологическим активным узлом
	 */
	protected void removeNodeLinkBetweenSiteAndActiveNode(
			NodeLink nodeLink1, 
			PhysicalLink physicalLink,
			SiteNode site,
			TopologicalNode node)
			throws Throwable {
		super.changePhysicalNodeActivity(node, false);

		super.removeNodeLink(nodeLink1);
		
		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink(nodeLink1);

		// обновить концевый узлы
		if (physicalLink.getStartNode().equals(site)) {
			physicalLink.setStartNode(node);
		}
		else {
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
			NodeLink nodeLink1, 
			PhysicalLink physicalLink,
			TopologicalNode node)
			throws Throwable {
		super.removeNode(node);
		super.removeNodeLink(nodeLink1);
		super.removePhysicalLink(physicalLink);
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "  //$NON-NLS-1$
				+ "delete nodeLink "  //$NON-NLS-1$
				+ this.nodeLink.getName() + " (" + this.nodeLink.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);
		
		// фрагмент может быть удален в результате атомарной команды в составе
		// другой команды удаления, в этом случае у него будет выставлен
		// флаг isRemoved
		if(this.nodeLink.isRemoved()) {
			return;
		}

		this.map = this.logicalNetLayer.getMapView().getMap();

		try {
			PhysicalLink physicalLink = 
					this.nodeLink.getPhysicalLink();
			final AbstractNode startNode = this.nodeLink.getStartNode();
			final AbstractNode endNode = this.nodeLink.getEndNode();
			if (startNode instanceof SiteNode
					&& endNode instanceof SiteNode) {
				this.removeNodeLinkBetweenTwoSites(this.nodeLink, physicalLink);
			}//MapSiteNodeElement && MapSiteNodeElement
			else if (startNode instanceof TopologicalNode
					&& endNode instanceof TopologicalNode) {
				TopologicalNode leftNode = 
						(TopologicalNode) startNode;
				TopologicalNode rightNode = 
						(TopologicalNode) endNode;
				
				if(leftNode.isActive() && rightNode.isActive()) {
					this.removeNodeLinkBetweenActiveNodes(
							this.nodeLink, 
							physicalLink, 
							leftNode, 
							rightNode);
				}// isActive() && isActive()
				else if( !leftNode.isActive() && !rightNode.isActive()) {
					this.removeNodeLinkBetweenInactiveNodes(
							this.nodeLink, 
							physicalLink, 
							leftNode, 
							rightNode);
				}// ! isActive() && ! isActive()
				else {
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
			else {
				// defaults to MapSiteNodeElement && MapPhysicalNodeElement
				SiteNode site = null;
				TopologicalNode node = null;
				
				if(startNode instanceof SiteNode
					&& endNode instanceof TopologicalNode) {
					site = (SiteNode)startNode;
					node = (TopologicalNode)endNode;

				}
				else if(endNode instanceof SiteNode
						&& startNode instanceof TopologicalNode) {
					site = (SiteNode)endNode;
					node = (TopologicalNode)startNode;
				}

				if (node.isActive()) {
					this.removeNodeLinkBetweenSiteAndActiveNode(
							this.nodeLink, 
							physicalLink, 
							site, 
							node);
				}//if (node.isActive())
				else {
					this.removeNodeLinkBetweenSiteAndInactiveNode(
							this.nodeLink, 
							physicalLink, 
							node);
				}//if ! (node.isActive())
			}//MapSiteNodeElement && MapPhysicalNodeElement
			MapView mapView = this.logicalNetLayer.getMapView();
			final AbstractNode startNode2 = physicalLink.getStartNode();
			final AbstractNode endNode2 = physicalLink.getEndNode();
			if (startNode2 instanceof SiteNode
					&& endNode2 instanceof SiteNode) {
				for(CablePath cablePath : mapView.getCablePaths(physicalLink)) {
					setUndoable(false);
					UnboundLink unbound = 
						super.createUnboundLinkWithNodeLink(
							startNode2,
							endNode2);
					unbound.setCablePath(cablePath);

//					CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(physicalLink);
					for(CableChannelingItem cableChannelingItem : cablePath.getSchemeCableLink().getPathMembers()) {
						if(cablePath.getBinding().get(cableChannelingItem) == physicalLink) {
							CableChannelingItem newCableChannelingItem = 
								CableController.generateCCI(
										cablePath, 
										unbound,
										cableChannelingItem.getStartSiteNode(),
										cableChannelingItem.getEndSiteNode());
							newCableChannelingItem.insertSelfBefore(cableChannelingItem);
							cableChannelingItem.setParentPathOwner(null, false);
							cablePath.removeLink(cableChannelingItem);
							cablePath.addLink(unbound, newCableChannelingItem);
						}
					}
				}
			}
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugException(e, Level.SEVERE);
		}
	}
}
