/*-
 * $Id: AbstractSchemePort.java,v 1.64 2005/09/13 08:35:41 bass Exp $
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
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.xml.XmlAbstractSchemePort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.64 $, $Date: 2005/09/13 08:35:41 $
 * @module scheme
 */
public abstract class AbstractSchemePort
		extends AbstractCloneableStorableObject
		implements Describable, Characterizable, ReverseDependencyContainer {
	private static final long serialVersionUID = 6943625949984422779L;

	private String name;

	private String description;

	private int directionType;

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

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	boolean portTypeSet = false;

	private transient CharacterizableDelegate characterizableDelegate;

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
		this.directionType = (directionType == null) ? 0 : directionType.value();

		assert portType == null || port == null;
		this.portTypeId = Identifier.possiblyVoid(portType);
		this.portId = Identifier.possiblyVoid(port);

		this.measurementPortId = Identifier.possiblyVoid(measurementPort);
		this.parentSchemeDeviceId = Identifier.possiblyVoid(parentSchemeDevice);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 */
	AbstractSchemePort(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id,
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
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
		return IdlDirectionType.from_int(this.directionType);
	}

	/**
	 * @see Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
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
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies()
	 */
	public final Set<Identifiable> getReverseDependencies() throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics(true)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
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
			return StorableObjectPool.getStorableObject(this.getMeasurementPortId(), true);
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
			return StorableObjectPool.getStorableObject(this.getParentSchemeDeviceId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	final Identifier getPortId() {
		assert true || this.assertPortTypeSetStrict() : OBJECT_BADLY_INITIALIZED;
		if (!this.assertPortTypeSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
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
			return StorableObjectPool.getStorableObject(this.getPortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	final Identifier getPortTypeId() {
		assert true || this.assertPortTypeSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertPortTypeSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		assert this.portTypeId.isVoid() || this.portTypeId.getMajor() == PORT_TYPE_CODE;
		return this.portTypeId;
	}

	/**
	 * A wrapper around {@link #getPortTypeId()}.
	 */
	public final PortType getPortType() {
		try {
			return this.getPortId().isVoid()
					? StorableObjectPool.<PortType>getStorableObject(this.getPortTypeId(), true)
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
			this.directionType = directionType.value();
			this.portTypeId = portTypeId;
			this.portId = portId;
			this.measurementPortId = measurementPortId;
			this.parentSchemeDeviceId = parentSchemeDeviceId;

			this.portTypeSet = true;
		}
	}

	/**
	 * @param directionType
	 */
	public final void setDirectionType(final IdlDirectionType directionType) {
		assert directionType != null: NON_NULL_EXPECTED;
		if (this.getDirectionType() == directionType) {
			return;
		}
		this.directionType = directionType.value();
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
	 * @param measurementPortId
	 */
	final void setMeasurementPortId(final Identifier measurementPortId) {
		assert measurementPortId != null : NON_NULL_EXPECTED;
		assert measurementPortId.isVoid() || measurementPortId.getMajor() == MEASUREMENTPORT_CODE;

		if (this.measurementPortId.equals(measurementPortId)) {
			return;
		}
		this.measurementPortId = measurementPortId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setMeasurementPortId(Identifier)}.
	 *
	 * @param measurementPort
	 */
	public final void setMeasurementPort(final MeasurementPort measurementPort) {
		this.setMeasurementPortId(Identifier.possiblyVoid(measurementPort));
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
	 * @param parentSchemeDeviceId
	 */
	final void setParentSchemeDeviceId(final Identifier parentSchemeDeviceId) {
		assert this.parentSchemeDeviceId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		assert parentSchemeDeviceId != null : NON_NULL_EXPECTED;
		final boolean parentSchemeDeviceIdVoid = parentSchemeDeviceId.isVoid();
		assert parentSchemeDeviceIdVoid || parentSchemeDeviceId.getMajor() == SCHEMEDEVICE_CODE;

		if (parentSchemeDeviceIdVoid) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		if (this.parentSchemeDeviceId.equals(parentSchemeDeviceId)) {
			return;
		}
		this.parentSchemeDeviceId = parentSchemeDeviceId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeDeviceId(Identifier)}.
	 *
	 * @param parentSchemeDevice
	 */
	public final void setParentSchemeDevice(final SchemeDevice parentSchemeDevice) {
		this.setParentSchemeDeviceId(Identifier.possiblyVoid(parentSchemeDevice));
	}

	/**
	 * @param portId
	 */
	final void setPortId(final Identifier portId) {
		assert this.assertPortTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		assert portId != null : NON_NULL_EXPECTED;
		final boolean portIdVoid = portId.isVoid();
		assert portIdVoid || portId.getMajor() == PORT_CODE;

		if (this.portId.equals(portId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (this.portId.isVoid()) {
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.portTypeId = VOID_IDENTIFIER;
		} else if (portIdVoid) {
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve).
			 */
			this.portTypeId = this.getPort().getTypeId();
		}
		this.portId = portId;
		super.markAsChanged();
	}

	/**
	 * <p>A wrapper around {@link #setPortId(Identifier)}.</p>
	 *
	 * <p>Overridden by descendants to add extra checks.</p>
	 *
	 * @param port
	 */
	public void setPort(final Port port) {
		this.setPortId(Identifier.possiblyVoid(port));
	}

	/**
	 * @param portTypeId
	 */
	final void setPortTypeId(final Identifier portTypeId) {
		assert this.assertPortTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		assert portTypeId != null : NON_NULL_EXPECTED;
		assert !portTypeId.isVoid() : NON_VOID_EXPECTED;
		assert portTypeId.getMajor() == PORT_TYPE_CODE;

		if (this.portId.isVoid()) {
			if (this.portTypeId.equals(portTypeId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.portTypeId = portTypeId;
			super.markAsChanged();
		} else {
			this.getPort().setTypeId(portTypeId);
		}
	}

	/**
	 * A wrapper around {@link #setPortTypeId(Identifier)}.
	 *
	 * @param portType
	 */
	public final void setPortType(final PortType portType) {
		this.setPortTypeId(Identifier.possiblyVoid(portType));
	}

	/**
	 * @param abstractSchemePort
	 * @param abstractPortTypeId
	 * @param abstractPortId
	 * @throws CreateObjectException
	 */
	final void fromTransferable(final IdlAbstractSchemePort abstractSchemePort,
			final IdlIdentifier abstractPortTypeId,
			final IdlIdentifier abstractPortId)
	throws CreateObjectException {
		try {
			super.fromTransferable(abstractSchemePort);
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = abstractSchemePort.name;
		this.description = abstractSchemePort.description;
		this.directionType = abstractSchemePort.directionType.value();
		this.portTypeId = new Identifier(abstractPortTypeId);
		this.portId = new Identifier(abstractPortId);
		this.measurementPortId = new Identifier(abstractSchemePort.measurementPortId);
		this.parentSchemeDeviceId = new Identifier(abstractSchemePort.parentSchemeDeviceId);

		this.portTypeSet = true;
	}

	/**
	 * @param abstractSchemePort
	 * @param importType
	 * @throws ApplicationException
	 */
	final void fromXmlTransferable(final XmlAbstractSchemePort abstractSchemePort,
			final String importType)
	throws ApplicationException {
		this.name = abstractSchemePort.getName();
		this.description = abstractSchemePort.isSetDescription()
				? abstractSchemePort.getDescription()
				: "";
		this.directionType = abstractSchemePort.getDirectionType().intValue() - 1;
		this.measurementPortId = abstractSchemePort.isSetMeasurementPortId()
				? Identifier.fromXmlTransferable(abstractSchemePort.getMeasurementPortId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.parentSchemeDeviceId = Identifier.fromXmlTransferable(abstractSchemePort.getParentSchemeDeviceId(), importType, MODE_THROW_IF_ABSENT);
		if (abstractSchemePort.isSetCharacteristics()) {
			for (final XmlCharacteristic characteristic : abstractSchemePort.getCharacteristics().getCharacteristicArray()) {
				Characteristic.createInstance(super.creatorId, characteristic, importType);
			}
		}

		this.portTypeSet = true;
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public AbstractSchemePort clone() throws CloneNotSupportedException {
		final StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
		final int depth = 2;
		if (stackTrace.length > depth) {
			final StackTraceElement stackTraceElement = stackTrace[depth];
			final String className = stackTraceElement.getClassName();
			final String methodName = stackTraceElement.getMethodName();
			if (!(className.equals(SchemeDevice.class.getName()) && methodName.equals("clone"))) {
				final StackTraceElement rootStackTraceElement = stackTrace[depth - 1];
				throw new CloneNotSupportedException(
						"invocation of "
						+ rootStackTraceElement.getClassName()
						+ '.' + rootStackTraceElement.getMethodName()
						+ '(' + rootStackTraceElement.getFileName()
						+ ':' + (rootStackTraceElement.getLineNumber() - 1)
						+ ") from " + className + '.' + methodName + '('
						+ stackTraceElement.getFileName() + ':'
						+ stackTraceElement.getLineNumber() + ')'
						+ " is prohibited");
			}
		}
		try {
			final AbstractSchemePort clone = (AbstractSchemePort) super.clone();

			if (clone.clonedIdMap == null) {
				clone.clonedIdMap = new HashMap<Identifier, Identifier>();
			}

			clone.clonedIdMap.put(this.id, clone.id);

			for (final Characteristic characteristic : this.getCharacteristics(true)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				characteristicClone.setCharacterizableId(clone.id);
			}
			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
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
}
