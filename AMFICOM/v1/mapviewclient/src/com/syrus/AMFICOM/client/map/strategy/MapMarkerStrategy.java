/*-
 * $$Id: MapMarkerStrategy.java,v 1.34 2006/02/15 12:54:38 stas Exp $$
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
import com.syrus.AMFICOM.client.map.ui.MotionDescriptor;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.util.Log;

/**
 * Стратегия управления маркером.
 * 
 * @version $Revision: 1.34 $, $Date: 2006/02/15 12:54:38 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapMarkerStrategy extends AbstractMapStrategy 
{
	/**
	 * Маркер.
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
		//Проверка того что маркер можно перемещать и его перемещение
		if (super.logicalNetLayer.getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER))
		{
			NodeLink nodeLink = this.marker.getNodeLink();
			AbstractNode startNode = this.marker.getStartNode();
			AbstractNode endNode = this.marker.getEndNode();
			Point anchorPoint = converter.convertMapToScreen(this.marker.getLocation());
			Point startPoint = converter.convertMapToScreen(startNode.getLocation());
			Point endPoint = converter.convertMapToScreen(endNode.getLocation());
			double lengthFromStartNode;
			MotionDescriptor motionDescriptor = new MotionDescriptor(startPoint, endPoint, anchorPoint, point);
			lengthFromStartNode = motionDescriptor.lengthFromStartNode;
			while (lengthFromStartNode > motionDescriptor.nodeLinkLength)
			{
				nodeLink = this.marker.nextNodeLink();
				if (nodeLink == null)
					lengthFromStartNode = motionDescriptor.nodeLinkLength;
				else
				{
					if(this.marker.getCablePath().getStartNode().equals(endNode)
							|| this.marker.getCablePath().getEndNode().equals(endNode)) {
						CablePath nextCablePath = this.marker.nextCablePath();
						if(nextCablePath != null) {
							this.marker.setCablePath(nextCablePath);
						}
					}

					startNode = endNode;
					endNode = nodeLink.getOtherNode(startNode);
					this.marker.setNodeLink(nodeLink);
					this.marker.setStartNode(startNode);
					this.marker.setEndNode(endNode);
					startPoint = converter.convertMapToScreen(startNode.getLocation());
					endPoint = converter.convertMapToScreen(endNode.getLocation());
					motionDescriptor = new MotionDescriptor(startPoint, endPoint, anchorPoint, point);
					lengthFromStartNode = motionDescriptor.lengthFromStartNode;
					if (lengthFromStartNode < 0)
					{
						lengthFromStartNode = 0;
						break;
					}
				}
			}
			while (lengthFromStartNode < 0)
			{
				nodeLink = this.marker.previousNodeLink();
				if (nodeLink == null)
					lengthFromStartNode = 0;
				else
				{
					if(this.marker.getCablePath().getStartNode().equals(startNode)
							|| this.marker.getCablePath().getEndNode().equals(startNode)) {
						CablePath previousCablePath = this.marker.previousCablePath();
						if(previousCablePath != null) {
							this.marker.setCablePath(previousCablePath);
						}
					}

					endNode = startNode;
					startNode = nodeLink.getOtherNode(endNode);
					this.marker.setNodeLink(nodeLink);
					this.marker.setStartNode(startNode);
					this.marker.setEndNode(endNode);
					startPoint = converter.convertMapToScreen(startNode.getLocation());
					endPoint = converter.convertMapToScreen(endNode.getLocation());
					motionDescriptor = new MotionDescriptor(startPoint, endPoint, anchorPoint, point);
					lengthFromStartNode = motionDescriptor.lengthFromStartNode;
					if (lengthFromStartNode > motionDescriptor.nodeLinkLength)
					{
						lengthFromStartNode = motionDescriptor.nodeLinkLength;
						break;
					}
				}
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
}

