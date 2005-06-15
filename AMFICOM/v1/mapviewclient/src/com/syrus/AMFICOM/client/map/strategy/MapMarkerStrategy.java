/**
 * $Id: MapMarkerStrategy.java,v 1.24 2005/06/15 07:42:28 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.strategy;

import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.ui.MotionDescriptor;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;

/**
 * Стратегия управления маркером.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.24 $, $Date: 2005/06/15 07:42:28 $
 * @module mapviewclient_v1
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
	public void setMapElement(MapElement me)
	{
		this.marker = (Marker)me;
	}

	/**
	 * {@inheritDoc}
	 */
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
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		MarkerController mc = (MarkerController)super.logicalNetLayer.getMapViewController().getController(this.marker);

		MapCoordinatesConverter converter = super.logicalNetLayer.getConverter();

		//Проверка того что маркер можно перемещать и его перемещение
		if (super.logicalNetLayer.getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER))
		{
			NodeLink nodeLink = this.marker.getNodeLink();
			AbstractNode sn = this.marker.getStartNode();
			AbstractNode en = this.marker.getEndNode();
			Point anchorPoint = converter.convertMapToScreen(this.marker.getLocation());
			Point start = converter.convertMapToScreen(sn.getLocation());
			Point end = converter.convertMapToScreen(en.getLocation());
			double lengthFromStartNode;
			MotionDescriptor md = new MotionDescriptor(start, end, anchorPoint, point);
			lengthFromStartNode = md.lengthFromStartNode;
			while (lengthFromStartNode > md.nodeLinkLength)
			{
				nodeLink = this.marker.nextNodeLink();
				if (nodeLink == null)
					lengthFromStartNode = md.nodeLinkLength;
				else
				{
					sn = en;
					en = nodeLink.getOtherNode(sn);
					this.marker.setNodeLink(nodeLink);
					this.marker.setStartNode(sn);
					this.marker.setEndNode(en);
					start = converter.convertMapToScreen(sn.getLocation());
					end = converter.convertMapToScreen(en.getLocation());
					md = new MotionDescriptor(start, end, anchorPoint, point);
					lengthFromStartNode = md.lengthFromStartNode;
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
					en = sn;
					sn = nodeLink.getOtherNode(en);
					this.marker.setNodeLink(nodeLink);
					this.marker.setStartNode(sn);
					this.marker.setEndNode(en);
					start = converter.convertMapToScreen(sn.getLocation());
					end = converter.convertMapToScreen(en.getLocation());
					md = new MotionDescriptor(start, end, anchorPoint, point);
					lengthFromStartNode = md.lengthFromStartNode;
					if (lengthFromStartNode > md.nodeLinkLength)
					{
						lengthFromStartNode = md.nodeLinkLength;
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
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{//empty
		}
	}
}

