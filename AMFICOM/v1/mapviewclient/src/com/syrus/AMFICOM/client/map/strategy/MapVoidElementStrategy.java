/*-
 * $$Id: MapVoidElementStrategy.java,v 1.39 2005/10/30 15:20:33 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * Стратегия управления элементами, когда нет выбранных элементов.
 * 
 * @version $Revision: 1.39 $, $Date: 2005/10/30 15:20:33 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapVoidElementStrategy extends AbstractMapStrategy {
	/**
	 * Топологическая схема.
	 */
	Map map;
	/**
	 * Вид карты.
	 */
	MapView mapView;

	/**
	 * Instance.
	 */
	private static MapVoidElementStrategy instance = new MapVoidElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapVoidElementStrategy() {
		// empty
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapVoidElementStrategy getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapElement(MapElement me) {
		this.mapView = ((VoidElement) me).getMapView();
		this.map = this.mapView.getMap();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMousePressed(MapState mapState, Point point)
			throws MapConnectionException, MapDataException {
		int actionMode = mapState.getActionMode();

		if(actionMode == MapState.NULL_ACTION_MODE) {
			super.logicalNetLayer.deselectAll();
		}// MapState.NULL_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseDragged(MapState mapState, Point point)
			throws MapConnectionException, MapDataException {
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if(actionMode == MapState.NULL_ACTION_MODE
				&& operationMode == MapState.NO_OPERATION) {
			mapState.setActionMode(MapState.SELECT_MARKER_ACTION_MODE);
		}// MapState.NULL_ACTION_MODE && MapState.NO_OPERATION
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseReleased(MapState mapState, Point point)
			throws MapConnectionException, MapDataException {
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if(actionMode == MapState.SELECT_MARKER_ACTION_MODE
				&& operationMode == MapState.NO_OPERATION) {
			int startX = super.logicalNetLayer.getStartPoint().x;
			int startY = super.logicalNetLayer.getStartPoint().y;
			int endX = point.x;
			int endY = point.y;
			Rectangle selectionRect = new Rectangle(
					Math.min(startX, endX),
					Math.min(startY, endY),
					Math.abs(endX - startX),
					Math.abs(endY - startY));
			selectElementsInRect(selectionRect);
			this.logicalNetLayer.sendSelectionChangeEvent();
			mapState.setActionMode(MapState.NULL_ACTION_MODE);
		}// MapState.SELECT_MARKER_ACTION_MODE && MapState.NO_OPERATION
	}

	/**
	 * Выделить все элементы, попадающие в область.
	 * 
	 * @param selectionRect область выборки в экранных координатах
	 */
	protected void selectElementsInRect(Rectangle selectionRect)
			throws MapConnectionException, MapDataException {
		long t1 = System.nanoTime();
		// Здесь просто проверяется что элемент содержится в прямоугольной
		// области
		Point topleft = new Point((int)selectionRect.getMinX(), (int)selectionRect.getMinY());
		Point bottomright = new Point((int)selectionRect.getMaxX(), (int)selectionRect.getMaxY());
		DoublePoint tl = super.logicalNetLayer.getConverter().convertScreenToMap(topleft);
		DoublePoint br = super.logicalNetLayer.getConverter().convertScreenToMap(bottomright);
		Rectangle2D.Double visibleBounds = new Rectangle2D.Double(
				Math.min(tl.getX(), br.getX()),
				Math.min(tl.getY(), br.getY()),
				Math.abs(br.getX() - tl.getX()),
				Math.abs(br.getY() - tl.getY()));

		this.map.clearSelection();

		for(AbstractNode abstractNode : this.logicalNetLayer.getVisibleNodes(visibleBounds)) {
			this.map.setSelected(abstractNode, true);
		}

		int showMode = super.logicalNetLayer.getMapState().getShowMode();
		List<PhysicalLink> checkedPhysicalLinks = null;
		for(NodeLink nodeLink : this.logicalNetLayer.getVisibleNodeLinks(visibleBounds)) {
			switch(showMode) {
				case MapState.SHOW_NODE_LINK: {
					// Пробегаем и смотрим вхотит ли в область nodeLink
					DoublePoint startNodeLocation = nodeLink.getStartNode().getLocation();
					DoublePoint endNodeLocation = nodeLink.getEndNode().getLocation();
					if(visibleBounds.contains(
								startNodeLocation.getX(),
								startNodeLocation.getY())
							&& visibleBounds.contains(
									endNodeLocation.getX(),
									endNodeLocation.getY())) {
						this.map.setSelected(nodeLink, true);
					}
				}
				case MapState.SHOW_PHYSICAL_LINK: {
					if(checkedPhysicalLinks == null) {
						checkedPhysicalLinks = new LinkedList<PhysicalLink>();
					}
					PhysicalLink link = nodeLink.getPhysicalLink();
					if(!checkedPhysicalLinks.contains(link)) {
						boolean select = true;
						for(NodeLink tmpNodeLink : link.getNodeLinks()) {
							DoublePoint startNodeLocation = tmpNodeLink.getStartNode().getLocation();
							DoublePoint endNodeLocation = tmpNodeLink.getEndNode().getLocation();
							if(!visibleBounds.contains(
									startNodeLocation.getX(),
									startNodeLocation.getY())
								|| !visibleBounds.contains(
										endNodeLocation.getX(),
										endNodeLocation.getY())) {
								select = false;
							}
						}
						this.map.setSelected(link, select);
						checkedPhysicalLinks.add(link);
					}
				}
				default:
					//nothing
			}
		}

		Set selection = super.logicalNetLayer.getSelectedElements();
		if(selection.size() == 1) {
			MapElement me = (MapElement) selection.iterator().next();
			super.logicalNetLayer.setCurrentMapElement(me);
		} else if(selection.size() > 1) {
			Selection sel = new Selection();
			sel.addAll(selection);
			super.logicalNetLayer.setCurrentMapElement(sel);
		}
		long t2 = System.nanoTime();
		assert Log.debugMessage((t2 - t1) + " ns ", //$NON-NLS-1$ 
			Level.FINE);
	}
}
