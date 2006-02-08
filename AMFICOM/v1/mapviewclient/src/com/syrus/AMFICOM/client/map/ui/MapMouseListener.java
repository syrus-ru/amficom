/*-
 * $$Id: MapMouseListener.java,v 1.75 2006/02/08 12:11:07 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.controllers.MapElementController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.map.popup.MapPopupMenu;
import com.syrus.AMFICOM.client.map.popup.MapPopupMenuManager;
import com.syrus.AMFICOM.client.map.strategy.MapStrategy;
import com.syrus.AMFICOM.client.map.strategy.MapStrategyManager;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * Обработчик мыши в окне карты. При обработке смотрится состояние логического
 * сетевого слоя operationMode. Если режим нулевой (NO_OPERATION), то обработка
 * события передается текущему активному элементу карты (посредством объекта
 * MapStrategy)
 * 
 * @version $Revision: 1.75 $, $Date: 2006/02/08 12:11:07 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapMouseListener implements MouseListener {
	protected MapNodeLinkSizeField sizeEditBox = null;

	/**
	 * Сущность для перемещения курсора мыши в нужную точку
	 */
	private Robot robot = null;

	/**
	 * Величина габарита активной области в процентах от габарита окна карты
	 */
	private static final double ACTIVE_AREA_SIZE = 0.25;

	private NetMapViewer netMapViewer;

	public MapMouseListener(NetMapViewer netMapViewer) throws MapDataException {
		this.netMapViewer = netMapViewer;
		try {
			this.robot = new Robot();
		} catch(AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MapDataException(
					"MapMouseListener - Constructor - Can't create robot"); //$NON-NLS-1$
		}
	}

	public void mouseClicked(MouseEvent me) {
		if(me.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(me)) {
			// nothing for now on double click
		}
	}

	public void mousePressed(MouseEvent me) {
		boolean proceed = true;

		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();

		MapState mapState = logicalNetLayer.getMapState();

		mapState.setMouseMode(MapState.MOUSE_PRESSED);// Установить режим

		Point point = me.getPoint();

		// do not change start and end point when drawing new node link
		if(mapState.getActionMode() != MapState.DRAW_LINES_ACTION_MODE) {
			logicalNetLayer.setStartPoint(point);// Установить начальную
													// точку
			logicalNetLayer.setEndPoint(point);// Установить конечную точку
		}
		switch(mapState.getOperationMode()) {
			case MapState.MOVE_HAND:// Флаг для меремещения карты лапкой
			// fall throuth
			case MapState.MEASURE_DISTANCE:
			// fall throuth
			case MapState.ZOOM_TO_POINT:
			// fall throuth
			case MapState.ZOOM_TO_RECT:
			// fall throuth
			case MapState.MOVE_TO_CENTER:
				// Берём фокус
				this.netMapViewer.getVisualComponent().grabFocus();
				break;
			case MapState.MOVE_FIXDIST:
				try {
					moveFixedDistance(point);
				} catch(MapConnectionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch(MapDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case MapState.NODELINK_SIZE_EDIT:
			// fall throuth
			case MapState.NAVIGATE:
			// fall throuth
			case MapState.NO_OPERATION:
				try {
					// if(SwingUtilities.isRightMouseButton(me)) {
					proceed = checkDescreteNavigation(point);
					// }
					if(!proceed) {
						// this.netMapViewer.repaint(true);
						break;
					}
					proceed = checkNodeSizeEdit(mapState, point);

					if(!proceed)
						break;

					long d = System.currentTimeMillis();
					defaultAction(me);
					long f = System.currentTimeMillis();
					Log
							.debugMessage(
									"defaultAction -------- " + (f - d) + " ms ---------", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
				} catch(MapConnectionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch(MapDataException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
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
									"MapMouseListener::mousePressed | current execution point with call stack:", Level.SEVERE); //$NON-NLS-1$
					Log.debugMessage(e, Level.SEVERE);
				}
				break;
		}// switch (mapState.getOperationMode()
		try {
			if(proceed)
				this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	private boolean checkDescreteNavigation(Point point)
			throws MapConnectionException, MapDataException {

		MapCoordinatesConverter converter = this.netMapViewer
				.getLogicalNetLayer().getConverter();

		if(MapPropertiesManager.isDescreteNavigation()) {
			Dimension imageSize = this.netMapViewer.getVisualComponent()
					.getSize();
			int mouseX = point.x;
			int mouseY = point.y;

			int quadrantX = (mouseX < imageSize.width
					* MapPropertiesManager.getNavigateAreaSize()) ? 0
					: (mouseX < imageSize.width
							* (1 - MapPropertiesManager.getNavigateAreaSize())) ? 1
							: 2;
			int quadrantY = (mouseY < imageSize.height
					* MapPropertiesManager.getNavigateAreaSize()) ? 0
					: (mouseY < imageSize.height
							* (1 - MapPropertiesManager.getNavigateAreaSize())) ? 1
							: 2;

			if(quadrantX != 1 || quadrantY != 1) {

				DoublePoint center = this.netMapViewer.getMapContext()
						.getCenter();

				// Разница между центрами соседних по горизонтали сегментов в
				// пикселах
				int xDifferenceScr = (int) Math.round(imageSize.width
						* (1.D - ACTIVE_AREA_SIZE));
				// Разница между центрами соседних по вертикали сегментов в
				// пикселах
				int yDifferenceScr = (int) Math.round(imageSize.height
						* (1.D - ACTIVE_AREA_SIZE));

				// Считаем координаты центра следующего по горизонтали сегмента
				Point nextHorizCenterScr = new Point(imageSize.width / 2
						+ xDifferenceScr, imageSize.height / 2);
				DoublePoint nextHorizCenterSph = converter
						.convertScreenToMap(nextHorizCenterScr);
				// Считаем расстояние между центрами
				double xDifferenceSph = nextHorizCenterSph.getX()
						- center.getX();

				// Считаем координаты центра следующего по горизонтали сегмента
				Point nextVertCenterScr = new Point(
						imageSize.width / 2,
						imageSize.height / 2 + yDifferenceScr);
				DoublePoint nextVertCenterSph = converter
						.convertScreenToMap(nextVertCenterScr);
				// Считаем расстояние между центрами
				double yDifferenceSph = nextVertCenterSph.getY()
						- center.getY();

				// Перемещаем центр
				// Географические координаты нового центра
				DoublePoint newCenter = new DoublePoint(center.getX()
						+ (quadrantX - 1) * xDifferenceSph, center.getY()
						+ (quadrantY - 1) * yDifferenceSph);

				// Географические координаты текущего положения мыши
				DoublePoint mousePositionSph = converter
						.convertScreenToMap(new Point(mouseX, mouseY));

				Point startPoint = this.netMapViewer.getLogicalNetLayer()
						.getStartPoint();
				startPoint.x -= (quadrantX - 1) * xDifferenceScr;
				startPoint.y -= (quadrantY - 1) * yDifferenceScr;
				this.netMapViewer.getLogicalNetLayer()
						.setStartPoint(startPoint);

				this.netMapViewer.setCenter(newCenter);

				if(MapPropertiesManager.isMoveMouseNavigating()) {
					// Курсор ставим в ту же (в топографических координатах)
					// точку -
					// центр уже сменен
					Point newMousePosition = converter
							.convertMapToScreen(mousePositionSph);

					Point frameLocation = this.netMapViewer
							.getVisualComponent().getLocationOnScreen();

					this.robot.mouseMove(
							frameLocation.x + newMousePosition.x,
							frameLocation.y + newMousePosition.y);
				}

				this.netMapViewer.getLogicalNetLayer().getMapState()
						.setOperationMode(MapState.NAVIGATE);
				return false;
			}

		}
		return true;
	}

	/**
	 * @param me
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void defaultAction(MouseEvent me)
			throws MapConnectionException, MapDataException {
		long d1 = System.currentTimeMillis();
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		Point point = me.getPoint();
		// Берём фокус
		this.netMapViewer.getVisualComponent().grabFocus();
		MapElement mapElement = logicalNetLayer.getVisibleMapElementAtPoint(
				point,
				this.netMapViewer.getVisibleBounds());
		MapElement curElement = logicalNetLayer.getCurrentMapElement();
		MapElementController mec = logicalNetLayer.getMapViewController()
				.getController(curElement);
		if(curElement instanceof Selection) {
			mapElement = curElement;
		} else if(!(curElement instanceof VoidElement)
				&& mec.isMouseOnElement(curElement, point)) {
			mapElement = curElement;
		} else {
			logicalNetLayer.setCurrentMapElement(mapElement);
		}
		if(SwingUtilities.isLeftMouseButton(me)) {
			long d0 = System.currentTimeMillis();
			MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
			if(strategy != null) {
				strategy.setNetMapViewer(this.netMapViewer);
				strategy.doContextChanges(me);
			}

			long d = System.currentTimeMillis();
			this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
			long f = System.currentTimeMillis();
			Log
					.debugMessage(
							"sendSelectionChangeEvent -------- " + (d0 - d1) + " " + (d - d0) + " " + (f - d) + " ms ---------", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else if(SwingUtilities.isRightMouseButton(me)) {
			MapPopupMenu contextMenu;

			contextMenu = MapPopupMenuManager.getPopupMenu(mapElement);
			if(contextMenu != null) {
				contextMenu.setNetMapViewer(this.netMapViewer);
				contextMenu.setElement(mapElement);
				contextMenu.setPoint(point);
				contextMenu.show(
						this.netMapViewer.getVisualComponent(),
						point.x,
						point.y);
				this.netMapViewer.setMenuShown(true);
			}
		}
	}

	/**
	 * @param mapState
	 * @param point
	 * @throws MapDataException
	 * @throws MapConnectionException
	 */
	private boolean checkNodeSizeEdit(MapState mapState, Point point)
			throws MapConnectionException, MapDataException {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		boolean proceed = true;
		if(logicalNetLayer.getCurrentMapElement() != null)
			if(logicalNetLayer.getCurrentMapElement() instanceof AbstractNode)
				if(mapState.getShowMode() == MapState.SHOW_NODE_LINK) {
					AbstractNode node = (AbstractNode) logicalNetLayer
							.getCurrentMapElement();
					NodeLink nodelink = logicalNetLayer.getEditedNodeLink(
							point,
							this.netMapViewer.getVisibleBounds());
					if(nodelink != null
							&& (nodelink.getStartNode().equals(node) || nodelink
									.getEndNode().equals(node))
							&& nodelink.getPhysicalLink().getType().getSort()
									.value() != PhysicalLinkTypeSort._INDOOR) {
						if(this.sizeEditBox != null)
							if(this.sizeEditBox.isVisible())
								proceed = false;
						this.sizeEditBox = new MapNodeLinkSizeField(
								logicalNetLayer,
								nodelink,
								node);
						NodeLinkController nlc = (NodeLinkController) logicalNetLayer
								.getMapViewController().getController(nodelink);
						Rectangle rect = nlc.getLabelBox(nodelink);
						this.sizeEditBox.setBounds(
								rect.x,
								rect.y,
								rect.width + 3,
								rect.height + 3);
						this.sizeEditBox.setText(MapPropertiesManager
								.getDistanceFormat().format(
										nodelink.getLengthLt()));
						this.sizeEditBox.setSelectionStart(0);
						this.sizeEditBox.setSelectionEnd(this.sizeEditBox
								.getText().length());
						this.sizeEditBox.selectAll();
						this.netMapViewer.getVisualComponent().add(
								this.sizeEditBox);
						this.sizeEditBox.setVisible(true);
						this.sizeEditBox.setEditable(true);
						this.sizeEditBox.grabFocus();

						mapState.setMouseMode(MapState.MOUSE_NONE);
						mapState.setOperationMode(MapState.NODELINK_SIZE_EDIT);

						proceed = false;
					}
				}
		return proceed;
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void moveFixedDistance(Point point)
			throws MapConnectionException, MapDataException {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		logicalNetLayer.deselectAll();
		Map map = logicalNetLayer.getMapView().getMap();
		map.setSelected(logicalNetLayer.getFixedNode(), true);
		MapElement mapElement = logicalNetLayer.getVisibleMapElementAtPoint(
				point,
				this.netMapViewer.getVisibleBounds());
		if(logicalNetLayer.getFixedNodeList().contains(mapElement)) {
			map.setSelected(mapElement, true);
			logicalNetLayer.setCurrentMapElement(mapElement);
			this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
		} else {
			logicalNetLayer.setCurrentMapElement(VoidElement
					.getInstance(logicalNetLayer.getMapView()));
		}
	}

	public void mouseEntered(MouseEvent me) {// empty
	}

	public void mouseExited(MouseEvent me) {// empty
	}

	public void mouseReleased(MouseEvent me) {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapState mapState = logicalNetLayer.getMapState();

		if(this.sizeEditBox != null)
			if(this.sizeEditBox.isVisible())
				return;

		mapState.setMouseMode(MapState.MOUSE_RELEASED);
		if(logicalNetLayer.getMapView() != null) {
			try {
				// Обрабатывает события на панели инстрементов
				Point point = me.getPoint();
				switch(mapState.getOperationMode()) {
					case MapState.MEASURE_DISTANCE:
						finishMeasureDistance(point);
						break;
					case MapState.ZOOM_TO_POINT:
						finishZoomToPoint(point);
						break;
					case MapState.ZOOM_TO_RECT:
						finishZoomToRect();
						break;
					case MapState.MOVE_TO_CENTER:
						finishMoveToCenter(point);
						break;
					case MapState.MOVE_HAND:
						finishMoveHand(me);
						break;
					case MapState.NAVIGATE:
						if(MapPropertiesManager.isMoveMouseNavigating())
							this.netMapViewer.setCursor(Cursor
									.getDefaultCursor());
						break;
					case MapState.MOVE_FIXDIST:
					// fall through
					case MapState.NODELINK_SIZE_EDIT:
					// fall throuth
					case MapState.NO_OPERATION:
						finishDefaultAction(me, mapState);
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
											"MapMouseListener::mouseReleased | current execution point with call stack:", Level.SEVERE); //$NON-NLS-1$
							Log.debugMessage(e, Level.SEVERE);
						}
						break;
				}// switch (mapState.getOperationMode()
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(mapState.getOperationMode() != MapState.MOVE_HAND
					&& mapState.getOperationMode() != MapState.MOVE_FIXDIST)
				mapState.setOperationMode(MapState.NO_OPERATION);

			// Убираем флаг
			this.netMapViewer.setMenuShown(false);
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);// Для мыши
	}

	/**
	 * @param me
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishDefaultAction(MouseEvent me, MapState mapState)
			throws MapConnectionException, MapDataException {
		if(!this.netMapViewer.isMenuShown()) {
			LogicalNetLayer logicalNetLayer = this.netMapViewer
					.getLogicalNetLayer();

			// эти поля почему-то меняются в strategy.doContextChanges(me) - поэтому запомним их здесь
			int actionMode = mapState.getActionMode();
			int operationMode = mapState.getOperationMode();
			
			// Контекстное меню показывать не надо и передаём управление
			// стратегии текущего объекта
			MapElement mapElement = logicalNetLayer.getCurrentMapElement();

			MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
			if(strategy != null) {
				strategy.setNetMapViewer(this.netMapViewer);
				strategy.doContextChanges(me);
			}
			// MAP_CHANGED now sends only for map changing events // Stas
			if (actionMode == MapState.ALT_LINK_ACTION_MODE
					|| actionMode == MapState.DRAW_ACTION_MODE
					|| actionMode == MapState.DRAW_LINES_ACTION_MODE){
				logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			} else if (actionMode == MapState.SELECT_MARKER_ACTION_MODE) {
				logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			}
		}
		// mapState.setActionMode(MapState.NULL_ACTION_MODE);
		// this.netMapViewer.repaint(false);
	}

	/**
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishMoveHand(MouseEvent me)
			throws MapConnectionException, MapDataException {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapCoordinatesConverter converter = logicalNetLayer.getConverter();
		// if(!MapPropertiesManager.isDescreteNavigation()) {
		DoublePoint center = this.netMapViewer.getMapContext().getCenter();
		DoublePoint p1 = converter.convertScreenToMap(logicalNetLayer
				.getStartPoint());
		DoublePoint p2 = converter.convertScreenToMap(me.getPoint());
		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY();
		center.setLocation(center.getX() + dx, center.getY() + dy);
		this.netMapViewer.setCenter(center);
		// }
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishMoveToCenter(Point point)
			throws MapConnectionException, MapDataException {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapCoordinatesConverter converter = logicalNetLayer.getConverter();
		DoublePoint newCenter = converter.convertScreenToMap(point);
		this.netMapViewer.setCenter(newCenter);
		// logicalNetLayer.getMapView().setCenter(newCenter);
		logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_MOVE_TO_CENTER,
				false);
		logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
		this.netMapViewer.setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishZoomToRect()
			throws MapConnectionException, MapDataException {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapCoordinatesConverter converter = logicalNetLayer.getConverter();
		this.netMapViewer.setCursor(Cursor.getDefaultCursor());
		logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_BOX,
				false);
		logicalNetLayer.getContext().getApplicationModel().fireModelChanged();

		logicalNetLayer.getMapState().setOperationMode(MapState.NO_OPERATION);

		if(!logicalNetLayer.getStartPoint().equals(
				logicalNetLayer.getEndPoint())) {
			this.netMapViewer
					.zoomToBox(converter.convertScreenToMap(logicalNetLayer
							.getStartPoint()), converter
							.convertScreenToMap(logicalNetLayer.getEndPoint()));
		}
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishZoomToPoint(Point point)
			throws MapConnectionException, MapDataException {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapCoordinatesConverter converter = logicalNetLayer.getConverter();
		DoublePoint pp = converter.convertScreenToMap(point);
		this.netMapViewer.setCenter(pp);
		this.netMapViewer.zoomIn();
		this.netMapViewer.setCursor(Cursor.getDefaultCursor());
		logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_TO_POINT,
				false);
		logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
		// logicalNetLayer.getMapView().setScale(mapContext.getScale());
		// logicalNetLayer.getMapView().setCenter(mapContext.getCenter());
	}

	/**
	 * @param point
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void finishMeasureDistance(Point point)
			throws MapConnectionException, MapDataException {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapCoordinatesConverter converter = logicalNetLayer.getConverter();
		DoublePoint sp = converter.convertScreenToMap(logicalNetLayer
				.getStartPoint());
		DoublePoint ep = converter.convertScreenToMap(point);
		double distance = converter.distance(sp, ep);
		this.netMapViewer.setCursor(Cursor.getDefaultCursor());
		logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_MEASURE_DISTANCE,
				false);
		logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
		JOptionPane
				.showMessageDialog(
						Environment.getActiveWindow(),
						I18N
								.getString(MapEditorResourceKeys.LABEL_DISTANCE)
								+ " : " //$NON-NLS-1$
								+ MapPropertiesManager.getDistanceFormat()
										.format(distance)
								+ MapPropertiesManager.getMetric(),
						I18N
								.getString(MapEditorResourceKeys.TITLE_MEASURE_DISTANCE),
						JOptionPane.PLAIN_MESSAGE);
		this.netMapViewer.repaint(false);
	}
}
