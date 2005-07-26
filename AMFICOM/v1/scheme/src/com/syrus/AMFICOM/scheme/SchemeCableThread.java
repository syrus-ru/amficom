/*-
 * $Id: SchemeCableThread.java,v 1.53 2005/07/26 12:03:49 bass Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableThread;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableThreadHelper;
import com.syrus.util.Log;

/**
 * #14 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.53 $, $Date: 2005/07/26 12:03:49 $
 * @module scheme
 */
public final class SchemeCableThread extends StorableObject
		implements Describable, Characterizable, Cloneable {
	private static final long serialVersionUID = 4050204133015171124L;

	private String name;

	private String description;

	private Identifier cableThreadTypeId;

	private Identifier linkId;

	Identifier sourceSchemePortId;

	Identifier targetSchemePortId;

	Identifier parentSchemeCableLinkId;

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
	SchemeCableThread(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final CableThreadType cableThreadType, final Link link,
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
	public static SchemeCableThread createInstance(
			final Identifier creatorId, final String name,
			final String description,
			final CableThreadType cableThreadType, final Link link,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeCableLink parentSchemeCableLink)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert cableThreadType != null: NON_NULL_EXPECTED;
		assert parentSchemeCableLink != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCableThread schemeCableThread = new SchemeCableThread(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMECABLETHREAD_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, cableThreadType,
					link, sourceSchemePort,
					targetSchemePort, parentSchemeCableLink);
			schemeCableThread.markAsChanged();
			return schemeCableThread;
		} catch (final IdentifierGenerationException ioee) {
			throw new CreateObjectException(
					"SchemeCableThread.createInstance | cannot generate identifier ", ioee);
		}
	}

	@Override
	public SchemeCableThread clone() throws CloneNotSupportedException {
		final SchemeCableThread schemeCableThread = (SchemeCableThread) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableThread;
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
			return (CableThreadType) StorableObjectPool.getStorableObject(this.getCableThreadTypeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see Characterizable#getCharacteristics()
	 */
	public Set<Characteristic> getCharacteristics() {
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
			return (Link) StorableObjectPool.getStorableObject(this.getLinkId(), true);
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
			return (SchemeCableLink) StorableObjectPool.getStorableObject(this.getParentSchemeCableLinkId(), true);
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
		final Identifier sourceSchemeDeviceId = sourceSchemePort
				.getParentSchemeDevice().getId();
		final Identifier targetSchemeDeviceId = targetSchemePort
				.getParentSchemeDevice().getId();
		final Identifier schemeDeviceId = schemeDevice.getId();
		if (schemeDeviceId.equals(sourceSchemeDeviceId))
			return sourceSchemePort;
		else if (schemeDeviceId.equals(targetSchemeDeviceId))
			return targetSchemePort;
		else
			throw new IllegalArgumentException(
					"This scheme cable thread is in no way connected to the scheme device specified.");
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
			return (SchemePort) StorableObjectPool.getStorableObject(this.getSourceSchemePortId(), true);
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
			return (SchemePort) StorableObjectPool.getStorableObject(this.getTargetSchemePortId(), true);
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
				this.version, this.name,
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
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Identifier cableThreadTypeId,
			final Identifier linkId,
			final Identifier sourceSchemePortId,
			final Identifier targetSchemePortId,
			final Identifier parentSchemeCableLinkId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert cableThreadTypeId != null && !cableThreadTypeId.isVoid() : NON_VOID_EXPECTED;
		assert linkId != null;
		assert sourceSchemePortId != null: NON_NULL_EXPECTED;
		assert targetSchemePortId != null: NON_NULL_EXPECTED;
		assert parentSchemeCableLinkId != null && !parentSchemeCableLinkId.isVoid() : NON_VOID_EXPECTED;

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

	public void setSourceSchemePort(final SchemePort sourceSchemePort) {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.sourceSchemePortId.isVoid()
				|| !this.sourceSchemePortId.equals(this.targetSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		final Identifier newSourceSchemePortId = Identifier.possiblyVoid(sourceSchemePort);
		assert newSourceSchemePortId.isVoid()
				|| !newSourceSchemePortId.equals(this.targetSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		if (this.sourceSchemePortId.equals(newSourceSchemePortId))
			return;
		this.sourceSchemePortId = newSourceSchemePortId;
		super.markAsChanged();
	}

	public void setTargetSchemePort(final SchemePort targetSchemePort) {
		assert this.sourceSchemePortId != null
				&& this.targetSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.targetSchemePortId.isVoid()
				|| !this.targetSchemePortId.equals(this.sourceSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		final Identifier newTargetSchemePortId = Identifier.possiblyVoid(targetSchemePort);
		assert newTargetSchemePortId.isVoid()
				|| !newTargetSchemePortId.equals(this.sourceSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		if (this.targetSchemePortId.equals(newTargetSchemePortId))
			return;
		this.targetSchemePortId = newTargetSchemePortId;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
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
}
