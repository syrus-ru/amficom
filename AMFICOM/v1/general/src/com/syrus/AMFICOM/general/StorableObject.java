/*
 * $Id: StorableObject.java,v 1.26 2005/02/10 13:40:30 arseniy Exp $
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

/**
 * @version $Revision: 1.26 $, $Date: 2005/02/10 13:40:30 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class StorableObject implements Identified, TransferableObject, Serializable {
	private static final long serialVersionUID = -1720579921164397193L;

	protected Identifier id;
	protected Date created;
	protected Identifier creatorId;
	protected Date modified;
	protected Identifier modifierId;
	protected long version;

	protected boolean changed;

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
	}

	protected StorableObject(StorableObject_Transferable sot) {
		this.id = new Identifier(sot.id);
		this.created = new Date(sot.created);
		this.modified = new Date(sot.modified);
		this.creatorId = new Identifier(sot.creator_id);
		this.modifierId = new Identifier(sot.modifier_id);
		this.version = sot.version;

		this.changed = false;
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

	/**
	 * Normally this method is not need to be invoked directly.
	 * It is used by methods <code>createInstance</code> to charge "changed" flag
	 */
	public void setChanged() {
		this.changed = true;
	}

	protected void incrementVersion() {
		if (this.version < Long.MAX_VALUE)
			this.version++;
		else
			this.version = Long.MIN_VALUE;
	}

	protected synchronized void setAttributes(Date created, Date modified, Identifier creatorId, Identifier modifierId, long version) {
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}
}
