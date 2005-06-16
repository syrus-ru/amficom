/**
 * $Id: NetMapViewer.java,v 1.17 2005/06/16 10:57:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.map.ui.MapDropTargetListener;
import com.syrus.AMFICOM.client.map.ui.MapKeyAdapter;
import com.syrus.AMFICOM.client.map.ui.MapMouseListener;
import com.syrus.AMFICOM.client.map.ui.MapMouseMotionListener;
import com.syrus.AMFICOM.client.map.ui.MapToolTippedPanel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.EventMarker;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * Класс, являясь абстрактным, реализует в себе общую 
 * функциональность по управлению отображением картографической информации. 
 * Классы, управляющие отображением информации в том или ином формате 
 * (SpatialFX, MapInfo) наследуют этот класс и реализуют специфические
 * для формата операции отображения.
 * Для того, чтобы получить компонент, содержащий в себе отображение 
 * картографии, следует вызвать метод {@link #getVisualComponent()}
 * <br> реализация com.syrus.AMFICOM.client.map.objectfx.OfxNetMapViewer 
 * <br> реализация com.syrus.AMFICOM.client.map.mapinfo.MapInfoNetMapViewer
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/06/16 10:57:19 $
 * @module mapviewclient_v1
 */
public abstract class NetMapViewer {
	private LogicalNetLayer logicalNetLayer;
	private MapContext mapContext;
	private MapImageRenderer renderer;

	private Dimension lastVisCompSize;
	private Image mapShotImage;
	
	/** Флаг того, что контекстное меню отображено. */	
	protected boolean menuShown = false;

	protected DropTargetListener dtl;
	protected MapToolTippedPanel mttp;
	protected ToolTipManager ttm;
	protected MouseListener ml;
	protected MouseMotionListener mml;
	protected MapKeyAdapter mka;
	
	public NetMapViewer(
			LogicalNetLayer logicalNetLayer,
			MapContext mapContext,
			MapImageRenderer renderer) {
		this.logicalNetLayer = logicalNetLayer;
		this.mapContext = mapContext;
		this.renderer = renderer;
	}
	
	/**
	 * Инициализация класса отображения картографии. Базовое действие - 
	 * соединиться с хранилищем картографической информации. Для реализации
	 * других специфических действий по отображению топографической информации
	 * следует переопределить этот метод
	 * <br> реализация com.syrus.AMFICOM.client.map.objectfx.OfxNetMapViewer.init() 
	 * <br> реализация com.syrus.AMFICOM.client.map.mapinfo.MapInfoNetMapViewer.init()
	 */	
	public void init()
		throws MapDataException {
		this.dtl = new MapDropTargetListener(this);
		this.mttp = new MapToolTippedPanel(this);
		this.ttm = ToolTipManager.sharedInstance();
		this.ttm.registerComponent(this.mttp);
		this.ml = new MapMouseListener(this);
		this.mml = new MapMouseMotionListener(this);
		this.mka = new MapKeyAdapter(null);
	}

	/**
	 * Осуществляет сохранение текущик параметров отображения карты для 
	 * следующей сессии.
	 */
	public void saveConfig() {
		//empty
	}

	/**
	 * Получить логический слой.
	 * @return 
	 * логический слой
	 */
	public abstract LogicalNetLayer getLogicalNetLayer();

	/**
	 * Получить графический компонент, в котором отображается картография.
	 * @return компонент
	 */
	public abstract JComponent getVisualComponent();

	/**
	 * Получить видимую область в географических координатах.
	 * @return видимая область
	 */
	public abstract Rectangle2D.Double getVisibleBounds()
		throws MapConnectionException, MapDataException;

	/**
	 * Центрировать географический объект.
	 * @param so географический объект
	 */
	public abstract void centerSpatialObject(SpatialObject so)
		throws MapConnectionException, MapDataException;

