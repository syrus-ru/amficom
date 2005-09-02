/*-
 * $Id: SiteNode.java,v 1.78 2005/09/02 12:45:04 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlSiteNode;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeHelper;
import com.syrus.AMFICOM.map.xml.XmlSiteNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

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
 * @version $Revision: 1.78 $, $Date: 2005/09/02 12:45:04 $
 * @module map
 */
public class SiteNode extends AbstractNode implements TypedObject, XmlBeansTransferable<XmlSiteNode> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257567325699190835L;

	private SiteNodeType type;

	private String city;
	private String street;
	private String building;
	
	private Identifier attachmentSiteNodeId = Identifier.VOID_IDENTIFIER;

	SiteNode(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(SITENODE_CODE).retrieve(this);
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
		this.attachmentSiteNodeId = new Identifier(snt.attachmentSiteNodeId);

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
			final SiteNode siteNode = new SiteNode(IdentifierPool.getGeneratedIdentifier(SITENODE_CODE),
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

			assert siteNode.isValid() : OBJECT_STATE_ILLEGAL;

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
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

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
				this.building,
				this.attachmentSiteNodeId.getTransferable());
	}

	public SiteNodeType getType() {
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

	Identifier getAttachmentSiteNodeId() {
		return this.attachmentSiteNodeId;
	}

	void setAttachmentSiteNodeId(final Identifier attachedSiteNodeId) {
		this.attachmentSiteNodeId = attachedSiteNodeId;
		super.markAsChanged();
	}
	
	public void setAttachmentSiteNode(final SiteNode attachmentSiteNode) {
		this.attachmentSiteNodeId = attachmentSiteNode.getId();
		super.markAsChanged();
	}
	
	public SiteNode getAttachmentSiteNode() {
		try {
			return StorableObjectPool.<SiteNode>getStorableObject(this.getAttachmentSiteNodeId(), true);
		} catch(ApplicationException e) {
			Log.errorException(e);
			return null;
		}
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
			final String building,
			final Identifier attachedSiteNodeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.location.setLocation(longitude, latitude);
		this.imageId = imageId;
		this.type = type;
		this.city = city;
		this.street = street;
		this.building = building;
		this.attachmentSiteNodeId = attachedSiteNodeId;
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
			SiteNodeType siteNodeType = StorableObjectPool.getStorableObject(msnes.mapProtoId, true);
			this.setType(siteNodeType);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public XmlSiteNode getXmlTransferable() {
		final XmlSiteNode xmlSiteNode = XmlSiteNode.Factory.newInstance();
		xmlSiteNode.setId(this.id.getXmlTransferable());
		xmlSiteNode.setName(this.name);
		xmlSiteNode.setDescription(this.description);
		xmlSiteNode.setSiteNodeTypeCodename(this.getType().getCodename());
		xmlSiteNode.setX(this.location.getX());
		xmlSiteNode.setY(this.location.getY());
		xmlSiteNode.setCity(this.city);
		xmlSiteNode.setStreet(this.street);
		xmlSiteNode.setBuilding(this.building);
		return xmlSiteNode;
	}

	SiteNode(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlSiteNode xmlSiteNode,
			final ClonedIdsPool clonedIdsPool,
			final String importType)
			throws CreateObjectException,
				ApplicationException {

		super(clonedIdsPool.getClonedId(SITENODE_CODE, xmlSiteNode.getId()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"",
				new DoublePoint(0, 0));
		this.selected = false;
		this.fromXmlTransferable(xmlSiteNode, clonedIdsPool, importType);
	}

	public void fromXmlTransferable(final XmlSiteNode xmlSiteNode, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException {
		this.name = xmlSiteNode.getName();
		this.description = xmlSiteNode.getDescription();
		this.city = xmlSiteNode.getCity();
		this.street = xmlSiteNode.getStreet();
		this.building = xmlSiteNode.getBuilding();
		super.location.setLocation(xmlSiteNode.getX(), xmlSiteNode.getY());

		String typeCodeName1 = xmlSiteNode.getSiteNodeTypeCodename();
		final TypicalCondition condition = new TypicalCondition(typeCodeName1,
				OperationSort.OPERATION_EQUALS,
				SITENODE_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
		Set<SiteNodeType> siteNodeTypes = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
		if (siteNodeTypes == null || siteNodeTypes.size() == 0) {
			typeCodeName1 = SiteNodeType.DEFAULT_BUILDING;

			condition.setValue(typeCodeName1);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			siteNodeTypes = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			if (siteNodeTypes == null || siteNodeTypes.size() == 0) {
				throw new CreateObjectException("SiteNodeType \'" + SiteNodeType.DEFAULT_BUILDING + "\' not found");
			}
		}
		
		this.type = siteNodeTypes.iterator().next();

		this.imageId = this.type.getImageId();
	}

	public static SiteNode createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlSiteNode xmlSiteNode, 
			final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException {

		try {
			final XmlIdentifier xmlId = xmlSiteNode.getId();
			Identifier existingIdentifier = Identifier.fromXmlTransferable(xmlId, importType);
			SiteNode siteNode = null;
			if(existingIdentifier != null) {
				clonedIdsPool.setExistingId(xmlId, existingIdentifier);
				siteNode = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(siteNode != null) {
					siteNode.fromXmlTransferable(xmlSiteNode, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, xmlId);
				}
			}
			if(siteNode == null) {
				siteNode = new SiteNode(creatorId, StorableObjectVersion.createInitial(), xmlSiteNode, clonedIdsPool, importType);
				ImportUidMapDatabase.insert(importType, xmlId, siteNode.id);
			}
			assert siteNode.isValid() : OBJECT_STATE_ILLEGAL;
			siteNode.markAsChanged();
			return siteNode;
		} catch (Exception e) {
			throw new CreateObjectException("SiteNode.createInstance |  ", e);
		}
	}

	public Set<SiteNode> getAttachedSiteNodes() {
		LinkedIdsCondition condition = new LinkedIdsCondition(this.getAttachmentSiteNodeId(), this.getId().getMajor());
		try {
			return StorableObjectPool.<SiteNode>getStorableObjectsByCondition(condition, false);
		} catch(ApplicationException e) {
			Log.errorException(e);
			return Collections.emptySet();
		}
	}
}
