/**
 * $Id: MapView.java,v 1.2 2004/09/15 08:12:50 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс используется для хранения объектов, отображаемых на 
 * топологической карте. объекты включают в себя:
 * 		- объект карты Map, то есть прокладку канализационную
 *      - набор физических схем Scheme, которые проложены по данному
 *        контексту
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/15 08:12:50 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapView extends StubResource
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "mapview";

	protected Map map;
	protected ArrayList schemes = new ArrayList();

	protected String id;
	protected String name = "Без названия";
	protected String description;
	protected String domainId;
	protected long created;
	protected long modified;
	protected String createdBy;
	protected String modifiedBy;

	protected String mapId = "";
	protected ArrayList schemeIds = new ArrayList();
	protected double scale = 0.00001;
	protected double longitude = 0.0;
	protected double latitude = 0.0;
	
	protected LogicalNetLayer logicalNetLayer = null;
	
	protected boolean isOpened = false;

	protected ArrayList cablePaths = new ArrayList();
	protected ArrayList measurementPaths = new ArrayList();
	/** Вектор маркеров */
	protected ArrayList markers = new ArrayList();

	/**
	 * 
	 * @param logical comments
	 */
	public MapView(LogicalNetLayer logical)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "Map(" + logical + ")");
		setLogicalNetLayer(logical);
		created = System.currentTimeMillis();

//		transferable = new MapView_Transferable();
	}

	/**
	 * constructor
	 */
	public MapView()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "Map()");
		setLogicalNetLayer(null);
		created = System.currentTimeMillis();

//		transferable = new MapView_Transferable();
	}

	/**
	 * Используется для создания элемента при подгрузке из базы данных
	 */
/*
	public MapView(MapView_Transferable transferable)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "constructor call", getClass().getName(), "Map(" + transferable + ")");
		this.transferable = transferable;
		setLocalFromTransferable();
	}
*/

	public Object clone(DataSourceInterface dataSource)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "clone(" + dataSource + ")");
		
		MapView mv = new MapView();
		mv.id = dataSource.GetUId(com.syrus.AMFICOM.Client.Resource.MapView.MapView.typ);
		mv.name = name + "(copy)";

		mv.description = description;
		mv.domainId = domainId;
		mv.mapId = mapId;
		mv.schemeIds = (ArrayList )schemeIds.clone();

		mv.createdBy = dataSource.getSession().getUserId();
		mv.modified = mv.created;
		mv.modifiedBy = mv.createdBy;

		mv.scale = scale;
		mv.longitude = longitude;
		mv.latitude = latitude;

		mv.schemeIds = new ArrayList();
		for(Iterator it = schemeIds.iterator(); it.hasNext();)
			mv.schemeIds.add(it.next());

		mv.markers = new ArrayList();

		Pool.put(com.syrus.AMFICOM.Client.Resource.MapView.MapView.typ, mv.getId(), mv);
		
		return mv;
	}

	/**
	 * Восстановление локальных переменных класса при подгрузке из базы данных
	 */
/*
	public void setLocalFromTransferable()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setLocalFromTransferable()");
		int i;
		int count;

		id = transferable.id;
		name = transferable.name;
		user_id = transferable.user_id;
		domain_id = transferable.domain_id;
		created = transferable.created;
		modified = transferable.modified;
		modified_by = transferable.modified_by;
		created_by = transferable.created_by;

		description = transferable.description;
		showPhysicalNodeElement = transferable.showPhysicalNodeElement;

		scale = transferable.scale;
		longitude = Double.parseDouble( transferable.longitude);
		latitude = Double.parseDouble( transferable.latitude);

		count = transferable.scheme_ids.length;
		scheme_ids = new ArrayList(count);
		for(i = 0; i < count; i++)
			scheme_ids.add(transferable.scheme_ids[i]);
	}
*/
	/**
	 * Установка полей в transferable по значениям локальных переменных
	 * для сохранения в базе данных
	 */
