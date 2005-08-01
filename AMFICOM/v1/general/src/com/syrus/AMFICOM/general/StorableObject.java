/*
 * $Id: StorableObject.java,v 1.85 2005/08/01 16:18:10 bass Exp $
 *
 * Copyright ø 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectHelper;
import com.syrus.util.Log;

/**
 * {@link Object#equals(Object)}and {@link Object#hashCode()} methods are
 * intentionally unimplemented, as it seems reasonable to compare only object
 * identifiers (which serve just as object references in
 * <em>Storable Object Framework</em>). Since within a single VM instance
 * there can only be a single inctance of <code>StorableObject</code> with the
 * same identifier, comparison of object references (in Java terms) is enough.
 *
 * @author $Author: bass $
 * @version $Revision: 1.85 $, $Date: 2005/08/01 16:18:10 $
 * @module general
 */
public abstract class StorableObject implements Identifiable, TransferableObject, Serializable {
	private static final long serialVersionUID = 3904998894075738999L;

	protected Identifier id;
	protected Date created;
	protected Identifier creatorId;
	protected Date modified;
	protected Identifier modifierId;
	protected StorableObjectVersion version;

	private boolean changed;

	private Date savedModified;
	private Identifier savedModifierId;
	private StorableObjectVersion savedVersion;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected StorableObject() {
		// £Õ–‘… Œ¡»
	}
	/**
	 * Server-side constructor.
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param id
	 */
	protected StorableObject(final Identifier id) {
		this.id = id;

		this.changed = false;
	}

	/**
	 * Client-side constructor.
	 *
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected StorableObject(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;

		this.changed = false;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = StorableObjectVersion.ILLEGAL_VERSION;
	}
	
	/**
	 *
	 * Will be overridden by descendants.
	 *
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unused")
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		this.id = new Identifier(transferable.id);
		this.created = new Date(transferable.created);
		this.modified = new Date(transferable.modified);
		this.creatorId = new Identifier(transferable.creatorId);
		this.modifierId = new Identifier(transferable.modifierId);
		this.version = new StorableObjectVersion(transferable.version);

		this.changed = false;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = StorableObjectVersion.ILLEGAL_VERSION;		
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @return <code>true</code> if storable object is valid, <code>false</code> otherwise
	 */
	protected boolean isValid() {
		return !(this.id == null || this.created == null || this.modified == null || this.creatorId == null || this.modifierId == null);
	}

	public final Date getCreated() {
		return this.created;
	}

	public final Identifier getCreatorId() {
		return this.creatorId;
	}

	/**
	 * Will be overridden by descendants.
	 *
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public abstract Set<Identifiable> getDependencies();

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlStorableObject getTransferable(final ORB orb) {
		return IdlStorableObjectHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue());
	}

	/**
	 * @see Identifiable#getId()
	 */
	public final Identifier getId() {
		return this.id;
	}

	public final Date getModified() {
		return this.modified;
	}

	public final Identifier getModifierId() {
		return this.modifierId;
	}

	public final StorableObjectVersion getVersion() {
		return this.version;
	}

	/**
	 * Returns <code>true</code> if object was changed locally (with respect
	 * to server).
	 */
	public final boolean isChanged() {
		return this.changed;
	}

	/**
	 * This method is called in:
	 * 1) all setters of a StorableObject
	 * 2) static method createInstance of StorableObject
	 * i. e., in all methods, which change state of an object.
	 * Subsequent call to StorableObjectPool.flush will save this changed object.
	 *
	 */
	protected final void markAsChanged() {
		this.changed = true;
		try {
			StorableObjectPool.putStorableObject(this);
		} catch (IllegalObjectEntityException ioee) {
			assert false : ioee.getMessage();
		}
	}

