/**
 * $Id: MapView.java,v 1.19 2004/11/01 15:40:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.CORBA.Map.MapView_Transferable;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import java.awt.geom.Point2D;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

/**
 * Класс используется для хранения объектов, отображаемых на 
 * топологической карте. объекты включают в себя:
 * 		- объект карты Map, то есть прокладку канализационную
 *      - набор физических схем Scheme, которые проложены по данному
 *        контексту
 * 
 * 
 * 
 * @version $Revision: 1.19 $, $Date: 2004/11/01 15:40:10 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapView extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "mapview";

	protected MapView_Transferable transferable;

	protected String id;
	protected String name = "Без названия";
	protected String description;
	protected String domainId;
	protected long created;
	protected long modified;
	protected String createdBy;
	protected String modifiedBy;

	protected String mapId = "";
	protected List schemeIds = new LinkedList();
	protected double scale = 0.00001;
	protected double longitude = 0.0;
	protected double latitude = 0.0;
	
	protected LogicalNetLayer logicalNetLayer = null;
	
	protected boolean isOpened = false;

	protected Map map;
	protected List schemes = new LinkedList();

	protected List cablePaths = new LinkedList();
	protected List measurementPaths = new LinkedList();
	/** Вектор маркеров */
	protected List markers = new LinkedList();

	/**
	 * 
	 * @param logical comments
	 */
	public MapView(LogicalNetLayer logical)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView(" + logical + ")");
		setLogicalNetLayer(logical);
		created = System.currentTimeMillis();

		transferable = new MapView_Transferable();
	}

	/**
	 * constructor
	 */
	public MapView()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView()");
		setLogicalNetLayer(null);
		created = System.currentTimeMillis();

		transferable = new MapView_Transferable();
	}

	/**
	 * Используется для создания элемента при подгрузке из базы данных
	 */
	public MapView(MapView_Transferable transferable)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView(" + transferable + ")");
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Object clone(DataSourceInterface dataSource)
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

	/**
	 * Восстановление локальных переменных класса при подгрузке из базы данных
	 */
	public void setLocalFromTransferable()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setLocalFromTransferable()");
		int i;
		int count;

		id = transferable.id;
		name = transferable.name;
		domainId = transferable.domainId;
		created = transferable.created;
		modified = transferable.modified;
		modifiedBy = transferable.modifiedBy;
		createdBy = transferable.createdBy;

		description = transferable.description;

		scale = transferable.scale;
		longitude = Double.parseDouble( transferable.longitude);
		latitude = Double.parseDouble( transferable.latitude);

		count = transferable.schemeIds.length;
		schemeIds = new LinkedList();
		for(i = 0; i < count; i++)
			schemeIds.add(transferable.schemeIds[i]);
	}

	/**
	 * Установка полей в transferable по значениям локальных переменных
	 * для сохранения в базе данных
	 */
	public void setTransferableFromLocal()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setTransferableFromLocal()");
		
		transferable.id = id;
		transferable.name = name;
		transferable.domainId = domainId;
		transferable.description = description;
		transferable.modified = System.currentTimeMillis();
		transferable.modifiedBy = modifiedBy;
		transferable.createdBy = createdBy;

		transferable.scale = scale;
		transferable.longitude = String.valueOf(longitude);
		transferable.latitude = String.valueOf(latitude);

		schemeIds = new LinkedList();
		
		for(Iterator it = schemes.iterator(); it.hasNext();)
		{
			Scheme sch = (Scheme )it.next();
			schemeIds.add(sch.getId());
		}
		transferable.schemeIds = (String [])schemeIds.toArray(new String[schemeIds.size()]);
	}

	/**
	 * 
	 */
	public String getTyp()
	{
		return typ;
	}

	/**
	 * геттер
	 */
	public String getName()
	{
		return name;
	}
	
	public void setName(String name )
	{
		this.name = name;
	}
	
	/**
	 * геттер
	 */
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * геттер
	 */
	public String getDomainId()
	{
		return domainId;
	}
	
	public void setDomainId(String domainId)
	{
		this.domainId = domainId;
	}
	
	/**
	 * геттер
	 */
	public long getModified()
	{
		return modified;
	}

	public Map getMap()
	{
		return this.map;
	}
	
	public void setMap(Map mc)
	{
		this.map = mc;
		if(map != null)
			mapId = mc.getId();
	}
	
	public List getSchemes()
	{
		return this.schemes;
	}
	
	/**
	 * Используется для обновления содержимого локальных переменных по 
	 * значениям, полученным из transferable
	 */
	public void updateLocalFromTransferable()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"updateLocalFromTransferable()");

		map = (Map )Pool.get(Map.typ, mapId);
		
		schemes = new LinkedList();

		for(Iterator it = schemeIds.iterator(); it.hasNext();)
			addScheme((Scheme )Pool.get(Scheme.typ, (String )it.next()));
	}

	/**
	 * получить объект для сохранения в базе данных
	 */
	public Object getTransferable()
	{
		return transferable;
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
		return scale;
	}
	
	/**
	 * Установить центральную точку вида в географических координатах
	 */
	public void setScale(double scale)
	{
		this.scale = scale;
	}
	
	/**
	 * Установить значения долготы, широты
	 */
	public void setLongLat( double longit, double latit)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setLongLat(" + longit + ", " + latit + ")");
		
		longitude = longit;
		latitude = latit;
	}
	/**
	 * Установить центральную точку вида карты
	 */
	public void setCenter(Point2D.Double center)
	{
		longitude = center.x;
		latitude = center.y;
	}

	/**
	 * Получить центральную точку вида карты
	 */
	public Point2D.Double getCenter()
	{
		return new Point2D.Double(longitude, latitude);
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
			longitude = logicalNetLayer.getCenter().x;
			latitude = logicalNetLayer.getCenter().y;

			scale = logicalNetLayer.getScale();

			revert();

			if(map != null)
			{
				map.setConverter(logicalNetLayer);
			}
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
//		for(Iterator it = getSchemes().iterator(); it.hasNext();)
//		{
//			Scheme sch = (Scheme )it.next();
//			removeScheme(sch);
//		}
	}

	public void scanElement(SchemeElement schemeElement)
	{
		MapSiteNodeElement node = findElement(schemeElement);
		if(node == null)
		{
			if(schemeElement.getLong() != 0.0D
				|| schemeElement.getLat() != 0.0D)
			{
				placeElement(
					schemeElement, 
					new Point2D.Double(
						schemeElement.getLong(), 
						schemeElement.getLat()));
			}
		}
	}
	
	public void scanElements(Scheme scheme)
	{
		for(Iterator it = scheme.getTopologicalElements().iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement )it.next();
			scanElement(element);
		}
		scanCables(scheme);
	}

	public void scanCable(SchemeCableLink schemeCableLink)
	{
		MapSiteNodeElement[] mne = getSideNodes(schemeCableLink);
		MapCablePathElement cp = findCablePath(schemeCableLink);
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
		for(Iterator it = scheme.getTopologicalCableLinks().iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			scanCable(scl);
		}
		scanPaths(scheme);
	}

	public void scanPath(SchemePath schemePath)
	{
		MapSiteNodeElement[] mne = getSideNodes(schemePath);
		MapMeasurementPathElement mp = findMeasurementPath(schemePath);
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
		for(Iterator it = scheme.getTopologicalPaths().iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			scanPath(path);
		}
	}

	public void removePaths(Scheme scheme)
	{
		Collection schemePaths = scheme.getTopologicalPaths();
		for(Iterator it = schemePaths.iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			MapMeasurementPathElement mp = findMeasurementPath(path);
			if(mp != null)
			{
				unplaceElement(mp);
			}
		}
//		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
//		{
//			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
//			if(schemePaths.contains(mp.getSchemePath()))
//			{
//				RemoveMeasurementPathCommandAtomic cmd = 
//					new RemoveMeasurementPathCommandAtomic(mp);
//				cmd.setLogicalNetLayer(logicalNetLayer);
//				cmd.execute();
//				removeMeasurementPath(mp);
//			}
//		}
	}

	public void removeCables(Scheme scheme)
	{
		Collection schemeCables = scheme.getTopologicalCableLinks();
		for(Iterator it = schemeCables.iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			MapCablePathElement cp = findCablePath(scl);
			if(cp != null)
			{
				unplaceElement(cp);
			}
		}

//		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
//		{
//			MapCablePathElement cp = (MapCablePathElement )it.next();
//			if(schemeCables.contains(cp.getSchemeCableLink()))
//			{
//				unplaceElement(cp);
//			}
//		}
	}

	public void removeElements(Scheme scheme)
	{
		Collection schemeElements = scheme.getTopologicalElements();
		for(Iterator it = schemeElements.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement )it.next();
			MapSiteNodeElement site = findElement(se);
			if(site != null)
			{
				unplaceElement(site, se);
			}
		}
	}

	/**
	 * Разместить элемент на карте.
	 */
	public void placeElement(SchemeElement se, Point2D.Double point)
	{
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	public void unplaceElement(MapSiteNodeElement node, SchemeElement se)
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
	
	public void unplaceElement(MapCablePathElement cp)
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

	public void unplaceElement(MapMeasurementPathElement mp)
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
	public void correctStartEndNodes(MapCablePathElement mcpe, SchemeCableLink scl)
	{
		MapSiteNodeElement[] mne = getSideNodes(scl);
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
	private static MapSiteNodeElement[] linkSideNodes = new MapSiteNodeElement[2];
	
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
	public MapSiteNodeElement[] getSideNodes(SchemeCableLink scl)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(sch.getTopologicalCableLinks().contains(scl))
				{
					SchemeElement se = sch.getTopologicalElement(
							sch.getSchemeElementByCablePort(scl.sourcePortId));
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = sch.getTopologicalElement(
							sch.getSchemeElementByCablePort(scl.targetPortId));
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
	
	public MapSiteNodeElement[] getSideNodes(SchemePath path)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(sch.getTopologicalPaths().contains(path))
				{
					SchemeElement se = sch.getTopologicalElement(
							sch.getSchemeElementByDevice(path.startDeviceId));
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = sch.getTopologicalElement(
							sch.getSchemeElementByDevice(path.endDeviceId));
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
	public MapSiteNodeElement findElement(SchemeElement se)
	{

		for(Iterator it = getMap().getMapSiteNodeElements().iterator(); it.hasNext();)
		{
			MapSiteNodeElement node = (MapSiteNodeElement )it.next();
			if(node instanceof MapUnboundNodeElement)
				if(((MapUnboundNodeElement)node).getSchemeElement().equals(se))
					return node;
			if(se != null
				&& se.siteId != null
				&& se.siteId.equals(node.getId()))
						return node;
		}
		return null;
	}

	/**
	 * Найти элемент с идентификатором id на карте.
	 */
	public MapCablePathElement findCablePath(SchemeCableLink scl)
	{

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			if(cp.getSchemeCableLink().equals(scl))
					return cp;
		}
		return null;
	}

	public MapMeasurementPathElement findMeasurementPath(SchemePath path)
	{
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
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
	public void addCablePath(MapCablePathElement ob)
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
	public void removeCablePath(MapCablePathElement ob)
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

	public List getCablePaths(MapPhysicalLinkElement mple)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			if(cp.getLinks().contains(mple))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public List getCablePaths(MapNodeElement mne)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			cp.sortNodes();
			if(cp.getSortedNodes().contains(mne))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public List getCablePaths(MapNodeLinkElement mnle)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
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
	public void addMeasurementPath(MapMeasurementPathElement ob)
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
	public void removeMeasurementPath(MapMeasurementPathElement ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeTransmissionPath(" + ob + ")");

		measurementPaths.remove(ob);
		ob.setSelected(false);
//		removedElements.add(ob);
	}

	public List getMeasurementPaths(MapCablePathElement cpath)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + cpath + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			if(mp.getSortedCablePaths().contains(cpath))
				returnVector.add(mp);
		}
		return returnVector;
	}

	public List getMeasurementPaths(MapPhysicalLinkElement mple)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				MapCablePathElement cp = (MapCablePathElement)it2.next();
				if(cp.getLinks().contains(mple))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	public List getMeasurementPaths(MapNodeElement mne)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				MapCablePathElement cp = (MapCablePathElement)it2.next();
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

	public List getMeasurementPaths(MapNodeLinkElement mnle)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				MapCablePathElement cp = (MapCablePathElement)it2.next();
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

	public MapMeasurementPathElement getMeasurementPathByMonitoredElementId(Identifier meId)
	{
		MapMeasurementPathElement path = null;
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
						MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
						if(mp.getSchemePath().path.equals(tp))
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
	public void removeMarker(MapMarker ob)
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
	public void addMarker(MapMarker ob)
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
	public MapMarker getMarker(String markerID)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getMarker(" + markerID + ")");
		
		Iterator e = markers.iterator();
		while( e.hasNext())
		{
			MapMarker marker = (MapMarker)e.next();
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
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}

		e = getMeasurementPaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}

		e = markers.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
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
			MapElement mapElement = (MapElement )e.next();
			mapElement.setSelected(false);
		}
	}

	/**
	 * remove all temporary objects on mapview when mapview was edited and
	 * closed without saving
	 */
	public void revert()
	{
		removeMarkers();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"writeObject(out)");
		
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(domainId);
		out.writeLong(created);
		out.writeObject(createdBy);
		out.writeLong(modified);
		out.writeObject(modifiedBy);
		out.writeObject(getMap().getId());

		schemeIds = new LinkedList();
		
		for(Iterator it = schemes.iterator(); it.hasNext();)
		{
			Scheme sch = (Scheme )it.next();
			schemeIds.add(sch.getId());
		}

		out.writeObject(schemeIds);
		out.writeDouble(scale);
		out.writeDouble(longitude);
		out.writeDouble(latitude);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"readObject(in)");
		
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		domainId = (String )in.readObject();
		created = in.readLong();
		createdBy = (String )in.readObject();
		modified = in.readLong();
		modifiedBy = (String )in.readObject();
		mapId = (String )in.readObject();
		schemeIds = (List )in.readObject();
		scale = in.readDouble();
		longitude = in.readDouble();
		latitude = in.readDouble();

		transferable = new MapView_Transferable();

		updateLocalFromTransferable();
	}

	public void setDescription(String description)
	{
		this.description = description;
	}


	public String getDescription()
	{
		return description;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}


	public long getCreated()
	{
		return created;
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