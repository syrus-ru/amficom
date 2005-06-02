/*
 * $Id: TransmissionPathType.java,v 1.49 2005/06/02 14:27:03 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable;
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
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.49 $, $Date: 2005/06/02 14:27:03 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class TransmissionPathType extends StorableObjectType implements Characterizable, Namable {

	private static final long serialVersionUID = 5311725679846973948L;

	private String name;

	private Set characteristics;

	TransmissionPathType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	TransmissionPathType(TransmissionPathType_Transferable tptt) throws CreateObjectException {
		try {
			this.fromTransferable(tptt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	TransmissionPathType(Identifier id,
			Identifier creatorId,
			long version,
			String codename,
			String description,
			String name) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId,
				version, codename, description);
		this.name = name;
		this.characteristics = new HashSet();
	}

	/**
	 * create new instance for client
	 *
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static TransmissionPathType createInstance(Identifier creatorId, String codename, String description, String name)
			throws CreateObjectException {
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			TransmissionPathType transmissionPathType = new TransmissionPathType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					name);

			assert transmissionPathType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			transmissionPathType.changed = true;
			try {
				StorableObjectPool.putStorableObject(transmissionPathType);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

			return transmissionPathType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		TransmissionPathType_Transferable tptt = (TransmissionPathType_Transferable) transferable;
		super.fromTransferable(tptt.header, tptt.codename, tptt.description);
		this.name = tptt.name;

		Set characteristicIds = Identifier.fromTransferables(tptt.characteristic_ids);
		this.characteristics = new HashSet(tptt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new TransmissionPathType_Transferable(super.getHeaderTransferable(), super.codename,
														super.description != null ? super.description
																: "", this.name != null ? this.name
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
}
