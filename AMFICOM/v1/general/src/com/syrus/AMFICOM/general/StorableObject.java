package com.syrus.AMFICOM.general;

import java.util.Date;

public abstract class StorableObject implements Identified, TransferableObject {
	protected Identifier id;
	protected Date created;
	protected Date modified;
	protected Identifier creatorId;
	protected Identifier modifierId;

	protected StorableObject(Identifier id) {
		this.id = id;
	}

	protected StorableObject(Identifier id,
													 Date created,
													 Date modified,
													 Identifier creatorId,
													 Identifier modifierId) {
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

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId) {
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
	}
}
