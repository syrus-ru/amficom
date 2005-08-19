/*-
 * $Id: SchemeCableThread.java,v 1.67 2005/08/19 16:11:14 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableThread;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableThreadHelper;
import com.syrus.util.Log;

/**
 * #14 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.67 $, $Date: 2005/08/19 16:11:14 $
 * @module scheme
 */
public final class SchemeCableThread extends AbstractCloneableStorableObject
		implements Describable, Characterizable, ReverseDependencyContainer {
	private static final long serialVersionUID = 4050204133015171124L;

	private String name;

	private String description;

	private Identifier cableThreadTypeId;

	private Identifier linkId;

	Identifier sourceSchemePortId;

	Identifier targetSchemePortId;

	Identifier parentSchemeCableLinkId;

	private transient CharacterizableDelegate characterizableDelegate;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCableThread(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		try {
			DatabaseContext.getDatabase(SCHEMECABLETHREAD_CODE).retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
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
	 * @param cableThreadType
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
			final CableThreadType cableThreadType,
			final Link link,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeCableLink parentSchemeCableLink) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.cableThreadTypeId = Identifier.possiblyVoid(cableThreadType);
		this.linkId = Identifier.possiblyVoid(link);
		this.sourceSchemePortId = Identifier.possiblyVoid(sourceSchemePort);
		this.targetSchemePortId = Identifier.possiblyVoid(targetSchemePort);
		this.parentSchemeCableLinkId = Identifier.possiblyVoid(parentSchemeCableLink);
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
	 * {@link #createInstance(Identifier, String, String, CableThreadType, Link, SchemePort, SchemePort, SchemeCableLink)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param cableThreadType
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static SchemeCableThread createInstance(final Identifier creatorId, final String name, final CableThreadType cableThreadType, final SchemeCableLink parentSchemeCableLink) throws CreateObjectException {
		return createInstance(creatorId, name, "", cableThreadType,
				null, null, null, parentSchemeCableLink);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param cableThreadType
	 * @param link
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeCableLink
	 * @throws CreateObjectException
	 */
	public static SchemeCableThread createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final CableThreadType cableThreadType,
			final Link link,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeCableLink parentSchemeCableLink) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert cableThreadType != null : NON_NULL_EXPECTED;
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
					cableThreadType,
					link,
					sourceSchemePort,
					targetSchemePort,
					parentSchemeCableLink);
			schemeCableThread.markAsChanged();
			return schemeCableThread;
		} catch (final IdentifierGenerationException ioee) {
			throw new CreateObjectException("SchemeCableThread.createInstance | cannot generate identifier ", ioee);
		}
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public SchemeCableThread clone() throws CloneNotSupportedException {
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
			final SchemeCableThread clone = (SchemeCableThread) super.clone();

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

	Identifier getCableThreadTypeId() {
		assert this.cableThreadTypeId != null && !this.cableThreadTypeId.isVoid(): OBJECT_BADLY_INITIALIZED;
		assert this.cableThreadTypeId.getMajor() == CABLETHREAD_TYPE_CODE;
		return this.cableThreadTypeId;
	}
	
	/**
	 * A wrapper around {@link #getCableThreadTypeId()}.
	 */
	public CableThreadType getCableThreadType() {
		try {
			return StorableObjectPool.getStorableObject(this.getCableThreadTypeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
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
		assert this.cableThreadTypeId != null && this.linkId != null
				&& this.sourceSchemePortId != null
				&& this.targetSchemePortId != null
				&& this.parentSchemeCableLinkId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.cableThreadTypeId);
		dependencies.add(this.linkId);
		dependencies.add(this.sourceSchemePortId);
		dependencies.add(this.targetSchemePortId);
		dependencies.add(this.parentSchemeCableLinkId);
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
		assert this.linkId != null: OBJECT_NOT_INITIALIZED;
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

	Identifier getSourceSchemePortId() {
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
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
				this.cableThreadTypeId.getTransferable(),
				this.linkId.getTransferable(),
				this.sourceSchemePortId.getTransferable(),
				this.targetSchemePortId.getTransferable(),
				this.parentSchemeCableLinkId.getTransferable());
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param cableThreadTypeId
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
			final Identifier cableThreadTypeId,
			final Identifier linkId,
			final Identifier parentSchemeCableLinkId,
			final Identifier sourceSchemePortId,
			final Identifier targetSchemePortId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert cableThreadTypeId != null && !cableThreadTypeId.isVoid() : NON_VOID_EXPECTED;
		assert linkId != null;
		assert sourceSchemePortId != null : NON_NULL_EXPECTED;
		assert targetSchemePortId != null : NON_NULL_EXPECTED;
		assert parentSchemeCableLinkId != null : NON_NULL_EXPECTED; 
		assert !parentSchemeCableLinkId.isVoid() : NON_VOID_EXPECTED;

		this.name = name;
		this.description = description;
		this.cableThreadTypeId = cableThreadTypeId;
		this.linkId = linkId;
		this.sourceSchemePortId = sourceSchemePortId;
		this.targetSchemePortId = targetSchemePortId;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
	}

	/**
	 * @param cableThreadType
	 */
	public void setCableThreadType(final CableThreadType cableThreadType) {
		assert this.cableThreadTypeId != null && !this.cableThreadTypeId.isVoid(): OBJECT_BADLY_INITIALIZED;
		assert cableThreadType != null: NON_NULL_EXPECTED;

		final Identifier newCableThreadTypeId = cableThreadType.getId();
		if (this.cableThreadTypeId.equals(newCableThreadTypeId))
			return;
		this.cableThreadTypeId = newCableThreadTypeId;
		super.markAsChanged();
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

	public void setLink(final Link link) {
		final Identifier newLinkId = Identifier.possiblyVoid(link);
		if (this.linkId.equals(newLinkId))
			return;
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
	 * @param parentSchemeCableLink
	 */
	public void setParentSchemeCableLink(final SchemeCableLink parentSchemeCableLink) {
		assert this.parentSchemeCableLinkId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeCableLinkId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeCableLink == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		final Identifier newParentSchemeCableLinkId = parentSchemeCableLink.getId();
		if (this.parentSchemeCableLinkId.equals(newParentSchemeCableLinkId))
			return;
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
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlSchemeCableThread schemeCableThread = (IdlSchemeCableThread) transferable;
		try {
			super.fromTransferable(schemeCableThread);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = schemeCableThread.name;
		this.description = schemeCableThread.description;
		this.cableThreadTypeId = new Identifier(schemeCableThread.cableThreadTypeId);
		this.linkId = new Identifier(schemeCableThread.linkId);
		this.sourceSchemePortId = new Identifier(schemeCableThread.sourceSchemePortId);
		this.targetSchemePortId = new Identifier(schemeCableThread.targetSchemePortId);
		this.parentSchemeCableLinkId = new Identifier(schemeCableThread.parentSchemeCableLinkId);
	}

	void setCableThreadTypeId(Identifier cableThreadTypeId) {
		//TODO: inroduce additional sanity checks
		assert cableThreadTypeId != null : NON_NULL_EXPECTED;
		assert cableThreadTypeId.isVoid() || cableThreadTypeId.getMajor() == CABLETHREAD_TYPE_CODE;
		this.cableThreadTypeId = cableThreadTypeId;
		super.markAsChanged();
	}

	void setLinkId(Identifier linkId) {
		//TODO: inroduce additional sanity checks
		assert linkId != null : NON_NULL_EXPECTED;
		assert linkId.isVoid() || linkId.getMajor() == LINK_CODE;
		this.linkId = linkId;
		super.markAsChanged();
	}

	void setParentSchemeCableLinkId(Identifier parentSchemeCableLinkId) {
//		TODO: inroduce additional sanity checks
		assert parentSchemeCableLinkId != null : NON_NULL_EXPECTED;
		assert parentSchemeCableLinkId.isVoid() || parentSchemeCableLinkId.getMajor() == SCHEMECABLELINK_CODE;
		this.parentSchemeCableLinkId = parentSchemeCableLinkId;
		super.markAsChanged();
	}
}
