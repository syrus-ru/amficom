/**
 * $Id: NetMapViewer.java,v 1.41 2005/08/19 12:54:27 krupenn Exp $
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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.command.action.DeleteSelectionCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkTypeEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeTypeEditor;
import com.syrus.AMFICOM.client.map.ui.MapDropTargetListener;
import com.syrus.AMFICOM.client.map.ui.MapKeyAdapter;
import com.syrus.AMFICOM.client.map.ui.MapMouseListener;
import com.syrus.AMFICOM.client.map.ui.MapMouseMotionListener;
import com.syrus.AMFICOM.client.map.ui.MapToolTippedPanel;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.EventMarker;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

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
 * @version $Revision: 1.41 $, $Date: 2005/08/19 12:54:27 $
 * @module mapviewclient
 */
public abstract class NetMapViewer {
	protected LogicalNetLayer logicalNetLayer;
	protected MapContext mapContext;
	protected MapImageRenderer renderer;

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

	/**
	 * Timer который отвечает за изменение реЖима отображения графических 
	 * элементов в состоянии alarmed с периодом DEFAULT_TIME_INTERVAL.
	 * Timer меняет флаг отрисовки и выдает команду
	 * логическому слою перерисовать свое содержимое.
	 */
	private AlarmIndicationTimer animateTimer;
	
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
		this.mka = new MapKeyAdapter(this);

