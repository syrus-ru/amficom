/*
 * $Id: AbstractSchemeElement.java,v 1.3 2005/03/17 18:17:27 bass Exp $
 * Copyright ¿ 2004 Syrus Systems. Dept. of Science & Technology. Project:
 * AMFICOM.
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
 * @version $Revision: 1.3 $, $Date: 2005/03/17 18:17:27 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeElement extends
		AbstractCloneableStorableObject implements Describable,
		Characterizable {
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
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
			return;
		this.name = name;
		this.changed = true;
	}

	/**
	 * @see Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}
}
