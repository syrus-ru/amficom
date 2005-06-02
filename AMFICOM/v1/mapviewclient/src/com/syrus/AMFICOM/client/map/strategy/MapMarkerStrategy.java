/**
 * $Id: MapMarkerStrategy.java,v 1.22 2005/04/13 11:28:11 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.UI.MotionDescriptor;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;

/**
 * ��������� ���������� ��������.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/04/13 11:28:11 $
 * @module mapviewclient_v1
 */
public final class MapMarkerStrategy extends AbstractMapStrategy 
{
	/**
	 * ������.
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

		MapCoordinatesConverter converter = super.logicalNetLayer;

		//�������� ���� ��� ������ ����� ���������� � ��� �����������
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

