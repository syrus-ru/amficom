/**
 * $Id: MapMouseMotionListener.java,v 1.10 2005/02/10 11:48:39 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategyManager;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Обработчик перемещения мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если действий в состоянии нет,
 * то обработка события передается текущему активному элементу карты
 * (посредством объекта MapStrategy)
 * 
 * @version $Revision: 1.10 $, $Date: 2005/02/10 11:48:39 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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

//		System.out.println("Dragged to (" + me.getPoint().x + ", " + me.getPoint().y + ")");

		this.logicalNetLayer.setCurrentPoint(me.getPoint());
		MapState mapState = this.logicalNetLayer.getMapState();
		
		mapState.setMouseMode(MapState.MOUSE_DRAGGED);
		if ( this.logicalNetLayer.getMapView() != null)
		{
			this.logicalNetLayer.setEndPoint(me.getPoint());
			this.logicalNetLayer.showLatLong(me.getPoint());

			//Обрабатывает события на панели инстрементов
			switch (mapState.getOperationMode())
			{
				case MapState.MEASURE_DISTANCE:
					this.logicalNetLayer.repaint(false);
					break;
				case MapState.MOVE_HAND:
					//Если перемещают карту лапкой
					this.logicalNetLayer.handDragged(me);
					break;
				case MapState.MOVE_TO_CENTER:
					break;
				case MapState.ZOOM_TO_RECT:
					this.logicalNetLayer.repaint(false);
					break;
				case MapState.NODELINK_SIZE_EDIT:
					break;
				case MapState.MOVE_FIXDIST:
					// fall through
				case MapState.NO_OPERATION:
					MapElement mapElement = this.logicalNetLayer.getCurrentMapElement();
					MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
					if(strategy != null)
					{
						strategy.setLogicalNetLayer(this.logicalNetLayer);
						strategy.doContextChanges(me);
					}
	
					this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
								mapElement, 
								MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					this.logicalNetLayer.repaint(false);
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
		this.logicalNetLayer.setCurrentPoint(me.getPoint());
		
		MapState mapState = this.logicalNetLayer.getMapState();

		mapState.setMouseMode( MapState.MOUSE_MOVED);
		this.logicalNetLayer.showLatLong( me.getPoint());//Выводим значения широты и долготы
		mapState.setMouseMode( MapState.MOUSE_NONE);
	}
}
