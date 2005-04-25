/*-
 * $Id: AbstractSchemePort.java,v 1.18 2005/04/25 15:07:11 bass Exp $
 * 
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/04/25 15:07:11 $
 * @module scheme_v1
 */
public abstract class AbstractSchemePort extends
		AbstractCloneableStorableObject implements Describable,
		Characterizable {
	private static final long serialVersionUID = 6943625949984422779L;

	private String name;

	private String description;

	private AbstractSchemePortDirectionType directionType;

	/**
	 * Depending on implementation, may reference either
	 * {@link PortType PortType} or {@link PortType CablePortType}.
	 */
	private Identifier portTypeId;

	/**
	 * Depending on implementation, may reference either {@link Port Port}
	 * or {@link Port CablePort}.
	 */
	private Identifier portId;

	private Identifier measurementPortTypeId;

	private Identifier measurementPortId;

	private Identifier parentSchemeDeviceId;

	private Set characteristics;

	/**
	 * @param id
	 */
	AbstractSchemePort(final Identifier id) {
		super(id);
		this.characteristics = new HashSet();
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
	 * @param measurementPortType
	 * @param measurementPort
	 * @param parentSchemeDevice
	 */
	AbstractSchemePort(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final AbstractSchemePortDirectionType directionType,
			final PortType portType, final Port port,
			final MeasurementPortType measurementPortType,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.directionType = directionType;

		assert portType == null || port == null;
		this.portTypeId = Identifier.possiblyVoid(portType);
		this.portId = Identifier.possiblyVoid(port);

		assert measurementPortType == null || measurementPort == null;
		this.measurementPortTypeId = Identifier.possiblyVoid(measurementPortType);
		this.measurementPortId = Identifier.possiblyVoid(measurementPort);

		this.parentSchemeDeviceId = Identifier.possiblyVoid(parentSchemeDevice);

		this.characteristics = new HashSet();
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
		this.changed = true;
	}

	public abstract AbstractSchemeLink getAbstractSchemeLink();

	public final AbstractSchemePortDirectionType getDirectionType() {
		assert this.directionType != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.directionType;
	}

	public final Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public final Set getDependencies() {
		assert this.portTypeId != null && this.portId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.portTypeId.isVoid() ^ this.portId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert this.measurementPortTypeId != null && this.measurementPortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.measurementPortTypeId.isVoid() ^ this.measurementPortId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert this.parentSchemeDeviceId != null && !this.parentSchemeDeviceId.isVoid(): ErrorMessages.OBJECT_NOT_INITIALIZED;

		final Set dependencies = new HashSet();
		dependencies.add(this.portTypeId);
		dependencies.add(this.portId);
		dependencies.add(this.measurementPortTypeId);
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
		assert this.measurementPortId != null && this.measurementPortTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.measurementPortId.isVoid() ^ this.measurementPortTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		if (this.measurementPortId.isVoid())
			return null;

		try {
			return (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(this.measurementPortId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public final MeasurementPortType getMeasurementPortType() {
		assert this.measurementPortId != null && this.measurementPortTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.measurementPortId.isVoid() ^ this.measurementPortTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		if (!this.measurementPortId.isVoid())
			return (MeasurementPortType) getMeasurementPort().getType();

		try {
			return (MeasurementPortType) ConfigurationStorableObjectPool.getStorableObject(this.measurementPortTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
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
			return (SchemeDevice) SchemeStorableObjectPool.getStorableObject(this.parentSchemeDeviceId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * Overridden by descendants to add extra checks.
	 */
	public Port getPort() {
		assert this.portId != null && this.portTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.portId.isVoid() ^ this.portTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		if (this.portId.isVoid())
			return null;

		try {
			return (Port) ConfigurationStorableObjectPool.getStorableObject(this.portId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public final PortType getPortType() {
		assert this.portId != null && this.portTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.portId.isVoid() ^ this.portTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		if (!this.portId.isVoid())
			return (PortType) getPort().getType();

		try {
			return (PortType) ConfigurationStorableObjectPool.getStorableObject(this.portTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public final void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		this.changed = true;
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
	 * @param measurementPortTypeId
	 * @param measurementPortId
	 * @param parentSchemeDeviceId
	 */
	final synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final AbstractSchemePortDirectionType directionType,
			final Identifier portTypeId, final Identifier portId,
			final Identifier measurementPortTypeId,
			final Identifier measurementPortId,
			final Identifier parentSchemeDeviceId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert directionType != null: ErrorMessages.NON_NULL_EXPECTED;

		assert portTypeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert portId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert portTypeId.isVoid() ^ portId.isVoid();

		assert measurementPortTypeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert measurementPortId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert measurementPortTypeId.isVoid() ^ measurementPortId.isVoid();

		assert parentSchemeDeviceId != null && !parentSchemeDeviceId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;

		this.name = name;
		this.description = description;
		this.directionType = directionType;
		this.portTypeId = portTypeId;
		this.portId = portId;
		this.measurementPortTypeId = measurementPortTypeId;
		this.measurementPortId = measurementPortId;
		this.parentSchemeDeviceId = parentSchemeDeviceId;
	}

	/**
	 * @param directionType
	 */
	public final void setDirectionType(final AbstractSchemePortDirectionType directionType) {
		assert this.directionType != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert directionType != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.directionType.value() == directionType.value())
			return;
		this.directionType = directionType;
		this.changed = true;
	}

	public final void setCharacteristics(final Set characteristics) {
		setCharacteristics0(characteristics);
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Set)
	 */
	public final void setCharacteristics0(final Set characteristics) {
		assert characteristics != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.characteristics == null)
			this.characteristics = new HashSet(characteristics.size());
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
		this.changed = true;
	}

	/**
	 * @param measurementPort
	 * @todo skip invariance checks.
	 */
	public final void setMeasurementPort(final MeasurementPort measurementPort) {
		assert this.measurementPortId != null && this.measurementPortTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.measurementPortId.isVoid() ^ this.measurementPortTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newMeasurementPortId = Identifier.possiblyVoid(measurementPort);
		if (this.measurementPortId.equals(newMeasurementPortId)) {
			Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
			return;
		}

		if (this.measurementPortId.isVoid())
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.measurementPortTypeId = Identifier.VOID_IDENTIFIER;
		else if (newMeasurementPortId.isVoid())
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve). 
			 */
			this.measurementPortTypeId = this.getMeasurementPort().getType().getId();
		this.measurementPortId = newMeasurementPortId;
		this.changed = true;
	}

	/**
	 * @param measurementPortType
	 * @todo skip invariance checks.
	 */
	public final void setMeasurementPortType(final MeasurementPortType measurementPortType) {
		assert this.measurementPortId != null && this.measurementPortTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.measurementPortId.isVoid() ^ this.measurementPortTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		assert measurementPortType != null: ErrorMessages.NON_NULL_EXPECTED;

		if (!this.measurementPortId.isVoid())
			this.getMeasurementPort().setType(measurementPortType);
		else {
			final Identifier newMeasurementPortTypeId = measurementPortType.getId();
			if (this.measurementPortTypeId.equals(newMeasurementPortTypeId)) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			this.measurementPortTypeId = newMeasurementPortTypeId;
			this.changed = true;
		}
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
		this.changed = true;
	}

	/**
	 * @param parentSchemeDevice
	 */
	public final void setParentSchemeDevice(final SchemeDevice parentSchemeDevice) {
		assert this.parentSchemeDeviceId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeDevice == null) {
			Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
			SchemeStorableObjectPool.delete(this.id);
			return;
		}
		final Identifier newParentSchemeDeviceId = parentSchemeDevice.getId();
		if (this.parentSchemeDeviceId.equals(newParentSchemeDeviceId))
			return;
		this.parentSchemeDeviceId = newParentSchemeDeviceId;
		this.changed = true;
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param port
	 * @todo skip invariance checks.
	 */
	public void setPort(final Port port) {
		assert this.portId != null && this.portTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.portId.isVoid() ^ this.portTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newPortId = Identifier.possiblyVoid(port);
		if (this.portId.equals(newPortId)) {
			Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
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
		this.changed = true;
	}

	/**
	 * @param portType
	 * @todo skip invariance checks.
	 */
	public final void setPortType(final PortType portType) {
		assert this.portId != null && this.portTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.portId.isVoid() ^ this.portTypeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		assert portType != null: ErrorMessages.NON_NULL_EXPECTED;

		if (!this.portId.isVoid())
			this.getPort().setType(portType);
		else {
			final Identifier newPortTypeId = portType.getId();
			if (this.portTypeId.equals(newPortTypeId)) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			this.portTypeId = newPortTypeId;
			this.changed = true;
		}
	}
}
