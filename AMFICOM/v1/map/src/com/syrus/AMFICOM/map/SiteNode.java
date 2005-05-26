/*-
 * $Id: SiteNode.java,v 1.38 2005/05/26 15:31:15 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
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
 * @version $Revision: 1.38 $, $Date: 2005/05/26 15:31:15 $
 * @module map_v1
 */
public class SiteNode extends AbstractNode implements TypedObject {

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

	SiteNode(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		SiteNodeDatabase database = (SiteNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITE_NODE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	SiteNode(SiteNode_Transferable snt) throws CreateObjectException {
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
		}
		catch (ApplicationException ae) {
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
			String name,
			String description,
			SiteNodeType siteNodeType,
			DoublePoint location,
			String city,
			String street,
			String building) throws CreateObjectException {

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
			SiteNode siteNode = new SiteNode(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE),
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
		}
		catch (IdentifierGenerationException ige) {
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

	public IDLEntity getTransferable() {
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
		this.type = (SiteNodeType) type;
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

	synchronized void setAttributes(Date created,
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
	public MapElementState getState() {
		return new SiteNodeState(this);
	}

	/**
	 * восстановить состояние
	 */
	public void revert(MapElementState state) {
		SiteNodeState msnes = (SiteNodeState) state;

		setName(msnes.name);
		setDescription(msnes.description);
		setImageId(msnes.imageId);
		setLocation(msnes.location);

		try {
			setType((SiteNodeType) (StorableObjectPool.getStorableObject(msnes.mapProtoId, true)));
		}
		catch (ApplicationException e) {
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
			}
			catch (ApplicationException e) {
				return null;
			}
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static SiteNode createInstance(Identifier creatorId, java.util.Map exportMap1) throws CreateObjectException {
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
					ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);

			Set set = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (set == null || set.size() == 0) {
				typeCodeName1 = SiteNodeType.DEFAULT_BUILDING;

				condition.setValue(typeCodeName1);

				set = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (set == null || set.size() == 0) {
					throw new CreateObjectException("SiteNodeType \'" + SiteNodeType.DEFAULT_BUILDING + "\' not found");
				}
			}
			siteNodeType = (SiteNodeType) set.iterator().next();

			Identifier imageId1;

			condition = new TypicalCondition(imageCodeName1,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);

			set = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (set == null || set.size() == 0) {
				//@todo add public static final String AbstractImageResource.DEFAULT_SITE_IMAGE = "images/building.gif"
//				imageCodeName1 = AbstractImageResource.DEFAULT_SITE_IMAGE;
				imageCodeName1 = "images/building.gif";

				condition.setValue(imageCodeName1);

				set = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (set == null || set.size() == 0) {
					throw new CreateObjectException("ImageResource \'" + imageCodeName1 + "\' not found");
				}
			}
			imageId1 = ((AbstractImageResource) set.iterator().next()).getId();

			SiteNode siteNode = new SiteNode(id1, creatorId, 0L, imageId1, name1, description1, siteNodeType, x1, y1, city1, street1, building1);
			siteNode.changed = true;
			return siteNode;
		}
		catch (ApplicationException e) {
			throw new CreateObjectException("SiteNode.createInstance |  ", e);
		}
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		this.changed = true;
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
}
