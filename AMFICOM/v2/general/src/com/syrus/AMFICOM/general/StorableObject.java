package com.syrus.AMFICOM.general;

import java.util.Date;
import org.omg.CORBA.portable.IDLEntity;

public abstract class StorableObject implements TransferableObject {
	protected Identifier id;
	protected Date created;
	protected Date modified;
	protected Identifier creator_id;
	protected Identifier modifier_id;

	public StorableObject(Identifier id) {
		this.id = id;
	}

	protected StorableObject(Identifier id,
													 Date created,
													 Date modified,
													 Identifier creator_id,
													 Identifier modifier_id) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creator_id = creator_id;
		this.modifier_id = modifier_id;
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
		return this.creator_id;
	}

	public Identifier getModifierId() {
		return this.modifier_id;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id) {
		this.created = created;
		this.modified = modified;
		this.creator_id = creator_id;
		this.modifier_id = modifier_id;
	}
}