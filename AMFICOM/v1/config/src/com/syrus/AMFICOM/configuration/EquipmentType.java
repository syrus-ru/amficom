/*
 * $Id: EquipmentType.java,v 1.45 2005/02/14 09:15:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.45 $, $Date: 2005/02/14 09:15:46 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class EquipmentType extends StorableObjectType implements Characterized {
	private static final long serialVersionUID = 9157517478787463967L;

	private String                     name;
	private String                     manufacturer;
	private String                     manufacturerCode;

	private List                       characteristics;

	private StorableObjectDatabase     equipmentTypeDatabase;

	public EquipmentType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new ArrayList();
		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			this.equipmentTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public EquipmentType(EquipmentType_Transferable ett) throws CreateObjectException {
		super(ett.header,
				new String(ett.codename),
				new String(ett.description));
		this.name = ett.name;
		this.manufacturer = ett.manufacturer;
		this.manufacturerCode = ett.manufacturerCode;
		try {
			this.characteristics = new ArrayList(ett.characteristic_ids.length);
			for (int i = 0; i < ett.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(ett.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
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
		this.characteristics = new LinkedList();

		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
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

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new EquipmentType_Transferable(super.getHeaderTransferable(),
										new String(super.codename),
										(super.description != null) ? (new String(super.description)) : "",
										(this.name != null) ? (new String(this.name)) : "",
										(this.manufacturer != null) ? (new String(this.manufacturer)) : "",
										(this.manufacturerCode != null) ? (new String(this.manufacturerCode)) : "",
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

	public List getDependencies() {
		return Collections.EMPTY_LIST;
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

	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final List characteristics) {
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
}
