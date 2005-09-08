/*
 * $Id: AbstractLinkType.java,v 1.20 2005/09/08 18:26:27 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @version $Revision: 1.20 $, $Date: 2005/09/08 18:26:27 $
 * @author $Author: bass $
 * @module config
 */
public abstract class AbstractLinkType extends StorableObjectType implements Namable, Characterizable {
	private static final long serialVersionUID = 6276017738364160981L;

	private transient CharacterizableDelegate characterizableDelegate;

	public AbstractLinkType() {
		super();
	}

	protected AbstractLinkType(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

}
