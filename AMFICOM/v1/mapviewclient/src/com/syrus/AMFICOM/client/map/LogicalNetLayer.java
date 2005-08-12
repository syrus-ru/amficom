/**
 * $Id: LogicalNetLayer.java,v 1.111 2005/08/12 10:41:11 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
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
import com.syrus.AMFICOM.client.map.controllers.MapElementController;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.MeasurementPathController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.CommandList;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
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
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.util.Log;

/**
 * Управляет отображением логической структуры сети.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.111 $, $Date: 2005/08/12 10:41:11 $
 * @module mapviewclient_v2
 */
public final class LogicalNetLayer
{
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
	protected List fixedNodeList = new LinkedList();

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
	protected List elementsToDisplay = new LinkedList();

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
	
	public LogicalNetLayer(ApplicationContext aContext, MapCoordinatesConverter converter, MapContext mapContext) throws ApplicationException {
		this.aContext = aContext;
		this.converter = converter;
		this.mapContext = mapContext;

		Identifier userId = LoginManager.getUserId();

		MapLibraryController.createDefaults(userId);

		AlarmMarkerController.init(userId);
		TopologicalNodeController.init(userId);
		EventMarkerController.init(userId);
		MarkController.init(userId);
		MarkerController.init(userId);
	}
	
	/**
	 * При изменении масштаба отображения карты необходимо обновить
	 * масштаб отображения всех объектов на карте.
	 * @throws MapConnectionException 
	 * @throws MapDataException 
	 */
	public void updateZoom() throws MapDataException, MapConnectionException
	{
		Log.debugMessage(getClass().getName() + "::" + "updateZoom()" + " | " + "method call", Level.FINER);
		
		if(getMapView() == null)
			return;
		Map map = getMapView().getMap();
		if(map != null)
		{
			Iterator en =  map.getNodes().iterator();
			while (en.hasNext())
			{
				AbstractNode curNode = (AbstractNode)en.next();
				((AbstractNodeController)getMapViewController().getController(curNode)).updateScaleCoefficient(curNode);
			}
		}
		if(MapPropertiesManager.isOptimizeLinks())
//			calculateVisualLinks();
			calculateVisualElements();
	}

	/**
	 * Получить контекст приложения.
	 * @return контекст приложения
	 */
	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	/**
	 * Инициализировать обозреватель заданным видом карты.
	 * @param mapView вид
	 */
	public void setMapView(MapView mapView)
		throws MapConnectionException, MapDataException
	{
		Log.debugMessage(getClass().getName() + "::" + "setMapView(" + mapView + ")" + " | " + "method call", Level.FINER);

		if(	getContext() != null
			&& getContext().getDispatcher() != null)
		{
				if(mapView != null
					&& mapView.getMap() != null)
				{
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(mapView, MapEvent.MAP_VIEW_SELECTED));
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(mapView.getMap(), MapEvent.MAP_SELECTED));
				}
				else
				{
					this.aContext.getDispatcher().firePropertyChange(
							new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_DESELECTED));
				}
		}

		this.mapView = mapView;
		this.linksForNodes.clear();

		if(mapView == null)
		{
			Log.debugMessage("mapView null!", Level.INFO);
		}

		getMapViewController().setMapView(this.mapView);

		//Поумолчанию текущий элемент Void
		this.currentMapElement = VoidElement.getInstance(this.mapView);

		this.commandList.flush();
	}

	/**
	 * получить вид.
	 * @return вид
	 */
	public MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * Получить масштаб элементов по умолчанию.
	 * @return масштаб элементов по умолчанию
	 */
	public double getDefaultScale()
	{
		return this.defaultScale;
	}
	
	/**
	 * Установить масштаб элементов по умолчанию.
	 * @param defaultScale масштаб элементов по умолчанию
	 * @throws MapConnectionException 
	 * @throws MapDataException 
	 */
	public void setDefaultScale(double defaultScale) throws MapDataException, MapConnectionException
	{
		Log.debugMessage(getClass().getName() + "::" + "setDefaultScale(" + defaultScale + ")" + " | " + "method call", Level.FINER);
		
		this.defaultScale = defaultScale;
		updateZoom();
	}
	
	/**
	 * Получить текущий масштаб точечных элементов.
	 * @return текущий масштаб точечных элементов
	 */
	public double getCurrentScale()
	{
		return this.currentScale;
	}
	
	/**
	 * Получить текущее состояние слоя карты.
	 * @return состояние слоя карты
	 */
	public MapState getMapState()
	{
		return this.mapState;
	}

	/**
	 * Установить текущее состояние слоя карты.
	 * @param state новое состояние
	 */
	public void setMapState(MapState state)
	{
		Log.debugMessage(getClass().getName() + "::" + "setMapState(" + state + ")" + " | " + "method call", Level.FINER);
		
		this.mapState = state;
	}

	/**
	 * Получить текущую экранную точку.
	 * @return текущая экранная точка
	 */
	public Point getCurrentPoint()
	{
		return this.currentPoint;
	}
	
	/**
	 * Установить текущую экранную точку.
	 * @param point текущая экранная точка
	 */
	public void setCurrentPoint(Point point)
	{
		this.currentPoint = point;
	}
	
	/**
	 * Получить запомненную начальную экранную точку.
	 * @return начальная экранная точка
	 */
	public Point getStartPoint()
	{
		return new Point(this.startPoint);
	}

	/**
	 * Установить начальную экранную точку.
	 * @param point начальная экранная точка
	 */
	public void setStartPoint(Point point)
	{
		this.startPoint = new Point(point);
	}

	/**
	 * Получить запомненную конечную экранную точку.
	 * @return конечная экранная точка
	 */
	public Point getEndPoint()
	{
		return new Point(this.endPoint);
	}

	/**
	 * Установить конечную экранную точку.
	 * @param point конечная экранная точка
	 */
	public void setEndPoint(Point point)
	{
		this.endPoint = new Point(point);
	}
	
	/**
	 * Получить список команд, исполняемых на слое.
	 * @return список команд
	 */
	public CommandList getCommandList()
	{
		return this.commandList;
	}

	/**
	 * Отрисовать все элементы логического слоя топологической схемы.
	 * @param g графический контекст
	 */
	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		Graphics2D p = (Graphics2D )g;
		
		// remember settings from graphics
		Color color = p.getColor();
		Stroke stroke = p.getStroke();
		Font font = p.getFont();
		Color background = p.getBackground();

		long f;
		long d;

		Log.debugMessage("\n\n------------------ LogicalNetLayer.paint() called ----------------------", 
				Level.INFO);
		try {
			throw new Exception("stacktrace");
		}
		catch (Exception e) {
//			Log.debugException(e, Log.FINE);
//			e.printStackTrace();
		}
