/**
 * $Id: MapView.java,v 1.26 2004/12/24 15:42:14 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Model.Environment;
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
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
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
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;

/**
 * Класс используется для хранения объектов, отображаемых на 
 * топологической карте. объекты включают в себя:
 * 		- объект карты Map, то есть прокладку канализационную
 *      - набор физических схем Scheme, которые проложены по данному
 *        контексту
 * 
 * 
 * 
 * @version $Revision: 1.26 $, $Date: 2004/12/24 15:42:14 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapView
{
	private static final long serialVersionUID = 01L;

	private com.syrus.AMFICOM.mapview.MapView mapViewStorable;

	protected LogicalNetLayer logicalNetLayer = null;
	
	protected boolean isOpened = false;

	protected boolean changed = false;

	protected Map map;
	protected List schemes = new LinkedList();

	protected List cablePaths = new LinkedList();
	protected List measurementPaths = new LinkedList();
	protected List markers = new LinkedList();

	/**
	 * 
	 * @param logical comments
	 */
	public MapView(LogicalNetLayer logical, Map map)
		throws CreateObjectException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView(" + logical + ")");

		AccessIdentifier_Transferable ait = 
			logical.getContext().getSessionInterface().getAccessIdentifier();
		Identifier creatorId = new Identifier(ait.user_id);
		Identifier domainId =  new Identifier(ait.domain_id);

		mapViewStorable = com.syrus.AMFICOM.mapview.MapView.createInstance(
			creatorId,
			domainId,
			"Без названия",
			"",
			0.0D,
			0.0D,
			1.0D,
			1.0D,
			map.getId());

		setLogicalNetLayer(logical);
	}

	/**
	 * constructor
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
	 * геттер
	 */
	public String getName()
	{
		return mapViewStorable.getName();
	}
	
	public void setName(String name)
	{
		mapViewStorable.setName(name);
	}
	
	/**
	 * геттер
	 */
	public Identifier getId()
	{
		return mapViewStorable.getId();
	}
	
