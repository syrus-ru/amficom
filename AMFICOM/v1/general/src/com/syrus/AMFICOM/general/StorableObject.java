/*
 * $Id: StorableObject.java,v 1.46 2005/03/21 16:43:46 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
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
 * @version $Revision: 1.46 $, $Date: 2005/03/21 16:43:46 $
 * @module general_v1
 */
public abstract class StorableObject implements Identifiable, TransferableObject, Serializable {
	private static final long serialVersionUID = 3904998894075738999L;

	private static final long VERSION_ILLEGAL = -1L;

	protected Identifier id;
	protected Date created;
	protected Identifier creatorId;
	protected Date modified;
	protected Identifier modifierId;
	protected long version;

	protected boolean changed;

	private Date savedModified;
	private Identifier savedModifierId;
	private long savedVersion;

	/**
	 * Server-side constructor.
	 *
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
			final long version) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;

		this.changed = false;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = 0;
	}

	protected StorableObject(StorableObject_Transferable sot) {
		this.id = new Identifier(sot.id);
		this.created = new Date(sot.created);
		this.modified = new Date(sot.modified);
		this.creatorId = new Identifier(sot.creator_id);
		this.modifierId = new Identifier(sot.modifier_id);
		this.version = sot.version;

		this.changed = false;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = 0;
	}

	public final Date getCreated() {
		return this.created;
	}

	public final Identifier getCreatorId() {
		return this.creatorId;
	}

	/**
	 * Will be overridden by descendants.
	 */
	public abstract List getDependencies();

	/**
	 * Returns structure to be transmitted via CORBA. Should be declared
	 * final as soon as <code>Marker</code>, <code>UnboundLink</code> and
	 * <code>UnboundNode</code> stop overriding it.
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		return new StorableObject_Transferable((Identifier_Transferable) this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				(Identifier_Transferable) this.creatorId.getTransferable(),
				(Identifier_Transferable) this.modifierId.getTransferable(),
				this.version);
	}

	/**
	 * This method called only when client succesfully updated object
	 * @param sot
	 */
	public final void updateFromHeaderTransferable(StorableObject_Transferable sot) {
		this.modified = new Date(sot.modified);
		this.modifierId = new Identifier(sot.modifier_id);
		if (this.version != sot.version)
			this.changed = false;
		this.version = sot.version;
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

	public final long getVersion() {
		return this.version;
	}

	/**
	 * Returns <code>true</code> if object was changed locally (with respect
	 * to server).
	 */
	public final boolean isChanged() {
		return this.changed;
	}

	protected final void setUpdated(Identifier modifierId) {
		this.savedModified = this.modified;
		this.savedModifierId = this.modifierId;
		this.savedVersion = this.version;

		this.modified = new Date(System.currentTimeMillis());
		this.modifierId = modifierId;
		this.incrementVersion();
		this.changed = false;
	}

	protected final void cleanupUpdate() {
		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = VERSION_ILLEGAL;
	}

	protected final void rollbackUpdate() {
		if (this.savedModified == null || this.savedModifierId == null || this.savedVersion == VERSION_ILLEGAL) {
			Log.errorMessage("Cannot rollback update of object: '" + this.id + "', entity: '" + ObjectEntities.codeToString(this.id.getMajor())  //$NON-NLS-1$//$NON-NLS-2$
					+ "' -- saved values are in illegal states!"); //$NON-NLS-1$
			return;
		}

		this.modified = this.savedModified;
		this.modifierId = this.savedModifierId;
		this.version = this.savedVersion;
		this.changed = true;

		this.cleanupUpdate();
	}

	private final void incrementVersion() {
		if (this.version < Long.MAX_VALUE) {
			this.version++;
			if (this.version == VERSION_ILLEGAL)
				this.version++;
		}
		else
			this.version = Long.MIN_VALUE;
	}

	public final boolean hasNewerVersion(long version1) {
		if (Math.abs(this.version - version1) < (Long.MAX_VALUE - Long.MIN_VALUE) / 2)
			return (this.version > version1);
		return (this.version < version1);
	}

	public final boolean hasOlderVersion(long version1) {
		if (Math.abs(this.version - version1) < (Long.MAX_VALUE - Long.MIN_VALUE) / 2)
			return (this.version < version1);
		return (this.version > version1);
	}

	/**
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
			final long version) {
		assert created != null && modified != null && creatorId != null
				&& modifierId != null;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	protected Object clone() throws CloneNotSupportedException {
		final StorableObject clone = (StorableObject) super.clone();
		clone.id = IdentifierPool.getGeneratedIdentifier0(this.id.getMajor());

		final Date cloneCreated = new Date();
		clone.created = cloneCreated;
		clone.modified = cloneCreated;

		/**
		 * @todo Initialize creatorId and modifierId with values from
		 *       current session, if this session is available. In the
		 *       general case, this can't be done.
		 */
		clone.creatorId = this.creatorId;
		clone.modifierId = this.modifierId;

		/*
		 * Initialize version vith 0L, like for all newly created
		 * objects.
		 */
		clone.version = 0L;
		clone.changed = true;
		return clone;
	}
}
