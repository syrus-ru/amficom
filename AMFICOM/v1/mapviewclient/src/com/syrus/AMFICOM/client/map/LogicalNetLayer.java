/**
 * $Id: LogicalNetLayer.java,v 1.33 2005/01/12 15:32:34 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.mapview.AlarmMarker;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.EventMarker;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.Selection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import com.syrus.AMFICOM.Client.Map.mapview.VoidElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.PathDecompositor;
import com.syrus.AMFICOM.scheme.corba.*;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 * Управляет отображением логической структуры сети.
 * 
 * 
 * 
 * @version $Revision: 1.33 $, $Date: 2005/01/12 15:32:34 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class LogicalNetLayer implements MapCoordinatesConverter
{
	protected CommandList commandList = new CommandList(20);
	
	protected MapViewController mapViewController;

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
	protected AbstractNode fixedNode = null;

	protected List fixedNodeList = new LinkedList();

	/**
	 * Текущий тип создаваемых физических линий
	 */
	protected PhysicalLinkType currentPen = null;

	protected SiteNodeType unboundProto = null;

	protected PhysicalLinkType unboundLinkProto = null;
	
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
	public abstract Point convertMapToScreen(DoublePoint point);
	
	/**
	 * Получить географические координаты по экранным
	 */
	public abstract DoublePoint convertScreenToMap(Point point);

	/**
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public abstract double distance(DoublePoint from, DoublePoint to);

	/**
	 * Установить центральную точку вида карты
	 */
	public abstract void setCenter(DoublePoint center);

	/**
	 * Получить центральную точку вида карты
	 */
	public abstract DoublePoint getCenter();

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
	public abstract void repaint(boolean fullRepaint);
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public abstract void setCursor(Cursor cursor);

	public abstract Cursor getCursor();

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
	public abstract void zoomToBox(DoublePoint from, DoublePoint to);

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
		
//		double sF = getDefaultScale() / getCurrentScale();

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
//				curNode.setScaleCoefficient(sF);
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
			System.out.println("mapView null!");
//			try
//			{
//				this.mapView = new MapView(this, null);
//			}
//			catch (CreateObjectException e)
//			{
//				
//			}
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
		currentMapElement = com.syrus.AMFICOM.Client.Map.mapview.VoidElement.getInstance(this.mapView);

		commandList.flush();

		repaint(true);
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
		
