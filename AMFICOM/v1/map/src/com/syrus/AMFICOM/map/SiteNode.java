/*
 * $Id: SiteNode.java,v 1.12 2005/01/17 15:05:24 bob Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ.
 * рТПЕЛФ: бнжйлпн.
 */

package com.syrus.AMFICOM.map;

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
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.12 $, $Date: 2005/01/17 15:05:24 $
 * @author $Author: bob $
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
	public static final String COLUMN_IMAGE_ID = "image_id";

	/** 
	 * массив параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static Object[][] exportColumns = null;	

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
			final Identifier imageId,
			String name,
			String description,
			SiteNodeType type,
			double longitude,
			double latitude,
			String city,
			String street,
			String building) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.imageId = imageId;
		this.type = type;
		this.name = name;
		this.description = description;
		this.location = new DoublePoint(longitude, latitude);
		this.city = city;
		this.street = street;
		this.building = building;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

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
			return new SiteNode(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE),
				creatorId,
				siteNodeType.getImageId(),
				name,
				description,
				siteNodeType,
				location.getX(),
				location.getY(),
				city,
				street,
				building);
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
		
		return new SiteNode(
				id,
				creatorId,
				type.getImageId(),
				name,
				description,
				type,
				x,
				y,
				city,
				street,
				building);
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.add(this.imageId);
		dependencies.addAll(this.characteristics);
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
		super.currentVersion = super.getNextVersion();
	}

	public String getBuilding() {
		return this.building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public void setStreet(String street) {
		this.street = street;
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
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
					modifierId);
			this.name = name;
			this.description = description;
			this.location.setLocation(longitude, latitude);
			this.imageId = imageId;
			this.type = type;
			this.city = city;
			this.street = street;
			this.building = building;					
	}

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
	 * @deprecated use {@link #getExportMap()}
	 */
	public Object[][] exportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new Object[10][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PROTO_ID;
			exportColumns[4][0] = COLUMN_X;
			exportColumns[5][0] = COLUMN_Y;
			exportColumns[6][0] = COLUMN_CITY;
			exportColumns[7][0] = COLUMN_STREET;
			exportColumns[8][0] = COLUMN_BUILDING;
			exportColumns[9][0] = COLUMN_IMAGE_ID;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getType().getId();
		exportColumns[4][1] = String.valueOf(getLocation().getX());
		exportColumns[5][1] = String.valueOf(getLocation().getY());
		exportColumns[6][1] = getCity();
		exportColumns[7][1] = getStreet();
		exportColumns[8][1] = getBuilding();
		exportColumns[9][1] = getImageId();
		
		return exportColumns;
	}
	
	public java.util.Map getExportMap() {
		if(exportMap == null)
			exportMap = new HashMap();		
		synchronized(exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			exportMap.put(COLUMN_PROTO_ID, this.type.getId());
			exportMap.put(COLUMN_X, String.valueOf(this.location.getX()));
			exportMap.put(COLUMN_Y, String.valueOf(this.location.getY()));
			exportMap.put(COLUMN_CITY, this.city);
			exportMap.put(COLUMN_STREET, this.street);
			exportMap.put(COLUMN_BUILDING, this.building);
			exportMap.put(COLUMN_IMAGE_ID, this.imageId);
			return Collections.unmodifiableMap(exportMap);
		}		
	}

	/**
	 * @deprecated use {@link #createInstance(Identifier, java.util.Map)}
	 */
	public static SiteNode createInstance(
			Identifier creatorId,
			Object[][] exportColumns)
		throws CreateObjectException 
	{
		Identifier id = null;
		String name = null;
		String description = null;
		Identifier typeId = null;
		String city = null;
		String street = null;
		String building = null;
		double x = -1.0D;
		double y = -1.0D;
		Identifier imageId = null;

		Object field;
		Object value;

		if (creatorId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		for(int i = 0; i < exportColumns.length; i++)
		{
			field = exportColumns[i][0];
			value = exportColumns[i][1];

			if(field.equals(COLUMN_ID))
				id = (Identifier )value;
			else
			if(field.equals(COLUMN_NAME))
				name = (String )value;
			else
			if(field.equals(COLUMN_DESCRIPTION))
				description = (String )value;
			else
			if(field.equals(COLUMN_PROTO_ID))
				typeId = (Identifier )value;
			else
			if(field.equals(COLUMN_X))
				x = Double.parseDouble((String )value);
			else
			if(field.equals(COLUMN_Y))
				y = Double.parseDouble((String )value);
			else
			if(field.equals(COLUMN_CITY))
				city = (String )value;
			else
			if(field.equals(COLUMN_STREET))
				street = (String )value;
			else
			if(field.equals(COLUMN_BUILDING))
				building = (String )value;
			else
			if(field.equals(COLUMN_PROTO_ID))
				imageId = (Identifier )value;
		}

		if (id == null || name == null || description == null || typeId == null 
				|| city == null || street == null || building == null || imageId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			SiteNodeType siteNodeType = (SiteNodeType ) 
				MapStorableObjectPool.getStorableObject(
					typeId, false);
			return new SiteNode(
					id, 
					creatorId, 
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

	public static SiteNode createInstance(Identifier creatorId,
	                                      java.util.Map exportMap) throws CreateObjectException {
			Identifier id = (Identifier) exportMap.get(COLUMN_ID);
			String name = (String) exportMap.get(COLUMN_NAME);
			String description = (String) exportMap.get(COLUMN_DESCRIPTION);
      		Identifier typeId = (Identifier) exportMap.get(COLUMN_PROTO_ID);
      		String city = (String) exportMap.get(COLUMN_CITY);
      		String street = (String) exportMap.get(COLUMN_STREET);
      		String building = (String) exportMap.get(COLUMN_BUILDING);
      		double x = Double.parseDouble((String) exportMap.get(COLUMN_X));
      		double y = Double.parseDouble((String) exportMap.get(COLUMN_Y));
      		Identifier imageId = (Identifier) exportMap.get(COLUMN_IMAGE_ID);

      		if (id == null || creatorId == null || name == null || description == null || typeId == null 
      				|| city == null || street == null || building == null || imageId == null)
      			throw new IllegalArgumentException("Argument is 'null'");

      		try {
      			SiteNodeType siteNodeType = (SiteNodeType ) 
      				MapStorableObjectPool.getStorableObject(
      					typeId, false);
      			return new SiteNode(
      					id, 
      					creatorId, 
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
