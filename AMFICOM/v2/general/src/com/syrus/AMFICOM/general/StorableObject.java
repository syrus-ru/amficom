package com.syrus.AMFICOM.general;

import java.util.Date;
import org.omg.CORBA.portable.IDLEntity;

public abstract class StorableObject implements TransferableObject {
	protected Identifier id;
	protected Date created;
	protected Date modified;

	public StorableObject(Identifier id) {
		this.id = id;
		this.created = null;
		this.modified = null;
	}

	public StorableObject(Identifier id,
												Date created,
												Date modified) {
		this.id = id;
		this.created = created;
		this.modified = modified;
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

	protected void setAttributes(Date created,
															 Date modified) {
		this.created = created;
		this.modified = modified;
	}
}