/*-
 * $Id: SchemeCableThread.java,v 1.104 2005/10/25 19:53:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
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
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
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
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableThread;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableThreadHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThread;
import com.syrus.util.Log;

/**
 * #14 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.104 $, $Date: 2005/10/25 19:53:13 $
 * @module scheme
 */
public final class SchemeCableThread
		extends AbstractCloneableStorableObject<SchemeCableThread>
		implements Describable, Characterizable,
		ReverseDependencyContainer,
		XmlBeansTransferable<XmlSchemeCableThread> {
	private static final long serialVersionUID = 4050204133015171124L;

	private String name;

	private String description;

	private Identifier linkTypeId;

	private Identifier linkId;

	Identifier sourceSchemePortId;

	Identifier targetSchemePortId;

	Identifier parentSchemeCableLinkId;

	private boolean linkTypeSet = false;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param linkType
	 * @param link
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeCableLink
	 */
	SchemeCableThread(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final LinkType linkType,
			final Link link,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeCableLink parentSchemeCableLink) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.linkTypeId = Identifier.possiblyVoid(linkType);
		this.linkId = Identifier.possiblyVoid(link);
		this.sourceSchemePortId = Identifier.possiblyVoid(sourceSchemePort);
		this.targetSchemePortId = Identifier.possiblyVoid(targetSchemePort);
		this.parentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);
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
	private SchemeCableThread(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEMECABLETHREAD_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeCableThread(final IdlSchemeCableThread transferable) throws CreateObjectException {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, LinkType, Link, SchemePort, SchemePort, SchemeCableLink)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param linkType
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static SchemeCableThread createInstance(final Identifier creatorId, final String name, final LinkType linkType, final SchemeCableLink parentSchemeCableLink) throws CreateObjectException {
		return createInstance(creatorId, name, "", linkType,
				null, null, null, parentSchemeCableLink);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param linkType
	 * @param link
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeCableThread createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final LinkType linkType,
			final Link link,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeCableLink parentSchemeCableLink)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert linkType != null : NON_NULL_EXPECTED;
		assert parentSchemeCableLink != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCableThread schemeCableThread = new SchemeCableThread(IdentifierPool.getGeneratedIdentifier(SCHEMECABLETHREAD_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					linkType,
					link,
					sourceSchemePort,
					targetSchemePort,
					parentSchemeCableLink);
			parentSchemeCableLink.getSchemeCableThreadContainerWrappee().addToCache(schemeCableThread, usePool);

			schemeCableThread.linkTypeSet = (link != null || linkType != null);

			schemeCableThread.markAsChanged();
			return schemeCableThread;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ioee) {
			throw new CreateObjectException("SchemeCableThread.createInstance | cannot generate identifier ", ioee);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeCableThread
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeCableThread createInstance(final Identifier creatorId,
			final XmlSchemeCableThread xmlSchemeCableThread,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlSchemeCableThread.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeCableThread schemeCableThread;
			if (id.isVoid()) {
				schemeCableThread = new SchemeCableThread(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeCableThread = StorableObjectPool.getStorableObject(id, true);
				if (schemeCableThread == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeCableThread = new SchemeCableThread(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeCableThread.fromXmlTransferable(xmlSchemeCableThread, importType);
			assert schemeCableThread.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeCableThread.markAsChanged();
			return schemeCableThread;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public SchemeCableThread clone() throws CloneNotSupportedException {
		final boolean usePool = false;

		final StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
		final int depth = 1;
		if (stackTrace.length > depth) {
			final StackTraceElement stackTraceElement = stackTrace[depth];
			final String className = stackTraceElement.getClassName();
			final String methodName = stackTraceElement.getMethodName();
			if (!(className.equals(SchemeCableLink.class.getName())
					&& methodName.equals("clone"))) {
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
			final SchemeCableThread clone = super.clone();

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
			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
	}

	Identifier getLinkTypeId() {
		assert true || this.assertLinkTypeSetStrict() : OBJECT_BADLY_INITIALIZED;
		if (!this.assertLinkTypeSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		assert this.linkTypeId.isVoid() || this.linkTypeId.getMajor() == LINK_TYPE_CODE;
		return this.linkTypeId;
	}

	/**
	 * A wrapper around {@link #getLinkTypeId()}.
	 */
	public LinkType getLinkType() {
		try {
			return this.linkId.isVoid()
					? StorableObjectPool.<LinkType>getStorableObject(this.getLinkTypeId(), true)
					: this.getLink().getType();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.linkTypeId != null && this.linkId != null
				&& this.sourceSchemePortId != null
				&& this.targetSchemePortId != null
				&& this.parentSchemeCableLinkId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.linkTypeId);
		dependencies.add(this.linkId);
		dependencies.add(this.sourceSchemePortId);
		dependencies.add(this.targetSchemePortId);
		dependencies.add(this.parentSchemeCableLinkId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
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

	Identifier getLinkId() {
		assert true || this.assertLinkTypeSetStrict() : OBJECT_BADLY_INITIALIZED;
		if (!this.assertLinkTypeSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		assert this.linkId.isVoid() || this.linkId.getMajor() == LINK_CODE;
		return this.linkId;
	}

	/**
	 * A wrapper around {@link #getLinkId()}.
	 */
	public Link getLink() {
		try {
			return StorableObjectPool.getStorableObject(this.getLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	Identifier getParentSchemeCableLinkId() {
		assert this.parentSchemeCableLinkId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeCableLinkId.isVoid(): OBJECT_BADLY_INITIALIZED;
		assert this.parentSchemeCableLinkId.getMajor() == SCHEMECABLELINK_CODE;
		return this.parentSchemeCableLinkId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeCableLinkId()}.
	 */
	public SchemeCableLink getParentSchemeCableLink() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeCableLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param schemeDevice
	 */
	public SchemePort getSchemePort(final SchemeDevice schemeDevice) {
		assert schemeDevice != null;
		final SchemePort sourceSchemePort = getSourceSchemePort();
		final SchemePort targetSchemePort = getTargetSchemePort();
		
		// fix by Stas
		// sourceSchemePort or targetSchemePort can be null 
		final Identifier schemeDeviceId = schemeDevice.getId();
		if (sourceSchemePort != null) {
			final Identifier sourceSchemeDeviceId = sourceSchemePort.getParentSchemeDevice().getId();
			if (schemeDeviceId.equals(sourceSchemeDeviceId))
				return sourceSchemePort;
		}
		if (targetSchemePort != null) {
			final Identifier targetSchemeDeviceId = targetSchemePort.getParentSchemeDevice().getId();
			if (schemeDeviceId.equals(targetSchemeDeviceId))
				return targetSchemePort;
		}
		return null;
		// fix by Stas
		// no need to throw exception here. It's correct in the case of greater number of SCT in SCL then SP in SD
//		throw new IllegalArgumentException(
//					"This scheme cable thread is in no way connected to the scheme device specified.");
	}

	public Identifier getSourceSchemePortId() {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.sourceSchemePortId.isVoid()
				|| !this.sourceSchemePortId.equals(this.targetSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		assert this.sourceSchemePortId.isVoid() || this.sourceSchemePortId.getMajor() == SCHEMEPORT_CODE;
		return this.sourceSchemePortId;
	}

	/**
	 * A wrapper around {@link #getSourceSchemePortId()}.
	 */
	public SchemePort getSourceSchemePort() {
		try {
			return StorableObjectPool.getStorableObject(this.getSourceSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getTargetSchemePortId() {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.targetSchemePortId.isVoid()
				|| !this.targetSchemePortId.equals(this.sourceSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		assert this.targetSchemePortId.isVoid() || this.targetSchemePortId.getMajor() == SCHEMEPORT_CODE;
		return this.targetSchemePortId;
	}

	/**
	 * A wrapper around {@link #getTargetSchemePortId()}.
	 */
	public SchemePort getTargetSchemePort() {
		try {
			return StorableObjectPool.getStorableObject(this.getTargetSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeCableThread getTransferable(final ORB orb) {
		return IdlSchemeCableThreadHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.linkTypeId.getTransferable(),
				this.linkId.getTransferable(),
				this.sourceSchemePortId.getTransferable(),
				this.targetSchemePortId.getTransferable(),
				this.parentSchemeCableLinkId.getTransferable());
	}

	/**
	 * @param schemeCableThread
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeCableThread schemeCableThread,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		super.id.getXmlTransferable(schemeCableThread.addNewId(), importType);
		schemeCableThread.setName(this.name);
		if (schemeCableThread.isSetDescription()) {
			schemeCableThread.unsetDescription();
		}
		if (this.description.length() != 0) {
			schemeCableThread.setDescription(this.description);
		}
		if (schemeCableThread.isSetLinkTypeId()) {
			schemeCableThread.unsetLinkTypeId();
		}
		if (!this.linkTypeId.isVoid()) {
			this.linkTypeId.getXmlTransferable(schemeCableThread.addNewLinkTypeId(), importType);
		}
		if (schemeCableThread.isSetLinkId()) {
			schemeCableThread.unsetLinkId();
		}
		if (!this.linkId.isVoid()) {
			this.linkId.getXmlTransferable(schemeCableThread.addNewLinkId(), importType);
		}
		if (schemeCableThread.isSetSourceSchemePortId()) {
			schemeCableThread.unsetSourceSchemePortId();
		}
		if (!this.sourceSchemePortId.isVoid()) {
			this.sourceSchemePortId.getXmlTransferable(schemeCableThread.addNewSourceSchemePortId(), importType);
		}
		if (schemeCableThread.isSetTargetSchemePortId()) {
			schemeCableThread.unsetTargetSchemePortId();
		}
		if (!this.targetSchemePortId.isVoid()) {
			this.targetSchemePortId.getXmlTransferable(schemeCableThread.addNewTargetSchemePortId(), importType);
		}
		this.parentSchemeCableLinkId.getXmlTransferable(schemeCableThread.addNewParentSchemeCableLinkId(), importType);
		if (schemeCableThread.isSetCharacteristics()) {
			schemeCableThread.unsetCharacteristics();
		}
		final Set<Characteristic> characteristics = this.getCharacteristics0(usePool);
		if (false && !characteristics.isEmpty()) {
			final XmlCharacteristicSeq characteristicSeq = schemeCableThread.addNewCharacteristics();
			for (final Characteristic characteristic : characteristics) {
				characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
			}
		}
		XmlComplementorRegistry.complementStorableObject(schemeCableThread, SCHEMECABLETHREAD_CODE, importType, EXPORT);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param linkTypeId
	 * @param linkId
	 * @param sourceSchemePortId
	 * @param targetSchemePortId
	 * @param parentSchemeCableLinkId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final Identifier linkTypeId,
			final Identifier linkId,
			final Identifier parentSchemeCableLinkId,
			final Identifier sourceSchemePortId,
			final Identifier targetSchemePortId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert linkTypeId != null && !linkTypeId.isVoid() : NON_VOID_EXPECTED;
		assert linkId != null;
		assert sourceSchemePortId != null : NON_NULL_EXPECTED;
		assert targetSchemePortId != null : NON_NULL_EXPECTED;
		assert parentSchemeCableLinkId != null : NON_NULL_EXPECTED; 
		assert !parentSchemeCableLinkId.isVoid() : NON_VOID_EXPECTED;

		this.name = name;
		this.description = description;
		this.linkTypeId = linkTypeId;
		this.linkId = linkId;
		this.sourceSchemePortId = sourceSchemePortId;
		this.targetSchemePortId = targetSchemePortId;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;

		this.linkTypeSet = true;
	}

	/**
	 * @param linkType
	 */
	public void setLinkType(final LinkType linkType) {
		assert this.assertLinkTypeSetNonStrict() : OBJECT_BADLY_INITIALIZED;
		assert linkType != null: NON_NULL_EXPECTED;

		if (this.linkId.isVoid()) {
			final Identifier newCableThreadTypeId = linkType.getId();
			if (this.linkTypeId.equals(newCableThreadTypeId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.linkTypeId = newCableThreadTypeId;
			super.markAsChanged();
		} else {
			this.getLink().setType(linkType);
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @param link
	 */
	public void setLink(final Link link) {
		assert this.assertLinkTypeSetNonStrict() : OBJECT_BADLY_INITIALIZED;

		final Identifier newLinkId = Identifier.possiblyVoid(link);
		if (this.linkId.equals(newLinkId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (this.linkId.isVoid()) {
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.linkTypeId = VOID_IDENTIFIER;
		} else if (newLinkId.isVoid()) {
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve).
			 */
			this.linkTypeId = this.getLink().getType().getId();
		}
		this.linkId = newLinkId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeCableLink(SchemeCableLink, boolean)}.
	 *
	 * @param parentSchemeCableLinkId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeCableLinkId(final Identifier parentSchemeCableLinkId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeCableLinkId != null : NON_NULL_EXPECTED;
		assert parentSchemeCableLinkId.isVoid() || parentSchemeCableLinkId.getMajor() == SCHEMECABLELINK_CODE;

		if (this.parentSchemeCableLinkId.equals(parentSchemeCableLinkId)) {
			return;
		}

		this.setParentSchemeCableLink(
				StorableObjectPool.<SchemeCableLink>getStorableObject(parentSchemeCableLinkId, true),
				usePool);
	}

	/**
	 * @param parentSchemeCableLink
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeCableLink(
			final SchemeCableLink parentSchemeCableLink,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeCableLinkId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeCableLinkId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newParentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);
		if (this.parentSchemeCableLinkId.equals(newParentSchemeCableLinkId)) {
			return;
		}

		this.getParentSchemeCableLink().getSchemeCableThreadContainerWrappee().removeFromCache(this, usePool);

		if (parentSchemeCableLink == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			parentSchemeCableLink.getSchemeCableThreadContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeCableLinkId = newParentSchemeCableLinkId;
		super.markAsChanged();
	}

	/**
	 * @param sourceSchemePortId
	 */
	void setSourceSchemePortId(final Identifier sourceSchemePortId) {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.sourceSchemePortId.isVoid()
				|| !this.sourceSchemePortId.equals(this.targetSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		assert sourceSchemePortId.isVoid()
				|| !sourceSchemePortId.equals(this.targetSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		if (this.sourceSchemePortId.equals(sourceSchemePortId))
			return;
		this.sourceSchemePortId = sourceSchemePortId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setSourceSchemePortId(Identifier)}.
	 *
	 * @param sourceSchemePort
	 */
	public void setSourceSchemePort(final SchemePort sourceSchemePort) {
		this.setSourceSchemePortId(Identifier.possiblyVoid(sourceSchemePort));
	}

	/**
	 * @param targetSchemePortId
	 */
	void setTargetSchemePortId(final Identifier targetSchemePortId) {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.targetSchemePortId.isVoid()
				|| !this.targetSchemePortId.equals(this.sourceSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		assert targetSchemePortId.isVoid()
				|| !targetSchemePortId.equals(this.sourceSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		if (this.targetSchemePortId.equals(targetSchemePortId))
			return;
		this.targetSchemePortId = targetSchemePortId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setTargetSchemePortId(Identifier)}.
	 *
	 * @param targetSchemePort
	 */
	public void setTargetSchemePort(final SchemePort targetSchemePort) {
		this.setTargetSchemePortId(Identifier.possiblyVoid(targetSchemePort));
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
			final IdlSchemeCableThread schemeCableThread = (IdlSchemeCableThread) transferable;
			try {
				super.fromTransferable(schemeCableThread);
			} catch (final CreateObjectException coe) {
				throw coe;
			} catch (final ApplicationException ae) {
				throw new CreateObjectException(ae);
			}
			this.name = schemeCableThread.name;
			this.description = schemeCableThread.description;
			this.linkTypeId = new Identifier(schemeCableThread.linkTypeId);
			this.linkId = new Identifier(schemeCableThread.linkId);
			this.sourceSchemePortId = new Identifier(schemeCableThread.sourceSchemePortId);
			this.targetSchemePortId = new Identifier(schemeCableThread.targetSchemePortId);
			this.parentSchemeCableLinkId = new Identifier(schemeCableThread.parentSchemeCableLinkId);

			this.linkTypeSet = true;
		}
	}

	/**
	 * @param schemeCableThread
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeCableThread schemeCableThread,
			final String importType)
	throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(schemeCableThread, SCHEMECABLETHREAD_CODE, importType, PRE_IMPORT);

		this.name = schemeCableThread.getName();
		this.description = schemeCableThread.isSetDescription()
				? schemeCableThread.getDescription()
				: "";

		final boolean setLinkTypeId = schemeCableThread.isSetLinkTypeId();
		final boolean setLinkId = schemeCableThread.isSetLinkId();
		if (setLinkTypeId) {
			assert !setLinkId : OBJECT_STATE_ILLEGAL;

			this.linkTypeId = Identifier.fromXmlTransferable(schemeCableThread.getLinkTypeId(), importType, MODE_THROW_IF_ABSENT);
			this.linkId = VOID_IDENTIFIER;
		} else if (setLinkId) {
			assert !setLinkTypeId : OBJECT_STATE_ILLEGAL;

			this.linkTypeId = VOID_IDENTIFIER;
			this.linkId = Identifier.fromXmlTransferable(schemeCableThread.getLinkId(), importType, MODE_THROW_IF_ABSENT);
		} else {
			throw new UpdateObjectException(
					"SchemeCableThread.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}

		this.sourceSchemePortId = schemeCableThread.isSetSourceSchemePortId()
				? Identifier.fromXmlTransferable(schemeCableThread.getSourceSchemePortId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.targetSchemePortId = schemeCableThread.isSetTargetSchemePortId()
				? Identifier.fromXmlTransferable(schemeCableThread.getTargetSchemePortId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.parentSchemeCableLinkId = Identifier.fromXmlTransferable(schemeCableThread.getParentSchemeCableLinkId(), importType, MODE_THROW_IF_ABSENT);
		if (schemeCableThread.isSetCharacteristics()) {
			for (final XmlCharacteristic characteristic : schemeCableThread.getCharacteristics().getCharacteristicArray()) {
				Characteristic.createInstance(super.creatorId, characteristic, importType);
			}
		}

		this.linkTypeSet = true;

		XmlComplementorRegistry.complementStorableObject(schemeCableThread, SCHEMECABLETHREAD_CODE, importType, POST_IMPORT);
	}

	void setLinkTypeId(Identifier linkTypeId) {
		//TODO: inroduce additional sanity checks
		assert linkTypeId != null : NON_NULL_EXPECTED;
		assert linkTypeId.isVoid() || linkTypeId.getMajor() == LINK_TYPE_CODE;
		this.linkTypeId = linkTypeId;
		super.markAsChanged();
	}

	void setLinkId(Identifier linkId) {
		//TODO: inroduce additional sanity checks
		assert linkId != null : NON_NULL_EXPECTED;
		assert linkId.isVoid() || linkId.getMajor() == LINK_CODE;
		this.linkId = linkId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeCableThreadWrapper getWrapper() {
		return SchemeCableThreadWrapper.getInstance();
	}

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertLinkTypeSetNonStrict() {
		if (this.linkTypeSet) {
			return this.assertLinkTypeSetStrict();
		}
		this.linkTypeSet = true;
		return this.linkId != null
				&& this.linkTypeId != null
				&& this.linkId.isVoid()
				&& this.linkTypeId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertLinkTypeSetStrict() {
		return this.linkId != null
				&& this.linkTypeId != null
				&& (this.linkId.isVoid() ^ this.linkTypeId.isVoid());
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
}