/*
	public void setTransferableFromLocal()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setTransferableFromLocal()");
		
		int i;
		int count;
		ObjectResource os;

		transferable.id = id;
		transferable.name = name;
		transferable.user_id = user_id;
		transferable.domain_id = domain_id;
		transferable.description = description;
		transferable.showPhysicalNodeElement = showPhysicalNodeElement;
		transferable.modified = System.currentTimeMillis();
		transferable.modified_by = user_id;
		transferable.created_by = user_id;

		transferable.scale = scale;
		transferable.longitude = String.valueOf(longitude);
		transferable.latitude = String.valueOf(latitude);

		scheme_ids = new ArrayList();
		
		for(Iterator it = scheme.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			scheme.add(os.getId());
		}
		transferable.scheme = (String [])scheme.toArray(new String[schemes.size()]);
	}
*/
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
	}
	
	public List getSchemes()
	{
		return this.schemes;
	}
	
	public void addScheme(Scheme sch)
	{
		this.schemes.add(sch);
	}
	
	public void removeScheme(Scheme sch)
	{
		this.schemes.remove(sch);
	}
	
	/**
	 * Используется для обновления содержимого локальных переменных по 
	 * значениям, полученным из transferable
	 */
	public void updateLocalFromTransferable()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "updateLocalFromTransferable()");
		
		schemes = new ArrayList();

		for(Iterator it = schemeIds.iterator(); it.hasNext();)
			schemes.add(Pool.get(Scheme.typ, (String )it.next()));

		map = (Map)Pool.get(com.syrus.AMFICOM.Client.Resource.Map.Map.typ, mapId);
	}

	/**
	 * получить объект для сохранения в базе данных
	 */
/*
	public Object getTransferable()
	{
		return transferable;
	}
*/
	/**
	 * получить модель объекта
	 */
	public ObjectResourceModel getModel()
	{
		return null;//new MapViewModel(this);
	}

	public static ObjectResourcePropertiesPane getPropertyPane1()
	{
		return null;
	}

	/**
	 * получить панель свойств
	 */
	public static PropertiesPanel getPropertyPane()
	{
		return null;//new MapViewPane();
	}

	/**
	 * получить модель отображения в таблице
	 */
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapViewDisplayModel();
	}

	/**
	 * флаг того, что контекст открыт в окне карты
	 */
	public boolean isOpened()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "isOpened()");
		
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setLongLat(" + longit + ", " + latit + ")");
		
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "setLogicalNetLayer(" + logical + ")");
		
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

	/**
	 * Возвращает суммарную физическую (строительную) длину линий и кабелей
	 * внутри элемента me, которые содержатся в пути sp и определяются
	 * последовательностью pathelements, начиная с элемента pe
	 */
	public PathElement countPhysicalLength(MapElement me, SchemePath sp, PathElement pe, Iterator pathelements)
	{
		return pe;
	}

	/**
	 * Разместить элемент sp типа mtppe на карте. используется при переносе 
	 * (drag/drop)
	 */
