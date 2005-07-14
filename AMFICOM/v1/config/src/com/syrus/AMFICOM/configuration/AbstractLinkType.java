/*
 * $Id: AbstractLinkType.java,v 1.16 2005/07/14 18:46:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;

import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectType;

/**
 * @version $Revision: 1.16 $, $Date: 2005/07/14 18:46:55 $
 * @author $Author: arseniy $
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

	protected AbstractLinkType(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description) {
		super(id, created, modified, creatorId, modifierId, version, codename, description);		
	}

	public abstract Identifier getImageId();

	public abstract String getManufacturer();

	public abstract void setManufacturer(final String manufacturer);

	public abstract String getManufacturerCode();

	public abstract void setManufacturerCode(final String manufacturerCode);

	public abstract LinkTypeSort getSort();

	public abstract String getName();

	public abstract void setName(final String Name);
}
