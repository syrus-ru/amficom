/**
 * $Id: MapViewController.java,v 1.11 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Класс используется для управления информацией о канализационной
 * прокладке кабелей и положении узлов и других топологических объектов.
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public final class MapViewController
{
	/** Хэш-таблица контроллеров элементов карты. */
	private static java.util.Map ctlMap = new HashMap();
	
	/** Instance. */
	private static MapViewController instance = null;
	
	/** Ссылка на логический слой, на котором отображается вид. */
	protected LogicalNetLayer logicalNetLayer = null;

	/** Хранимый объект. */
	private MapView mapView;

	/**
	 * Приветный конструктор. Использовать 
	 * {@link MapViewController#getInstance(LogicalNetLayer)}.
	 * @param logicalNetLayer логический слой
	 */
	private MapViewController(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}
	
	/**
	 * Instance getter.
	 * @param logicalNetLayer логический слой
	 * @return контроллер вида
	 */
	public static MapViewController getInstance(LogicalNetLayer logicalNetLayer)
	{
		if(instance == null)
			instance = new MapViewController(logicalNetLayer);
		return instance;
	}

	static
	{
		ctlMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			TopologicalNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			SiteNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			NodeLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			PhysicalLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Mark.class,
			MarkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Collector.class,
			CollectorController.getInstance());

		ctlMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
			CableController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.MeasurementPath.class,
			MeasurementPathController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.UnboundNode.class,
			UnboundNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.UnboundLink.class,
			UnboundLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.Marker.class,
			MarkerController.getInstance());
	}

	/**
	 * Получить контроллер для элемента карты.
	 * @param me элемент карты
	 * @return контроллер
	 */
	public MapElementController getController(MapElement me)
	{
		MapElementController controller = (MapElementController)ctlMap.get(me.getClass());
		if(controller != null)
			controller.setLogicalNetLayer(this.logicalNetLayer);
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
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		getController(me).paint(me, g, visibleBounds);
	}

	/**
	 * Возвращает флаг, указывающий, что точка currentMousePoint находится
	 * в определенных границах элемента. Для узла границы определяются
	 * размерами иконки, для линии дельта-окрестностью линии. Дельта задается
	 * полем {@link com.syrus.AMFICOM.Client.Map.MapPropertiesManager#getMouseTolerancy()}.
	 * @param me элемент карты
	 * @param currentMousePoint точка в экранных координатах
	 * @return <code>true</code>, если точка на элементе карты, иначе 
	 * <code>false</code>
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
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
	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		return getController(me).isElementVisible(me, visibleBounds);
	}

	/**
	 * Получить текст всплывающей подсказки для элемента карты.
	 * @param me элемент карты
	 * @return строка для всплывающей подсказки
	 */
	String getToolTipText(MapElement me)
	{
		return getController(me).getToolTipText(me);
	}


	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapViewPanel";

	/**
	 * Получить имя класса, описывающего панель свойств вида.
	 * @return имя класса
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * Установить вид, с которым будет работать контроллер.
	 * @param mapView вид
	 */
	public void setMapView(com.syrus.AMFICOM.mapview.MapView mapView)
	{
		this.mapView = mapView;

		this.mapView.setLongitude(this.logicalNetLayer.getCenter().getX());
		this.mapView.setLatitude(this.logicalNetLayer.getCenter().getY());

		this.mapView.setScale(this.logicalNetLayer.getScale());

		this.mapView.revert();
	}
	
	/**
	 * Установить топологическую схему.
	 * @param map топологическая схема
	 */
	public void setMap(Map map)
	{
		this.mapView.setMap(map);
		scanSchemes();
	}

	/**
	 * Получить хранимый объект вида.
	 * @return хранимый объект вида
	 */
	public com.syrus.AMFICOM.mapview.MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * Конструктор для создания нового объекта пользователем.
	 * @param creatorId идентификатор пользователя
	 * @param domainId идентификатор домена
	 * @param map Ссылка на топологическую схему
	 * @return new MapView
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *    см. {@link com.syrus.AMFICOM.mapview.MapView#createInstance(
	 *    	Identifier,
	 *  		Identifier,
	 *  		String,
	 *  		String,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		Map)}
	 * @deprecated use {@link com.syrus.AMFICOM.mapview.MapView#createInstance(
	 *    	Identifier,
	 *  		Identifier,
	 *  		String,
	 *  		String,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		Map)}
	 */
	public static MapView createMapView(Identifier creatorId, Identifier domainId, Map map)
		throws CreateObjectException
	{
		return com.syrus.AMFICOM.mapview.MapView.createInstance(
			creatorId,
			domainId,
			LangModelMap.getString("New"),
			"",
			0.0D,
			0.0D,
			1.0D,
			1.0D,
			map);
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
		throws CommunicationException, DatabaseException
	{
		MeasurementPath path = null;
		MonitoredElement me = (MonitoredElement )
			ConfigurationStorableObjectPool.getStorableObject(meId, true);
		if(me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH))
		{
			Identifier tpId = (Identifier )(me.getMonitoredDomainMemberIds().get(0));
			TransmissionPath tp = (TransmissionPath )
				ConfigurationStorableObjectPool.getStorableObject(tpId, true);
			if(tp != null)
			{
				for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();)
				{
					MeasurementPath mp = (MeasurementPath)it.next();
					if(mp.getSchemePath().pathImpl().equals(tp))
					{
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
	public void scanSchemes()
	{
		for(Iterator it = this.mapView.getSchemes().iterator(); it.hasNext();)
		{
			scanElements((Scheme )it.next());
		}
	}


	/**
	 * Добавить схему в вид.
	 * @param sch схема
	 */
	public void addScheme(Scheme sch)
	{
		this.mapView.addScheme(sch);
		scanElements(sch);
	}

	/**
	 * Убрать схему из вида.
	 * @param sch схема
	 */
	public void removeScheme(Scheme sch)
	{
		this.mapView.removeScheme(sch);
		removePaths(sch);
		removeCables(sch);
		removeElements(sch);
	}

	/**
	 * Убрать все схемы из вида. Реализуется поэлементным удалением
	 * всех схем из вида, поскольку удаление каждой отдельной
	 * схемы включает в себя сканирование элементов схемы и их удаление
	 * из вида.
	 */
	public void removeSchemes()
	{
		while(this.mapView.getSchemes().size() != 0)
		{
			Scheme sch = (Scheme )this.mapView.getSchemes().get(0);
			removeScheme(sch);
		}
	}

	/**
	 * Сканировать элемент схемы на предмет его привязки к элементу карты или
	 * нанесения нового непривязанного элемента.
	 * @param schemeElement элемент схемы
	 */
	public void scanElement(SchemeElement schemeElement)
	{
		SiteNode node = this.mapView.findElement(schemeElement);
		if(node == null)
		{
			Equipment equipment = schemeElement.equipmentImpl();
			if(equipment != null 
				&& (equipment.getLongitude() != 0.0D
					|| equipment.getLatitude() != 0.0D) )
			{
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
	public void scanElements(Scheme scheme)
	{
		
		for(Iterator it = SchemeUtils.getTopologicalElements(scheme).iterator(); it.hasNext();)
		{
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
	public void scanCable(SchemeCableLink schemeCableLink)
	{
		SiteNode cableStartNode = this.mapView.getStartNode(schemeCableLink);
		SiteNode cableEndNode = this.mapView.getEndNode(schemeCableLink);
		CablePath cp = this.mapView.findCablePath(schemeCableLink);
		if(cp == null)
		{
			if(cableStartNode != null && cableEndNode != null)
			{
				placeElement(schemeCableLink);
			}
		}
		else
		{
			if(cableStartNode == null || cableEndNode == null)
			{
				unplaceElement(cp);
			}
			else
			{
				placeElement(schemeCableLink);
			}
		}
	}
	
	/**
	 * Сканировать все кабели схемы.
	 * @param scheme схема
	 */
	public void scanCables(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalCableLinks(scheme).iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			scanCable(scl);
		}
		scanPaths(scheme);
	}

	/**
	 * Сканировать схемный путь на предмет его привязки к тоннелям.
	 * @param schemePath схемный путь
	 */
	public void scanPath(SchemePath schemePath)
	{
		SiteNode pathStartNode = this.mapView.getStartNode(schemePath);
		SiteNode pathEndNode = this.mapView.getEndNode(schemePath);
		MeasurementPath mp = this.mapView.findMeasurementPath(schemePath);
		if(mp == null)
		{
			if(pathStartNode != null && pathEndNode != null)
			{
				placeElement(schemePath);
			}
		}
		else
		{
			if(pathStartNode == null || pathEndNode == null)
			{
				unplaceElement(mp);
			}
			else
			{
				placeElement(schemePath);
			}
		}
	}

	/**
	 * Сканировать все схемные пути на схеме.
	 * @param scheme схема
	 */
	public void scanPaths(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalPaths(scheme).iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			scanPath(path);
		}
	}

	/**
	 * Убрать все схемные пути заданной схемы из вида.
	 * @param scheme схема
	 */
	public void removePaths(Scheme scheme)
	{
		Collection schemePaths = SchemeUtils.getTopologicalPaths(scheme);
		for(Iterator it = schemePaths.iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			MeasurementPath mp = this.mapView.findMeasurementPath(path);
			if(mp != null)
			{
				unplaceElement(mp);
			}
		}
	}

	/**
	 * Убрать все кабели заданной схемы из вида.
	 * @param scheme схема
	 */
	public void removeCables(Scheme scheme)
	{
		Collection schemeCables = SchemeUtils.getTopologicalCableLinks(scheme);
		for(Iterator it = schemeCables.iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			CablePath cp = this.mapView.findCablePath(scl);
			if(cp != null)
			{
				unplaceElement(cp);
			}
		}
	}

	/**
	 * Убрать все элементы заданной схемы из вида.
	 * @param scheme схема
	 */
	public void removeElements(Scheme scheme)
	{
		Collection schemeElements = SchemeUtils.getTopologicalElements(scheme);
		for(Iterator it = schemeElements.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement )it.next();
			SiteNode site = this.mapView.findElement(se);
			if(site != null)
			{
				if(site instanceof UnboundNode)
				{
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
	public void placeElement(SchemeElement se, DoublePoint point)
	{
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Убрать элемент схемы из вида. Подразумевает отвязку от элемента карты
	 * или удаление непривязанного элемента.
	 * @param node элемент карты
	 * @param se элемент схемы
	 */
	public void unplaceElement(SiteNode node, SchemeElement se)
	{
		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Разместить кабель на топологической схеме.
	 * @param scl кабель
	 */
	public void placeElement(SchemeCableLink scl)
	{
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}
	
	/**
	 * Убрать кабель из вида. Подразумевает отавязку от всех тоннелей 
	 * и удаление непривязанных фрагментов.
	 * @param cablePath кабель
	 */
	public void unplaceElement(CablePath cp)
	{
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Разместить схемный путь на карте. Используется при переносе 
	 * (drag/drop).
	 * @param sp схемный путь
	 */
	public void placeElement(SchemePath sp)
	{
		PlaceSchemePathCommand cmd = new PlaceSchemePathCommand(sp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Убрать топологический путь из вида.
	 * @param mp топологический путь
	 */
	public void unplaceElement(MeasurementPath mp)
	{
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
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
