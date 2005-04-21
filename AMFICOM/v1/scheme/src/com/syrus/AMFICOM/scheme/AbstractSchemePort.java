/*-
 * $Id: AbstractSchemePort.java,v 1.16 2005/04/21 11:25:09 bass Exp $
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

import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemePort}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/04/21 11:25:09 $
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
		throw new UnsupportedOperationException();
	}

	public final Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public final MeasurementPort getMeasurementPort() {
		throw new UnsupportedOperationException();
	}

	public final MeasurementPortType getMeasurementPortType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public final SchemeDevice getParentSchemeDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 */
	public Port getPort() {
		throw new UnsupportedOperationException();
	}

	public final PortType getPortType() {
		throw new UnsupportedOperationException();
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

	public final void setDirectionType(final AbstractSchemePortDirectionType directionType) {
		throw new UnsupportedOperationException();
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
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
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
		throw new UnsupportedOperationException();
	}

	/**
	 * @param measurementPortType
	 * @todo skip invariance checks.
	 */
	public final void setMeasurementPortType(final MeasurementPortType measurementPortType) {
		throw new UnsupportedOperationException();
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

	public final void setParentSchemeDevice(final SchemeDevice parentSchemeDevice) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param port
	 * @todo skip invariance checks.
	 */
	public void setPort(final Port port) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param portType
	 * @todo skip invariance checks.
	 */
	public final void setPortType(final PortType portType) {
		throw new UnsupportedOperationException();
	}
}
