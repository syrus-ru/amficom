/*
 * $Id: TransmissionPath.java,v 1.109.2.1 2006/04/04 09:30:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPath;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPathHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
/**
 * @version $Revision: 1.109.2.1 $, $Date: 2006/04/04 09:30:38 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class TransmissionPath extends DomainMember
		implements MonitoredDomainMember, Characterizable, TypedObject<TransmissionPathType>, ReverseDependencyContainer,
		IdlTransferableObjectExt<IdlTransmissionPath> {
	private static final long serialVersionUID = 8129503678304843903L;

	private TransmissionPathType type;
	private String name;
	private String description;
	private Identifier startPortId;
	private Identifier finishPortId;

	public TransmissionPath(final IdlTransmissionPath tpt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(tpt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	TransmissionPath(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final TransmissionPathType type,
			final Identifier startPortId,
			final Identifier finishPortId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;
	}

	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param startPortId
	 * @param finishPortId
	 * @throws CreateObjectException
	 */
	public static TransmissionPath createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final TransmissionPathType type,
			final Identifier startPortId,
			final Identifier finishPortId) throws CreateObjectException {
		if (creatorId == null
				|| domainId == null
				|| name == null
				|| description == null
				|| type == null
				|| startPortId == null
				|| finishPortId == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final TransmissionPath transmissionPath = new TransmissionPath(IdentifierPool.getGeneratedIdentifier(TRANSMISSIONPATH_CODE),
					creatorId,
					INITIAL_VERSION,
					domainId,
					name,
					description,
					type,
					startPortId,
					finishPortId);

			assert transmissionPath.isValid() : OBJECT_STATE_ILLEGAL;

			transmissionPath.markAsChanged();

			return transmissionPath;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public synchronized void fromIdlTransferable(final IdlTransmissionPath tpt) throws IdlConversionException {
		try {
			super.fromIdlTransferable(tpt, new Identifier(tpt.domainId));

			this.type = (TransmissionPathType) StorableObjectPool.getStorableObject(new Identifier(tpt._typeId), true);

			this.name = tpt.name;
			this.description = tpt.description;
			this.startPortId = new Identifier(tpt.startPortId);
			this.finishPortId = new Identifier(tpt.finishPortId);
		} catch (final ApplicationException ae) {
			throw new IdlConversionException(ae);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTransmissionPath getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlTransmissionPathHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.getDomainId().getIdlTransferable(),
				this.name,
				this.description,
				this.type.getId().getIdlTransferable(),
				this.startPortId.getIdlTransferable(),
				this.finishPortId.getIdlTransferable());
	}

	public String getName() {
		return this.name;
	}

	public TransmissionPathType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Identifier getFinishPortId() {
		return this.finishPortId;
	}

	/**
	 * Returns the ending {@link Port} of this {@link TransmissionPath}.
	 * The {@link Port} is guaranteed to exist.
	 *
	 * @return the ending {@link Port} of this {@link TransmissionPath}.
	 * @throws ApplicationException
	 */
	public Port getFinishPort() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getFinishPortId(), true);
	}

	public Identifier getStartPortId() {
		return this.startPortId;
	}

	/**
	 * Returns the starting {@link Port} of this {@link TransmissionPath}.
	 * The {@link Port} is guaranteed to exist.
	 *
	 * @return the starting {@link Port} of this {@link TransmissionPath}.
	 * @throws ApplicationException
	 */
	public Port getStartPort() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getStartPortId(), true);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final TransmissionPathType type,
			final Identifier startPortId,
			final Identifier finishPortId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		dependencies.add(this.startPortId);
		dependencies.add(this.finishPortId);
		return dependencies;
	}

	/**
	 * @param finishPortId The finishPortId to set.
	 */
	public void setFinishPortId(final Identifier finishPortId) {
		this.finishPortId = finishPortId;
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
	 * @param startPortId The startPortId to set.
	 */
	public void setStartPortId(final Identifier startPortId) {
		this.startPortId = startPortId;
		super.markAsChanged();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(final TransmissionPathType type) {
		this.type = type;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.MonitoredDomainMember#getMonitoredElementIds()
	 */
	public Set<Identifier> getMonitoredElementIds() {
		//TODO Implement
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected TransmissionPathWrapper getWrapper() {
		return TransmissionPathWrapper.getInstance();
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(this.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
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
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic, final boolean usePool) throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(final Characteristic characteristic, final boolean usePool) throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool) throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool) throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics, final boolean usePool) throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
