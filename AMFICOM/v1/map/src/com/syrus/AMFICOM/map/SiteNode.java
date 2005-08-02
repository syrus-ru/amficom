/*-
 * $Id: SiteNode.java,v 1.61 2005/08/02 16:50:17 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.corba.IdlSiteNode;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeHelper;

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
 * @author $Author: krupenn $
 * @version $Revision: 1.61 $, $Date: 2005/08/02 16:50:17 $
 * @module map_v1
 */
public class SiteNode extends AbstractNode implements TypedObject, XMLBeansTransferable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257567325699190835L;

	private SiteNodeType type;

	private String city;
	private String street;
	private String building;

	SiteNode(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.SITENODE_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public SiteNode(final IdlSiteNode snt) throws CreateObjectException {
		super(snt);
		super.name = snt.name;
		super.description = snt.description;
		super.location = new DoublePoint(snt.longitude, snt.latitude);
		this.imageId = new Identifier(snt.imageId);
		this.city = snt.city;
		this.street = snt.street;
		this.building = snt.building;

		try {
			this.type = StorableObjectPool.getStorableObject(new Identifier(snt.siteNodeTypeId), true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected SiteNode(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
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
			final SiteNode siteNode = new SiteNode(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
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

		return SiteNode.createInstance(creatorId, type.getName(), "", type, location, "", "", "");
	}

	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
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
		return IdlSiteNodeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.imageId.getTransferable(),
				this.type.getId().getTransferable(),
				this.city,
				this.street,
				this.building);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public void setType(final StorableObjectType type) {
		this.type = (SiteNodeType) type;
		this.setImageId(this.type.getImageId());
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
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final Identifier imageId,
			final SiteNodeType type,
			final String city,
			final String street,
			final String building) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
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
		final SiteNodeState msnes = (SiteNodeState) state;

		this.setName(msnes.name);
		this.setDescription(msnes.description);
		this.setImageId(msnes.imageId);
		this.setLocation(msnes.location);

		try {
			this.setType((StorableObjectType )StorableObjectPool.getStorableObject(msnes.mapProtoId, true));
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public XmlObject getXMLTransferable() {
		final com.syrus.amficom.map.xml.SiteNode xmlSiteNode = com.syrus.amficom.map.xml.SiteNode.Factory.newInstance();
		this.fillXMLTransferable(xmlSiteNode);
		return xmlSiteNode;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		final com.syrus.amficom.map.xml.SiteNode xmlSiteNode = (com.syrus.amficom.map.xml.SiteNode) xmlObject; 

		final SiteNodeType siteNodeType = (SiteNodeType) this.getType(); 

		final com.syrus.amficom.general.xml.UID uid = xmlSiteNode.addNewUid();
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

	SiteNode(final Identifier creatorId,
			final StorableObjectVersion version,
			final com.syrus.amficom.map.xml.SiteNode xmlSiteNode,
			final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException,
				ApplicationException {

		super(clonedIdsPool.getClonedId(ObjectEntities.SITENODE_CODE, xmlSiteNode.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"",
				new DoublePoint(0, 0));
		if (xmlSiteNode.getUid().getStringValue().equals("507133")) {
			System.out.println("id for 507133 is " + this.id.toString());
		}
		this.selected = false;
		this.fromXMLTransferable(xmlSiteNode, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		final com.syrus.amficom.map.xml.SiteNode xmlSiteNode = (com.syrus.amficom.map.xml.SiteNode) xmlObject;

		this.name = xmlSiteNode.getName();
		this.description = xmlSiteNode.getDescription();
		this.city = xmlSiteNode.getCity();
		this.street = xmlSiteNode.getStreet();
		this.building = xmlSiteNode.getBuilding();
		super.location.setLocation(xmlSiteNode.getX(), xmlSiteNode.getY());

		String typeCodeName1 = xmlSiteNode.getSitenodetypeuid().toString();
		final TypicalCondition condition = new TypicalCondition(typeCodeName1,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SITENODE_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
		Set objects = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
		if (objects == null || objects.size() == 0) {
			typeCodeName1 = SiteNodeType.DEFAULT_BUILDING;

			condition.setValue(typeCodeName1);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			objects = StorableObjectPool.getStorableObjectsByCondition(condition, true, false);
			if (objects == null || objects.size() == 0) {
				throw new CreateObjectException("SiteNodeType \'" + SiteNodeType.DEFAULT_BUILDING + "\' not found");
			}
		}
		
		this.type = (SiteNodeType) objects.iterator().next();

		this.imageId = this.type.getImageId();
	}

	public static SiteNode createInstance(final Identifier creatorId, final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException {

		final com.syrus.amficom.map.xml.SiteNode xmlSiteNode = (com.syrus.amficom.map.xml.SiteNode) xmlObject;

		try {
			final SiteNode siteNode = new SiteNode(creatorId, StorableObjectVersion.createInitial(), xmlSiteNode, clonedIdsPool);
			assert siteNode.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			siteNode.markAsChanged();
			return siteNode;
		} catch (Exception e) {
			throw new CreateObjectException("SiteNode.createInstance |  ", e);
		}
	}
}
