/*-
 * $Id: SchemeDevice.java,v 1.78 2005/09/12 00:10:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
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
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.XML_BEAN_NOT_COMPLETE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeDevice;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeDeviceHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePort;
import com.syrus.util.Log;

/**
 * #09 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.78 $, $Date: 2005/09/12 00:10:48 $
 * @module scheme
 */
public final class SchemeDevice extends AbstractCloneableStorableObject
		implements Describable, Characterizable,
		ReverseDependencyContainer,
		XmlBeansTransferable<XmlSchemeDevice> {
	private static final long serialVersionUID = 3762529027398644793L;

	private String name;

	private String description;

	Identifier parentSchemeElementId;

	Identifier parentSchemeProtoElementId;

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	private boolean parentSet = false;

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
	 * @param parentSchemeProtoElement
	 * @param parentSchemeElement
	 */
	SchemeDevice(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final SchemeProtoElement parentSchemeProtoElement,
			final SchemeElement parentSchemeElement) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;

		assert parentSchemeProtoElement == null || parentSchemeElement == null : EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 */
	private SchemeDevice(final Identifier id,
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
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeDevice(final IdlSchemeDevice transferable) throws CreateObjectException {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for {@link #createInstance(Identifier, String, String)}.
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId
	 * @param name
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name) throws CreateObjectException {
		return createInstance(creatorId, name, "");
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, SchemeProtoElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", parentSchemeProtoElement);
		
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, SchemeElement)}
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", parentSchemeElement);
	}

	/**
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId, final String name, final String description)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(IdentifierPool.getGeneratedIdentifier(SCHEMEDEVICE_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					null,
					null);
			schemeDevice.markAsChanged();
			return schemeDevice;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeDevice.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final SchemeProtoElement parentSchemeProtoElement) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(IdentifierPool.getGeneratedIdentifier(SCHEMEDEVICE_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					parentSchemeProtoElement,
					null);
			schemeDevice.markAsChanged();
			schemeDevice.parentSet = true;
			return schemeDevice;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeDevice.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final SchemeElement parentSchemeElement) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeElement != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(IdentifierPool.getGeneratedIdentifier(SCHEMEDEVICE_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					null,
					parentSchemeElement);
			schemeDevice.markAsChanged();
			schemeDevice.parentSet = true;
			return schemeDevice;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeDevice.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeDevice
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final XmlSchemeDevice xmlSchemeDevice,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlSchemeDevice.getId(), importType, SCHEMEDEVICE_CODE);
			SchemeDevice schemeDevice = StorableObjectPool.getStorableObject(id, true);
			if (schemeDevice == null) {
				schemeDevice = new SchemeDevice(id, new Date(), creatorId);
			}
			schemeDevice.fromXmlTransferable(xmlSchemeDevice, importType);
			assert schemeDevice.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeDevice.markAsChanged();
			return schemeDevice;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param schemeCablePort cannot be <code>null</code>.
	 */
	public void addSchemeCablePort(final SchemeCablePort schemeCablePort) {
		assert schemeCablePort != null: NON_NULL_EXPECTED;
		schemeCablePort.setParentSchemeDevice(this);
	}

	/**
	 * @param schemePort cannot be <code>null</code>.
	 */
	public void addSchemePort(final SchemePort schemePort) {
		assert schemePort != null: NON_NULL_EXPECTED;
		schemePort.setParentSchemeDevice(this);
	}

	@Override
	public SchemeDevice clone() throws CloneNotSupportedException {
		final StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
		final int depth = 1;
		if (stackTrace.length > depth) {
			final StackTraceElement stackTraceElement = stackTrace[depth];
			final String className = stackTraceElement.getClassName();
			final String methodName = stackTraceElement.getMethodName();
			if ((!className.equals(SchemeElement.class.getName())
					&& !className.equals(SchemeProtoElement.class.getName()))
					|| (!methodName.equals("clone")
					&& !methodName.equals("fillProperties"))) {
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
			final SchemeDevice clone = (SchemeDevice) super.clone();
	
			if (clone.clonedIdMap == null) {
				clone.clonedIdMap = new HashMap<Identifier, Identifier>();
			}
	
			clone.clonedIdMap.put(this.id, clone.id);
	
			for (final Characteristic characteristic : this.getCharacteristics(true)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				characteristicClone.setCharacterizableId(clone.id);
			}
			for (final SchemePort schemePort : this.getSchemePorts0()) {
				final SchemePort schemePortClone = schemePort.clone();
				clone.clonedIdMap.putAll(schemePortClone.getClonedIdMap());
				clone.addSchemePort(schemePortClone);
			}
			for (final SchemeCablePort schemeCablePort : this.getSchemeCablePorts0()) {
				final SchemeCablePort schemeCablePortClone = schemeCablePort.clone();
				clone.clonedIdMap.putAll(schemeCablePortClone.getClonedIdMap());
				clone.addSchemeCablePort(schemeCablePortClone);
			}
			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
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
	public Set<Identifiable> getDependencies() {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies()
	 */
	public Set<Identifiable> getReverseDependencies() throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics(true)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemePorts0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeCablePorts0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * @throws IllegalStateException
	 */
	Identifier getParentSchemeElementId() {
		assert true || this.assertParentSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert parentSchemeElementIdVoid || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		if (parentSchemeElementIdVoid) {
			Log.debugMessage("SchemeDevice.getParentSchemeElementId() | Parent SchemeElement was requested, while parent is a SchemeProtoElement; returning null.",
					FINE);
		}
		return this.parentSchemeElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeElementId()}.
	 *
	 * @throws IllegalStateException
	 */
	public SchemeElement getParentSchemeElement() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @throws IllegalStateException
	 */
	Identifier getParentSchemeProtoElementId() {
		assert true || this.assertParentSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		if (parentSchemeProtoElementIdVoid) {
			Log.debugMessage("SchemeDevice.getParentSchemeProtoElementId() | Parent SchemeProtoElement was requested, while parent is a SchemeElement; returning null.",
					FINE);
		}
		return this.parentSchemeProtoElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoElementId()}.
	 *
	 * @throws IllegalStateException
	 */
	public SchemeProtoElement getParentSchemeProtoElement() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeProtoElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeCablePort> getSchemeCablePorts() {
		try {
			return Collections.unmodifiableSet(this.getSchemeCablePorts0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemeCablePort> getSchemeCablePorts0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMECABLEPORT_CODE), true);
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemePort> getSchemePorts() {
		try {
			return Collections.unmodifiableSet(this.getSchemePorts0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemePort> getSchemePorts0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEPORT_CODE), true);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeDevice getTransferable(final ORB orb) {
		return IdlSchemeDeviceHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.parentSchemeProtoElementId.getTransferable(),
				this.parentSchemeElementId.getTransferable());
	}

	/**
	 * @see XmlBeansTransferable#getXmlTransferable(String)
	 */
	public XmlSchemeDevice getXmlTransferable(final String importType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The <code>SchemeCablePort</code> must belong to this
	 * <code>SchemeDevice</code>, or crap will meet the fan.
	 *
	 * @param schemeCablePort
	 */
	public void removeSchemeCablePort(final SchemeCablePort schemeCablePort) {
		assert schemeCablePort != null: NON_NULL_EXPECTED;
		assert schemeCablePort.getParentSchemeDeviceId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCablePort.setParentSchemeDevice(null);
	}

	/**
	 * The <code>SchemePort</code> must belong to this
	 * <code>SchemeDevice</code>, or crap will meet the fan.
	 *
	 * @param schemePort
	 */
	public void removeSchemePort(final SchemePort schemePort) {
		assert schemePort != null: NON_NULL_EXPECTED;
		assert schemePort.getParentSchemeDeviceId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemePort.setParentSchemeDevice(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param parentSchemeProtoElementId
	 * @param parentSchemeElementId
	 */
	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final Identifier parentSchemeProtoElementId,
			final Identifier parentSchemeElementId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);
	
			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert description != null : NON_NULL_EXPECTED;
			assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
			assert parentSchemeElementId != null : NON_NULL_EXPECTED;
			assert parentSchemeProtoElementId.isVoid() ^ parentSchemeElementId.isVoid();
	
			this.name = name;
			this.description = description;
			this.parentSchemeProtoElementId = parentSchemeProtoElementId;
			this.parentSchemeElementId = parentSchemeElementId;

			this.parentSet = true;
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description)) {
			return;
		}
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @todo skip check if parentless.
	 */
	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert this.assertParentSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeElementId;
		if (this.parentSchemeProtoElementId.isVoid()) {
			/*
			 * Moving from an element to another element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			if (this.parentSchemeElementId.equals(newParentSchemeElementId)) {
				return;
			}
		} else {
			/*
			 * Moving from a protoelement to an element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	/**
	 * @todo skip check if parentless.
	 */
	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		assert this.assertParentSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeProtoElementId;
		if (this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a protoelement to another protoelement.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId)) {
				return;
			}
		} else {
			/*
			 * Moving from an element to a protoelement.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			this.parentSchemeElementId = VOID_IDENTIFIER;
		}
		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		super.markAsChanged();
	}

	public void setSchemeCablePorts(final Set<SchemeCablePort> schemeCablePorts) throws ApplicationException {
		assert schemeCablePorts != null: NON_NULL_EXPECTED;
		final Set<SchemeCablePort> oldSchemeCablePorts = this.getSchemeCablePorts0();
		/*
		 * Check is made to prevent SchemeCablePorts from
		 * permanently losing their parents.
		 */
		oldSchemeCablePorts.removeAll(schemeCablePorts);
		for (final SchemeCablePort oldSchemeCablePort : oldSchemeCablePorts) {
			this.removeSchemeCablePort(oldSchemeCablePort);
		}
		for (final SchemeCablePort schemeCablePort : schemeCablePorts) {
			this.addSchemeCablePort(schemeCablePort);
		}
	}

	public void setSchemePorts(final Set<SchemePort> schemePorts) throws ApplicationException {
		assert schemePorts != null: NON_NULL_EXPECTED;
		final Set<SchemePort> oldSchemePorts = this.getSchemePorts0();
		/*
		 * Check is made to prevent SchemePorts from
		 * permanently losing their parents.
		 */
		oldSchemePorts.removeAll(schemePorts);
		for (final SchemePort oldSchemePort : oldSchemePorts) {
			this.removeSchemePort(oldSchemePort);
		}
		for (final SchemePort schemePort : schemePorts) {
			this.addSchemePort(schemePort);
		}
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable)
	throws CreateObjectException {
		synchronized (this) {
			final IdlSchemeDevice schemeDevice = (IdlSchemeDevice) transferable;
			try {
				super.fromTransferable(schemeDevice);
			} catch (final CreateObjectException coe) {
				throw coe;
			} catch (final ApplicationException ae) {
				throw new CreateObjectException(ae);
			}
			this.name = schemeDevice.name;
			this.description = schemeDevice.description;
			this.parentSchemeProtoElementId = new Identifier(schemeDevice.parentSchemeProtoElementId);
			this.parentSchemeElementId = new Identifier(schemeDevice.parentSchemeElementId);
	
			this.parentSet = true;
		}
	}

	/**
	 * @param schemeDevice
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(final XmlSchemeDevice schemeDevice,
			final String importType)
	throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(schemeDevice, SCHEMEDEVICE_CODE, importType);

		this.name = schemeDevice.getName();
		this.description = schemeDevice.isSetDescription()
				? schemeDevice.getDescription()
				: "";

		final boolean setParentSchemeProtoElementId = schemeDevice.isSetParentSchemeProtoElementId();
		final boolean setParentSchemeElementId = schemeDevice.isSetParentSchemeElementId();
		if (setParentSchemeProtoElementId) {
			assert !setParentSchemeElementId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeProtoElementId = Identifier.fromXmlTransferable(schemeDevice.getParentSchemeProtoElementId(), importType, MODE_THROW_IF_ABSENT);
			this.parentSchemeElementId = VOID_IDENTIFIER;
		} else if (setParentSchemeElementId) {
			assert !setParentSchemeProtoElementId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
			this.parentSchemeElementId = Identifier.fromXmlTransferable(schemeDevice.getParentSchemeElementId(), importType, MODE_THROW_IF_ABSENT);
		} else {
			throw new UpdateObjectException(
					"SchemeDevice.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}

		if (schemeDevice.isSetCharacteristics()) {
			for (final XmlCharacteristic characteristic : schemeDevice.getCharacteristics().getCharacteristicArray()) {
				Characteristic.createInstance(super.creatorId, characteristic, importType);
			}
		}
		if (schemeDevice.isSetSchemePorts()) {
			for (final XmlSchemePort schemePort : schemeDevice.getSchemePorts().getSchemePortArray()) {
				SchemePort.createInstance(super.creatorId, schemePort, importType);
			}
		}
		if (schemeDevice.isSetSchemeCablePorts()) {
			for (final XmlSchemeCablePort schemeCablePort : schemeDevice.getSchemeCablePorts().getSchemeCablePortArray()) {
				SchemeCablePort.createInstance(super.creatorId, schemeCablePort, importType);
			}
		}

		this.parentSet = true;
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertParentSetNonStrict() {
		if (this.parentSet) {
			return this.assertParentSetStrict();
		}
		this.parentSet = true;
		return this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& this.parentSchemeElementId.isVoid()
				&& this.parentSchemeProtoElementId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertParentSetStrict() {
		return this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeElementId.isVoid() ^ this.parentSchemeProtoElementId.isVoid());
	}

	void setParentSchemeElementId(Identifier parentSchemeElementId) {
		//TODO: inroduce additional sanity checks
		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeElementId.isVoid() || parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		this.parentSchemeElementId = parentSchemeElementId;
		super.markAsChanged();
	}

	void setParentSchemeProtoElementId(Identifier parentSchemeProtoElementId) {
		//TODO: inroduce additional sanity checks
		assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId.isVoid() || parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
		super.markAsChanged();
	}
}
