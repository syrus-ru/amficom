/**
 * $Id: MapMouseMotionListener.java,v 1.13 2005/05/31 16:10:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
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
 * @version $Revision: 1.13 $, $Date: 2005/05/31 16:10:25 $
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
			try {
				this.logicalNetLayer.showLatLong(me.getPoint());
			} catch(MapConnectionException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch(MapDataException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			//Обрабатывает события на панели инстрементов
			switch (mapState.getOperationMode())
			{
				case MapState.MEASURE_DISTANCE:
					this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
					break;
				case MapState.MOVE_HAND:
					try {
						//Если перемещают карту лапкой
						this.logicalNetLayer.handDragged(me);
					} catch(MapConnectionException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch(MapDataException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					break;
				case MapState.MOVE_TO_CENTER:
					break;
				case MapState.ZOOM_TO_RECT:
					this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
					break;
				case MapState.NODELINK_SIZE_EDIT:
					break;
				case MapState.MOVE_FIXDIST:
					// fall through
				case MapState.NO_OPERATION:
					try {
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
						this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(
								new MapEvent(this, MapEvent.NEED_REPAINT));
					} catch(MapConnectionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch(MapDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
		try {
			//Выводим значения широты и долготы
			this.logicalNetLayer.showLatLong( me.getPoint());
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Обрабатывает события на панели инстрементов
		if(mapState.getOperationMode() == MapState.MOVE_HAND) {
				try {
					//Если перемещают карту лапкой
					this.logicalNetLayer.handMoved(me);
				} catch(MapConnectionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch(MapDataException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		}

		mapState.setMouseMode(MapState.MOUSE_NONE);
	}
}
