/**
 * $Id: MapViewController.java,v 1.28 2005/06/16 10:57:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.client.map.command.action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemePathCommand;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;

/**
 * Класс используется для управления информацией о канализационной
 * прокладке кабелей и положении узлов и других топологических объектов.
 * @author $Author: krupenn $
 * @version $Revision: 1.28 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public final class MapViewController {
	public static final String ELEMENT_SITENODE = "sitenode";
	public static final String ELEMENT_WELL = "well";
	public static final String ELEMENT_PIQUET = "piquet";
	public static final String ELEMENT_PHYSICALLINK = "physicallink";
	public static final String ELEMENT_COLLECTOR = "collector";
	public static final String ELEMENT_CABLEPATH = "cablepath";
	public static final String ELEMENT_TOPOLOGICALNODE = "topologicalnode";
	public static final String ELEMENT_MARK = "mark";
	public static final String ELEMENT_MEASUREMENTPATH = "measurementpath";
	public static final String ELEMENT_MARKER = "marker";
	public static final String ELEMENT_CABLEINLET = "cableinlet";
	public static final String ELEMENT_NODELINK = "nodelink";

	public static String getMapElementReadableType(MapElement mapElement) {
		if(mapElement instanceof SiteNode) {
			SiteNode site = (SiteNode )mapElement;
			return ((SiteNodeType )site.getType()).getName();
		}
		return LangModelMap.getString(MapViewController.getMapElementType(mapElement));
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
		return "";
	}

	/** Хэш-таблица контроллеров элементов карты. */
	private java.util.Map ctlMap = new HashMap();
	
	/** Instance. */
//	private static MapViewController instance = null;
	
	/** Ссылка на логический слой, на котором отображается вид. */
	protected LogicalNetLayer logicalNetLayer = null;

	/** Хранимый объект. */
	private MapView mapView;

	private NetMapViewer netMapViewer;

	/**
	 * Приветный конструктор. Использовать 
	 * {@link MapViewController#getInstance()}.
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
	
	/**
	 * Instance getter.
	 * @return контроллер вида
	 */