//		try
//		{
//			setMapView(new MapView(this, map));
//		}
//		catch (CreateObjectException e)
//		{
//			
//		}
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
		DoublePoint p = this.convertScreenToMap(point);
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
		drawSelection(p);
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
//				mple.paint(g, visibleBounds);
			}
		}
		else
		if (getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			elementsToDisplay.addAll(getMapView().getMap().getPhysicalLinks());
			for(Iterator it = getMapView().getCablePaths().iterator(); it.hasNext();)
			{
				CablePath cpath = 
					(CablePath)it.next();
				getMapViewController().getController(cpath).paint(cpath, g, visibleBounds);
//				cpath.paint(g, visibleBounds);
				elementsToDisplay.removeAll(cpath.getLinks());
			}
			for(Iterator it = elementsToDisplay.iterator(); it.hasNext();)
			{
				PhysicalLink mple = 
					(PhysicalLink)it.next();
				getMapViewController().getController(mple).paint(mple, g, visibleBounds);
//				mple.paint(g, visibleBounds);
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
//				mple.paint(g, visibleBounds);
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
//				curNodeLink.paint(p, visibleBounds);
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
			AbstractNode curNode = (AbstractNode)e.next();
			if(curNode instanceof TopologicalNode)
			{
				if(showNodes)
				{
					getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
//					curNode.paint(pg, visibleBounds);
				}
			}
			else
				getMapViewController().getController(curNode).paint(curNode, g, visibleBounds);
//				curNode.paint(pg, visibleBounds);
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

	public void drawSelection(Graphics g)
	{
		Graphics2D pg = (Graphics2D )g;

		Rectangle2D.Double visibleBounds = this.getVisibleBounds();

		Iterator e = getMapView().getMap().getSelectedElements().iterator();
		while (e.hasNext())
		{
			MapElement el = (MapElement)e.next();
			getMapViewController().getController(el).paint(el, g, visibleBounds);
//			el.paint(pg, visibleBounds);
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
			repaint(false);
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_ELEMENT_CHANGED))
		{
			Object me = ae.getSource();
			if(me instanceof SchemeElement)
			{
				getMapView().scanElement((SchemeElement )me);
				getMapView().scanCables(((SchemeElement )me).scheme());
			}
			else
			if(me instanceof SchemeCableLink)
			{
				getMapView().scanCable((SchemeCableLink )me);
				getMapView().scanPaths(((SchemeCableLink )me).scheme());
			}
			else
			if(me instanceof CablePath)
			{
				getMapView().scanCable(((CablePath)me).getSchemeCableLink());
				getMapView().scanPaths(((CablePath)me).getSchemeCableLink().scheme());
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
				MeasurementPath path = mapView.getMeasurementPathByMonitoredElementId(mne.getMeId());

				if(path != null)
				{
					Marker marker = new Marker(
						mne.getMarkerId(),
	                    getMapView(),
						mne.getDistance(),
						path,
						mne.getMeId());
					getMapView().addMarker(marker);

					MarkerController mc = (MarkerController)getMapViewController().getController(marker);
					mc.moveToFromStartLo(marker, mne.getDistance());
				}
			}
			else
			if(mne.isDataEventMarkerCreated())
			{
				MeasurementPath path = mapView.getMeasurementPathByMonitoredElementId(mne.getMeId());

				if(path != null)
				{
					EventMarker marker = new EventMarker(
						mne.getMarkerId(),
	                    getMapView(),
						mne.getDistance(),
						path,
						mne.getMeId());
//					marker.descriptor = mne.descriptor;
					getMapView().addMarker(marker);

					MarkerController mc = (MarkerController)getMapViewController().getController(marker);

					mc.moveToFromStartLo(marker, mne.getDistance());
				}
			}
			else
			if(mne.isDataAlarmMarkerCreated())
			{
				MeasurementPath path = mapView.getMeasurementPathByMonitoredElementId(mne.getMeId());

				AlarmMarker marker = null;
				if(path != null)
				{
					for(Iterator it = getMapView().getMarkers().iterator(); it.hasNext();)
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
						getMapView().addMarker(marker);
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
				Marker marker = getMapView().getMarker(mne.getMarkerId());
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
				Marker marker = getMapView().getMarker(mne.getMarkerId());
				if(marker != null)
					marker.setSelected(true);
			}
			else
			if(mne.isDataMarkerDeselected())
			{
				Marker marker = getMapView().getMarker(mne.getMarkerId());
				if(marker != null)
					marker.setSelected(false);
			}
			else
			if(mne.isDataMarkerDeleted())
			{
				Marker marker = getMapView().getMarker(mne.getMarkerId());
				if(marker != null)
					getMapView().removeMarker(marker);
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
						SiteNode site = getMapView().findElement(ses[i]);
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
						MeasurementPath mmp = getMapView().findMeasurementPath(sps[i]);
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
						CablePath mcp = getMapView().findCablePath(scs[i]);
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
						SiteNode site = getMapView().findElement(ses[i]);
						if(site != null)
							site.setSelected(false);
					}
				}

				if(sne.SCHEME_PATH_DESELECTED)
				{
					SchemePath[] sps = (SchemePath[] )sne.getSource();
	
					for(int i = 0; i < sps.length; i++)
					{
						MeasurementPath mmp = getMapView().findMeasurementPath(sps[i]);
						if(mmp != null)
							mmp.setSelected(false);
					}
				}
	
				if(sne.SCHEME_CABLE_LINK_DESELECTED)
				{
					SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
					for(int i = 0; i < scs.length; i++)
					{
						CablePath mcp = getMapView().findCablePath(scs[i]);
						if(mcp != null)
							mcp.setSelected(false);
					}
				}
	
				repaint(false);
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
	public AbstractNode getFixedNode()
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
			boolean canFixDist = (curMapElement instanceof TopologicalNode)
				|| (curMapElement instanceof SiteNode);
			if(getContext().getApplicationModel().isEnabled(
				MapApplicationModel.OPERATION_MOVE_FIXED) != canFixDist)
			{
				if(canFixDist)
				{
					fixedNode = (AbstractNode)curMapElement;
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
	 * Получить текущий элемент по экранной коордитате на карте
	 */
	public MapElement getMapElementAtPoint(Point point)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMapElementAtPoint(" + point + ")");
		
		int showMode = getMapState().getShowMode();
		MapElement curME = com.syrus.AMFICOM.Client.Map.mapview.VoidElement.getInstance(this.getMapView());

		Rectangle2D.Double visibleBounds = this.getVisibleBounds();
		
		MapView mapView = getMapView();
		Map map = mapView.getMap();

		//Здесь пробегаемся по всем элементам и если на каком-нибудь из них курсор
		//то устанавливаем его текущим элементом
		Iterator e = mapView.getAllElements().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			MapElementController controller = getMapViewController().getController(mapElement);
//			if(mapElement.isVisible(visibleBounds))
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
						Iterator it = mapView.getCablePaths((NodeLink)mapElement).iterator();
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
						Iterator it = mapView.getMeasurementPaths((NodeLink)mapElement).iterator();
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
						Iterator it = mapView.getCablePaths((PhysicalLink)mapElement).iterator();
						if(it.hasNext())
						{
							curME = (CablePath)it.next();
						}
					}
					else
					if ( showMode == MapState.SHOW_MEASUREMENT_PATH)
					{
						Iterator it = mapView.getMeasurementPaths((PhysicalLink)mapElement).iterator();
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
	 * Отменить выбор всем элементам
	 */
	public void deselectAll()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");

		Iterator e = getMapView().getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
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
			MapElement curElement = (MapElement)e.next();
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
			MapElement curElement = (MapElement)e.next();
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
	
	public void setNodeLinkSizeFrom(NodeLink nodelink, AbstractNode node, double dist)
	{
		DoublePoint anchor1 = node.getLocation();
		
		MoveNodeCommand cmd = new MoveNodeCommand(node);
		cmd.setLogicalNetLayer(this);
		
		((NodeLinkController)getMapViewController().getController(nodelink))
			.setSizeFrom(nodelink, node, dist);
//		nodelink.setSizeFrom(node, dist);

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
	 * Выполнить удаление выбранных элементов
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
	 * Отменить последнюю выполненную команду пользователя
	 */
	public void undo()
	{
		commandList.undo();
		repaint(false);
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
	/**
	 * Повторить отмененную команду прользователя
	 */
	public void redo()
	{
		commandList.redo();
		repaint(false);
		sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
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
	
	public void setPen(PhysicalLinkType pen)
	{
		this.currentPen = pen;
	}
/*
	public Identifier getImageId(String codename, String filename)
	{
		try 
		{
			StringFieldCondition condition = new StringFieldCondition(
				String.valueOf(ImageResourceSort._FILE),
				ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
				StringFieldSort.STRINGSORT_INTEGER);
			List bitMaps = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);

			for (Iterator it = bitMaps.iterator(); it.hasNext(); ) 
			{
				FileImageResource ir = (FileImageResource )it.next();
				if(ir.getCodename().equals(codename))
					return ir.getId();
				
			}
		}
		catch (ApplicationException ex) 
		{
			ex.printStackTrace();
		}
		try
		{
			Identifier creatorId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);
			FileImageResource ir = FileImageResource.createInstance(
				creatorId,
				filename);
			ResourceStorableObjectPool.putStorableObject(ir);
			return ir.getId();
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public SiteNodeType getSiteNodeType(
			String codename)
	{
		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			codename,
			ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		try
		{
			List pTypes =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				SiteNodeType type = (SiteNodeType )it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching ParameterType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
			Identifier creatorId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);
			SiteNodeType snt = SiteNodeType.createInstance(
				creatorId,
				codename,
				LangModelMap.getString(codename),
				"",
				getImageId(codename, NodeTypeController.getImageFileName(codename)),
				true);
				
			MapStorableObjectPool.putStorableObject(snt);
			return snt;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
*/
/*
	public PhysicalLinkType getPhysicalLinkType(
			String codename)
	{
		StorableObjectCondition pTypeCondition = new StringFieldCondition(
			codename,
			ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE,
			StringFieldSort.STRINGSORT_BASE);

		try
		{
			List pTypes =
				MapStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				PhysicalLinkType type = (PhysicalLinkType )it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching ParameterType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
			LinkTypeController ltc = (LinkTypeController)LinkTypeController.getInstance();
			ltc.setLogicalNetLayer(this);

			Identifier creatorId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().user_id);

			PhysicalLinkType pType = PhysicalLinkType.createInstance(
				creatorId,
				codename,
				LangModelMap.getString(codename),
				"",
				LinkTypeController.getBindDimension(codename));

			ltc.setLineSize(pType, LinkTypeController.getLineThickness(codename));
			ltc.setColor(pType, LinkTypeController.getLineColor(codename));

			MapStorableObjectPool.putStorableObject(pType);
			return pType;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
*/
/*
	protected SiteNodeType getDefaultUnboundProto()
	{
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		return NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.UNBOUND);
	}
*/
/*
	public List getPens()
	{
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		PhysicalLinkType mlpe = null;

		mlpe = LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.TUNNEL);
		mlpe = LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.COLLECTOR);

		List list = null;
		try
		{
			list =
				MapStorableObjectPool.getStorableObjectsByConditionButIds(null, null, true);

			list.remove(getUnboundPen());
		}
		catch(Exception e)
		{
			list = new LinkedList();
		}
		
		return list;
	}
	
	protected PhysicalLinkType getDefaultPen()
	{
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);
		return LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.TUNNEL);
	}

	protected PhysicalLinkType getDefaultCable()
	{
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);
		return LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.UNBOUND);
	}
*/
/*	
	List topologicalProtos = new LinkedList();

	public List getTopologicalProtos()
	{
		SiteNodeType mnpe = null;

		topologicalProtos.clear();
		
		Identifier creatorId = new Identifier(
			aContext.getSessionInterface().getAccessIdentifier().user_id);

		topologicalProtos.add(NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.ATS));
		topologicalProtos.add(NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.BUILDING));
		topologicalProtos.add(NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.PIQUET));
		topologicalProtos.add(NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.WELL));
		topologicalProtos.add(NodeTypeController.getSiteNodeType(creatorId, SiteNodeType.CABLE_INLET));

		try
		{
			List list2 =
				MapStorableObjectPool.getStorableObjectsByConditionButIds(null, null, true);

			for(Iterator it = list2.iterator(); it.hasNext();)
			{
				mnpe = (SiteNodeType )it.next();
				if(mnpe.isTopological())
					topologicalProtos.add(mnpe);
			}
		}
		catch(Exception e)
		{
		}
		
		topologicalProtos.remove(getUnboundProto());
		return topologicalProtos;
	}
*/
	public MapViewController getMapViewController()
	{
		return com.syrus.AMFICOM.Client.Map.Controllers.MapViewController.getInstance(this);
	}
}
