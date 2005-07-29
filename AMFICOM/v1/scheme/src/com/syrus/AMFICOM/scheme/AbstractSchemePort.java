/*-
 * $Id: AbstractSchemePort.java,v 1.45 2005/07/29 13:07:00 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CloneableStorableObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.45 $, $Date: 2005/07/29 13:07:00 $
 * @module scheme
 */
public abstract class AbstractSchemePort
		extends StorableObject
		implements Describable, Characterizable, CloneableStorableObject {
	private static final long serialVersionUID = 6943625949984422779L;

	private String name;

	private String description;

	private IdlDirectionType directionType;

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

	transient boolean portTypeSet = false;

	transient Map<Identifier, Identifier> clonedIdMap;

	/**
	 * @param id
	 */
	AbstractSchemePort(final Identifier id) {
		super(id);
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
	AbstractSchemePort(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final IdlDirectionType directionType,
			final PortType portType,
			final Port port,
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
	}

	/**
	 * Will transmute to the constructor from the corresponding
	 * transferable.
	 */
	AbstractSchemePort() {
		// super();
	}

	public abstract AbstractSchemeLink getAbstractSchemeLink();

	public final IdlDirectionType getDirectionType() {
		assert this.directionType != null: OBJECT_NOT_INITIALIZED;
		return this.directionType;
	}

	/**
	 * @see Characterizable#getCharacteristics()
	 */
	public final Set<Characteristic> getCharacteristics() {
		try {
			return Collections.unmodifiableSet(this.getCharacteristics0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<Characteristic> getCharacteristics0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, CHARACTERISTIC_CODE), true);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public final Set<Identifiable> getDependencies() {
		assert this.portTypeId != null && this.portId != null: OBJECT_NOT_INITIALIZED;
		assert this.portTypeId.isVoid() ^ this.portId.isVoid(): OBJECT_BADLY_INITIALIZED;
		assert this.measurementPortId != null: OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeDeviceId != null && !this.parentSchemeDeviceId.isVoid(): OBJECT_NOT_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.portTypeId);
		dependencies.add(this.portId);
		dependencies.add(this.measurementPortId);
		dependencies.add(this.parentSchemeDeviceId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null: OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	final Identifier getMeasurementPortId() {
		assert this.measurementPortId != null: OBJECT_NOT_INITIALIZED;
		assert this.measurementPortId.isVoid() || this.measurementPortId.getMajor() == MEASUREMENTPORT_CODE;
		return this.measurementPortId;
	}

	/**
	 * A wrapper around {@link #getMeasurementPortId()}.
	 */
	public final MeasurementPort getMeasurementPort() {
		try {
			return (MeasurementPort) StorableObjectPool.getStorableObject(this.getMeasurementPortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0: OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	final Identifier getParentSchemeDeviceId() {
		assert this.parentSchemeDeviceId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		assert this.parentSchemeDeviceId.getMajor() == SCHEMEDEVICE_CODE;
		return this.parentSchemeDeviceId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeDeviceId()}.
	 */
	public final SchemeDevice getParentSchemeDevice() {
		try {
			return (SchemeDevice) StorableObjectPool.getStorableObject(this.getParentSchemeDeviceId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	final Identifier getPortId() {
		assert this.assertPortTypeSetStrict() : OBJECT_BADLY_INITIALIZED;
		assert this.portId.isVoid() || this.portId.getMajor() == PORT_CODE;
		return this.portId;
	}

	/**
	 * <p>A wrapper around {@link #getPortId()}.</p>
	 *
	 * <p>Overridden by descendants to add extra checks.</p>
	 */
	public Port getPort() {
		try {
			return (Port) StorableObjectPool.getStorableObject(this.getPortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	final Identifier getPortTypeId() {
		assert this.assertPortTypeSetStrict(): OBJECT_BADLY_INITIALIZED;
		assert this.portTypeId.isVoid() || this.portTypeId.getMajor() == PORT_TYPE_CODE;
		return this.portTypeId;
	}

	/**
	 * A wrapper around {@link #getPortTypeId()}.
	 */
	public final PortType getPortType() {
		try {
			return this.getPortId().isVoid()
					? (PortType) StorableObjectPool.getStorableObject(this.getPortTypeId(), true)
					: this.getPort().getType();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
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
	final void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final IdlDirectionType directionType,
			final Identifier portTypeId,
			final Identifier portId,
			final Identifier measurementPortId,
			final Identifier parentSchemeDeviceId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);
	
			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert description != null : NON_NULL_EXPECTED;
			assert directionType != null : NON_NULL_EXPECTED;
	
			assert portTypeId != null : NON_NULL_EXPECTED;
			assert portId != null : NON_NULL_EXPECTED;
			assert portTypeId.isVoid() ^ portId.isVoid();
	
			assert measurementPortId != null : NON_NULL_EXPECTED;
			assert parentSchemeDeviceId != null && !parentSchemeDeviceId.isVoid() : NON_VOID_EXPECTED;
	
			this.name = name;
			this.description = description;
			this.directionType = directionType;
			this.portTypeId = portTypeId;
			this.portId = portId;
			this.measurementPortId = measurementPortId;
			this.parentSchemeDeviceId = parentSchemeDeviceId;
		}
	}

	/**
	 * @param directionType
	 */
	public final void setDirectionType(final IdlDirectionType directionType) {
		assert this.directionType != null: OBJECT_NOT_INITIALIZED;
		assert directionType != null: NON_NULL_EXPECTED;
		if (this.directionType.value() == directionType.value()) {
			return;
		}
		this.directionType = directionType;
		super.markAsChanged();
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null: OBJECT_NOT_INITIALIZED;
		assert description != null: NON_NULL_EXPECTED;
		if (this.description.equals(description)) {
			return;
		}
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @param measurementPort
	 */
	public final void setMeasurementPort(final MeasurementPort measurementPort) {
		final Identifier newMeasurementPortId = Identifier.possiblyVoid(measurementPort);
		if (this.measurementPortId.equals(newMeasurementPortId)) {
			return;
		}
		this.measurementPortId = newMeasurementPortId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @param parentSchemeDevice
	 */
	public final void setParentSchemeDevice(final SchemeDevice parentSchemeDevice) {
		assert this.parentSchemeDeviceId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeDevice == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		final Identifier newParentSchemeDeviceId = parentSchemeDevice.getId();
		if (this.parentSchemeDeviceId.equals(newParentSchemeDeviceId)) {
			return;
		}
		this.parentSchemeDeviceId = newParentSchemeDeviceId;
		super.markAsChanged();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param port
	 */
	public void setPort(final Port port) {
		assert this.assertPortTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		final Identifier newPortId = Identifier.possiblyVoid(port);
		if (this.portId.equals(newPortId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (this.portId.isVoid()) {
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.portTypeId = VOID_IDENTIFIER;
		} else if (newPortId.isVoid()) {
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve).
			 */
			this.portTypeId = this.getPort().getType().getId();
		}
		this.portId = newPortId;
		super.markAsChanged();
	}

	/**
	 * @param portType
	 */
	public final void setPortType(final PortType portType) {
		assert this.assertPortTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;
		assert portType != null: NON_NULL_EXPECTED;

		if (this.portId.isVoid()) {
			final Identifier newPortTypeId = portType.getId();
			if (this.portTypeId.equals(newPortTypeId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.portTypeId = newPortTypeId;
			super.markAsChanged();
		} else {
			this.getPort().setType(portType);
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
	 * @throws CreateObjectException
	 */
	void fromTransferable(final IdlStorableObject header,
			final String name1, final String description1,
			final IdlDirectionType directionType1,
			final IdlIdentifier portTypeId1,
			final IdlIdentifier portId1,
			final IdlIdentifier measurementPortId1,
			final IdlIdentifier parentSchemeDeviceId1)
			throws CreateObjectException {
		try {
			super.fromTransferable(header);
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
		if (this.portTypeSet) {
			return this.assertPortTypeSetStrict();
		}
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

	/**
	 * @see CloneableStorableObject#getClonedIdMap()
	 */
	public final Map<Identifier, Identifier> getClonedIdMap() {
		return (this.clonedIdMap == null)
				? Collections.<Identifier, Identifier>emptyMap()
				: Collections.unmodifiableMap(this.clonedIdMap);
	}
}
