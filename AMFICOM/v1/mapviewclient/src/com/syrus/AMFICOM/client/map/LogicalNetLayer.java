/**
 * $Id: LogicalNetLayer.java,v 1.38 2005/01/30 15:38:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.CommandList;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveNodeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.mapview.AlarmMarker;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.EventMarker;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.Selection;
import com.syrus.AMFICOM.Client.Map.mapview.VoidElement;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
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
import com.syrus.AMFICOM.scheme.PathDecompositor;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Управляет отображением логической структуры сети.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.38 $, $Date: 2005/01/30 15:38:17 $
 * @module mapviewclient_v2
 */
public abstract class LogicalNetLayer implements MapCoordinatesConverter
{
	protected CommandList commandList = new CommandList(20);
	
	protected MapViewController mapViewController;

	/** Нить, управляющая анимацией на слое. */
	protected AnimateThread animateThread = null;

	/** Перменная показывающая что сейчас отображать. */
	protected MapState mapState = new MapState();
	
	/** Флаг видимости топологических узлов. */
	protected boolean showPhysicalNodeElement = true;
	
	/** Содержимое карты. */
	protected MapView mapView = null;
	
	/**
	 * Флаг обработки событий, необходимый для избежания зацикливания.
	 * При отправке сообщения Диспетчеру флаг выставляется в false с тем, 
	 * чтобы исключить обработку своих событий (посылку самому себе).
	 * После посылки флаг выставляется обратно в true. При обработке
	 * событий следует проверять этот флаг, и если он равен false, то обработку
	 * этого события не производить.
	 */
	protected boolean performProcessing = true;
	
	/** Флаг отправки сообщений. */
	protected boolean doNotify = true;
	
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
	protected PhysicalLinkType currentPen = null;

	/** Тип непривязанного схемного элемента. */
	protected SiteNodeType unboundProto = null;

	/** Тип непривязанного кабеля. */
	protected PhysicalLinkType unboundLinkProto = null;
	
	/** Текущая точка курсора мыши на карте (в экранных координатах). */	
	protected Point currentPoint = new Point(0, 0);

	/** Контекст приложения. */
	protected ApplicationContext aContext = null;

	/** Объект, ответственный за управление отображением карты. */
	protected NetMapViewer viewer = null;

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

	/** Флаг того, что контекстное меню отображено. */	
	
	protected boolean menuShown = false;

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
	 * Получить экранные координаты по географическим координатам.
	 * @param point географическая координата
	 * @return экранная координата
	 */
	public abstract Point convertMapToScreen(DoublePoint point);
	
	/**
	 * Получить географические координаты по экранным.
	 * @param point экранная координата
	 * @return географическая координата
	 */
	public abstract DoublePoint convertScreenToMap(Point point);

	/**
	 * Получить дистанцию между двумя точками в экранных координатах.
	 * @param from географическая координата
	 * @param to географическая координата
	 * @return расстояние
	 */
	public abstract double distance(DoublePoint from, DoublePoint to);

	/**
	 * Установить центральную точку вида карты.
	 * @param center географическая координата центра
	 */
	public abstract void setCenter(DoublePoint center);

	/**
	 * Получить центральную точку вида карты.
	 * @return географическая координата центра
	 */
	public abstract DoublePoint getCenter();

	/**
	 * Получить видимую область в географических координатах.
	 * @return видимая область
	 */
	public abstract Rectangle2D.Double getVisibleBounds();

	/**
	 * Произвести поиск географических объектов по подстроке.
	 * @param searchText текст поиска
	 * @return список найденных объектов ({@link SpatialObject})
	 */
	public abstract List findSpatialObjects(String searchText);
	
	/**
	 * Центрировать географический объект.
	 * @param so географический объект
	 */
	public abstract void centerSpatialObject(SpatialObject so);

	/**
	 * Освободить ресурсы компонента с картой и завершить отображение карты.
	 */
	public abstract void release();

	/**
	 * Перерисовать содержимое компонента с картой.
	 * @param fullRepaint производить ли полную перерисовку.
	 * <code>true</code> - перерисовываются географические объекты и элементы
	 * топологической схемы. <code>false</code> - перерисовываются только 
	 * элементы топологической схемы.
	 */
	public abstract void repaint(boolean fullRepaint);
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты.
	 * @param cursor курсор
	 */
	public abstract void setCursor(Cursor cursor);

