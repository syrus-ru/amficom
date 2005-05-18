/*
 * $Id: AbstractLinkType.java,v 1.13 2005/05/18 11:27:14 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;

import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectType;

/**
 * @version $Revision: 1.13 $, $Date: 2005/05/18 11:27:14 $
 * @author $Author: bass $
 * @module config_v1
 */
public abstract class AbstractLinkType extends StorableObjectType implements Namable {
	private static final long serialVersionUID = 6276017738364160981L;

	public AbstractLinkType(Identifier id) {
		super(id);
	}
	
	public AbstractLinkType() {
		super();
	}

	protected AbstractLinkType(Identifier id,
			Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String codename,
			String description) {
		super(id,
			created,
			modified,
			creatorId,
			modifierId,
			version,
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
		this.changed = true;
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
