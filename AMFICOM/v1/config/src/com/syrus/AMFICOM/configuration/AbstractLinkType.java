/*
 * $Id: AbstractLinkType.java,v 1.8 2005/01/14 18:07:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;

import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.8 $, $Date: 2005/01/14 18:07:07 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public abstract class AbstractLinkType extends StorableObjectType {
	private static final long serialVersionUID = 6276017738364160981L;

	public AbstractLinkType(Identifier id) {
		super(id);
	}
	
	public AbstractLinkType(final StorableObject_Transferable transferable,
			final String codename,
			final String description) {
		super(transferable, codename, description);
	}

	protected AbstractLinkType(Identifier id,
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			String codename,
			String description) {
		super(id,
			created,
			modified,
			creatorId,
			modifierId,
			codename,
			description);		
	}

	public String getCodename() {
		return super.codename;
	}

	public String getDescription() {
		return super.description;
	}
	
	public void setDescription(final String description){
		this.currentVersion = getNextVersion();
		setDescription0(description);
	}	

	protected void setDescription0(final String description) {
		this.description = description;
	}    
    
	public abstract Identifier getImageId();
    
    public abstract String getManufacturer();
    public abstract void setManufacturer(String manufacturer);
    
    public abstract String getManufacturerCode();
    public abstract void setManufacturerCode(String manufacturerCode);
    
    public abstract LinkTypeSort getSort();
    
    public abstract String getName();        
    public abstract void setName(String Name);
}
