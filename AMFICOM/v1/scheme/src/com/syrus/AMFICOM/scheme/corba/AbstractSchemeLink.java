/*
 * $Id: AbstractSchemeLink.java,v 1.2 2005/03/11 17:26:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemeLink;
import com.syrus.AMFICOM.general.Identifier;
import java.util.Date;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeLink}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/11 17:26:58 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeLink extends AbstractSchemeElement
		implements ComSyrusAmficomConfigurationAbstractSchemeLink {

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

	public abstract AbstractSchemePort sourceAbstractSchemePort();

	public abstract void sourceAbstractSchemePort(
			AbstractSchemePort newSourceAbstractSchemePort);

	public abstract AbstractSchemePort targetAbstractSchemePort();

	public abstract void targetAbstractSchemePort(
			AbstractSchemePort newTargetAbstractSchemePort);
}
