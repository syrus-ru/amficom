/*-
 * $Id: SiteNode.java,v 1.50 2005/06/25 17:07:48 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.corba.IdlSiteNode;
import com.syrus.AMFICOM.resource.AbstractBitmapImageResource;
import com.syrus.AMFICOM.resource.AbstractImageResource;

/**
 * Сетевой узел на топологической схеме. Характеризуется типом
 * (<code>{@link SiteNodeType}</code>).
 * Предуствновленными являются несколько типов -
 * колодец (<code>{@link SiteNodeType#DEFAULT_WELL}</code>),
 * пикет (<code>{@link SiteNodeType#DEFAULT_PIQUET}</code>),
 * кабельный ввод (<code>{@link SiteNodeType#DEFAULT_CABLE_INLET}</code>)
 * здание (<code>{@link SiteNodeType#DEFAULT_BUILDING}</code>),
 * телефонный узел (<code>{@link SiteNodeType#DEFAULT_ATS}</code>).
 * Дополнительно описывается полями
 * {@link #city}, {@link #street}, {@link #building} для поиска по
 * географическим параметрам.
 * @author $Author: bass $
 * @version $Revision: 1.50 $, $Date: 2005/06/25 17:07:48 $
 * @module map_v1
 */
public class SiteNode extends AbstractNode implements TypedObject, XMLBeansTransferable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257567325699190835L;

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

	private SiteNodeType type;

	private String city;
	private String street;
	private String building;

	SiteNode(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		SiteNodeDatabase database = (SiteNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITENODE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	SiteNode(final IdlSiteNode snt) throws CreateObjectException {
		super(snt.header);
		super.name = snt.name;
		super.description = snt.description;
		super.location = new DoublePoint(snt.longitude, snt.latitude);
		this.imageId = new Identifier(snt.imageId);
		this.city = snt.city;
		this.street = snt.street;
		this.building = snt.building;

		try {
			this.type = (SiteNodeType) StorableObjectPool.getStorableObject(new Identifier(snt.siteNodeTypeId), true);

			this.characteristics = new HashSet(snt.characteristicIds.length);
			Set characteristicIds = new HashSet(snt.characteristicIds.length);
			for (int i = 0; i < snt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(snt.characteristicIds[i]));

			this.characteristics.addAll(StorableObjectPool.getStorableObjects(characteristicIds, true));
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

		this.characteristics = new HashSet();

		this.selected = false;
	}

	public static SiteNode createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final SiteNodeType siteNodeType,
			final DoublePoint location,
			final String city,
			final String street,
			final String building) throws CreateObjectException {

		if (creatorId == null
				|| name == null
				|| description == null
				|| siteNodeType == null
				|| location == null
				|| city == null
				|| street == null
				|| building == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			SiteNode siteNode = new SiteNode(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_CODE),
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

			assert siteNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			siteNode.markAsChanged();

			return siteNode;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public static SiteNode createInstance(final Identifier creatorId, final DoublePoint location, final SiteNodeType type)
			throws CreateObjectException {

		return SiteNode.createInstance(creatorId,
				type.getName(),
				"",
				type,
				location,
				"",
				"",
				"");
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.add(this.type);
		dependencies.add(this.imageId);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSiteNode getTransferable(final ORB orb) {
		int i = 0;
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return new IdlSiteNode(super.getHeaderTransferable(orb),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.imageId.getTransferable(),
				this.type.getId().getTransferable(),
				this.city,
				this.street,
				this.building,
				charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public void setType(final StorableObjectType type) {
		this.type = (SiteNodeType) type;
		setImageId(this.type.getImageId());
		super.markAsChanged();
	}

	public String getBuilding() {
		return this.building;
	}

	public void setBuilding(final String building) {
		this.building = building;
		super.markAsChanged();
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
		super.markAsChanged();
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(final String street) {
		this.street = street;
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final Identifier imageId,
			final SiteNodeType type,
			final String city,
			final String street,
			final String building) {
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
	public MapElementState getState() {
		return new SiteNodeState(this);
	}

	/**
	 * восстановить состояние
	 */
	public void revert(final MapElementState state) {
		SiteNodeState msnes = (SiteNodeState) state;

		setName(msnes.name);
		setDescription(msnes.description);
		setImageId(msnes.imageId);
		setLocation(msnes.location);

		try {
			setType((SiteNodeType) (StorableObjectPool.getStorableObject(msnes.mapProtoId, true)));
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			try {
				exportMap.put(COLUMN_ID, this.id);
				exportMap.put(COLUMN_NAME, this.name);
				exportMap.put(COLUMN_DESCRIPTION, this.description);
				exportMap.put(COLUMN_PROTO_ID, this.type.getCodename());
				exportMap.put(COLUMN_X, String.valueOf(this.location.getX()));
				exportMap.put(COLUMN_Y, String.valueOf(this.location.getY()));
				exportMap.put(COLUMN_CITY, this.city);
				exportMap.put(COLUMN_STREET, this.street);
				exportMap.put(COLUMN_BUILDING, this.building);
				AbstractBitmapImageResource imageResource = (AbstractBitmapImageResource) StorableObjectPool.getStorableObject(this.imageId,
						false);
				exportMap.put(COLUMN_IMAGE, imageResource.getCodename());
			} catch (ApplicationException e) {
				return null;
			}
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static SiteNode createInstance(final Identifier creatorId, final java.util.Map exportMap1) throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		String description1 = (String) exportMap1.get(COLUMN_DESCRIPTION);
		String typeCodeName1 = (String) exportMap1.get(COLUMN_PROTO_ID);
		String city1 = (String) exportMap1.get(COLUMN_CITY);
		String street1 = (String) exportMap1.get(COLUMN_STREET);
		String building1 = (String) exportMap1.get(COLUMN_BUILDING);
		double x1 = Double.parseDouble((String) exportMap1.get(COLUMN_X));
		double y1 = Double.parseDouble((String) exportMap1.get(COLUMN_Y));
		String imageCodeName1 = (String) exportMap1.get(COLUMN_IMAGE);

		if (id1 == null
				|| creatorId == null
				|| name1 == null
				|| description1 == null
				|| city1 == null
				|| street1 == null
				|| building1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			SiteNodeType siteNodeType;

			TypicalCondition condition = new TypicalCondition(typeCodeName1,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.SITENODE_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			Set set = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
			if (set == null || set.size() == 0) {
				typeCodeName1 = SiteNodeType.DEFAULT_BUILDING;

				condition.setValue(typeCodeName1);

				//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
				set = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
				if (set == null || set.size() == 0) {
					throw new CreateObjectException("SiteNodeType \'" + SiteNodeType.DEFAULT_BUILDING + "\' not found");
				}
			}
			siteNodeType = (SiteNodeType) set.iterator().next();

			Identifier imageId1;

			condition = new TypicalCondition(imageCodeName1,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.IMAGERESOURCE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			set = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
			if (set == null || set.size() == 0) {
				//@todo add public static final String AbstractImageResource.DEFAULT_SITE_IMAGE = "images/building.gif"
//				imageCodeName1 = AbstractImageResource.DEFAULT_SITE_IMAGE;
				imageCodeName1 = "images/building.gif";

				condition.setValue(imageCodeName1);

				//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
				set = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
				if (set == null || set.size() == 0) {
					throw new CreateObjectException("ImageResource \'" + imageCodeName1 + "\' not found");
				}
			}
			imageId1 = ((AbstractImageResource) set.iterator().next()).getId();

			SiteNode siteNode = new SiteNode(id1, creatorId, 0L, imageId1, name1, description1, siteNodeType, x1, y1, city1, street1, building1);

			assert siteNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			siteNode.markAsChanged();

			return siteNode;
		} catch (ApplicationException e) {
			throw new CreateObjectException("SiteNode.createInstance |  ", e);
		}
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public XmlObject getXMLTransferable() {
		com.syrus.amficom.map.xml.SiteNode xmlSiteNode = com.syrus.amficom.map.xml.SiteNode.Factory.newInstance();
		fillXMLTransferable(xmlSiteNode);
		return xmlSiteNode;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		com.syrus.amficom.map.xml.SiteNode xmlSiteNode = (com.syrus.amficom.map.xml.SiteNode )xmlObject; 

		SiteNodeType siteNodeType = (SiteNodeType) this.getType(); 

		com.syrus.amficom.general.xml.UID uid = xmlSiteNode.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlSiteNode.setName(this.name);
		xmlSiteNode.setDescription(this.description);
		xmlSiteNode.setSitenodetypeuid(com.syrus.amficom.map.xml.SiteNodeTypeSort.Enum.forString(siteNodeType.getSort().value()));
		xmlSiteNode.setX(this.location.getX());
		xmlSiteNode.setY(this.location.getY());
		xmlSiteNode.setCity(this.city);
		xmlSiteNode.setStreet(this.street);
		xmlSiteNode.setBuilding(this.building);
	}

	SiteNode(
			final Identifier creatorId, 
			final com.syrus.amficom.map.xml.SiteNode xmlSiteNode, 
			final ClonedIdsPool clonedIdsPool) 
		throws CreateObjectException, ApplicationException {

		super(
				clonedIdsPool.getClonedId(
						ObjectEntities.SITENODE_CODE, 
						xmlSiteNode.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				0,
				"",
				"",
				new DoublePoint(0, 0));
		if(xmlSiteNode.getUid().getStringValue().equals("507133")) {
			System.out.println("id for 507133 is " + this.id.toString());
		}
		this.characteristics = new HashSet();
		this.selected = false;
		this.fromXMLTransferable(xmlSiteNode, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		com.syrus.amficom.map.xml.SiteNode xmlSiteNode = (com.syrus.amficom.map.xml.SiteNode )xmlObject;

		this.name = xmlSiteNode.getName();
		this.description = xmlSiteNode.getDescription();
		this.city = xmlSiteNode.getCity();
		this.street = xmlSiteNode.getStreet();
		this.building = xmlSiteNode.getBuilding();
		super.location.setLocation(xmlSiteNode.getX(), xmlSiteNode.getY());

		String typeCodeName1 = xmlSiteNode.getSitenodetypeuid().toString();
		TypicalCondition condition = new TypicalCondition(typeCodeName1,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SITENODE_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
		Collection collection = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
		if (collection == null || collection.size() == 0) {
			typeCodeName1 = SiteNodeType.DEFAULT_BUILDING;

			condition.setValue(typeCodeName1);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			collection = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
			if (collection == null || collection.size() == 0) {
				throw new CreateObjectException("SiteNodeType \'" + SiteNodeType.DEFAULT_BUILDING + "\' not found");
			}
		}
		
		this.type = (SiteNodeType) collection.iterator().next();

		this.imageId = this.type.getImageId();
	}

	public static SiteNode createInstance(final Identifier creatorId, final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException {

		com.syrus.amficom.map.xml.SiteNode xmlSiteNode = (com.syrus.amficom.map.xml.SiteNode )xmlObject;

		try {
			SiteNode siteNode = new SiteNode(creatorId, xmlSiteNode, clonedIdsPool);
			assert siteNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			siteNode.markAsChanged();
			return siteNode;
		} catch (Exception e) {
			throw new CreateObjectException("SiteNode.createInstance |  ", e);
		}
	}
}
