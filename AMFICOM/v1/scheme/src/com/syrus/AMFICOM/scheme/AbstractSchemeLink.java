/*-
 * $Id: AbstractSchemeLink.java,v 1.3 2005/03/25 13:24:52 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/03/25 13:24:52 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeLink extends AbstractSchemeElement {
	private static final long serialVersionUID = 1423195997939538835L;

	/**
	 * Depending on implementation, may reference either
	 * {@link com.syrus.AMFICOM.configuration.LinkType}or
	 * {@link com.syrus.AMFICOM.configuration.CableLinkType}.
	 */
	protected Identifier abstractLinkTypeId = null;

	/**
	 * Depending on implementation, may reference either
	 * {@link com.syrus.AMFICOM.configuration.Link link}or
	 * {@link com.syrus.AMFICOM.configuration.Link cable link}.
	 */
	protected Identifier linkId = null;

	/**
	 * Depending on implementation, may reference either {@link SchemePort}
	 * or {@link SchemeCablePort}.
	 */
	protected Identifier sourceAbstractSchemePortId = null;

	/**
	 * Depending on implementation, may reference either {@link SchemePort}
	 * or {@link SchemeCablePort}.
	 */
	protected Identifier targetAbstractSchemePortId = null;

	/**
	 * 0 means either zero or unspecified length.
	 * 
	 * @see #opticalLength()
	 */
	protected double thisOpticalLength = 0;

	/**
	 * 0 means either zero or unspecified length.
	 * 
	 * @see #physicalLength()
	 */
	protected double thisPhysicalLength = 0;

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

	/**
	 * Getter returns optical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #thisOpticalLength
	 */
	public abstract double opticalLength();

	/**
	 * Getter returns optical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #thisOpticalLength
	 */
	public abstract void opticalLength(double newOpticalLength);

	/**
	 * Getter returns physical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #thisPhysicalLength
	 */
	public abstract double physicalLength();

	/**
	 * Getter returns physical length of this scheme link or scheme cable
	 * link.
	 * 
	 * @see #thisPhysicalLength
	 */
	public abstract void physicalLength(double newPhysicalLength);

	public abstract AbstractSchemePort getSourceAbstractSchemePort();

	public abstract void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort);

	public abstract AbstractSchemePort getTargetAbstractSchemePort();

	public abstract void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort);

	public abstract AbstractLinkType getAbstractLinkType();
	
	public abstract void setAbstractLinkType(final AbstractLinkType abstractLinkType);
	
	public abstract Link getLink();

	public abstract void setLink(final Link link);
}
