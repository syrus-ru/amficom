/*
 * $Id: StorableObjectType.java,v 1.4 2004/08/17 10:34:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/17 10:34:11 $
 * @author $Author: bob $
 * @module general_v1
 */

public abstract class StorableObjectType extends StorableObject {
	protected String codename;
	protected String description;
	
	protected static final String ID_CODENAME 		= "codename=";
	protected static final String ID_DESCRIPTION 	= "description=";

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

	public String getCodename() {
		return this.codename;
	}

	public String getDescription() {
		return this.description;
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
