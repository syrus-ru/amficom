package com.syrus.AMFICOM.general;

import org.omg.CORBA.portable.IDLEntity;

public abstract class StorableObject implements TransferableObject {
	protected Identifier id;

	public StorableObject(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return this.id;
	}
}