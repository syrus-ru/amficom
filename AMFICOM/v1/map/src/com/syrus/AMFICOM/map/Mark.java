/**
 * $Id: Mark.java,v 1.17 2005/02/02 14:48:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Метка на линии на топологической схеме. Метка частично характеризуется 
 * абстрактным узлом, одняко привязан к некоторой дистанции на линии и, 
 * соответственно, не может быть концевым для линий и фрагментов линий, 
 * в связи с чем методы класса {@link AbstractNode}, работающие с линиями и 
 * фрагментами линий, переопределены и бросают 
 * <code>{@link UnsupportedOperationException}</code>.
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/02/02 14:48:45 $
 * @module map_v1
 */
public class Mark extends AbstractNode implements Characterized {

	public static final String IMAGE_NAME = "mark";
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258126938496186164L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_STREET = "street";
	public static final String COLUMN_BUILDING = "building";

	/** 
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	private PhysicalLink			physicalLink;

	private double					distance;

	private String					city;
	private String					street;
	private String					building;

	private StorableObjectDatabase	markDatabase;


	protected transient double sizeInDoubleLt;

	protected transient NodeLink nodeLink;

	protected transient AbstractNode startNode;


	public Mark(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.markDatabase = MapDatabaseContext.getMarkDatabase();
		try {
			this.markDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Mark(Mark_Transferable mt) throws CreateObjectException {
		super(mt.header);
		super.name = mt.name;
		super.description = mt.description;

		super.location = new DoublePoint(mt.longitude, mt.latitude);

		this.distance = mt.distance;

		this.city = mt.city;
		this.street = mt.street;
		this.building = mt.building;

		try {
			this.physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(
				new Identifier(mt.physicalLinkId), true);

			super.characteristics = new ArrayList(mt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(mt.characteristicIds.length);
			for (int i = 0; i < mt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(mt.characteristicIds[i]));
			super.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Mark(final Identifier id, 
				   final Identifier creatorId, 
				   final String name, 
				   final String description,
				   final double longitude, 
				   final double latitude, 
				   final PhysicalLink physicalLink,
				   final double distance,
				   final String city, 
				   final String street, 
				   final String building) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.name = name;
		super.description = description;
		super.location = new DoublePoint(longitude, latitude);		
		this.physicalLink = physicalLink;
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;

		super.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.markDatabase = MapDatabaseContext.getMarkDatabase();
	}

	
	public void insert() throws CreateObjectException {
		this.markDatabase = MapDatabaseContext.getMarkDatabase();
		try {
			if (this.markDatabase != null)
				this.markDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static Mark createInstance(
			final Identifier creatorId,
			final PhysicalLink link,
			final double len)
		throws CreateObjectException 
	{
		return Mark.createInstance(
			creatorId,
			"",
			"",
			0.0D,
			0.0D,
			link,
			len,
			"",
			"",
			"");
	}

	public static Mark createInstance(	final Identifier creatorId,
										final String name,
										final String description,
										final double longitude,
										final double latitude,
										final PhysicalLink physicalLink,
										final double distance,
										final String city,
										final String street,
										final String building) throws CreateObjectException {
		if (creatorId == null || name == null || description == null || physicalLink == null 
				|| city == null || street == null || building == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Mark(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MARK_ENTITY_CODE), 
					creatorId, 
					name,
					description,
					longitude, 
					latitude,
					physicalLink,
					distance,
					city,
					street,
					building);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Mark.createInstance | cannot generate identifier ", e);
		}
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.physicalLink);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		return new Mark_Transferable(super.getHeaderTransferable(),
						this.name,
						this.description,
						this.location.getX(),
						this.location.getY(),
						(Identifier_Transferable)this.physicalLink.getId().getTransferable(),
						this.distance,
						this.city,
						this.street,
						this.building,
						charIds);
	}
	
	public String getBuilding() {
		return this.building;
	}
	
	protected void setBuilding0(String building) {
		this.building = building;
	}
	
	public void setBuilding(String building) {
		this.setBuilding0(building);
		super.currentVersion = super.getNextVersion();
	}
	
	public String getCity() {
		return this.city;
	}
	
	protected void setCity0(String city) {
		this.city = city;
	}
	
	public void setCity(String city) {
		this.setCity0(city);
		super.currentVersion = super.getNextVersion();
	}
	
	public double getDistance() {
		return this.distance;
	}
	
	protected void setDistance0(double distance) {
		this.distance = distance;
	}
	
	public void setDistance(double distance) {
		this.setDistance0(distance);
		super.currentVersion = super.getNextVersion();
	}
	
	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}
	
	protected void setPhysicalLink0(PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
	}
	
	public void setPhysicalLink(PhysicalLink physicalLink) {
		this.setPhysicalLink0(physicalLink);
		super.currentVersion = super.getNextVersion();
	}
	
	public String getStreet() {
		return this.street;
	}	
	
	protected void setStreet0(String street) {
		this.street = street;
	}
	
	public void setStreet(String street) {
		this.setStreet0(street);
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,											  
			String name,
			String description,
			double longitude,
			double latitude,
			PhysicalLink physicalLink,
			double distance,
			String city,
			String street,
			String building) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId);
		super.name = name;
		super.description = description;
		super.location.setLocation(longitude, latitude);
		this.physicalLink = physicalLink;
		this.distance = distance;
		this.city = city;
		this.street = street;
		this.building = building;					
	}

	/**
	 * Установить фрагмент, на который помещается метка.
	 * @param nodeLink фрагмент линии
	 */
	public void setNodeLink(NodeLink nodeLink)
	{
		this.nodeLink = nodeLink;
	}

