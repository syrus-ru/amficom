/**
 * $Id: MapView.java,v 1.31 2005/01/30 15:38:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс используется для хранения объектов, отображаемых на 
 * топологической карте. Jбъекты включают в себя:
 * 		- объект карты Map, то есть прокладку канализационную
 *      - набор физических схем Scheme, которые проложены по данному
 *        контексту
 * Объект <code>MapView</code> являтся вроппером для класса 
 * <code>{@link com.syrus.AMFICOM.mapview.MapView}</code>
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.31 $, $Date: 2005/01/30 15:38:18 $
 * @module mapviewclient_v1
 * @deprecated use {@link LogicalNetLayer#getMapViewController()}
 */
public final class MapView
{
	private static final long serialVersionUID = 01L;

	/** Хранимый объект. */
	private com.syrus.AMFICOM.mapview.MapView mapViewStorable;

	/** Ссылка на логический слой, на котором отображается вид. */
	protected LogicalNetLayer logicalNetLayer = null;
	
	/** Список кабелей. */
	protected List cablePaths = new LinkedList();
	
	/** Список измерительных путей. */
	protected List measurementPaths = new LinkedList();
	
	/** Список маркеров. */
	protected List markers = new LinkedList();

	/**
	 * Конструктор для создания нового объекта пользователем.
	 * @param creatorId идентификатор пользователя
	 * @param domainId идентификатор домена
	 * @param map Ссылка на топологическую схему
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *  см. {@link com.syrus.AMFICOM.mapview.MapView#createInstance(
	 *  	Identifier,
	 *		Identifier,
	 *		String,
	 *		String,
	 *		double,
	 *		double,
	 *		double,
	 *		double,
	 *		Map)}
	 */
	public MapView(Identifier creatorId, Identifier domainId, Map map)
		throws CreateObjectException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView(" + creatorId + ", " + domainId + ", " + map + ")");

		mapViewStorable = com.syrus.AMFICOM.mapview.MapView.createInstance(
			creatorId,
			domainId,
			LangModelMap.getString("New"),
			"",
			0.0D,
			0.0D,
			1.0D,
			1.0D,
			map);

