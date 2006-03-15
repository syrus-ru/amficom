/*-
 * $Id: SchemeDevice.java,v 1.121 2006/03/15 14:47:28 bass Exp $
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
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeDevice;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeDeviceHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePortSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePortSeq;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #09 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.121 $, $Date: 2006/03/15 14:47:28 $
 * @module scheme
 */
public final class SchemeDevice
		extends AbstractCloneableStorableObject
		implements Describable, Characterizable,
		ReverseDependencyContainer,
		XmlTransferableObject<XmlSchemeDevice>,
		IdlTransferableObjectExt<IdlSchemeDevice> {
	private static final long serialVersionUID = 3762529027398644793L;

	private String name;

	private String description;

	Identifier parentSchemeElementId;

	Identifier parentSchemeProtoElementId;

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
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private SchemeDevice(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEMEDEVICE_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeDevice(final IdlSchemeDevice transferable) throws CreateObjectException {
		try {
			fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
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
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final SchemeProtoElement parentSchemeProtoElement)
	throws CreateObjectException {
		final boolean usePool = false;

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
					StorableObjectVersion.INITIAL_VERSION,
					name,
					description,
					parentSchemeProtoElement,
					null);
			parentSchemeProtoElement.getSchemeDeviceContainerWrappee().addToCache(schemeDevice, usePool);

			schemeDevice.markAsChanged();
			return schemeDevice;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeDevice.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final SchemeElement parentSchemeElement)
	throws CreateObjectException {
		final boolean usePool = false;

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
					StorableObjectVersion.INITIAL_VERSION,
					name,
					description,
					null,
					parentSchemeElement);
			parentSchemeElement.getSchemeDeviceContainerWrappee().addToCache(schemeDevice, usePool);

			schemeDevice.markAsChanged();
			return schemeDevice;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeDevice.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
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
			final XmlIdentifier xmlId = xmlSchemeDevice.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeDevice schemeDevice;
			if (id.isVoid()) {
				schemeDevice = new SchemeDevice(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeDevice = StorableObjectPool.getStorableObject(id, true);
				if (schemeDevice == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeDevice = new SchemeDevice(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeDevice.fromXmlTransferable(xmlSchemeDevice, importType);
			assert schemeDevice.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeDevice.markAsChanged();
			return schemeDevice;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	@Override
	public SchemeDevice clone() throws CloneNotSupportedException {
		final boolean usePool = true;

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
	
			clone.characteristicContainerWrappee = null;
			for (final Characteristic characteristic : this.getCharacteristics0(usePool)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				clone.addCharacteristic(characteristicClone, usePool);
			}
			clone.schemePortContainerWrappee = null;
			for (final SchemePort schemePort : this.getSchemePorts0(usePool)) {
				final SchemePort schemePortClone = schemePort.clone();
				clone.clonedIdMap.putAll(schemePortClone.getClonedIdMap());
				clone.addSchemePort(schemePortClone, usePool);
			}
			clone.schemeCablePortContainerWrappee = null;
			for (final SchemeCablePort schemeCablePort : this.getSchemeCablePorts0(usePool)) {
				final SchemeCablePort schemeCablePortClone = schemeCablePort.clone();
				clone.clonedIdMap.putAll(schemeCablePortClone.getClonedIdMap());
				clone.addSchemeCablePort(schemeCablePortClone, usePool);
			}
			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependenciesTmpl()
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemePorts0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeCablePorts0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
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

	Identifier getParentSchemeElementId() {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeElementId.isVoid() ^ this.parentSchemeProtoElementId.isVoid()): OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert parentSchemeElementIdVoid || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		if (parentSchemeElementIdVoid) {
			Log.debugMessage("Parent SchemeElement was requested, while parent is a SchemeProtoElement; returning null.",
					FINE);
		}
		return this.parentSchemeElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeElementId()}.
	 */
	public SchemeElement getParentSchemeElement() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	Identifier getParentSchemeProtoElementId() {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeElementId.isVoid() ^ this.parentSchemeProtoElementId.isVoid()): OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		if (parentSchemeProtoElementIdVoid) {
			Log.debugMessage("Parent SchemeProtoElement was requested, while parent is a SchemeElement; returning null.",
					FINE);
		}
		return this.parentSchemeProtoElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoElementId()}.
	 */
	public SchemeProtoElement getParentSchemeProtoElement() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeProtoElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeDevice getIdlTransferable(final ORB orb) {
		return IdlSchemeDeviceHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.parentSchemeProtoElementId.getIdlTransferable(),
				this.parentSchemeElementId.getIdlTransferable());
	}

	/**
	 * @param schemeDevice
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeDevice schemeDevice,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			super.id.getXmlTransferable(schemeDevice.addNewId(), importType);
			schemeDevice.setName(this.name);
			if (schemeDevice.isSetDescription()) {
				schemeDevice.unsetDescription();
			}
			if (this.description.length() != 0) {
				schemeDevice.setDescription(this.description);
			}
			if (schemeDevice.isSetParentSchemeProtoElementId()) {
				schemeDevice.unsetParentSchemeProtoElementId();
			}
			if (!this.parentSchemeProtoElementId.isVoid()) {
				this.parentSchemeProtoElementId.getXmlTransferable(schemeDevice.addNewParentSchemeProtoElementId(), importType);
			}
			if (schemeDevice.isSetParentSchemeElementId()) {
				schemeDevice.unsetParentSchemeElementId();
			}
			if (!this.parentSchemeElementId.isVoid()) {
				this.parentSchemeElementId.getXmlTransferable(schemeDevice.addNewParentSchemeElementId(), importType);
			}
			if (schemeDevice.isSetCharacteristics()) {
				schemeDevice.unsetCharacteristics();
			}
			final Set<Characteristic> characteristics = this.getCharacteristics0(usePool);
			if (!characteristics.isEmpty()) {
				final XmlCharacteristicSeq characteristicSeq = schemeDevice.addNewCharacteristics();
				for (final Characteristic characteristic : characteristics) {
					characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
				}
			}
			if (schemeDevice.isSetSchemePorts()) {
				schemeDevice.unsetSchemePorts();
			}
			final Set<SchemePort> schemePorts = this.getSchemePorts0(usePool);
			if (!schemePorts.isEmpty()) {
				final XmlSchemePortSeq schemePortSeq = schemeDevice.addNewSchemePorts();
				for (final SchemePort schemePort : schemePorts) {
					schemePort.getXmlTransferable(schemePortSeq.addNewSchemePort(), importType, usePool);
				}
			}
			if (schemeDevice.isSetSchemeCablePorts()) {
				schemeDevice.unsetSchemeCablePorts();
			}
			final Set<SchemeCablePort> schemeCablePorts = this.getSchemeCablePorts0(usePool);
			if (!schemeCablePorts.isEmpty()) {
				final XmlSchemeCablePortSeq schemeCablePortSeq = schemeDevice.addNewSchemeCablePorts();
				for (final SchemeCablePort schemeCablePort : schemeCablePorts) {
					schemeCablePort.getXmlTransferable(schemeCablePortSeq.addNewSchemeCablePort(), importType, usePool);
				}
			}
			XmlComplementorRegistry.complementStorableObject(schemeDevice, SCHEMEDEVICE_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
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
	 * A wrapper around {@link #setParentSchemeElement(SchemeElement, boolean)}.
	 *
	 * @param parentSchemeElementId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeElementId(final Identifier parentSchemeElementId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeElementId.isVoid() || parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;

		if (this.parentSchemeElementId.equals(parentSchemeElementId)) {
			return;
		}

		this.setParentSchemeElement(
				StorableObjectPool.<SchemeElement>getStorableObject(parentSchemeElementId, true),
				usePool);
	}

	/**
	 * @param parentSchemeElement
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeElement(final SchemeElement parentSchemeElement,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeElementId.isVoid() ^ this.parentSchemeProtoElementId.isVoid()): OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeElementNull = (parentSchemeElement == null);

		final Identifier newParentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		if (this.parentSchemeElementId.equals(newParentSchemeElementId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (this.parentSchemeProtoElementId.isVoid()) {
			/*
			 * Moving from an element to another element. At this
			 * point, newParentSchemeElementId may be void.
			 */
			this.getParentSchemeElement().getSchemeDeviceContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeElementNull) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			}
		} else {
			/*
			 * Moving from a protoelement to an element. At this
			 * point, newParentSchemeElementId is non-void.
			 */
			this.getParentSchemeProtoElement().getSchemeDeviceContainerWrappee().removeFromCache(this, usePool);

			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		}

		if (!parentSchemeElementNull) {
			parentSchemeElement.getSchemeDeviceContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeProtoElement(SchemeProtoElement, boolean)}.
	 *
	 * @param parentSchemeProtoElementId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeProtoElementId(final Identifier parentSchemeProtoElementId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId.isVoid() || parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;

		if (this.parentSchemeProtoElementId.equals(parentSchemeProtoElementId)) {
			return;
		}

		this.setParentSchemeProtoElement(
				StorableObjectPool.<SchemeProtoElement>getStorableObject(parentSchemeProtoElementId, true),
				usePool);
	}

	/**
	 * @param parentSchemeProtoElement
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeElementId.isVoid() ^ this.parentSchemeProtoElementId.isVoid()): OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeProtoElementNull = (parentSchemeProtoElement == null);

		final Identifier newParentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
		if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a protoelement to another protoelement.
			 * At this point, newParentSchemeProtoElementId may be
			 * void.
			 */
			this.getParentSchemeProtoElement().getSchemeDeviceContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeProtoElementNull) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			}
		} else {
			/*
			 * Moving from an element to a protoelement. At this
			 * point, newParentSchemeProtoElementId is non-void.
			 */
			this.getParentSchemeElement().getSchemeDeviceContainerWrappee().removeFromCache(this, usePool);

			this.parentSchemeElementId = VOID_IDENTIFIER;
		}

		if (!parentSchemeProtoElementNull) {
			parentSchemeProtoElement.getSchemeDeviceContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		super.markAsChanged();
	}

	/**
	 * @param schemeDevice
	 * @throws IdlConversionException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(com.syrus.AMFICOM.general.corba.IdlStorableObject) 
	 */
	public void fromIdlTransferable(final IdlSchemeDevice schemeDevice)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable(schemeDevice);
			this.name = schemeDevice.name;
			this.description = schemeDevice.description;
			this.parentSchemeProtoElementId = new Identifier(schemeDevice.parentSchemeProtoElementId);
			this.parentSchemeElementId = new Identifier(schemeDevice.parentSchemeElementId);
		}
	}

	/**
	 * @param schemeDevice
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlSchemeDevice schemeDevice,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(schemeDevice, SCHEMEDEVICE_CODE, importType, PRE_IMPORT);
	
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
				throw new XmlConversionException(
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
	
			XmlComplementorRegistry.complementStorableObject(schemeDevice, SCHEMEDEVICE_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeDeviceWrapper getWrapper() {
		return SchemeDeviceWrapper.getInstance();
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
	public void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
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

	/*-********************************************************************
	 * Children manipulation: scheme ports                                *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemePort> schemePortContainerWrappee;

	StorableObjectContainerWrappee<SchemePort> getSchemePortContainerWrappee() {
		return (this.schemePortContainerWrappee == null)
				? this.schemePortContainerWrappee = new StorableObjectContainerWrappee<SchemePort>(this, SCHEMEPORT_CODE)
				: this.schemePortContainerWrappee;
	}

	/**
	 * @param schemePort cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemePort(final SchemePort schemePort,
			final boolean usePool)
	throws ApplicationException {
		assert schemePort != null: NON_NULL_EXPECTED;
		schemePort.setParentSchemeDevice(this, usePool);
	}

	/**
	 * The <code>SchemePort</code> must belong to this
	 * <code>SchemeDevice</code>, or crap will meet the fan.
	 *
	 * @param schemePort
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemePort(final SchemePort schemePort,
			final boolean usePool)
	throws ApplicationException {
		assert schemePort != null: NON_NULL_EXPECTED;
		assert schemePort.getParentSchemeDeviceId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemePort.setParentSchemeDevice(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<SchemePort> getSchemePorts(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemePorts0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemePort> getSchemePorts0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemePortContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemePorts
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemePorts(final Set<SchemePort> schemePorts,
			final boolean usePool)
	throws ApplicationException {
		assert schemePorts != null: NON_NULL_EXPECTED;

		final Set<SchemePort> oldSchemePorts = this.getSchemePorts0(usePool);

		final Set<SchemePort> toRemove = new HashSet<SchemePort>(oldSchemePorts);
		toRemove.removeAll(schemePorts);
		for (final SchemePort schemePort : toRemove) {
			this.removeSchemePort(schemePort, usePool);
		}

		final Set<SchemePort> toAdd = new HashSet<SchemePort>(schemePorts);
		toAdd.removeAll(oldSchemePorts);
		for (final SchemePort schemePort : toAdd) {
			this.addSchemePort(schemePort, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme cable ports                          *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeCablePort> schemeCablePortContainerWrappee;

	StorableObjectContainerWrappee<SchemeCablePort> getSchemeCablePortContainerWrappee() {
		return (this.schemeCablePortContainerWrappee == null)
				? this.schemeCablePortContainerWrappee = new StorableObjectContainerWrappee<SchemeCablePort>(this, SCHEMECABLEPORT_CODE)
				: this.schemeCablePortContainerWrappee;
	}

	/**
	 * @param schemeCablePort cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeCablePort(final SchemeCablePort schemeCablePort,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCablePort != null: NON_NULL_EXPECTED;
		schemeCablePort.setParentSchemeDevice(this, usePool);
	}

	/**
	 * The <code>SchemeCablePort</code> must belong to this
	 * <code>SchemeDevice</code>, or crap will meet the fan.
	 *
	 * @param schemeCablePort
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeCablePort(final SchemeCablePort schemeCablePort,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCablePort != null: NON_NULL_EXPECTED;
		assert schemeCablePort.getParentSchemeDeviceId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCablePort.setParentSchemeDevice(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<SchemeCablePort> getSchemeCablePorts(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeCablePorts0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeCablePort> getSchemeCablePorts0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeCablePortContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeCablePorts
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeCablePorts(
			final Set<SchemeCablePort> schemeCablePorts,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCablePorts != null: NON_NULL_EXPECTED;

		final Set<SchemeCablePort> oldSchemeCablePorts = this.getSchemeCablePorts0(usePool);

		final Set<SchemeCablePort> toRemove = new HashSet<SchemeCablePort>(oldSchemeCablePorts);
		toRemove.removeAll(schemeCablePorts);
		for (final SchemeCablePort schemeCablePort : toRemove) {
			this.removeSchemeCablePort(schemeCablePort, usePool);
		}

		final Set<SchemeCablePort> toAdd = new HashSet<SchemeCablePort>(schemeCablePorts);
		toAdd.removeAll(oldSchemeCablePorts);
		for (final SchemeCablePort schemeCablePort : toAdd) {
			this.addSchemeCablePort(schemeCablePort, usePool);
		}
	}
}