	/**
	 * Получить установленный курсор.
	 * @return курсор
	 */
	public abstract Cursor getCursor();

	/**
	 * Получить текущий масштаб вида карты.
	 * @return масштаб
	 */
	public abstract double getScale();

	/**
	 * Установить заданный масштаб вида карты.
	 * @param scale масштаб
	 */
	public abstract void setScale(double scale);

	/**
	 * Установить масштаб вида карты с заданным коэффициентом.
	 * @param scaleСoef коэффициент масштабирования
	 */
	public abstract void scaleTo(double scaleСoef);

	/**
	 * Приблизить вид карты со стандартным коэффициентом.
	 */
	public abstract void zoomIn();

	/**
	 * Отдалить вид карты со стандартным коэффициентом.
	 */
	public abstract void zoomOut();
	
	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек.
	 * @param from географическая координата
	 * @param to географическая координата
	 */
	public abstract void zoomToBox(DoublePoint from, DoublePoint to);

	/**
	 * В режиме перемещения карты "лапкой" ({@link MapState#MOVE_HAND})
	 * передвинута мышь.
	 * @param me мышиное событие
	 */	
	public abstract void handDragged(MouseEvent me);
	
	/**
	 * При изменении масштаба отображения карты необходимо обновить
	 * масштаб отображения всех объектов на карте.
	 */
	public void updateZoom()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateZoom()");
		
		if(getMapView() == null)
			return;
		Map map = getMapView().getMap();
		if(map != null)
		{
			Iterator en =  getMapView().getMap().getNodes().iterator();
			while (en.hasNext())
			{
				AbstractNode curNode = (AbstractNode)en.next();
				((AbstractNodeController)getMapViewController().getController(curNode)).updateScaleCoefficient(curNode);
			}
		}
	}

	/**
	 * Установить контекст приложения.
	 * @param aContext контекст приложения
	 */
	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	/**
	 * Получить контекст приложения.
	 * @return контекст приложения
	 */
	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	/**
	 * Установить отображатель.
	 * @param mapViewer отображатель
	 */
	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapViewer(" + mapViewer + ")");
		
		this.viewer = mapViewer;
	}

	/**
	 * Получить отображатель.
	 * @return отображатель
	 */
	public NetMapViewer getMapViewer()
	{
		return viewer;
	}

	/**
	 * Инициализировать обозреватель заданным видом карты.
	 * @param mapView вид
	 */
	public void setMapView(MapView mapView)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapView(" + mapView + ")");
		
		if(animateThread != null)
			animateThread.stopRunning();

		if(	getContext() != null
			&& getContext().getDispatcher() != null)
		{
				if(mapView != null
					&& mapView.getMap() != null)
				{
					aContext.getDispatcher().notify(
						new MapEvent(mapView, MapEvent.MAP_VIEW_SELECTED));
					aContext.getDispatcher().notify(
						new MapEvent(mapView.getMap(), MapEvent.MAP_SELECTED));
				}
				else
				{
					aContext.getDispatcher().notify(
							new MapEvent(this, MapEvent.MAP_VIEW_DESELECTED));
					aContext.getDispatcher().notify(
						new MapEvent(this, MapEvent.MAP_DESELECTED));
				}
		}

		if(this.mapView != null)
		{
//			this.mapView.setLogicalNetLayer(null);
		}

		this.mapView = mapView;

		if(mapView == null)
		{
			System.out.println("mapView null!");
		}
		else
		{
			setScale(mapView.getScale());
			setCenter(mapView.getCenter());

			Iterator e = getMapViewController().getAllElements().iterator();
			while (e.hasNext())
			{
				MapElement mapElement = (MapElement )e.next();
				mapElement.setMap(mapView.getMap());
			}

			if(aContext != null)
				if(aContext.getApplicationModel() != null)
					if (aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_INDICATION))
				{
					animateThread = new AnimateThread(this);
					animateThread.start();
				}
		}

