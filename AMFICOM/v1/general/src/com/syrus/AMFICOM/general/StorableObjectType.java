/*
 * $Id: StorableObjectType.java,v 1.18 2005/04/13 10:45:50 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.18 $, $Date: 2005/04/13 10:45:50 $
 * @author $Author: bob $
 * @module general_v1
 */

public abstract class StorableObjectType extends StorableObject {
	static final long serialVersionUID = 6253817645176813979L;

	protected String codename;
	protected String description;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public StorableObjectType(Identifier id) {
		super(id);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public StorableObjectType(Identifier id,
			Date created,
			Date modified,
			Identifier creator_id,
			Identifier modifier_id,
			long version,
			String codename,
			String description) {
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
	protected void setCodename0(String codename) {
		this.codename = codename;
	}

	public void setCodename(String codename) {
		this.setCodename0(codename);
		super.changed = true;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDescription0(String description) {
		this.description = description;
	}

	public void setDescription(String description) {
		this.setDescription0(description);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creator_id,
			Identifier modifier_id,
			long version,
			String codename,
			String description) {
		super.setAttributes(created, modified, creator_id, modifier_id, version);
		this.codename = codename;
		this.description = description;
	}
}
