/**
 * $Id: MapMouseMotionListener.java,v 1.5 2004/11/12 19:09:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
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
 * Обработчик перемещения мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если действий в состоянии нет,
 * то обработка события передается текущему активному элементу карты
 * (посредством объекта MapStrategy)
 * 
 * @version $Revision: 1.5 $, $Date: 2004/11/12 19:09:55 $
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

			//Обрабатывает события на панели инстрементов
			switch (mapState.getOperationMode())
			{
				case MapState.MEASURE_DISTANCE:
					logicalNetLayer.repaint();
					break;
				case MapState.MOVE_HAND:
					//Если перемещают карту лапкой
					logicalNetLayer.handDragged(me);
					break;
				case MapState.NODELINK_SIZE_EDIT:
					break;
				default:
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
			}//switch (mapState.getOperationMode()
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	public void mouseMoved(MouseEvent me)
	{
		logicalNetLayer.setCurrentPoint(me.getPoint());
		
		MapState mapState = logicalNetLayer.getMapState();

		mapState.setMouseMode( MapState.MOUSE_MOVED);
		logicalNetLayer.showLatLong( me.getPoint());//Выводим значения широты и долготы
		mapState.setMouseMode( MapState.MOUSE_NONE);
	}
}
