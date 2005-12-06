/*-
 * $Id: SiteNode.java,v 1.117 2005/12/06 09:43:34 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
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
 * @author $Author: bass $
 * @version $Revision: 1.117 $, $Date: 2005/12/06 09:43:34 $
 * @module map
 */
public class SiteNode extends AbstractNode<SiteNode>
		implements Characterizable,
		TypedObject<SiteNodeType>, XmlBeansTransferable<XmlSiteNode> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257567325699190835L;

	private SiteNodeType type;

	private String city;
	private String street;
	private String building;
	
	private Identifier attachmentSiteNodeId = Identifier.VOID_IDENTIFIER;

	public SiteNode(final IdlSiteNode snt) throws CreateObjectException {
		super(snt);
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

		final boolean usePool = false;

		try {
			final SiteNode siteNode = new SiteNode(IdentifierPool.getGeneratedIdentifier(SITENODE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					siteNodeType.getImageId(),
					name,
					description,
					siteNodeType,
					location.getX(),
					location.getY(),
					city,
					street,
					building);

			siteNode.copyCharacteristics(siteNodeType, usePool);

			assert siteNode.isValid() : OBJECT_BADLY_INITIALIZED;

			siteNode.markAsChanged();

			return siteNode;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	private void copyCharacteristics(SiteNodeType siteNodeType, boolean usePool) throws ApplicationException {
		try {
			for (final Characteristic characteristic : siteNodeType.getCharacteristics0(usePool)) {
				if(characteristic.getType().getSort().value() == CharacteristicTypeSort._CHARACTERISTICTYPESORT_OPERATIONAL) {
					final Characteristic characteristicClone = characteristic.clone();
					this.addCharacteristic(characteristicClone, usePool);
				}
			}
		} catch (final CloneNotSupportedException cnse) {
			throw new CreateObjectException(cnse);
		}
	}

	public static SiteNode createInstance(final Identifier creatorId, final DoublePoint location, final SiteNodeType type)
			throws CreateObjectException {

		return SiteNode.createInstance(creatorId, type.getName(), "", type, location, "", "", "");
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		dependencies.add(this.imageId);
		dependencies.add(this.attachmentSiteNodeId);
		
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);

		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSiteNode getIdlTransferable(final ORB orb) {
		return IdlSiteNodeHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.location.getX(),
				this.location.getY(),
				this.imageId.getIdlTransferable(),
				this.type.getId().getIdlTransferable(),
				this.city,
				this.street,
				this.building,
				this.attachmentSiteNodeId.getIdlTransferable());
	}
	
	@Override
	protected synchronized void fromTransferable(IdlStorableObject transferable) throws ApplicationException {
		IdlSiteNode idlSiteNode = (IdlSiteNode) transferable; 
		super.fromTransferable(idlSiteNode);
		this.imageId = new Identifier(idlSiteNode.imageId);
		this.city = idlSiteNode.city;
		this.street = idlSiteNode.street;
		this.building = idlSiteNode.building;
		this.attachmentSiteNodeId = new Identifier(idlSiteNode.attachmentSiteNodeId);

		try {
			this.type = StorableObjectPool.getStorableObject(new Identifier(idlSiteNode.siteNodeTypeId), true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public final SiteNodeType getType() {
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

	void setAttachmentSiteNodeId(final Identifier attachmentSiteNodeId) {
		this.attachmentSiteNodeId = Identifier.possiblyVoid(attachmentSiteNodeId);
		super.markAsChanged();
	}
	
	public void setAttachmentSiteNode(final SiteNode attachmentSiteNode) {
		this.attachmentSiteNodeId = attachmentSiteNode.getId();
		super.markAsChanged();
	}
	
	public SiteNode getAttachmentSiteNode() {
		try {
			return StorableObjectPool.<SiteNode>getStorableObject(this.attachmentSiteNodeId, true);
		} catch(ApplicationException e) {
			Log.errorMessage(e);
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
			final Identifier attachmentSiteNodeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.location.setLocation(longitude, latitude);
		this.imageId = imageId;
		this.type = type;
		this.city = city;
		this.street = street;
		this.building = building;
		this.attachmentSiteNodeId = Identifier.possiblyVoid(attachmentSiteNodeId);
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

	/**
	 * @param siteNode
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public final void getXmlTransferable(final XmlSiteNode siteNode,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		this.id.getXmlTransferable(siteNode.addNewId(), importType);
		siteNode.setName(this.name);
		if(this.description != null && this.description.length() != 0) {
			siteNode.setDescription(this.description);
		}
		siteNode.setSiteNodeTypeCodename(this.getType().getCodename());
		siteNode.setX(this.location.getX());
		siteNode.setY(this.location.getY());
		if(this.city != null && this.city.length() != 0) {
			siteNode.setCity(this.city);
		}
		if(this.street != null && this.street.length() != 0) {
			siteNode.setStreet(this.street);
		}
		if(this.building != null && this.building.length() != 0) {
			siteNode.setBuilding(this.building);
		}
		if (!this.attachmentSiteNodeId.isVoid()) {
			this.attachmentSiteNodeId.getXmlTransferable(siteNode.addNewAttachmentSiteNodeId(), importType);
		}
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private SiteNode(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SITENODE_CODE, created, creatorId);
		/**
		 * @todo Should go to #fromXmlTransferable(...) or
		 *       the corresponding complementor.
		 */
		this.name = "";
		this.description = "";
		this.location = new DoublePoint(.0, .0);
		this.selected = false;
	}

	public final void fromXmlTransferable(final XmlSiteNode xmlSiteNode,
			final String importType)
	throws ApplicationException {
		this.name = xmlSiteNode.getName();
		if(xmlSiteNode.isSetDescription()) {
			this.description = xmlSiteNode.getDescription();
		}
		else {
			this.description = "";
		}
		if(xmlSiteNode.isSetCity()) {
			this.city = xmlSiteNode.getCity();
		}
		else {
			this.city = "";
		}
		if(xmlSiteNode.isSetStreet()) {
			this.street = xmlSiteNode.getStreet();
		}
		else {
			this.street = "";
		}
		if(xmlSiteNode.isSetBuilding()) {
			this.building = xmlSiteNode.getBuilding();
		}
		else {
			this.building = "";
		}

		super.location.setLocation(xmlSiteNode.getX(), xmlSiteNode.getY());
		if (xmlSiteNode.isSetAttachmentSiteNodeId()) {
			// NOTE: this call to Identifier.fromXmlTransferable may result in
			// ObjectNotFoundException if this siteNode is being imported prior
			// to it's attachment site node.
			// to avoid the exception make sure imported site node are correctly
			// sorted. This is done by sorting site nodes at export time
			// (exporting from AMFICOM - see Map#getXmlTransferable,
			// exporting from UniCableMap - see module 'importUCM').
			this.attachmentSiteNodeId = Identifier.fromXmlTransferable(xmlSiteNode.getAttachmentSiteNodeId(), importType, MODE_THROW_IF_ABSENT); 
		} else {
			this.attachmentSiteNodeId = VOID_IDENTIFIER;
		}

		final TypicalCondition condition = new TypicalCondition(xmlSiteNode.getSiteNodeTypeCodename(),
				OperationSort.OPERATION_EQUALS,
				SITENODE_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
		Set<SiteNodeType> siteNodeTypes = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
		if (siteNodeTypes.isEmpty()) {
			condition.setValue(SiteNodeType.DEFAULT_BUILDING);

			//NOTE: This call never results in using loader, so it doesn't matter what to pass as 3-d argument
			siteNodeTypes = StorableObjectPool.getStorableObjectsByCondition(condition, false, false);
			if (siteNodeTypes.isEmpty()) {
				throw new CreateObjectException("SiteNodeType \'" + SiteNodeType.DEFAULT_BUILDING + "\' not found");
			}
		}
		
		this.type = siteNodeTypes.iterator().next();

		this.imageId = this.type.getImageId();
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlSiteNode
	 * @throws CreateObjectException
	 */
	public static SiteNode createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlSiteNode xmlSiteNode) 
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlSiteNode.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SiteNode siteNode;
			if (id.isVoid()) {
				siteNode = new SiteNode(xmlId,
						importType,
						created,
						creatorId);
			} else {
				siteNode = StorableObjectPool.getStorableObject(id, true);
				if (siteNode == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					siteNode = new SiteNode(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			siteNode.fromXmlTransferable(xmlSiteNode, importType);
			assert siteNode.isValid() : OBJECT_BADLY_INITIALIZED;
			siteNode.markAsChanged();
			return siteNode;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	public Set<SiteNode> getAttachedSiteNodes() {
		LinkedIdsCondition condition = new LinkedIdsCondition(this.id, SITENODE_CODE);
		try {
			return StorableObjectPool.<SiteNode>getStorableObjectsByCondition(condition, true);
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
		return Collections.emptySet();
	}

	public Characterizable getCharacterizable() {
		return this;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected final SiteNodeWrapper getWrapper() {
		return SiteNodeWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public final StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public final void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public final void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public final Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	final Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public final void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
