/**
 * $Id: LogicalNetLayer.java,v 1.9 2004/10/05 12:55:58 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;

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

/**
 * Управляет отображением логической структуры сети.
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/10/05 12:55:58 $
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
	public boolean performProcessing = true;
	
	/**
	 * Текущий элемент
	 */
	protected MapElement currentMapElement = null;

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
					if (aContext.getApplicationModel().isEnabled("mapActionIndication"))
				{
					animateThread = new AnimateThread(this);
					animateThread.start();
				}
		}

		this.mapView.setLogicalNetLayer(this);

		//Поумолчанию текущий элемент Void
		currentMapElement = VoidMapElement.getInstance(this.mapView);
		
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
	 * геттер
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

		if (getMapState().getShowMode() == MapState.SHOW_TRANSMISSION_PATH)
		{
			elementsToDisplay.addAll(getMapView().getMap().getPhysicalLinks());
			for(Iterator it = getMapView().getMeasurementPaths().iterator(); it.hasNext();)
			{
				MapMeasurementPathElement mpath = 
					(MapMeasurementPathElement )it.next();
				mpath.paint(g, visibleBounds);
				for(Iterator it2 = mpath.getCablePaths().iterator(); it2.hasNext();)
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

		Iterator e = getMapView().getMap().getNodes().iterator();
		while (e.hasNext())
		{
			MapNodeElement curNode = (MapNodeElement )e.next();
			if ( curNode instanceof MapSiteNodeElement)
			{
				if ( aContext.getApplicationModel().isEnabled("mapActionShowEquipment"))
				{
					curNode.paint(pg, visibleBounds);
				}
			}
			if ( curNode instanceof MapPhysicalNodeElement)
			{
				if ( MapPropertiesManager.isShowPhysicalNodes())
				{
					curNode.paint(pg, visibleBounds);
				}
			}
			if ( curNode instanceof MapMarkElement)
			{
				if (aContext.getApplicationModel().isEnabled("mapActionMarkShow"))
				{
//					MapMarkElement mme = (MapMarkElement )curNode;
//					mme.moveToFromStartLt(mme.getDistance());
					curNode.paint(pg, visibleBounds);
				}
			}
		}

		e = getMapView().getMarkers().iterator();
		while (e.hasNext())
		{
			MapMarker marker = (MapMarker)e.next();
			marker.paint(pg, visibleBounds);
		}
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
		if(ae.getActionCommand().equals(MapEvent.MAP_CHANGED))
		{
			repaint();
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )ae;
/*
			//Здесь принимаюттся собитыя по создению и управлению маркером
			if(mne.DATA_MARKER_CREATED)
			{
//				System.out.println("event_DATA_MARKER_CREATED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);

				MapTransmissionPathElement path = null;
				SchemePath the_sp = null;

				for(Iterator it = getMapView().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
						{
							TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
							if(tp != null && tp.monitored_element_id != null)
								if(tp.monitored_element_id.equals(mne.meID))
								{
									path = mappath;
									the_sp = sp;
			//						break;
								}
						}
						if(sp != null && sp.getId().equals(mne.mappathID))
						{
							path = mappath;
							the_sp = sp;
	//						break;
						}
					}
				}

				MapMarker marker;
				if(path != null)
				{
					marker = new MapMarker(
						mne.marker_id,
	                    lnl().getMap(),
	                    new Rectangle(14, 14),
						"",
						mne.distance,
						path);
					lnl().getMap().markers.add(marker);
//					marker.spd = mne.spd;
//					marker.spd.setSchemePath(the_sp);
					marker.moveToFromStartLo(mne.distance);
				}
			}
			if(mne.DATA_EVENTMARKER_CREATED)
			{
//				System.out.println("event_DATA_EVENTMARKER_CREATED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);

				MapTransmissionPathElement path = null;
				SchemePath the_sp = null;

				for(Iterator it = lnl().getMap().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
						{
							TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
							if(tp.monitored_element_id != null)
								if(tp.monitored_element_id.equals(mne.meID))
								{
									path = mappath;
									the_sp = sp;
			//						break;
								}
						}
						if(sp != null && sp.getId().equals(mne.mappathID))
						{
							path = mappath;
							the_sp = sp;
	//						break;
						}
					}
				}

				MapEventMarker marker;
				if(path != null)
				{
					marker = new MapEventMarker(
						mne.marker_id,
	                    lnl().getMap(),
	                    new Rectangle(14, 14),
						"",
						mne.distance,
						path);
					marker.descriptor = mne.descriptor;
					lnl().getMap().markers.add(marker);
//					marker.spd = mne.spd;
//					marker.spd.setSchemePath(the_sp);
					marker.moveToFromStartLo(mne.distance);
				}
			}
			if(mne.DATA_ALARMMARKER_CREATED)
			{
//				System.out.println("event_DATA_ALARMMARKER_CREATED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);

				MapTransmissionPathElement path = null;
				SchemePath the_sp = null;

				for(Iterator it = lnl().getMap().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
						{
							TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
							if(tp.monitored_element_id != null)
								if(tp.monitored_element_id.equals(mne.meID))
								{
									path = mappath;
									the_sp = sp;
			//						break;
								}
						}
						if(sp != null && sp.getId().equals(mne.mappathID))
						{
							path = mappath;
							the_sp = sp;
	//						break;
						}
					}
				}

				MapAlarmMarker marker = null;
				if(path != null)
				{
					for(Iterator it = lnl().getMap().markers.iterator(); it.hasNext();)
					{
						try
						{
							marker = (MapAlarmMarker )it.next();
							if(marker.path_id.equals(path.getId()))
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
							mne.marker_id,
							lnl().getMap(),
							new Rectangle(14, 14),
							"",
							mne.distance,
							path,
							mne.linkID);
						marker.descriptor = mne.descriptor;
						lnl().getMap().markers.add(marker);
					}
					else
					{
						marker.id = mne.marker_id;
					}
//					marker.spd = mne.spd;
//					marker.spd.setSchemePath(the_sp);
					marker.moveToFromStartLo(mne.distance);
				}

				boolean found = false;

				MapPhysicalLinkElement link = findMapLinkByCableLink(mne.linkID);
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
			}
			if(mne.DATA_MARKER_MOVED)
			{
//				System.out.println("event_DATA_MARKER_MOVED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMap().getMarker(mne.marker_id);
				if(marker != null)
				{
					if(marker.spd == null)
						marker.spd = (SchemePathDecompositor )mne.spd;
					marker.moveToFromStartLo(mne.distance);
				}
			}
			if(mne.DATA_MARKER_SELECTED)
			{
//				System.out.println("event_DATA_MARKER_SELECTED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMap().getMarker(mne.marker_id);
				if(marker != null)
					marker.getMessage_Marker_Selected();
			}
			if(mne.DATA_MARKER_DESELECTED)
			{
//				System.out.println("event_DATA_MARKER_DESELECTED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMap().getMarker(mne.marker_id);
				if(marker != null)
					marker.getMessage_Marker_Deselected();
			}
			if(mne.DATA_MARKER_DELETED)
			{
//				System.out.println("event_DATA_MARKER_DELETED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMap().getMarker(mne.marker_id);
				if(marker != null)
					lnl().getMap().markers.remove(marker);
			}
			if(mne.DATA_EVENTMARKER_DELETED)
			{
//				System.out.println("event_DATA_EVENTMARKER_DELETED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapMarker marker = lnl().getMap().getMarker(mne.marker_id);
				if(marker != null)
					lnl().getMap().markers.remove(marker);
			}
			if(mne.DATA_ALARMMARKER_DELETED)
			{
//				System.out.println("event_DATA_ALARMMARKER_DELETED id " + 
//					mne.marker_id + " ME_ID " + mne.meID + " MPATH_ID " + mne.mappathID);
				MapAlarmMarker marker = (MapAlarmMarker )lnl().getMap().getMarker(mne.marker_id);
				if(marker != null)
				{
					lnl().getMap().markers.remove(marker);

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
				}
			}
*/
/*
			if(mne.MAP_PATH_SELECTED)
				if(lnl().performProcessing)
				{
//					System.out.println("event_DATA_MAP_PATH_SELECTED");
					MapTransmissionPathElement mtpe = 
							lnl().getMap().getMapTransmissionPathElement(mne.mappathID);
					if(mtpe != null)
						mtpe.select();
				}
			if(mne.MAP_PATH_DESELECTED)
				if(lnl().performProcessing)
				{
//					System.out.println("event_DATA_MAP_PATH_DESELECTED");
					MapTransmissionPathElement mtpe = 
							lnl().getMap().getMapTransmissionPathElement(mne.mappathID);
					if(mtpe != null)
						mtpe.deselect();
				}
*/
			if(mne.isMapElementSelected())
				if(performProcessing)
				{
					MapElement me = (MapElement )mne.getSource();
					if(me != null)
						me.setSelected(true);
				}
			if(mne.isMapElementDeselected())
				if(performProcessing)
				{
					MapElement me = (MapElement )mne.getSource();
					if(me != null)
						me.setSelected(false);
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
/*
		if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
			if(performProcessing)
		{
			SchemeNavigateEvent sne = (SchemeNavigateEvent )ae;

			if(sne.SCHEME_ELEMENT_SELECTED)
			{
//				System.out.println("SCHEME_ELEMENT_SELECTED");
				SchemeElement[] ses = (SchemeElement[] )sne.getSource();
				Scheme s = (Scheme )Pool.get(Scheme.typ, this.getMap().scheme_id);

				for(Iterator it = lnl().getMap().getNodes().iterator(); it.hasNext();)
				{
					Object obj = it.next();
					if(	obj instanceof MapSiteNodeElement )
					{
						MapSiteNodeElement mape = (MapSiteNodeElement )obj;
						for(int i = 0; i < ses.length; i++)
						{
							SchemeElement se = s.getTopologicalElement(ses[i].getId());
							if(mape.element_id.equals(se.getId()))
								mape.select();
						}
					}
				}
			}

			if(sne.SCHEME_PATH_SELECTED)
			{
//				System.out.println("SCHEME_PATH_SELECTED");
				SchemePath[] sps = (SchemePath[] )sne.getSource();
				for(Iterator it = lnl().getMap().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					for(int i = 0; i < sps.length; i++)
						if(mappath.PATH_ID.equals(sps[i].getId()))
							mappath.select();
				}
			}

			if(sne.SCHEME_CABLE_LINK_SELECTED)
			{
//				System.out.println("SCHEME_CABLE_LINK_SELECTED");
				SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
				for(Iterator it = lnl().getMap().getPhysicalLinks().iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
					for(int i = 0; i < scs.length; i++)
						if(link.LINK_ID.equals(scs[i].getId()))
							link.select();
				}
			}

			if(sne.SCHEME_ELEMENT_DESELECTED)
			{
//				System.out.println("SCHEME_ELEMENT_DESELECTED");
				SchemeElement[] ses = (SchemeElement[] )sne.getSource();
				Scheme s = (Scheme )Pool.get(Scheme.typ, this.getMap().scheme_id);

				for(Iterator it = lnl().getMap().getNodes().iterator(); it.hasNext();)
				{
					Object obj = it.next();
					if(	obj instanceof MapSiteNodeElement )
					{
						MapSiteNodeElement mape = (MapSiteNodeElement )obj;
						for(int i = 0; i < ses.length; i++)
						{
							SchemeElement se = s.getTopologicalElement(ses[i].getId());
							if(mape.element_id.equals(se.getId()))
								mape.deselect();
						}
					}
				}
			}

			if(sne.SCHEME_PATH_DESELECTED)
			{
//				System.out.println("SCHEME_PATH_DESELECTED");
				SchemePath[] sps = (SchemePath[] )sne.getSource();
				for(Iterator it = lnl().getMap().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					for(int i = 0; i < sps.length; i++)
						if(mappath.PATH_ID.equals(sps[i].getId()))
							mappath.deselect();
				}
			}

			if(sne.SCHEME_CABLE_LINK_DESELECTED)
			{
//				System.out.println("SCHEME_CABLE_LINK_DESELECTED");
				SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
				for(Iterator it = lnl().getMap().getPhysicalLinks().iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
					for(int i = 0; i < scs.length; i++)
						if(link.LINK_ID.equals(scs[i].getId()))
							link.deselect();
				}
			}

			lnl().postDirtyEvent();
			lnl().postPaintEvent();
		}
		if(ae.getActionCommand().equals(CatalogNavigateEvent.type))
			if(lnl().performProcessing)
		{
			CatalogNavigateEvent cne = (CatalogNavigateEvent )ae;

			if(cne.CATALOG_EQUIPMENT_SELECTED)
			{
//				System.out.println("CATALOG_EQUIPMENT_SELECTED");
			}

			if(cne.CATALOG_PATH_SELECTED)
			{
//				System.out.println("CATALOG_PATH_SELECTED");
				TransmissionPath[] tps = (TransmissionPath[] )cne.getSource();
				for(Iterator it = lnl().getMap().getTransmissionPath().iterator(); it.hasNext();)
				{
					MapTransmissionPathElement mappath = (MapTransmissionPathElement )it.next();
					if(mappath.PATH_ID != null && !mappath.PATH_ID.equals(""))
					{
						SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, mappath.PATH_ID);
						if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
							for(int i = 0; i < tps.length; i++)
								if(sp.path_id.equals(tps[i].getId()))
									mappath.select();
					}
				}
			}

			if(cne.CATALOG_CABLE_LINK_SELECTED)
			{
//				System.out.println("CATALOG_CABLE_LINK_SELECTED");
				CableLink[] cs = (CableLink[] )cne.getSource();
				for(Iterator it = lnl().getMap().getPhysicalLinks().iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
					if(link.LINK_ID != null && !link.LINK_ID.equals(""))
					{
						SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
						if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
							for(int i = 0; i < cs.length; i++)
								if(scl.cable_link_id.equals(cs[i].getId()))
									link.select();
					}
				}
			}
			lnl().postDirtyEvent();
			lnl().postPaintEvent();
		}
		if(ae.getActionCommand().equals("placeElement"))
		{
			MapSchemeElementLabel el = (MapSchemeElementLabel )ae.getSource();
			if(el.sElement instanceof SchemeElement)
			{
				SchemeElement se = (SchemeElement )el.sElement;
//				lnl().placeElement(se, se.mpe, new SxDoublePoint(se.getLong(), se.getLat()));
			}
			else
			if(el.sElement instanceof SchemeCableLink)
			{
				SchemeCableLink scl = (SchemeCableLink )el.sElement;
				lnl().placeElement(scl, scl.mplpe);
			}
			else
			if(el.sElement instanceof SchemePath)
			{
				SchemePath sp = (SchemePath )el.sElement;
				lnl().placeElement(sp, sp.mtppe);
			}
			lnl().postDirtyEvent();
			lnl().postPaintEvent();
		}
*/
	}

	/**
	 * 
	 * @param link_id
	 * @return 
	 */
	public MapSiteNodeElement findMapElementByCableLink(String link_id)
	{
/*
		SchemeElement se = null;
		Scheme sc = (Scheme )Pool.get(Scheme.typ, this.getMap().scheme_id);
		Hashtable ht = Pool.getHash(SchemeCableLink.typ);
		if(ht != null)
			for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
			{
				SchemeCableLink scl = (SchemeCableLink )enum.nextElement();
				if(scl != null)
					if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
						if(scl.cable_link_id.equals(link_id))
						{
							se = sc.getSchemeElementByCablePort(scl.source_port_id);
							se = sc.getTopologicalElement(se);
//							se = sc.getTopLevelElement(se);
							break;
						}
			}
		if(se == null)
		{
			Hashtable ht2 = Pool.getHash(SchemeLink.typ);
			if(ht2 != null)
				for(Enumeration enum = ht2.elements(); enum.hasMoreElements();)
				{
					SchemeLink sl = (SchemeLink )enum.nextElement();
					if(sl != null)
						if(sl.link_id != null && !sl.link_id.equals(""))
							if(sl.link_id.equals(link_id))
							{
								se = sc.getSchemeElementByPort(sl.source_port_id);
								se = sc.getTopLevelElement(se);
								break;
							}
				}
		}
		if(se != null)
		{
			for(Iterator it = lnl().getMap().getMapSiteNodeElements().iterator(); it.hasNext();)
			{
				MapSiteNodeElement node = (MapSiteNodeElement )it.next();
				if(node.element_id != null && node.element_id.equals(se.getId()))
					return node;
			}
		}
*/
		return null;
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

		Dispatcher disp = getContext().getDispatcher();
		if(disp != null)
		{
			performProcessing = false;
/*
			// разобраться, почему сообщение о пути посылается отдельно от остальных!!!
			if(mapElement instanceof MapTransmissionPathElement)
			{
				MapNavigateEvent mne = new MapNavigateEvent(mc, MapNavigateEvent.MAP_PATH_SELECTED_EVENT);
				mne.mappathID = path.getId();
				dispatcher.notify(mne);
			}
			else
*/
			disp.notify(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			performProcessing = true;
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
	 * Генерация сообщения о выборке элемента каталога
	 */
	public void notifyCatalogueEvent(MapElement mapElement)
	{
/*
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "notifyCatalogueEvent(" + mapElement + ")");
		
		CatalogNavigateEvent cne;
		Dispatcher dispatcher = logicalNetLayer.mapMainFrame.aContext.getDispatcher();
		try 
		{
			MapSiteNodeElement mapel = (MapSiteNodeElement )mapElement;

			if(mapel.element_id != null && !mapel.element_id.equals(""))
			{
				SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mapel.element_id);
				
				if(se.equipment_id != null && !se.equipment_id.equals(""))
				{
					Equipment eq = (Equipment )Pool.get(Equipment.typ, se.equipment_id);
//					System.out.println("notify CATALOG_EQUIPMENT_SELECTED_EVENT " + eq.getId());
					cne = new CatalogNavigateEvent(
						new Equipment[] { eq },
						CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT);
					dispatcher.notify(cne);
					return;
				}
			}
		} 
		catch (Exception ex){ }

		try
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )mapElement;

			if(link.LINK_ID != null && !link.LINK_ID.equals(""))
			{
				SchemeCableLink scl = (SchemeCableLink )Pool.get(SchemeCableLink.typ, link.LINK_ID);
				
				if(scl.cable_link_id != null && !scl.cable_link_id.equals(""))
				{
					CableLink cl = (CableLink )Pool.get(CableLink.typ, scl.cable_link_id);
//					System.out.println("notify CATALOG_EQUIPMENT_SELECTED_EVENT " + cl.getId());
					cne = new CatalogNavigateEvent(
						new CableLink[] { cl },
						CatalogNavigateEvent.CATALOG_CABLE_LINK_SELECTED_EVENT);
					dispatcher.notify(cne);
					return;
				}
			}
		} 
		catch (Exception ex){ }

		try 
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )mapElement;

			if(path.PATH_ID != null && !path.PATH_ID.equals(""))
			{
				SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
				if(sp.path_id != null && !sp.path_id.equals(""))
				{
					TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
//					System.out.println("notify CATALOG_PATH_SELECTED_EVENT " + tp.getId());
					cne = new CatalogNavigateEvent(
						new TransmissionPath[] { tp }, 
						CatalogNavigateEvent.CATALOG_PATH_SELECTED_EVENT);
					dispatcher.notify(cne);
				}
			}
		} 
		catch (Exception ex){ } 
*/
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

		if(curMapElement instanceof VoidMapElement)
			return;
		curMapElement.setSelected(true);
		notifyMapEvent(currentMapElement);
		notifySchemeEvent(currentMapElement);
		notifyCatalogueEvent(currentMapElement);
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

		//Здесь пробегаемся по всем элементам и если на каком-нибудь из них курсор
		//то устанавливаем его текущим элементом
		Iterator e = getMapView().getAllElements().iterator();
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
						curME = getMapView().getMap().getPhysicalLink( 
							((MapNodeLinkElement )mapElement).getPhysicalLinkId());
					}
					else
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = getMapView().getCablePaths((MapNodeLinkElement )mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MapCablePathElement)it.next();
						}
						else
						{
							curME = getMapView().getMap().getPhysicalLink( 
								((MapNodeLinkElement )mapElement).getPhysicalLinkId());
						}
					}
				}
				else
				if ( mapElement instanceof MapPhysicalLinkElement)
				{
					if ( showMode == MapState.SHOW_CABLE_PATH)
					{
						Iterator it = getMapView().getCablePaths((MapPhysicalLinkElement )mapElement).iterator();
						if(it.hasNext())
						{
							curME = (MapCablePathElement)it.next();
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
	public List getSelectedElements()
	{
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