	/**
	 * Перерисовать содержимое компонента с картой.
	 * @param fullRepaint производить ли полную перерисовку.
	 * <code>true</code> - перерисовываются географические объекты и элементы
	 * топологической схемы. <code>false</code> - перерисовываются только 
	 * элементы топологической схемы.
	 */
	public abstract void repaint(boolean fullRepaint)
		throws MapConnectionException, MapDataException;

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
	 * В режиме перемещения карты "лапкой" ({@link MapState#MOVE_HAND})
	 * передвинута мышь с нажатой клавишей.
	 * @param me мышиное событие
	 */	
	public abstract void handDragged(MouseEvent me)
		throws MapConnectionException, MapDataException;
	
	/**
	 * В режиме перемещения карты "лапкой" ({@link MapState#MOVE_HAND})
	 * передвинута мышь.
	 * @param me мышиное событие
	 */	
	public abstract void handMoved(MouseEvent me)
		throws MapConnectionException, MapDataException;

	/**
	 * В пустом режиме ({@link MapState#NULL_ACTION_MODE})
	 * передвинута мышь.
	 * @param me мышиное событие
	 */	
	public abstract void mouseMoved(MouseEvent me)
		throws MapConnectionException, MapDataException;

	/**
	 * Передать команду на отображение координат текущей точки в окне координат.
	 * @param point экранная координата мыши
	 */	
	public void showLatLong(Point point)
		throws MapConnectionException, MapDataException
	{
		LogicalNetLayer logicalNetLayer = this.getLogicalNetLayer();
		if(logicalNetLayer.aContext == null)
			return;
		Dispatcher disp = logicalNetLayer.aContext.getDispatcher();
		if(disp == null)
			return;
		DoublePoint doublePoint = logicalNetLayer.getConverter().convertScreenToMap(point);
		disp.firePropertyChange(new MapEvent(doublePoint, MapEvent.MAP_VIEW_CENTER_CHANGED));
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
	 * Получить снимок вида карты с отрисованными объектами.
	 * @return снимок
	 */
	public Image getMapShot() {
		JComponent component = getVisualComponent();
		int width = component.getWidth();
		int height = component.getHeight();
		if (this.lastVisCompSize == null) {
			this.lastVisCompSize = component.getSize();
			this.mapShotImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		}
		else if (!this.lastVisCompSize.equals(component.getSize()))
			this.mapShotImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		
		component.paint(this.mapShotImage.getGraphics());
		
		return this.mapShotImage;
	}

	public void propertyChange(PropertyChangeEvent pce)
	{
		try
		{
			if(pce.getPropertyName().equals(MapEvent.NEED_FULL_REPAINT))
			{
				repaint(true);
				return;
			}
			if(pce.getPropertyName().equals(MapEvent.NEED_REPAINT))
			{
				repaint(false);
				return;
			}

			if(!pce.getSource().equals(this.logicalNetLayer))
				return;

			MapView mapView = this.logicalNetLayer.getMapView();
			
			MapViewController mapViewController = this.logicalNetLayer.getMapViewController();

			if(pce.getPropertyName().equals(MapEvent.DESELECT_ALL))
			{
				this.logicalNetLayer.deselectAll();
			}
			else
			if(pce.getPropertyName().equals(MapEvent.MAP_CHANGED))
			{
				Set selectedElements = this.logicalNetLayer.getMapView().getMap().getSelectedElements();
				if(selectedElements.size() > 1)
				{
					Selection sel;
					if(! (this.logicalNetLayer.getCurrentMapElement() instanceof Selection))
					{
						sel = new Selection();
						this.logicalNetLayer.setCurrentMapElement(sel);
					}
					else
						sel = (Selection)this.logicalNetLayer.getCurrentMapElement();

					sel.clear();
					sel.addAll(selectedElements);
//					this.sendMapEvent(new MapEvent(sel, MapEvent.MAP_ELEMENT_SELECTED));
				}
				else
				if(selectedElements.size() == 1)
				{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
						MapElement me = (MapElement)selectedElements.iterator().next();
						this.logicalNetLayer.setCurrentMapElement(me);
//						this.sendMapEvent(new MapEvent(me, MapEvent.MAP_ELEMENT_SELECTED));
//				}
				}
				else
				//selectedElements.size() == 0
				{
//				if(getCurrentMapElement() instanceof MapSelection)
//				{
					this.logicalNetLayer.setCurrentMapElement(VoidElement.getInstance(this.logicalNetLayer.getMapView()));
//						this.sendMapEvent(new MapEvent(getCurrentMapElement(), MapEvent.MAP_ELEMENT_SELECTED));
//				}
				}
				this.logicalNetLayer.updateZoom();
				repaint(false);
			}
			else
			if(pce.getPropertyName().equals(MapEvent.MAP_ELEMENT_CHANGED))
			{
				Object me = pce.getNewValue();
				if(me instanceof SchemeElement)
				{
					mapViewController.scanElement((SchemeElement )me);
					mapViewController.scanCables(((SchemeElement )me).getParentScheme());
				}
				else
				if(me instanceof SchemeCableLink)
				{
					mapViewController.scanCable((SchemeCableLink )me);
					mapViewController.scanPaths(((SchemeCableLink )me).getParentScheme());
				}
				else
				if(me instanceof CablePath)
				{
					mapViewController.scanCable(((CablePath)me).getSchemeCableLink());
					mapViewController.scanPaths(((CablePath)me).getSchemeCableLink().getParentScheme());
				}
				else
				if(me instanceof SiteNode)
				{
					SiteNode site = (SiteNode)me;
					SiteNodeController snc = (SiteNodeController)mapViewController.getController(site);
					snc.updateScaleCoefficient(site);
				}

				repaint(false);
			}
			else
			if(pce.getPropertyName().equals(MapEvent.MAP_NAVIGATE))
			{
				MapNavigateEvent mne = (MapNavigateEvent )pce;

				//Здесь принимаюттся собитыя по создению и управлению маркером
				if(mne.isDataMarkerCreated())
				{
					MeasurementPath path;
					try
					{
						path = mapViewController.getMeasurementPathByMonitoredElementId(mne.getMeId());
					}
					catch (ApplicationException e)
					{
						e.printStackTrace();
						return;
					}

					if(path != null)
					{
						Marker marker = new Marker(
							mne.getMarkerId(),
							LoginManager.getUserId(),
			                mapView,
							mne.getDistance(),
							path,
							mne.getMeId(),
							LangModelMap.getString("Marker"));
						mapView.addMarker(marker);

						MarkerController mc = (MarkerController)mapViewController.getController(marker);
						mc.moveToFromStartLo(marker, mne.getDistance());
					}
				}
				else
				if(mne.isDataEventMarkerCreated())
				{
					MeasurementPath path;
					try
					{
						path = mapViewController.getMeasurementPathByMonitoredElementId(mne.getMeId());
					}
					catch (ApplicationException e)
					{
						e.printStackTrace();
						return;
					}

					if(path != null)
					{
						EventMarker marker = new EventMarker(
							mne.getMarkerId(),
							LoginManager.getUserId(),
			                mapView,
							mne.getDistance(),
							path,
							mne.getMeId(),
							LangModelMap.getString("Event"));
						mapView.addMarker(marker);

						MarkerController mc = (MarkerController)mapViewController.getController(marker);

						mc.moveToFromStartLo(marker, mne.getDistance());
					}
				}
				else
				if(mne.isDataAlarmMarkerCreated())
				{
					MeasurementPath path;
					try
					{
						path = mapViewController.getMeasurementPathByMonitoredElementId(mne.getMeId());
					}
					catch (ApplicationException e)
					{
						e.printStackTrace();
						return;
					}

					AlarmMarker marker = null;
					if(path != null)
					{
						for(Iterator it = mapView.getMarkers().iterator(); it.hasNext();)
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
								LoginManager.getUserId(),
								mapView,
								mne.getDistance(),
								path,
								mne.getMeId(),
								LangModelMap.getString("Alarm"));
							mapView.addMarker(marker);
						}
						else
						{
							marker.setId(mne.getMarkerId());
						}

						MarkerController mc = (MarkerController)mapViewController.getController(marker);

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
					Marker marker = mapView.getMarker(mne.getMarkerId());
					if(marker != null)
					{
						final MeasurementPath measurementPath = marker.getMeasurementPath();
						if (measurementPath.getSchemePath() == null)
							measurementPath.setSchemePath((SchemePath) mne.getSchemePath());

						MarkerController mc = (MarkerController)mapViewController.getController(marker);

						mc.moveToFromStartLo(marker, mne.getDistance());
					}
				}
				else
				if(mne.isDataMarkerSelected())
				{
					Marker marker = mapView.getMarker(mne.getMarkerId());
					if(marker != null)
						mapView.getMap().setSelected(marker, true);
				}
				else
				if(mne.isDataMarkerDeselected())
				{
					Marker marker = mapView.getMarker(mne.getMarkerId());
					if(marker != null)
						mapView.getMap().setSelected(marker, false);
				}
				else
				if(mne.isDataMarkerDeleted())
				{
					Marker marker = mapView.getMarker(mne.getMarkerId());
					if(marker != null)
						mapView.removeMarker(marker);
					if(marker instanceof AlarmMarker)
					{
/*
						AlarmMarker amarker = (AlarmMarker)marker;
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
					MapElement me = (MapElement)mne.getSource();
					if(me != null)
						mapView.getMap().setSelected(me, true);
				}
				else
				if(mne.isMapElementDeselected())
				{
					MapElement me = (MapElement)mne.getSource();
					if(me != null)
						mapView.getMap().setSelected(me, false);
				}

				repaint(false);
			}
//			else
//			if(pce.getPropertyName().equals(TreeDataSelectionEvent.type))
//			{
//				TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)pce;
//
//				List data = tdse.getList();
//				int n = tdse.getSelectionNumber();
//
//				if (n != -1)
//				{
//					try 
//					{
//						MapElement me = (MapElement)data.get(n);
//						this.mapView.getMap().setSelected(me, true);
//						repaint(false);
//					} 
//					catch (Exception ex) 
//					{
//						ex.printStackTrace();
//					} 
//				}
//			}
//			else
//			if(pce.getPropertyName().equals(TreeListSelectionEvent.typ))
//			{
//				if(pce.getSource() instanceof MapElement)
//				{
//					MapElement me = (MapElement)pce.getSource();
//					this.mapView.getMap().setSelected(me, true);
//					repaint(false);
//				} 
//			}

			else
			if(pce.getPropertyName().equals(ObjectSelectedEvent.TYPE))
			{
				ObjectSelectedEvent selectEvent = (ObjectSelectedEvent )pce;
				if(selectEvent.isSelected(ObjectSelectedEvent.SCHEME_ELEMENT))
				{
					SchemeElement schemeElement = (SchemeElement )selectEvent.getSelectedObject();

					SiteNode site = mapView.findElement(schemeElement);
					if(site != null)
						mapView.getMap().setSelected(site, true);
				}
				else
				if(selectEvent.isSelected(ObjectSelectedEvent.SCHEME_PATH))
				{
					SchemePath schemePath = (SchemePath )selectEvent.getSelectedObject();
					
					MeasurementPath measurementPath = mapView.findMeasurementPath(schemePath);
					if(measurementPath != null)
						mapView.getMap().setSelected(measurementPath, true);
				}
				else
				if(selectEvent.isSelected(ObjectSelectedEvent.SCHEME_CABLELINK))
				{
					SchemeCableLink schemeCableLink = (SchemeCableLink )selectEvent.getSelectedObject();
					CablePath cablePath = mapView.findCablePath(schemeCableLink);
					if(cablePath != null)
						mapView.getMap().setSelected(cablePath, true);
				}
/*
					else
					if(sne.SCHEME_ELEMENT_DESELECTED)
					{
						SchemeElement[] ses = (SchemeElement[] )sne.getSource();

						for(int i = 0; i < ses.length; i++)
						{
							SiteNode site = this.mapView.findElement(ses[i]);
							if(site != null)
								this.mapView.getMap().setSelected(site, false);
						}
					}

					if(sne.SCHEME_PATH_DESELECTED)
					{
						SchemePath[] sps = (SchemePath[] )sne.getSource();

						for(int i = 0; i < sps.length; i++)
						{
							MeasurementPath measurementPath = this.mapView.findMeasurementPath(sps[i]);
							if(measurementPath != null)
								this.mapView.getMap().setSelected(measurementPath, false);
						}
					}

					if(sne.SCHEME_CABLE_LINK_DESELECTED)
					{
						SchemeCableLink[] scs = (SchemeCableLink[] )sne.getSource();
						for(int i = 0; i < scs.length; i++)
						{
							CablePath cablePath = this.mapView.findCablePath(scs[i]);
							if(cablePath != null)
								this.mapView.getMap().setSelected(cablePath, false);
						}
					}
*/
				repaint(false);
			}
		}
		catch(MapConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(MapDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Создает объект вьюера.
	 * @param viewerClass класс вьюера
	 * @return объект вьюера
	 */
	public static NetMapViewer create(
			String viewerClass,
			LogicalNetLayer logicalNetLayer,
			MapContext mapContext,
			MapImageRenderer renderer)
				throws MapDataException {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call NetMapViewer.create()");

		try {
			Class clazz = Class.forName(viewerClass);
			Constructor[] constructors = clazz.getDeclaredConstructors();
			for (int i = 0; i < constructors.length; i++) {
				Class[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length == 3 
						&& parameterTypes[0].equals(LogicalNetLayer.class)
						&& parameterTypes[1].equals(MapContext.class)
						&& parameterTypes[2].equals(MapImageRenderer.class)) {
					Constructor constructor = constructors[i];
					constructor.setAccessible(true);
					Object[] initArgs = new Object[2];
					initArgs[0] = logicalNetLayer;
					initArgs[1] = mapContext;
					initArgs[2] = renderer;
					return (NetMapViewer)constructor.newInstance(initArgs);
				}
			}
//			mapViewer = (NetMapViewer )Class.forName(viewerClass).newInstance();
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw new MapDataException(
					"NetMapViewer.create() throws ClassNotFoundException");
		} catch(InstantiationException ie) {
			ie.printStackTrace();
			throw new MapDataException(
					"NetMapViewer.create() throws InstantiationException");
		} catch(IllegalAccessException iae) {
			iae.printStackTrace();
			throw new MapDataException(
					"NetMapViewer.create() throws IllegalAccessException");
		} catch(IllegalArgumentException iae) {
			iae.printStackTrace();
			throw new MapDataException("NetMapViewer.create() throws IllegalArgumentException");
		} catch(InvocationTargetException ite) {
			ite.printStackTrace();
			throw new MapDataException("NetMapViewer.create() throws InvocationTargetException");
		}
		throw new MapDataException("NetMapViewer.create() cannot find constructor with arguments (LogicalNetLayer, MapImageRenderer) for class " + viewerClass);
	}

	/**
	 * @return Returns the mapContext.
	 */
	public MapContext getMapContext() {
		return this.mapContext;
	}

	/**
	 * @return Returns the renderer.
	 */
	public MapImageRenderer getRenderer() {
		return this.renderer;
	}
}