//		System.out.println("--------------------------------------");
		f = System.currentTimeMillis();
		if(MapPropertiesManager.isOptimizeLinks()) {
			drawVisualElements(p, visibleBounds);
		}
		else {
			drawLines(p, visibleBounds);
			drawNodes(p, visibleBounds);
		}
		drawSelection(p, visibleBounds);
		drawTempLines(p, visibleBounds);
		d = System.currentTimeMillis();
		Log.debugMessage("\n--------------------- LogicalNetLayer.paint() finished in " + String.valueOf(d - f) + " ms -----------------\n", 
				Level.INFO);
//		System.out.println("--------------------------------------");

		// revert graphics to previous settings
		p.setColor(color);
		p.setStroke(stroke);
		p.setFont(font);
		p.setBackground(background);
	}

	/**
	 * Отрисовать линейные объекты.
	 * @param g графический контекст
	 */
	public void drawLines(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		long f = System.currentTimeMillis();

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();

		this.elementsToDisplay.clear();
		
		//Если режим показа nodeLink не разрешён, то включам режим показа physicalLink
		if (! this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK))
		{
			Command modeCommand = getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		if (getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH)
		{
			this.elementsToDisplay.addAll(getMapView().getMap().getAllPhysicalLinks());
			for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();)
			{
				MeasurementPath measurementPath = 
					(MeasurementPath)it.next();

//				if(getMapViewController().getController(measurementPath).isElementVisible(measurementPath, visibleBounds))
						getMapViewController().getController(measurementPath).paint(measurementPath, g, visibleBounds);
				
				// to avoid multiple cicling through scheme elements
				// use once sorted cable links
				for(Iterator it2 = measurementPath.getSortedCablePaths().iterator(); it2.hasNext();)
				{
					CablePath cablePath = 
						(CablePath)it2.next();
					this.elementsToDisplay.removeAll(cablePath.getLinks());
				}
			}
			for(Iterator it = this.elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink physicalLink = 
					(PhysicalLink)it.next();
//				if(getMapViewController().getController(physicalLink).isElementVisible(physicalLink, visibleBounds))
					getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			this.elementsToDisplay.addAll(getMapView().getMap().getAllPhysicalLinks());
			for(Iterator it = this.mapView.getCablePaths().iterator(); it.hasNext();)
			{
				CablePath cablePath = 
					(CablePath)it.next();
//				if(getMapViewController().getController(cablePath).isElementVisible(cablePath, visibleBounds))
					getMapViewController().getController(cablePath).paint(cablePath, g, visibleBounds);
				this.elementsToDisplay.removeAll(cablePath.getLinks());
			}
			for(Iterator it = this.elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink physicalLink = 
					(PhysicalLink)it.next();
//				if(getMapViewController().getController(physicalLink).isElementVisible(physicalLink, visibleBounds))
					getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			Iterator iterator = getMapView().getMap().getAllPhysicalLinks().iterator();
			while (iterator.hasNext())
			{
				PhysicalLink physicalLink = 
					(PhysicalLink)iterator.next();
//				if(getMapViewController().getController(physicalLink).isElementVisible(physicalLink, visibleBounds))
					getMapViewController().getController(physicalLink).paint(physicalLink, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			Iterator iterator = getMapView().getMap().getAllNodeLinks().iterator();
			while (iterator.hasNext())
			{
				NodeLink nodeLink = (NodeLink)iterator.next();
//				if(getMapViewController().getController(nodeLink).isElementVisible(nodeLink, visibleBounds))
					getMapViewController().getController(nodeLink).paint(nodeLink, g, visibleBounds);
			}
		}
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawLines | " + String.valueOf(d - f) + " ms\n" 
				+ "		" + String.valueOf(MapViewController.getTime1()) + " ms (isElementVisible)\n"
				+ "		" + String.valueOf(MapViewController.getTime2()) + " ms (getStroke)\n"
				+ "		" + String.valueOf(MapViewController.getTime3()) + " ms (getColor)\n"
				+ "		" + String.valueOf(MapViewController.getTime4()) + " ms (paint)\n"
				+ "		" + String.valueOf(MapViewController.getTime5()) + " ms (painting labels)",
				Level.INFO);
	}

	/**
	 * Отрисовать узлы.
	 * @param g графический контекст
	 */
	public void drawNodes(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		long f = System.currentTimeMillis();
		boolean showNodes = MapPropertiesManager.isShowPhysicalNodes();
		Iterator e = getMapView().getMap().getNodes().iterator();
		while (e.hasNext())
		{
			AbstractNode curNode = (AbstractNode )e.next();
			if(curNode instanceof TopologicalNode)
			{
				if(showNodes)
				{
					getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
				}
			}
			else
				getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
		}
		e = getMapView().getMap().getExternalNodes().iterator();
		while (e.hasNext())
		{
			AbstractNode curNode = (AbstractNode )e.next();
			getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
		}
/*
		e = getMapView().getMarkers().iterator();
		while (e.hasNext())
		{
			MapMarker marker = (MapMarker)e.next();
			marker.paint(pg, visibleBounds);
		}
*/
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawNodes | " + String.valueOf(d - f) + " ms", 
				Level.INFO);
	}

	/**
	 * Отрисовать временние линии.
	 * 		- прямоугольник выбора объектов,
	 * 		- вновь проводимую линию
	 *      - прямоугольник масштабирования
	 * @param g графический контекст
	 */
	public void drawTempLines(Graphics g, Rectangle2D.Double visibleBounds)
	{
		long f = System.currentTimeMillis();
		Graphics2D p = ( Graphics2D )g;
		int startX = getStartPoint().x;
		int startY = getStartPoint().y;
		int endX = getEndPoint().x;
		int endY = getEndPoint().y;
		
		p.setStroke(new BasicStroke(3));

		switch (this.mapState.getActionMode())
		{
			case MapState.SELECT_MARKER_ACTION_MODE:
				p.setColor( Color.BLUE);
				p.drawRect(
						Math.min(startX, endX),
						Math.min(startY, endY),
						Math.abs(endX - startX),
						Math.abs(endY - startY));
				break;
			case MapState.DRAW_LINES_ACTION_MODE:
				p.setColor( Color.RED);
				p.drawLine( startX, startY, endX, endY);
				break;
			default:
				break;
		}

		//То рисовать прямоугольник
		if (this.mapState.getOperationMode() == MapState.ZOOM_TO_RECT )
		{
			p.setColor(Color.YELLOW);
			p.drawRect(
					Math.min(startX, endX),
					Math.min(startY, endY),
					Math.abs(endX - startX),
					Math.abs(endY - startY));
		}
		else
		if (this.mapState.getOperationMode() == MapState.MEASURE_DISTANCE )
		{
			p.setColor(Color.GREEN);
			p.drawLine(startX, startY, endX, endY);
		}
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawTempLines | " + String.valueOf(d - f) + " ms", 
				Level.INFO);
	}

	/**
	 * Отрисовать выделенные элементы.
	 * @param g графический контекст
	 */
	public void drawSelection(Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		long f = System.currentTimeMillis();
		Iterator e = getMapView().getMap().getSelectedElements().iterator();
		while (e.hasNext())
		{
			MapElement el = (MapElement)e.next();
			getMapViewController().getController(el).paint(el, g, visibleBounds);
		}
		long d = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawSelection | " + String.valueOf(d - f) + " ms", 
				Level.INFO);
	}

	/**
	 * Отправить событие карты.
	 */
	public void sendScaleEvent(Double scale)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_SCALE_CHANGED, scale));
		}
	}

	/**
	 * Отправить событие карты.
	 */
	public void sendMapEvent(String eventString)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			getContext().getDispatcher().firePropertyChange(new MapEvent(this, eventString));
		}
	}
	
	public void sendSelectionChangeEvent() {
		if(getContext() != null)
			if(getContext().getDispatcher() != null) {
			getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.SELECTION_CHANGED, this.mapView.getMap().getSelectedElements()));
		}
	}

	/**
	 * Генерация сообщеия о выборке элемента карты.
	 * @param mapElement выбранный элемент карты
	 */
	public void notifySchemeEvent(MapElement mapElement)
	{
		Log.debugMessage(getClass().getName() + "::" + "notifySchemeEvent(" + mapElement + ")" + " | " + "method call", Level.FINER);
/*		
		SchemeNavigateEvent sne;
		Dispatcher dispatcher = logicalNetLayer.mapMainFrame.this.aContext.getDispatcher();
		try 
		{
			MapSiteNodeElement mapel = (MapSiteNodeElement )mapElement;

			if(mapel.element_id != null && !mapel.element_id.equals(""))
			{
				SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mapel.element_id);
//				System.out.println("notify SCHEME_ELEMENT_SELECTED_EVENT " + se.getId());
				this.logicalNetLayer.performProcessing = false;
				sne = new SchemeNavigateEvent(
						new SchemeElement[] { se },
						SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.performProcessing = true;
				return;
			}
		} 
		catch (Exception ex){ }

		try
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )mapElement;

			if(link.LINK_ID != null && !link.LINK_ID.equals(""))
			{
				SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
//				System.out.println("notify SCHEME_CABLE_LINK_SELECTED_EVENT " + scl.getId());
				this.logicalNetLayer.performProcessing = false;
				sne = new SchemeNavigateEvent(
						new SchemeCableLink[] { scl },
						SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.performProcessing = true;
				return;
			}
		} 
		catch (Exception ex){ }

		try 
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )mapElement;

			if(path.PATH_ID != null && !path.PATH_ID.equals(""))
			{
				SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
//				System.out.println("notify SCHEME_PATH_SELECTED_EVENT " + sp.getId());
				this.logicalNetLayer.performProcessing = false;
				sne = new SchemeNavigateEvent(
						new SchemePath[] { sp },
						SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT);
				dispatcher.notify(sne);
				this.logicalNetLayer.performProcessing = true;
				return;
			}
		} 
		catch (Exception ex){ } 
*/
	}

	/**
	 * Получить текущий фиксированный элемент.
	 * Используется в режиме {@link MapState#MOVE_FIXDIST}.
	 * @return текущий фиксированный элемент
	 */
	public AbstractNode getFixedNode()
	{
		return this.fixedNode;
	}

	/**
	 * Получить Список узлов, соседних (через фрагменты) с фиксированным узлом.
	 * Используется в режиме {@link MapState#MOVE_FIXDIST}.
	 * @return список элементов. 
	 */
	public List getFixedNodeList()
	{
		return this.fixedNodeList;
	}

	/**
	 * Получить текущий выбранный элемент.
	 * @return текущий выбранный элемент
	 */
	public MapElement getCurrentMapElement()
	{
		return this.currentMapElement;
	}

	/**
	 * Установить текущий выбранный элемент.
	 * @param curMapElement текущий выбранный элемент
	 */
	public void setCurrentMapElement(MapElement curMapElement)
	{
		Log.debugMessage(getClass().getName() + "::" + "setCurrentMapElement(" + curMapElement + ")" + " | " + "method call", Level.FINER);
		
		this.currentMapElement = curMapElement;

		if(getMapState().getOperationMode() == MapState.NO_OPERATION)
		{
			boolean canFixDist = (curMapElement instanceof TopologicalNode)
				|| (curMapElement instanceof SiteNode);
			if(getContext().getApplicationModel().isEnabled(
				MapApplicationModel.OPERATION_MOVE_FIXED) != canFixDist)
			{
				if(canFixDist)
				{
					this.fixedNode = (AbstractNode )curMapElement;
					this.fixedNodeList.clear();
					for(Iterator it = this.mapView.getMap().getNodeLinks(this.fixedNode).iterator(); it.hasNext();)
					{
						NodeLink mnle = (NodeLink)it.next();
						this.fixedNodeList.add(mnle.getOtherNode(this.fixedNode));
					}
				}
				getContext().getApplicationModel().setEnabled(
					MapApplicationModel.OPERATION_MOVE_FIXED,
					canFixDist);
				getContext().getApplicationModel().fireModelChanged();
			}
		}

		if(curMapElement instanceof VoidElement)
			return;
		if(! (curMapElement instanceof Selection))
			this.mapView.getMap().setSelected(curMapElement, true);
		notifySchemeEvent(this.currentMapElement);
	}

	/**
	 * Получить текущий элемент по экранной коордитате на карте.
	 * @param point экранная координата
	 * @return элемент в точке
	 */
	public MapElement getMapElementAtPoint(Point point, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		Log.debugMessage(getClass().getName() + "::" + "getMapElementAtPoint(" + point + ")" + " | " + "method call", Level.FINER);
		
		int showMode = getMapState().getShowMode();
		MapElement curME = VoidElement.getInstance(this.getMapView());

		//Здесь пробегаемся по всем элементам и если на каком-нибудь из них курсор
		//то устанавливаем его текущим элементом
		Iterator e = this.mapView.getAllElements().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			MapElementController controller = getMapViewController().getController(mapElement);
			if(controller.isElementVisible(mapElement, visibleBounds))
			if ( controller.isMouseOnElement(mapElement, point))
			{
				curME = mapElement;
			
				if ( mapElement instanceof NodeLink)
				{
					//Здесь смотрим по флагу linkState что делать
					if ( showMode == MapState.SHOW_NODE_LINK)
					{// curME остается NodeLink
					}
					else
					if ( showMode == MapState.SHOW_PHYSICAL_LINK)
					{
						curME = ((NodeLink)mapElement).getPhysicalLink();
					}
					else
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = this.mapView.getCablePaths((NodeLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (CablePath)it.next();
						}
						else
						{
							curME = ((NodeLink)mapElement).getPhysicalLink();
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = this.mapView.getMeasurementPaths((NodeLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MeasurementPath)it.next();
						}
						else
						{
							curME = ((NodeLink)mapElement).getPhysicalLink();
						}
					}
				}
				else
				if ( mapElement instanceof PhysicalLink)
				{
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = this.mapView.getCablePaths((PhysicalLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (CablePath)it.next();
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = this.mapView.getMeasurementPaths((PhysicalLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MeasurementPath)it.next();
						}
					}
				}
				break;
			}
		}
		return curME;
	}

	/**
	 * Отменить выбор всем элементам.
	 */
	public void deselectAll()
	{
		Log.debugMessage(getClass().getName() + "::" + "deselectAll()" + " | " + "method call", Level.FINER);

		Collection selectedElements = new LinkedList();
		selectedElements.addAll(this.mapView.getMap().getSelectedElements());
		Iterator it = selectedElements.iterator();
		while ( it.hasNext())
		{
			MapElement mapElement = (MapElement )it.next();
			mapElement.setSelected(false);
		}
		this.mapView.getMap().clearSelection();
	}

	/**
	 * Выбрать все элеметны.
	 */
	public void selectAll()
	{
		Log.debugMessage(getClass().getName() + "::" + "selectAll()" + " | " + "method call", Level.FINER);
		
		Iterator e = this.mapView.getAllElements().iterator();
		Map map = this.mapView.getMap();

		while(e.hasNext())
		{
			MapElement curElement = (MapElement)e.next();
			map.setSelected(curElement, true);
		}
	}

	/**
	 * Получить список выбранных элементов.
	 * @return список элементов
	 */	
	public Set getSelectedElements()
	{
		return this.mapView.getMap().getSelectedElements();
	}

	/**
	 * Проверка того, что хотя бы один элемент выбран.
	 * @return <code>true</code> если есть выбранный элемент
	 */
	public boolean isSelectionEmpty()
	{
		return getMapView().getMap().getSelectedElements().isEmpty();
	}

	/**
	 * Получить фрагмент линии, для которого булет произволиться
	 * редактирование длины, по коордитате на карте.
	 * @param point экранная координата
	 * @return фрагмент линии, или <code>null</code>, если фрагмента в данной
	 * точке нет
	 */
	public NodeLink getEditedNodeLink(Point point)
	{
		Log.debugMessage(getClass().getName() + "::" + "getEditedNodeLink(" + point + ")" + " | " + "method call", Level.FINER);
		
		NodeLinkController nlc = null;
		
		Iterator e = getMapView().getMap().getNodeLinks().iterator();
		while (e.hasNext())
		{
			NodeLink mapElement = (NodeLink)e.next();
			if(nlc == null)
				nlc = (NodeLinkController)getMapViewController().getController(mapElement);
			if (nlc.isMouseOnThisObjectsLabel(mapElement, point))
			{
				return mapElement;
			}
		}
		return null;
	}
	
	/**
	 * Установить новую длину фрагмента линии - расстояние от узла node до
	 * другого узла.
	 * @param nodelink фрагмент линии
	 * @param node узел, от которого откладывается расстояние
	 * @param dist расстояние
	 */
	public void setNodeLinkSizeFrom(NodeLink nodelink, AbstractNode node, double dist)
		throws MapConnectionException, MapDataException
	{
		DoublePoint anchor1 = node.getLocation();
		
		MoveNodeCommand cmd = new MoveNodeCommand(node);
		cmd.setLogicalNetLayer(this);
		
		((NodeLinkController)getMapViewController().getController(nodelink))
			.setSizeFrom(nodelink, node, dist);

		DoublePoint anchor2 = node.getLocation();
		cmd.setParameter(
				MoveSelectionCommandBundle.DELTA_X, 
				String.valueOf(anchor2.getX() - anchor1.getX()));
		cmd.setParameter(
				MoveSelectionCommandBundle.DELTA_Y, 
				String.valueOf(anchor2.getY() - anchor1.getY()));
		getCommandList().add(cmd);
		getCommandList().execute();

		getMapState().setOperationMode(MapState.NO_OPERATION);
	}

	/**
	 * Отменить последнюю выполненную команду пользователя.
	 */
	public void undo()
	{
		this.commandList.undo();
		sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	/**
	 * Повторить отмененную команду прользователя.
	 */
	public void redo()
	{
		this.commandList.redo();
		sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	/**
	 * Получить выбранный тип для создания новых линий.
	 * @return тип линии
	 * @throws ApplicationException 
	 */
	public PhysicalLinkType getCurrentPhysicalLinkType() throws ApplicationException
	{
		if(this.currentPhysicalLinkType == null) {
			this.currentPhysicalLinkType = LinkTypeController.getDefaultPhysicalLinkType();
		}
		return this.currentPhysicalLinkType;
	}
	
	/**
	 * Получить тип непривязанного узла.
	 * @return тип узла
	 * @throws ApplicationException 
	 */
	public SiteNodeType getUnboundNodeType() throws ApplicationException
	{
		if(this.unboundNodeType == null) {
			this.unboundNodeType = NodeTypeController.getUnboundNodeType();
		}
		return this.unboundNodeType;
	}
	
	/**
	 * Получить тип непривязанного кабеля.
	 * @return тип непривязанного кабеля
	 * @throws ApplicationException 
	 */
	public PhysicalLinkType getUnboundLinkType() throws ApplicationException
	{
		if(this.unboundLinkType == null) {
			this.unboundLinkType = LinkTypeController.getUnboundPhysicalLinkType();
		}
		return this.unboundLinkType;
	}
	
	/**
	 * Выбрать тип линии для создания новых линий.
	 * @param type тип линии
	 */
	public void setCurrentPhysicalLinkType(PhysicalLinkType type)
	{
		this.currentPhysicalLinkType = type;
	}

	/**
	 * получить контроллер вида.
	 * @return контроллер вида
	 */
	public MapViewController getMapViewController()
	{
		return this.mapViewController;
	}

	public void setMapViewController(MapViewController mapViewController )
	{
		this.mapViewController = mapViewController ;
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
	 * @author $Author: krupenn $
	 * @version $Revision: 1.111 $, $Date: 2005/08/12 10:41:11 $
	 * @module mapviewclient_modifying
	 */
	private class VisualMapElement
	{
		public AbstractNode startNode = null;
		public AbstractNode endNode = null;
		public Color color = null;
		public Stroke stroke = null;
		public MapElement mapElement = null;

		public VisualMapElement(AbstractNode startNode, AbstractNode endNode)
		{
			this.startNode = startNode;
			this.endNode = endNode;			
		}

		public VisualMapElement(
				AbstractNode startNode, 
				AbstractNode endNode,
				Color color,
				Stroke stroke)
		{
			this.startNode = startNode;
			this.endNode = endNode;
			this.color = color;
			this.stroke = stroke;			
		}

		public VisualMapElement(
				MapElement mapElement,
				Color color,
				Stroke stroke)
		{
			this.mapElement = mapElement;
			this.color = color;
			this.stroke = stroke;			
		}
	}
	
	/**
	 * Список VisualMapElement'ов, которые будут отображаться при текущем масштабе
	 */
	private Set<Object> visualElements = new HashSet<Object>();

	/**
	 * Линки с экранной длиной меньшей, этого параметра сливаются с соседними
	 * в один общий отображаемый элемент
	 */
	private static final int MINIMUM_SCREEN_LENGTH = 20;
	
	private java.util.Map<AbstractNode,Set<NodeLink>> linksForNodes =
		new HashMap<AbstractNode,Set<NodeLink>>();

	/**
	 * Обновляет список элементов которые должны отображаться при текущем массштабе.
	 */
	public void calculateVisualElements() 
			throws MapDataException, MapConnectionException {

		MapViewController.nullTime1();
		MapViewController.nullTime2();
		MapViewController.nullTime3();
		MapViewController.nullTime4();
		MapViewController.nullTime5();
		MapViewController.nullTime6();
		MapViewController.nullTime7();
		MapViewController.nullTime8();

		long startTime = System.currentTimeMillis();
		this.visualElements.clear();
		long t1 = System.currentTimeMillis();
		
		Map map = this.getMapView().getMap();

		Set<NodeLink> allNodeLinks = new HashSet<NodeLink>(this.mapView.getMap().getNodeLinks());
		long t2 = System.currentTimeMillis();

		if (getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH)
		{
			for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();)
			{
				MeasurementPath measurementPath = (MeasurementPath)it.next();

				Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				fillOptimizationSets(measurementPath, allNodeLinks, nodes, nodeLinks);
				
				MeasurementPathController controller = (MeasurementPathController )
						getMapViewController().getController(measurementPath);
				calculateVisualElements(
						nodes, 
						nodeLinks,
						controller.getColor(measurementPath),
						controller.getStroke(measurementPath),
						false);
			}
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			for(Iterator it = this.mapView.getCablePaths().iterator(); it.hasNext();)
			{
				CablePath cablePath = (CablePath)it.next();

				Set<AbstractNode> nodes = new HashSet<AbstractNode>();
				Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
				fillOptimizationSets(cablePath, allNodeLinks, nodes, nodeLinks);
				
				CableController controller = (CableController )
						getMapViewController().getController(cablePath);
				calculateVisualElements(
						nodes, 
						nodeLinks,
						controller.getColor(cablePath),
						controller.getStroke(cablePath),
						false);
			}
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			calculateVisualElements(
					map.getNodes(), 
					allNodeLinks,
					MapPropertiesManager.getColor(),
					MapPropertiesManager.getStroke(),
					true);
		}
		long endTime = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.calculateVisualLinks | "
				+ "optimized map for " + (endTime - startTime) + "ms. Got " + this.visualElements.size() + " visual links.\n"
				+ "		" + (t1 - startTime) + " ms (visualElements.clear())\n"
				+ "		" + (t2 - t1) + " ms (getNodeLinks)\n"				
				+ "		" + (endTime - t2) + " ms (recursing) divided to:\n"
				+ "			" + MapViewController.getTime1() + " ms (fillOptimizationSets)\n"
				+ "			" + MapViewController.getTime2() + " ms (searchLinksForNodes)\n"
				+ "			" + MapViewController.getTime3() + " ms (fill Calculated maps)\n"
				+ "			" + MapViewController.getTime4() + " ms (create VisualElements)\n"
				+ "			" + MapViewController.getTime6() + " ns (getCharacteristics)\n"
				+ "			" + MapViewController.getTime5() + " ms (calculate distance)\n",
				Level.INFO);
	}

	private void fillOptimizationSets(
			CablePath cablePath, 
			Set<NodeLink> allNodeLinks, 
			Set<AbstractNode> nodes, 
			Set<NodeLink> nodeLinks) {

		long t1 = System.currentTimeMillis();
		for(Iterator cpIterator = cablePath.getLinks().iterator(); cpIterator.hasNext();) {
			PhysicalLink physicalLink = (PhysicalLink )cpIterator.next();
			for(Iterator plIterator = physicalLink.getNodeLinks().iterator(); plIterator.hasNext();) {
				NodeLink nodeLink = (NodeLink )plIterator.next();
				nodes.add(nodeLink.getStartNode());
				nodes.add(nodeLink.getEndNode());
				nodeLinks.add(nodeLink);
				allNodeLinks.remove(nodeLink);
			}
		}
		long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	private void fillOptimizationSets(
			MeasurementPath measurementPath, 
			Set<NodeLink> allNodeLinks, 
			Set<AbstractNode> nodes, 
			Set<NodeLink> nodeLinks) {

		long t1 = System.currentTimeMillis();
		for(Iterator mpIterator = measurementPath.getSortedCablePaths().iterator(); mpIterator.hasNext();) {
			CablePath cablePath = (CablePath )mpIterator.next();
			for(Iterator cpIterator = cablePath.getLinks().iterator(); cpIterator.hasNext();) {
				PhysicalLink physicalLink = (PhysicalLink )cpIterator.next();
				for(Iterator plIterator = physicalLink.getNodeLinks().iterator(); plIterator.hasNext();) {
					NodeLink nodeLink = (NodeLink )plIterator.next();
					nodes.add(nodeLink.getStartNode());
					nodes.add(nodeLink.getEndNode());
					nodeLinks.add(nodeLink);
					allNodeLinks.remove(nodeLink);
				}
			}
		}
		long t2 = System.currentTimeMillis();
		MapViewController.addTime1(t2 - t1);
	}

	public void calculateVisualElements(
			Set<AbstractNode> nodes,
			Set<NodeLink> nodeLinks,
			Color color,
			Stroke stroke,
			boolean pullAttributesFromController)
			throws MapDataException, MapConnectionException {

		this.searchLinksForNodes(nodeLinks);

		Map map = this.getMapView().getMap();

		long t1 = System.currentTimeMillis();
		//Таблица, в которой содержится информация о том, обрабатывался ли
		//уже узел или нет
		java.util.Map<AbstractNode, Boolean> nodesCalculated = 
			new HashMap<AbstractNode, Boolean>(nodes.size());
		for (Iterator nodesIt = nodes.iterator(); nodesIt.hasNext();) {
			AbstractNode node = (AbstractNode) nodesIt.next();
			nodesCalculated.put(node, Boolean.FALSE);
		}

		java.util.Map<NodeLink, Boolean> nodeLinksCalculated = 
			new HashMap<NodeLink, Boolean>(nodeLinks.size());
		for (Iterator nodesIt = nodeLinks.iterator(); nodesIt.hasNext();) {
			NodeLink nodeLink = (NodeLink) nodesIt.next();
			nodeLinksCalculated.put(nodeLink, Boolean.FALSE);
		}
		long t2 = System.currentTimeMillis();
		MapViewController.addTime3(t2 - t1);

		//Запускаем рекурсию. 
		//Если очередной затравочный узел уже обработан - рекурсию с него не запускаем.
		//Если карта - это связный граф, то рекурсия будет запущена один раз.
		Collection<AbstractNode> frontedge = new LinkedList<AbstractNode>();
		for (Iterator nodesIt = nodes.iterator(); nodesIt.hasNext();) {
			AbstractNode startRecursionNode = (AbstractNode) nodesIt.next();

			while(startRecursionNode != null) {
				if (! nodesCalculated.get(startRecursionNode).booleanValue()) {
						pullVisualLinksFromNode(
								startRecursionNode, 
								startRecursionNode,
								null, 
								nodesCalculated,
								nodeLinksCalculated,
								frontedge,
								map,
								color,
								stroke,
								pullAttributesFromController);
				}
				Iterator<AbstractNode> it = frontedge.iterator();
				if(it.hasNext()) {
					startRecursionNode = it.next();
					it.remove();
				}
				else
					startRecursionNode = null;
			}
		}
	}

	/**
	 * Рекурсивная процедура обхода сегмента схемы карты, которому принадлежит
	 * "затравочный" узел.
	 * @param nodeToPullFrom Узел, от которого тянется текущий VisualMapElement.
	 * @param lastNode Предыдущий узел - от него мы пришли к данному узлу
	 * @param incomingLink Линк, по которому мы пришли в данный узел. Для первого
	 * уровня рекурсии = null.
	 * @param nodesCalculated Таблица содержащая информацию о том, обработан ли тот или 
	 * иной узел.
	 * @param nodeLinksCalculated 
	 * @param frontedge 
	 * @param map Объект "карта"
	 * @param stroke 
	 * @param color 
	 */
	private void pullVisualLinksFromNode(
			AbstractNode nodeToPullFrom,
			AbstractNode lastNode, 
			NodeLink incomingLink,
			java.util.Map<AbstractNode, Boolean> nodesCalculated, 
			java.util.Map<NodeLink, Boolean> nodeLinksCalculated, 
			Collection<AbstractNode> frontedge, 
			Map map, 
			Color color, 
			Stroke stroke,
			boolean pullAttributesFromController) 
			throws MapDataException, MapConnectionException {
		//Рассматриваемый узел
		AbstractNode nodeProcessed = null;

		if (incomingLink == null)
			nodeProcessed = lastNode;
		else
			nodeProcessed = incomingLink.getOtherNode(lastNode);

		if (nodeProcessed == null)
			throw new AssertionError(
					"LogicalNetLayer | pullVisualLinksFromNode | The nodeProcessed can't be null");

		//Получаем список всех входящих/исходящих линий для данного узла
		Set<NodeLink> allLinksForNodeProcessed = this.linksForNodes.get(nodeProcessed);
		
		boolean hasUncalculatedLinks = false;
		if (allLinksForNodeProcessed != null) {
			for (NodeLink outgoingLink : allLinksForNodeProcessed) {
				//Исключаем из списка линк, по которому мы пришли в данный узел
				if (!(outgoingLink == incomingLink)
						&& !nodeLinksCalculated.get(outgoingLink).booleanValue()) {
					hasUncalculatedLinks = true;
					break;
				}
			}
		}
		
		if (allLinksForNodeProcessed == null
				|| allLinksForNodeProcessed.size() == 0) {
			//Это одиночный узел (без связей). Он отображается всегда.
			this.visualElements.add(nodeProcessed);
			//Отмечаем в таблице, что узел обработан. 
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
			return;
		}

		if (nodesCalculated.get(nodeProcessed).booleanValue()
			||	(allLinksForNodeProcessed.size() == 1 
					&& allLinksForNodeProcessed.contains(incomingLink))
			|| !hasUncalculatedLinks) {
			//Первое условие останова - данный узел уже рассматривался или
			//является конечным в цепочке или все остальный линии, выходящие 
			//из него, уже рассмотрены.
			//Создаём элемент отображения и выходим.

			//Отмечаем в таблице, что узел обработан - для случая конечного
			//узла в цепочке.			
			nodesCalculated.put(lastNode, Boolean.TRUE);
			nodesCalculated.put(nodeProcessed, Boolean.TRUE);
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);
			
			VisualMapElement visualMapElement;
			long t1 = System.currentTimeMillis();
			if (lastNode == nodeToPullFrom) {
				//Если последний "натягиваемый" отображаемый элемент состоит из одного
				//линка - его и отображаем
				if(pullAttributesFromController) {
					NodeLinkController controller = (NodeLinkController )getMapViewController().getController(incomingLink);
					visualMapElement = new VisualMapElement(incomingLink, controller.getColor(incomingLink), controller.getStroke(incomingLink));
				}
				else
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
			}
			else {
				visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
			}
			long t2 = System.currentTimeMillis();
			MapViewController.addTime4(t2 - t1);

			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(nodeProcessed);
			return;
		}

		long t5 = System.currentTimeMillis();
		//Экранные координаты узла, от которого мы тянем визуальный элемент 
		Point startVENodeScr = this.converter.convertMapToScreen(
				nodeToPullFrom.getLocation());
		//Экранные координаты текущего узла		
		Point currEndVENodeScr = this.converter.convertMapToScreen(
				nodeProcessed.getLocation());
		double distance = Math.sqrt(
				(currEndVENodeScr.x - startVENodeScr.x) * (currEndVENodeScr.x - startVENodeScr.x)
			+	(currEndVENodeScr.y - startVENodeScr.y) * (currEndVENodeScr.y - startVENodeScr.y));
		long t6 = System.currentTimeMillis();
		MapViewController.addTime5(t6 - t5);

		if (distance >= MINIMUM_SCREEN_LENGTH)
		{
			//Второе условие останова - получили цепочку длиннее критической
			//длины - рекурсия останавливается, но ПРЕДПОСЛЕДНИЙ
			//узел цепочки ставится во фронт волны.
			
			VisualMapElement visualMapElement;
			AbstractNode lastVMENode;
			if (lastNode == nodeToPullFrom) {
				long t1 = System.currentTimeMillis();
				//Если "натягиваемый" отображаемый элемент состоит из одного
				//линка - его и отображаем
				if(pullAttributesFromController) {
					NodeLinkController controller = (NodeLinkController )getMapViewController().getController(incomingLink);
					visualMapElement = new VisualMapElement(incomingLink, controller.getColor(incomingLink), controller.getStroke(incomingLink));
				}
				else
					visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				long t2 = System.currentTimeMillis();
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
				for (NodeLink outLink : this.linksForNodes.get(nodeToPullFrom))
				{
					if (	outLink.getStartNode() == lastNode
						||	outLink.getEndNode() == lastNode) {
						linkBetween = outLink;
						break;
					}
				}
				
				long t1 = System.currentTimeMillis();
				if (linkBetween != null)
				{
					if(pullAttributesFromController) {
						NodeLinkController controller = (NodeLinkController )getMapViewController().getController(linkBetween);
						visualMapElement = new VisualMapElement(linkBetween, controller.getColor(linkBetween), controller.getStroke(linkBetween));
					}
					else
						visualMapElement = new VisualMapElement(linkBetween, color, stroke);
				}
				else
					visualMapElement = new VisualMapElement(nodeToPullFrom, lastNode, color, stroke);
				long t2 = System.currentTimeMillis();
				MapViewController.addTime4(t2 - t1);
				lastVMENode = lastNode;	
			}
			
			this.visualElements.add(visualMapElement);
			this.visualElements.add(nodeToPullFrom);
			this.visualElements.add(lastVMENode);
			
			frontedge.add(lastVMENode);
			return;
		}

		//Определяем тип - если мы имеем дело с SiteNode
		SiteNodeType nodeType = null;
		if (nodeProcessed instanceof SiteNode)
		{
			SiteNode sNode = (SiteNode)nodeProcessed;
			nodeType = (SiteNodeType)sNode.getType();
		}
		
		if (	(incomingLink != null)
				&&	(	(allLinksForNodeProcessed.size() > 2)
					||	(	(nodeType != null)
						&&	(nodeType.getSort().value() == SiteNodeTypeSort._BUILDING
							||	nodeType.getSort().value() == SiteNodeTypeSort._UNBOUND
							||	nodeType.getSort().value() == SiteNodeTypeSort._ATS)))) {
				//Третье условие останова - наткнулись на узел с развилкой или
				//узел типа, не подлежащего оптимизации, - рекурсия останавливается,
				//но ПОСЛЕДНИЙ узел цепочки ставится во фронт волны.
				
				
				VisualMapElement visualMapElement;
				long t1 = System.currentTimeMillis();
				if (lastNode == nodeToPullFrom) {
					//Если последний "натягиваемый" отображаемый элемент состоит из одного
					//линка - его и отображаем
					if(pullAttributesFromController) {
						NodeLinkController controller = (NodeLinkController )getMapViewController().getController(incomingLink);
						visualMapElement = new VisualMapElement(incomingLink, controller.getColor(incomingLink), controller.getStroke(incomingLink));
					}
					else
						visualMapElement = new VisualMapElement(incomingLink, color, stroke);
				}
				else {
					visualMapElement = new VisualMapElement(nodeToPullFrom, nodeProcessed, color, stroke);
				}
				long t2 = System.currentTimeMillis();
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
		if(incomingLink != null) {
			nodeLinksCalculated.put(incomingLink, Boolean.TRUE);
		}

		//Тянем элемент отображения дальше. Случай ветвления рассмотрен выше
		//(третье условие останова). Поэтому вызов рекурсивного метода из этого
		//цикла произойдёт единожды.
		for (NodeLink outgoingLink : allLinksForNodeProcessed) {
			//Исключаем из списка линк, по которому мы пришли в данный узел
			if (!(outgoingLink == incomingLink)
					&& !nodeLinksCalculated.get(outgoingLink).booleanValue()) {
				pullVisualLinksFromNode(
						nodeToPullFrom, 
						nodeProcessed,
						outgoingLink, 
						nodesCalculated, 
						nodeLinksCalculated,
						frontedge,
						map,
						color,
						stroke,
						pullAttributesFromController);
			}
		}
	}

	/**
	 * Отрисовывает элементы отображения, расположенные в заданных границах
	 * @param g
	 * @param visibleBounds
	 * @throws MapConnectionException
	 * @throws MapDataException
	 */
	private void drawVisualElements(Graphics g, Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		long t1 = System.currentTimeMillis();

		//Если режим показа nodeLink не разрешён, то включам режим показа physicalLink
		if (! this.aContext.getApplicationModel().isEnabled(MapApplicationModel.MODE_NODE_LINK))
		{
			Command modeCommand = getContext().getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
			modeCommand.execute();
		}

		for (Iterator veIterator = this.visualElements.iterator(); veIterator
				.hasNext();) {
			Object veElement = veIterator.next();
			if (veElement instanceof SiteNode) 
				continue;

			if (veElement instanceof VisualMapElement) {
				VisualMapElement vme = (VisualMapElement) veElement;
				if(vme.mapElement != null) {
					if(vme.mapElement instanceof NodeLink) {
						NodeLink nodeLink = (NodeLink)vme.mapElement;
						NodeLinkController controller = (NodeLinkController )getMapViewController().getController(nodeLink);
						if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
							controller.paint(nodeLink, g, visibleBounds);
						else {
							boolean selectionVisible = false;
							controller.paint(nodeLink, g, visibleBounds, vme.stroke, vme.color, selectionVisible);
						}
					}
					continue;
				}

				DoublePoint startLocation = vme.startNode.getLocation();
				DoublePoint endLocation = vme.endNode.getLocation();
				if (visibleBounds.intersectsLine(
						startLocation.getX(),
						startLocation.getY(), 
						endLocation.getX(), 
						endLocation.getY())) {
					Point from = this.converter.convertMapToScreen(startLocation);
					Point to = this.converter.convertMapToScreen(endLocation);
					g.setColor(vme.color);
					((Graphics2D )g).setStroke(vme.stroke);
					g.drawLine(from.x, from.y, to.x, to.y);
				}
			}
		}
		for (Iterator veIterator = this.visualElements.iterator(); veIterator.hasNext();) {
			Object veElement = veIterator.next();
			if (veElement instanceof AbstractNode) {
				getMapViewController().getController((AbstractNode)veElement).paint((AbstractNode)veElement, g, visibleBounds);
			}
		}
		long t2 = System.currentTimeMillis();
		Log.debugMessage("LogicalNetLayer.drawVisualElements | " + String.valueOf(t2 - t1) + " ms\n", 
				Level.INFO);
	}
	
	private void searchLinksForNodes(
			Set<NodeLink> nodeLinks)
	{
		long t1 = System.currentTimeMillis();

		Collection <Set<NodeLink>> emptySets = this.linksForNodes.values();
		for (Iterator emptySetsIt = emptySets.iterator(); emptySetsIt.hasNext();)
		{
			Set set = (Set)emptySetsIt.next();
			set.clear();
		}
		
		for (Iterator nodesLinksIt = nodeLinks.iterator(); nodesLinksIt.hasNext();)
		{
			NodeLink link = (NodeLink)nodesLinksIt.next();
			
			AbstractNode startNode = link.getStartNode();
			Set<NodeLink> startNodeLinksSet = this.linksForNodes.get(startNode);
			if (startNodeLinksSet == null)
			{
				startNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(startNode, startNodeLinksSet);
			}
			startNodeLinksSet.add(link);

			AbstractNode endNode = link.getEndNode();
			Set<NodeLink> endNodeLinksSet = this.linksForNodes.get(endNode);
			if (endNodeLinksSet == null)
			{
				endNodeLinksSet = new HashSet<NodeLink>();
				this.linksForNodes.put(endNode, endNodeLinksSet);
			}
			endNodeLinksSet.add(link);
		}
		
		long t2 = System.currentTimeMillis();
		MapViewController.addTime2(t2 - t1);
	}
}
