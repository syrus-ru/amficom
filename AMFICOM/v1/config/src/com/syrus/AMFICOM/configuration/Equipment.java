/*
 * $Id: Equipment.java,v 1.74 2005/03/23 18:02:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.74 $, $Date: 2005/03/23 18:02:27 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class Equipment extends DomainMember implements MonitoredDomainMember, Characterizable, TypedObject {

	private static final long serialVersionUID = -6115401698444070841L;

	private EquipmentType          type;
	private String                 name;
	private String                 description;
	private Identifier             imageId;
	private float                  longitude;
	private float                  latitude;
	private String                 supplier;
	private String                 supplierCode;
	private String                 hwSerial;
	private String                 hwVersion;
	private String                 swSerial;
	private String                 swVersion;
	private String                 inventoryNumber;

	private Collection characteristics;

	private StorableObjectDatabase equipmentDatabase;

	public Equipment(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new ArrayList();

		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
		try {
			this.equipmentDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Equipment(Equipment_Transferable et) throws CreateObjectException {
		super(et.header,
				new Identifier(et.domain_id));

		try {
			this.type = (EquipmentType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.name = new String(et.name);
		this.description = new String(et.description);
		this.imageId = new Identifier(et.image_id);
		this.supplier = new String(et.supplier);
		this.supplier = new String(et.supplierCode);
		this.longitude = et.longitude;
		this.latitude = et.latitude;
		this.hwSerial = et.hwSerial;
		this.hwVersion = et.hwVersion;
		this.swSerial = et.swSerial;
		this.swVersion = et.swVersion;
		this.inventoryNumber = et.inventoryNumber;

		try {
			this.characteristics = new ArrayList(et.characteristic_ids.length);
			for (int i = 0; i < et.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(et.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
	}

	protected Equipment(Identifier id,
			Identifier creatorId,
			long version,
			Identifier domainId,
			EquipmentType type,
			String name,
			String description,
			Identifier imageId,
			String supplier,
			String supplierCode,
			float longitude,
			float latitude,
			String hwSerial,
			String hwVersion,
			String swSerial,
			String swVersion,
			String inventoryNumber) {
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

		this.characteristics = new ArrayList();

		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
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
	public static Equipment createInstance(Identifier creatorId,
			Identifier domainId,
			EquipmentType type,
			String name,
			String description,
			Identifier imageId,
			String supplier,
			String supplierCode,
			float longitude,
			float latitude,
			String hwSerial,
			String hwVersion,
			String swSerial,
			String swVersion,
			String inventoryNumber) throws CreateObjectException {
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
			Equipment equipment = new Equipment(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENT_ENTITY_CODE),
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
			equipment.changed = true;
			return equipment;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Equipment.createInstance | cannot generate identifier ", e);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new Equipment_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				this.name != null ? new String(this.name) : "",
				this.description != null ? new String(this.description) : "",
				this.supplier != null ? new String(this.supplier) : "",
				this.supplierCode != null ? new String(this.supplierCode) : "",
				this.longitude,
				this.latitude,
				this.hwSerial != null ? new String(this.hwSerial) : "",
				this.hwVersion != null ? new String(this.hwVersion) : "",
				this.swSerial != null ? new String(this.swSerial) : "",
				this.swVersion != null ? new String(this.swVersion) : "",
				this.inventoryNumber != null ? new String(this.inventoryNumber) : "",
				(Identifier_Transferable) this.imageId.getTransferable(),
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

	public void setDescription(String description) {
		this.description = description;
		super.changed = true;
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.changed = true;
		}
	}

	public Collection getCharacteristics() {
		return Collections.unmodifiableCollection(this.characteristics);
	}

	public void setCharacteristics0(final Collection characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Collection characteristics) {
		this.setCharacteristics0(characteristics);
		super.changed = true;
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			Identifier domainId,
			EquipmentType type,
			String name,
			String description,
			Identifier imageId,
			String supplier,
			String supplierCode,
			float longitude,
			float latitude,
			String hwSerial,
			String hwVersion,
			String swSerial,
			String swVersion,
			String inventoryNumber) {
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

	public List getDependencies() {
		return Collections.EMPTY_LIST;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
		super.changed = true;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
		super.changed = true;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
		super.changed = true;
	}

	public String getHwSerial() {
		return this.hwSerial;
	}

	public void setHwSerial(String hwSerial) {
		this.hwSerial = hwSerial;
		super.changed = true;
	}

	public String getHwVersion() {
		return this.hwVersion;
	}

	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
		super.changed = true;
	}

	public String getInventoryNumber() {
		return this.inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
		super.changed = true;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
		super.changed = true;
	}

	public String getSwSerial() {
		return this.swSerial;
	}

	public void setSwSerial(String swSerial) {
		this.swSerial = swSerial;
		super.changed = true;
	}

	public String getSwVersion() {
		return this.swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
		super.changed = true;
	}	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(EquipmentType type) {
		this.type = type;
		super.changed = true;
	}
	/**
	 * @param imageId The imageId to set.
	 */
	public void setImageId(Identifier imageId) {
		this.imageId = imageId;
		super.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.MonitoredDomainMember#getMonitoredElementIds()
	 */
	public Collection getMonitoredElementIds() {
		// TODO Implement
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT;
	}

	/**
	 * @return <code>Collection&lt;Port&gt;</code>
	 */
	public Collection getPorts() {
		try {
			return ConfigurationStorableObjectPool
					.getStorableObjectsByCondition(
							new LinkedIdsCondition(
									this.id,
									ObjectEntities.PORT_ENTITY_CODE),
							true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_LIST;
		}
	}
}