/*
	public void placeElement(SchemePath sp, MapTransmissionPathProtoElement mtppe)
	{
		if(sp == null)
			return;

		// fill in plink_ids. assume plink_ids are sorted!
		Vector plink_ids = new Vector();

		PathElement pes[] = new PathElement[sp.links.size()];
		for(int i = 0; i < sp.links.size(); i++)
		{
			PathElement pe = (PathElement )sp.links.get(i);
			pes[pe.n] = pe;
		}
		for(int i = 0; i < pes.length; i++)
		{
			PathElement pe = pes[i];
			if(pe.is_cable)
			{
				MapPhysicalLinkElement mple = findPhysicalLink(pe.link_id);
				if(mple == null)
				{
					System.out.println("Place cables first!!!");
					return;
				}
				plink_ids.add(mple.getId());
			}
		}

		MapTransmissionPathElement mtpe = findPath(sp.getId());
		if(mtpe != null)
		{
			mtpe.physicalLink_ids = plink_ids;

			mtpe.select();
			return;
		}

		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		MapTransmissionPathElement thePath;
		if ( mapMainFrame.aContext.getApplicationModel()
				.isEnabled("mapActionCreatePath"))
		{
			Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMap().scheme_id);

//			MapSiteNodeElement smne = findElement(scheme.getTopLevelElement(sp.start_device_id).getId());
			MapSiteNodeElement smne = findElement(scheme.getTopologicalElement(sp.start_device_id).getId());
//			MapSiteNodeElement emne = findElement(scheme.getTopLevelElement(sp.end_device_id).getId());
			MapSiteNodeElement emne = findElement(scheme.getTopologicalElement(sp.end_device_id).getId());

			if(smne == null || emne == null)
				return;

			thePath = new MapTransmissionPathElement(
					dataSource.GetUId( MapPhysicalLinkElement.typ ),
					smne, 
					emne,
					getMap());
			Pool.put(MapTransmissionPathElement.typ, thePath.getId(), thePath);

			thePath.type_id = mtppe.getId();
			thePath.attributes = ResourceUtil.copyAttributes(dataSource, mtppe.attributes);
			thePath.physicalLink_ids = plink_ids;
			thePath.PATH_ID = sp.getId();
			thePath.name = sp.getName();

			getMap().getTransmissionPath().add(thePath);
		}
	}
*/	
	/**
	 * Разместить элемент scl типа mplpe на карте. используется при переносе 
	 * (drag/drop)
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
	
	private static MapSiteNodeElement[] linkSideNodes = new MapSiteNodeElement[2];
	
	public MapSiteNodeElement[] getSideNodes(SchemeCableLink scl)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(sch.getAllCableLinks().contains(scl))
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
	
/*
	public void placeElement(SchemeCableLink scl, MapLinkProtoElement mplpe)
	{
		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		MapPhysicalLinkElement theLink;
		if ( mapMainFrame.aContext.getApplicationModel()
				.isEnabled("mapActionCreateLink"))
		{
			if(scl == null)
				return;

			Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMap().schemeId);

			MapSiteNodeElement smne;
			MapSiteNodeElement emne;
			try
			{
//				SchemeElement se = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.sourceportId));
				SchemeElement se = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.sourcePortId));
				smne = findElement(se.getId());
//				SchemeElement se2 = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.targetPortId));
				SchemeElement se2 = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.targetPortId));
				emne = findElement(se2.getId());
			}
			catch(Exception ex)
			{
				System.out.println("Place nodes first!!!");
				return;
			}
			

			if(smne == null || emne == null)
				return;

			MapNodeLinkElement nodeLink = new MapNodeLinkElement( 
				dataSource.GetUId( MapNodeLinkElement.typ),
				smne, 
				emne, 
				getMap());
			Pool.put(MapNodeLinkElement.typ, nodeLink.getId(), nodeLink);
			getMap().getNodeLinks().add(nodeLink);

			theLink = new MapPhysicalLinkElement(
					dataSource.GetUId( MapPhysicalLinkElement.typ ),
					smne, 
					emne,
					getMap());

			theLink.typeId = mplpe.getId();
			theLink.attributes = ResourceUtil.copyAttributes(dataSource, mplpe.attributes);
			theLink.nodeLink_ids.add( nodeLink.getId());
			theLink.link_type_id = scl.cable_link_type_id;
			theLink.LINK_ID = scl.getId();
			theLink.name = scl.getName();

			Pool.put( MapPhysicalLinkElement.typ, theLink.getId(), theLink );
			getMap().getPhysicalLinks().add(theLink);
		}
	}
*/	
	/**
	 * Разместить элемент типа mtppe на карте. используется при переносе 
	 * (drag/drop)
	 */