//	public void setId(Identifier id)
//	{
//		mapViewStorable.setId(id);
//	}

	/**
	 * геттер
	 */
	public Identifier getDomainId()
	{
		return mapViewStorable.getDomainId();
	}
	
	public void setDomainId(Identifier domainId)
	{
		mapViewStorable.setDomainId(domainId);
	}
	
	/**
	 * геттер
	 */
	public Date getModified()
	{
		return mapViewStorable.getModified();
	}

	public Map getMap()
	{
		return this.map;
	}
	
	public void setMap(Map mc)
	{
		this.map = mc;
		mapViewStorable.setMapId(mc.getId());
	}
	
	public List getSchemes()
	{
		return this.schemes;
	}
	
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapViewPanel";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * флаг того, что контекст открыт в окне карты
	 */
	public boolean isOpened()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"isOpened()");
		
		return this.isOpened;
	}
	
	/**
	 * Остуновить флаг открытия при отображении вида в окне карты
	 */
	public void setOpened(boolean opened)
	{
		this.isOpened = opened;
	}

	/**
	 * Возвращает центральную точку вида в географических координатах
	 */
	public double getScale()
	{
		return mapViewStorable.getScale();
	}
	
	/**
	 * Установить центральную точку вида в географических координатах
	 */
	public void setScale(double scale)
	{
		mapViewStorable.setScale(scale);
	}
	
	/**
	 * Установить значения долготы, широты
	 */
	public void setLongLat( double longitude, double latitude)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setLongLat(" + longitude + ", " + latitude + ")");

		mapViewStorable.setLongitude(longitude);
		mapViewStorable.setLatitude(latitude);
	}
	/**
	 * Установить центральную точку вида карты
	 */
	public void setCenter(DoublePoint center)
	{
		mapViewStorable.setLongitude(center.getX());
		mapViewStorable.setLatitude(center.getY());
	}

	/**
	 * Получить центральную точку вида карты
	 */
	public DoublePoint getCenter()
	{
		return new DoublePoint(
			mapViewStorable.getLongitude(),
			mapViewStorable.getLatitude());
	}

	/**
	 * установить ссылку на класс логической схемы, что соответствует открытию 
	 * контекста в окне карты
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
	 * Получить ссылку на слой логической сети
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	public void addScheme(Scheme sch)
	{
		this.schemes.add(sch);
//		mapViewStorable.addSchemeId(sch.id());
		scanElements(sch);
	}

	public void removeScheme(Scheme sch)
	{
		this.schemes.remove(sch);
		removePaths(sch);
		removeCables(sch);
		removeElements(sch);
	}

	public void removeSchemes()
	{
		while(getSchemes().size() != 0)
		{
			Scheme sch = (Scheme )getSchemes().get(0);
			removeScheme(sch);
		}
	}

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
	
	public void scanElements(Scheme scheme)
	{
		
		for(Iterator it = SchemeUtils.getTopologicalElements(scheme).iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement )it.next();
			scanElement(element);
		}
		scanCables(scheme);
	}

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
	
	public void scanCables(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalCableLinks(scheme).iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			scanCable(scl);
		}
		scanPaths(scheme);
	}

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

	public void scanPaths(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalPaths(scheme).iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			scanPath(path);
		}
	}

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
//					unplaceElement(site, se);
				}
			}
		}
	}

	/**
	 * Разместить элемент на карте.
	 */
	public void placeElement(SchemeElement se, DoublePoint point)
	{
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	public void unplaceElement(SiteNode node, SchemeElement se)
	{
		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	public void placeElement(SchemeCableLink scl)
	{
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}
	
	public void unplaceElement(CablePath cp)
	{
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * Разместить элемент sp типа mtppe на карте. используется при переносе 
	 * (drag/drop)
	 */
	public void placeElement(SchemePath sp)
	{
		PlaceSchemePathCommand cmd = new PlaceSchemePathCommand(sp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	public void unplaceElement(MeasurementPath mp)
	{
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * коррекция начального и конечного узлов топологической прокладки кабеля
	 * по элементам карты, в которых размещены начальный и конечный элемент
	 * схемного кабеля
	 * 
	 * @param mcpe
	 * @param scl
	 */
	public void correctStartEndNodes(CablePath mcpe, SchemeCableLink scl)
	{
		SiteNode[] mne = getSideNodes(scl);
		if(mne[0] != null && mne[1] != null)
		{
			mcpe.setStartNode(mne[0]);
			mcpe.setEndNode(mne[1]);
		}
	}
	
	/**
	 * поле сделано статическим, чтобы каждый раз при вызове метода 
	 * getSideNodes не создавался новый массив ссылок
	 */
	private static SiteNode[] linkSideNodes = new SiteNode[2];
	
	/**
	 * Возвращает массив из двух топологических элементов, в которых 
	 * расположены концевые элементы кабеля. Если элемент не найден (не
	 * нанесен на карту), соответствующий элемент массива равен null
	 * 
	 * @param scl
	 * @return 
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
	 * @param se
	 * @return 
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
	 * Найти элемент с идентификатором id на карте.
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
	 * Получить список путей тестирования
	 */
	public List getCablePaths()
	{
		return cablePaths;
	}

	/**
	 * Добавить новый путь тестирования
	 */
	public void addCablePath(CablePath ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addTransmissionPath(" + ob + ")");

		cablePaths.add(ob);
	}

	/**
	 * Удалить путь тестирования
	 */
	public void removeCablePath(CablePath ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeCablePath(" + ob + ")");

		cablePaths.remove(ob);
		ob.setSelected(false);
//		removedElements.add(ob);
	}

	public List getCablePaths(PhysicalLink mple)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getLinks().contains(mple))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public List getCablePaths(AbstractNode mne)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodes();
			if(cp.getSortedNodes().contains(mne))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public List getCablePaths(NodeLink mnle)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodeLinks();
			if(cp.getSortedNodeLinks().contains(mnle))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * Получить список путей тестирования
	 */
	public List getMeasurementPaths()
	{
		return measurementPaths;
	}

	/**
	 * Добавить новый путь тестирования
	 */
	public void addMeasurementPath(MeasurementPath ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addTransmissionPath(" + ob + ")");

		measurementPaths.add(ob);
	}

	/**
	 * Удалить путь тестирования
	 */
	public void removeMeasurementPath(MeasurementPath ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeTransmissionPath(" + ob + ")");

		measurementPaths.remove(ob);
		ob.setSelected(false);
	}

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

	public List getMeasurementPaths(PhysicalLink mple)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				if(cp.getLinks().contains(mple))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	public List getMeasurementPaths(AbstractNode mne)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodes();
				if(cp.getSortedNodes().contains(mne))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	public List getMeasurementPaths(NodeLink mnle)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodeLinks();
				if(cp.getSortedNodeLinks().contains(mnle))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	public MeasurementPath getMeasurementPathByMonitoredElementId(Identifier meId)
	{
		MeasurementPath path = null;
		try
		{
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
		}
		catch (CommunicationException e)
		{
		}
		catch (DatabaseException e)
		{
		}

		return path;
	}

	/**
	 * Удалить все маркеры
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
	 * Удалить путь тестирования
	 */
	public void removeMarker(Marker ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarker(" + ob + ")");

		markers.remove(ob);
		getMap().removeNode(ob);
		ob.setSelected(false);
	}

	/**
	 * Получить все маркеры
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
	 * Добавить новый путь тестирования
	 */
	public void addMarker(Marker ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addMarker(" + ob + ")");

		markers.add(ob);
		getMap().addNode(ob);
	}

	/**
	 * Получить маркер по ID
	 */
	public Marker getMarker(Identifier markerID)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getMarker(" + markerID + ")");
		
		Iterator e = markers.iterator();
		while( e.hasNext())
		{
			Marker marker = (Marker)e.next();
			if ( marker.getId().equals(markerID))
				return marker;
		}
		return null;
	}

	/**
	 * Получить список всех олементов контекста карты
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
	 * Отменить выбор всем элементам
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
	 * remove all temporary objects on mapview when mapview was edited and
	 * closed without saving
	 */
	public void revert()
	{
		removeMarkers();
	}
	public void setDescription(String description)
	{
		mapViewStorable.setDescription(description);
	}


	public String getDescription()
	{
		return mapViewStorable.getDescription();
	}

	public Identifier getCreatorId()
	{
		return mapViewStorable.getCreatorId();
	}


	public Date getCreated()
	{
		return mapViewStorable.getCreated();
	}


	public boolean isChanged()
	{
		return mapViewStorable.isChanged();
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

/*	public Object clone(DataSourceInterface dataSource)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"clone(" + dataSource + ")");
		
		MapView mv = new MapView();
		mv.id = dataSource.GetUId(com.syrus.AMFICOM.Client.Resource.MapView.MapView.typ);
		mv.name = name + "(copy)";

		mv.description = description;
		mv.domainId = domainId;
		mv.mapId = mapId;
		mv.schemeIds = new LinkedList();
		for(Iterator it = schemes.iterator(); it.hasNext();)
			mv.schemeIds.add(((Scheme )it.next()).getId());

		mv.createdBy = dataSource.getSession().getUserId();
		mv.modified = mv.created;
		mv.modifiedBy = mv.createdBy;

		mv.scale = scale;
		mv.longitude = longitude;
		mv.latitude = latitude;

		mv.markers = new LinkedList();

		Pool.put(MapView.typ, mv.getId(), mv);
		
		return mv;
	}
*/

}