package com.syrus.AMFICOM.general;

import java.util.Date;

public abstract class StorableObject implements Identified, TransferableObject {

	protected Identifier	id;
	protected Date			created;
	protected Date			modified;
	protected Identifier	creatorId;
	protected Identifier	modifierId;
	protected long			version			= 0;
	protected long			currentVersion	= 0;

	protected StorableObject(Identifier id) {
		this.id = id;
	}

	protected StorableObject(Identifier id, Date created, Date modified, Identifier creatorId, Identifier modifierId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
	}

	public Identifier getId() {
		return this.id;
	}

	public Date getCreated() {
		return this.created;
	}

	public Date getModified() {
		return this.modified;
	}

	public Identifier getCreatorId() {
		return this.creatorId;
	}

	public Identifier getModifierId() {
		return this.modifierId;
	}

	public boolean isChanged() throws VersionCollisionException {
		boolean result = (this.currentVersion != this.version);
		/**
		 * @todo: check version at DB, if object was deleted or DB version isn't
		 *        equal local version throws VersionCollisionException
		 */
		return result;
	}
	
	protected long getNextVersion(){
		/**
		 * @todo recast to another getting modified version
		 */
		return this.version + 1;
	}

	protected synchronized void setAttributes(Date created, Date modified, Identifier creatorId, Identifier modifierId) {
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
	}
}