/*
 * $Id: AbstractSchemeElement.java,v 1.2 2005/03/17 09:40:22 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import java.util.Date;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeElement}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 09:40:22 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeElement extends AbstractCloneableStorableObject implements
		Describable, Characterizable {
	static final long serialVersionUID = 4644766113809681630L;

	protected Identifier characteristicIds[] = null;

	protected Identifier schemeId = null;

	private String description;

	private String name;

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
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		return this.description;
	}

	/**
	 * @see Namable#getName()
	 */
	public final String getName() {
		return this.name;
	}

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

	/**
	 * @param description can be null.
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		this.description = description;
		this.changed = true;
	}

	/**
	 * @param name
	 * @see Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert name != null;
		this.name = name;
		this.changed = true;
	}
}
