/*
 * $Id: StorableObject.java,v 1.21 2005/01/20 13:34:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.21 $, $Date: 2005/01/20 13:34:04 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class StorableObject implements Identified, TransferableObject, Serializable {
	private static final long serialVersionUID = -1720579921164397193L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_CREATOR_ID = "creator_id";
	public static final String COLUMN_MODIFIED = "modified";
	public static final String COLUMN_MODIFIER_ID = "modifier_id";

	protected Identifier id;
	protected Date created;
	protected Identifier creatorId;
	protected Date modified;
	protected Identifier modifierId;

	protected long currentVersion;
	protected long version;

	Map exportedColumns;

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

	protected StorableObject (Map exportedColumns) {
		if (exportedColumns == null)
			throw new IllegalArgumentException("Argument is 'null'");

		this.id = new Identifier((String)exportedColumns.get(COLUMN_ID));
		this.created = new Date(Long.parseLong((String)exportedColumns.get(COLUMN_CREATED)));
		this.modified = new Date(Long.parseLong((String)exportedColumns.get(COLUMN_MODIFIED)));
		this.creatorId = new Identifier((String)exportedColumns.get(COLUMN_CREATOR_ID));
		this.modifierId = new Identifier((String)exportedColumns.get(COLUMN_MODIFIER_ID));
		this.exportedColumns = exportedColumns;
	}

	protected void exportColumns() {
		if (this.exportedColumns == null) {
			this.exportedColumns = new HashMap();
		}
		this.exportedColumns.put(COLUMN_ID, this.id.toString());
		this.exportedColumns.put(COLUMN_CREATED, Long.toString(this.created.getTime()));
		this.exportedColumns.put(COLUMN_MODIFIED, Long.toString(this.modified.getTime()));
		this.exportedColumns.put(COLUMN_CREATOR_ID, this.creatorId.toString());
		this.exportedColumns.put(COLUMN_MODIFIER_ID, this.modifierId.toString());
	}

	public Map getExportedColumns() {
		return Collections.unmodifiableMap(this.exportedColumns);
	}
}