//	private static MapViewController getInstance() {
//		return instance;
//	}

	public static MapViewController createInstance(NetMapViewer netMapViewer) {
//		if(instance != null)
//		{
//			instance.setNetMapViewer(netMapViewer);
//
//			TopologicalNodeController.getInstance().setNetMapViewer(netMapViewer);
//			SiteNodeController.getInstance().setNetMapViewer(netMapViewer);
//			NodeLinkController.getInstance().setNetMapViewer(netMapViewer);
//			PhysicalLinkController.getInstance().setNetMapViewer(netMapViewer);
//			MarkController.getInstance().setNetMapViewer(netMapViewer);
//			CollectorController.getInstance().setNetMapViewer(netMapViewer);
//
//			CableController.getInstance().setNetMapViewer(netMapViewer);
//			MeasurementPathController.getInstance().setNetMapViewer(netMapViewer);
//			UnboundNodeController.getInstance().setNetMapViewer(netMapViewer);
//			UnboundLinkController.getInstance().setNetMapViewer(netMapViewer);
//			MarkerController.getInstance().setNetMapViewer(netMapViewer);
//			return instance;
//		}
		MapViewController instance = new MapViewController(netMapViewer);

		return instance;
	}

	/**
	 * Получить контроллер для элемента карты.
	 * @param me элемент карты
	 * @return контроллер
	 */
	public MapElementController getController(MapElement me) {
		return (MapElementController)ctlMap.get(me.getClass());
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

		this.mapView.setLongitude(this.logicalNetLayer.getMapContext().getCenter().getX());
		this.mapView.setLatitude(this.logicalNetLayer.getMapContext().getCenter().getY());

		this.mapView.setScale(this.logicalNetLayer.getMapContext().getScale());

		this.mapView.revert();
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
	 *  см. {@link ConfigurationStorableObjectPool#getStorableObject(Identifier, boolean)}
	 * @throws com.syrus.AMFICOM.general.DatabaseException
	 *  см. {@link ConfigurationStorableObjectPool#getStorableObject(Identifier, boolean)}
	 */
	public MeasurementPath getMeasurementPathByMonitoredElementId(Identifier meId)
			throws ApplicationException {
		MeasurementPath path = null;
		MonitoredElement me = (MonitoredElement )
			StorableObjectPool.getStorableObject(meId, true);
		if(me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
			Identifier tpId = (Identifier )(me.getMonitoredDomainMemberIds().iterator().next());
			TransmissionPath tp = (TransmissionPath )
				StorableObjectPool.getStorableObject(tpId, true);
			if(tp != null) {
				for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();) {
					MeasurementPath mp = (MeasurementPath)it.next();
					if(mp.getSchemePath().getTransmissionPath().equals(tp)) {
						path = mp;
						break;
					}
				}
			}
		}

		return path;
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
			Scheme sch = (Scheme )this.mapView.getSchemes().iterator().next();
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

		for(Iterator it = SchemeUtils.getTopologicalElements(scheme).iterator(); it.hasNext();) {
			SchemeElement element = (SchemeElement )it.next();
			scanElement(element);
		}
		scanCables(scheme);
	}

	/**
	 * Сканировать кабель на предмет его привязки к тоннелю или нанесения
	 * нового непривязанного кабеля ка топологическую схему.
	 * @param schemeCableLink кабель
	 */
	public void scanCable(SchemeCableLink schemeCableLink) {
		SiteNode cableStartNode = this.mapView.getStartNode(schemeCableLink);
		SiteNode cableEndNode = this.mapView.getEndNode(schemeCableLink);
		CablePath cp = this.mapView.findCablePath(schemeCableLink);
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
	}
	
	/**
	 * Сканировать все кабели схемы.
	 * 
	 * @param scheme схема
	 */
	public void scanCables(Scheme scheme) {
		for(Iterator it = SchemeUtils.getTopologicalCableLinks(scheme).iterator(); it.hasNext();) {
			SchemeCableLink scl = (SchemeCableLink )it.next();
			scanCable(scl);
		}
		scanPaths(scheme);
	}

	/**
	 * Сканировать схемный путь на предмет его привязки к тоннелям.
	 * @param schemePath схемный путь
	 */
	public void scanPath(SchemePath schemePath) {
		SiteNode pathStartNode = this.mapView.getStartNode(schemePath);
		SiteNode pathEndNode = this.mapView.getEndNode(schemePath);
		MeasurementPath mp = this.mapView.findMeasurementPath(schemePath);
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
	}

	/**
	 * Сканировать все схемные пути на схеме.
	 * 
	 * @param scheme схема
	 */
	public void scanPaths(Scheme scheme) {
		for(Iterator it = SchemeUtils.getTopologicalPaths(scheme).iterator(); it.hasNext();) {
			SchemePath path = (SchemePath )it.next();
			scanPath(path);
		}
	}

	/**
	 * Убрать все схемные пути заданной схемы из вида.
	 * @param scheme схема
	 */
	public void removePaths(Scheme scheme) {
		Collection schemePaths = SchemeUtils.getTopologicalPaths(scheme);
		for(Iterator it = schemePaths.iterator(); it.hasNext();) {
			SchemePath path = (SchemePath )it.next();
			MeasurementPath mp = this.mapView.findMeasurementPath(path);
			if(mp != null) {
				unplaceElement(mp);
			}
		}
	}

	/**
	 * Убрать все кабели заданной схемы из вида.
	 * 
	 * @param scheme схема
	 */
	public void removeCables(Scheme scheme) {
		Collection schemeCables = SchemeUtils.getTopologicalCableLinks(scheme);
		for(Iterator it = schemeCables.iterator(); it.hasNext();) {
			SchemeCableLink scl = (SchemeCableLink )it.next();
			CablePath cp = this.mapView.findCablePath(scl);
			if(cp != null) {
				unplaceElement(cp);
			}
		}
	}

	/**
	 * Убрать все элементы заданной схемы из вида.
	 * 
	 * @param scheme схема
	 */
	public void removeElements(Scheme scheme) {
		Collection schemeElements = SchemeUtils.getTopologicalElements(scheme);
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
	}

	/**
	 * Разместить элемент на карте.
	 * @param se элемент схемы
	 * @param point географическая точка
	 */
	public void placeElement(SchemeElement se, DoublePoint point) {
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.setNetMapViewer(this.netMapViewer);
		cmd.execute();
	}

	/**
	 * Убрать элемент схемы из вида. Подразумевает отвязку от элемента карты
	 * или удаление непривязанного элемента.
	 * @param node элемент карты
	 * @param se элемент схемы
	 */
	public void unplaceElement(SiteNode node, SchemeElement se) {
		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Разместить кабель на топологической схеме.
	 * @param scl кабель
	 */
	public void placeElement(SchemeCableLink scl) {
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}
	
	/**
	 * Убрать кабель из вида. Подразумевает отавязку от всех тоннелей 
	 * и удаление непривязанных фрагментов.
	 * @param cablePath кабель
	 */
	public void unplaceElement(CablePath cablePath) {
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cablePath);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Разместить схемный путь на карте. Используется при переносе 
	 * (drag/drop).
	 * @param sp схемный путь
	 */
	public void placeElement(SchemePath sp) {
		PlaceSchemePathCommand cmd = new PlaceSchemePathCommand(sp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Убрать топологический путь из вида.
	 * @param mp топологический путь
	 */
	public void unplaceElement(MeasurementPath mp) {
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * @param netMapViewer The netMapViewer to set.
	 */
	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
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
					System.out.println("Something wrong... - schemeCableLink == null");
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
					System.out.println("Something wrong... - schemeLink == null");
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
