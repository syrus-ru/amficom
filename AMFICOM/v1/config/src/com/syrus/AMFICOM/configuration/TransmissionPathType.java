/*
 * $Id: TransmissionPathType.java,v 1.35 2005/04/01 11:02:30 bass Exp $
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

import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.35 $, $Date: 2005/04/01 11:02:30 $
 * @author $Author: bass $
 * @module config_v1
 */

public class TransmissionPathType extends StorableObjectType implements Characterizable {

	private static final long		serialVersionUID	= 5311725679846973948L;

	private String					name;

	private Set					characteristics;

	private StorableObjectDatabase	transmissionPathTypeDatabase;

	public TransmissionPathType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.characteristics = new HashSet();
		this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
		try {
			this.transmissionPathTypeDatabase.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public TransmissionPathType(TransmissionPathType_Transferable tptt) throws CreateObjectException {
		super(tptt.header, new String(tptt.codename), new String(tptt.description));
		this.name = tptt.name;
		try {
			this.characteristics = new HashSet(tptt.characteristic_ids.length);
			for (int i = 0; i < tptt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(
					new Identifier(tptt.characteristic_ids[i]), true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
	}

	protected TransmissionPathType(Identifier id,
			Identifier creatorId,
			long version,
			String codename,
			String description,
			String name) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
				version, codename, description);
		this.name = name;
		this.characteristics = new HashSet();

		this.transmissionPathTypeDatabase = ConfigurationDatabaseContext.getTransmissionPathTypeDatabase();
	}

	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static TransmissionPathType createInstance(	Identifier creatorId,
														String codename,
														String description,
														String name) throws CreateObjectException {
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			TransmissionPathType transmissionPathType = new TransmissionPathType(IdentifierPool
					.getGeneratedIdentifier(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE), creatorId, 0L, codename,
																					description, name);
			transmissionPathType.changed = true;
			return transmissionPathType;
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TransmissionPathType.createInstance | cannot generate identifier ", e);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new TransmissionPathType_Transferable(super.getHeaderTransferable(), new String(super.codename),
														(super.description != null) ? (new String(super.description))
																: "", (this.name != null) ? (new String(this.name))
																: "", charIds);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		super.changed = true;
		this.name = name;
	}

	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												String codename,
												String description,
												String name) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
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

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATHTYPE;
	}
}