/*
	public void placeElement(MapTransmissionPathProtoElement mtppe)
	{
		if(sp == null)
			return;

		// fill in plink_ids. assume plink_ids are sorted!
		Vector plink_ids = new Vector();

		PathElement pes[] = new PathElement[sp.links.size()];
		for(int i = 0; i < sp.links.size(); i++)
		{
			PathElement pe = (PathElement )sp.links.get(i);
			pes[pe.n] = pe;
		}
		for(int i = 0; i < pes.length; i++)
		{
			PathElement pe = pes[i];
			if(pe.is_cable)
			{
				MapPhysicalLinkElement mple = findPhysicalLink(pe.link_id);
				if(mple == null)
				{
					System.out.println("Place cables first!!!");
					return;
				}
				plink_ids.add(mple.getId());
			}
		}

		MapTransmissionPathElement mtpe = findPath(sp.getId());
		if(mtpe != null)
		{
			mtpe.physicalLink_ids = plink_ids;

			mtpe.select();
			return;
		}

		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		MapTransmissionPathElement thePath;
		if ( mapMainFrame.aContext.getApplicationModel()
				.isEnabled("mapActionCreatePath"))
		{
			Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMap().scheme_id);

			MapSiteNodeElement smne = findElement(scheme.getTopologicalElement(sp.start_device_id).getId());
			MapSiteNodeElement emne = findElement(scheme.getTopologicalElement(sp.end_device_id).getId());

			if(smne == null || emne == null)
				return;

			thePath = new MapTransmissionPathElement(
					dataSource.GetUId( MapPhysicalLinkElement.typ ),
					smne, 
					emne,
					getMap());
			Pool.put(MapTransmissionPathElement.typ, thePath.getId(), thePath);

			thePath.type_id = mtppe.getId();
			thePath.attributes = ResourceUtil.copyAttributes(dataSource, mtppe.attributes);
			thePath.physicalLink_ids = plink_ids;
			thePath.PATH_ID = sp.getId();
			thePath.name = sp.getName();

			getMap().getTransmissionPath().add(thePath);
		}
	}
*/	

	/**
	 * Разместить элемент типа mplpe на карте. используется при переносе 
	 * (drag/drop)
	 */
	public void placeElement(MapLinkProtoElement mplpe)
	{
/*
		MapPhysicalLinkElement mple = findPhysicalLink(scl.getId());
		if(mple != null)
		{
			MapSiteNodeElement smne;
			MapSiteNodeElement emne;
			try
			{
				Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMap().scheme_id);

//				SchemeElement se = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				SchemeElement se = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				smne = findElement(se.getId());
//				SchemeElement se2 = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				SchemeElement se2 = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				emne = findElement(se2.getId());
			}
			catch(Exception ex)
			{
				System.out.println("Place nodes first!!!");
				return;
			}
			if(smne != null && emne != null)
			{
				mple.startNode = smne; 
				mple.endNode = emne;
			}

			mple.select();
			return;
		}

		DataSourceInterface dataSource = mapMainFrame.getContext().getDataSourceInterface();

		MapPhysicalLinkElement theLink;
		if ( mapMainFrame.aContext.getApplicationModel()
				.isEnabled("mapActionCreateLink"))
		{
			if(scl == null)
				return;

			Scheme scheme = (Scheme )Pool.get(Scheme.typ, getMap().scheme_id);

			MapSiteNodeElement smne;
			MapSiteNodeElement emne;
			try
			{
//				SchemeElement se = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				SchemeElement se = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.source_port_id));
				smne = findElement(se.getId());
//				SchemeElement se2 = scheme.getTopLevelElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				SchemeElement se2 = scheme.getTopologicalElement(scheme.getSchemeElementByCablePort(scl.target_port_id));
				emne = findElement(se2.getId());
			}
			catch(Exception ex)
			{
				System.out.println("Place nodes first!!!");
				return;
			}
			

			if(smne == null || emne == null)
				return;

			MapNodeLinkElement nodeLink = new MapNodeLinkElement( 
				dataSource.GetUId( MapNodeLinkElement.typ),
				smne, 
				emne, 
				getMap());
			Pool.put(MapNodeLinkElement.typ, nodeLink.getId(), nodeLink);
			getMap().getNodeLinks().add(nodeLink);

			theLink = new MapPhysicalLinkElement(
					dataSource.GetUId( MapPhysicalLinkElement.typ ),
					smne, 
					emne,
					getMap());

			theLink.type_id = mplpe.getId();
			theLink.attributes = ResourceUtil.copyAttributes(dataSource, mplpe.attributes);
			theLink.nodeLink_ids.add( nodeLink.getId());
			theLink.link_type_id = scl.cable_link_type_id;
			theLink.LINK_ID = scl.getId();
			theLink.name = scl.getName();

			Pool.put( MapPhysicalLinkElement.typ, theLink.getId(), theLink );
			getMap().getPhysicalLinks().add(theLink);
		}
*/	
	}

	/**
	 * Найти элемент на карте.
	 */
	public MapSiteNodeElement findElement(SchemeElement se)
	{

		for(Iterator it = getMap().getMapSiteNodeElements().iterator(); it.hasNext();)
		{
			MapSiteNodeElement node = (MapSiteNodeElement )it.next();
			if(node instanceof MapUnboundNodeElement)
				if(((MapUnboundNodeElement)node).getSchemeElement().equals(se))
					return node;
			if(se != null)
				if(se.siteId != null)
					if(se.siteId.equals(node.getId()))
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
/*
	public MapTransmissionPathElement findPath(String id)
	{
		for(Iterator it = getMap().getTransmissionPath().iterator(); it.hasNext();)
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )it.next();
			if(path.PATH_ID.equals(id))
				return path;
		}
		return null;
	}
*/
	/**
	 * Получить список путей тестирования
	 */
	public ArrayList getCablePaths()
	{
		return cablePaths;
	}

	/**
	 * Добавить новый путь тестирования
	 */
	public void addCablePath(MapCablePathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addTransmissionPath(" + ob + ")");

		cablePaths.add(ob);
	}

	/**
	 * Удалить путь тестирования
	 */
	public void removeCablePath(MapCablePathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeCablePath(" + ob + ")");

		cablePaths.remove(ob);
//		removedElements.add(ob);
	}

	public LinkedList getCablePaths(MapPhysicalLinkElement mple)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getCablePaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();;
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			if(cp.getLinks().contains(mple))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public LinkedList getCablePaths(MapNodeElement mne)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getCablePaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();;
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			cp.sortNodes();
			if(cp.getSortedNodes().contains(mne))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public LinkedList getCablePaths(MapNodeLinkElement mnle)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getCablePaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();;
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
	public ArrayList getPaths()
	{
		return measurementPaths;
	}

	/**
	 * Добавить новый путь тестирования
	 */
	public void addPath(MapPathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "addTransmissionPath(" + ob + ")");

		measurementPaths.add(ob);
	}

	/**
	 * Удалить путь тестирования
	 */
	public void removePath(MapPathElement ob)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeTransmissionPath(" + ob + ")");

		measurementPaths.remove(ob);
//		removedElements.add(ob);
	}

	public LinkedList getPaths(MapPhysicalLinkElement mple)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();;
