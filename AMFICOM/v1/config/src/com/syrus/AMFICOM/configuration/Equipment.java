/*
 * $Id: Equipment.java,v 1.151 2005/12/06 09:41:25 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.XML_BEAN_NOT_COMPLETE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.IdlEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentHelper;
import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.151 $, $Date: 2005/12/06 09:41:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class Equipment extends DomainMember<Equipment>
		implements MonitoredDomainMember,
		Characterizable,
		XmlBeansTransferable<XmlEquipment> {
	private static final long serialVersionUID = 2432748205979033898L;

	private Identifier protoEquipmentId;
	private String name;
	private String description;
	private Identifier imageId;
	private float latitude;
	private float longitude;
	private String supplier;
	private String supplierCode;
	private String hwSerial;
	private String hwVersion;
	private String swSerial;
	private String swVersion;
	private String inventoryNumber;

	public Equipment(final IdlEquipment et) throws CreateObjectException {
		try {
			this.fromTransferable(et);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Equipment(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final Identifier protoEquipmentId,
			final String name,
			final String description,
			final Identifier imageId,
			final String supplier,
			final String supplierCode,
			final float latitude,
			final float longitude,
			final String hwSerial,
			final String hwVersion,
			final String swSerial,
			final String swVersion,
			final String inventoryNumber) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);

		this.protoEquipmentId = protoEquipmentId;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hwSerial = hwSerial;
		this.hwVersion = hwVersion;
		this.swSerial = swSerial;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;
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
	private Equipment(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, EQUIPMENT_CODE, created, creatorId);
	}

	/**
	 * create new instance for client
	 *
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param imageId
	 * @throws CreateObjectException
	 */
	public static Equipment createInstance(final Identifier creatorId,
			final Identifier domainId,
			final Identifier protoEquipmentId,
			final String name,
			final String description,
			final Identifier imageId,
			final String supplier,
			final String supplierCode,
			final float latitude,
			final float longitude,
			final String hwSerial,
			final String hwVersion,
			final String swSerial,
			final String swVersion,
			final String inventoryNumber) throws CreateObjectException {
		if (creatorId == null
				|| domainId == null
				|| protoEquipmentId == null
				|| name == null
				|| description == null
				|| imageId == null
				|| supplier == null
				|| supplierCode == null
				|| hwSerial == null
				|| hwVersion == null
				|| swSerial == null
				|| swVersion == null
				|| inventoryNumber == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final Equipment equipment = new Equipment(IdentifierPool.getGeneratedIdentifier(EQUIPMENT_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					domainId,
					protoEquipmentId,
					name,
					description,
					imageId,
					supplier,
					supplierCode,
					latitude,
					longitude,
					hwSerial,
					hwVersion,
					swSerial,
					swVersion,
					inventoryNumber);

			assert equipment.isValid() : OBJECT_STATE_ILLEGAL;

			equipment.markAsChanged();

			return equipment;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * Create new instance on import/export from XML
	 * @param creatorId
	 * @param xmlEquipment
	 * @param importType
	 * @return new instance
	 * @throws CreateObjectException
	 */
	public static Equipment createInstance(final Identifier creatorId, final XmlEquipment xmlEquipment, final String importType)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlEquipment.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			Equipment equipment;
			if (id.isVoid()) {
				equipment = new Equipment(xmlId, importType, created, creatorId);
			} else {
				equipment = StorableObjectPool.getStorableObject(id, true);
				if (equipment == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					equipment = new Equipment(xmlId, importType, created, creatorId);
				}
			}
			equipment.fromXmlTransferable(xmlEquipment, importType);
			assert equipment.isValid() : OBJECT_BADLY_INITIALIZED;
			equipment.markAsChanged();
			return equipment;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlEquipment et = (IdlEquipment) transferable;
		super.fromTransferable(et, new Identifier(et.domainId));

		this.protoEquipmentId = new Identifier(et.protoEquipmentId);

		this.name = et.name;
		this.description = et.description;
		this.imageId = new Identifier(et.imageId);
		this.supplier = et.supplier;
		this.supplier = et.supplierCode;
		this.latitude = et.latitude;
		this.longitude = et.longitude;
		this.hwSerial = et.hwSerial;
		this.hwVersion = et.hwVersion;
		this.swSerial = et.swSerial;
		this.swVersion = et.swVersion;
		this.inventoryNumber = et.inventoryNumber;
	}

	/**
	 * @param equipment
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(final XmlEquipment equipment, final String importType) throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(equipment, EQUIPMENT_CODE, importType, PRE_IMPORT);

		this.name = equipment.getName();
		this.description = equipment.isSetDescription() ? equipment.getDescription() : "";
		this.supplier = equipment.isSetSupplier() ? equipment.getSupplier() : "";
		this.supplierCode = equipment.isSetSupplierCode() ? equipment.getSupplierCode() : "";
		this.latitude = equipment.getLatitude();
		this.longitude = equipment.getLongitude();
		this.hwSerial = equipment.isSetHwSerial() ? equipment.getHwSerial() : "";
		this.hwVersion = equipment.isSetHwVersion() ? equipment.getHwVersion() : "";
		this.swSerial = equipment.isSetSwSerial() ? equipment.getSwSerial() : "";
		this.swVersion = equipment.isSetSwVersion() ? equipment.getSwVersion() : "";
		this.inventoryNumber = equipment.isSetInventoryNumber() ? equipment.getInventoryNumber() : "";
		if (equipment.isSetDomainId()) {
			super.setDomainId0(Identifier.fromXmlTransferable(equipment.getDomainId(), importType, MODE_THROW_IF_ABSENT));
		} else {
			throw new UpdateObjectException("Equipment.fromXmlTransferable() | " + XML_BEAN_NOT_COMPLETE);
		}
		this.protoEquipmentId = Identifier.fromXmlTransferable(equipment.getProtoEquipmentId(), importType, MODE_THROW_IF_ABSENT);
		this.imageId = equipment.isSetSymbolId() ? Identifier.fromXmlTransferable(equipment.getSymbolId(),
				importType,
				MODE_THROW_IF_ABSENT) : VOID_IDENTIFIER;
		if (equipment.isSetCharacteristics()) {
			for (final XmlCharacteristic characteristic : equipment.getCharacteristics().getCharacteristicArray()) {
				Characteristic.createInstance(super.creatorId, characteristic, importType);
			}
		}

		XmlComplementorRegistry.complementStorableObject(equipment, EQUIPMENT_CODE, importType, POST_IMPORT);
	}

	/**
	 * @param orb
	 * @see com.syrus.util.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEquipment getIdlTransferable(final ORB orb) {
		return IdlEquipmentHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.getDomainId().getIdlTransferable(),
				this.protoEquipmentId.getIdlTransferable(),
				this.name != null ? this.name : "",
				this.description != null ? this.description : "",
				this.supplier != null ? this.supplier : "",
				this.supplierCode != null ? this.supplierCode : "",
				this.latitude,
				this.longitude,
				this.hwSerial != null ? this.hwSerial : "",
				this.hwVersion != null ? this.hwVersion : "",
				this.swSerial != null ? this.swSerial : "",
				this.swVersion != null ? this.swVersion : "",
				this.inventoryNumber != null ? this.inventoryNumber : "",
				this.imageId.getIdlTransferable());
	}

	/**
	 * @param equipment
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlEquipment equipment,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		super.id.getXmlTransferable(equipment.addNewId(), importType);
		equipment.setName(this.name);
		if (equipment.isSetDescription()) {
			equipment.unsetDescription();
		}
		if (this.description != null && this.description.length() != 0) {
			equipment.setDescription(this.description);
		}
		if (equipment.isSetSupplier()) {
			equipment.unsetSupplier();
		}
		if (this.supplier != null && this.supplier.length() != 0) {
			equipment.setSupplier(this.supplier);
		}
		if (equipment.isSetSupplierCode()) {
			equipment.unsetSupplierCode();
		}
		if (this.supplierCode != null && this.supplierCode.length() != 0) {
			equipment.setSupplierCode(this.supplierCode);
		}
		equipment.setLatitude(this.latitude);
		equipment.setLongitude(this.longitude);
		if (equipment.isSetHwSerial()) {
			equipment.unsetHwSerial();
		}
		if (this.hwSerial != null && this.hwSerial.length() != 0) {
			equipment.setHwSerial(this.hwSerial);
		}
		if (equipment.isSetHwVersion()) {
			equipment.unsetHwVersion();
		}
		if (this.hwVersion != null && this.hwVersion.length() != 0) {
			equipment.setHwVersion(this.hwVersion);
		}
		if (equipment.isSetSwSerial()) {
			equipment.unsetSwSerial();
		}
		if (this.swSerial != null && this.swSerial.length() != 0) {
			equipment.setSwSerial(this.swSerial);
		}
		if (equipment.isSetSwVersion()) {
			equipment.unsetSwVersion();
		}
		if (this.swVersion != null && this.swVersion.length() != 0) {
			equipment.setSwVersion(this.swVersion);
		}
		if (equipment.isSetInventoryNumber()) {
			equipment.unsetInventoryNumber();
		}
		if (this.inventoryNumber != null && this.inventoryNumber.length() != 0) {
			equipment.setInventoryNumber(this.inventoryNumber);
		}
		if (equipment.isSetDomainId()) {
			equipment.unsetDomainId();
		}
		final Identifier domainId = super.getDomainId();
		if (!domainId.isVoid()) {
			domainId.getXmlTransferable(equipment.addNewDomainId(), importType);
		}
		this.protoEquipmentId.getXmlTransferable(equipment.addNewProtoEquipmentId(), importType);
		if (equipment.isSetSymbolId()) {
			equipment.unsetSymbolId();
		}
		if (!this.imageId.isVoid()) {
			this.imageId.getXmlTransferable(equipment.addNewSymbolId(), importType);
		}
		if (equipment.isSetCharacteristics()) {
			equipment.unsetCharacteristics();
		}
		final Set<Characteristic> characteristics = this.getCharacteristics(false);
		if (false && !characteristics.isEmpty()) {
			final XmlCharacteristicSeq characteristicSeq = equipment.addNewCharacteristics();
			for (final Characteristic characteristic : characteristics) {
				characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
			}
		}

		XmlComplementorRegistry.complementStorableObject(equipment, EQUIPMENT_CODE, importType, EXPORT);
	}

	public Identifier getProtoEquipmentId() {
		return this.protoEquipmentId;
	}

	/**
	 * A wrapper around {@link #getProtoEquipmentId()}.
	 *
	 * @throws ApplicationException
	 */
	public ProtoEquipment getProtoEquipment() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getProtoEquipmentId(), true);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final Identifier protoEquipmentId,
			final String name,
			final String description,
			final Identifier imageId,
			final String supplier,
			final String supplierCode,
			final float latitude,
			final float longitude,
			final String hwSerial,
			final String hwVersion,
			final String swSerial,
			final String swVersion,
			final String inventoryNumber) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.protoEquipmentId = protoEquipmentId;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hwSerial = hwSerial;
		this.hwVersion = hwVersion;
		this.swSerial = swSerial;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies =  new HashSet<Identifiable>();
		dependencies.add(this.protoEquipmentId);
		return dependencies;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(final String supplier) {
		this.supplier = supplier;
		super.markAsChanged();
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final float latitude) {
		this.latitude = latitude;
		super.markAsChanged();
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final float longitude) {
		this.longitude = longitude;
		super.markAsChanged();
	}

	public String getHwSerial() {
		return this.hwSerial;
	}

	public void setHwSerial(final String hwSerial) {
		this.hwSerial = hwSerial;
		super.markAsChanged();
	}

	public String getHwVersion() {
		return this.hwVersion;
	}

	public void setHwVersion(final String hwVersion) {
		this.hwVersion = hwVersion;
		super.markAsChanged();
	}

	public String getInventoryNumber() {
		return this.inventoryNumber;
	}

	public void setInventoryNumber(final String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
		super.markAsChanged();
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(final String supplierCode) {
		this.supplierCode = supplierCode;
		super.markAsChanged();
	}

	public String getSwSerial() {
		return this.swSerial;
	}

	public void setSwSerial(final String swSerial) {
		this.swSerial = swSerial;
		super.markAsChanged();
	}

	public String getSwVersion() {
		return this.swVersion;
	}

	public void setSwVersion(final String swVersion) {
		this.swVersion = swVersion;
		super.markAsChanged();
	}	
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public void setProtoEquipmentId(final Identifier protoEquipmentId) {
		this.protoEquipmentId = protoEquipmentId;
		super.markAsChanged();
	}

	public void setProtoEquipment(final ProtoEquipment protoEquipment) {
		this.setProtoEquipmentId(Identifier.possiblyVoid(protoEquipment));
	}

	/**
	 * @param imageId The imageId to set.
	 */
	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.MonitoredDomainMember#getMonitoredElementIds()
	 */
	public Set<Identifier> getMonitoredElementIds() {
		// TODO Implement
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	/**
	 * @return <code>Collection&lt;Port&gt;</code>
	 */
	public Set<Port> getPorts() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, PORT_CODE),
					true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, Level.SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected EquipmentWrapper getWrapper() {
		return EquipmentWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
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
	public void addCharacteristic(final Characteristic characteristic,
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
	public void removeCharacteristic(
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
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
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