	/**
	 * Установить фрагмент, на котором находится метка.
	 * @return фрагмент линии
	 */
	public NodeLink getNodeLink()
	{
		return this.nodeLink;
	}

	/**
	 * Установить начальный узел фрагмента, на котором находится метка.
	 * @param startNode узел
	 */
	public void setStartNode(AbstractNode startNode)
	{
		this.startNode = startNode;
	}

	/**
	 * Получить начальный узел фрагмента, на котором находится метка.
	 * @return узел
	 */
	public AbstractNode getStartNode()
	{
		return this.startNode;
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation()
	{
		return (DoublePoint)this.location.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLocation(DoublePoint location)
	{
		super.setLocation(location);
		setDistance(this.getFromStartLengthLt());
	}

	/**
	 * Получить топологическую дистанцию от начального узла линии до метки.
	 * @return дистанция
	 */
	public double getFromStartLengthLt()
	{
		getPhysicalLink().sortNodeLinks();

		double pathLength = 0;
		
		for(Iterator it = getPhysicalLink().getNodeLinks().iterator(); it.hasNext();){
			NodeLink nl = (NodeLink )it.next();
			if(nl.equals(this.nodeLink)){
				pathLength += this.getSizeInDoubleLt();
				break;
			}
			pathLength += nl.getLengthLt();
		}
		return pathLength;
	}

	/**
	 * Получить топологическую дистанцию от концевого узла линии до метки.
	 * @return дистанция
	 */
	public double getFromEndLengthLt()
	{
		getPhysicalLink().sortNodeLinks();

		double pathLength = 0;
		
		ListIterator it = getPhysicalLink().getNodeLinks().listIterator(
				getPhysicalLink().getNodeLinks().size());
		while(it.hasPrevious())
		{
			NodeLink nl = (NodeLink )it.previous();
			if(nl == this.nodeLink)
			{
				pathLength += nl.getLengthLt() - this.getSizeInDoubleLt();
				break;
			}
			pathLength += nl.getLengthLt();
		}
		return pathLength;
	}

	/**
	 * Установить листанцию от начального узла фрагмента линии, на которой
	 * находится метка, до метки. Вычисление осуществляется в том месте, где
	 * осуществляется управление передвижением метки.
	 * @param sizeInDoubleLt дистанция
	 */
	public void setSizeInDoubleLt(double sizeInDoubleLt)
	{
		this.sizeInDoubleLt = sizeInDoubleLt;
	}

	/**
	 * Получить листанцию от начального узла фрагмента линии, на которой
	 * находится метка, до метки. 
	 * @return дистанция
	 */
	public double getSizeInDoubleLt()
	{
		return this.sizeInDoubleLt;
	}

	/**
	 * {@inheritDoc}
	 * <br>Suppress since mark cannot be an end node
	 */
	public List getNodeLinks()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * <br>Suppress since mark cannot be an end node
	 */
	public NodeLink getOtherNodeLink(NodeLink nl)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * <br>Suppress since mark cannot be an end node
	 */
	public List getPhysicalLinks()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * <br>Suppress since mark cannot be an end node
	 */
	public List getOppositeNodes()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState()
	{
		return new NodeState(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(MapElementState state)
	{
		NodeState mnes = (NodeState )state;
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		setLocation(mnes.location);
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if(exportMap == null)
			exportMap = new HashMap();		
		synchronized(exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			exportMap.put(COLUMN_PHYSICAL_LINK_ID, this.physicalLink.getId());
			exportMap.put(COLUMN_DISTANCE, String.valueOf(this.distance));
			exportMap.put(COLUMN_X, String.valueOf(this.location.getX()));
			exportMap.put(COLUMN_Y, String.valueOf(this.location.getY()));
			exportMap.put(COLUMN_CITY, this.city);
			exportMap.put(COLUMN_STREET, this.street);
			exportMap.put(COLUMN_BUILDING, this.building);
			return Collections.unmodifiableMap(exportMap);
		}		
	}
	
	public static Mark createInstance(Identifier creatorId,
	                      			java.util.Map exportMap) throws CreateObjectException {
			Identifier id = (Identifier) exportMap.get(COLUMN_ID);
			String name = (String) exportMap.get(COLUMN_NAME);
			String description = (String) exportMap.get(COLUMN_DESCRIPTION);
      		Identifier physicalLinkId = (Identifier) exportMap.get(COLUMN_PHYSICAL_LINK_ID);
      		double distance = Double.parseDouble((String) exportMap.get(COLUMN_DISTANCE));
      		String city = (String) exportMap.get(COLUMN_CITY);
      		String street = (String) exportMap.get(COLUMN_STREET);
      		String building = (String) exportMap.get(COLUMN_BUILDING);
      		double x = Double.parseDouble((String) exportMap.get(COLUMN_X));
      		double y = Double.parseDouble((String) exportMap.get(COLUMN_Y));

      		if (id == null || creatorId == null || name == null || description == null || physicalLinkId == null 
      				|| city == null || street == null || building == null)
      			throw new IllegalArgumentException("Argument is 'null'");

      		try {
      			PhysicalLink physicalLink = (PhysicalLink ) 
      				MapStorableObjectPool.getStorableObject(
      					physicalLinkId, false);
      			return new Mark(
      					id, 
      					creatorId, 
      					name,
      					description,
      					x, 
      					y,
      					physicalLink,
      					distance,
      					city,
      					street,
      					building);
      		} catch (ApplicationException e) {
      			throw new CreateObjectException("Mark.createInstance |  ", e);
      		}
      	}

}
