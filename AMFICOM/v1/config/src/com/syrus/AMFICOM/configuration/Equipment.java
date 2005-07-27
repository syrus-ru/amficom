/*
 * $Id: Equipment.java,v 1.111 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.IdlEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.111 $, $Date: 2005/07/27 15:59:22 $
 * @author $Author: bass $
 * @module config
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

	Equipment(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.EQUIPMENT_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

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
			final Equipment equipment = new Equipment(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENT_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
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

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlEquipment et = (IdlEquipment) transferable;
		super.fromTransferable(et, new Identifier(et.domainId));

		this.type = (EquipmentType) StorableObjectPool.getStorableObject(new Identifier(et._typeId), true);

		this.name = et.name;
		this.description = et.description;
		this.imageId = new Identifier(et.imageId);
		this.supplier = et.supplier;
		this.supplier = et.supplierCode;
		this.longitude = et.longitude;
		this.latitude = et.latitude;
		this.hwSerial = et.hwSerial;
		this.hwVersion = et.hwVersion;
		this.swSerial = et.swSerial;
		this.swVersion = et.swVersion;
		this.inventoryNumber = et.inventoryNumber;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEquipment getTransferable(final ORB orb) {
		return IdlEquipmentHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.getDomainId().getTransferable(),
				this.type.getId().getTransferable(),
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
				this.imageId.getTransferable());
	}

	public EquipmentType getType() {
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

	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
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
	public Set<Identifier> getMonitoredElementIds() {
		// TODO Implement
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * @return <code>Collection&lt;Port&gt;</code>
	 */
	public Set getPorts() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.PORT_CODE),
					true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Level.SEVERE);
			return Collections.EMPTY_SET;
		}
	}
}
