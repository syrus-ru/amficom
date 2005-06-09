/**
 * $Id: MapMouseMotionListener.java,v 1.17 2005/06/09 08:46:48 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.strategy.MapStrategy;
import com.syrus.AMFICOM.client.map.strategy.MapStrategyManager;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.MapElement;

/**
 * Обработчик перемещения мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если действий в состоянии нет,
 * то обработка события передается текущему активному элементу карты
 * (посредством объекта MapStrategy)
 * 
 * @version $Revision: 1.17 $, $Date: 2005/06/09 08:46:48 $
 * @author $Author: peskovsky $
 * @module mapviewclient_v1
 */
public final class MapMouseMotionListener implements MouseMotionListener
{
	private static final int IMG_SIZE = 16;

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
		if (mapState.getOperationMode() == MapState.MOVE_HAND)
		{		
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
		if(mapState.getActionMode() == MapState.NULL_ACTION_MODE)
			if(MapPropertiesManager.isTopologicalImageCache()) {
				try {
					//Если перемещают карту лапкой
					this.logicalNetLayer.mouseMoved(me);
				} catch(MapConnectionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch(MapDataException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		
		if(mapState.getActionMode() == MapState.NULL_ACTION_MODE)
		if(MapPropertiesManager.isDescreteNavigation()) {
			Dimension imageSize = this.logicalNetLayer.getMapViewer()
					.getVisualComponent().getSize();
			int mouseX = me.getPoint().x;
			int mouseY = me.getPoint().y;

			int cursorX =
				(mouseX < imageSize.width * BORDER_AREA_SIZE_COEFICIENT) 
				? 0
				: (mouseX < imageSize.width * (1 - BORDER_AREA_SIZE_COEFICIENT)) 
				? 1
				:2;
			int cursorY =
				(mouseY < imageSize.height * BORDER_AREA_SIZE_COEFICIENT) 
				? 0
				: (mouseY < imageSize.height * (1 - BORDER_AREA_SIZE_COEFICIENT)) 
				? 1
				: 2;

			this.logicalNetLayer.setCursor(cursors[cursorX][cursorY]);

		}

		mapState.setMouseMode(MapState.MOUSE_NONE);
	}
	/**
	 * Величина габарита области границы (при входе в неё происходит смещение экрана)
	 * в процентах от габарита окна карты
	 */
	public static final double BORDER_AREA_SIZE_COEFICIENT = 0.1;

	private static Cursor[][] cursors = new Cursor[3][3];

//	.getScaledInstance(
//			MapMouseMotionListener.IMG_SIZE,
//			MapMouseMotionListener.IMG_SIZE,
//			Image.SCALE_SMOOTH)
	private static final String NORTHWEST = "northwest";
	static {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Point hotSpot = new Point(0, 0);
		cursors[0][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonorthwest.gif"),
				hotSpot,
				NORTHWEST);
		cursors[0][1] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gowest.gif"),
				hotSpot,
				NORTHWEST);
		cursors[0][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosouthwest.gif"),
				hotSpot,
				NORTHWEST);
		cursors[1][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonorth.gif"),
				hotSpot,
				NORTHWEST);
		cursors[1][1] = Cursor.getDefaultCursor();
		cursors[1][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosouth.gif"),
				hotSpot,
				NORTHWEST);
		cursors[2][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonortheast.gif"),
				hotSpot,
				NORTHWEST);
		cursors[2][1] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/goeast.gif"),
				hotSpot,
				NORTHWEST);		
		cursors[2][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosoutheast.gif"),
				hotSpot,
				NORTHWEST);
	}
}
