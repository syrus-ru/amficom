/*
 * $Id: TransmissionPathType.java,v 1.52 2005/06/17 12:32:20 bass Exp $
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.52 $, $Date: 2005/06/17 12:32:20 $
 * @author $Author: bass $
 * @module config_v1
 */

public class TransmissionPathType extends StorableObjectType implements Characterizable, Namable {

	private static final long serialVersionUID = 5311725679846973948L;

	private String name;

	private Set characteristics;

	TransmissionPathType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.TRANSPATH_TYPE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	TransmissionPathType(final TransmissionPathType_Transferable tptt) throws CreateObjectException {
		try {
			this.fromTransferable(tptt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	TransmissionPathType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final String name) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
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
	public static TransmissionPathType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name) throws CreateObjectException {
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			TransmissionPathType transmissionPathType = new TransmissionPathType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TRANSPATH_TYPE_CODE),
					creatorId,
					0L,
					codename,
					description,
					name);

			assert transmissionPathType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			transmissionPathType.markAsChanged();

			return transmissionPathType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		TransmissionPathType_Transferable tptt = (TransmissionPathType_Transferable) transferable;
		super.fromTransferable(tptt.header, tptt.codename, tptt.description);
		this.name = tptt.name;

		Set characteristicIds = Identifier.fromTransferables(tptt.characteristic_ids);
		this.characteristics = new HashSet(tptt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		final Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);

		return new TransmissionPathType_Transferable(super.getHeaderTransferable(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				charIds);
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final 	Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
	}

	public Set getDependencies() {
		return Collections.EMPTY_SET;
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
}
