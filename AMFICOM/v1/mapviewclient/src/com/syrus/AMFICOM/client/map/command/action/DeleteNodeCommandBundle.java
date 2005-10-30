/*-
 * $$Id: DeleteNodeCommandBundle.java,v 1.53 2005/10/30 15:20:31 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * 
 * @version $Revision: 1.53 $, $Date: 2005/10/30 15:20:31 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class DeleteNodeCommandBundle extends MapActionCommandBundle {
	/**
	 * Удаляемый узел
	 */
	AbstractNode node;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public DeleteNodeCommandBundle(AbstractNode node) {
		this.node = node;
	}
	
	/**
	 * Удалить узел сети
	 */
	protected void deleteSite(SiteNode site)
		throws Throwable {
		if ( !getContext().getApplicationModel().isEnabled(
				MapApplicationModel.ACTION_EDIT_MAP)) {
			return;
		}

		MapView mapView = this.logicalNetLayer.getMapView();
		
		// если удаляется сетевой узел (не непривязанный элемент),
		// необходимо проверить все кабельные пути, включающие его
		for(Iterator it = mapView.getMeasurementPaths(site).iterator(); it.hasNext();) {
			MeasurementPath measurementPath = (MeasurementPath)it.next();
			
			// если удаляемый узел содержит привязку концевого элемента
			// кабельного пути, кабельный путь убирается с карты
			if(measurementPath.getStartNode().equals(site)
					|| measurementPath.getEndNode().equals(site)) {
				super.removeMeasurementPath(measurementPath);
				setUndoable(false);
			}
		}

		List<Scheme> schemes = new LinkedList<Scheme>();
		// если удаляется сетевой узел (не непривязанный элемент),
		// необходимо проверить все кабельные пути, включающие его
		for(CablePath cablePath : mapView.getCablePaths(site)) {
			
			setUndoable(false);
			// если удаляемый узел содержит привязку концевого элемента
			// кабельного пути, кабельный путь убирается с карты
			if(cablePath.getStartNode().equals(site)
				|| cablePath.getEndNode().equals(site)) {
				super.removeCablePathLinks(cablePath);
				super.removeCablePath(cablePath);
				schemes.add(cablePath.getSchemeCableLink().getParentScheme());
			}
			else {
				// в противном случае прохождение кабельного пути через узел
				// заменяется на непривязанную линию
				PhysicalLink left = null;
				PhysicalLink right = null;
				
				// находятся "левая" и "крайняя" линия пути относительно
				// удаляемого узла. Их будет две, поскольку случай
				// одной линии рассмотрен предыдущим ифом
				for(Iterator it2 = cablePath.getLinks().iterator(); it2.hasNext();) {
					PhysicalLink le = (PhysicalLink)it2.next();
					if(le.getStartNode().equals(site)
						|| le.getEndNode().equals(site)) {
						if(left == null)
							left = le;
						else
							right = le;
					}
				}
				
				// удаляются линии
				// вместо них создается новая непривязанная
				UnboundLink unbound = 
					super.createUnboundLinkWithNodeLink(
						left.getOtherNode(site),
						right.getOtherNode(site));
				unbound.setCablePath(cablePath);

				CableChannelingItem leftCableChannelingItem = cablePath.getFirstCCI(left);
				CableChannelingItem rightCableChannelingItem = cablePath.getFirstCCI(right);

				while(leftCableChannelingItem != null) {
					SiteNode start;
					SiteNode end;
					if(leftCableChannelingItem.getStartSiteNode() == rightCableChannelingItem.getEndSiteNode()) {
						start = rightCableChannelingItem.getStartSiteNode();
						end = leftCableChannelingItem.getEndSiteNode();
					}
					else {
						start = leftCableChannelingItem.getStartSiteNode();
						end = rightCableChannelingItem.getEndSiteNode();
					}
					
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								cablePath, 
								unbound,
								start,
								end);
	
					newCableChannelingItem.insertSelfBefore(rightCableChannelingItem);
					leftCableChannelingItem.setParentPathOwner(null, false);
					rightCableChannelingItem.setParentPathOwner(null, false);
	
					cablePath.removeLink(leftCableChannelingItem);
					cablePath.removeLink(rightCableChannelingItem);
	
					cablePath.addLink(unbound, newCableChannelingItem);
	
					leftCableChannelingItem = cablePath.getFirstCCI(left);
					rightCableChannelingItem = cablePath.getFirstCCI(right);
				}

				// если "левая" была непмривязанной, она удаляется (вместе 
				// со своими фрагментами
				if(left instanceof UnboundLink) {
					super.removeUnboundLink((UnboundLink)left);
				}
				// если "правая" была непмривязанной, она удаляется (вместе 
				// со своими фрагментами
				if(right instanceof UnboundLink) {
					super.removeUnboundLink((UnboundLink)right);
				}
			}
		}

		for(Scheme scheme : schemes) {
			this.logicalNetLayer.getMapViewController().scanPaths(scheme);
		}

		//При удалении узла удаляются все фрагменты линий, исходящие из него
		java.util.Set nodeLinksToDelete = mapView.getNodeLinks(site);
		Iterator iter = nodeLinksToDelete.iterator();

		// бежим по списку удаляемых фрагментов
		while(iter.hasNext()) {
			NodeLink nodeLink = (NodeLink)iter.next();
			PhysicalLink physicalLink = nodeLink.getPhysicalLink();
					
			if(physicalLink instanceof UnboundLink) {
				// данный случай обработан в предыдущем цикле
				// (непривязанная линия удалена)
				continue;
			}

			// узел на другом конце фрагмента линии
			AbstractNode oppositeNode = nodeLink.getOtherNode(site);

			// если фрагмент соединяет два узла сети, он составляет одну
			// физическую линию, которую, следовательно, необходимо удалить
			if(oppositeNode instanceof SiteNode) {
				super.removeNodeLink(nodeLink);
				super.removePhysicalLink(physicalLink);
			}//if(oppositeNode instanceof MapSiteNodeElement)
			else {
				TopologicalNode physicalNode = (TopologicalNode)oppositeNode;
				
				// если топологический узел активен, то фрагмент линии удаляется
				// из физической линии
				if(physicalNode.isActive()) {
					super.changePhysicalNodeActivity(physicalNode, false);
					
					super.removeNodeLink(nodeLink);

					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					final AbstractNode endNode = physicalLink.getEndNode();
					if (endNode == site)
						physicalLink.setEndNode(oppositeNode);
					else
						physicalLink.setStartNode(oppositeNode);

					super.registerStateChange(physicalLink, pls, physicalLink.getState());
				}//if(mpne.isActive())
				else {
					// в противном случае фрагмент составляет одну
					// физическую линию, ее необходимо удалить, а также
					// конечный топологический узел
					super.removeNode(physicalNode);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}
			}//if ! (oppositeNode instanceof MapSiteNodeElement)
		}//while(e.hasNext())

		super.removeNode(site);
		setResult(Command.RESULT_OK);
	}
	
	/**
	 * удаление топологического узла
	 */
	public void deletePhysicalNode(TopologicalNode topologicalNode)
		throws Throwable {
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			return;
		}

		MapView mapView = this.logicalNetLayer.getMapView();
		
		PhysicalLink physicalLink = topologicalNode.getPhysicalLink();

		// если узел не активен, то есть является концевым узлом физической линии
		if ( !topologicalNode.isActive() ) {
			//При удалении узла удаляются все фрагменты линий, исходящие из него
			java.util.Set nodeLinksToDelete = mapView.getNodeLinks(topologicalNode);
			Iterator iter = nodeLinksToDelete.iterator();
	
			// бежим по списку удаляемых фрагментов (фактически там только 
			// один элемент)
			while(iter.hasNext()) {
				NodeLink nodeLink = (NodeLink)iter.next();
				AbstractNode oppositeNode = nodeLink.getOtherNode(topologicalNode);
						
				// если фрагмент соединяет топологический узел и узел сети
				// то он составляет одну физическую линию, ее необходимо 
				// удалить, а также конечный топологический узел
				if(oppositeNode instanceof SiteNode) {
					super.removeNode(topologicalNode);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else {
					// в противном случае топологический узел - концевой для
					// физической линии. удалить его
					TopologicalNode mpne = (TopologicalNode)oppositeNode;

					MapElementState pls = physicalLink.getState();

					super.removeNode(topologicalNode);
					super.removeNodeLink(nodeLink);
					
					physicalLink.removeNodeLink(nodeLink);

					super.registerStateChange(physicalLink, pls, physicalLink.getState());
					
					// если второй топологический узел тоже не активен, то 
					// имеется физическая лниия из одного фрагмента и двух
					// узлов, все это хозяйство надо удалить
					if(!mpne.isActive()) {
						super.removeNode(mpne);
						super.removePhysicalLink(physicalLink);
					}
					else {
						// в противном случае обновить концевой узел физической линии
						super.changePhysicalNodeActivity(mpne, false);

						MapElementState pls2 = physicalLink.getState();
						
						final AbstractNode endNode = physicalLink.getEndNode();
						if (endNode == topologicalNode)
							physicalLink.setEndNode(mpne);
						else
							physicalLink.setStartNode(mpne);

						super.registerStateChange(physicalLink, pls2, physicalLink.getState());
					}
				}// if ! (oppositeNode instanceof MapSiteNodeElement)
			}//while(e.hasNext())
		}//if ( !node.isActive() )
		else if ( topologicalNode.isActive() ) {
			// если узел активен, то при его удалении два фрагмента сливаются в один

			// получить смежные фрагменты линии
			Iterator nodeLinksIterator = mapView.getNodeLinks(topologicalNode).iterator();
			NodeLink nodeLinkLeft = 
					(NodeLink)nodeLinksIterator.next();
			NodeLink nodeLinkRight = 
					(NodeLink)nodeLinksIterator.next();
			
			// получить концевые узлы смежных фрагментов
			AbstractNode nodeLeft =
					nodeLinkLeft.getOtherNode(topologicalNode);
			AbstractNode nodeRight =
					nodeLinkRight.getOtherNode(topologicalNode);

			// удалить два фрагмента и связывающий их узел с карты
			super.removeNodeLink(nodeLinkLeft);
			super.removeNodeLink(nodeLinkRight);
			super.removeNode(topologicalNode);

			// В случае, если физическая линия состоит из двух фрагментов,
			// возможны 5 комбинаций:
			// 1. топ узел между двумя сайтами
			// 2. топ узел между топ узлом и сайтом
			// 3. топ узел между двумя концевыми топ узлами
			// 4. топ узел соединенный двумя фрагментами с сайтом
			// 5. топ узел соединенный двумя фрагментами с топ узлом
			//
			// В случаях 1 и 2 обработка такая же, как и в обычном случае
			// (когда больше 2-х фрагментов).
			// В слечаях 3, 4, 5 удаляется физическая линия и все топ. узлы.
			// За управление этой проверкой отвечает переменная do_remove_link
			// и дополнительная соответствующая проверка
			
			boolean doRemoveLink = (nodeLeft == nodeRight)
					|| ((physicalLink.getNodeLinks().size() == 2)
						&& nodeLeft instanceof TopologicalNode
						&& nodeRight instanceof TopologicalNode);

			MapElementState pls = physicalLink.getState();

			//Уделяем два смежных фрагмента из линии и добавить вместо них новый
			physicalLink.removeNodeLink(nodeLinkLeft);
			physicalLink.removeNodeLink(nodeLinkRight);
			
			if(doRemoveLink) {
				// удаляется линия
				super.removePhysicalLink(physicalLink);
				if(nodeLeft instanceof TopologicalNode) {
					super.removeNode(nodeLeft);
					if(nodeLeft != nodeRight)
						super.removeNode(nodeRight);
				}
			}
			else {
				// создать новый фрагмент линии и добавить на карту и в линию
				NodeLink newNodeLink = super.createNodeLink(physicalLink, nodeLeft, nodeRight);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());

		}//if ( node.isActive() )
		setResult(Command.RESULT_OK);
	}

	/**
	 * удаление непривязанного узла.
	 * при этом с карты убираются все кабельные пути, содержащие 
	 * удаляемый элемент
	 */
	protected void deleteUnbound(UnboundNode unbound) throws Throwable {
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING)) {
			return;
		}

		MapView mapView = this.logicalNetLayer.getMapView();
	
		super.removeNode(unbound);

		// отдельный список для удаления		
		List<Scheme> schemes = new LinkedList<Scheme>();
		
		for(CablePath cablePath : new LinkedList<CablePath>(mapView.getCablePaths(unbound))) {
			super.removeCablePathLinks(cablePath);
			super.removeCablePath(cablePath);
			schemes.add(cablePath.getSchemeCableLink().getParentScheme());
			setUndoable(false);
		}
		for(Scheme scheme : schemes) {
			this.logicalNetLayer.getMapViewController().scanPaths(scheme);
		}
		setResult(Command.RESULT_OK);
	}

	/**
	 * Удалить метку на физической линии
	 */
	public void deleteMark(Mark mark) throws Throwable {
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		super.removeNode(mark);
		setResult(Command.RESULT_OK);
	}

	/**
	 * Удалить маркер
	 */
	public void deleteMarker(Marker marker) throws Throwable {
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER)) {
			return;
		}

		this.netMapViewer.animateTimer.remove(marker.getNodeLink().getPhysicalLink());
		this.logicalNetLayer.getMapView().removeMarker(marker);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void execute() {
		assert Log.debugMessage(
			getClass().getName() + "::execute() | "  //$NON-NLS-1$
				+ "delete node "  //$NON-NLS-1$
				+ this.node.getName() + " (" + this.node.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		// узел может быть удален в результате атомарной команда в составе
		// другой команды удаления, в этом случае у него будет выставлен
		// флаг isRemoved
		if(this.node.isRemoved())
			return;
			
		this.map = this.logicalNetLayer.getMapView().getMap();
		
		try {
			//В зависимости от того какого типа node и от флагов разрешения удаляем
			if ( this.node instanceof UnboundNode) {
				this.deleteUnbound((UnboundNode)this.node);
			}
			else if ( this.node instanceof SiteNode) {
				this.deleteSite((SiteNode)this.node);
			}
			else if ( this.node instanceof TopologicalNode) {
				this.deletePhysicalNode((TopologicalNode)this.node);
			}
			else if ( this.node instanceof Mark) {
				this.deleteMark((Mark)this.node);
			}
			else if ( this.node instanceof Marker) {
				this.deleteMarker((Marker)this.node);
			}
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}

}

