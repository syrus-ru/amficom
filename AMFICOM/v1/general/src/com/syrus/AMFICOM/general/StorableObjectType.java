/*
 * $Id: StorableObjectType.java,v 1.13 2005/02/03 14:26:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.13 $, $Date: 2005/02/03 14:26:18 $
 * @author $Author: arseniy $
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
							  String codename,
							  String description) {
		super(id,
			  created,
			  modified,
			  creator_id,
			  modifier_id);
		this.codename = codename;
		this.description = description;
	}
	
	public StorableObjectType(StorableObject_Transferable transferable,
							  String codename,
							  String description) {
			super(transferable);
			this.codename = codename;
			this.description = description;
	}

	public String getCodename() {
		return this.codename;
	}
	
	protected void setCodename0(String codename){
		this.codename = codename;
	}
	
	public void setCodename(String codename){
		this.setCodename0(codename);
		super.currentVersion = super.getNextVersion();
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description){
		this.setDescription0(description);
		super.currentVersion = super.getNextVersion();
	}
	
	protected void setDescription0(String description){
		this.description = description;
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creator_id,
											  Identifier modifier_id,
											  String codename,
											  String description) {
		super.setAttributes(created,
							modified,
							creator_id,
							modifier_id);
		this.codename = codename;
		this.description = description;
	}
}
