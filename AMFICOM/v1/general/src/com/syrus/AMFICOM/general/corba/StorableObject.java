/*
 * $Id: StorableObject.java,v 1.3 2005/03/10 15:04:11 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.*;
import java.util.Date;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link StorableObject} instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/10 15:04:11 $
 * @module general_v1
 */
public class StorableObject implements IStorableObject, Cloneable {

	private static final long serialVersionUID = 3675974125608086175L;
	protected Date created = null;
	protected Identifier creatorId = null;
	protected Identifier id = null;
	protected Date modified = null;
	protected Identifier modifierId = null;
	protected long version = 0;

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#changed
	 */
	private boolean changed;

	/**
	 * Server-side constructor.
	 *
	 * @param id
	 * @see com.syrus.AMFICOM.general.StorableObject#StorableObject(com.syrus.AMFICOM.general.Identifier)
	 */
	protected StorableObject(final Identifier id) {
		this.id = id;
		this.changed = false;
	}

	/**
	 * Client-side constructor.
	 *
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @see com.syrus.AMFICOM.general.StorableObject#StorableObject(com.syrus.AMFICOM.general.Identifier, java.util.Date, java.util.Date, com.syrus.AMFICOM.general.Identifier, com.syrus.AMFICOM.general.Identifier, long)
	 */
	protected StorableObject(final Identifier id, final Date created, final Date modified, final Identifier creatorId, final Identifier modifierId, final long version) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	/**
	 * @see StorableObject#getCreated()
	 * @see com.syrus.AMFICOM.general.StorableObject#getCreated()
	 */
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @see StorableObject#getCreatorId()
	 * @see com.syrus.AMFICOM.general.StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		return this.creatorId;
	}

	/**
	 * Can (and possibly will) be overridden by descendants.
	 *
	 * @see StorableObject#getDependencies()
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Identifier[] getDependencies() {
		return new Identifier[0];
	}

	/**
	 * Returns structure to be transmitted via CORBA.
	 *
	 * @see StorableObject#getHeaderTransferable()
	 * @see com.syrus.AMFICOM.general.StorableObject#getHeaderTransferable()
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
	 * @see Identifiable#getId()
	 * @see com.syrus.AMFICOM.general.Identifiable#getId()
	 * @see com.syrus.AMFICOM.general.StorableObject#getId()
	 */
	public Identifier getId() {
		return this.id;
	}

	/**
	 * @see StorableObject#getModified()
	 * @see com.syrus.AMFICOM.general.StorableObject#getModified()
	 */
	public Date getModified() {
		return this.modified;
	}

	/**
	 * @see StorableObject#getModifierId()
	 * @see com.syrus.AMFICOM.general.StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		return this.modifierId;
	}

	/**
	 * @see StorableObject#getVersion()
	 * @see com.syrus.AMFICOM.general.StorableObject#getVersion()
	 */
	public long getVersion() {
		return this.version;
	}

	/**
	 * Returns <code>true</code> if object was changed locally (with respect
	 * to server).
	 *
	 * @see StorableObject#isChanged()
	 * @see com.syrus.AMFICOM.general.StorableObject#isChanged()
	 */
	public boolean isChanged() {
		return this.changed;
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @see StorableObject#setAttributes(Date, Date, Identifier, Identifier, long)
	 * @see com.syrus.AMFICOM.general.StorableObject#setAttributes(java.util.Date, java.util.Date, com.syrus.AMFICOM.general.Identifier, com.syrus.AMFICOM.general.Identifier, long)
	 */
	public synchronized void setAttributes(final Date created, final Date modified, final Identifier creatorId, final Identifier modifierId, final long version) {
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	/**
	 * @param invoker
	 * @param changed
	 * @see StorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory invoker, final boolean changed) {
		assert invoker != null;
		this.changed = changed;
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		final StorableObject clone = (StorableObject) super.clone();
		/**
		 * @todo Later, for id property, generate a <b>new</b>
		 *       identifier from the same sequence. 
		 */
		clone.id = this.id;
		
		final Date cloneCreated = new Date();
		clone.created = cloneCreated;
		clone.modified = cloneCreated;
		/**
		 * @todo Initialize creatorId and modifierId with values from
		 * current session, if this session is available.
		 */
		clone.creatorId = this.creatorId;
		clone.modifierId = this.modifierId;
		/*
		 * Initialize version vith 0L, like for all newly created objects.
		 */
		clone.version = 0L;
		/**
		 * @todo Also set changed = true.
		 */
		return clone;
	}
}
