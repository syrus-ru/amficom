/*-
 * $$Id: MapMarkerStrategy.java,v 1.35 2006/03/09 13:17:00 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.util.Log;

/**
 * —тратеги€ управлени€ маркером.
 * 
 * @version $Revision: 1.35 $, $Date: 2006/03/09 13:17:00 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapMarkerStrategy extends AbstractMapStrategy 
{
	/**
	 * ћаркер.
	 */
	Marker marker;

	/**
	 * Instance.
	 */
	private static MapMarkerStrategy instance = new MapMarkerStrategy();

	/**
	 * Private constructor.
	 */
	private MapMarkerStrategy()
	{//empty
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapMarkerStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapElement(MapElement me)
	{
		this.marker = (Marker)me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (!(mel instanceof Selection))
			{
				super.logicalNetLayer.deselectAll();
			}
		}//MapState.SELECT_ACTION_MODE
		else if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		super.logicalNetLayer.getMapView().getMap().setSelected(this.marker, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		MarkerController mc = (MarkerController)super.logicalNetLayer.getMapViewController().getController(this.marker);

		MapCoordinatesConverter converter = super.logicalNetLayer.getConverter();

		Log.debugMessage("At distance " + this.marker.getPhysicalDistance()  //$NON-NLS-1$
				+ " " + this.marker.getNodeLink().getId() //$NON-NLS-1$
				+ " " + this.marker.getStartNode().getId() //$NON-NLS-1$
				+ " " + this.marker.getEndNode().getId() //$NON-NLS-1$
				+ " " + this.marker.getCablePath().getId(), Log.DEBUGLEVEL09); //$NON-NLS-1$

//	ѕроверка того что маркер можно перемещать и его перемещение
		if (super.logicalNetLayer.getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER)) {
			// все NodeLink'и по которым может перемещатьс€ маркер
//			List<NodeLink> nodeLinks = this.marker.getMeasurementPath().getSortedNodeLinks();
			
			NodeLink nearestNodeLink = null;
			CablePath path = null;
			double shortestDistance = Integer.MAX_VALUE;
			
			for(CablePath cablePath : this.marker.getMeasurementPath().getSortedCablePaths()) {
				for(NodeLink nodeLink : cablePath.getSortedNodeLinks()) {
					double dist = distance(point, nodeLink);
					if (dist < shortestDistance) {
						shortestDistance = dist;
						nearestNodeLink = nodeLink;
						path = cablePath;
					}		
				}
			}

			AbstractNode startNode = nearestNodeLink.getStartNode();
			AbstractNode endNode = nearestNodeLink.getEndNode();
			Point startPoint = converter.convertMapToScreen(startNode.getLocation());
			Point endPoint = converter.convertMapToScreen(endNode.getLocation());
			Point anchorPoint = converter.convertMapToScreen(this.marker.getLocation());
			
			double lengthFromStartNode = getLenghtFromStart(startPoint, endPoint, anchorPoint, point);
			double nodeLinkLength = distance(startPoint, endPoint);
			if (lengthFromStartNode < 0) {
				lengthFromStartNode = 0;
			} else if (lengthFromStartNode > nodeLinkLength) {
				lengthFromStartNode = nodeLinkLength;
			}
			
			this.marker.setNodeLink(nearestNodeLink);
			this.marker.setCablePath(path);
			this.marker.setStartNode(startNode);
			this.marker.setEndNode(endNode);
			try {
				final double fromStartLengthLf = mc.getFromStartLengthLf(this.marker);
				this.marker.setPhysicalDistance(fromStartLengthLf);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			 
			mc.adjustPosition(this.marker, lengthFromStartNode);
			mc.notifyMarkerMoved(this.marker);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{//empty
		}
	}
	
	private double distance(Point b, NodeLink link) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		final Point a = converter.convertMapToScreen(link.getStartNode().getLocation());
		final Point c = converter.convertMapToScreen(link.getEndNode().getLocation());
		
		double scalar1 = scalar(a, c, a, b);
		if (scalar1 < 0) { // угол между AC и AB тупой 
			return distance(a, b);
		}
		double scalar2 = scalar(c, a, c, b);
		if (scalar2 < 0) { // угол между CA и CB тупой 
			return distance(c, b);
		}
		double l1 = distance(a, b);
		double l2 = distance(b, c);
		double l3 = distance(a, c);
		double p = (l1 + l2 + l3) / 2;
		double h = (2 * Math.sqrt(p * (p - l1) * (p - l2) * (p - l3))) / l3;
		return h;
	}
	
	private double distance(Point p1, Point p2) {
		return Math.sqrt((long)(p1.x - p2.x) * (long)(p1.x - p2.x) + (long)(p1.y - p2.y) * (long)(p1.y - p2.y));
	}
	
	private double scalar(Point p1, Point p2, Point p3, Point p4) {
		return (p2.x - p1.x) * (p4.x - p3.x) + (p2.y - p1.y) * (p4.y - p3.y);
	}
	
	private double getLenghtFromStart(Point startPoint, Point endPoint, Point anchorPoint, Point point) {
		double nodeLinkLength = distance(startPoint, endPoint);
		double lengthFromStartNode = distance(startPoint, anchorPoint);
		double lengthThisToMousePoint = distance(point, anchorPoint);
		double cosA = (lengthThisToMousePoint == 0 ) ? 0.0 :
				(	(endPoint.x - startPoint.x) * (point.x - anchorPoint.x) + 
					(endPoint.y - startPoint.y) * (point.y - anchorPoint.y) ) /
					( nodeLinkLength * lengthThisToMousePoint );
		
		// скал€рное произведение векторов фрагмента и lengthFromStartNode
		double scalar = scalar(startPoint, endPoint, startPoint, anchorPoint);

		if(scalar < 0) {
			lengthFromStartNode = -lengthFromStartNode;
		}
		lengthFromStartNode = lengthFromStartNode + cosA * lengthThisToMousePoint;
		return lengthFromStartNode;
	}
}

