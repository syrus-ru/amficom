/*
 * $Id: StorableObjectImpl.java,v 1.10 2005/03/04 13:29:36 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link StorableObject} instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/03/04 13:29:36 $
 * @module general_v1
 */
final class StorableObjectImpl extends StorableObject implements Cloneable {
	private static final long serialVersionUID = 3675974125608086175L;

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#changed
	 */
	private boolean changed;

	/**
	 * Internal constructor used when restoring from stream.
	 */
	StorableObjectImpl() {
		// empty
	}

	/**
	 * Server-side constructor.
	 *
	 * @param id
	 * @see com.syrus.AMFICOM.general.StorableObject#StorableObject(com.syrus.AMFICOM.general.Identifier)
	 */
	StorableObjectImpl(final Identifier id) {
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
	StorableObjectImpl(final Identifier id, final long created, final long modified, final Identifier creatorId, final Identifier modifierId, final long version) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	/**
	 * @see StorableObject#cloneInstance()
	 */
	public StorableObject cloneInstance() {
		try {
			return (StorableObject) this.clone();
		} catch (final CloneNotSupportedException cnse) {
			/**
			 * @todo Later, process with an error handler.
			 */
			cnse.printStackTrace();
			return null;
		}
	}

	/**
	 * @see StorableObject#getCreated()
	 * @see com.syrus.AMFICOM.general.StorableObject#getCreated()
	 */
	public long getCreated() {
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
		return new StorableObject_Transferable(this.id.getTransferable(),
				this.created,
				this.modified,
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
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
	 * @see StorableObject#getModified()
	 * @see com.syrus.AMFICOM.general.StorableObject#getModified()
	 */
	public long getModified() {
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
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @see StorableObject#setAttributes(long, long, Identifier, Identifier, long)
	 * @see com.syrus.AMFICOM.general.StorableObject#setAttributes(java.util.Date, java.util.Date, com.syrus.AMFICOM.general.Identifier, com.syrus.AMFICOM.general.Identifier, long)
	 */
	public synchronized void setAttributes(final long created, final long modified, final Identifier creatorId, final Identifier modifierId, final long version) {
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
	 * @see StorableObject#getVersion()
	 * @see com.syrus.AMFICOM.general.StorableObject#getVersion()
	 */
	public long getVersion() {
		return this.version;
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		final StorableObjectImpl storableObject = (StorableObjectImpl) super.clone();
		/**
		 * @todo Later, for id property, generate a <b>new</b>
		 *       identifier from the same sequence. 
		 */
		storableObject.id = this.id;
		storableObject.created = this.created;
		storableObject.modified = this.modified;
		storableObject.creatorId = this.creatorId;
		storableObject.modifierId = this.modifierId;
		storableObject.version = this.version;
		return storableObject;
	}
}
