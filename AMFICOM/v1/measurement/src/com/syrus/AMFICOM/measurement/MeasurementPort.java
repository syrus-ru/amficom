/*
 * $Id: MeasurementPort.java,v 1.22.2.1 2006/05/18 17:50:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPort;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPortHelper;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.22.2.1 $, $Date: 2006/05/18 17:50:00 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementPort extends StorableObject
		implements Characterizable,
		TypedObject<MeasurementPortType>,
		IdlTransferableObjectExt<IdlMeasurementPort> {
	private static final long serialVersionUID = -5100885507408715167L;

	private MeasurementPortType type;

	private String name;
	private String description;

	private Identifier kisId;
	private Identifier portId;

	public MeasurementPort(final IdlMeasurementPort mpt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(mpt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	MeasurementPort(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
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
					StorableObjectVersion.INITIAL_VERSION,
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

	public synchronized void fromIdlTransferable(final IdlMeasurementPort mpt)
	throws IdlConversionException {
		try {
			super.fromIdlTransferable(mpt);
	
			this.type = (MeasurementPortType) StorableObjectPool.getStorableObject(new Identifier(mpt._typeId), true);
	
			this.name = mpt.name;
			this.description = mpt.description;
	
			this.kisId = new Identifier(mpt.kisId);
			this.portId = new Identifier(mpt.portId);
		} catch (final ApplicationException ae) {
			throw new IdlConversionException(ae);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMeasurementPort getIdlTransferable(final ORB orb) {
		return IdlMeasurementPortHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.type.getId().getIdlTransferable(),
				this.name,
				this.description,
				this.kisId.getIdlTransferable(),
				this.portId.getIdlTransferable());
	}

	public MeasurementPortType getType() {
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

	/**
	 * Returns the {@link Port} view of this {@link MeasurementPort}. In
	 * rare cases, this view may be {@code null}.
	 * 
	 * @return the {@link Port} view of this {@link MeasurementPort}.
	 * @throws ApplicationException
	 */
	public Port getPort() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getPortId(), true);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
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

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MeasurementPortWrapper getWrapper() {
		return MeasurementPortWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this);
	}

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this);
	}

	/**
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set<Characteristic> getCharacteristics()
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0());
	}

	/**
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0()
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees();
	}

	/**
	 * @param characteristics
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0();

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic);
		}
	}
}
