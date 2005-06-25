/*
 * $Id: MeasurementPort.java,v 1.64 2005/06/25 17:07:54 bass Exp $
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

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPort;
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.64 $, $Date: 2005/06/25 17:07:54 $
 * @author $Author: bass $
 * @module config_v1
 */
public final class MeasurementPort extends StorableObject implements Characterizable, TypedObject {
	private static final long serialVersionUID = -5100885507408715167L;

	private MeasurementPortType type;

	private String name;
	private String description;

	private Identifier kisId;
	private Identifier portId;

	private Set<Characteristic> characteristics;

	MeasurementPort(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();

		final MeasurementPortDatabase database = (MeasurementPortDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPort(final IdlMeasurementPort mpt) throws CreateObjectException {
		try {
			this.fromTransferable(mpt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	MeasurementPort(final Identifier id,
			final Identifier creatorId,
			final long version,
			final MeasurementPortType type,
			final String name,
			final String description,	
			final Identifier kisId,
			final Identifier portId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
		this.characteristics = new HashSet<Characteristic>();
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param name
	 * @param description
	 * @param kisId
	 * @param portId
	 * @throws CreateObjectException
	 */
	public static MeasurementPort createInstance(final Identifier creatorId,
			final MeasurementPortType type,
			final String name,
			final String description,	
			final Identifier kisId,
			final Identifier portId) throws CreateObjectException{
		if (creatorId == null || type == null || name == null || description == null ||
				kisId == null || portId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final MeasurementPort measurementPort = new MeasurementPort(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORT_CODE),
					creatorId,
					0L,
					type,
					name,
					description,
					kisId,
					portId);

			assert measurementPort.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementPort.markAsChanged();

			return measurementPort;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlMeasurementPort mpt = (IdlMeasurementPort) transferable;
		super.fromTransferable(mpt.header);

		this.type = (MeasurementPortType) StorableObjectPool.getStorableObject(new Identifier(mpt._typeId), true);

		this.name = mpt.name;
		this.description = mpt.description;

		this.kisId = new Identifier(mpt.kisId);
		this.portId = new Identifier(mpt.portId);

		final Set characteristicIds = Identifier.fromTransferables(mpt.characteristicIds);
		this.characteristics = new HashSet<Characteristic>(mpt.characteristicIds.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMeasurementPort getTransferable(final ORB orb) {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return new IdlMeasurementPort(super.getHeaderTransferable(orb),
				this.type.getId().getTransferable(),
				this.name,
				this.description,
				this.kisId.getTransferable(),
				this.portId.getTransferable(),
				charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}
	
	public String getName() {
		return this.name;
	}
	
	public Identifier getKISId() {
		return this.kisId;
	}

	public Identifier getPortId() {
		return this.portId;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final MeasurementPortType type,
			final String name,
			final String description,	
			final Identifier kisId,
			final Identifier portId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
	}

	public Set<Identifiable> getDependencies() {
		Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		dependencies.add(this.kisId);
		dependencies.add(this.portId);
		return dependencies;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	
	public void setType(final MeasurementPortType type) {
		this.type = type;
		super.markAsChanged();
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

	/**
	 * @param kisId The kisId to set.
	 */
	public void setKISId(Identifier kisId) {
		this.kisId = kisId;
		super.markAsChanged();
	}
	/**
	 * @param portId The portId to set.
	 */
	public void setPortId(Identifier portId) {
		this.portId = portId;
		super.markAsChanged();
	}
}
