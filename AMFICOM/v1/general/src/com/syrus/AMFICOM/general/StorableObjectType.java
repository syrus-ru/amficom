/*
 * $Id: StorableObjectType.java,v 1.20 2005/05/27 12:59:36 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.20 $, $Date: 2005/05/27 12:59:36 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public abstract class StorableObjectType extends StorableObject {
	static final long serialVersionUID = 6253817645176813979L;

	protected String codename;
	protected String description;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public StorableObjectType(final Identifier id) {
		super(id);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public StorableObjectType(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creator_id,
			final Identifier modifier_id,
			final long version,
			final String codename,
			final String description) {
		super(id, created, modified, creator_id, modifier_id, version);
		this.codename = codename;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected StorableObjectType() {
		// empty
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(final IDLEntity transferable, final String codename1, final String description1)
			throws ApplicationException {
		super.fromTransferable(transferable);
		this.codename = codename1;
		this.description = description1;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.codename != null && this.codename.length() != 0 && this.description != null;
	}
	
	public String getCodename() {
		return this.codename;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setCodename0(final String codename) {
		this.codename = codename;
	}

	public void setCodename(final String codename) {
		this.setCodename0(codename);
		super.changed = true;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDescription0(final String description) {
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.codename = codename;
		this.description = description;
	}
}
