/**
 * $Id: LogicalNetLayer.java,v 1.22 2004/11/16 17:31:17 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveNodeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapAlarmMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapEventMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathDecompositor;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
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
 * @version $Revision: 1.22 $, $Date: 2004/11/16 17:31:17 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class LogicalNetLayer implements MapCoordinatesConverter
{
	protected CommandList commandList = new CommandList(20);

	/**
	 * Нить, управляющая анимацией на слое
	 */
	protected AnimateThread animateThread = null;

	/**
	 * Перменная показывающая что сейчас отображать
	 */
	protected MapState mapState = new MapState();
	
	/**
	 * Флаг видимости PhysicalNodeElement
	 */
	protected boolean showPhysicalNodeElement = true;
	
	/**
	 * Содержимое карты
	 */
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
	
	protected boolean doNotify = true;
	
	/**
	 * Текущий элемент
	 */
	protected MapElement currentMapElement = null;

	/**
	 * фиксированный элемент
	 */
	protected MapNodeElement fixedNode = null;

	protected List fixedNodeList = new LinkedList();

	/**
	 * Текущий тип создаваемых физических линий
	 */
	protected MapLinkProtoElement currentPen = null;

	protected MapNodeProtoElement unboundProto = null;

	protected MapLinkProtoElement unboundLinkProto = null;
	
	/**
	 * Текущая точка курсора мыши на карте (в экранных координатах)
	 */	
	public Point currentPoint = new Point(0, 0);

	/**
	 * Контекст приложения
	 */
	protected ApplicationContext aContext = null;

	/**
	 * Объект, ответственный за управление отображением карты
	 */
	protected NetMapViewer viewer = null;

	/**
	 * При нажатии правой кнопки мыши и её перемещении, поля startX, startY получают
	 * координаты начальной точки (где произошло нажатие), а поля endX, endY координаты
	 * текущего положения мыши.
	 * Начальная точка для операций пользователя на карте с помощью мыши
	 */
	protected Point startPoint = new Point(0, 0);

	/**
	 * При нажатии правой кнопки мыши и её перемещении, поля startX, startY получают
	 * координаты начальной точки (где произошло нажатие), а поля endX, endY координаты
	 * текущего положения мыши.
	 * Конечная точка для операций пользователя на карте с помощью мыши
	 */
	protected Point endPoint = new Point(0, 0);

	/**
	 * 
	 */	
	
	protected boolean menuShown = false;

	/**
	 * Масштаб отображения объектов карты по умолчанию. Используется для
	 * автоматического масштабирования отрисовки объектов. Коэффициент
	 * увеличения размера отрисовываемого объекта определяется как отношение
	 * defaultScale / currentScale
	 */
	protected double defaultScale = 0.00001;
	
	/**
	 * Текуший масштаб отображения оюъектов карты
	 */
	protected double currentScale = 0.00001;

	/**
	 * Получить экранные координаты по географическим координатам
	 */
	public abstract Point convertMapToScreen(Point2D.Double point);
	
	/**
	 * Получить географические координаты по экранным
	 */
	public abstract Point2D.Double convertScreenToMap(Point point);

	/**
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public abstract double distance(Point2D.Double from, Point2D.Double to);

	/**
	 * Установить центральную точку вида карты
	 */
	public abstract void setCenter(Point2D.Double center);

	/**
	 * Получить центральную точку вида карты
	 */
	public abstract Point2D.Double getCenter();

	public abstract Rectangle2D.Double getVisibleBounds();

	public abstract List findSpatialObjects(String searchText);
	
	public abstract void centerSpatialObject(SpatialObject so);

	/**
	 * Освободить ресурсы компонента с картой и завершить отображение карты
	 */
	public abstract void release();

	/**
	 * Перерисовать содержимое компонента с картой
	 */
	public abstract void repaint();
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public abstract void setCursor(Cursor cursor);

	/**
	 * Получить текущий масштаб вида карты
	 */
	public abstract double getScale();

	/**
	 * Установить заданный масштаб вида карты
	 */
	public abstract void setScale(double scale);

	/**
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public abstract void scaleTo(double scaleСoef);

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public abstract void zoomIn();

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public abstract void zoomOut();
	
	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
	 */
	public abstract void zoomToBox(Point2D.Double from, Point2D.Double to);

	/**
	 * В режиме перемещения карты "лапкой" (MapState.HAND_MOVE) передвинута мышь
	 */	
	public abstract void handDragged(MouseEvent me);
	
	/**
	 * При изменении масштаба отображения карты необходимо обновить
	 * масштаб отображения всех объектов на карте
	 */
	public void updateZoom()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateZoom()");
		
		double sF = getDefaultScale() / getCurrentScale();

		Map map = getMapView().getMap();
		if(map != null)
		{
			Iterator en =  getMapView().getMap().getNodes().iterator();
			while (en.hasNext())
			{
				MapNodeElement curNode = (MapNodeElement )en.next();
				curNode.setScaleCoefficient(sF);
//				curNode.setImageId(curNode.getImageId());
			}
		}
	}

	/**
	 * сеттер
	 */
	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	/**
	 * геттер
	 */
	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	/**
	 * сеттер
	 */
	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapViewer(" + mapViewer + ")");
		
		this.viewer = mapViewer;
	}

	/**
	 * геттер
	 */
	public NetMapViewer getMapViewer()
	{
		return viewer;
	}

	/**
	 * Инициализировать обозреватель заданным видом карты
	 */
	public void setMapView(MapView mapView)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapView(" + mapView + ")");
		
		if(animateThread != null)
			animateThread.stop_running();

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
			this.mapView.setLogicalNetLayer(null);
		}

		this.mapView = mapView;

		if(mapView == null)
		{
			this.mapView = new MapView();
		}
		else
		{
			setScale(mapView.getScale());
			setCenter(mapView.getCenter());

			Iterator e = mapView.getAllElements().iterator();
			while (e.hasNext())
			{
				MapElement mapElement = (MapElement)e.next();
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

		this.mapView.setLogicalNetLayer(this);

		//Поумолчанию текущий элемент Void
		currentMapElement = VoidMapElement.getInstance(this.mapView);

		commandList.flush();

		repaint();
	}

	/**
	 * геттер
	 */
	public MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * сеттер
	 */
	public void setMap( Map map)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMap(" + map + ")");
		
		setMapView(new MapView());
		getMapView().setMap(map);
	}

	/**
	 * геттер
	 */
	public double getDefaultScale()
	{
		return this.defaultScale;
	}
	
	/**
	 * сеттер
	 */
	public void setDefaultScale(double defaultScale)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setDefaultScale(" + defaultScale + ")");
		
		this.defaultScale = defaultScale;
		updateZoom();
	}
	
	/**
	 * получить текущий масштаб точечных элементов
	 */
	public double getCurrentScale()
	{
		return this.currentScale;
	}
	
	/**
	 * сеттер
	 */
	public void setCurrentScale(double currentScale)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentScale(" + currentScale + ")");
		this.currentScale = currentScale;
		updateZoom();
	}
	
	/**
	 * геттер
	 */
	public MapState getMapState()
	{
		return mapState;
	}

	/**
	 * сеттер
	 */
	public void setMapState(MapState state)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setMapState(" + state + ")");
		
		this.mapState = state;
	}

	/**
	 * геттер
	 */
	public Point getCurrentPoint()
	{
		return currentPoint;
	}
	
	/**
	 * сеттер
	 */
	public void setCurrentPoint(Point point)
	{
		this.currentPoint = point;
	}
	
	/**
	 * передать команду на отображение координат текущей точки
	 */	
	public void showLatLong(Point point)
	{
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		Point2D.Double p = this.convertScreenToMap(point);
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_CENTER_CHANGED));
	}

	/**
	 * uеттер
	 */
	public Point getStartPoint()
	{
		return new Point(startPoint);
	}

	/**
	 * сеттер
	 */
	public void setStartPoint(Point point)
	{
		startPoint = new Point(point);
	}

	/**
	 * uеттер
	 */
	public Point getEndPoint()
	{
		return new Point(endPoint);
	}

	/**
	 * сеттер
	 */
	public void setEndPoint(Point point)
	{
		endPoint = new Point(point);
	}
	
	public CommandList getCommandList()
	{
		return this.commandList;
	}

	/**
	 * 
	 */
	public boolean isMenuShown()
	{
		return this.menuShown;
	}

	/**
	 * 
	 */	
	public void setMenuShown(boolean isMenuShown)
	{
		this.menuShown = isMenuShown;
	}

	/**
	 * отрисовать все элементы логического слоя топологической схемы
	 * @param g
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
		drawTempLines(p);
		
		// revert graphics to previous settings
		p.setColor(color);
		p.setStroke(stroke);
		p.setFont(font);
		p.setBackground(background);
	}

	protected static List elementsToDisplay = new LinkedList();
	/**
	 * Отрисовать линейные объекты
	 * @param g
	 */
	public void drawLines(Graphics g)
	{
		Graphics2D p = (Graphics2D )g;
		
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
			for(Iterator it = getMapView().getMeasurementPaths().iterator(); it.hasNext();)
			{
				MapMeasurementPathElement mpath = 
					(MapMeasurementPathElement )it.next();
				mpath.paint(g, visibleBounds);
				
				// to avoid multiple cicling through scheme elements
				// use once sorted cable links
				for(Iterator it2 = mpath.getSortedCablePaths().iterator(); it2.hasNext();)
				{
					MapCablePathElement cpath = 
						(MapCablePathElement )it2.next();
					elementsToDisplay.removeAll(cpath.getLinks());
				}
			}
			for(Iterator it = elementsToDisplay.iterator(); it.hasNext();)
			{
				MapPhysicalLinkElement mple = 
					(MapPhysicalLinkElement )it.next();
				mple.paint(g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			elementsToDisplay.addAll(getMapView().getMap().getPhysicalLinks());
			for(Iterator it = getMapView().getCablePaths().iterator(); it.hasNext();)
			{
				MapCablePathElement cpath = 
					(MapCablePathElement )it.next();
				cpath.paint(g, visibleBounds);
				elementsToDisplay.removeAll(cpath.getLinks());
			}
			for(Iterator it = elementsToDisplay.iterator(); it.hasNext();)
			{
				MapPhysicalLinkElement mple = 
					(MapPhysicalLinkElement )it.next();
				mple.paint(g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			e = getMapView().getMap().getPhysicalLinks().iterator();
			while (e.hasNext())
			{
				MapPhysicalLinkElement mple = 
					(MapPhysicalLinkElement )e.next();
				mple.paint(g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			e = getMapView().getMap().getNodeLinks().iterator();
			while (e.hasNext())
			{
				MapNodeLinkElement curNodeLink = (MapNodeLinkElement )e.next();
				curNodeLink.paint(p, visibleBounds);
			}
		}
	}

	/**
	 * Рисует элементы наследники от класса Node
	 */
	public void drawNodes(Graphics g)
	{
		Graphics2D pg = (Graphics2D )g;

		Rectangle2D.Double visibleBounds = this.getVisibleBounds();

		boolean showNodes = MapPropertiesManager.isShowPhysicalNodes();
		Iterator e = getMapView().getMap().getNodes().iterator();
		while (e.hasNext())
		{
			MapNodeElement curNode = (MapNodeElement )e.next();
			if(curNode instanceof MapPhysicalNodeElement)
			{
				if(showNodes)
				{
					curNode.paint(pg, visibleBounds);
				}
			}
			else
				curNode.paint(pg, visibleBounds);
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
	 * Рисует временние линии 
	 * 		- прямоугольник выбора объектов,
	 * 		- вновь проводимую линию
	 *      - прямоугольник масштабирования
	 */
	public void drawTempLines(Graphics g)
	{
		MapState mapState = getMapState();
		
		Graphics2D p = ( Graphics2D)g;
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
	 * Обработка событий
	 */
	public void operationPerformed(OperationEvent ae)
	{
		if(!performProcessing)
			return;

		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED))
		{
			getMapView().setChanged(true);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_CHANGED))
		{
			getMapView().getMap().setChanged(true);

			Set selectedElements = getMapView().getMap().getSelectedElements();
			if(selectedElements.size() > 1)
			{
				MapSelection sel;
				if(! (getCurrentMapElement() instanceof MapSelection))
				{
					sel = new MapSelection(this);
					setCurrentMapElement(sel);
				}
				else
					sel = (MapSelection )getCurrentMapElement();

				sel.clear();
				sel.addAll(selectedElements);
				this.sendMapEvent(new MapEvent(sel, MapEvent.MAP_ELEMENT_SELECTED));
			}
			else
			if(selectedElements.size() == 1)
			{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
					MapElement me = (MapElement )selectedElements.iterator().next();
					setCurrentMapElement(me);
					this.sendMapEvent(new MapEvent(me, MapEvent.MAP_ELEMENT_SELECTED));
//				}
			}
			else
			//selectedElements.size() == 0
			{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
					setCurrentMapElement(VoidMapElement.getInstance(getMapView()));
					this.sendMapEvent(new MapEvent(getCurrentMapElement(), MapEvent.MAP_ELEMENT_SELECTED));
//				}
			}
			repaint();
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_ELEMENT_CHANGED))
		{
			Object me = ae.getSource();
			if(me instanceof SchemeElement)
			{
				getMapView().scanElement((SchemeElement )me);
				getMapView().scanCables((Scheme )Pool.get(Scheme.typ, ((SchemeElement )me).getSchemeId()));
			}
			else
			if(me instanceof SchemeCableLink)
			{
				getMapView().scanCable((SchemeCableLink )me);
				getMapView().scanPaths((Scheme )Pool.get(Scheme.typ, ((SchemeCableLink )me).getSchemeId()));
			}
			else
			if(me instanceof MapCablePathElement)
			{
				getMapView().scanCable(((MapCablePathElement )me).getSchemeCableLink());
				getMapView().scanPaths((Scheme )Pool.get(Scheme.typ, ((MapCablePathElement )me).getSchemeCableLink().getSchemeId()));
			}

			repaint();
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )ae;

			//Здесь принимаюттся собитыя по создению и управлению маркером
			if(mne.isDataMarkerCreated())
			{
				MapMeasurementPathElement path = mapView.getMeasurementPathByMonitoredElementId(mne.getMeId());

				if(path != null)
				{
					MapMarker marker = new MapMarker(
						mne.getMarkerId(),
	                    getMapView(),
						mne.getDistance(),
						path,
						mne.getMeId());
					getMapView().addMarker(marker);
					marker.moveToFromStartLo(mne.getDistance());
				}
			}
			else
			if(mne.isDataEventMarkerCreated())
			{
				MapMeasurementPathElement path = mapView.getMeasurementPathByMonitoredElementId(mne.getMeId());

				if(path != null)
				{
					MapEventMarker marker = new MapEventMarker(
						mne.getMarkerId(),
	                    getMapView(),
						mne.getDistance(),
						path,
						mne.getMeId());
//					marker.descriptor = mne.descriptor;
					getMapView().addMarker(marker);
					marker.moveToFromStartLo(mne.getDistance());
				}
			}
			else
			if(mne.isDataAlarmMarkerCreated())
			{
				MapMeasurementPathElement path = mapView.getMeasurementPathByMonitoredElementId(mne.getMeId());

				MapAlarmMarker marker = null;
				if(path != null)
				{
					for(Iterator it = getMapView().getMarkers().iterator(); it.hasNext();)
					{
						try
						{
							marker = (MapAlarmMarker )it.next();
							if(marker.getMeasurementPath().equals(path))
								break;
							marker = null;
						}
						catch(Exception ex)
						{
						}
					}
					if(marker == null)
					{
						marker = new MapAlarmMarker(
							mne.getMarkerId(),
							getMapView(),
							mne.getDistance(),
							path,
							mne.getMeId());
//						marker.descriptor = mne.descriptor;
						getMapView().addMarker(marker);
					}
					else
					{
						marker.setId(mne.getMarkerId());
					}
					marker.moveToFromStartLo(mne.getDistance());
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
				MapMarker marker = getMapView().getMarker(mne.getMarkerId());
				if(marker != null)
				{
					if(marker.getPathDecompositor() == null)
						marker.setPathDecompositor((PathDecompositor )mne.getSchemePathDecompositor());
					marker.moveToFromStartLo(mne.getDistance());
				}
			}
			else
			if(mne.isDataMarkerSelected())
			{
				MapMarker marker = getMapView().getMarker(mne.getMarkerId());
				if(marker != null)
					marker.setSelected(true);
			}
			else
			if(mne.isDataMarkerDeselected())
			{
				MapMarker marker = getMapView().getMarker(mne.getMarkerId());
				if(marker != null)
					marker.setSelected(false);
			}
			else
			if(mne.isDataMarkerDeleted())
			{
				MapMarker marker = getMapView().getMarker(mne.getMarkerId());
				if(marker != null)
					getMapView().removeMarker(marker);
				if(marker instanceof MapAlarmMarker)
				{
					MapAlarmMarker amarker = (MapAlarmMarker )marker;
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
					MapElement me = (MapElement )mne.getSource();
					if(me != null)
						me.setSelected(true);
				}
			}
			else
			if(mne.isMapElementDeselected())
			{
				if(performProcessing)
				{
					MapElement me = (MapElement )mne.getSource();
					if(me != null)
						me.setSelected(false);
				}
			}

			repaint();
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
					MapElement me = (MapElement )data.get(n);
					me.setSelected(true);
					repaint();
				} 
				catch (Exception ex) 
				{
				} 
			}
		}
		else
		if(ae.getActionCommand().equals(TreeListSelectionEvent.typ))
		{
			if(ae.getSource() instanceof MapElement)
			{
				MapElement me = (MapElement )ae.getSource();
				me.setSelected(true);
				repaint();
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
						MapSiteNodeElement site = getMapView().findElement(ses[i]);
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
						MapMeasurementPathElement mmp = getMapView().findMeasurementPath(sps[i]);
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
						MapCablePathElement mcp = getMapView().findCablePath(scs[i]);
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
						MapSiteNodeElement site = getMapView().findElement(ses[i]);
						if(site != null)
							site.setSelected(false);
					}
				}

				if(sne.SCHEME_PATH_DESELECTED)
				{
					SchemePath[] sps = (SchemePath[] )sne.getSource();
	
					for(int i = 0; i < sps.length; i++)
					{
						MapMeasurementPathElement mmp = getMapView().findMeasurementPath(sps[i]);
						if(mmp != null)
							mmp.setSelected(false);
					}
				}
	
				if(sne.SCHEME_CABLE_LINK_DESELECTED)
				{
					SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
					for(int i = 0; i < scs.length; i++)
					{
						MapCablePathElement mcp = getMapView().findCablePath(scs[i]);
						if(mcp != null)
							mcp.setSelected(false);
					}
				}
	
				repaint();
			}
		}
	}

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
	 * Генерация сообщеия о выборке элемента карты
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
	 * Генерация сообщеия о выборке элемента карты
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
	 * Получить текущий фиксированный элемент
	 */
	public MapNodeElement getFixedNode()
	{
		return fixedNode;
	}

	/**
	 * Получить Список узлов, соседних (через фрагменты) с фиксированным узлом
	 */
	public List getFixedNodeList()
	{
		return fixedNodeList;
	}

	/**
	 * Получить текущий выбранный элемент
	 */
	public MapElement getCurrentMapElement()
	{
		return currentMapElement;
	}

	/**
	 * Установить текущий выбранный элемент
	 */
	public void setCurrentMapElement(MapElement curMapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setCurrentMapElement(" + curMapElement + ")");
		
		this.currentMapElement = curMapElement;

		if(getMapState().getOperationMode() == MapState.NO_OPERATION)
		{
			boolean canFixDist = (curMapElement instanceof MapPhysicalNodeElement)
				|| (curMapElement instanceof MapSiteNodeElement);
			if(getContext().getApplicationModel().isEnabled(
				MapApplicationModel.OPERATION_MOVE_FIXED) != canFixDist)
			{
				if(canFixDist)
				{
					fixedNode = (MapNodeElement )curMapElement;
					fixedNodeList.clear();
					for(Iterator it = fixedNode.getNodeLinks().iterator(); it.hasNext();)
					{
						MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
						fixedNodeList.add(mnle.getOtherNode(fixedNode));
					}
				}
				getContext().getApplicationModel().setEnabled(
					MapApplicationModel.OPERATION_MOVE_FIXED,
					canFixDist);
				getContext().getApplicationModel().fireModelChanged();
			}
		}

		if(curMapElement instanceof VoidMapElement)
			return;
		curMapElement.setSelected(true);
		notifyMapEvent(currentMapElement);
		notifySchemeEvent(currentMapElement);
	}

	/**
	 * Получить текущий элемент по экранной коордитате на карте
	 */
	public MapElement getMapElementAtPoint(Point point)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapElementAtPoint(" + point + ")");
		
		int showMode = getMapState().getShowMode();
		MapElement curME = VoidMapElement.getInstance(this.getMapView());

		Rectangle2D.Double visibleBounds = this.getVisibleBounds();
		
		MapView mapView = getMapView();
		Map map = mapView.getMap();

		//Здесь пробегаемся по всем элементам и если на каком-нибудь из них курсор
		//то устанавливаем его текущим элементом
		Iterator e = mapView.getAllElements().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			if(mapElement.isVisible(visibleBounds))
			if ( mapElement.isMouseOnThisObject(point))
			{
				curME = mapElement;
			
				if ( mapElement instanceof MapNodeLinkElement)
				{
					//Здесь смотрим по флагу linkState что делать
					if ( showMode == MapState.SHOW_NODE_LINK)
					{
					}
					else
					if ( showMode == MapState.SHOW_PHYSICAL_LINK)
					{
						curME = map.getPhysicalLink( 
							((MapNodeLinkElement )mapElement).getPhysicalLinkId());
					}
					else
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = mapView.getCablePaths((MapNodeLinkElement )mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MapCablePathElement)it.next();
						}
						else
						{
							curME = map.getPhysicalLink( 
								((MapNodeLinkElement )mapElement).getPhysicalLinkId());
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = mapView.getMeasurementPaths((MapNodeLinkElement )mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MapMeasurementPathElement )it.next();
						}
						else
						{
							curME = map.getPhysicalLink( 
								((MapNodeLinkElement )mapElement).getPhysicalLinkId());
						}
					}
				}
				else
				if ( mapElement instanceof MapPhysicalLinkElement)
				{
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = mapView.getCablePaths((MapPhysicalLinkElement )mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MapCablePathElement)it.next();
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = mapView.getMeasurementPaths((MapPhysicalLinkElement )mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MapMeasurementPathElement )it.next();
						}
					}
				}
				break;
			}
		}
		return curME;
	}

	/**
	 * Отменить выбор всем элементам
	 */
	public void deselectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");

		Iterator e = getMapView().getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			mapElement.setSelected(false);
		}
		getMapView().getMap().clearSelection();
	}

	/**
	 * Выбрать все элеметны
	 */
	public void selectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "selectAll()");
		
		Iterator e = getMapView().getAllElements().iterator();

		while(e.hasNext())
		{
			MapElement curElement = (MapElement )e.next();
			curElement.setSelected(true);
		}
	}

	/**
	 * Получить список выбранных элементов
	 */	
	public Set getSelectedElements()
	{
/*
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getSelectedElements()");

		List returnList = new LinkedList();
		
		Iterator e = getMapView().getAllElements().iterator();

		while(e.hasNext())
		{
			MapElement curElement = (MapElement )e.next();
			if(curElement.isSelected())
				returnList.add(curElement);
		}
		
		return returnList;
*/
		return getMapView().getMap().getSelectedElements();
	}

	/**
	 * Проверка того, что хотя бы один элемент выбран
	 */
	public boolean isSelectionEmpty()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "isSelectionEmpty()");
		
		Iterator e = getMapView().getAllElements().iterator();

		while (e.hasNext())
		{
			MapElement curElement = (MapElement )e.next();
			if (curElement.isSelected())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Получить MapNodeLinkElement, для которого булет произволиться
	 * редактирование длины, по коордитате на карте
	 */
	public MapNodeLinkElement getEditedNodeLink(Point point)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getEditedNodeLink(" + point + ")");
		
		Iterator e = getMapView().getMap().getNodeLinks().iterator();
		while (e.hasNext())
		{
			MapNodeLinkElement mapElement = (MapNodeLinkElement )e.next();
			if (mapElement.isMouseOnThisObjectsLabel(point))
			{
				return mapElement;
			}
		}
		return null;
	}
	
	public void setNodeLinkSizeFrom(MapNodeLinkElement nodelink, MapNodeElement node, double dist)
	{
		Point2D.Double anchor1 = node.getAnchor();
		
		MoveNodeCommand cmd = new MoveNodeCommand(node);
		cmd.setLogicalNetLayer(this);
		nodelink.setSizeFrom(node, dist);

		Point2D.Double anchor2 = node.getAnchor();
		cmd.setParameter(
				MoveSelectionCommandBundle.DELTA_X, 
				String.valueOf(anchor2.x - anchor1.x));
		cmd.setParameter(
				MoveSelectionCommandBundle.DELTA_Y, 
				String.valueOf(anchor2.y - anchor1.y));
		getCommandList().add(cmd);
		getCommandList().execute();

		getMapState().setOperationMode(MapState.NO_OPERATION);
	}

	/**
	 * Выполнить удаление выбранных элементов
	 */
	public void delete()
	{
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(this);
		getCommandList().add(command);
		getCommandList().execute();
		repaint();
	}

	/**
	 * Отменить последнюю выполненную команду пользователя
	 */
	public void undo()
	{
		commandList.undo();
		repaint();
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
	/**
	 * Повторить отмененную команду прользователя
	 */
	public void redo()
	{
		commandList.redo();
		repaint();
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
	public MapLinkProtoElement getPen()
	{
		if(currentPen == null)
			currentPen = getDefaultPen();
		return currentPen;
	}
	
	public MapNodeProtoElement getUnboundProto()
	{
		if(unboundProto == null)
			unboundProto = getDefaultUnboundProto();
		return unboundProto;
	}
	
	public MapLinkProtoElement getUnboundPen()
	{
		if(unboundLinkProto == null)
			unboundLinkProto = getDefaultCable();
		return unboundLinkProto;
	}
	
	public void setPen(MapLinkProtoElement pen)
	{
		this.currentPen = pen;
	}

	protected MapNodeProtoElement getDefaultUnboundProto()
	{
		MapNodeProtoElement mnpe = null;
		
		mnpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, MapNodeProtoElement.UNBOUND);
		if(mnpe == null)
		{
			ImageCatalogue.add(
				"unbound",
				new ImageResource("unbound", "unbound", "images/unbound.gif"));

			mnpe = new MapNodeProtoElement(
				MapNodeProtoElement.UNBOUND,
				LangModelMap.getString("UnboundElement"),
				true,
				"unbound",
				"desc");
			Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);
		}
		
		return mnpe;
	}

	public List getPens()
	{
		MapLinkProtoElement mlpe = null;
		
		mlpe = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, MapLinkProtoElement.TUNNEL);
		if(mlpe == null)
		{
			mlpe = new MapLinkProtoElement(
				MapLinkProtoElement.TUNNEL,
				LangModelMap.getString("Tunnel"),
				"desc",
				new Dimension(3, 4));
			mlpe.setLineSize(2);
			mlpe.setColor(Color.BLACK);
			Pool.put(MapLinkProtoElement.typ, mlpe.getId(), mlpe);
		}
		
		mlpe = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, MapLinkProtoElement.COLLECTIOR);
		if(mlpe == null)
		{
			mlpe = new MapLinkProtoElement(
				MapLinkProtoElement.COLLECTIOR,
				LangModelMap.getString("CollectorFragment"),
				"desc",
				new Dimension(2, 6));
			mlpe.setLineSize(4);
			mlpe.setColor(Color.DARK_GRAY);
			Pool.put(MapLinkProtoElement.typ, mlpe.getId(), mlpe);
		}

		List list = new LinkedList();
		
		for(Iterator it = Pool.getList(MapLinkProtoElement.typ).iterator(); it.hasNext();)
		{
			try
			{
				mlpe = (MapLinkProtoElement )it.next();
				list.add(mlpe);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		list.remove(getUnboundPen());
		
		return list;
	}
	
	protected MapLinkProtoElement getDefaultPen()
	{
		return (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, MapLinkProtoElement.TUNNEL);
	}

	protected MapLinkProtoElement getDefaultCable()
	{
		MapLinkProtoElement mlpe = null;
		
		mlpe = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, MapLinkProtoElement.UNBOUND);
		if(mlpe == null)
		{
			mlpe = new MapLinkProtoElement(
				MapLinkProtoElement.UNBOUND,
				LangModelMap.getString("Unbound"),
				"desc",
				new Dimension(0, 0));
			mlpe.setLineSize(1);
			mlpe.setColor(Color.RED);
			Pool.put(MapLinkProtoElement.typ, mlpe.getId(), mlpe);
		}
		
		return mlpe;
	}

	public List getTopologicalProtos()
	{
		MapNodeProtoElement mnpe = null;
		
		mnpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, MapNodeProtoElement.ATS);
		if(mnpe == null)
		{
			ImageCatalogue.add(
				"ats",
				new ImageResource("ats", "ats", "images/ats.gif"));

			mnpe = new MapNodeProtoElement(
				MapNodeProtoElement.ATS,
				LangModelMap.getString("Ats"),
				true,
				"ats",
				"description");
			Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);
		}

		mnpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, MapNodeProtoElement.BUILDING);
		if(mnpe == null)
		{
			ImageCatalogue.add(
				"building",
				new ImageResource("building", "building", "images/building.gif"));

			mnpe = new MapNodeProtoElement(
				MapNodeProtoElement.BUILDING,
				LangModelMap.getString("Building"),
				true,
				"building",
				"description");
			Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);
		}

		mnpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, MapNodeProtoElement.PIQUET);
		if(mnpe == null)
		{
			ImageCatalogue.add(
				"piquet",
				new ImageResource("piquet", "piquet", "images/piquet.gif"));

			mnpe = new MapNodeProtoElement(
				MapNodeProtoElement.PIQUET,
				LangModelMap.getString("Piquet"),
				true,
				"piquet",
				"description");
			Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);
		}

		mnpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, MapNodeProtoElement.WELL);
		if(mnpe == null)
		{
			ImageCatalogue.add(
				"well",
				new ImageResource("well", "well", "images/well.gif"));

			mnpe = new MapNodeProtoElement(
				MapNodeProtoElement.WELL,
				LangModelMap.getString("Well"),
				true,
				"well",
				"description");
			Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);
		}

		mnpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, MapNodeProtoElement.CABLE_INLET);
		if(mnpe == null)
		{
			ImageCatalogue.add(
				"cableinlet",
				new ImageResource("cableinlet", "cableinlet", "images/cableinlet.gif"));

			mnpe = new MapNodeProtoElement(
				MapNodeProtoElement.CABLE_INLET,
				LangModelMap.getString("CableInlet"),
				true,
				"cableinlet",
				"description");
			Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);
		}

		List list = new LinkedList();
		
		for(Iterator it = Pool.getList(MapNodeProtoElement.typ).iterator(); it.hasNext();)
		{
			try
			{
				mnpe = (MapNodeProtoElement )it.next();
				if(mnpe.isTopological())
					list.add(mnpe);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		list.remove(getUnboundProto());
		
		return list;
	}
}
