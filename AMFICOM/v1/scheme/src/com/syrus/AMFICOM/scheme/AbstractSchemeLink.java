/*-
 * $Id: AbstractSchemeLink.java,v 1.4 2005/03/28 12:01:28 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.Identifier;
import java.util.Date;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeLink}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/28 12:01:28 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeLink extends AbstractSchemeElement {
	private static final long serialVersionUID = 1423195997939538835L;

	/**
	 * Depending on implementation, may reference either {@link LinkType} or
	 * {@link CableLinkType}.
	 */
	protected Identifier abstractLinkTypeId;

	/**
	 * Depending on implementation, may reference either {@link SchemePort}
	 * or {@link SchemeCablePort}.
	 */
	protected Identifier sourceAbstractSchemePortId;

	/**
	 * Depending on implementation, may reference either {@link SchemePort}
	 * or {@link SchemeCablePort}.
	 */
	protected Identifier targetAbstractSchemePortId;

	/**
	 * Depending on implementation, may reference either {@link Link link}
	 * or {@link Link cable link}.
	 */
	private Identifier linkId;

	/**
	 * 0 means either zero or unspecified length.
	 * 
	 * @see #getOpticalLength()
	 */
	private double opticalLength;

	/**
	 * 0 means either zero or unspecified length.
	 * 
	 * @see #getPhysicalLength()
	 */
	private double physicalLength;

	/**
	 * @param id
	 */
	protected AbstractSchemeLink(Identifier id) {
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
	protected AbstractSchemeLink(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	public abstract AbstractLinkType getAbstractLinkType();
	
	/**
	 * Overridden by descendants to add extra checks.
	 */
	public Link getLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter returns optical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #opticalLength
	 */
	public final double getOpticalLength() {
		return this.opticalLength;
	}

	/**
	 * Getter returns physical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #physicalLength
	 */
	public final double getPhysicalLength() {
		return this.physicalLength;
	}

	public abstract AbstractSchemePort getSourceAbstractSchemePort();

	public abstract AbstractSchemePort getTargetAbstractSchemePort();
	
	public abstract void setAbstractLinkType(final AbstractLinkType abstractLinkType);

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param link
	 */
	public void setLink(final Link link) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter returns optical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #opticalLength
	 */
	public final void setOpticalLength(final double opticalLength) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter returns physical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #physicalLength
	 */
	public final void setPhysicalLength(final double physicalLength) {
		throw new UnsupportedOperationException();
	}

	public abstract void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort);

	public abstract void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort);
}
