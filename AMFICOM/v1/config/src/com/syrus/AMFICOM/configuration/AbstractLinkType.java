/*
 * $Id: AbstractLinkType.java,v 1.1 2004/11/19 09:00:09 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/11/19 09:00:09 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public abstract class AbstractLinkType extends StorableObjectType {
	static final long serialVersionUID = 6276017738364160981L;

	public AbstractLinkType(Identifier id) {
		super(id);
	}

	public AbstractLinkType(Identifier id,
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
			  modifier_id,
			  codename,
			  description);		
	}
	
	public AbstractLinkType(StorableObject_Transferable transferable,
							  String codename,
							  String description) {
			super(transferable, codename, description);
	}

	public String getCodename() {
		return super.codename;
	}

	public String getDescription() {
		return super.description;
	}
	
	public void setDescription(String description){
		super.description = description;
		super.currentVersion = super.getNextVersion();
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
							modifier_id,
							codename,
							description);
	}
}