//		this.mapView.setLogicalNetLayer(this);
		getMapViewController().setMapView(mapView);

		//Поумолчанию текущий элемент Void
		currentMapElement = VoidElement.getInstance(this.mapView);

		commandList.flush();

		repaint(true);
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
	 * Установить топологическую карту.
	 * @param map топологическая карта
	 */
	public void setMap( Map map)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMap(" + map + ")");
		
		getMapView().setMap(map);
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
	 */
	public void setDefaultScale(double defaultScale)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setDefaultScale(" + defaultScale + ")");
		
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
	 * Установить текущий масштаб точечных элементов.
	 * @param currentScale текущий масштаб точечных элементов
	 */
	public void setCurrentScale(double currentScale)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentScale(" + currentScale + ")");
		this.currentScale = currentScale;
		updateZoom();
	}
	
	/**
	 * Получить текущее состояние слоя карты.
	 * @return состояние слоя карты
	 */
	public MapState getMapState()
	{
		return mapState;
	}

	/**
	 * Установить текущее состояние слоя карты.
	 * @param state новое состояние
	 */
	public void setMapState(MapState state)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapState(" + state + ")");
		
		this.mapState = state;
	}

	/**
	 * Получить текущую экранную точку.
	 * @return текущая экранная точка
	 */
	public Point getCurrentPoint()
	{
		return currentPoint;
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
	 * Передать команду на отображение координат текущей точки в окне координат.
	 * @param point экранная координата мыши
	 */	
	public void showLatLong(Point point)
	{
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		DoublePoint p = this.convertScreenToMap(point);
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_CENTER_CHANGED));
	}

	/**
	 * Получить запомненную начальную экранную точку.
	 * @return начальная экранная точка
	 */
	public Point getStartPoint()
	{
		return new Point(startPoint);
	}

	/**
	 * Установить начальную экранную точку.
	 * @param point начальная экранная точка
	 */
	public void setStartPoint(Point point)
	{
		startPoint = new Point(point);
	}

	/**
	 * Получить запомненную конечную экранную точку.
	 * @return конечная экранная точка
	 */
	public Point getEndPoint()
	{
		return new Point(endPoint);
	}

	/**
	 * Установить конечную экранную точку.
	 * @param point конечная экранная точка
	 */
	public void setEndPoint(Point point)
	{
		endPoint = new Point(point);
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
	 * Получить флаг отображения контекстного меню.
	 * @return флаг
	 */
	public boolean isMenuShown()
	{
		return this.menuShown;
	}

	/**
	 * Установить флаг отображения контекстного меню.
	 * @param isMenuShown флаг
	 */	
	public void setMenuShown(boolean isMenuShown)
	{
		this.menuShown = isMenuShown;
	}

	/**
	 * Отрисовать все элементы логического слоя топологической схемы.
	 * @param g графический контекст
	 */
	public void paint(Graphics g)
	{
		Graphics2D p = (Graphics2D )g;
		
		// remember settings from graphics
		Color color = p.getColor();
		Stroke stroke = p.getStroke();
		Font font = p.getFont();
		Color background = p.getBackground();

		drawLines(p);
		drawNodes(p);
		drawSelection(p);
		drawTempLines(p);
		
		// revert graphics to previous settings
		p.setColor(color);
		p.setStroke(stroke);
		p.setFont(font);
		p.setBackground(background);
	}

	/**
	 * Списокотображаемых элементов.
	 */
	protected List elementsToDisplay = new LinkedList();
	/**
	 * Отрисовать линейные объекты.
	 * @param g графический контекст
	 */
	public void drawLines(Graphics g)
	{
		Iterator e;
	
		Rectangle2D.Double visibleBounds = this.getVisibleBounds();
		
		elementsToDisplay.clear();

		//Если режим показа nodeLink не разрешён, то включам режим показа physicalLink
		if (! aContext.getApplicationModel().isEnabled("mapModeNodeLink"))
		{
			Command com = getContext().getApplicationModel().getCommand("mapModeLink");
			com.setParameter("applicationModel", aContext.getApplicationModel());
			com.setParameter("logicalNetLayer", this);
			com.execute();
		}

		if (getMapState().getShowMode() == MapState.SHOW_MEASUREMENT_PATH)
		{
			elementsToDisplay.addAll(getMapView().getMap().getPhysicalLinks());
			for(Iterator it = getMapViewController().getMeasurementPaths().iterator(); it.hasNext();)
			{
				MeasurementPath mpath = 
					(MeasurementPath)it.next();

				getMapViewController().getController(mpath).paint(mpath, g, visibleBounds);
				
				// to avoid multiple cicling through scheme elements
				// use once sorted cable links
				for(Iterator it2 = mpath.getSortedCablePaths().iterator(); it2.hasNext();)
				{
					CablePath cpath = 
						(CablePath)it2.next();
					elementsToDisplay.removeAll(cpath.getLinks());
				}
			}
			for(Iterator it = elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink mple = 
					(PhysicalLink)it.next();
				getMapViewController().getController(mple).paint(mple, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			elementsToDisplay.addAll(getMapView().getMap().getPhysicalLinks());
			for(Iterator it = getMapViewController().getCablePaths().iterator(); it.hasNext();)
			{
				CablePath cpath = 
					(CablePath)it.next();
				getMapViewController().getController(cpath).paint(cpath, g, visibleBounds);
				elementsToDisplay.removeAll(cpath.getLinks());
			}
			for(Iterator it = elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink mple = 
					(PhysicalLink)it.next();
				getMapViewController().getController(mple).paint(mple, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			e = getMapView().getMap().getPhysicalLinks().iterator();
			while (e.hasNext())
			{
				PhysicalLink mple = 
					(PhysicalLink)e.next();
				getMapViewController().getController(mple).paint(mple, g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			e = getMapView().getMap().getNodeLinks().iterator();
			while (e.hasNext())
			{
				NodeLink curNodeLink = (NodeLink)e.next();
				getMapViewController().getController(curNodeLink).paint(curNodeLink, g, visibleBounds);
			}
		}
	}

	/**
	 * Отрисовать узлы.
	 * @param g графический контекст
	 */
	public void drawNodes(Graphics g)
	{
		Rectangle2D.Double visibleBounds = this.getVisibleBounds();

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
/*
		e = getMapView().getMarkers().iterator();
		while (e.hasNext())
		{
			MapMarker marker = (MapMarker)e.next();
			marker.paint(pg, visibleBounds);
		}
*/
	}

	/**
	 * Отрисовать временние линии.
	 * 		- прямоугольник выбора объектов,
	 * 		- вновь проводимую линию
	 *      - прямоугольник масштабирования
	 * @param g графический контекст
	 */
	public void drawTempLines(Graphics g)
	{
		Graphics2D p = ( Graphics2D )g;
		int startX = getStartPoint().x;
		int startY = getStartPoint().y;
		int endX = getEndPoint().x;
		int endY = getEndPoint().y;
		
		p.setStroke(new BasicStroke(3));

		switch (mapState.getActionMode())
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
		if (mapState.getOperationMode() == MapState.ZOOM_TO_RECT )
		{
			p.setColor(Color.YELLOW);
			p.drawRect(
					Math.min(startX, endX),
					Math.min(startY, endY),
					Math.abs(endX - startX),
					Math.abs(endY - startY));
		}
		else
		if (mapState.getOperationMode() == MapState.MEASURE_DISTANCE )
		{
			p.setColor(Color.GREEN);
			p.drawLine(startX, startY, endX, endY);
		}
	}

	/**
	 * Отрисовать выделенные элементы.
	 * @param g графический контекст
	 */
	public void drawSelection(Graphics g)
	{
		Rectangle2D.Double visibleBounds = this.getVisibleBounds();

		Iterator e = getMapView().getMap().getSelectedElements().iterator();
		while (e.hasNext())
		{
			MapElement el = (MapElement)e.next();
			getMapViewController().getController(el).paint(el, g, visibleBounds);
		}
	}

	/**
	 * Обработка событий.
	 * @param ae событие
	 */
	public void operationPerformed(OperationEvent ae)
	{
		if(!performProcessing)
			return;

		if(ae.getActionCommand().equals(MapEvent.NEED_FULL_REPAINT))
		{
			repaint(true);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.NEED_REPAINT))
		{
			repaint(false);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.DESELECT_ALL))
		{
			getMapViewController().deselectAll();
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED))
		{
//			getMapView().setChanged(true);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_CHANGED))
		{
			Set selectedElements = getMapView().getMap().getSelectedElements();
			if(selectedElements.size() > 1)
			{
				Selection sel;
				if(! (getCurrentMapElement() instanceof Selection))
				{
					sel = new Selection(this);
					setCurrentMapElement(sel);
				}
				else
					sel = (Selection)getCurrentMapElement();

				sel.clear();
				sel.addAll(selectedElements);
				this.sendMapEvent(new MapEvent(sel, MapEvent.MAP_ELEMENT_SELECTED));
			}
			else
			if(selectedElements.size() == 1)
			{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
					MapElement me = (MapElement)selectedElements.iterator().next();
					setCurrentMapElement(me);
					this.sendMapEvent(new MapEvent(me, MapEvent.MAP_ELEMENT_SELECTED));
//				}
			}
			else
			//selectedElements.size() == 0
			{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
					setCurrentMapElement(com.syrus.AMFICOM.Client.Map.mapview.VoidElement.getInstance(getMapView()));
					this.sendMapEvent(new MapEvent(getCurrentMapElement(), MapEvent.MAP_ELEMENT_SELECTED));
//				}
			}
			updateZoom();
			repaint(false);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_ELEMENT_CHANGED))
		{
			Object me = ae.getSource();
			if(me instanceof SchemeElement)
			{
				getMapViewController().scanElement((SchemeElement )me);
				getMapViewController().scanCables(((SchemeElement )me).scheme());
			}
			else
			if(me instanceof SchemeCableLink)
			{
				getMapViewController().scanCable((SchemeCableLink )me);
				getMapViewController().scanPaths(((SchemeCableLink )me).scheme());
			}
			else
			if(me instanceof CablePath)
			{
				getMapViewController().scanCable(((CablePath)me).getSchemeCableLink());
				getMapViewController().scanPaths(((CablePath)me).getSchemeCableLink().scheme());
			}
			else
			if(me instanceof SiteNode)
			{
				SiteNode site = (SiteNode)me;
				SiteNodeController snc = (SiteNodeController)getMapViewController().getController(site);
				snc.updateScaleCoefficient(site);
			}

			repaint(false);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )ae;

			//Здесь принимаюттся собитыя по создению и управлению маркером
			if(mne.isDataMarkerCreated())
			{
				MeasurementPath path;
				try
				{
					path = getMapViewController().getMeasurementPathByMonitoredElementId(mne.getMeId());
				}
				catch (CommunicationException e)
				{
					e.printStackTrace();
					return;
				}
				catch (DatabaseException e)
				{
					e.printStackTrace();
					return;
				}

				if(path != null)
				{
					Marker marker = new Marker(
						mne.getMarkerId(),
	                    getMapView(),
						mne.getDistance(),
						path,
						mne.getMeId());
					getMapViewController().addMarker(marker);

					MarkerController mc = (MarkerController)getMapViewController().getController(marker);
					mc.moveToFromStartLo(marker, mne.getDistance());
				}
			}
			else
			if(mne.isDataEventMarkerCreated())
			{
				MeasurementPath path;
				try
				{
					path = getMapViewController().getMeasurementPathByMonitoredElementId(mne.getMeId());
				}
				catch (CommunicationException e)
				{
					e.printStackTrace();
					return;
				}
				catch (DatabaseException e)
				{
					e.printStackTrace();
					return;
				}

				if(path != null)
				{
					EventMarker marker = new EventMarker(
						mne.getMarkerId(),
	                    getMapView(),
						mne.getDistance(),
						path,
						mne.getMeId());
//					marker.descriptor = mne.descriptor;
					getMapViewController().addMarker(marker);

					MarkerController mc = (MarkerController)getMapViewController().getController(marker);

					mc.moveToFromStartLo(marker, mne.getDistance());
				}
			}
			else
			if(mne.isDataAlarmMarkerCreated())
			{
				MeasurementPath path;
				try
				{
					path = getMapViewController().getMeasurementPathByMonitoredElementId(mne.getMeId());
				}
				catch (CommunicationException e)
				{
					e.printStackTrace();
					return;
				}
				catch (DatabaseException e)
				{
					e.printStackTrace();
					return;
				}

				AlarmMarker marker = null;
				if(path != null)
				{
					for(Iterator it = getMapViewController().getMarkers().iterator(); it.hasNext();)
					{
						try
						{
							marker = (AlarmMarker)it.next();
							if(marker.getMeasurementPath().equals(path))
								break;
							marker = null;
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
					if(marker == null)
					{
						marker = new AlarmMarker(
							mne.getMarkerId(),
							getMapView(),
							mne.getDistance(),
							path,
							mne.getMeId());
//						marker.descriptor = mne.descriptor;
						getMapViewController().addMarker(marker);
					}
					else
					{
						marker.setId(mne.getMarkerId());
					}

					MarkerController mc = (MarkerController)getMapViewController().getController(marker);

					mc.moveToFromStartLo(marker, mne.getDistance());
				}
/*
				boolean found = false;

				MapPhysicalLinkElement link = 
				getMapView().findCablePath(mne.getSchemePathElementId());
				if(link != null)
				{
					link.setAlarmState(true);
					link.select();
				}
				else
				{
					MapSiteNodeElement node = findMapElementByCableLink(mne.linkID);
					if(node != null)
					{
						node.setAlarmState(true);
						node.select();
					}
				}
*/
			}
			else
			if(mne.isDataMarkerMoved())
			{
				Marker marker = getMapViewController().getMarker(mne.getMarkerId());
				if(marker != null)
				{
					if(marker.getPathDecompositor() == null)
						marker.setPathDecompositor((PathDecompositor )mne.getSchemePathDecompositor());

					MarkerController mc = (MarkerController)getMapViewController().getController(marker);

					mc.moveToFromStartLo(marker, mne.getDistance());
				}
			}
			else
			if(mne.isDataMarkerSelected())
			{
				Marker marker = getMapViewController().getMarker(mne.getMarkerId());
				if(marker != null)
					marker.setSelected(true);
			}
			else
			if(mne.isDataMarkerDeselected())
			{
				Marker marker = getMapViewController().getMarker(mne.getMarkerId());
				if(marker != null)
					marker.setSelected(false);
			}
			else
			if(mne.isDataMarkerDeleted())
			{
				Marker marker = getMapViewController().getMarker(mne.getMarkerId());
				if(marker != null)
					getMapViewController().removeMarker(marker);
				if(marker instanceof AlarmMarker)
				{
					AlarmMarker amarker = (AlarmMarker)marker;
/*
					MapPhysicalLinkElement link = findMapLinkByCableLink(marker.link_id);
					if(link != null)
					{
						link.setAlarmState(false);
						link.deselect();
					}
					else
					{
						MapSiteNodeElement node = findMapElementByCableLink(marker.link_id);
						if(node != null)
						{
							node.setAlarmState(false);
							node.deselect();
						}
					}
*/
				}
			}
			else
			if(mne.isMapElementSelected())
			{
				if(performProcessing)
				{
					MapElement me = (MapElement)mne.getSource();
					if(me != null)
						me.setSelected(true);
				}
			}
			else
			if(mne.isMapElementDeselected())
			{
				if(performProcessing)
				{
					MapElement me = (MapElement)mne.getSource();
					if(me != null)
						me.setSelected(false);
				}
			}

			repaint(false);
		}
		else
		if(ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)ae;

			List data = tdse.getList();
			int n = tdse.getSelectionNumber();

			if (n != -1)
			{
				try 
				{
					MapElement me = (MapElement)data.get(n);
					me.setSelected(true);
					repaint(false);
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				} 
			}
		}
		else
		if(ae.getActionCommand().equals(TreeListSelectionEvent.typ))
		{
			if(ae.getSource() instanceof MapElement)
			{
				MapElement me = (MapElement)ae.getSource();
				me.setSelected(true);
				repaint(false);
			} 
		}
		else
		if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			if(performProcessing)
			{
			SchemeNavigateEvent sne = (SchemeNavigateEvent )ae;
				if(sne.SCHEME_ELEMENT_SELECTED)
				{
					SchemeElement[] ses = (SchemeElement[] )sne.getSource();
	
					for(int i = 0; i < ses.length; i++)
					{
						SiteNode site = getMapViewController().findElement(ses[i]);
						if(site != null)
							site.setSelected(true);
					}
				}
				else
				if(sne.SCHEME_PATH_SELECTED)
				{
					SchemePath[] sps = (SchemePath[] )sne.getSource();
					
					for(int i = 0; i < sps.length; i++)
					{
						MeasurementPath mmp = getMapViewController().findMeasurementPath(sps[i]);
						if(mmp != null)
							mmp.setSelected(true);
					}
				}
				else
				if(sne.SCHEME_CABLE_LINK_SELECTED)
				{
					SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
					for(int i = 0; i < scs.length; i++)
					{
						CablePath mcp = getMapViewController().findCablePath(scs[i]);
						if(mcp != null)
							mcp.setSelected(true);
					}
				}
				else
				if(sne.SCHEME_ELEMENT_DESELECTED)
				{
					SchemeElement[] ses = (SchemeElement[] )sne.getSource();
	
					for(int i = 0; i < ses.length; i++)
					{
						SiteNode site = getMapViewController().findElement(ses[i]);
						if(site != null)
							site.setSelected(false);
					}
				}

				if(sne.SCHEME_PATH_DESELECTED)
				{
					SchemePath[] sps = (SchemePath[] )sne.getSource();
	
					for(int i = 0; i < sps.length; i++)
					{
						MeasurementPath mmp = getMapViewController().findMeasurementPath(sps[i]);
						if(mmp != null)
							mmp.setSelected(false);
					}
				}
	
				if(sne.SCHEME_CABLE_LINK_DESELECTED)
				{
					SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
					for(int i = 0; i < scs.length; i++)
					{
						CablePath mcp = getMapViewController().findCablePath(scs[i]);
						if(mcp != null)
							mcp.setSelected(false);
					}
				}
	
				repaint(false);
			}
		}
	}

	/**
	 * Отправить событие карты. Чтобы не отправить событие себе самому, 
	 * используется флаг {@link #performProcessing}
	 * @param me событие карты
	 */
	public void sendMapEvent(MapEvent me)
	{
		if(getContext() != null)
			if(getContext().getDispatcher() != null)
		{
			performProcessing = false;
			getContext().getDispatcher().notify(me);
			performProcessing = true;
		}
	}

	/**
	 * Генерация сообщеия о выборке элемента карты.
	 * @param mapElement выбранный элемент карты
	 */
	public void notifyMapEvent(MapElement mapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "notifyMapEvent(" + mapElement + ")");

		if(doNotify)
		{
			Dispatcher disp = getContext().getDispatcher();
			if(disp != null)
			{
				performProcessing = false;
				disp.notify(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				performProcessing = true;
			}
		}
	}

	/**
	 * Генерация сообщеия о выборке элемента карты.
	 * @param mapElement выбранный элемент карты
	 */
	public void notifySchemeEvent(MapElement mapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "notifySchemeEvent(" + mapElement + ")");
/*		
		SchemeNavigateEvent sne;
		Dispatcher dispatcher = logicalNetLayer.mapMainFrame.aContext.getDispatcher();
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
		return fixedNode;
	}

	/**
	 * Получить Список узлов, соседних (через фрагменты) с фиксированным узлом.
	 * Используется в режиме {@link MapState#MOVE_FIXDIST}.
	 * @return список элементов. 
	 * 
	 */
	public List getFixedNodeList()
	{
		return fixedNodeList;
	}

	/**
	 * Получить текущий выбранный элемент.
	 * @return текущий выбранный элемент
	 */
	public MapElement getCurrentMapElement()
	{
		return currentMapElement;
	}

	/**
	 * Установить текущий выбранный элемент.
	 * @param curMapElement текущий выбранный элемент
	 */
	public void setCurrentMapElement(MapElement curMapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentMapElement(" + curMapElement + ")");
		
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
					fixedNode = (AbstractNode )curMapElement;
					fixedNodeList.clear();
					for(Iterator it = fixedNode.getNodeLinks().iterator(); it.hasNext();)
					{
						NodeLink mnle = (NodeLink)it.next();
						fixedNodeList.add(mnle.getOtherNode(fixedNode));
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
		curMapElement.setSelected(true);
		notifyMapEvent(currentMapElement);
		notifySchemeEvent(currentMapElement);
	}

	/**
	 * Получить текущий элемент по экранной коордитате на карте.
	 * @param point экранная координата
	 * @return элемент в точке
	 */
	public MapElement getMapElementAtPoint(Point point)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapElementAtPoint(" + point + ")");
		
		int showMode = getMapState().getShowMode();
		MapElement curME = VoidElement.getInstance(this.getMapView());

		Rectangle2D.Double visibleBounds = this.getVisibleBounds();
		
		//Здесь пробегаемся по всем элементам и если на каком-нибудь из них курсор
		//то устанавливаем его текущим элементом
		Iterator e = getMapViewController().getAllElements().iterator();
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
					{
					}
					else
					if ( showMode == MapState.SHOW_PHYSICAL_LINK)
					{
						curME = ((NodeLink)mapElement).getPhysicalLink();
					}
					else
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = getMapViewController().getCablePaths((NodeLink)mapElement).iterator();
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
						Iterator it = getMapViewController().getMeasurementPaths((NodeLink)mapElement).iterator();
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
						Iterator it = getMapViewController().getCablePaths((PhysicalLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (CablePath)it.next();
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = getMapViewController().getMeasurementPaths((PhysicalLink)mapElement).iterator();
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");

		Iterator e = getMapViewController().getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			mapElement.setSelected(false);
		}
		getMapView().getMap().clearSelection();
	}

	/**
	 * Выбрать все элеметны.
	 */
	public void selectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "selectAll()");
		
		Iterator e = getMapViewController().getAllElements().iterator();

		while(e.hasNext())
		{
			MapElement curElement = (MapElement)e.next();
			curElement.setSelected(true);
		}
	}

	/**
	 * Получить список выбранных элементов.
	 * @return список элементов
	 */	
	public Set getSelectedElements()
	{
		return getMapView().getMap().getSelectedElements();
	}

	/**
	 * Проверка того, что хотя бы один элемент выбран.
	 * @return <code>true</code> если есть выбранный элемент
	 */
	public boolean isSelectionEmpty()
	{
//		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "isSelectionEmpty()");
//		
//		Iterator e = getMapView().getAllElements().iterator();
//
//		while (e.hasNext())
//		{
//			MapElement curElement = (MapElement)e.next();
//			if (curElement.isSelected())
//			{
//				return false;
//			}
//		}
//		return true;
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getEditedNodeLink(" + point + ")");
		
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
	 * Выполнить удаление выбранных элементов.
	 */
	public void delete()
	{
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(this);
		getCommandList().add(command);
		getCommandList().execute();
		repaint(false);
	}

	/**
	 * Отменить последнюю выполненную команду пользователя.
	 */
	public void undo()
	{
		commandList.undo();
		repaint(false);
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
	/**
	 * Повторить отмененную команду прользователя.
	 */
	public void redo()
	{
		commandList.redo();
		repaint(false);
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
	/**
	 * Получить выбранный тип для создания новых линий.
	 * @return тип линии
	 */
	public PhysicalLinkType getPen()
	{
		if(currentPen == null)
		{
			Identifier creatorId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);

			currentPen = LinkTypeController.getDefaultPen(creatorId);
		}
		return currentPen;
	}
	
	/**
	 * Получить тип непривязанного узла.
	 * @return тип узла
	 */
	public SiteNodeType getUnboundProto()
	{
		if(unboundProto == null)
		{
			Identifier creatorId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);

			unboundProto = NodeTypeController.getDefaultUnboundProto(creatorId);
		}
		return unboundProto;
	}
	
	/**
	 * Получить тип непривязанного кабеля.
	 * @return тип непривязанного кабеля
	 */
	public PhysicalLinkType getUnboundPen()
	{
		if(unboundLinkProto == null)
		{
			Identifier creatorId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);

			unboundLinkProto = LinkTypeController.getDefaultUnboundPen(creatorId);
		}
		return unboundLinkProto;
	}
	
	/**
	 * Выбрать тип линии для создания новых линий.
	 * @param pen тип линии
	 */
	public void setPen(PhysicalLinkType pen)
	{
		this.currentPen = pen;
	}

	/**
	 * получить контроллер вида.
	 * @return контроллер вида
	 */
	public MapViewController getMapViewController()
	{
		return com.syrus.AMFICOM.Client.Map.Controllers.MapViewController.getInstance(this);
	}
}
