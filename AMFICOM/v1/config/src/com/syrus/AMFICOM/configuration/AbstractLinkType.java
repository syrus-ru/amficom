/*
 * $Id: AbstractLinkType.java,v 1.2 2004/12/03 19:23:45 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2004/12/03 19:23:45 $
 * @author $Author: bass $
 * @module configuration_v1
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

	/**
	 * @todo This method is absolutely unnecessary.
	 */
	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creator_id,
			Identifier modifier_id,
			String codename,
			String description) {
		super.setAttributes(created,
			modified,
			creator_id,
			modifier_id,
			codename,
			description);
	}

	protected void setDescription0(final String description) {
		this.description = description;
	}
}
