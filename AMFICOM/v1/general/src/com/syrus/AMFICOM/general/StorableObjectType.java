/*
 * $Id: StorableObjectType.java,v 1.15 2005/04/01 14:12:32 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.15 $, $Date: 2005/04/01 14:12:32 $
 * @author $Author: bob $
 * @module general_v1
 */

public abstract class StorableObjectType extends StorableObject {
	static final long serialVersionUID = 6253817645176813979L;

	protected String codename;
	protected String description;

	public StorableObjectType(Identifier id) {
		super(id);
	}

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

	protected StorableObjectType() {
		// empty
	}
	
	protected void fromTransferable(IDLEntity transferable, String codename, String description) throws CreateObjectException {
		super.fromTransferable(transferable);
		this.codename = codename;
		this.description = description;
	}

	public String getCodename() {
		return this.codename;
	}

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

	protected void setDescription0(String description) {
		this.description = description;
	}

	public void setDescription(String description) {
		this.setDescription0(description);
		super.changed = true;
	}

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