		if(this.logicalNetLayer.aContext != null)
			if(this.logicalNetLayer.aContext.getApplicationModel() != null)
				if (this.logicalNetLayer.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_INDICATION)) {
					this.animateTimer = new AlarmIndicationTimer(this);
		}
	}

	public void dispose() {
		this.ttm.unregisterComponent(this.mttp);
		this.animateTimer.dispose();
	}
	
	/**
	 * Осуществляет сохранение текущик параметров отображения карты для 
	 * следующей сессии.
	 */
	public void saveConfig() {
		try {
			MapPropertiesManager.setCenter(this.mapContext.getCenter());
			MapPropertiesManager.setZoom(this.mapContext.getScale());
			MapPropertiesManager.saveIniFile();
		} catch(MapException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Получить логический слой.
	 * @return 
	 * логический слой
	 */
	public LogicalNetLayer getLogicalNetLayer() {
		return this.logicalNetLayer;
	}

	/**
	 * Выполнить удаление выбранных элементов.
	 */
	public void delete(){
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setNetMapViewer(this);
		this.getLogicalNetLayer().commandList.add(command);
		this.getLogicalNetLayer().commandList.execute();
	}

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
	 * Установить центральную точку вида карты.
	 * @param center географическая координата центра
	 */
	public void setCenter(DoublePoint center)
			throws MapConnectionException, MapDataException {
		this.mapContext.setCenter(center);
		this.showLatLong(center);
		this.renderer.setCenter(center);
		repaint(true);
	}

	/**
	 * Установить заданный масштаб вида карты.
	 * @param scale масштаб
	 */
	public void setScale(double scale)
			throws MapConnectionException, MapDataException {
		this.mapContext.setScale(scale);
		this.logicalNetLayer.updateZoom();
		this.logicalNetLayer.sendScaleEvent(new Double(this.mapContext.getScale()));
		this.renderer.setScale(this.mapContext.getScale());
		repaint(true);
	}

	/**
	 * Изменить масштаб вида карты в заданное число раз.
	 * @param scaleCoef коэффициент масштабирования
	 */
	public void scaleTo(double scaleCoef)
			throws MapConnectionException, MapDataException {
		this.mapContext.scaleTo(scaleCoef);
		this.logicalNetLayer.updateZoom();
		this.logicalNetLayer.sendScaleEvent(new Double(this.mapContext.getScale()));
		this.renderer.setScale(this.mapContext.getScale());
		repaint(true);
	}

	/**
	 * Приблизить вид карты в стандартное число раз.
	 */
	public void zoomIn()
			throws MapConnectionException, MapDataException {
		this.mapContext.zoomIn();
		this.logicalNetLayer.updateZoom();
		this.logicalNetLayer.sendScaleEvent(new Double(this.mapContext.getScale()));
		this.renderer.setScale(this.mapContext.getScale());
		repaint(true);
	}

	/**
	 * Отдалить вид карты в стандартное число раз.
	 */
	public void zoomOut()
			throws MapConnectionException, MapDataException {
		this.mapContext.zoomOut();
		this.logicalNetLayer.updateZoom();
		this.logicalNetLayer.sendScaleEvent(new Double(this.mapContext.getScale()));
		this.renderer.setScale(this.mapContext.getScale());
		repaint(true);
	}

	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек.
	 * @param from географическая координата
	 * @param to географическая координата
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
			throws MapConnectionException, MapDataException {
		this.mapContext.zoomToBox(from, to);
		this.logicalNetLayer.updateZoom();
		this.logicalNetLayer.sendScaleEvent(new Double(this.mapContext.getScale()));
		this.renderer.setScale(this.mapContext.getScale());
		this.renderer.setCenter(this.mapContext.getCenter());
		repaint(true);
	}

	/**
	 * Передать команду на отображение координат текущей точки в окне координат.
	 * @param doublePoint географическая координата мыши
	 */	
	public void showLatLong(DoublePoint doublePoint) {
		LogicalNetLayer logicalNetLayer = this.getLogicalNetLayer();
		if(logicalNetLayer.aContext == null)
			return;
		Dispatcher disp = logicalNetLayer.aContext.getDispatcher();
		if(disp == null)
			return;
		disp.firePropertyChange(new MapEvent(logicalNetLayer, MapEvent.MAP_VIEW_CENTER_CHANGED, doublePoint));
	}

	/**
	 * Передать команду на отображение координат текущей точки в окне координат.
	 * @param point экранная координата мыши
	 */	
	public void showLatLong(Point point)
		throws MapConnectionException, MapDataException {
		DoublePoint doublePoint = this.logicalNetLayer.getConverter().convertScreenToMap(point);
		showLatLong(doublePoint);
	}

	/**
	 * Получить флаг отображения контекстного меню.
	 * @return флаг
	 */
	public boolean isMenuShown() {
		return this.menuShown;
	}

	/**
	 * Установить флаг отображения контекстного меню.
	 * @param isMenuShown флаг
	 */	
	public void setMenuShown(boolean isMenuShown) {
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

	public void propertyChange(PropertyChangeEvent pce) {
		try {
			MapViewController mapViewController = this.logicalNetLayer.getMapViewController();

			MapView mapView = this.logicalNetLayer.getMapView();
			
			if(pce.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
				MapEvent mapEvent = (MapEvent )pce;
				String mapEventType = mapEvent.getMapEventType();

				if(mapEventType.equals(MapEvent.COPY_TYPE)) {
					Object selectedObject = pce.getNewValue();
					if(selectedObject instanceof SiteNodeType) {
						copyType((SiteNodeType)selectedObject);
					}
					else if(selectedObject instanceof PhysicalLinkType) {
						copyType((PhysicalLinkType)selectedObject);
					}
				}
				else if(mapEventType.equals(MapEvent.OTHER_SELECTED)) {
					Object selectedObject = pce.getNewValue();
					if(selectedObject instanceof PhysicalLinkType) {
						this.logicalNetLayer.setCurrentPhysicalLinkType((PhysicalLinkType)selectedObject);
					}
				}
				else if(mapEventType.equals(MapEvent.NEED_FULL_REPAINT)) {
					this.logicalNetLayer.calculateVisualElements();
					repaint(true);
				}
				else if(mapEventType.equals(MapEvent.NEED_REPAINT)) {
					repaint(false);
				}
				else if(mapEventType.equals(MapEvent.MAP_CHANGED)) {
					updateSelectedElements();
					this.logicalNetLayer.updateZoom();
					this.logicalNetLayer.sendSelectionChangeEvent();
					repaint(false);
				}
				else if(mapEventType.equals(MapEvent.MAP_ELEMENT_CHANGED)) {
					Object me = pce.getNewValue();
					if(me instanceof SchemeElement) {
						mapViewController.scanElement((SchemeElement )me);
						mapViewController.scanCables(((SchemeElement )me).getParentScheme());
					}
					else if(me instanceof SchemeCableLink) {
						mapViewController.scanCable((SchemeCableLink )me);
						mapViewController.scanPaths(((SchemeCableLink )me).getParentScheme());
					}
					else if(me instanceof CablePath) {
						mapViewController.scanCable(((CablePath)me).getSchemeCableLink());
						mapViewController.scanPaths(((CablePath)me).getSchemeCableLink().getParentScheme());
					}
					else if(me instanceof SiteNode) {
						SiteNode site = (SiteNode)me;
						SiteNodeController snc = (SiteNodeController)mapViewController.getController(site);
						snc.updateScaleCoefficient(site);
					}
	
					this.logicalNetLayer.calculateVisualElements();
					repaint(false);
				}
				else if(mapEventType.equals(MapEvent.MAP_VIEW_CHANGED)) {
					this.logicalNetLayer.calculateVisualElements();
					this.logicalNetLayer.sendSelectionChangeEvent();
					repaint(false);
				}
				else if(mapEventType.equals(MapEvent.NEED_SELECT)) {
					Collection elements = (Collection )pce.getNewValue();
					for(Iterator iter = elements.iterator(); iter.hasNext();) {
						Object element = iter.next();
						if(element instanceof MapElement) {
							MapElement mapElement = (MapElement )element;
							mapView.getMap().setSelected(mapElement, true);
						}
						else if(element instanceof SchemeElement) {
							SchemeElement schemeElement = (SchemeElement)element;
							MapElement mapElement = mapView.findElement(schemeElement);
							if(mapElement != null)
								mapView.getMap().setSelected(mapElement, true);
						}
						else if(element instanceof SchemeCableLink) {
							SchemeCableLink schemeCableLink = (SchemeCableLink)element;
							MapElement mapElement = mapView.findCablePath(schemeCableLink);
							if(mapElement != null)
								mapView.getMap().setSelected(mapElement, true);
						}
						else if(element instanceof SchemePath) {
							SchemePath schemePath = (SchemePath)element;
							MapElement mapElement = mapView.findMeasurementPath(schemePath);
							if(mapElement != null)
								mapView.getMap().setSelected(mapElement, true);
						}
					}
					updateSelectedElements();
					this.logicalNetLayer.sendSelectionChangeEvent();
					repaint(false);
				}
				else if(mapEventType.equals(MapEvent.NEED_DESELECT)) {
					Collection elements = (Collection )pce.getNewValue();
					for(Iterator iter = elements.iterator(); iter.hasNext();) {
						Object element = iter.next();
						if(element instanceof MapElement) {
							MapElement mapElement = (MapElement )element;
							mapView.getMap().setSelected(mapElement, false);
						}
					}
					updateSelectedElements();
					this.logicalNetLayer.sendSelectionChangeEvent();
					repaint(false);
				}

				if(pce.getSource().equals(this.logicalNetLayer))
					return;
	
				if(mapEventType.equals(MapEvent.SELECTION_CHANGED)) {
					updateSelectedElements();
					this.logicalNetLayer.sendSelectionChangeEvent();
					repaint(false);
					return;
				}
				else if(mapEventType.equals(MapEvent.DESELECT_ALL)) {
					this.logicalNetLayer.deselectAll();
					this.logicalNetLayer.sendSelectionChangeEvent();
				}
				else if(mapEventType.equals(MapEvent.MAP_NAVIGATE)) {
					MapNavigateEvent mne = (MapNavigateEvent )mapEvent;
	
					//Здесь принимаюттся собитыя по создению и управлению маркером
					if(mne.isDataMarkerCreated()) {
						MeasurementPath path;
						try {
							path = mapViewController.getMeasurementPathByMonitoredElementId(mne.getMeId());
						} catch (ApplicationException e) {
							e.printStackTrace();
							return;
						}
	
						if(path != null) {
							Marker marker = new Marker(
								mne.getMarkerId(),
								LoginManager.getUserId(),
				                mapView,
								path,
								mne.getMeId(),
								LangModelMap.getString("Marker"));
							mapView.addMarker(marker);
	
							MarkerController mc = (MarkerController)
									mapViewController.getController(marker);
							mc.moveToFromStartLo(marker, mne.getDistance());
						}
					}
					else if(mne.isDataEventMarkerCreated()) {
							MeasurementPath path;
							try {
								path = mapViewController
									.getMeasurementPathByMonitoredElementId(
											mne.getMeId());
							} catch(ApplicationException e) {
								e.printStackTrace();
								return;
							}
	
							if(path != null) {
								EventMarker marker = new EventMarker(
										mne.getMarkerId(),
										LoginManager.getUserId(),
										mapView,
										path,
										mne.getMeId(),
										LangModelMap.getString("Event"));
								mapView.addMarker(marker);
	
								MarkerController mc = (MarkerController) 
										mapViewController.getController(marker);
	
								mc.moveToFromStartLo(marker, mne.getDistance());
							}
						}
						else if(mne.isDataAlarmMarkerCreated()) {
							MeasurementPath path;
							try {
								path = mapViewController
									.getMeasurementPathByMonitoredElementId(
											mne.getMeId());
							} catch(ApplicationException e) {
								e.printStackTrace();
								return;
							}
	
							AlarmMarker marker = null;
							if(path != null) {
								for(Iterator it = mapView.getMarkers().iterator(); it.hasNext();) {
									try {
										marker = (AlarmMarker) it.next();
										if(marker.getMeasurementPath().equals(path))
											break;
										marker = null;
									} catch(Exception ex) {
										ex.printStackTrace();
									}
								}
								if(marker == null) {
									marker = new AlarmMarker(
											mne.getMarkerId(),
											LoginManager.getUserId(),
											mapView,
											path,
											mne.getMeId(),
											LangModelMap.getString("Alarm"));
									mapView.addMarker(marker);
								}
								else {
									marker.setId(mne.getMarkerId());
								}
	
								MarkerController mc = (MarkerController) 
										mapViewController.getController(marker);
	
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

				repaint(false);
			}
			}
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
				repaint(false);
			}
		} catch(MapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void copyType(SiteNodeType siteNodeType) {
		try {
			SiteNodeType newSiteNodeType = SiteNodeType.createInstance(
					LoginManager.getUserId(), 
					siteNodeType.getSort(), 
					"codename", 
					"Copy of " + siteNodeType.getName(), 
					siteNodeType.getDescription(), 
					siteNodeType.getImageId(), 
					true, 
					siteNodeType.getMapLibrary().getId());
			newSiteNodeType.setCodename(newSiteNodeType.getId().toString());
			SiteNodeTypeEditor siteNodeTypeEditor = new SiteNodeTypeEditor();

			siteNodeTypeEditor.setNetMapViewer(this);
			if(EditorDialog.showEditorDialog(
					LangModelMap.getString("sitenodetype"),
					newSiteNodeType,
					siteNodeTypeEditor) ) {
				StorableObjectPool.flush(newSiteNodeType, LoginManager.getUserId(), true);
			} else {
				StorableObjectPool.delete(newSiteNodeType.getId());
			}
			this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.LIBRARY_SET_CHANGED));
		} catch(CreateObjectException e) {
			e.printStackTrace();
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
	}

	protected void copyType(PhysicalLinkType physicalLinkType) {
		try {
			PhysicalLinkType newPhysicalLinkType = PhysicalLinkType.createInstance(
					LoginManager.getUserId(), 
					physicalLinkType.getSort(), 
					"codename", 
					"Copy of " + physicalLinkType.getName(), 
					physicalLinkType.getDescription(), 
					physicalLinkType.getBindingDimension(), 
					true, 
					physicalLinkType.getMapLibrary().getId());
			newPhysicalLinkType.setCodename(newPhysicalLinkType.getId().toString());
			PhysicalLinkTypeEditor physicalLinkTypeEditor = new PhysicalLinkTypeEditor();
			physicalLinkTypeEditor.setNetMapViewer(this);
			if(EditorDialog.showEditorDialog(
					LangModelMap.getString("physicallinktype"),
					newPhysicalLinkType,
					physicalLinkTypeEditor)) {
				StorableObjectPool.flush(newPhysicalLinkType, LoginManager.getUserId(), true);
			}
			else {
				StorableObjectPool.delete(newPhysicalLinkType.getId());
			}
		} catch(CreateObjectException e) {
			e.printStackTrace();
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void updateSelectedElements() {
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
		}
		else
		if(selectedElements.size() == 1)
		{
			MapElement me = (MapElement)selectedElements.iterator().next();
			this.logicalNetLayer.setCurrentMapElement(me);
		}
		else
		//selectedElements.size() == 0
		{
			this.logicalNetLayer.setCurrentMapElement(VoidElement.getInstance(this.logicalNetLayer.getMapView()));
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
		Log.debugMessage("method call NetMapViewer.create()", Level.FINER);

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
					Object[] initArgs = new Object[3];
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
		throw new MapDataException("NetMapViewer.create() cannot find constructor with arguments (LogicalNetLayer, MapContext, MapImageRenderer) for class " + viewerClass);
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

	public void cancelMode() {
		MapState state = this.logicalNetLayer.getMapState();

		state.setActionMode(MapState.NULL_ACTION_MODE);
		state.setOperationMode(MapState.NO_OPERATION);
		state.setMouseMode(MapState.MOUSE_NONE);
		state.setShowMode(MapState.SHOW_PHYSICAL_LINK);

		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_MOVE_TO_CENTER, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_BOX, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_HAND_PAN, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_TO_POINT, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_IN, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_OUT, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_ZOOM_BOX, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_MEASURE_DISTANCE, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_MOVE_FIXED, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.OPERATION_CENTER_SELECTION, 
				false);

		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.MODE_NODE_LINK, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.MODE_LINK, 
				true);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.MODE_CABLE_PATH, 
				false);
		this.logicalNetLayer.getContext().getApplicationModel().setSelected(
				MapApplicationModel.MODE_PATH, 
				false);

		this.logicalNetLayer.getContext().getApplicationModel().fireModelChanged();
	}
}
