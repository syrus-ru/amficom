/*
 * $Id: AbstractSchemeElement.java,v 1.3 2005/03/11 17:26:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import java.util.Date;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeElement}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/11 17:26:58 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeElement extends CloneableStorableObject implements
		Namable, Describable, Characterizable {
	protected Identifier characteristicIds[] = null;

	protected Identifier schemeId = null;

	protected String thisDescription = null;

	protected String thisName = null;

	/**
	 * @param id
	 */
	protected AbstractSchemeElement(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected AbstractSchemeElement(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * Transient attribute
	 */
	public abstract boolean alarmed();

	/**
	 * Transient attribute
	 */
	public abstract void alarmed(boolean newAlarmed);

	/**
	 * Getter returns scheme parent to this scheme link or scheme cable link
	 * or scheme element.
	 * 
	 * @see #schemeId
	 */
	public abstract Scheme scheme();

	/**
	 * Getter returns scheme parent to this scheme link or scheme cable link
	 * or scheme element.
	 * 
	 * @see #schemeId
	 */
	public abstract void scheme(Scheme newScheme);
}
