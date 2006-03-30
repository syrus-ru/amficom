/*-
 * $$Id: MapMouseMotionListener.java,v 1.45 2006/03/30 11:29:40 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.strategy.MapStrategy;
import com.syrus.AMFICOM.client.map.strategy.MapStrategyManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * Обработчик перемещения мыши в окне карты. При обработке смотрится состояние
 * логического сетевого слоя operationMode. Если действий в состоянии нет, то
 * обработка события передается текущему активному элементу карты (посредством
 * объекта MapStrategy)
 * 
 * @version $Revision: 1.45 $, $Date: 2006/03/30 11:29:40 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapMouseMotionListener implements MouseMotionListener {
	NetMapViewer netMapViewer;

	public MapMouseMotionListener(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	public void mouseDragged(MouseEvent me) {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		final LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		MapState mapState = logicalNetLayer.getMapState();
		
		if (mapState.getActionMode() == MapState.NULL_ACTION_MODE) { // not an edit action!
			
		} else { 
			if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
				aContext.getDispatcher().firePropertyChange(
						new StatusMessageEvent(
								this,
								StatusMessageEvent.STATUS_MESSAGE,
								I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
				return;
			}
			if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
				aContext.getDispatcher().firePropertyChange(
						new StatusMessageEvent(
								this,
								StatusMessageEvent.STATUS_MESSAGE,
								I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
				return;
			}
		}

		// Log.debugMessage("Dragged to (" + me.getPoint().x + ", " +
		// me.getPoint().y + ")");

		logicalNetLayer.setCurrentPoint(me.getPoint());

		mapState.setMouseMode(MapState.MOUSE_DRAGGED);
		if(logicalNetLayer.getMapView() != null) {
			logicalNetLayer.setEndPoint(me.getPoint());
			try {
				this.netMapViewer.showLatLong(me.getPoint());
			} catch(MapConnectionException e3) {
				Log.errorMessage(e3);
			} catch(MapDataException e3) {
				Log.errorMessage(e3);
			}

			// Обрабатывает события на панели инстрементов
			switch(mapState.getOperationMode()) {
				case MapState.MEASURE_DISTANCE:
					logicalNetLayer.sendMapEvent(MapEvent.NEED_REPAINT);
					break;
				case MapState.MOVE_HAND:
					try {
						// Если перемещают карту лапкой
						this.netMapViewer.handDragged(me);
					} catch(MapConnectionException e2) {
						Log.errorMessage(e2);
					} catch(MapDataException e2) {
						Log.errorMessage(e2);
					}
					break;
				case MapState.MOVE_TO_CENTER:
					break;
				case MapState.NAVIGATE:
					break;
				case MapState.ZOOM_TO_RECT:
					logicalNetLayer.getContext().getDispatcher()
							.firePropertyChange(
									new MapEvent(this, MapEvent.NEED_REPAINT));
					break;
				case MapState.NODELINK_SIZE_EDIT:
					break;
				case MapState.MOVE_FIXDIST:
				// fall through
				case MapState.NO_OPERATION:
					try {
						MapElement mapElement = logicalNetLayer.getCurrentMapElement();
						MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
						if(strategy != null) {
							strategy.setNetMapViewer(this.netMapViewer);
							strategy.doContextChanges(me);
						}
						logicalNetLayer.sendMapEvent(MapEvent.NEED_REPAINT);
					} catch(MapConnectionException e1) {
						Log.errorMessage(e1);
					} catch(MapDataException e1) {
						Log.errorMessage(e1);
					}
					break;
				default:
					try {
						Log
								.debugMessage(
										"unknown map operation: " + mapState.getOperationMode(), Level.SEVERE); //$NON-NLS-1$
						throw new Exception("dummy"); //$NON-NLS-1$
					} catch(Exception e) {
						Log
								.debugMessage(
										"MapMouseMotionListener::mouseDragged | current execution point with call stack:", Level.SEVERE); //$NON-NLS-1$
						Log.errorMessage(e);
					}
					break;
			}// switch (mapState.getOperationMode()
		}
		// mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	public void mouseMoved(MouseEvent me) {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		logicalNetLayer.setCurrentPoint(me.getPoint());

		MapState mapState = logicalNetLayer.getMapState();

		mapState.setMouseMode(MapState.MOUSE_MOVED);
		try {
			// Выводим значения широты и долготы
			this.netMapViewer.showLatLong(me.getPoint());
		} catch(MapConnectionException e) {
			Log.errorMessage(e);
		} catch(MapDataException e) {
			Log.errorMessage(e);
		}

		// Обрабатывает события на панели инстрементов
		if(mapState.getOperationMode() == MapState.MOVE_HAND) {
			try {
				// Если перемещают карту лапкой
				this.netMapViewer.handMoved(me);
			} catch(MapConnectionException e2) {
				Log.errorMessage(e2);
			} catch(MapDataException e2) {
				Log.errorMessage(e2);
			}
		}
		if(mapState.getActionMode() == MapState.NULL_ACTION_MODE)
			if(MapPropertiesManager.isTopologicalImageCache()) {
				try {
					// Если перемещают карту лапкой
					this.netMapViewer.mouseMoved(me);
				} catch(MapConnectionException e2) {
					Log.errorMessage(e2);
				} catch(MapDataException e2) {
					Log.errorMessage(e2);
				}
			}

			if(mapState.getOperationMode() == MapState.NO_OPERATION) {
				if(MapPropertiesManager.isDescreteNavigation()) {
					Dimension imageSize = this.netMapViewer
							.getVisualComponent().getSize();
					int mouseX = me.getPoint().x;
					int mouseY = me.getPoint().y;

					int cursorX = (mouseX < imageSize.width
							* MapPropertiesManager.getNavigateAreaSize()) ? 0
							: (mouseX < imageSize.width
									* (1 - MapPropertiesManager
											.getNavigateAreaSize())) ? 1 : 2;
					int cursorY = (mouseY < imageSize.height
							* MapPropertiesManager.getNavigateAreaSize()) ? 0
							: (mouseY < imageSize.height
									* (1 - MapPropertiesManager
											.getNavigateAreaSize())) ? 1 : 2;

					this.netMapViewer.setCursor(cursors[cursorX][cursorY]);
				}
			} else if(mapState.getOperationMode() == MapState.MOVE_HAND) {
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else if(mapState.getActionMode() == MapState.MEASURE_DISTANCE) {
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			} else if(mapState.getActionMode() == MapState.MOVE_TO_CENTER) {
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			} else if(mapState.getActionMode() == MapState.ZOOM_TO_RECT) {
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			} else if(mapState.getActionMode() == MapState.ZOOM_TO_POINT) {
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}


		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	private static Cursor[][] cursors = new Cursor[3][3];

	// .getScaledInstance(
	// MapMouseMotionListener.IMG_SIZE,
	// MapMouseMotionListener.IMG_SIZE,
	// Image.SCALE_SMOOTH)
	private static final String NORTH = "north"; //$NON-NLS-1$
	private static final String NORTHWEST = "northwest"; //$NON-NLS-1$
	private static final String WEST = "west"; //$NON-NLS-1$
	private static final String SOUTHWEST = "southwest"; //$NON-NLS-1$
	private static final String SOUTH = "south"; //$NON-NLS-1$
	private static final String SOUTHEAST = "southeast"; //$NON-NLS-1$
	private static final String EAST = "east"; //$NON-NLS-1$
	private static final String NORTHEAST = "northeast"; //$NON-NLS-1$
	static {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension cursorSize = toolkit.getBestCursorSize(24, 24);
		Point hotSpot = new Point(cursorSize.width / 2, cursorSize.height / 2);
		cursors[0][0] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/gonorthwest.gif"), //$NON-NLS-1$
				hotSpot, NORTHWEST);
		cursors[0][1] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/gowest.gif"), //$NON-NLS-1$
				hotSpot, WEST);
		cursors[0][2] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/gosouthwest.gif"), //$NON-NLS-1$
				hotSpot, SOUTHWEST);
		cursors[1][0] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/gonorth.gif"), //$NON-NLS-1$
				hotSpot, NORTH);
		cursors[1][1] = Cursor.getDefaultCursor();
		cursors[1][2] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/gosouth.gif"), //$NON-NLS-1$
				hotSpot, SOUTH);
		cursors[2][0] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/gonortheast.gif"), //$NON-NLS-1$
				hotSpot, NORTHEAST);
		cursors[2][1] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/goeast.gif"), //$NON-NLS-1$
				hotSpot, EAST);
		cursors[2][2] = toolkit.createCustomCursor(toolkit
				.createImage("images/cursors/gosoutheast.gif"), //$NON-NLS-1$
				hotSpot, SOUTHEAST);
	}
}
