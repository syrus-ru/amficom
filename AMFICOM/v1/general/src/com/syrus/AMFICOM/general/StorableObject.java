/*
 * $Id: StorableObject.java,v 1.34 2005/02/22 11:32:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.34 $, $Date: 2005/02/22 11:32:58 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class StorableObject implements Identified, TransferableObject, Serializable {
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

	protected StorableObject(Identifier id) {
		this.id = id;

		this.changed = false;
	}

	protected StorableObject(Identifier id, Date created, Date modified, Identifier creatorId, Identifier modifierId, long version) {
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

//	/**
//	 * @deprecated use
//	 *             com.syrus.AMFICOM.general.corba.StorableObjectDatabase#update()
//	 *             instead
//	 */
//	public abstract void insert() throws CreateObjectException;

	/**
	 * @see com.syrus.AMFICOM.general.corba.StorableObject#created()
	 */
	public Date getCreated() {
		return this.created;
	}

	public Identifier getCreatorId() {
		return this.creatorId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.StorableObject#dependencies()
	 */
	public abstract List getDependencies();

	/**
	 * Return structure to transmit throw CORBA
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
	public void updateFromHeaderTransferable(StorableObject_Transferable sot) {
		this.modified = new Date(sot.modified);
		this.modifierId = new Identifier(sot.modifier_id);
		if (this.version != sot.version)
			this.changed = false;
		this.version = sot.version;
	}

	public Identifier getId() {
		return this.id;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.StorableObject#modified()
	 */
	public Date getModified() {
		return this.modified;
	}

	public Identifier getModifierId() {
		return this.modifierId;
	}

	public long getVersion() {
		return this.version;
	}

	/**
	 * Returns <code>true</code> if object was locally changed with respect to
	 * server.
	 */
	public boolean isChanged() {
		return this.changed;
	}

	protected void setUpdated(Identifier modifierId) {
		this.savedModified = this.modified;
		this.savedModifierId = this.modifierId;
		this.savedVersion = this.version;

		this.modified = new Date(System.currentTimeMillis());
		this.modifierId = modifierId;
		this.incrementVersion();
		this.changed = false;
	}

	protected void cleanupUpdate() {
		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = VERSION_ILLEGAL;
	}

	protected void rollbackUpdate() {
		if (this.savedModified == null || this.savedModifierId == null || this.savedVersion == VERSION_ILLEGAL) {
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

	private void incrementVersion() {
		if (this.version < Long.MAX_VALUE) {
			this.version++;
			if (this.version == VERSION_ILLEGAL)
				this.version++;
		}
		else
			this.version = Long.MIN_VALUE;
	}

	public boolean hasNewerVersion(long version1) {
		if (Math.abs(this.version - version1) < (Long.MAX_VALUE - Long.MIN_VALUE) / 2)
			return (this.version > version1);
		return (this.version < version1);
	}

	public boolean hasOlderVersion(long version1) {
		if (Math.abs(this.version - version1) < (Long.MAX_VALUE - Long.MIN_VALUE) / 2)
			return (this.version < version1);
		return (this.version > version1);
	}

	protected synchronized void setAttributes(Date created, Date modified, Identifier creatorId, Identifier modifierId, long version) {
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}
}
