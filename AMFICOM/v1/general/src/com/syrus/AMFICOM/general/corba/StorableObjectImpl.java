/*
 * $Id: StorableObjectImpl.java,v 1.8 2005/02/28 14:21:03 bass Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/02/28 14:21:03 $
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
		this.thisId = id;
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
		this.thisId = id;
		this.thisCreated = created;
		this.thisModified = modified;
		this.thisCreatorId = creatorId;
		this.thisModifierId = modifierId;
		this.thisVersion = version;
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
	 * @see StorableObject#created()
	 * @see com.syrus.AMFICOM.general.StorableObject#getCreated()
	 */
	public long created() {
		return this.thisCreated;
	}

	/**
	 * @see StorableObject#creatorId()
	 * @see com.syrus.AMFICOM.general.StorableObject#getCreatorId()
	 */
	public Identifier creatorId() {
		return this.thisCreatorId;
	}

	/**
	 * Can (and possibly will) be overridden by descendants.
	 *
	 * @see StorableObject#dependencies()
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Identifier[] dependencies() {
		return new Identifier[0];
	}

	/**
	 * Returns structure to be transmitted via CORBA.
	 *
	 * @see StorableObject#headerTransferable()
	 * @see com.syrus.AMFICOM.general.StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		return new StorableObject_Transferable(this.thisId.transferable(),
				this.thisCreated,
				this.thisModified,
				this.thisCreatorId.transferable(),
				this.thisModifierId.transferable(),
				this.thisVersion);
	}

	/**
	 * @see Identifiable#id()
	 * @see com.syrus.AMFICOM.general.Identified#getId()
	 * @see com.syrus.AMFICOM.general.StorableObject#getId()
	 */
	public Identifier id() {
		return this.thisId;
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
	 * @see StorableObject#modified()
	 * @see com.syrus.AMFICOM.general.StorableObject#getModified()
	 */
	public long modified() {
		return this.thisModified;
	}

	/**
	 * @see StorableObject#modifierId()
	 * @see com.syrus.AMFICOM.general.StorableObject#getModifierId()
	 */
	public Identifier modifierId() {
		return this.thisModifierId;
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
		this.thisCreated = created;
		this.thisModified = modified;
		this.thisCreatorId = creatorId;
		this.thisModifierId = modifierId;
		this.thisVersion = version;
	}

	/**
	 * @param storableObjectFactory
	 * @param changed
	 * @see StorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
		assert storableObjectFactory != null;
		this.changed = changed;
	}

	/**
	 * @see StorableObject#version()
	 * @see com.syrus.AMFICOM.general.StorableObject#getVersion()
	 */
	public long version() {
		return this.thisVersion;
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
		storableObject.thisId = this.thisId;
		storableObject.thisCreated = this.thisCreated;
		storableObject.thisModified = this.thisModified;
		storableObject.thisCreatorId = this.thisCreatorId;
		storableObject.thisModifierId = this.thisModifierId;
		storableObject.thisVersion = this.thisVersion;
		return storableObject;
	}
}
