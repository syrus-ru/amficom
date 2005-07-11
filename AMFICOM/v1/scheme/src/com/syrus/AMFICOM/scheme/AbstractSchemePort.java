/*-
 * $Id: AbstractSchemePort.java,v 1.33 2005/07/11 08:19:02 bass Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.33 $, $Date: 2005/07/11 08:19:02 $
 * @module scheme_v1
 */
public abstract class AbstractSchemePort extends
		AbstractCloneableStorableObject implements Describable,
		Characterizable {
	private static final long serialVersionUID = 6943625949984422779L;

	private String name;

	private String description;

	private DirectionType directionType;

	/**
	 * Depending on implementation, may reference either
	 * {@link PortType PortType} or {@link PortType CablePortType}.
	 */
	Identifier portTypeId;

	/**
	 * Depending on implementation, may reference either {@link Port Port}
	 * or {@link Port CablePort}.
	 */
	Identifier portId;

	Identifier measurementPortId;

	Identifier parentSchemeDeviceId;

	private Set<Characteristic> characteristics;

	boolean portTypeSet = false;

	/**
	 * @param id
	 */
	AbstractSchemePort(final Identifier id) {
		super(id);
		this.characteristics = new HashSet<Characteristic>();
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param directionType
	 * @param portType
	 * @param port
	 * @param measurementPort
	 * @param parentSchemeDevice
	 */
	AbstractSchemePort(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final DirectionType directionType,
			final PortType portType, final Port port,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.directionType = directionType;

		assert portType == null || port == null;
		this.portTypeId = Identifier.possiblyVoid(portType);
		this.portId = Identifier.possiblyVoid(port);

		this.measurementPortId = Identifier.possiblyVoid(measurementPort);
		this.parentSchemeDeviceId = Identifier.possiblyVoid(parentSchemeDevice);

		this.characteristics = new HashSet<Characteristic>();
	}

	/**
	 * Will transmute to the constructor from the corresponding
	 * transferable.
	 */
	AbstractSchemePort() {
		// super();
	}

	public final void addCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		this.characteristics.add(characteristic);
		super.markAsChanged();
	}

	public abstract AbstractSchemeLink getAbstractSchemeLink();

	public final DirectionType getDirectionType() {
		assert this.directionType != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.directionType;
	}

	public final Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public final Set<Identifiable> getDependencies() {
		assert this.portTypeId != null && this.portId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.portTypeId.isVoid() ^ this.portId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert this.measurementPortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeDeviceId != null && !this.parentSchemeDeviceId.isVoid(): ErrorMessages.OBJECT_NOT_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.portTypeId);
		dependencies.add(this.portId);
		dependencies.add(this.measurementPortId);
		dependencies.add(this.parentSchemeDeviceId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public final MeasurementPort getMeasurementPort() {
		assert this.measurementPortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (MeasurementPort) StorableObjectPool.getStorableObject(this.measurementPortId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Level.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public final SchemeDevice getParentSchemeDevice() {
		assert this.parentSchemeDeviceId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		
		try {
			return (SchemeDevice) StorableObjectPool.getStorableObject(this.parentSchemeDeviceId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Level.SEVERE);
			return null;
		}
	}

	/**
	 * Overridden by descendants to add extra checks.
	 */
	public Port getPort() {
		assert this.assertPortTypeSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		try {
			return (Port) StorableObjectPool.getStorableObject(this.portId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Level.SEVERE);
			return null;
		}
	}

	public final PortType getPortType() {
		assert this.assertPortTypeSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		if (!this.portId.isVoid())
			return getPort().getType();

		try {
			return (PortType) StorableObjectPool.getStorableObject(this.portTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Level.SEVERE);
			return null;
		}
	}

	public final void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		super.markAsChanged();
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param directionType
	 * @param portTypeId
	 * @param portId
	 * @param measurementPortId
	 * @param parentSchemeDeviceId
	 */
	final synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final DirectionType directionType,
			final Identifier portTypeId, final Identifier portId,
			final Identifier measurementPortId,
			final Identifier parentSchemeDeviceId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert directionType != null: ErrorMessages.NON_NULL_EXPECTED;

		assert portTypeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert portId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert portTypeId.isVoid() ^ portId.isVoid();

		assert measurementPortId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeDeviceId != null && !parentSchemeDeviceId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;

		this.name = name;
		this.description = description;
		this.directionType = directionType;
		this.portTypeId = portTypeId;
		this.portId = portId;
		this.measurementPortId = measurementPortId;
		this.parentSchemeDeviceId = parentSchemeDeviceId;
	}

	/**
	 * @param directionType
	 */
	public final void setDirectionType(final DirectionType directionType) {
		assert this.directionType != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert directionType != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.directionType.value() == directionType.value())
			return;
		this.directionType = directionType;
		super.markAsChanged();
	}

	public final void setCharacteristics(final Set<Characteristic> characteristics) {
		setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Set)
	 */
	public final void setCharacteristics0(final Set<Characteristic> characteristics) {
		assert characteristics != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.characteristics == null)
			this.characteristics = new HashSet<Characteristic>(characteristics.size());
		else
			this.characteristics.clear();
		this.characteristics.addAll(characteristics);
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @param measurementPort
	 */
	public final void setMeasurementPort(final MeasurementPort measurementPort) {
		final Identifier newMeasurementPortId = Identifier.possiblyVoid(measurementPort);
		if (this.measurementPortId.equals(newMeasurementPortId))
			return;
		this.measurementPortId = newMeasurementPortId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @param parentSchemeDevice
	 */
	public final void setParentSchemeDevice(final SchemeDevice parentSchemeDevice) {
		assert this.parentSchemeDeviceId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeDevice == null) {
			Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Level.WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		final Identifier newParentSchemeDeviceId = parentSchemeDevice.getId();
		if (this.parentSchemeDeviceId.equals(newParentSchemeDeviceId))
			return;
		this.parentSchemeDeviceId = newParentSchemeDeviceId;
		super.markAsChanged();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param port
	 */
	public void setPort(final Port port) {
		assert this.assertPortTypeSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		final Identifier newPortId = Identifier.possiblyVoid(port);
		if (this.portId.equals(newPortId)) {
			Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Level.INFO);
			return;
		}

		if (this.portId.isVoid())
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.portTypeId = Identifier.VOID_IDENTIFIER;
		else if (newPortId.isVoid())
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve).
			 */
			this.portTypeId = this.getPort().getType().getId();
		this.portId = newPortId;
		super.markAsChanged();
	}

	/**
	 * @param portType
	 */
	public final void setPortType(final PortType portType) {
		assert this.assertPortTypeSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert portType != null: ErrorMessages.NON_NULL_EXPECTED;

		if (!this.portId.isVoid())
			this.getPort().setType(portType);
		else {
			final Identifier newPortTypeId = portType.getId();
			if (this.portTypeId.equals(newPortTypeId)) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Level.INFO);
				return;
			}
			this.portTypeId = newPortTypeId;
			super.markAsChanged();
		}
	}

	/**
	 * @param header
	 * @param name1
	 * @param description1
	 * @param directionType1
	 * @param portTypeId1
	 * @param portId1
	 * @param measurementPortId1
	 * @param parentSchemeDeviceId1
	 * @param characteristicIds
	 * @throws CreateObjectException
	 */
	void fromTransferable(final IdlStorableObject header,
			final String name1, final String description1,
			final DirectionType directionType1,
			final IdlIdentifier portTypeId1,
			final IdlIdentifier portId1,
			final IdlIdentifier measurementPortId1,
			final IdlIdentifier parentSchemeDeviceId1,
			final IdlIdentifier characteristicIds[])
			throws CreateObjectException {
		try {
			super.fromTransferable(header);
			final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjects(Identifier.fromTransferables(characteristicIds), true);
			this.setCharacteristics0(characteristics0);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = name1;
		this.description = description1;
		this.directionType = directionType1;
		this.portTypeId = new Identifier(portTypeId1);
		this.portId = new Identifier(portId1);
		this.measurementPortId = new Identifier(measurementPortId1);
		this.parentSchemeDeviceId = new Identifier(parentSchemeDeviceId1);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertPortTypeSetNonStrict() {
		if (this.portTypeSet)
			return this.assertPortTypeSetStrict();
		this.portTypeSet = true;
		return this.portId != null
				&& this.portTypeId != null
				&& this.portId.isVoid()
				&& this.portTypeId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertPortTypeSetStrict() {
		return this.portId != null
				&& this.portTypeId != null
				&& (this.portId.isVoid() ^ this.portTypeId.isVoid());
	}
}
