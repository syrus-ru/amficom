/*-
 * $$Id: MapViewController.java,v 1.67 2006/02/15 12:54:38 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.client.map.command.action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemePathCommand;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * Класс используется для управления информацией о канализационной
 * прокладке кабелей и положении узлов и других топологических объектов.
 * 
 * @version $Revision: 1.67 $, $Date: 2006/02/15 12:54:38 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapViewController {
	public static final String ELEMENT_SITENODE = MapEditorResourceKeys.ENTITY_SITE_NODE;
//	public static final String ELEMENT_WELL = "well"; //$NON-NLS-1$
//	public static final String ELEMENT_PIQUET = MapEditorResourceKeys.ENTITY_PIQUET;
	public static final String ELEMENT_PHYSICALLINK = MapEditorResourceKeys.ENTITY_PHYSICAL_LINK;
	public static final String ELEMENT_COLLECTOR = MapEditorResourceKeys.ENTITY_COLLECTOR;
	public static final String ELEMENT_CABLEPATH = MapEditorResourceKeys.ENTITY_CABLEPATH;
	public static final String ELEMENT_TOPOLOGICALNODE = MapEditorResourceKeys.ENTITY_TOPOLOGICAL_NODE;
	public static final String ELEMENT_MARK = MapEditorResourceKeys.ENTITY_MARK;
	public static final String ELEMENT_MEASUREMENTPATH = MapEditorResourceKeys.ENTITY_MEASUREMENT_PATH;
	public static final String ELEMENT_MARKER = MapEditorResourceKeys.ENTITY_MARKER;
//	public static final String ELEMENT_CABLEINLET = "cableinlet"; //$NON-NLS-1$
	public static final String ELEMENT_NODELINK = MapEditorResourceKeys.ENTITY_NODE_LINK;

	public static String getMapElementReadableType(MapElement mapElement) {
		if(mapElement instanceof SiteNode) {
			SiteNode site = (SiteNode )mapElement;
			return site.getType().getName();
		}
		return I18N.getString(MapViewController.getMapElementType(mapElement));
	}

	public static String getMapElementType(MapElement mapElement)
	{
		if(mapElement instanceof SiteNode)
			return MapViewController.ELEMENT_SITENODE;
		else if(mapElement instanceof PhysicalLink)
			return MapViewController.ELEMENT_PHYSICALLINK;
		else if(mapElement instanceof Collector)
			return MapViewController.ELEMENT_COLLECTOR;
		else if(mapElement instanceof CablePath)
			return MapViewController.ELEMENT_CABLEPATH;
		else if(mapElement instanceof TopologicalNode)
			return MapViewController.ELEMENT_TOPOLOGICALNODE;
		else if(mapElement instanceof Mark)
			return MapViewController.ELEMENT_MARK;
		else if(mapElement instanceof MeasurementPath)
			return MapViewController.ELEMENT_MEASUREMENTPATH;
		else if(mapElement instanceof Marker)
			return MapViewController.ELEMENT_MARKER;
		else if(mapElement instanceof NodeLink)
			return MapViewController.ELEMENT_NODELINK;
		return ""; //$NON-NLS-1$
	}

	/** Хэш-таблица контроллеров элементов карты. */
	private java.util.Map<Class, MapElementController> ctlMap = new HashMap<Class, MapElementController>();
	
	/** Ссылка на логический слой, на котором отображается вид. */
	protected LogicalNetLayer logicalNetLayer = null;

	/** Хранимый объект. */
	private MapView mapView;

	private NetMapViewer netMapViewer;

	private static long time1 = 0L;
	private static long time2 = 0L;
	private static long time3 = 0L;
	private static long time4 = 0L;
	private static long time5 = 0L;
	private static long time6 = 0L;	
	private static long time7 = 0L;	
	private static long time8 = 0L;	

	/**
	 * Приветный конструктор. Использовать 
	 * {@link MapViewController#createInstance(NetMapViewer)}.
	 * @param netMapViewer логический слой
	 */
	private MapViewController(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = this.netMapViewer.getLogicalNetLayer();

		this.ctlMap.put(TopologicalNode.class,
			TopologicalNodeController.createInstance(netMapViewer));
		this.ctlMap.put(SiteNode.class,
			SiteNodeController.createInstance(netMapViewer));
		this.ctlMap.put(NodeLink.class,
			NodeLinkController.createInstance(netMapViewer));
		this.ctlMap.put(PhysicalLink.class,
			PhysicalLinkController.createInstance(netMapViewer));
		this.ctlMap.put(Mark.class,
			MarkController.createInstance(netMapViewer));
		this.ctlMap.put(Collector.class,
			CollectorController.createInstance(netMapViewer));

		this.ctlMap.put(CablePath.class,
			CableController.createInstance(netMapViewer));
		this.ctlMap.put(MeasurementPath.class,
			MeasurementPathController.createInstance(netMapViewer));
		this.ctlMap.put(UnboundNode.class,
			UnboundNodeController.createInstance(netMapViewer));
		this.ctlMap.put(UnboundLink.class,
			UnboundLinkController.createInstance(netMapViewer));
		this.ctlMap.put(Marker.class,
			MarkerController.createInstance(netMapViewer));
	}
	
	public static MapViewController createInstance(NetMapViewer netMapViewer) {
		return new MapViewController(netMapViewer);
	}

	/**
	 * Получить контроллер для элемента карты.
	 * @param me элемент карты
	 * @return контроллер
	 */
	public MapElementController getController(MapElement me) {
		MapElementController controller = this.ctlMap.get(me.getClass());
		if (controller == null) {
			controller = this.ctlMap.get(me.getClass().getSuperclass());
		}
		return controller;
	}


	/**
	 * Отрисовать элемент. При отрисовке необходимо производить проверку 
	 * вхождения элемента карты в вилимую область, и отрисовывать только
	 * в этом случае.
	 * @param me элемент карты, который необходимо отрисовать
	 * @param g графический контекст
	 * @param visibleBounds видимая облать
	 */
	public void paint(
			MapElement me,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		getController(me).paint(me, g, visibleBounds);
	}

	/**
	 * Возвращает флаг, указывающий, что точка currentMousePoint находится
	 * в определенных границах элемента. Для узла границы определяются
	 * размерами иконки, для линии дельта-окрестностью линии. Дельта задается
	 * полем {@link com.syrus.AMFICOM.client.map.MapPropertiesManager#getMouseTolerancy()}.
	 * @param me элемент карты
	 * @param currentMousePoint точка в экранных координатах
	 * @return <code>true</code>, если точка на элементе карты, иначе 
	 * <code>false</code>
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		return getController(me).isMouseOnElement(me, currentMousePoint);
	}

	/**
	 * Определить, попадает ли элемент в область visibleBounds.
	 * Используется при отрисовке (отображаются только элементы, попавшие
	 * в видимую область).
	 * @param me элемент карты
	 * @param visibleBounds видимая облать
	 * @return <code>true</code>, если элемент попадает в область, иначе 
	 * <code>false</code>
	 */
	public boolean isElementVisible(
			MapElement me,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		return getController(me).isElementVisible(me, visibleBounds);
	}

	/**
	 * Получить текст всплывающей подсказки для элемента карты.
	 * @param me элемент карты
	 * @return строка для всплывающей подсказки
	 */
	String getToolTipText(MapElement me) {
		return getController(me).getToolTipText(me);
	}

	/**
	 * Установить вид, с которым будет работать контроллер.
	 * @param mapView вид
	 */
	public void setMapView(MapView mapView)
			throws MapConnectionException, MapDataException {
		this.mapView = mapView;

		this.mapView.setCenter(this.logicalNetLayer.getMapContext().getCenter());

		this.mapView.setScale(this.logicalNetLayer.getMapContext().getScale());

		this.mapView.revert();
		this.scanSchemes();
	}
	
	/**
	 * Установить топологическую схему.
	 * @param map топологическая схема
	 */
	public void setMap(Map map) {
		this.mapView.setMap(map);
		scanSchemes();
	}

	/**
	 * Получить хранимый объект вида.
	 * @return хранимый объект вида
	 */
	public MapView getMapView() {
		return this.mapView;
	}

	/**
	 * Получить топологический путь по идентификатору измерительного элемента.
	 * @param meId идентификатор измерительного элемента
	 * @return топологический путь
	 * @throws com.syrus.AMFICOM.general.CommunicationException 
	 * @throws com.syrus.AMFICOM.general.DatabaseException
	 */
	public MeasurementPath getMeasurementPathByMonitoredElementId(Identifier meId)
			throws ApplicationException {
		if(meId == null)
			return null;
		MeasurementPath path = null;
		MonitoredElement me = StorableObjectPool.getStorableObject(meId, true);
		if(me == null)
			return null;
		if(me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
			Set<Identifier> monitoredDomainMemberIds = me.getMonitoredDomainMemberIds();
			if(monitoredDomainMemberIds.size() == 0)
				return null;
			Identifier tpId = monitoredDomainMemberIds.iterator().next();
			TransmissionPath tp = StorableObjectPool.getStorableObject(tpId, true);
			if(tp != null) {
				for(MeasurementPath mp : this.mapView.getMeasurementPaths()) {
					if(mp.getSchemePath().getTransmissionPath().equals(tp)) {
						path = mp;
						break;
					}
				}
			}
		}

		return path;
	}

	public MeasurementPath getMeasurementPathBySchemePathId(Identifier schemePathId) {
		for(MeasurementPath mp : this.mapView.getMeasurementPaths()) {
			if(mp.getSchemePath().equals(schemePathId)) {
				return mp;
			}
		}
		return null;
	}

	/**
	 * Сканировать схемы, включенный в вид, на предмет визуализации элементов схем.
	 */
	public void scanSchemes() {
		for(Iterator it = this.mapView.getSchemes().iterator(); it.hasNext();) {
			scanElements((Scheme )it.next());
		}
	}


	/**
	 * Добавить схему в вид.
	 * 
	 * @param sch схема
	 */
	public void addScheme(Scheme sch) {
		this.mapView.addScheme(sch);
		scanElements(sch);
	}

	/**
	 * Убрать схему из вида.
	 * 
	 * @param sch схема
	 */
	public void removeScheme(Scheme sch) {
		this.mapView.removeScheme(sch);
		removePaths(sch);
		removeCables(sch);
		removeElements(sch);
	}

	/**
	 * Убрать все схемы из вида. Реализуется поэлементным удалением всех схем из
	 * вида, поскольку удаление каждой отдельной схемы включает в себя
	 * сканирование элементов схемы и их удаление из вида.
	 */
	public void removeSchemes() {
		while(!this.mapView.getSchemes().isEmpty()) {
			Scheme sch = this.mapView.getSchemes().iterator().next();
			removeScheme(sch);
		}
	}

	/**
	 * Сканировать элемент схемы на предмет его привязки к элементу карты или
	 * нанесения нового непривязанного элемента.
	 * 
	 * @param schemeElement элемент схемы
	 */
	public void scanElement(SchemeElement schemeElement) {
		SiteNode node = this.mapView.findElement(schemeElement);
		if(node == null) {
			Equipment equipment = schemeElement.getEquipment();
			if(equipment != null 
				&& (equipment.getLongitude() != 0.0D
					|| equipment.getLatitude() != 0.0D) ) {
				placeElement(
					schemeElement, 
					new DoublePoint(
						equipment.getLongitude(), 
						equipment.getLatitude()));
			}
		}
	}
	
	/**
	 * Сканировать все элементы схемы.
	 * @param scheme схема
	 */
	public void scanElements(Scheme scheme) {

		try {
			for(Iterator it = scheme.getTopologicalSchemeElementsRecursively(false).iterator(); it.hasNext();) {
				SchemeElement element = (SchemeElement )it.next();
				scanElement(element);
			}
			scanCables(scheme);
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * Сканировать кабель на предмет его привязки к тоннелю или нанесения
	 * нового непривязанного кабеля ка топологическую схему.
	 * @param schemeCableLink кабель
	 */
	public void scanCable(SchemeCableLink schemeCableLink) {
		long t1 = System.currentTimeMillis();
		SiteNode cableStartNode = this.mapView.getStartNode(schemeCableLink);
		long t2 = System.currentTimeMillis();
		SiteNode cableEndNode = this.mapView.getEndNode(schemeCableLink);
		long t3 = System.currentTimeMillis();
		CablePath cp = this.mapView.findCablePath(schemeCableLink);
		long t4 = System.currentTimeMillis();
		if(cp == null) {
			if(cableStartNode != null && cableEndNode != null) {
				placeElement(schemeCableLink);
			}
		}
		else {
			if(cableStartNode == null || cableEndNode == null) {
				unplaceElement(cp);
			}
			else {
				placeElement(schemeCableLink);
			}
		}
		long t5 = System.currentTimeMillis();
		Log.debugMessage("scanCable :: get start node for scl " + (t2 - t1) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		Log.debugMessage("scanCable :: get end node for scl " + (t3 - t2) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		Log.debugMessage("scanCable :: find cable path " + (t4 - t3) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		Log.debugMessage("scanCable :: placeElement(scl) " + (t5 - t4) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Сканировать все кабели схемы.
	 * 
	 * @param scheme схема
	 */
	public void scanCables(Scheme scheme) {
		try {
			long t1 = System.currentTimeMillis();
			Set<SchemeCableLink> topologicalCableLinks = scheme.getTopologicalSchemeCableLinksRecursively(false);
			long t2 = System.currentTimeMillis();
			for(Iterator it = topologicalCableLinks.iterator(); it.hasNext();) {
				SchemeCableLink scl = (SchemeCableLink )it.next();
				scanCable(scl);
			}
			long t3 = System.currentTimeMillis();
			scanPaths(scheme);
			long t4 = System.currentTimeMillis();
			Log.debugMessage("scanCables :: SchemeUtils.getTopologicalCableLinks(scheme) " + (t2 - t1) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("scanCables :: scanCable(scl) : topologicalCableLinks " + (t3 - t2) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("scanCables :: scanPaths(scheme); " + (t4 - t3) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * Сканировать схемный путь на предмет его привязки к тоннелям.
	 * @param schemePath схемный путь
	 */
	public void scanPath(SchemePath schemePath) {
		long t1 = System.currentTimeMillis();
		SiteNode pathStartNode = this.mapView.getStartNode(schemePath);
		long t2 = System.currentTimeMillis();
		SiteNode pathEndNode = this.mapView.getEndNode(schemePath);
		long t3 = System.currentTimeMillis();
		MeasurementPath mp = this.mapView.findMeasurementPath(schemePath);
		long t4 = System.currentTimeMillis();
		if(mp == null) {
			if(pathStartNode != null && pathEndNode != null) {
				placeElement(schemePath);
			}
		}
		else {
		if(pathStartNode == null || pathEndNode == null) {
			unplaceElement(mp);
		}
		else {
			placeElement(schemePath);
		}
		}
		long t5 = System.currentTimeMillis();
		Log.debugMessage("scanPath :: get start node for sp " + (t2 - t1) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		Log.debugMessage("scanPath :: get end node for sp " + (t3 - t2) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		Log.debugMessage("scanPath :: find measurement path " + (t4 - t3) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		Log.debugMessage("scanPath :: placeElement(sp) " + (t5 - t4) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Сканировать все схемные пути на схеме.
	 * 
	 * @param scheme схема
	 */
	public void scanPaths(final Scheme scheme) {
		try {
			long t1 = System.currentTimeMillis();
			Set<SchemePath> topologicalPaths = scheme.getTopologicalSchemePathsRecursively(false);
			long t2 = System.currentTimeMillis();
			for (final SchemePath schemePath : topologicalPaths) {
				this.scanPath(schemePath);
			}
			long t3 = System.currentTimeMillis();
			Log.debugMessage("scanPaths :: scheme.getTopologicalPaths() " + (t2 - t1) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("scanPaths :: scanPath(schemePath) : topologicalPaths " + (t3 - t2) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * Убрать все схемные пути заданной схемы из вида.
	 * @param scheme схема
	 */
	public void removePaths(final Scheme scheme) {
		try {
			for (final SchemePath schemePath : scheme.getTopologicalSchemePathsRecursively(false)) {
				final MeasurementPath measurementPath = this.mapView.findMeasurementPath(schemePath);
				if (measurementPath != null) {
					this.unplaceElement(measurementPath);
				}
			}
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * Убрать все кабели заданной схемы из вида.
	 * 
	 * @param scheme схема
	 */
	public void removeCables(Scheme scheme) {
		try {
			Collection schemeCables = scheme.getTopologicalSchemeCableLinksRecursively(false);
			for(Iterator it = schemeCables.iterator(); it.hasNext();) {
				SchemeCableLink scl = (SchemeCableLink )it.next();
				CablePath cp = this.mapView.findCablePath(scl);
				if(cp != null) {
					unplaceElement(cp);
				}
			}
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * Убрать все элементы заданной схемы из вида.
	 * 
	 * @param scheme схема
	 */
	public void removeElements(Scheme scheme) {
		try {
			Collection schemeElements = scheme.getTopologicalSchemeElementsRecursively(false);
			for(Iterator it = schemeElements.iterator(); it.hasNext();) {
				SchemeElement se = (SchemeElement )it.next();
				SiteNode site = this.mapView.findElement(se);
				if(site != null) {
					if(site instanceof UnboundNode) {
						RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(site);
						cmd.setLogicalNetLayer(this.logicalNetLayer);
						cmd.execute();
					}
				}
			}
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	/**
	 * Разместить элемент на карте.
	 * @param se элемент схемы
	 * @param point географическая точка
	 */
	public void placeElement(SchemeElement se, DoublePoint point) {
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setNetMapViewer(this.netMapViewer);
		cmd.execute();
	}

	/**
	 * Убрать элемент схемы из вида. Подразумевает отвязку от элемента карты
	 * или удаление непривязанного элемента.
	 * @param node элемент карты
	 * @param se элемент схемы
	 */
//	public void unplaceElement(SiteNode node, SchemeElement se) {
//		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
//		cmd.setNetMapViewer(this.netMapViewer);
//		cmd.execute();
//	}

	/**
	 * Разместить кабель на топологической схеме.
	 * @param scl кабель
	 */
	public void placeElement(SchemeCableLink scl) {
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setNetMapViewer(this.netMapViewer);
		cmd.execute();
	}
	
	/**
	 * Убрать кабель из вида. Подразумевает отавязку от всех тоннелей 
	 * и удаление непривязанных фрагментов.
	 * @param cablePath кабель
	 */
	public void unplaceElement(CablePath cablePath) {
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cablePath);
		cmd.setNetMapViewer(this.netMapViewer);
		cmd.execute();
	}

	/**
	 * Разместить схемный путь на карте. Используется при переносе 
	 * (drag/drop).
	 * @param sp схемный путь
	 */
	public void placeElement(SchemePath sp) {
		PlaceSchemePathCommand cmd = new PlaceSchemePathCommand(sp);
		cmd.setNetMapViewer(this.netMapViewer);
		cmd.execute();
	}

	/**
	 * Убрать топологический путь из вида.
	 * @param mp топологический путь
	 */
	public void unplaceElement(MeasurementPath mp) {
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setNetMapViewer(this.netMapViewer);
		cmd.execute();
	}

	/**
	 * @param netMapViewer The netMapViewer to set.
	 */
	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	public static long getTime1() {
		return time1;
	}

	public static void addTime1(long difftime) {
		MapViewController.time1 += difftime;
	}

	public static void nullTime1() {
		time1 = 0L;
	}

	public static long getTime2() {
		return time2;
	}

	public static void addTime2(long difftime) {
		MapViewController.time2 += difftime;
	}

	public static void nullTime2() {
		time2 = 0L;
	}

	public static long getTime3() {
		return time3;
	}

	public static long getTime4() {
		return time4;
	}

	public static long getTime5() {
		return time5;
	}

	public static long getTime6() {
		return time6;
	}
	
	public static long getTime7() {
		return time7;
	}
	
	public static long getTime8() {
		return time8;
	}
	
	public static void addTime3(long difftime) {
		MapViewController.time3 += difftime;
	}

	public static void addTime4(long difftime) {
		MapViewController.time4 += difftime;
	}

	public static void addTime5(long difftime) {
		MapViewController.time5 += difftime;
	}

	public static void addTime6(long difftime) {
		MapViewController.time6 += difftime;
	}
	
	public static void addTime7(long difftime) {
		MapViewController.time7 += difftime;
	}
	
	public static void addTime8(long difftime) {
		MapViewController.time8 += difftime;
	}
	
	public static void nullTime3() {
		time3 = 0L;
	}

	public static void nullTime4() {
		time4 = 0L;
	}

	public static void nullTime5() {
		time5 = 0L;
	}

	public static void nullTime6() {
		time6 = 0L;
	}

	public static void nullTime7() {
		time7 = 0L;
	}

	public static void nullTime8() {
		time8 = 0L;
	}

/* from SiteNode

	//Возвращяет длинну линий внутри данного узла,
	//пересчитанную на коэффициент топологической привязки
	public PathElement countPhysicalLength(SchemePath sp, PathElement pe, Enumeration pathelements)
	{
		physical_length = 0.0;

		if(this.elementId == null || this.elementId.equals(""))
			return pe;
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, this.elementId);

		Vector vec = new Vector();
		Enumeration e = se.getAllSchemesLinks();
		for(;e.hasMoreElements();)
			vec.add(e.nextElement());

		for(;;)
		{
			if(pe.is_cable)
			{
				SchemeCableLink schemeCableLink =
						(SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.linkId);
				if(schemeCableLink == null)
				{
					Log.debugMessage("Something wrong... - schemeCableLink == null");
					return pe;
				}
				if(!vec.contains(schemeCableLink))
					return pe;
				physical_length += schemeCableLink.getPhysicalLength();
			}
			else
			{
				SchemeLink schemeLink =
						(SchemeLink )Pool.get(SchemeLink.typ, pe.linkId);
				if(schemeLink == null)
				{
					Log.debugMessage("Something wrong... - schemeLink == null");
					return pe;
				}
				if(!vec.contains(schemeLink))
					return pe;
				physical_length += schemeLink.getPhysicalLength();
			}
			if(pathelements.hasMoreElements())
				pe = (PathElement )pathelements.nextElement();
			else
				return null;
		}

		return pe;//stub
	}
*/	
}
