/*
 * $Id: StorableObject.java,v 1.15 2004/11/29 10:24:29 bass Exp $
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
 * @version $Revision: 1.15 $, $Date: 2004/11/29 10:24:29 $
 * @author $Author: bass $
 * @module general_v1
 */
public abstract class StorableObject implements
//		com.syrus.AMFICOM.general.corba.IStorableObject,
		Identified,
		TransferableObject,
		Serializable {
	private static final long serialVersionUID = -1720579921164397193L;

	private static final String[] TRUNCATABLE_IDS = {
		"IDL:com/syrus/AMFICOM/general/StorableObject:1.0"
	};

	protected Date created;
	protected Identifier creatorId;
	protected long currentVersion = 0;
	protected Identifier id;
	protected Date modified;
	protected Identifier modifierId;
	protected long version = 0;

	protected StorableObject(Identifier id) {
		this.id = id;
	}

	protected StorableObject(Identifier id, Date created, Date modified, Identifier creatorId, Identifier modifierId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = 0;
	}

	protected StorableObject(StorableObject_Transferable transferable){
		this.id = new Identifier(transferable.id);
		this.created = new Date(transferable.created);
		this.modified = new Date(transferable.modified);
		this.creatorId = new Identifier(transferable.creator_id);
		this.modifierId = new Identifier(transferable.modifier_id);
		this.version = transferable.version;
		this.currentVersion = this.version;
	}

	/**
	 * @see org.omg.CORBA.portable.ValueBase#_truncatable_ids()
	 */
	public String[] _truncatable_ids() {
		return TRUNCATABLE_IDS;
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @see ComSyrusAmficomGeneralIStorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		return this.creatorId;
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getDependencies()
	 */
	public abstract List getDependencies();

	/**
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		return new StorableObject_Transferable((Identifier_Transferable)this.id.getTransferable(),
			this.created.getTime(),
			this.modified.getTime(),
			(Identifier_Transferable)this.creatorId.getTransferable(),
			(Identifier_Transferable)this.modifierId.getTransferable(),
			this.currentVersion);
	}

	/**
	 * @see ComSyrusAmficomGeneralIStorableObject#getId()
	 */
	public Identifier getId() {
		return this.id;
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
		return this.modified;
	}

	/**
	 * @see ComSyrusAmficomGeneralIStorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		return this.modifierId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
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
