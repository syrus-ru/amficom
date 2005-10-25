/*-
 * $$Id: LogicalNetLayer.java,v 1.133 2005/10/25 08:02:45 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.action.MoveNodeCommand;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.AbstractNodeController;
import com.syrus.AMFICOM.client.map.controllers.AlarmMarkerController;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.EventMarkerController;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.MeasurementPathController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.CommandList;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * Управляет отображением логической структуры сети.
 * 
 * @version $Revision: 1.133 $, $Date: 2005/10/25 08:02:45 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class LogicalNetLayer {
	protected CommandList commandList = new CommandList(20);

	/** Перменная показывающая что сейчас отображать. */
	protected MapState mapState = new MapState();

	/** Содержимое карты. */
	protected MapView mapView = null;

	/** Текущий элемент. */
	protected MapElement currentMapElement = null;

	/** 
	 * фиксированный узел. 
	 * Используется в режиме {@link MapState#MOVE_FIXDIST}.
	 */
	protected AbstractNode fixedNode = null;

	/** 
	 * Список фиксированных узлов. 
	 * Используется в режиме {@link MapState#MOVE_FIXDIST}.
	 */
	protected List<AbstractNode> fixedNodeList = new LinkedList<AbstractNode>();

	/** Текущий тип создаваемых физических линий. */
	protected PhysicalLinkType currentPhysicalLinkType = null;

	/** Тип непривязанного схемного элемента. */
	protected SiteNodeType unboundNodeType = null;

	/** Тип непривязанного кабеля. */
	protected PhysicalLinkType unboundLinkType = null;
	
	/** Текущая точка курсора мыши на карте (в экранных координатах). */	
	protected Point currentPoint = new Point(0, 0);

	/** Контекст приложения. */
	protected ApplicationContext aContext = null;

	/**
	 * Начальная точка для операций пользователя на карте с помощью мыши.
	 * При нажатии правой кнопки мыши и её перемещении, поля startX, startY получают
	 * координаты начальной точки (где произошло нажатие), а поля endX, endY координаты
	 * текущего положения мыши.
	 */
	protected Point startPoint = new Point(0, 0);

	/**
	 * Конечная точка для операций пользователя на карте с помощью мыши
	 * При нажатии правой кнопки мыши и её перемещении, поля startX, startY получают
	 * координаты начальной точки (где произошло нажатие), а поля endX, endY координаты
	 * текущего положения мыши.
	 */
	protected Point endPoint = new Point(0, 0);

	/**
	 * Список отображаемых элементов.
	 */
	protected List<PhysicalLink> elementsToDisplay = new LinkedList<PhysicalLink>();

	/**
	 * Масштаб отображения объектов карты по умолчанию. Используется для
	 * автоматического масштабирования отрисовки объектов. Коэффициент
	 * увеличения размера отрисовываемого объекта определяется как отношение
	 * defaultScale / currentScale.
	 */
	protected double defaultScale = 0.00001;

	/** Текуший масштаб отображения оюъектов карты. */
	protected double currentScale = 0.00001;

	/**
	 * Величина шага по смещению центра карты в долях от величины экрана
	 */
	public static final double MOVE_CENTER_STEP_SIZE = 0.33;

	private final MapCoordinatesConverter converter;

	private final MapContext mapContext;

	private MapViewController mapViewController;

	public LogicalNetLayer(final ApplicationContext aContext, final MapCoordinatesConverter converter, final MapContext mapContext)
			throws ApplicationException {
		this.aContext = aContext;
		this.converter = converter;
		this.mapContext = mapContext;

		MapLibraryController.createDefaults();

		AlarmMarkerController.init();
		TopologicalNodeController.init();
		EventMarkerController.init();
		MarkController.init();
		MarkerController.init();
	}

	/**
	 * При изменении масштаба отображения карты необходимо обновить
	 * масштаб отображения всех объектов на карте.
	 * @throws MapConnectionException 
	 * @throws MapDataException 
	 */
	public void updateZoom() throws MapDataException, MapConnectionException {
		Log.debugMessage(this.getClass().getName() + "::" + "updateZoom()" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		if (this.getMapView() == null) {
			return;
		}
		final Map map = this.getMapView().getMap();
		if (map != null) {
			for (final AbstractNode curNode : this.mapView.getAllNodes()) {
				((AbstractNodeController) this.getMapViewController().getController(curNode)).updateScaleCoefficient(curNode);
			}
		}
		if (MapPropertiesManager.isOptimizeLinks()) {
			// calculateVisualLinks();
			try {
				calculateVisualElements();
			} catch(ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Получить контекст приложения.
	 * 
	 * @return контекст приложения
	 */
	public ApplicationContext getContext() {
		return this.aContext;
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	/**
	 * Инициализировать обозреватель заданным видом карты.
	 * 
	 * @param mapView
	 *        вид
	 */
	public void setMapView(final MapView mapView) throws MapConnectionException, MapDataException {
		Log.debugMessage(this.getClass().getName() + "::" + "setMapView(" + mapView + ")" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		if (this.getContext() != null && this.getContext().getDispatcher() != null) {
			if (mapView != null && mapView.getMap() != null) {
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_VIEW_SELECTED, mapView));
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_SELECTED, mapView.getMap()));
			} else {
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
				this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(this, MapEvent.MAP_DESELECTED));
			}
		}

		this.mapView = mapView;
		this.linksForNodes.clear();

		if (mapView == null) {
			Log.debugMessage("mapView null!", Level.SEVERE); //$NON-NLS-1$
		}

		this.getMapViewController().setMapView(this.mapView);

		// Поумолчанию текущий элемент Void
		this.currentMapElement = VoidElement.getInstance(this.mapView);

		this.commandList.flush();
	}

	/**
	 * получить вид.
	 * 
	 * @return вид
	 */
	public MapView getMapView() {
		return this.mapView;
	}

	/**
	 * Получить масштаб элементов по умолчанию.
	 * 
	 * @return масштаб элементов по умолчанию
	 */
	public double getDefaultScale() {
		return this.defaultScale;
	}

	/**
	 * Установить масштаб элементов по умолчанию.
	 * 
	 * @param defaultScale
	 *        масштаб элементов по умолчанию
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	public void setDefaultScale(final double defaultScale) throws MapDataException, MapConnectionException {
		Log.debugMessage(this.getClass().getName() + "::" + "setDefaultScale(" + defaultScale + ")" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		this.defaultScale = defaultScale;
		this.updateZoom();
	}

	/**
	 * Получить текущий масштаб точечных элементов.
	 * 
	 * @return текущий масштаб точечных элементов
	 */
	public double getCurrentScale() {
		return this.currentScale;
	}

	/**
	 * Получить текущее состояние слоя карты.
	 * 
	 * @return состояние слоя карты
	 */
	public MapState getMapState() {
		return this.mapState;
	}

	/**
	 * Установить текущее состояние слоя карты.
	 * 
	 * @param state
	 *        новое состояние
	 */
	public void setMapState(final MapState state) {
		Log.debugMessage(this.getClass().getName() + "::" + "setMapState(" + state + ")" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		this.mapState = state;
	}

	/**
	 * Получить текущую экранную точку.
	 * 
	 * @return текущая экранная точка
	 */
	public Point getCurrentPoint() {
		return this.currentPoint;
	}

	/**
	 * Установить текущую экранную точку.
	 * 
	 * @param point
	 *        текущая экранная точка
	 */
	public void setCurrentPoint(final Point point) {
		this.currentPoint = point;
	}

	/**
	 * Получить запомненную начальную экранную точку.
	 * 
	 * @return начальная экранная точка
	 */
	public Point getStartPoint() {
		return new Point(this.startPoint);
	}

	/**
	 * Установить начальную экранную точку.
	 * 
	 * @param point
	 *        начальная экранная точка
	 */
	public void setStartPoint(final Point point) {
		this.startPoint = new Point(point);
	}

	/**
	 * Получить запомненную конечную экранную точку.
	 * 
	 * @return конечная экранная точка
	 */
	public Point getEndPoint() {
		return new Point(this.endPoint);
	}

	/**
	 * Установить конечную экранную точку.
	 * 
	 * @param point
	 *        конечная экранная точка
	 */
	public void setEndPoint(final Point point) {
		this.endPoint = new Point(point);
	}

	/**
	 * Получить список команд, исполняемых на слое.
	 * 
	 * @return список команд
	 */
	public CommandList getCommandList() {
		return this.commandList;
	}

	/**
	 * Отрисовать все элементы логического слоя топологической схемы.
	 * 
	 * @param g
	 *        графический контекст
	 */
	public void paint(final Graphics g, final Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException {
		if(this.mapView == null) {
			return;
		}
		final Graphics2D p = (Graphics2D) g;

		// remember settings from graphics
		final Color color = p.getColor();
		final Stroke stroke = p.getStroke();
		final Font font = p.getFont();
		final Color background = p.getBackground();

		Log.debugMessage("\n\n------------------ LogicalNetLayer.paint() called ----------------------", Level.FINE); //$NON-NLS-1$
		final long f = System.currentTimeMillis();
		if (MapPropertiesManager.isOptimizeLinks()) {
			drawVisualElements(p, visibleBounds);
		} else {
			try {
				drawLines(p, visibleBounds);
			} catch(ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			drawNodes(p, visibleBounds);
		}
		drawSelection(p, visibleBounds);
		drawTempLines(p, visibleBounds);
		final long d = System.currentTimeMillis();
		Log.debugMessage("\n--------------------- LogicalNetLayer.paint() finished in " //$NON-NLS-1$
				+ String.valueOf(d - f)
				+ " ms -----------------\n", Level.FINE); //$NON-NLS-1$

		// revert graphics to previous settings
		p.setColor(color);
		p.setStroke(stroke);
		p.setFont(font);
		p.setBackground(background);
	}

	/**
	 * Отрисовать линейные объекты.
	 * @param g графический контекст
	 * @throws ApplicationException 
	 */
	public void drawLines(final Graphics g, final Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException, ApplicationException {
		final long f = System.currentTimeMillis();

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();

		this.elementsToDisplay.clear();

		// Если режим показа nodeLink не разрешён, то включам режим показа
		// physicalLink
		if (!this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK)) {
			final Command modeCommand = this.getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		if (this.getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH) {
			this.elementsToDisplay.addAll(this.getMapView().getAllPhysicalLinks());
			for (final MeasurementPath measurementPath : this.mapView.getMeasurementPaths()) {
				// if(this.getMapViewController().getController(measurementPath).isElementVisible(measurementPath,
				// visibleBounds))
				this.getMapViewController().getController(measurementPath).paint(measurementPath, g, visibleBounds);

				// to avoid multiple cicling through scheme elements
				// use once sorted cable links
				for (final CablePath cablePath : measurementPath.getSortedCablePaths()) {
					this.elementsToDisplay.removeAll(cablePath.getLinks());
				}
			}
			for (final PhysicalLink physicalLink : this.elementsToDisplay) {
				// if(this.getMapViewController().getController(physicalLink).isElementVisible(physicalLink,
				// visibleBounds))
				this.getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		} else if (this.getMapState().getShowMode() == MapState.SHOW_CABLE_PATH) {
			this.elementsToDisplay.addAll(this.getMapView().getAllPhysicalLinks());
			for (final CablePath cablePath : this.mapView.getCablePaths()) {
				// if(this.getMapViewController().getController(cablePath).isElementVisible(cablePath,
				// visibleBounds))
				this.getMapViewController().getController(cablePath).paint(cablePath, g, visibleBounds);
				this.elementsToDisplay.removeAll(cablePath.getLinks());
			}
			for (final PhysicalLink physicalLink : this.elementsToDisplay) {
				// if(this.getMapViewController().getController(physicalLink).isElementVisible(physicalLink,
				// visibleBounds))
				this.getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		} else if (this.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK) {
			for (final PhysicalLink physicalLink : this.getMapView().getAllPhysicalLinks()) {
				// if(this.getMapViewController().getController(physicalLink).isElementVisible(physicalLink,
				// visibleBounds))
				this.getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		} else if (this.getMapState().getShowMode() == MapState.SHOW_NODE_LINK) {
			for (final NodeLink nodeLink : this.getMapView().getAllNodeLinks()) {
				// if(this.getMapViewController().getController(nodeLink).isElementVisible(nodeLink,
				// visibleBounds))
				this.getMapViewController().getController(nodeLink).paint(nodeLink, g, visibleBounds);
			}
		}
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawLines | " + String.valueOf(d - f) + " ms\n" + "		" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ String.valueOf(MapViewController.getTime1()) + " ms (isElementVisible)\n" + "		" //$NON-NLS-1$ //$NON-NLS-2$
				+ String.valueOf(MapViewController.getTime2()) + " ms (getStroke)\n" + "		" //$NON-NLS-1$ //$NON-NLS-2$
				+ String.valueOf(MapViewController.getTime3()) + " ms (getColor)\n" + "		" //$NON-NLS-1$ //$NON-NLS-2$
				+ String.valueOf(MapViewController.getTime4()) + " ms (paint)\n" + "		" //$NON-NLS-1$ //$NON-NLS-2$
				+ String.valueOf(MapViewController.getTime5()) + " ms (painting labels)", Level.FINE); //$NON-NLS-1$
	}

	/**
	 * Отрисовать узлы.
	 * 
	 * @param g
	 *        графический контекст
	 */
	public void drawNodes(final Graphics g, final Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException {
		final long f = System.currentTimeMillis();
		final boolean showNodes = MapPropertiesManager.isShowPhysicalNodes();
		for (final AbstractNode curNode : this.getMapView().getAllNodes()) {
			if (curNode instanceof TopologicalNode) {
				if (showNodes) {
					this.getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
				}
			} else {
				this.getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
			}
		}
		for (final AbstractNode curNode : this.getMapView().getMap().getExternalNodes()) {
			this.getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
		}
		/*
		 * e = getMapView().getMarkers().iterator(); while (e.hasNext()) { MapMarker
		 * marker = (MapMarker)e.next(); marker.paint(pg, visibleBounds); }
		 */
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawNodes | " + String.valueOf(d - f) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Отрисовать временние линии. - прямоугольник выбора объектов, - вновь
	 * проводимую линию - прямоугольник масштабирования
	 * 
	 * @param g
	 *        графический контекст
	 */
	public void drawTempLines(final Graphics g, final Rectangle2D.Double visibleBounds) {
		final long f = System.currentTimeMillis();
		final Graphics2D p = (Graphics2D) g;
		final int startX = getStartPoint().x;
		final int startY = getStartPoint().y;
		final int endX = getEndPoint().x;
		final int endY = getEndPoint().y;

		p.setStroke(new BasicStroke(3));

		switch (this.mapState.getActionMode()) {
			case MapState.SELECT_MARKER_ACTION_MODE:
				p.setColor(Color.BLUE);
				p.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
				break;
			case MapState.DRAW_LINES_ACTION_MODE:
				p.setColor(Color.RED);
				p.drawLine(startX, startY, endX, endY);
				break;
			default:
				break;
		}

		// То рисовать прямоугольник
		if (this.mapState.getOperationMode() == MapState.ZOOM_TO_RECT) {
			p.setColor(Color.YELLOW);
			p.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY));
		} else if (this.mapState.getOperationMode() == MapState.MEASURE_DISTANCE) {
			p.setColor(Color.GREEN);
			p.drawLine(startX, startY, endX, endY);
		}
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawTempLines | " + String.valueOf(d - f) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Отрисовать выделенные элементы.
	 * 
	 * @param g
	 *        графический контекст
	 */
	public void drawSelection(final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		final long f = System.currentTimeMillis();
		for (final MapElement el : this.getMapView().getMap().getSelectedElements()) {
			this.getMapViewController().getController(el).paint(el, g, visibleBounds);
		}
		final long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawSelection | " + String.valueOf(d - f) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Отправить событие карты.
	 */
	public void sendScaleEvent(final Double scale) {
		if (this.getContext() != null) {
			if (this.getContext().getDispatcher() != null) {
				this.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_SCALE_CHANGED, scale));
			}
		}
	}

	/**
	 * Отправить событие карты.
	 */
	public void sendMapEvent(final String eventString) {
		if (this.getContext() != null) {
			if (this.getContext().getDispatcher() != null) {
				this.getContext().getDispatcher().firePropertyChange(new MapEvent(this, eventString));
			}
		}
	}

	public void sendSelectionChangeEvent() {
		if (this.getContext() != null) {
			if (this.getContext().getDispatcher() != null) {
				this.getContext().getDispatcher().firePropertyChange(new MapEvent(this,
						MapEvent.SELECTION_CHANGED,
						this.mapView.getMap().getSelectedElements()));
			}
		}
	}

	/**
	 * Получить текущий фиксированный элемент. Используется в режиме
	 * {@link MapState#MOVE_FIXDIST}.
	 * 
	 * @return текущий фиксированный элемент
	 */
	public AbstractNode getFixedNode() {
		return this.fixedNode;
	}

	/**
	 * Получить Список узлов, соседних (через фрагменты) с фиксированным узлом.
	 * Используется в режиме {@link MapState#MOVE_FIXDIST}.
	 * 
	 * @return список элементов.
	 */
	public List getFixedNodeList() {
		return this.fixedNodeList;
	}

	/**
	 * Получить текущий выбранный элемент.
	 * 
	 * @return текущий выбранный элемент
	 */
	public MapElement getCurrentMapElement() {
		return this.currentMapElement;
	}

	/**
	 * Установить текущий выбранный элемент.
	 * 
	 * @param curMapElement
	 *        текущий выбранный элемент
	 */
	public void setCurrentMapElement(final MapElement curMapElement) {
		Log.debugMessage(this.getClass().getName() + "::" + "setCurrentMapElement(" + curMapElement + ")" + " | " + "method call", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				Level.FINEST);

		this.currentMapElement = curMapElement;

		if (this.getMapState().getOperationMode() == MapState.NO_OPERATION) {
			final boolean canFixDist = (curMapElement instanceof TopologicalNode) || (curMapElement instanceof SiteNode);
			if (this.getContext().getApplicationModel().isEnabled(MapApplicationModel.OPERATION_MOVE_FIXED) != canFixDist) {
				if (canFixDist) {
					this.fixedNode = (AbstractNode) curMapElement;
					this.fixedNodeList.clear();
					for (final NodeLink mnle : this.mapView.getNodeLinks(this.fixedNode)) {
						this.fixedNodeList.add(mnle.getOtherNode(this.fixedNode));
					}
				}
				this.getContext().getApplicationModel().setEnabled(MapApplicationModel.OPERATION_MOVE_FIXED, canFixDist);
				this.getContext().getApplicationModel().fireModelChanged();
			}
		}

		if (curMapElement instanceof VoidElement) {
			return;
		}
		if (!(curMapElement instanceof Selection)) {
			this.mapView.getMap().setSelected(curMapElement, true);
		}
	}

	/**
	 * Получить текущий элемент по экранной коордитате на карте.
	 * 
	 * @param point
	 *        экранная координата
	 * @return элемент в точке
	 */
	public MapElement getVisibleMapElementAtPoint(
			final Point point, 
			final Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		Log.debugMessage(this.getClass().getName() + "::" + "getMapElementAtPoint(" + point + ")" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		final int showMode = this.getMapState().getShowMode();
		MapElement curME = VoidElement.getInstance(this.mapView);
		for(final AbstractNode abstractNode : this.getVisibleNodes(visibleBounds)) {
			boolean visible = true;
			if(abstractNode instanceof TopologicalNode) {
				visible = MapPropertiesManager.isShowPhysicalNodes();
			} else if(abstractNode instanceof SiteNode) {
				visible = MapPropertiesManager.isLayerVisible(((SiteNode)abstractNode).getType());
			}
			if (visible && this.mapViewController.isMouseOnElement(abstractNode, point)) {
				return abstractNode;
			}
		}

		// Здесь пробегаемся по всем элементам и если на каком-нибудь из них
		// курсор
		// то устанавливаем его текущим элементом
		for(final NodeLink nodeLink : this.getVisibleNodeLinks(visibleBounds)) {
			if(this.mapViewController.isMouseOnElement(nodeLink, point)) {
				switch(showMode) {
					case MapState.SHOW_NODE_LINK:
						return nodeLink;
					case MapState.SHOW_PHYSICAL_LINK:
						return nodeLink.getPhysicalLink();
					case MapState.SHOW_CABLE_PATH: {
						PhysicalLink physicalLink = nodeLink.getPhysicalLink();
						if(physicalLink instanceof UnboundLink) {
							return ((UnboundLink)physicalLink).getCablePath();
						}
						List<Object> bindObjects = physicalLink.getBinding()
								.getBindObjects();
						if(bindObjects.isEmpty()) {
							curME = physicalLink;
						} else {
							return (MapElement)bindObjects.iterator().next();
						}
						break;
					}
					case MapState.SHOW_MEASUREMENT_PATH: {
						PhysicalLink physicalLink = nodeLink.getPhysicalLink();
						curME = physicalLink;
						for(Object boundObject : physicalLink.getBinding().getBindObjects()) {
							CablePath cablePath = (CablePath) boundObject;
							curME = cablePath;
							boolean doBreak = false;
							LinkedIdsCondition condition = new LinkedIdsCondition(
									cablePath.getSchemeCableLink().getId(),
									ObjectEntities.PATHELEMENT_CODE);
							try {
								Set<PathElement> pathElements = StorableObjectPool
										.<PathElement> getStorableObjectsByCondition(
												condition,
												true);
								for(PathElement pathElement : pathElements) {
									SchemePath schemePath = pathElement
											.getParentPathOwner();
									MeasurementPath measurementPath = this.mapView
											.findMeasurementPath(schemePath);
									if(measurementPath != null) {
										curME = measurementPath;
										doBreak = true;
										break;
									}
								}
							} catch(ApplicationException e) {
								e.printStackTrace();
							}
							if(doBreak) {
								return curME;
							}
						}
						break;
					}
					default:
						throw new UnsupportedOperationException(
								"Unknown show mode: " + showMode); //$NON-NLS-1$
				}
//				break;
			}
		}
		return curME;
	}

	/**
	 * Получить текущий элемент по экранной коордитате на карте.
	 * 
	 * @param point
	 *        экранная координата
	 * @return элемент в точке
	 */
	public MapElement getMapElementAtPoint(
			final Point point, 
			final Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		Log.debugMessage(this.getClass().getName() + "::" + "getMapElementAtPoint(" + point + ")" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		final int showMode = this.getMapState().getShowMode();
		MapElement curME = VoidElement.getInstance(this.mapView);
		for(final AbstractNode abstractNode : this.getVisibleNodes(visibleBounds)) {
			if (this.mapViewController.isMouseOnElement(abstractNode, point)) {
				return abstractNode;
			}
		}

		// Здесь пробегаемся по всем элементам и если на каком-нибудь из них
		// курсор
		// то устанавливаем его текущим элементом
		for(final NodeLink nodeLink : this.getVisibleNodeLinks(visibleBounds)) {
			if(this.mapViewController.isMouseOnElement(nodeLink, point)) {
				switch(showMode) {
					case MapState.SHOW_NODE_LINK:
						return nodeLink;
					case MapState.SHOW_PHYSICAL_LINK:
						return nodeLink.getPhysicalLink();
					case MapState.SHOW_CABLE_PATH: {
						PhysicalLink physicalLink = nodeLink.getPhysicalLink();
						if(physicalLink instanceof UnboundLink) {
							return ((UnboundLink)physicalLink).getCablePath();
						}
						List<Object> bindObjects = physicalLink.getBinding()
								.getBindObjects();
						if(bindObjects.isEmpty()) {
							curME = physicalLink;
						} else {
							return (MapElement)bindObjects.iterator().next();
						}
						break;
					}
					case MapState.SHOW_MEASUREMENT_PATH: {
						PhysicalLink physicalLink = nodeLink.getPhysicalLink();
						curME = physicalLink;
						for(Object boundObject : physicalLink.getBinding().getBindObjects()) {
							CablePath cablePath = (CablePath) boundObject;
							curME = cablePath;
							boolean doBreak = false;
							LinkedIdsCondition condition = new LinkedIdsCondition(
									cablePath.getSchemeCableLink().getId(),
									ObjectEntities.PATHELEMENT_CODE);
							try {
								Set<PathElement> pathElements = StorableObjectPool
										.<PathElement> getStorableObjectsByCondition(
												condition,
												true);
								for(PathElement pathElement : pathElements) {
									SchemePath schemePath = pathElement
											.getParentPathOwner();
									MeasurementPath measurementPath = this.mapView
											.findMeasurementPath(schemePath);
									if(measurementPath != null) {
										curME = measurementPath;
										doBreak = true;
										break;
									}
								}
							} catch(ApplicationException e) {
								e.printStackTrace();
							}
							if(doBreak) {
								return curME;
							}
						}
						break;
					}
					default:
						throw new UnsupportedOperationException(
								"Unknown show mode: " + showMode); //$NON-NLS-1$
				}
//				break;
			}
		}
		return curME;
	}

	/**
	 * Отменить выбор всем элементам.
	 */
	public void deselectAll() {
		Log.debugMessage(this.getClass().getName() + "::" + "deselectAll()" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		final Collection<MapElement> selectedElements = new LinkedList<MapElement>();
		selectedElements.addAll(this.mapView.getMap().getSelectedElements());
		for (final MapElement mapElement : selectedElements) {
			mapElement.setSelected(false);
		}
		this.mapView.getMap().clearSelection();
	}

	/**
	 * Выбрать все элеметны.
	 */
//	public void selectAll() {
//		Log.debugMessage(this.getClass().getName() + "::" + "selectAll()" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//
//		Map map = this.mapView.getMap();
//		for (final MapElement curElement : this.mapView.getAllElements()) {
//			map.setSelected(curElement, true);
//		}
//	}

	/**
	 * Получить список выбранных элементов.
	 * 
	 * @return список элементов
	 */
	public Set<MapElement> getSelectedElements() {
		return this.mapView.getMap().getSelectedElements();
	}

	/**
	 * Проверка того, что хотя бы один элемент выбран.
	 * 
	 * @return <code>true</code> если есть выбранный элемент
	 */
	public boolean isSelectionEmpty() {
		return this.getMapView().getMap().getSelectedElements().isEmpty();
	}

	/**
	 * Получить фрагмент линии, для которого булет произволиться редактирование
	 * длины, по коордитате на карте.
	 * 
	 * @param point
	 *        экранная координата
	 * @param visibleBounds 
	 * @return фрагмент линии, или <code>null</code>, если фрагмента в данной
	 *         точке нет
	 */
	public NodeLink getEditedNodeLink(final Point point, Rectangle2D.Double visibleBounds) {
		Log.debugMessage(this.getClass().getName() + "::" + "getEditedNodeLink(" + point + ")" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		NodeLinkController nlc = null;
		try {
			for (final NodeLink mapElement : this.getVisibleNodeLinks(visibleBounds)) {
				if (nlc == null) {
					nlc = (NodeLinkController) this.getMapViewController().getController(mapElement);
				}
				if (nlc.isMouseOnThisObjectsLabel(mapElement, point)) {
					return mapElement;
				}
			}
		} catch(MapException e) {
			Log.debugException(e, Level.WARNING);
		}
		return null;
	}

	/**
	 * Установить новую длину фрагмента линии - расстояние от узла node до другого
	 * узла.
	 * 
	 * @param nodelink
	 *        фрагмент линии
	 * @param node
	 *        узел, от которого откладывается расстояние
	 * @param dist
	 *        расстояние
	 */
	public void setNodeLinkSizeFrom(final NodeLink nodelink, final AbstractNode node, final double dist)
			throws MapConnectionException,
				MapDataException {
		final DoublePoint anchor1 = node.getLocation();

		final MoveNodeCommand cmd = new MoveNodeCommand(node);
		cmd.setLogicalNetLayer(this);

		((NodeLinkController) this.getMapViewController().getController(nodelink)).setSizeFrom(nodelink, node, dist);

		final DoublePoint anchor2 = node.getLocation();
		cmd.setParameter(MoveSelectionCommandBundle.DELTA_X, String.valueOf(anchor2.getX() - anchor1.getX()));
		cmd.setParameter(MoveSelectionCommandBundle.DELTA_Y, String.valueOf(anchor2.getY() - anchor1.getY()));
		this.getCommandList().add(cmd);
		this.getCommandList().execute();

		this.getMapState().setOperationMode(MapState.NO_OPERATION);
		sendMapEvent(MapEvent.MAP_CHANGED);
	}

	/**
	 * Отменить последнюю выполненную команду пользователя.
	 */
	public void undo() {
		this.commandList.undo();
		this.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	/**
	 * Повторить отмененную команду прользователя.
	 */
	public void redo() {
		this.commandList.redo();
		this.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	/**
	 * Получить выбранный тип для создания новых линий.
	 * 
	 * @return тип линии
	 * @throws ApplicationException
	 */
	public PhysicalLinkType getCurrentPhysicalLinkType() throws ApplicationException {
		if (this.currentPhysicalLinkType == null) {
			this.currentPhysicalLinkType = LinkTypeController.getDefaultPhysicalLinkType();
		}
		return this.currentPhysicalLinkType;
	}

	/**
	 * Получить тип непривязанного узла.
	 * 
	 * @return тип узла
	 * @throws ApplicationException
	 */
	public SiteNodeType getUnboundNodeType() throws ApplicationException {
		if (this.unboundNodeType == null) {
			this.unboundNodeType = NodeTypeController.getUnboundNodeType();
		}
		return this.unboundNodeType;
	}

	/**
	 * Получить тип непривязанного кабеля.
	 * 
	 * @return тип непривязанного кабеля
	 * @throws ApplicationException
	 */
	public PhysicalLinkType getUnboundLinkType() throws ApplicationException {
		if (this.unboundLinkType == null) {
			this.unboundLinkType = LinkTypeController.getUnboundPhysicalLinkType();
		}
		return this.unboundLinkType;
	}

	/**
	 * Выбрать тип линии для создания новых линий.
	 * 
	 * @param type
	 *        тип линии
	 */
	public void setCurrentPhysicalLinkType(final PhysicalLinkType type) {
		this.currentPhysicalLinkType = type;
	}

	/**
	 * получить контроллер вида.
	 * 
	 * @return контроллер вида
	 */
	public MapViewController getMapViewController() {
		return this.mapViewController;
	}

	public void setMapViewController(final MapViewController mapViewController) {
		this.mapViewController = mapViewController;
	}

	/**
	 * @return Returns the converter.
	 */
	public MapCoordinatesConverter getConverter() {
		return this.converter;
	}

	/**
	 * @return Returns the mapContext.
	 */
	public MapContext getMapContext() {
		return this.mapContext;
	}

	/**
	 * Объект, замещающий при отображении несколько NodeLink'ов
	 * 
	 * @author $Author: krupenn $
	 * @version $Revision: 1.133 $, $Date: 2005/10/25 08:02:45 $
	 * @module mapviewclient_modifying
	 */
	private class VisualMapElement {
		public AbstractNode startNode = null;
		public AbstractNode endNode = null;
		public Color color = null;
		public Stroke stroke = null;
		public MapElement mapElement = null;

		public VisualMapElement(final AbstractNode startNode, final AbstractNode endNode) {
			this.startNode = startNode;
			this.endNode = endNode;
		}

		public VisualMapElement(final AbstractNode startNode, final AbstractNode endNode, final Color color, final Stroke stroke) {
			this.startNode = startNode;
			this.endNode = endNode;
			this.color = color;
			this.stroke = stroke;
		}

		public VisualMapElement(final MapElement mapElement, final Color color, final Stroke stroke) {
			this.mapElement = mapElement;
			this.color = color;
			this.stroke = stroke;
		}

		public VisualMapElement(final MapElement mapElement) {
			this.mapElement = mapElement;
		}
	}

	/**
	 * Список VisualMapElement'ов, которые будут отображаться при текущем масштабе
	 */
	private Set<Object> visualElements = new HashSet<Object>();

	/**
	 * Линки с экранной длиной меньшей, этого параметра сливаются с соседними в
	 * один общий отображаемый элемент
	 */
	private static final int MINIMUM_SCREEN_LENGTH = 20;

	private java.util.Map<AbstractNode, Set<NodeLink>> linksForNodes = new HashMap<AbstractNode, Set<NodeLink>>();

	/**
	 * Обновляет список элементов которые должны отображаться при текущем
	 * массштабе.
	 * @throws ApplicationException 
	 */
	public void calculateVisualElements() throws MapDataException, MapConnectionException, ApplicationException {

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();
		MapViewController.nullTime6();
		MapViewController.nullTime7();
		MapViewController.nullTime8();

		final long startTime = System.currentTimeMillis();
		this.visualElements.clear();
		final long t1 = System.currentTimeMillis();

		final Map map = this.getMapView().getMap();

		final Set<NodeLink> allNodeLinks = getVisibleNodeLinksForOptimization(null);
		final Set<AbstractNode> allNodes = getVisibleNodesForOptimization(null);
		final long t2 = System.currentTimeMillis();

		if (this.getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH) {
			for (final MeasurementPath measurementPath : this.mapView.getMeasurementPaths()) {
				final Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				final Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				this.fillOptimizationSets(measurementPath, allNodes, allNodeLinks, nodes, nodeLinks);

				final MeasurementPathController controller = (MeasurementPathController) this.getMapViewController().getController(measurementPath);
				this.calculateVisualElements(nodes,
						nodeLinks,
						controller.getColor(measurementPath.getCharacterizable()),
						controller.getStroke(measurementPath.getCharacterizable()),
						false);
			}
			this.calculateVisualElements(this.mapView.getAllNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		} else if (this.getMapState().getShowMode() == MapState.SHOW_CABLE_PATH) {
			for (final CablePath cablePath : this.mapView.getCablePaths()) {
				final Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				final Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				this.fillOptimizationSets(cablePath, allNodes, allNodeLinks, nodes, nodeLinks);

				final CableController controller = (CableController) this.getMapViewController().getController(cablePath);
				this.calculateVisualElements(
						nodes, 
						nodeLinks, 
						controller.getColor(cablePath.getCharacterizable()), 
						controller.getStroke(cablePath.getCharacterizable()), 
						false);
			}
			this.calculateVisualElements(this.mapView.getAllNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		} else if (this.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK) {
			this.calculateVisualElements(this.mapView.getAllNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		} else if (this.getMapState().getShowMode() == MapState.SHOW_NODE_LINK) {
			this.calculateVisualElements(this.mapView.getAllNodes(),
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		
		this.visualElements.addAll(this.getMapView().getMarkers());
//		for(AbstractNode node : allNodes) {
//			this.visualElements.add(node);
//		}
		final long endTime = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.calculateVisualLinks | " //$NON-NLS-1$
				+ "optimized map for " + (endTime - startTime) + "ms. Got " + this.visualElements.size() + " visual links.\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "		" + (t1 - startTime) + " ms (visualElements.clear())\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "		" + (t2 - t1) + " ms (getNodeLinks)\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "		" + (endTime - t2) + " ms (recursing) divided to:\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "			" + MapViewController.getTime1() + " ms (fillOptimizationSets)\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "			" + MapViewController.getTime2() + " ms (searchLinksForNodes)\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "			" + MapViewController.getTime3() + " ms (fill Calculated maps)\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "			" + MapViewController.getTime4() + " ms (create VisualElements)\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "			" + MapViewController.getTime6() + " ns (getCharacteristics)\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "			" + MapViewController.getTime5() + " ms (calculate distance)\n", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void fillOptimizationSets(final CablePath cablePath,
			final Set<AbstractNode> allNodes, 
			final Set<NodeLink> allNodeLinks,
			final Set<AbstractNode> nodes,
			final Set<NodeLink> nodeLinks) throws ApplicationException {

		final long t1 = System.currentTimeMillis();
		for (final PhysicalLink physicalLink : cablePath.getLinks()) {
			for (final NodeLink nodeLink : physicalLink.getNodeLinks()) {
				nodes.add(nodeLink.getStartNode());
				nodes.add(nodeLink.getEndNode());
				nodeLinks.add(nodeLink);
				allNodeLinks.remove(nodeLink);
				allNodes.remove(nodeLink.getStartNode());
				allNodes.remove(nodeLink.getEndNode());
			}
		}
		final long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	private void fillOptimizationSets(final MeasurementPath measurementPath,
			final Set<AbstractNode> allNodes, 
			final Set<NodeLink> allNodeLinks,
			final Set<AbstractNode> nodes,
			final Set<NodeLink> nodeLinks) throws ApplicationException {

		final long t1 = System.currentTimeMillis();
		for (final CablePath cablePath : measurementPath.getSortedCablePaths()) {
			for (final PhysicalLink physicalLink : cablePath.getLinks()) {
				for (final NodeLink nodeLink : physicalLink.getNodeLinks()) {
					nodes.add(nodeLink.getStartNode());
					nodes.add(nodeLink.getEndNode());
					nodeLinks.add(nodeLink);
					allNodeLinks.remove(nodeLink);
					allNodes.remove(nodeLink.getStartNode());
					allNodes.remove(nodeLink.getEndNode());
				}
			}
		}
		final long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	public void calculateVisualElements(final Set<AbstractNode> nodes,
			final Set<NodeLink> nodeLinks,
			final Color color,
			final Stroke stroke,
			final boolean pullAttributesFromController) throws MapDataException, MapConnectionException {

		this.searchLinksForNodes(nodeLinks);

		final long t1 = System.currentTimeMillis();
		// Таблица, в которой содержится информация о том, обрабатывался ли
		// уже узел или нет
		final java.util.Map<AbstractNode, Boolean> nodesCalculated = new HashMap<AbstractNode, Boolean>(nodes.size());
		for (final AbstractNode node : nodes) {
			nodesCalculated.put(node, Boolean.FALSE);
		}

		final java.util.Map<NodeLink, Boolean> nodeLinksCalculated = new HashMap<NodeLink, Boolean>(nodeLinks.size());
		for (final NodeLink nodeLink : nodeLinks) {
			nodeLinksCalculated.put(nodeLink, Boolean.FALSE);
		}
		final long t2 = System.currentTimeMillis();
		MapViewController.addTime3(t2 - t1);

		// Запускаем рекурсию.
		// Если очередной затравочный узел уже обработан - рекурсию с него не
		// запускаем.
		// Если карта - это связный граф, то рекурсия будет запущена один раз.
		final Collection<AbstractNode> frontedge = new LinkedList<AbstractNode>();
		for (final Iterator<AbstractNode> nodesIt = nodes.iterator(); nodesIt.hasNext();) {
			AbstractNode startRecursionNode = nodesIt.next();

			while (startRecursionNode != null) {
				if (!nodesCalculated.get(startRecursionNode).booleanValue()) {
					this.pullVisualLinksFromNode(startRecursionNode,
							startRecursionNode,
							null,
							nodesCalculated,
							nodeLinksCalculated,
							frontedge,
							color,
							stroke,
							pullAttributesFromController);
				}
				final Iterator<AbstractNode> it = frontedge.iterator();
				if (it.hasNext()) {
					startRecursionNode = it.next();
					it.remove();
				} else {
					startRecursionNode = null;
				}
			}
		}
	}

	/**
	 * Рекурсивная процедура обхода сегмента схемы карты, которому принадлежит
	 * "затравочный" узел.
	 * 
	 * @param nodeToPullFrom
	 *        Узел, от которого тянется текущий VisualMapElement.
	 * @param lastNode
	 *        Предыдущий узел - от него мы пришли к данному узлу
	 * @param incomingLink
	 *        Линк, по которому мы пришли в данный узел. Для первого уровня
	 *        рекурсии = null.
	 * @param nodesCalculated
	 *        Таблица содержащая информацию о том, обработан ли тот или иной узел.
	 * @param nodeLinksCalculated
	 * @param frontedge
	 * @param stroke
	 * @param color
	 */
	private void pullVisualLinksFromNode(final AbstractNode nodeToPullFrom,
			final AbstractNode lastNode,
			final NodeLink incomingLink,
			final java.util.Map<AbstractNode, Boolean> nodesCalculated,
			final java.util.Map<NodeLink, Boolean> nodeLinksCalculated,
			final Collection<AbstractNode> frontedge,
			final Color color,
			final Stroke stroke,
			final boolean pullAttributesFromController) throws MapDataException, MapConnectionException {
		// Рассматриваемый узел
		AbstractNode nodeProcessed = null;

		if (incomingLink == null) {
			nodeProcessed = lastNode;
		}
		else {
			nodeProcessed = incomingLink.getOtherNode(lastNode);
		}

		if(nodeProcessed == null){
			int a = 0;
			nodeProcessed = incomingLink.getOtherNode(lastNode);
		}

		assert nodeProcessed != null : "LogicalNetLayer | pullVisualLinksFromNode | The nodeProcessed can't be null"; //$NON-NLS-1$

		//Получаем список всех входящих/исходящих линий для данного узла
		final Set<NodeLink> allLinksForNodeProcessed = this.linksForNodes.get(nodeProcessed);

		boolean hasUncalculatedLinks = false;
		if (allLinksForNodeProcessed != null) {
			for (final NodeLink outgoingLink : allLinksForNodeProcessed) {
				// Исключаем из списка линк, по которому мы пришли в данный узел
				if (!(outgoingLink == incomingLink) && !nodeLinksCalculated.get(outgoingLink).booleanValue()) {
					hasUncalculatedLinks = true;
					break;
				}
			}
		}

		if (allLinksForNodeProcessed == null || allLinksForNodeProcessed.size() == 0) {
			// Это одиночный узел (без связей). Он отображается всегда.
			this.visualElements.add(nodeProcessed);
			// Отмечаем в таблице, что узел обработан.
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
			return;
		}

		if (nodesCalculated.get(nodeProcessed).booleanValue()
				|| (allLinksForNodeProcessed.size() == 1 && allLinksForNodeProcessed.contains(incomingLink))
				|| !hasUncalculatedLinks) {
			// Первое условие останова - данный узел уже рассматривался или
			//является конечным в цепочке или все остальный линии, выходящие 
			//из него, уже рассмотрены.
			//Создаём элемент отображения и выходим.

			//Отмечаем в таблице, что узел обработан - для случая конечного
			//узла в цепочке.			
			nodesCalculated.put(lastNode, Boolean.TRUE);
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);

			VisualMapElement visualMapElement;
			final long t1 = System.currentTimeMillis();
			if (lastNode == nodeToPullFrom) {
				// Если последний "натягиваемый" отображаемый элемент состоит из одного
				// линка - его и отображаем
				if (pullAttributesFromController) {
					visualMapElement = new VisualMapElement(incomingLink);
//					final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(incomingLink);
//					visualMapElement = new VisualMapElement(incomingLink,
//							controller.getColor(incomingLink),
//							controller.getStroke(incomingLink));
				} else {
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				}
			} else {
				visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
			}
			final long t2 = System.currentTimeMillis();
			MapViewController.addTime4(t2 - t1);

			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			return;
		}

		final long t5 = System.currentTimeMillis();
		// Экранные координаты узла, от которого мы тянем визуальный элемент
		final Point startVENodeScr = this.converter.convertMapToScreen(nodeToPullFrom.getLocation());
		// Экранные координаты текущего узла
		final Point currEndVENodeScr = this.converter.convertMapToScreen(nodeProcessed.getLocation());
		final double distance = Math.sqrt((currEndVENodeScr.x - startVENodeScr.x)
				* (currEndVENodeScr.x - startVENodeScr.x)
				+ (currEndVENodeScr.y - startVENodeScr.y)
				* (currEndVENodeScr.y - startVENodeScr.y));
		final long t6 = System.currentTimeMillis();
		MapViewController.addTime5(t6 - t5);

		if (distance >= MINIMUM_SCREEN_LENGTH) {
			// Второе условие останова - получили цепочку длиннее критической
			// длины - рекурсия останавливается, но ПРЕДПОСЛЕДНИЙ
			// узел цепочки ставится во фронт волны.

			VisualMapElement visualMapElement;
			AbstractNode lastVMENode;
			if (lastNode == nodeToPullFrom) {
				final long t1 = System.currentTimeMillis();
				// Если "натягиваемый" отображаемый элемент состоит из одного
				// линка - его и отображаем
				if (pullAttributesFromController) {
					visualMapElement = new VisualMapElement(incomingLink);
//					final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(incomingLink);
//					visualMapElement = new VisualMapElement(incomingLink,
//							controller.getColor(incomingLink),
//							controller.getStroke(incomingLink));
				} else {
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				}
				final long t2 = System.currentTimeMillis();
				MapViewController.addTime4(t2 - t1);
				lastVMENode = nodeProcessed;
				nodesCalculated.put(lastNode, Boolean.TRUE);
				nodeLinksCalculated.put(incomingLink, Boolean.TRUE);
			}
			else {
				//В обратном случае - создаём отображаемый элемент до
				//предыдущего узла

				//Если в основе этого элемента отображения таки один элемент - на основе его и создаём
				NodeLink linkBetween = null;
				for (final NodeLink outLink : this.linksForNodes.get(nodeToPullFrom)) {
					if (outLink.getStartNode() == lastNode || outLink.getEndNode() == lastNode) {
						linkBetween = outLink;
						break;
					}
				}

				final long t1 = System.currentTimeMillis();
				if (linkBetween != null) {
					if (pullAttributesFromController) {
						visualMapElement = new VisualMapElement(linkBetween);
//						final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(linkBetween);
//						visualMapElement = new VisualMapElement(linkBetween,
//								controller.getColor(linkBetween),
//								controller.getStroke(linkBetween));
					} else {
						visualMapElement = new VisualMapElement(linkBetween, color, stroke);
					}
				} else {
					visualMapElement = new VisualMapElement(nodeToPullFrom, lastNode, color, stroke);
				}
				final long t2 = System.currentTimeMillis();
				MapViewController.addTime4(t2 - t1);
				lastVMENode = lastNode;
			}

			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(lastVMENode);

			frontedge.add(lastVMENode);
			return;
		}

		// Определяем тип - если мы имеем дело с SiteNode
		SiteNodeType nodeType = null;
		if (nodeProcessed instanceof SiteNode) {
			final SiteNode sNode = (SiteNode) nodeProcessed;
			nodeType = sNode.getType();
		}

		if ((incomingLink != null)
				&& ((allLinksForNodeProcessed.size() > 2) || ((nodeType != null) && (nodeType.getSort().value() == SiteNodeTypeSort._BUILDING
						|| nodeType.getSort().value() == SiteNodeTypeSort._UNBOUND || nodeType.getSort().value() == SiteNodeTypeSort._ATS)))) {
			// Третье условие останова - наткнулись на узел с развилкой или
			// узел типа, не подлежащего оптимизации, - рекурсия останавливается,
			// но ПОСЛЕДНИЙ узел цепочки ставится во фронт волны.


			VisualMapElement visualMapElement;
			final long t1 = System.currentTimeMillis();
			if (lastNode == nodeToPullFrom) {
				// Если последний "натягиваемый" отображаемый элемент состоит из одного
				// линка - его и отображаем
				if (pullAttributesFromController) {
					visualMapElement = new VisualMapElement(incomingLink);
//					final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(incomingLink);
//					visualMapElement = new VisualMapElement(incomingLink,
//							controller.getColor(incomingLink),
//							controller.getStroke(incomingLink));
				} else {
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				}
			} else {
				visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
			}
			final long t2 = System.currentTimeMillis();
			MapViewController.addTime4(t2 - t1);

			nodesCalculated.put(lastNode, Boolean.TRUE);
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);

			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			frontedge.add(nodeProcessed);
			return;
		}

		nodesCalculated.put(lastNode, Boolean.TRUE);
		if (incomingLink != null) {
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);
		}

		//Тянем элемент отображения дальше. Случай ветвления рассмотрен выше
		//(третье условие останова). Поэтому вызов рекурсивного метода из этого
		//цикла произойдёт единожды.
		for (final NodeLink outgoingLink : allLinksForNodeProcessed) {
			// Исключаем из списка линк, по которому мы пришли в данный узел
			if (!(outgoingLink == incomingLink) && !nodeLinksCalculated.get(outgoingLink).booleanValue()) {
				this.pullVisualLinksFromNode(nodeToPullFrom,
						nodeProcessed,
						outgoingLink,
						nodesCalculated,
						nodeLinksCalculated,
						frontedge,
						color,
						stroke,
						pullAttributesFromController);
			}
		}
	}

	/**
	 * Отрисовывает элементы отображения, расположенные в заданных границах
	 * 
	 * @param g
	 * @param visibleBounds
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void drawVisualElements(final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		MapViewController.nullTime6();
		final long t1 = System.currentTimeMillis();

		// Если режим показа nodeLink не разрешён, то включам режим показа
		// physicalLink
		ApplicationModel aModel = this.aContext.getApplicationModel();
		if (this.getMapState().getShowMode() == MapState.SHOW_NODE_LINK
				&& !aModel.isEnabled(MapApplicationModel.MODE_NODE_LINK)) {
			aModel.setSelected(MapApplicationModel.MODE_NODE_LINK, false);
			aModel.setSelected(MapApplicationModel.MODE_LINK, true);
			aModel.setSelected(MapApplicationModel.MODE_CABLE_PATH, false);
			aModel.setSelected(MapApplicationModel.MODE_PATH, false);

			this.getMapState().setShowMode(MapState.SHOW_PHYSICAL_LINK);

			aModel.fireModelChanged();
		}

		for (final Object veElement : this.visualElements) {
			if (veElement instanceof SiteNode) {
				continue;
			}

			if (veElement instanceof VisualMapElement) {
				final VisualMapElement vme = (VisualMapElement) veElement;
				if (vme.mapElement != null) {
					if (vme.mapElement instanceof NodeLink) {
						final NodeLink nodeLink = (NodeLink) vme.mapElement;
						final NodeLinkController controller = (NodeLinkController) this.getMapViewController().getController(nodeLink);
						if(!controller.isElementVisible(nodeLink, visibleBounds)) {
							continue;
						}
						Color color = vme.color;
						Stroke stroke = vme.stroke;
						if (this.getMapState().getShowMode() == MapState.SHOW_NODE_LINK) {
							controller.paint(nodeLink, g, visibleBounds);
						}
						else {
							final boolean selectionVisible = false;
							if(color == null) {
								color = controller.getColor(nodeLink);
								stroke = controller.getStroke(nodeLink);
							}
							controller.paint(nodeLink, g, visibleBounds, stroke, color, selectionVisible);
						}
					}
					continue;
				}

				final DoublePoint startLocation = vme.startNode.getLocation();
				final DoublePoint endLocation = vme.endNode.getLocation();
				if (visibleBounds.intersectsLine(startLocation.getX(), startLocation.getY(), endLocation.getX(), endLocation.getY())) {
					final Point from = this.converter.convertMapToScreen(startLocation);
					final Point to = this.converter.convertMapToScreen(endLocation);
					g.setColor(vme.color);
					((Graphics2D) g).setStroke(vme.stroke);
					g.drawLine(from.x, from.y, to.x, to.y);
				}
			}
		}
		for (final Object veElement : this.visualElements) {
			if (veElement instanceof AbstractNode) {
				this.getMapViewController().getController((AbstractNode) veElement).paint((AbstractNode) veElement, g, visibleBounds);
			}
		}
		final long t2 = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawVisualElements | " + String.valueOf(t2 - t1) + " ms\n" //$NON-NLS-1$ //$NON-NLS-2$
				+ "			" + (MapViewController.getTime6() / 1000000L) + " ms (getCharacteristics)\n", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void searchLinksForNodes(final Set<NodeLink> nodeLinks) {
		final long t1 = System.currentTimeMillis();

		final Collection<Set<NodeLink>> emptySets = this.linksForNodes.values();
		for (final Set set : emptySets) {
			set.clear();
		}

		for (final NodeLink link : nodeLinks) {
			final AbstractNode startNode = link.getStartNode();
			Set<NodeLink> startNodeLinksSet = this.linksForNodes.get(startNode);
			if (startNodeLinksSet == null) {
				startNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(startNode, startNodeLinksSet);
			}
			startNodeLinksSet.add(link);

			final AbstractNode endNode = link.getEndNode();
			Set<NodeLink> endNodeLinksSet = this.linksForNodes.get(endNode);
			if (endNodeLinksSet == null) {
				endNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(endNode, endNodeLinksSet);
			}
			endNodeLinksSet.add(link);
		}

		final long t2 = System.currentTimeMillis();
		MapViewController.addTime2(t2 - t1);
	}

	private Set<AbstractNode> getVisibleNodesForOptimization(Rectangle2D.Double visibleBounds) {
		// TODO return getVisibleNodes(Double visibleBounds)
		return new HashSet<AbstractNode>(this.mapView.getAllNodes());
	}

	private Set<NodeLink> getVisibleNodeLinksForOptimization(Rectangle2D.Double visibleBounds) {
		// TODO return getVisibleNodeLinkss(Double visibleBounds)
		return new HashSet<NodeLink>(this.mapView.getAllNodeLinks());
	}

	public Set<AbstractNode> getVisibleNodes(Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException {
		Set<AbstractNode> hashSet = new HashSet<AbstractNode>();
		for(AbstractNode abstractNode : this.mapView.getAllNodes()) {
			if(this.mapViewController.isElementVisible(abstractNode, visibleBounds)) {
				hashSet.add(abstractNode);
			}
		}

		return hashSet;
	}

	public Set<NodeLink> getVisibleNodeLinks(Rectangle2D.Double visibleBounds) throws MapConnectionException, MapDataException {
		HashSet<NodeLink> hashSet = new HashSet<NodeLink>();
		Set<Identifier> physicalLinkIds = new HashSet<Identifier>();
		for(NodeLink nodeLink : this.mapView.getAllNodeLinks()) {
			if(this.mapViewController.isElementVisible(nodeLink, visibleBounds)) {
				hashSet.add(nodeLink);
				physicalLinkIds.add(nodeLink.getPhysicalLinkId());
			}
		}
		// ensure characteristics are loaded
		if(!physicalLinkIds.isEmpty()) {
			try {
				final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(
						new LinkedIdsCondition(
								physicalLinkIds, 
								ObjectEntities.CHARACTERISTIC_CODE), 
						true);
				} catch(ApplicationException e) {
				e.printStackTrace();
			}
		}

//		if(!physicalLinkIds.isEmpty()) {
//			try {
//				// m
//				final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(
//						new LinkedIdsCondition(
//								physicalLinkIds, 
//								ObjectEntities.CHARACTERISTIC_CODE), 
//						true);
//				// n
//				final Set<PhysicalLink> physicalLinks = StorableObjectPool.getStorableObjects(physicalLinkIds, true);
//				
//				final java.util.Map<Identifiable, Set<Characteristic>> fooBar = new HashMap<Identifiable, Set<Characteristic>>();
//				// n
//				for (final PhysicalLink physicalLink : physicalLinks) {
//					fooBar.put(physicalLink, null);
//				}
//				// m
//				for (final Characteristic characteristic : characteristics) {
//					final Identifier parentCharacterizableId = characteristic.getParentCharacterizableId();
//					Set<Characteristic> characteristics2 = fooBar.get(parentCharacterizableId);
//					if (characteristics2 == null) {
//						characteristics2 = new HashSet<Characteristic>();
//						fooBar.put(parentCharacterizableId, characteristics2);
//					}
//					characteristics2.add(characteristic);
//				}
//				// n
//			} catch(ApplicationException e) {
//				e.printStackTrace();
//			}
//		}
		return hashSet;
	}

}
