/*
 * $Id: Equipment.java,v 1.96 2005/06/17 13:06:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.96 $, $Date: 2005/06/17 13:06:56 $
 * @author $Author: bass $
 * @module config_v1
 */

public final class Equipment extends DomainMember implements MonitoredDomainMember, Characterizable, TypedObject {

	private static final long serialVersionUID = -6115401698444070841L;

	private EquipmentType type;
	private String name;
	private String description;
	private Identifier imageId;
	private float longitude;
	private float latitude;
	private String supplier;
	private String supplierCode;
	private String hwSerial;
	private String hwVersion;
	private String swSerial;
	private String swVersion;
	private String inventoryNumber;

	private Set characteristics;

	Equipment(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();

		EquipmentDatabase database = (EquipmentDatabase) DatabaseContext.getDatabase(ObjectEntities.EQUIPMENT_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	Equipment(final Equipment_Transferable et) throws CreateObjectException {
		try {
			this.fromTransferable(et);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Equipment(final Identifier id,
			final Identifier creatorId,
			final long version,
			final Identifier domainId,
			final EquipmentType type,
			final String name,
			final String description,
			final Identifier imageId,
			final String supplier,
			final String supplierCode,
			final float longitude,
			final float latitude,
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

		this.type = type;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.hwSerial = hwSerial;
		this.hwVersion = hwVersion;
		this.swSerial = swSerial;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;

		this.characteristics = new HashSet();
	}

	/**
	 * create new instance for client
	 *
	 * @param creatorId
	 * @param domainId
	 * @param type
	 * @param name
	 * @param description
	 * @param imageId
	 * @throws CreateObjectException
	 */
	public static Equipment createInstance(final Identifier creatorId,
			final Identifier domainId,
			final EquipmentType type,
			final String name,
			final String description,
			final Identifier imageId,
			final String supplier,
			final String supplierCode,
			final float longitude,
			final float latitude,
			final String hwSerial,
			final String hwVersion,
			final String swSerial,
			final String swVersion,
			final String inventoryNumber) throws CreateObjectException {
		if (creatorId == null
				|| domainId == null
				|| type == null
				|| name == null
				|| description == null
				|| imageId == null
				|| supplier == null
				|| supplierCode == null
				|| hwSerial == null
				|| hwVersion == null
				|| swSerial == null
				|| swVersion == null
				|| inventoryNumber == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Equipment equipment = new Equipment(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENT_CODE),
					creatorId,
					0L,
					domainId,
					type,
					name,
					description,
					imageId,
					supplier,
					supplierCode,
					longitude,
					latitude,
					hwSerial,
					hwVersion,
					swSerial,
					swVersion,
					inventoryNumber);

			assert equipment.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			equipment.markAsChanged();

			return equipment;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		Equipment_Transferable et = (Equipment_Transferable) transferable;
		super.fromTransferable(et.header, new Identifier(et.domain_id));

		this.type = (EquipmentType) StorableObjectPool.getStorableObject(new Identifier(et.type_id), true);

		this.name = et.name;
		this.description = et.description;
		this.imageId = new Identifier(et.image_id);
		this.supplier = et.supplier;
		this.supplier = et.supplierCode;
		this.longitude = et.longitude;
		this.latitude = et.latitude;
		this.hwSerial = et.hwSerial;
		this.hwVersion = et.hwVersion;
		this.swSerial = et.swSerial;
		this.swVersion = et.swVersion;
		this.inventoryNumber = et.inventoryNumber;

		Set characteristicIds = Identifier.fromTransferables(et.characteristic_ids);
		this.characteristics = new HashSet(et.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return new Equipment_Transferable(super.getHeaderTransferable(),
				(IdlIdentifier) this.getDomainId().getTransferable(),
				(IdlIdentifier) this.type.getId().getTransferable(),
				this.name != null ? this.name : "",
				this.description != null ? this.description : "",
				this.supplier != null ? this.supplier : "",
				this.supplierCode != null ? this.supplierCode : "",
				this.longitude,
				this.latitude,
				this.hwSerial != null ? this.hwSerial : "",
				this.hwVersion != null ? this.hwVersion : "",
				this.swSerial != null ? this.swSerial : "",
				this.swVersion != null ? this.swVersion : "",
				this.inventoryNumber != null ? this.inventoryNumber : "",
				(IdlIdentifier) this.imageId.getTransferable(),
				charIds);
	}

	public StorableObjectType getType() {
		return this.type;
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

	public void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.markAsChanged();
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final Identifier domainId,
			final EquipmentType type,
			final String name,
			final String description,
			final Identifier imageId,
			final String supplier,
			final String supplierCode,
			final float longitude,
			final float latitude,
			final String hwSerial,
			final String hwVersion,
			final String swSerial,
			final String swVersion,
			final String inventoryNumber) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.type = type;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.hwSerial = hwSerial;
		this.hwVersion = hwVersion;
		this.swSerial = swSerial;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;
	}

	public Set getDependencies() {
		return Collections.EMPTY_SET;
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
	/**
	 * @param type The type to set.
	 */
	public void setType(final EquipmentType type) {
		this.type = type;
		super.markAsChanged();
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
	public Set getMonitoredElementIds() {
		// TODO Implement
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * @return <code>Collection&lt;Port&gt;</code>
	 */
	public Set getPorts(final boolean breakOnLoadError) {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.PORT_CODE),
					true,
					breakOnLoadError);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}
}