	protected final void setUpdated(final Identifier modifierId) {
		this.savedModified = this.modified;
		this.savedModifierId = this.modifierId;
		this.savedVersion = this.version.clone();

		this.modified = new Date(System.currentTimeMillis());
		this.modifierId = modifierId;
		this.version.increment();
		this.changed = false;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void cleanupUpdate() {
		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = StorableObjectVersion.ILLEGAL_VERSION;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void rollbackUpdate() {
		if (this.savedModified == null || this.savedModifierId == null || this.savedVersion == StorableObjectVersion.ILLEGAL_VERSION) {
			Log.errorMessage("Cannot rollback update of object: '" + this.id + "', entity: '" + ObjectEntities.codeToString(this.id.getMajor())
					+ "' -- saved values are in illegal states!");
			return;
		}

		this.modified = this.savedModified;
		this.modifierId = this.savedModifierId;
		this.version = this.savedVersion;
		this.changed = true;

		this.cleanupUpdate();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected final synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		assert created != null && modified != null && creatorId != null
				&& modifierId != null;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	/**
	 * for descendsants that inherit {@link CloneableStorableObject} and
	 * thus <em>do support</em> cloning, {@link #creatorId} and
	 * {@link #modifierId} of a newly created object are set to the same
	 * values as those of the original one. 
	 *
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	protected StorableObject clone() throws CloneNotSupportedException {
		final StorableObject clone = (StorableObject) super.clone();
		try {
			clone.id = IdentifierPool.getGeneratedIdentifier(this.id.getMajor());
		} catch (final IdentifierGenerationException ige) {
			throw new RuntimeException("Cannot generate identifier", ige);
		}

		final Date cloneCreated = new Date();
		clone.created = cloneCreated;
		clone.modified = cloneCreated;

		/*
		 * Initialize version vith 0L, like for all newly created
		 * objects.
		 */
		clone.version = StorableObjectVersion.createInitial();
		clone.markAsChanged();
		return clone;
	}

	/**
	 * @param that
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that instanceof StorableObject) {
			return this.id.equals(((StorableObject) that).id);
		}
		return false;
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	public static final IdlStorableObject[] createTransferables(final Set<? extends StorableObject> storableObjects, final ORB orb) {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;

		final IdlStorableObject[] transferables = new IdlStorableObject[storableObjects.size()];
		int i = 0;
		for (final StorableObject storableObject : storableObjects) {
			transferables[i++] = storableObject.getTransferable(orb);
		}
		return transferables;
	}

	@SuppressWarnings("unchecked")
	public static final <T extends StorableObject> Set<T> fromTransferables(final IdlStorableObject[] storableObjectsT) {
		assert storableObjectsT != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set<T> storableObjects = new HashSet<T>();
		for (final IdlStorableObject idlStorableObject : storableObjectsT) {
			try {
				storableObjects.add((T) idlStorableObject.getNative());
			} catch (IdlCreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return storableObjects;
	}

	public static final Map<Identifier, StorableObjectVersion> createVersionsMap(final Set<? extends StorableObject> storableObjects) {
		final Map<Identifier, StorableObjectVersion> versionsMap = new HashMap<Identifier, StorableObjectVersion>();
		for (final StorableObject storableObject : storableObjects) {
			versionsMap.put(storableObject.id, storableObject.version);
		}
		return versionsMap;
	}

	/**
	 * This method should only be invoked during assertion evaluation, and
	 * never in a release system.
	 *
	 * @param identifiables non-null set of identifiables (empty set is ok).
	 * @return <code>true</code> if all entities within this set are of
	 *         the same type, <code>false</code> otherwise.
	 */
	public static final boolean hasSingleTypeEntities(final Set<? extends Identifiable> identifiables) {
		/*
		 * Nested assertions are ok.
		 */
		assert identifiables != null;

		if (identifiables.isEmpty()) {
			return true;
		}

		short entityCode = VOID_IDENTIFIER.getMajor();
		final Iterator<? extends Identifiable> identifiableIterator = identifiables.iterator();
		while (identifiableIterator.hasNext()) {
			final Identifier id = identifiableIterator.next().getId();
			if (id.isVoid()) {
				continue;
			}
			entityCode = id.getMajor();
			break;
		}
		while (identifiableIterator.hasNext()) {
			final Identifier id = identifiableIterator.next().getId();
			if (id.isVoid()) {
				continue;
			}
			if (entityCode != id.getMajor()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see #hasSingleTypeEntities(Set)
	 */
	public static final boolean hasSingleTypeEntities(final IdlIdentifier ids[]) {
		assert ids != null;

		final int length = ids.length;
		if (length == 0) {
			return true;
		}

		short entityCode = VOID_IDENTIFIER.getMajor();
		int i = 0;
		for (; i < length; i++) {
			final Identifier id = new Identifier(ids[i]);
			if (id.isVoid()) {
				continue;
			}
			entityCode = id.getMajor();
			break;
		}
		for (; i < length; i++) {
			final Identifier id = new Identifier(ids[i]);
			if (id.isVoid()) {
				continue;
			}
			if (entityCode != id.getMajor()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method should only be invoked during assertion evaluation, and
	 * never in a release system.
	 *
	 * @param identifiables non-null set of identifiables (empty set is ok).
	 * @return <code>true</code> if all entities within this set belong to
	 *         the same group, <code>false</code> otherwise.
	 */
	public static final boolean hasSingleGroupEntities(final Set<? extends Identifiable> identifiables) {
		/*
		 * Nested assertions are ok.
		 */
		assert identifiables != null;

		if (identifiables.isEmpty()) {
			return true;
		}

		final Iterator<? extends Identifiable> identifiableIterator = identifiables.iterator();
		final short groupCode = ObjectGroupEntities.getGroupCode(identifiableIterator.next().getId().getMajor());
		while (identifiableIterator.hasNext()) {
			if (groupCode != ObjectGroupEntities.getGroupCode(identifiableIterator.next().getId().getMajor())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method checks:
	 * 1) for non-null and non-empty set,
	 * 2) for the same entity code of all elements in set (@see #hasSingleTypeEntities(Set)).
	 *
	 * @param identifiables non-null, non-empty set of storable objects or
	 *        identifiers of the same type.
	 * @return common type of identifiables supplied as
	 *         <code>short</code>.
	 */
	public static final short getEntityCodeOfIdentifiables(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null && !identifiables.isEmpty();
		assert hasSingleTypeEntities(identifiables) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		for (final Identifiable identifiable : identifiables) {
			final Identifier id = identifiable.getId();
			if (id.isVoid()) {
				continue;
			}
			return id.getMajor();
		}
		return VOID_IDENTIFIER.getMajor();
	}

	/**
	 * @see #getEntityCodeOfIdentifiables(Set)
	 */
	public static final short getEntityCodeOfIdentifiables(final IdlIdentifier ids[]) {
		assert ids != null && ids.length != 0;
		assert hasSingleTypeEntities(ids) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		for (final IdlIdentifier idlIdentifier : ids) {
			final Identifier id = new Identifier(idlIdentifier);
			if (id.isVoid()) {
				continue;
			}
			return id.getMajor();
		}
		return VOID_IDENTIFIER.getMajor();
	}

	/**
	 * Code that invokes this method, should preliminarily call
	 * {@link #hasSingleGroupEntities(Set)} with the same parameter and
	 * ensure that return value is <code>true</code>, e.g.:
	 *
	 * <pre>
	 * assert hasSingleGroupEntities(identifiables) : &quot;Identifiables of different group should be treated separately...&quot;;
	 * </pre>
	 * @param identifiables non-null, non-empty set of storable objects or
	 *        identifiers of the same type.
	 * @return common group of identifiables supplied as
	 *         <code>short</code>.
	 */
	public static final short getGroupCodeOfIdentifiables(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null && !identifiables.isEmpty();
		assert hasSingleGroupEntities(identifiables) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_GROUP;

		return ObjectGroupEntities.getGroupCode(identifiables.iterator().next().getId().getMajor());
	}

}
