/*
 * $Id: EquipmentType.java,v 1.55 2005/04/12 14:52:46 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
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
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.55 $, $Date: 2005/04/12 14:52:46 $
 * @author $Author: bob $
 * @module config_v1
 */

public class EquipmentType extends StorableObjectType implements Characterizable {
	private static final long serialVersionUID = 9157517478787463967L;

	private String name;
	private String manufacturer;
	private String manufacturerCode;

	private Set characteristics;

	public EquipmentType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		EquipmentTypeDatabase database = ConfigurationDatabaseContext.getEquipmentTypeDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public EquipmentType(EquipmentType_Transferable ett) throws CreateObjectException {
		try {
			this.fromTransferable(ett);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected EquipmentType(Identifier id,
							Identifier creatorId,
							long version,
							String codename,
							String description,
							String name,
							String manufacturer,
							String manufacturerCode) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.characteristics = new HashSet();
	}


	/**
	 * Create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static EquipmentType createInstance(Identifier creatorId,
												 String codename,
												 String description,
												 String name,
												 String manufacturer,
												 String manufacturerCode) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null || name == null
								|| manufacturer == null || manufacturerCode == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			EquipmentType equipmentType = new EquipmentType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE),
										creatorId,
										0L,
										codename,
										description,
										name,
										manufacturer,
										manufacturerCode);
			equipmentType.changed = true;
			return equipmentType;
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EquipmentType.createInstance | cannot generate identifier ", ioee);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		EquipmentType_Transferable ett = (EquipmentType_Transferable) transferable;
		super.fromTransferable(ett.header, ett.codename, ett.description);
		this.name = ett.name;
		this.manufacturer = ett.manufacturer;
		this.manufacturerCode = ett.manufacturerCode;

		Set characteristicIds = Identifier.fromTransferables(ett.characteristic_ids);
		this.characteristics = new HashSet(ett.characteristic_ids.length);
		this.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new EquipmentType_Transferable(super.getHeaderTransferable(),
										super.codename,
										super.description != null ? super.description : "",
										this.name != null ? this.name : "",
										this.manufacturer != null ? this.manufacturer : "",
										this.manufacturerCode != null ? this.manufacturerCode : "",
										charIds);
	}

	protected synchronized void setAttributes(Date created,
											Date modified,
											Identifier creatorId,
											Identifier modifierId,
											long version,
											String codename,
											String description,
											String name,
											String manufacturer,
											String manufacturerCode) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							codename,
							description);
		this.name = name;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		super.changed = true;
		this.name = name;
	}

	public Set getDependencies() {
		return Collections.EMPTY_SET;
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
		super.changed = true;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
		super.changed = true;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		super.changed = true;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE;
	}
}
