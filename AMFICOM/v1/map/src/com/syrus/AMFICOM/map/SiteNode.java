/**
 * $Id: SiteNode.java,v 1.21 2005/02/14 10:30:56 bob Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.resource.AbstractBitmapImageResource;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;

/**
 * Сетевой узел на топологической схеме. Характеризуется типом 
 * (<code>{@link SiteNodeType}</code>). 
 * Предуствновленными являются несколько типов - 
 * колодец (<code>{@link SiteNodeType#WELL}</code>), 
 * пикет (<code>{@link SiteNodeType#PIQUET}</code>), 
 * кабельный ввод (<code>{@link SiteNodeType#CABLE_INLET}</code>) 
 * здание (<code>{@link SiteNodeType#BUILDING}</code>), 
 * телефонный узел (<code>{@link SiteNodeType#ATS}</code>). 
 * Дополнительно описывается полями
 * {@link #city}, {@link #street}, {@link #building} для поиска по 
 * географическим параметрам. 
 * @author $Author: bob $
 * @version $Revision: 1.21 $, $Date: 2005/02/14 10:30:56 $
 * @module map_v1
 */
public class SiteNode extends AbstractNode implements TypedObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257567325699190835L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_PROTO_ID = "proto_id";	
	public static final String COLUMN_CITY = "city";	
	public static final String COLUMN_STREET = "street";	
	public static final String COLUMN_BUILDING = "building";	
	public static final String COLUMN_COEF = "coef";
	public static final String COLUMN_IMAGE = "image_id";

	/** 
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	private SiteNodeType			type;

	private String					city;
	private String					street;
	private String					building;

	private StorableObjectDatabase	siteNodeDatabase;

	public SiteNode(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.siteNodeDatabase = MapDatabaseContext.getSiteNodeDatabase();
		try {
			this.siteNodeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public SiteNode(SiteNode_Transferable snt) throws CreateObjectException {
		super(snt.header);
		super.name = snt.name;
		super.description = snt.description;
		super.location = new DoublePoint(snt.longitude, snt.latitude);
		this.imageId = new Identifier(snt.imageId);		
		this.city = snt.city;
		this.street = snt.street;
		this.building = snt.building;

		try {
			this.type = (SiteNodeType) MapStorableObjectPool.getStorableObject(new Identifier(snt.siteNodeTypeId), true);
			
			this.characteristics = new ArrayList(snt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(snt.characteristicIds.length);
			for (int i = 0; i < snt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(snt.characteristicIds[i]));

			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected SiteNode(final Identifier id,
			final Identifier creatorId,
			final long version,
			final Identifier imageId,
			final String name,
			final String description,
			final SiteNodeType type,
			final double longitude,
			final double latitude,
			final String city,
			final String street,
			final String building) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version,
			name,
			description,
			new DoublePoint(longitude, latitude));
		this.imageId = imageId;
		this.type = type;
		this.city = city;
		this.street = street;
		this.building = building;

		this.characteristics = new LinkedList();

		this.siteNodeDatabase = MapDatabaseContext.getSiteNodeDatabase();

		this.selected = false;
	}

	public void insert() throws CreateObjectException {
		this.siteNodeDatabase = MapDatabaseContext.getSiteNodeDatabase();
		try {
			if (this.siteNodeDatabase != null)
				this.siteNodeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static SiteNode createInstance(
			final Identifier creatorId,
			String name,
			String description,
			SiteNodeType siteNodeType,
			DoublePoint location,
			String city,
			String street,
			String building)
		throws CreateObjectException {

		if (creatorId == null || name == null || description == null ||						 
				siteNodeType == null || location == null || city == null || street == null || building == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			SiteNode siteNode = new SiteNode(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE),
				creatorId,
				0L,
				siteNodeType.getImageId(),
				name,
				description,
				siteNodeType,
				location.getX(),
				location.getY(),
				city,
				street,
				building);
			siteNode.changed = true;
			return siteNode;
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("SiteNode.createInstance | cannot generate identifier ", e);
		}
	}

	public static SiteNode createInstance(
			final Identifier creatorId,
			final DoublePoint location,
			final SiteNodeType type)
		throws CreateObjectException {

		return SiteNode.createInstance(
			creatorId,
			type.getName(),
			"",
			type,
			location,
			"",
			"",
			"");		
	}

	public static SiteNode importInstance(
			final Identifier id,
			final Identifier creatorId,
			final String name,
			final String description,
			final SiteNodeType type,
			final double x,
			final double y,
			final String city,
			final String building,
			final String street)
		throws CreateObjectException {

		if (id == null 
			|| creatorId == null 
			|| name == null 
			|| description == null 
			|| type == null 
			|| city == null 
			|| street == null 
			|| building == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		SiteNode siteNode = new SiteNode(
				id,
				creatorId,
				0L,
				type.getImageId(),
				name,
				description,
				type,
				x,
				y,
				city,
				street,
				building);
		siteNode.changed = true;
		return siteNode;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.add(this.imageId);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new SiteNode_Transferable(super.getHeaderTransferable(), 
							this.name,
							this.description,
							this.location.getX(),
							this.location.getY(), 
							(Identifier_Transferable) this.imageId.getTransferable(),
							(Identifier_Transferable) this.type.getId().getTransferable(),
							this.city,
							this.street, 
							this.building, 
							charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public void setType(StorableObjectType type) {
		this.type = (SiteNodeType )type;
		setImageId(this.type.getImageId());
		this.changed = true;
	}

	public String getBuilding() {
		return this.building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
		this.changed = true;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
		this.changed = true;
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public void setStreet(String street) {
		this.street = street;
		this.changed = true;
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,	
											  long version,
											  String name,
											  String description,
											  double longitude,
											  double latitude,
											  Identifier imageId,
											  SiteNodeType type,
											  String city,
											  String street,
											  String building) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version);
			this.name = name;
			this.description = description;
			this.location.setLocation(longitude, latitude);
			this.imageId = imageId;
			this.type = type;
			this.city = city;
			this.street = street;
			this.building = building;					
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState()
	{
		return new SiteNodeState(this);
	}

	/**
	 * восстановить состояние
	 */
	public void revert(MapElementState state)
	{
		SiteNodeState msnes = (SiteNodeState)state;
		
		setName(msnes.name);
		setDescription(msnes.description);
		setImageId(msnes.imageId);
		setLocation(msnes.location);
		
		try
		{
			setType((SiteNodeType )(MapStorableObjectPool.getStorableObject(msnes.mapProtoId, true)));
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if(exportMap == null)
			exportMap = new HashMap();		
		synchronized(exportMap) {
			exportMap.clear();
			try
			{
				exportMap.put(COLUMN_ID, this.id);
				exportMap.put(COLUMN_NAME, this.name);
				exportMap.put(COLUMN_DESCRIPTION, this.description);
				exportMap.put(COLUMN_PROTO_ID, this.type.getCodename());
				exportMap.put(COLUMN_X, String.valueOf(this.location.getX()));
				exportMap.put(COLUMN_Y, String.valueOf(this.location.getY()));
				exportMap.put(COLUMN_CITY, this.city);
				exportMap.put(COLUMN_STREET, this.street);
				exportMap.put(COLUMN_BUILDING, this.building);
				AbstractBitmapImageResource imageResource = (AbstractBitmapImageResource )
					ResourceStorableObjectPool.getStorableObject(this.imageId, false);
				exportMap.put(COLUMN_IMAGE, imageResource.getCodename());
			}
			catch (ApplicationException e)
			{
				return null;
			}
			return Collections.unmodifiableMap(exportMap);
		}		
	}

	public static SiteNode createInstance(Identifier creatorId,
	                                      java.util.Map exportMap) throws CreateObjectException {
			Identifier id = (Identifier) exportMap.get(COLUMN_ID);
			String name = (String) exportMap.get(COLUMN_NAME);
			String description = (String) exportMap.get(COLUMN_DESCRIPTION);
	  		String typeCodeName = (String) exportMap.get(COLUMN_PROTO_ID);
      		String city = (String) exportMap.get(COLUMN_CITY);
      		String street = (String) exportMap.get(COLUMN_STREET);
      		String building = (String) exportMap.get(COLUMN_BUILDING);
      		double x = Double.parseDouble((String) exportMap.get(COLUMN_X));
      		double y = Double.parseDouble((String) exportMap.get(COLUMN_Y));
      		String imageCodeName = (String) exportMap.get(COLUMN_IMAGE);

      		if (id == null || creatorId == null || name == null || description == null
      				|| city == null || street == null || building == null)
      			throw new IllegalArgumentException("Argument is 'null'");

      		try {
				SiteNodeType siteNodeType;
	
				TypicalCondition condition = new TypicalCondition(
						typeCodeName,
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE,
						StorableObjectWrapper.COLUMN_CODENAME);
				
				Collection collection = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if(collection == null || collection.size() == 0)
				{
					typeCodeName = SiteNodeType.BUILDING;
	
					condition.setValue(typeCodeName);
					
					collection = MapStorableObjectPool.getStorableObjectsByCondition(condition, true);
					if(collection == null || collection.size() == 0)
					{
						throw new CreateObjectException("SiteNodeType \'" + SiteNodeType.BUILDING + "\' not found");
					}
				}
				siteNodeType = (SiteNodeType) collection.iterator().next();

				Identifier imageId;
	
				condition = new TypicalCondition(
					imageCodeName,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
				
				collection = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if(collection == null || collection.size() == 0)
				{
					imageCodeName = SiteNodeType.BUILDING_IMAGE;
	
					condition.setValue(imageCodeName);
					
					collection = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);
					if(collection == null || collection.size() == 0)
					{
						throw new CreateObjectException("ImageResource \'" + SiteNodeType.BUILDING_IMAGE + "\' not found");
					}
				}
				imageId = ((AbstractImageResource ) collection.iterator().next()).getId();

      			return new SiteNode(
      					id,
      					creatorId,
      					0L,
      					imageId,
      					name,
      					description,
      					siteNodeType,
      					x, 
      					y,
      					city,
      					street,
      					building);
      		} catch (ApplicationException e) {
      			throw new CreateObjectException("SiteNode.createInstance |  ", e);
      		}
      	}
}
