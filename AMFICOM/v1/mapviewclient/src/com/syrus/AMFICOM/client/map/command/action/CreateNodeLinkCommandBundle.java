/*-
 * $$Id: CreateNodeLinkCommandBundle.java,v 1.36 2005/10/30 14:48:56 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * создание фрагмента линии, которое строится из 
 * последовательности атомарных действий. соответствует рисованию фрагмента
 * пользователем: пользователь нажимает на узле левой кнопкой, драгает
 * курсор в нужное место и отпускает. В момент отпускания выполняется 
 * данная команда
 * 
 * @version $Revision: 1.36 $, $Date: 2005/10/30 14:48:56 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateNodeLinkCommandBundle extends MapActionCommandBundle {
	/**
	 * константа используется для передачи в команду параметра (конечной
	 * точки для фрагмента)
	 */
	public static final String END_POINT = "endpoint"; //$NON-NLS-1$

	/** Начальный узел фрагмента */
	AbstractNode startNode;
	
	Map map;
	
	/** Точка, в которой должен быть конечный узел фрагмента */
	Point endPoint;

	private PhysicalLink physicalLink;

	private NodeLink nodeLink;

	/**
	 * 
	 * @param startNode
	 */
	public CreateNodeLinkCommandBundle(AbstractNode startNode) {
		super();
		this.startNode = startNode;
	}

	@Override
	public void setParameter(String field, Object value) {
		if(field.equals(END_POINT)) {
			this.endPoint = (Point) value;
		}
	}

	void createSiteToSite(AbstractNode endNode) throws Throwable {
		// создается новая физическая линия из одного фрагмента
		this.physicalLink = super.createPhysicalLink(this.startNode, endNode);
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);
	}

	void createSiteToNewNode(DoublePoint mapEndPoint) throws Throwable {
		this.physicalLink = super.createPhysicalLink(this.startNode, this.startNode);
		AbstractNode endNode = super.createPhysicalNode(this.physicalLink, mapEndPoint);
		this.physicalLink.setEndNode(endNode);
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);
	}

	void createSiteToNode(TopologicalNode endNode) throws Throwable {
		this.physicalLink = endNode.getPhysicalLink();
		MapElementState pls = this.physicalLink.getState();
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);
		super.changePhysicalNodeActivity(endNode, true);

		// Коррекция начального и конечного узлов линии
		if(this.physicalLink.getEndNode().equals(endNode))
			this.physicalLink.setEndNode(this.physicalLink.getStartNode());
		this.physicalLink.setStartNode(this.startNode);
		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	void createNodeToSite(SiteNode endNode) throws Throwable {
		// существующая физическая линия завершается на узле site
		TopologicalNode topologicalNode = (TopologicalNode )this.startNode;
		this.physicalLink = topologicalNode.getPhysicalLink();
		MapElementState pls = this.physicalLink.getState();
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);

		super.changePhysicalNodeActivity(topologicalNode, true);

		// Коррекция начального и конечного узлов линии
		if(this.physicalLink.getStartNode().equals(this.startNode))
			this.physicalLink.setStartNode(this.physicalLink.getEndNode());
		this.physicalLink.setEndNode(endNode);
		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	void createNodeToNewNode(DoublePoint mapEndPoint) throws Throwable {
		TopologicalNode startTopologicalNode = (TopologicalNode )this.startNode;

		this.physicalLink = startTopologicalNode.getPhysicalLink();

		MapElementState pls = this.physicalLink.getState();

		TopologicalNode endNode = super.createPhysicalNode(this.physicalLink, mapEndPoint);

		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);

		super.changePhysicalNodeActivity(startTopologicalNode, true);

		// Коррекция начального и конечного узлов линии
		if(this.physicalLink.getStartNode().equals(this.startNode))
			this.physicalLink.setStartNode(this.physicalLink.getEndNode());
		this.physicalLink.setEndNode(endNode);

		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	void createNodeToNode(TopologicalNode endNode) throws Throwable {
		TopologicalNode startTopologicalNode = (TopologicalNode )this.startNode;
		TopologicalNode endTopologicalNode = endNode;

		this.physicalLink = startTopologicalNode.getPhysicalLink();
		PhysicalLink physicalLinkToRemove = endTopologicalNode.getPhysicalLink();

		MapElementState pls = this.physicalLink.getState();

		this.nodeLink = super.createNodeLink(this.physicalLink, startTopologicalNode, endTopologicalNode);

		super.changePhysicalNodeActivity(startTopologicalNode, true);
		super.changePhysicalNodeActivity(endTopologicalNode, true);

		// Коррекция начального и конечного узлов линии
		if(this.physicalLink.getStartNode().equals(this.startNode))
			this.physicalLink.setStartNode(this.physicalLink.getEndNode());
		this.physicalLink.setEndNode(endNode);

		if(physicalLinkToRemove != null
				&& !physicalLinkToRemove.equals(this.physicalLink)) {
			MapElementState pls2 = physicalLinkToRemove.getState();
	
			// отдельный список, поскольку используется операция удаления
			LinkedList nodeLinksToMove = new LinkedList();
			nodeLinksToMove.addAll(physicalLinkToRemove.getNodeLinks());
	
			// Перенос фрагментов линии из одной линии в другую
			for(Iterator it = nodeLinksToMove.iterator(); it.hasNext();) {
				NodeLink tmpNodeLink = (NodeLink)it.next();
				physicalLinkToRemove.removeNodeLink(tmpNodeLink);
				tmpNodeLink.setPhysicalLink(this.physicalLink);
				this.physicalLink.addNodeLink(tmpNodeLink);
			}			
		
			// Коррекция начального и конечного узлов линии
			if(physicalLinkToRemove.getStartNode().equals(endNode))
				this.physicalLink.setEndNode(physicalLinkToRemove.getEndNode());
			else
				this.physicalLink.setEndNode(physicalLinkToRemove.getStartNode());
	
			super.registerStateChange(physicalLinkToRemove, pls2, physicalLinkToRemove.getState());
	
			super.removePhysicalLink(physicalLinkToRemove);
		}

		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	@Override
	public void execute() {
		try {
			Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "create node link from node " + this.startNode.getName() //$NON-NLS-1$
					+ " (" + this.startNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);
			// анализируется элемент в точке, в которой отпущена мышка		
			MapElement curElementAtPoint = this.logicalNetLayer.getVisibleMapElementAtPoint(this.endPoint, this.netMapViewer.getVisibleBounds());
			// если мышка отпущена на том же элементе, то линию не рисовать
			if(curElementAtPoint.equals(this.startNode))
				return;
			this.map = this.logicalNetLayer.getMapView().getMap();
			AbstractNode endNode = null;
			DoublePoint mapEndPoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.endPoint);
			// если в конечной точке уже есть элемент, проверяем, какой это узел
			if ( curElementAtPoint != null
				&& curElementAtPoint instanceof AbstractNode) {
				endNode = (AbstractNode )curElementAtPoint;

				// конечный элемент - топологический узел
				if(endNode instanceof TopologicalNode) {
					TopologicalNode mpne = (TopologicalNode )endNode;
			
					// если он активный, то есть находится в середине другой линии,
					// то в той же точке создается новый
					if(mpne.isActive()) {
						// node created with fake this.physicalLink
						// should be later updated (e.g. through call to
						// nodelink.setPhysicalLink or node.setPhysicalLink)
						endNode = null;
//					endNode = super.createPhysicalNode(mpne.getPhysicalLink(), mapEndPoint);
					}
					else {
						// если он - концевой для линии, то замкнуть новый фрагмент
						// на него (дорисовка существующей линии или объелинение
						// физических линий)
						super.changePhysicalNodeActivity(mpne, true);
					}
				}
			}
			else {
				// Если в конечной точке нет элемента, то создаем новый конечный 
				// топологический узел
				endNode = null;
//			endNode = super.createPhysicalNode(mapEndPoint);
			}
			if(endNode == null) {
				if (this.startNode instanceof SiteNode) {
					createSiteToNewNode(mapEndPoint);
				}
				else if (this.startNode instanceof TopologicalNode) {
					createNodeToNewNode(mapEndPoint);
				}
			}
			else {
				if (this.startNode instanceof SiteNode 
					&& endNode instanceof SiteNode ) {
					if(endNode instanceof UnboundNode) {
						createSiteToNewNode(mapEndPoint);
					}
					else {
						createSiteToSite(endNode);
					}
				}
				else if ( this.startNode instanceof SiteNode
					&& endNode instanceof TopologicalNode ) {
					createSiteToNode((TopologicalNode )endNode);
				}
				else if ( this.startNode instanceof TopologicalNode
					&& endNode instanceof SiteNode ) {
					createNodeToSite((SiteNode )endNode);
				}
				else if ( this.startNode instanceof TopologicalNode 
					&& endNode instanceof TopologicalNode ) {
					createNodeToNode((TopologicalNode )endNode);
				}
			}
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugMessage(e, Level.SEVERE);
		}
	}
}
