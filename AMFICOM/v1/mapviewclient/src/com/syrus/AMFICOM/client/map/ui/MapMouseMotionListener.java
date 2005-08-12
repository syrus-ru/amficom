/**
 * $Id: MapMouseMotionListener.java,v 1.30 2005/08/12 10:57:49 krupenn Exp $
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
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.strategy.MapStrategy;
import com.syrus.AMFICOM.client.map.strategy.MapStrategyManager;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * Обработчик перемещения мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если действий в состоянии нет,
 * то обработка события передается текущему активному элементу карты
 * (посредством объекта MapStrategy)
 * 
 * @version $Revision: 1.30 $, $Date: 2005/08/12 10:57:49 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public final class MapMouseMotionListener implements MouseMotionListener
{
	NetMapViewer netMapViewer;
	
	public MapMouseMotionListener(NetMapViewer netMapViewer)
	{
		this.netMapViewer = netMapViewer;
	}

	public void mouseDragged(MouseEvent me)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();

//		System.out.println("Dragged to (" + me.getPoint().x + ", " + me.getPoint().y + ")");

		logicalNetLayer.setCurrentPoint(me.getPoint());
		MapState mapState = logicalNetLayer.getMapState();
		
		mapState.setMouseMode(MapState.MOUSE_DRAGGED);
		if ( logicalNetLayer.getMapView() != null)
		{
			logicalNetLayer.setEndPoint(me.getPoint());
			try {
				this.netMapViewer.showLatLong(me.getPoint());
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
					logicalNetLayer.sendMapEvent(MapEvent.NEED_REPAINT);
					break;
				case MapState.MOVE_HAND:
					try {
						//Если перемещают карту лапкой
						this.netMapViewer.handDragged(me);
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
				case MapState.NAVIGATE:
					break;
				case MapState.ZOOM_TO_RECT:
					logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
					break;
				case MapState.NODELINK_SIZE_EDIT:
					break;
				case MapState.MOVE_FIXDIST:
					// fall through
				case MapState.NO_OPERATION:
					try {
						MapElement mapElement = logicalNetLayer.getCurrentMapElement();
						MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
						if(strategy != null)
						{
							strategy.setNetMapViewer(this.netMapViewer);
							strategy.doContextChanges(me);
						}
						logicalNetLayer.sendMapEvent(MapEvent.NEED_REPAINT);
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
						Log.debugMessage("unknown map operation: " + mapState.getOperationMode(), Level.SEVERE);
						throw new Exception("dummy");
					}
					catch(Exception e)
					{
						Log.debugMessage("MapMouseMotionListener::mouseDragged | current execution point with call stack:", Level.FINER);
						Log.debugException(e, Level.SEVERE);
					}
					break;
			}//switch (mapState.getOperationMode()
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	public void mouseMoved(MouseEvent me)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		logicalNetLayer.setCurrentPoint(me.getPoint());
		
		MapState mapState = logicalNetLayer.getMapState();

		mapState.setMouseMode( MapState.MOUSE_MOVED);
		try {
			//Выводим значения широты и долготы
			this.netMapViewer.showLatLong( me.getPoint());
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
				this.netMapViewer.handMoved(me);
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
					this.netMapViewer.mouseMoved(me);
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
			Dimension imageSize = this.netMapViewer.getVisualComponent().getSize();
			int mouseX = me.getPoint().x;
			int mouseY = me.getPoint().y;

			int cursorX =
				(mouseX < imageSize.width * MapPropertiesManager.getNavigateAreaSize()) 
				? 0
				: (mouseX < imageSize.width * (1 - MapPropertiesManager.getNavigateAreaSize())) 
				? 1
				:2;
			int cursorY =
				(mouseY < imageSize.height * MapPropertiesManager.getNavigateAreaSize()) 
				? 0
				: (mouseY < imageSize.height * (1 - MapPropertiesManager.getNavigateAreaSize())) 
				? 1
				: 2;

			this.netMapViewer.setCursor(cursors[cursorX][cursorY]);
		}

		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	private static Cursor[][] cursors = new Cursor[3][3];

//	.getScaledInstance(
//			MapMouseMotionListener.IMG_SIZE,
//			MapMouseMotionListener.IMG_SIZE,
//			Image.SCALE_SMOOTH)
	private static final String NORTH = "north";
	private static final String NORTHWEST = "northwest";
	private static final String WEST = "west";
	private static final String SOUTHWEST = "southwest";
	private static final String SOUTH = "south";
	private static final String SOUTHEAST = "southeast";
	private static final String EAST = "east";
	private static final String NORTHEAST = "northeast";
	static {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension cursorSize = toolkit.getBestCursorSize(24, 24);
		Point hotSpot = new Point(cursorSize.width / 2, cursorSize.height / 2);
		cursors[0][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonorthwest.gif"),
				hotSpot,
				NORTHWEST);
		cursors[0][1] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gowest.gif"),
				hotSpot,
				WEST);
		cursors[0][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosouthwest.gif"),
				hotSpot,
				SOUTHWEST);
		cursors[1][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonorth.gif"),
				hotSpot,
				NORTH);
		cursors[1][1] = Cursor.getDefaultCursor();
		cursors[1][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosouth.gif"),
				hotSpot,
				SOUTH);
		cursors[2][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonortheast.gif"),
				hotSpot,
				NORTHEAST);
		cursors[2][1] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/goeast.gif"),
				hotSpot,
				EAST);		
		cursors[2][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosoutheast.gif"),
				hotSpot,
				SOUTHEAST);
	}
}
