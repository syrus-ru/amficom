/**
 * $Id: MapMouseMotionListener.java,v 1.1 2004/09/13 12:33:43 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategyManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * ���������� ����������� ���� � ���� �����. ��� ��������� ��������� ���������
 * ����������� �������� ���� operationMode. ���� �������� � ��������� ���,
 * �� ��������� ������� ���������� �������� ��������� �������� �����
 * (����������� ������� MapStrategy)
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapMouseMotionListener implements MouseMotionListener
{
	LogicalNetLayer logicalNetLayer;

	public MapMouseMotionListener(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void mouseDragged(MouseEvent me)
	{
		logicalNetLayer.setCurrentPoint(me.getPoint());
		MapState mapState = logicalNetLayer.getMapState();
		
		mapState.setMouseMode(MapState.MOUSE_DRAGGED);
		if ( logicalNetLayer.getMapView() != null)
		{
			logicalNetLayer.setEndPoint(me.getPoint());
			logicalNetLayer.showLatLong(me.getPoint());

			//������������ ������� �� ������ ������������
			switch (mapState.getOperationMode())
			{
				case MapState.MOVE_HAND:
					//���� ���������� ����� ������
					logicalNetLayer.handDragged(me);
	//				logicalNetLayer.getMapViewer().getVisualComponent().setLocation(
	//						me.getPoint().x - logicalNetLayer.getHandMoveStartPoint().x,
	//						me.getPoint().y - logicalNetLayer.getHandMoveStartPoint().y);
					break;
				default:
					MapElement mapElement = logicalNetLayer.getCurrentMapElement();
					MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
					strategy.setMapElement(mapElement);
//					MapStrategy strategy = mapElement.getMapStrategy();
					strategy.setLogicalNetLayer(logicalNetLayer);
					strategy.doContextChanges(me);
	
					logicalNetLayer.sendMapEvent(new MapNavigateEvent(
								mapElement, 
								MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					logicalNetLayer.repaint();
					break;
			}//switch (mapState.getOperationMode()
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	public void mouseMoved(MouseEvent me)
	{
		logicalNetLayer.setCurrentPoint(me.getPoint());
		
		MapState mapState = logicalNetLayer.getMapState();

		mapState.setMouseMode( MapState.MOUSE_MOVED);
		logicalNetLayer.showLatLong( me.getPoint());//������� �������� ������ � �������
		mapState.setMouseMode( MapState.MOUSE_NONE);
	}
}