		setLogicalNetLayer(null);
	}

	/**
	 * Конструктор для создания вида из БД.
	 * @param mapViewStorable хранимый объект вида
	 */
	public MapView(com.syrus.AMFICOM.mapview.MapView mapViewStorable)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView()");

		this.mapViewStorable = mapViewStorable;
		setLogicalNetLayer(null);
	}

	/**
	 * Получить название вида.
	 * @return название
	 */
	public String getName()
	{
		return mapViewStorable.getName();
	}
	
	/**
	 * Установить новое название вида.
	 * @param name новое название
	 */
	public void setName(String name)
	{
		mapViewStorable.setName(name);
	}
	
	/**
	 * Получить идентификатор вида.
	 * @return идентификатор
	 */
	public Identifier getId()
	{
		return mapViewStorable.getId();
	}
	
	/**
	 * Получить идентификатор домена.
	 * @return идентификатор домена
	 */
	public Identifier getDomainId()
	{
		return mapViewStorable.getDomainId();
	}
	
	/**
	 * Установить идентификатор домена.
	 * @param domainId новый идентификатор домена
	 */
	public void setDomainId(Identifier domainId)
	{
		mapViewStorable.setDomainId(domainId);
	}
	
	/**
	 * Получить время последнего изменения.
	 * @return время последнего изменения
	 */
	public Date getModified()
	{
		return mapViewStorable.getModified();
	}

	/**
	 * Получить топологическую схему.
	 * @return топологическая схема
	 */
	public Map getMap()
	{
		return mapViewStorable.getMap();
	}
	
	/**
	 * Установить топологическую схему.
	 * @param map топологическая схема
	 */
	public void setMap(Map map)
	{
		mapViewStorable.setMap(map);
		scanSchemes();
	}
	
	/**
	 * Получить список схем.
	 * @return список схем
	 */
	public List getSchemes()
	{
		return mapViewStorable.getSchemes();
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
	 * Возвращает масштаб вида.
	 * @return масштаб
	 */
	public double getScale()
	{
		return mapViewStorable.getScale();
	}
	
	/**
	 * Установить масштаб вида.
	 * @param scale масштаб
	 */
	public void setScale(double scale)
	{
		mapViewStorable.setScale(scale);
	}
	
	/**
	 * Установить центральную точку вида в географических координатах.
	 * @param center центр вида
	 */
	public void setCenter(DoublePoint center)
	{
		mapViewStorable.setLongitude(center.getX());
		mapViewStorable.setLatitude(center.getY());
	}

	/**
	 * Получить центральную точку вида в географических координатах.
	 * @return центр вида
	 */
	public DoublePoint getCenter()
	{
		return new DoublePoint(
			mapViewStorable.getLongitude(),
			mapViewStorable.getLatitude());
	}

	/**
	 * Установить ссылку на логической слой, что соответствует открытию 
	 * вида в окне карты.
	 * @param logical логическая схема
	 */
	public void setLogicalNetLayer(LogicalNetLayer logical)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setLogicalNetLayer(" + logical + ")");
		
		logicalNetLayer = logical;

		if(logicalNetLayer != null)
		{
			mapViewStorable.setLongitude(logicalNetLayer.getCenter().getX());
			mapViewStorable.setLatitude(logicalNetLayer.getCenter().getY());

			mapViewStorable.setScale(logicalNetLayer.getScale());

			revert();
		}
	}

	/**
	 * Получить ссылку на слой логической сети.
	 * @return логический слой
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	/**
	 * Сканировать схемы, включенный в вид, на предмет визуализации элементов схем.
	 */
	public void scanSchemes()
	{
		for(Iterator it = mapViewStorable.getSchemes().iterator(); it.hasNext();)
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
		mapViewStorable.addScheme(sch);
		scanElements(sch);
	}

	/**
	 * Убрать схему из вида.
	 * @param sch схема
	 */
	public void removeScheme(Scheme sch)
	{
		mapViewStorable.removeScheme(sch);
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
		while(getSchemes().size() != 0)
		{
			Scheme sch = (Scheme )getSchemes().get(0);
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
		SiteNode node = findElement(schemeElement);
		if(node == null)
		{
			if(schemeElement.equipmentImpl() != null)
			{
				Equipment equipment = schemeElement.equipmentImpl();
				if(equipment.getLongitude() != 0.0D
					|| equipment.getLatitude() != 0.0D)
				{
					placeElement(
						schemeElement, 
						new DoublePoint(
							equipment.getLongitude(), 
							equipment.getLatitude()));
				}
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
		SiteNode[] mne = getSideNodes(schemeCableLink);
		CablePath cp = findCablePath(schemeCableLink);
		if(cp == null)
		{
			if(mne[0] != null && mne[1] != null)
			{
				placeElement(schemeCableLink);
			}
		}
		else
		{
			if(mne[0] == null || mne[1] == null)
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
		SiteNode[] mne = getSideNodes(schemePath);
		MeasurementPath mp = findMeasurementPath(schemePath);
		if(mp == null)
		{
			if(mne[0] != null && mne[1] != null)
			{
				placeElement(schemePath);
			}
		}
		else
		{
			if(mne[0] == null || mne[1] == null)
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
			MeasurementPath mp = findMeasurementPath(path);
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
			CablePath cp = findCablePath(scl);
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
			SiteNode site = findElement(se);
			if(site != null)
			{
				if(site instanceof UnboundNode)
				{
					RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(site);
					cmd.setLogicalNetLayer(logicalNetLayer);
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
		cmd.setLogicalNetLayer(logicalNetLayer);
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
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Разместить кабель на топологической схеме.
	 * @param scl кабель
	 */
	public void placeElement(SchemeCableLink scl)
	{
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}
	
	/**
	 * Убрать кабель из вида. Подразумевает отавязку от всех тоннелей 
	 * и удаление непривязанных фрагментов.
	 * @param cp кабель
	 */
	public void unplaceElement(CablePath cp)
	{
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cp);
		cmd.setLogicalNetLayer(logicalNetLayer);
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
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Убрать топологический путь из вида.
	 * @param mp топологический путь
	 */
	public void unplaceElement(MeasurementPath mp)
	{
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Коррекция начального и конечного узлов топологической прокладки кабеля
	 * по элементам карты, в которых размещены начальный и конечный элемент
	 * схемного кабеля.
	 * 
	 * @param cable топологический кабель
	 * @param scl схемный кабель
	 */
	public void correctStartEndNodes(CablePath cable, SchemeCableLink scl)
	{
		SiteNode[] node = getSideNodes(scl);
		if(node[0] != null && node[1] != null)
		{
			cable.setStartNode(node[0]);
			cable.setEndNode(node[1]);
		}
	}
	
	/**
	 * Поле сделано статическим, чтобы каждый раз при вызове метода 
	 * getSideNodes не создавался новый массив ссылок
	 */
	private static SiteNode[] linkSideNodes = new SiteNode[2];
	
	/**
	 * Возвращает массив из двух топологических элементов, в которых 
	 * расположены концевые элементы кабеля. Если элемент не найден (не
	 * нанесен на карту), соответствующий элемент массива равен null.
	 * 
	 * @param scl кабель
	 * @return массив концевых узлов:
	 * 	linkSideNodes[0] - начальный узел
	 *  linkSideNodes[1] - конечный узел
	 */
	public SiteNode[] getSideNodes(SchemeCableLink scl)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalCableLinks(sch).contains(scl))
				{
					SchemeElement se = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(sch, scl.sourceAbstractSchemePort().schemeDevice()));
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(sch, scl.targetAbstractSchemePort().schemeDevice()));
					linkSideNodes[1] = findElement(se2);
					break;
				}
			}
		}
		catch(Exception ex)
		{
			linkSideNodes[0] = null;
			linkSideNodes[1] = null;
		}
		return linkSideNodes;
	}
	
	/**
	 * Возвращает массив из двух топологических элементов, в которых 
	 * расположены концевые элементы схемного пути. Если элемент не найден (не
	 * нанесен на карту), соответствующий элемент массива равен null.
	 * @param path путь
	 * @return массив концевых узлов:
	 * 	linkSideNodes[0] - начальный узел
	 *  linkSideNodes[1] - конечный узел
	 */
	public SiteNode[] getSideNodes(SchemePath path)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalPaths(sch).contains(path))
				{
					SchemeElement se = SchemeUtils.getTopologicalElement(
							sch,
							path.startDevice());
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = SchemeUtils.getTopologicalElement(
							sch,
							path.endDevice());
					linkSideNodes[1] = findElement(se2);
					break;
				}
			}
		}
		catch(Exception ex)
		{
			linkSideNodes[0] = null;
			linkSideNodes[1] = null;
		}
		return linkSideNodes;
	}
	
	/**
	 * Найти элемент карты, к которому привязан данный схемный элемент.
	 * @param se элемент схемы
	 * @return узел.
	 * 	null если элемент не найден
	 */
	public SiteNode findElement(SchemeElement se)
	{
		if(se == null)
			return null;
		for(Iterator it = getMap().getSiteNodes().iterator(); it.hasNext();)
		{
			SiteNode node = (SiteNode )it.next();
			if(node instanceof UnboundNode)
				if(((UnboundNode)node).getSchemeElement().equals(se))
					return node;
			if(se.siteNodeImpl() != null
				&& se.siteNodeImpl().equals(node))
						return node;
		}
		return null;
	}

	/**
	 * Найти элемент кабеля на карте.
	 * @param scl кабель
	 * @return топологический кабель
	 */
	public CablePath findCablePath(SchemeCableLink scl)
	{

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getSchemeCableLink().equals(scl))
					return cp;
		}
		return null;
	}

	/**
	 * Найти топологический путь, соответствующий схемному пути.
	 * @param path схемный путь
	 * @return топологический путь
	 */
	public MeasurementPath findMeasurementPath(SchemePath path)
	{
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			if(mp.getSchemePath().equals(path))
				return mp;
		}
		return null;
	}

	/**
	 * Получить список топологических кабелей.
	 * @return список топологических кабелей
	 */
	public List getCablePaths()
	{
		return cablePaths;
	}

	/**
	 * Добавить новый топологический кабель.
	 * @param cable топологический кабель
	 */
	public void addCablePath(CablePath cable)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addTransmissionPath(" + cable + ")");

		cablePaths.add(cable);
	}

	/**
	 * Удалить топологический кабель.
	 * @param cable топологический кабель
	 */
	public void removeCablePath(CablePath cable)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeCablePath(" + cable + ")");

		cablePaths.remove(cable);
		cable.setSelected(false);
	}

	/**
	 * Получить список топологических кабелей, проложенных по указанной
	 * линии.
	 * @param link линия
	 * @return список топологических кабелей
	 */
	public List getCablePaths(PhysicalLink link)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + link + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getLinks().contains(link))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * Получить список топологических кабелей, проходящих через указанный узел.
	 * @param node узел
	 * @return список топологических кабелей
	 */
	public List getCablePaths(AbstractNode node)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + node + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodes();
			if(cp.getSortedNodes().contains(node))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * Получить список топологических кабелей, проходящих через указанный 
	 * фрагмент линии.
	 * @param nodeLink фрагмент линии
	 * @return список топологических кабелей
	 */
	public List getCablePaths(NodeLink nodeLink)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + nodeLink + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodeLinks();
			if(cp.getSortedNodeLinks().contains(nodeLink))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * Получить список путей тестирования.
	 * @return список путей тестирования
	 */
	public List getMeasurementPaths()
	{
		return measurementPaths;
	}

	/**
	 * Добавить новый путь тестирования.
	 * @param path новый путь тестирования
	 */
	public void addMeasurementPath(MeasurementPath path)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addTransmissionPath(" + path + ")");

		measurementPaths.add(path);
	}

	/**
	 * Удалить путь тестирования.
	 * @param path путь тестирования
	 */
	public void removeMeasurementPath(MeasurementPath path)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeTransmissionPath(" + path + ")");

		measurementPaths.remove(path);
		path.setSelected(false);
	}

	/**
	 * Получить список топологических путей, проходящих через указанный 
	 * топологический кабель.
	 * @param cpath топологический кабель
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(CablePath cpath)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + cpath + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			if(mp.getSortedCablePaths().contains(cpath))
				returnVector.add(mp);
		}
		return returnVector;
	}

	/**
	 * Получить список топологических путей, проходящих через указанную линию.
	 * @param link линия
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(PhysicalLink link)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + link + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				if(cp.getLinks().contains(link))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * Получить список топологических путей, проходящих через указанный узел.
	 * @param node узел
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(AbstractNode node)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + node + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodes();
				if(cp.getSortedNodes().contains(node))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * Получить список топологических путей, проходящих через указанный 
	 * фрагмент линии.
	 * @param nodeLink фрагмент линии
	 * @return список топологических путей
	 */
	public List getMeasurementPaths(NodeLink nodeLink)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + nodeLink + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodeLinks();
				if(cp.getSortedNodeLinks().contains(nodeLink))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
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
				for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
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
	 * Удалить все маркеры.
	 */
	public void removeMarkers()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarkers()");
		getMap().getNodes().removeAll(markers);
		markers.clear();
	}

	/**
	 * Удалить путь тестирования.
	 * @param marker маркер
	 */
	public void removeMarker(Marker marker)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarker(" + marker + ")");

		markers.remove(marker);
		getMap().removeNode(marker);
		marker.setSelected(false);
	}

	/**
	 * Получить все маркеры.
	 * @return список маркеров
	 */
	public List getMarkers()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarkers()");
		
		return markers;
	}

	/**
	 * Добавить новый маркер.
	 * @param marker маркер
	 */
	public void addMarker(Marker marker)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addMarker(" + marker + ")");

		markers.add(marker);
		getMap().addNode(marker);
	}

	/**
	 * Получить маркер по идентификатору.
	 * @param markerId идентификатор
	 * @return маркер
	 */
	public Marker getMarker(Identifier markerId)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getMarker(" + markerId + ")");
		
		Iterator e = markers.iterator();
		while( e.hasNext())
		{
			Marker marker = (Marker)e.next();
			if ( marker.getId().equals(markerId))
				return marker;
		}
		return null;
	}

	/**
	 * Получить список всех олементов контекста карты.
	 * @return список всех топологических элементов
	 */
	public List getAllElements()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getAllElements()");
		
		List returnVector = getMap().getAllElements();
		
		Iterator e;

		e = getCablePaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		e = getMeasurementPaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		e = markers.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		return returnVector;
	}

	/**
	 * Отменить выбор всем элементам.
	 */
	public void deselectAll()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"deselectAll()");
		
		Iterator e = getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			mapElement.setSelected(false);
		}
		getMap().clearSelection();
	}

	/**
	 * Remove all temporary objects on mapview when mapview was edited and
	 * closed without saving.
	 */
	public void revert()
	{
		removeMarkers();
	}
	/**
	 * Установить описание вида.
	 * @param description описание вида
	 */
	public void setDescription(String description)
	{
		mapViewStorable.setDescription(description);
	}


	/**
	 * Получить описание вида.
	 * @return описание вида
	 */
	public String getDescription()
	{
		return mapViewStorable.getDescription();
	}

	/**
	 * Получить идентификатор создателя вида.
	 * @return идентификатор создателя вида
	 */
	public Identifier getCreatorId()
	{
		return mapViewStorable.getCreatorId();
	}


	/**
	 * получить время создания вида.
	 * @return время создания вида
	 */
	public Date getCreated()
	{
		return mapViewStorable.getCreated();
	}


	/**
	 * Получить флаг измененности вида.
	 * @return флаг измененности вида
	 */
	public boolean isChanged()
	{
		return mapViewStorable.isChanged();
	}


	/**
	 * получить хранимый объект вида.
	 * @return хранимый объект вида
	 */
	public com.syrus.AMFICOM.mapview.MapView getMapViewStorable()
	{
		return mapViewStorable;
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
