/*
 * $Id: StorableObject.java,v 1.23 2005/01/25 08:50:07 bob Exp $
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
 * @version $Revision: 1.23 $, $Date: 2005/01/25 08:50:07 $
 * @author $Author: bob $
 * @module general_v1
 */
public abstract class StorableObject implements Identified, TransferableObject, Serializable {
	private static final long serialVersionUID = -1720579921164397193L;

	protected Identifier id;
	protected Date created;
	protected Identifier creatorId;
	protected Date modified;
	protected Identifier modifierId;

	protected long currentVersion;
	protected long version;

	protected StorableObject(Identifier id) {
		this.id = id;
	}

	protected StorableObject(Identifier id, Date created, Date modified, Identifier creatorId, Identifier modifierId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;

		this.currentVersion = 0;
		this.version = 0;
	}

	protected StorableObject(StorableObject_Transferable transferable) {
		this.id = new Identifier(transferable.id);
		this.created = new Date(transferable.created);
		this.modified = new Date(transferable.modified);
		this.creatorId = new Identifier(transferable.creator_id);
		this.modifierId = new Identifier(transferable.modifier_id);

		this.version = transferable.version;
		this.currentVersion = this.version;
	}

	public abstract void insert() throws CreateObjectException;

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
	 * @see com.syrus.AMFICOM.general.corba.StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		return new StorableObject_Transferable((Identifier_Transferable)this.id.getTransferable(),
			this.created.getTime(),
			this.modified.getTime(),
			(Identifier_Transferable)this.creatorId.getTransferable(),
			(Identifier_Transferable)this.modifierId.getTransferable(),
			this.currentVersion);
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

	/**
	 * @see com.syrus.AMFICOM.general.corba.StorableObject#changed()
	 */
	public boolean isChanged() {
		/**
		 * @todo: check version at DB, if object was deleted or DB version isn't
		 *        equal local version throws VersionCollisionException
		 */
		return (this.currentVersion != this.version);
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
