/**
 * $Id: MapMouseMotionListener.java,v 1.7 2004/11/25 13:00:50 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
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
 * @version $Revision: 1.7 $, $Date: 2004/11/25 13:00:50 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapMouseMotionListener implements MouseMotionListener
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
				case MapState.MEASURE_DISTANCE:
					logicalNetLayer.repaint();
					break;
				case MapState.MOVE_HAND:
					//���� ���������� ����� ������
					logicalNetLayer.handDragged(me);
					break;
				case MapState.MOVE_TO_CENTER:
					break;
				case MapState.ZOOM_TO_RECT:
					logicalNetLayer.repaint();
					break;
				case MapState.NODELINK_SIZE_EDIT:
					break;
				case MapState.MOVE_FIXDIST:
					// fall through
				case MapState.NO_OPERATION:
					MapElement mapElement = logicalNetLayer.getCurrentMapElement();
					MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
					if(strategy != null)
					{
						strategy.setLogicalNetLayer(logicalNetLayer);
						strategy.doContextChanges(me);
					}
	
					logicalNetLayer.sendMapEvent(new MapNavigateEvent(
								mapElement, 
								MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					logicalNetLayer.repaint();
					break;
				default:
					try
					{
						System.out.println("unknown map operation: " + mapState.getOperationMode());
						throw new Exception("dummy");
					}
					catch(Exception e)
					{
						Environment.log(Environment.LOG_LEVEL_FINER, "current execution point with call stack:", null, null, e);
					}
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
