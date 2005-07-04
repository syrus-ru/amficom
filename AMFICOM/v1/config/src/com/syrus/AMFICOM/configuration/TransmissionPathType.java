/*
 * $Id: TransmissionPathType.java,v 1.59 2005/07/04 13:00:53 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPathType;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPathTypeHelper;
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.59 $, $Date: 2005/07/04 13:00:53 $
 * @author $Author: bass $
 * @module config_v1
 */

public final class TransmissionPathType extends StorableObjectType implements Characterizable, Namable {

	private static final long serialVersionUID = 5311725679846973948L;

	private String name;

	private Set<Characteristic> characteristics;

	TransmissionPathType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();

		final TransmissionPathTypeDatabase database = (TransmissionPathTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.TRANSPATH_TYPE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	TransmissionPathType(final IdlTransmissionPathType tptt) throws CreateObjectException {
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
		this.characteristics = new HashSet<Characteristic>();
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

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlTransmissionPathType tptt = (IdlTransmissionPathType) transferable;
		super.fromTransferable(tptt, tptt.codename, tptt.description);
		this.name = tptt.name;

		final Set<Identifier> characteristicIds = Identifier.fromTransferables(tptt.characteristicIds);
		this.characteristics = new HashSet<Characteristic>(tptt.characteristicIds.length);
		final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjects(characteristicIds, true);
		this.setCharacteristics0(characteristics0);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTransmissionPathType getTransferable(final ORB orb) {
		final IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return IdlTransmissionPathTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version,
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

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
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

	public Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set<Characteristic> characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set<Characteristic> characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}
}
