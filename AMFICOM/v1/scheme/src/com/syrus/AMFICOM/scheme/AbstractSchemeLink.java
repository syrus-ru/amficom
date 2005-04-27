/*-
 * $Id: AbstractSchemeLink.java,v 1.9 2005/04/27 13:22:09 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeLink}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/04/27 13:22:09 $
 * @module scheme_v1
 */
public abstract class AbstractSchemeLink extends AbstractSchemeElement {
	private static final long serialVersionUID = 1423195997939538835L;

	/**
	 * 0 means either zero or unspecified length.
	 * 
	 * @see #getPhysicalLength()
	 */
	private double physicalLength;

	/**
	 * 0 means either zero or unspecified length.
	 * 
	 * @see #getOpticalLength()
	 */
	private double opticalLength;

	/**
	 * Depending on implementation, may reference either {@link LinkType} or
	 * {@link CableLinkType}.
	 */
	private Identifier abstractLinkTypeId;

	/**
	 * Depending on implementation, may reference either {@link Link link}
	 * or {@link Link cable link}.
	 */
	private Identifier linkId;

	/**
	 * Depending on implementation, may reference either {@link SchemePort}
	 * or {@link SchemeCablePort}.
	 */
	Identifier sourceAbstractSchemePortId;

	/**
	 * Depending on implementation, may reference either {@link SchemePort}
	 * or {@link SchemeCablePort}.
	 */
	Identifier targetAbstractSchemePortId;

	boolean abstractLinkTypeSet = false;

	/**
	 * @param id
	 */
	AbstractSchemeLink(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param abstractLinkType
	 * @param link
	 * @param sourceAbstractSchemePort
	 * @param targetAbstractSchemePort
	 * @param parentScheme
	 */
	AbstractSchemeLink(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength,
			final AbstractLinkType abstractLinkType,
			final Link link,
			final AbstractSchemePort sourceAbstractSchemePort,
			final AbstractSchemePort targetAbstractSchemePort,
			final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, parentScheme);
		this.physicalLength = physicalLength;
		this.opticalLength = opticalLength;

		assert abstractLinkType == null || link == null;
		this.abstractLinkTypeId = Identifier.possiblyVoid(abstractLinkType);
		this.linkId = Identifier.possiblyVoid(link);

		this.sourceAbstractSchemePortId = Identifier.possiblyVoid(sourceAbstractSchemePort);
		this.targetAbstractSchemePortId = Identifier.possiblyVoid(targetAbstractSchemePort);
	}

	/**
	 * Will transmute to the constructor from the corresponding
	 * transferable. 
	 */
	AbstractSchemeLink() {
		// super();
	}
	
	/**
	 * Overridden by descendants to add extra checks.
	 */
	public Link getLink() {
		assert this.assertAbstractLinkTypeSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		if (this.linkId.isVoid())
			return null;

		try {
			return (Link) ConfigurationStorableObjectPool.getStorableObject(this.linkId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * Overridden by descendants to add extra checks.
	 */
	public AbstractLinkType getAbstractLinkType() {
		assert this.assertAbstractLinkTypeSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		if (!this.linkId.isVoid())
			return (AbstractLinkType) getLink().getType();

		try {
			return (AbstractLinkType) ConfigurationStorableObjectPool.getStorableObject(this.abstractLinkTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
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
	
	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param link
	 */
	public void setLink(final Link link) {
		assert this.assertAbstractLinkTypeSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		final Identifier newLinkId = Identifier.possiblyVoid(link);
		if (this.linkId.equals(newLinkId)) {
			Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
			return;
		}

		if (this.linkId.isVoid())
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.abstractLinkTypeId = Identifier.VOID_IDENTIFIER;
		else if (newLinkId.isVoid())
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve). 
			 */
			this.abstractLinkTypeId = this.getLink().getType().getId();
		this.linkId = newLinkId;
		this.changed = true;
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param abstractLinkType
	 */
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert this.assertAbstractLinkTypeSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert abstractLinkType != null: ErrorMessages.NON_NULL_EXPECTED;

		if (!this.linkId.isVoid())
			this.getLink().setType(abstractLinkType);
		else {
			final Identifier newAbstractLinkTypeId = abstractLinkType.getId();
			if (this.abstractLinkTypeId.equals(newAbstractLinkTypeId)) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			this.abstractLinkTypeId = newAbstractLinkTypeId;
			this.changed = true;
		}
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

	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier abstractLinkTypeId,
			final Identifier linkId,
			final Identifier sourceAbstractSchemePortId,
			final Identifier targetAbstractSchemePortId,			
			final Identifier parentSchemeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, name, description, parentSchemeId);

		assert abstractLinkTypeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert linkId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert abstractLinkTypeId.isVoid() ^ linkId.isVoid();

		assert sourceAbstractSchemePortId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert targetAbstractSchemePortId != null: ErrorMessages.NON_NULL_EXPECTED;

		this.physicalLength = physicalLength;
		this.opticalLength = opticalLength;
		this.abstractLinkTypeId = abstractLinkTypeId;
		this.linkId = linkId;
		this.sourceAbstractSchemePortId = sourceAbstractSchemePortId;
		this.targetAbstractSchemePortId = targetAbstractSchemePortId;
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertAbstractLinkTypeSetNonStrict() {
		if (this.abstractLinkTypeSet)
			return this.assertAbstractLinkTypeSetStrict();
		this.abstractLinkTypeSet = true;
		return this.linkId != null
				&& this.abstractLinkTypeId != null
				&& this.linkId.isVoid()
				&& this.abstractLinkTypeId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertAbstractLinkTypeSetStrict() {
		return this.linkId != null
				&& this.abstractLinkTypeId != null
				&& (this.linkId.isVoid() ^ this.abstractLinkTypeId.isVoid());
	}
}