//		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
//		{
//			MapPathElement cp = (MapPathElement )it.next();
//			if(cp.getLinks().contains(mple))
//				returnVector.add(cp);
//		}
		return returnVector;
	}

	public LinkedList getPaths(MapNodeElement mne)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();;
//		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
//		{
//			MapPathElement cp = (MapPathElement )it.next();
//			cp.sortNodes();
//			if(cp.getSortedNodes().contains(mne))
//				returnVector.add(cp);
//		}
		return returnVector;
	}

	public LinkedList getPaths(MapNodeLinkElement mnle)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getPaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();;
//		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
//		{
//			MapPathElement cp = (MapPathElement )it.next();
//			cp.sortNodeLinks();
//			if(cp.getSortedNodeLinks().contains(mnle))
//				returnVector.add(cp);
//		}
		return returnVector;
	}

	/**
	 * Удалить все маркеры
	 */
	public void removeMarkers()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeMarkers()");
		
		markers.clear();
	}

	/**
	 * Получить все маркеры
	 */
	public List getMarkers()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "removeMarkers()");
		
		return markers;
	}

	/**
	 * Получить маркер по ID
	 */
	public MapMarker getMarker(String markerID)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getMarker(" + markerID + ")");
		
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getAllElements()");
		
		List returnVector = getMap().getAllElements1();
		
		Iterator e;

		e = getCablePaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			returnVector.add( mapElement);
		}

		e = getPaths().iterator();
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "deselectAll()");
		
		Iterator e = getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement )e.next();
			mapElement.setSelected(false);
		}
	}

	/**
	 * 
	 */
	public void revert()
	{
		removeMarkers();
	}

/* from SiteNode
 * 
	protected double physical_length = 0.0;

	public double getPhysicalLength()
	{
		return physical_length;
	}

	//Возвращяет длинну линий внутри данного узла,
	//пересчитанную на коэффициент топологической привязки
	public PathElement countPhysicalLength(SchemePath sp, PathElement pe, Enumeration pathelements)
	{
		physical_length = 0.0;

		if(this.element_id == null || this.element_id.equals(""))
			return pe;
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, this.element_id);

		Vector vec = new Vector();
		Enumeration e = se.getAllSchemesLinks();
		for(;e.hasMoreElements();)
			vec.add(e.nextElement());

		for(;;)
		{
			if(pe.is_cable)
			{
				SchemeCableLink schemeCableLink =
						(SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
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
						(SchemeLink )Pool.get(SchemeLink.typ, pe.link_id);
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