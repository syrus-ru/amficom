/*
* $Id: MapView.java,v 1.7 2005/02/01 14:34:23 krupenn Exp $
*
* Copyright ї 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.IdentifierDefaultFactory;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.corba.MapView_Transferable;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс используется для хранения объектов, отображаемых на 
 * топологической схеме. Объекты включают в себя:
 * <br>&#9;- объект топологической схемы {@link Map}, то есть прокладку 
 * канализационную
 * <br>&#9;- набор физических схем {@link Scheme}, которые проложены по данной
 * топологической схеме
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/01 14:34:23 $
 * @module mapview_v1
 * @todo use getCenter, setCenter instead of pair longitude, latitude
 */
public class MapView extends StorableObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256722862181200184L;

	private Identifier domainId;
	private String name;
	private String description;

	private double longitude;
	private double latitude;
	private double scale;
	private double defaultScale;

	private Map map;
	/**
	 *  List&lt;{@link com.syrus.AMFICOM.scheme.corba.Scheme}&gt;
	 */
	private List schemes;

	private StorableObjectDatabase	mapViewDatabase;

	/** Список кабелей. */
	protected transient List cablePaths = new LinkedList();
	
	/** Список измерительных путей. */
	protected transient List measurementPaths = new LinkedList();
	
	/** Список маркеров. */
	protected transient List markers = new LinkedList();

	public MapView(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.mapViewDatabase = MapViewDatabaseContext.getMapViewDatabase();
		try {
			this.mapViewDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MapView(MapView_Transferable mvt) throws CreateObjectException {
		super(mvt.header);
		
		this.domainId = new Identifier(mvt.domain_id);		

		this.name = mvt.name;
		this.description = mvt.description;
		
		this.longitude = mvt.longitude;
		this.latitude = mvt.latitude;
		this.scale = mvt.scale;
		this.defaultScale = mvt.defaultScale;		

		IdentifierDefaultFactory identifierDefaultFactory = new IdentifierDefaultFactory();
		List schemeIds = new ArrayList(mvt.schemeIds.length);
		for (int i = 0; i < mvt.schemeIds.length; i++) 
			schemeIds.add(identifierDefaultFactory.newInstanceFromTransferable(mvt.schemeIds[i]));

		Identifier mapId = new Identifier(mvt.mapId);
		try{
			this.map = (Map) MapStorableObjectPool.getStorableObject(mapId, true);
		}catch(ApplicationException ae){
			throw new CreateObjectException("MapView.<init> | cannot get map " + mapId.toString(), ae);
		}			
		
		try{
			this.schemes = SchemeStorableObjectPool.getStorableObjects(schemeIds, true);
		}catch(ApplicationException ae){
			throw new CreateObjectException("MapView.<init> | cannot get schemes ", ae);
		}			

	}

	protected MapView(final Identifier id, 
				  final Identifier creatorId, 
				  final Identifier domainId,
				  final String name,
				  final String description,
				  final double longitude,
				  final double latitude,
				  final double scale,
				  final double defaultScale,
				  final Map map) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.domainId = domainId;
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.scale = scale;
		this.defaultScale = defaultScale;
		this.map = map;

		this.schemes = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.mapViewDatabase = MapViewDatabaseContext.getMapViewDatabase();
	}

	
	public void insert() throws CreateObjectException {
		this.mapViewDatabase = MapViewDatabaseContext.getMapViewDatabase();
		try {
			if (this.mapViewDatabase != null)
				this.mapViewDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static MapView createInstance(
			final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final double scale,
			final double defaultScale,
			final Map map) 
		throws CreateObjectException {
		if (domainId == null || name == null || description == null || map == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			return new MapView(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAPVIEW_ENTITY_CODE),
					creatorId,
					domainId,
					name,
					description,
					longitude,
					latitude,
					scale,
					defaultScale,
					map);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MapView.createInstance | cannot generate identifier ", e);
		}
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.domainId);
		dependencies.add(this.map);
		dependencies.addAll(this.schemes);		
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] schemeIdsTransferable = new Identifier_Transferable[this.schemes.size()];
		for (Iterator iterator = this.schemes.iterator(); iterator.hasNext();)
			schemeIdsTransferable[i++] = (((Scheme) iterator.next()).id()).transferable();		

		return new MapView_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable)this.domainId.getTransferable(),
				this.name,
				this.description,
				this.longitude,
				this.latitude,
				this.scale,
				this.defaultScale,
				(Identifier_Transferable)this.map.getId().getTransferable(),
				schemeIdsTransferable);
	}

	/**
	 * Получить список схем.
	 * @return список схем
	 */
	public List getSchemes() {
		return  Collections.unmodifiableList(this.schemes);
	}
	
	public void addScheme(Scheme scheme)
	{
		this.schemes.add(scheme);
		super.currentVersion = super.getNextVersion();
	}
	
	public void removeScheme(Scheme scheme)
	{
		this.schemes.remove(scheme);
		super.currentVersion = super.getNextVersion();
	}
	
	protected void setSchemes0(List schemes) {
		this.schemes.clear();
		if (schemes != null)
			this.schemes.addAll(schemes);
	}
	
	public void setSchemeIds(List schemeIds) {
		this.setSchemes0(schemeIds);
		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * Получить описание вида.
	 * @return описание вида
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Установить описание вида.
	 * @param description описание вида
	 */
	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * Получить идентификатор домена.
	 * @return идентификатор домена
	 */
	public Identifier getDomainId() {
		return this.domainId;
	}
	
	/**
	 * Установить идентификатор домена.
	 * @param domainId новый идентификатор домена
	 */
	public void setDomainId(Identifier domainId) {
		this.domainId = domainId;
		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * Получить название вида.
	 * @return название
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Установить новое название вида.
	 * @param name новое название
	 */
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}	
	
	protected synchronized void setAttributes(final Date created,
											  final Date modified,
											  final Identifier creatorId,
											  final Identifier modifierId,											  
											  final Identifier domainId,
											  final String name,
											  final String description,
											  final double longitude,
											  final double latitude,
											  final double scale,
											  final double defaultScale,
											  final Map map) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.domainId = domainId;
			this.name = name;
			this.description = description;
			this.longitude = longitude;
			this.latitude = latitude;
			this.scale = scale;
			this.defaultScale = defaultScale;
			this.map = map;
			
	}

	public double getDefaultScale() {
		return this.defaultScale;
	}
	
	public void setDefaultScale(double defaultScale) {
		this.defaultScale = defaultScale;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * Получить топологическую схему.
	 * @return топологическая схема
	 */
	public Map getMap() {
		return this.map;
	}
	
	/**
	 * Установить топологическую схему.
	 * @param map топологическая схема
	 */
	public void setMap(Map map) {
		this.map = map;
		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * Возвращает масштаб вида.
	 * @return масштаб
	 */
	public double getScale() {
		return this.scale;
	}
	
	/**
	 * Установить масштаб вида.
	 * @param scale масштаб
	 */
	public void setScale(double scale) {
		this.scale = scale;
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * Установить центральную точку вида в географических координатах.
	 * @param center центр вида
	 */
	public void setCenter(DoublePoint center)
	{
		setLongitude(center.getX());
		setLatitude(center.getY());
	}

	/**
	 * Получить центральную точку вида в географических координатах.
	 * @return центр вида
	 */
	public DoublePoint getCenter()
	{
		return new DoublePoint(
			getLongitude(),
			getLatitude());
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
		SiteNode startNode = getStartNode(scl);
		SiteNode endNode = getEndNode(scl);
		if(startNode != null && endNode != null)
		{
			cable.setStartNode(startNode);
			cable.setEndNode(endNode);
		}
	}
	
	/**
	 * Возвращает топологический элемент, в котором расположен начальный
	 * элемент кабеля.
	 * 
	 * @param scl кабель
	 * @return начальный элемент кабеля, или null, если элемент не найден
	 * (не нанесен на карту)
	 */
	public SiteNode getStartNode(SchemeCableLink scl)
	{
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
							SchemeUtils.getSchemeElementByDevice(
								sch, 
								scl.sourceAbstractSchemePort().schemeDevice()));
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Возвращает топологический элемент, в котором расположен конечный
	 * элемент кабеля.
	 * 
	 * @param scl кабель
	 * @return конечный элемент кабеля, или null, если элемент не найден
	 * (не нанесен на карту)
	 */
	public SiteNode getEndNode(SchemeCableLink scl)
	{
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
							SchemeUtils.getSchemeElementByDevice(
								sch, 
								scl.targetAbstractSchemePort().schemeDevice()));
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Возвращает топологический элемент, в котором расположен начальный
	 * элемент пути.
	 * 
	 * @param path путь
	 * @return начальный элемент пути, или null, если элемент не найден
	 *   (не нанесен на карту)
	 */
	public SiteNode getStartNode(SchemePath path)
	{
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
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Возвращает топологический элемент, в котором расположен конечный
	 * элемент пути.
	 * 
	 * @param path путь
	 * @return конечный элемент пути, или null, если элемент не найден
	 *   (не нанесен на карту)
	 */
	public SiteNode getEndNode(SchemePath path)
	{
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalPaths(sch).contains(path))
				{
					SchemeElement se = SchemeUtils.getTopologicalElement(
							sch,
							path.endDevice());
					return findElement(se);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
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
		cablePaths.add(cable);
	}

	/**
	 * Удалить топологический кабель.
	 * @param cable топологический кабель
	 */
	public void removeCablePath(CablePath cable)
	{
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
		measurementPaths.add(path);
	}

	/**
	 * Удалить путь тестирования.
	 * @param path путь тестирования
	 */
	public void removeMeasurementPath(MeasurementPath path)
	{
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
	 * Удалить все маркеры.
	 */
	public void removeMarkers()
	{
		getMap().getNodes().removeAll(markers);
		markers.clear();
	}

	/**
	 * Удалить путь тестирования.
	 * @param marker маркер
	 */
	public void removeMarker(Marker marker)
	{
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
		return markers;
	}

	/**
	 * Добавить новый маркер.
	 * @param marker маркер
	 */
	public void addMarker(Marker marker)
	{
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


}

