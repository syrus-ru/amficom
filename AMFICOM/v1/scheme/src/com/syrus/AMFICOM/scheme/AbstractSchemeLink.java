/*-
 * $Id: AbstractSchemeLink.java,v 1.12 2005/05/05 15:57:09 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeLink}instead.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/05/05 15:57:09 $
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
	Identifier abstractLinkTypeId;

	/**
	 * Depending on implementation, may reference either {@link Link link}
	 * or {@link Link cable link}.
	 */
	Identifier linkId;

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
	 * @return optical length of this <code>SchemeLink</code> or
	 *         <code>SchemeCableLink</code>.
	 * @see #opticalLength
	 */
	public final double getOpticalLength() {
		return this.opticalLength;
	}

	/**
	 * @return physical length of this <code>SchemeLink</code> or
	 *         <code>SchemeCableLink</code>.
	 * @see #physicalLength
	 */
	public final double getPhysicalLength() {
		return this.physicalLength;
	}

	/**
	 * Overridden by descendants to add extra checks.
	 */
	public AbstractSchemePort getSourceAbstractSchemePort() {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.sourceAbstractSchemePortId.isVoid()
				|| !this.sourceAbstractSchemePortId.equals(this.targetAbstractSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;

		try {
			return this.sourceAbstractSchemePortId.isVoid()
					? null
					: (AbstractSchemePort) SchemeStorableObjectPool.getStorableObject(this.sourceAbstractSchemePortId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * Overridden by descendants to add extra checks.
	 */
	public AbstractSchemePort getTargetAbstractSchemePort() {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.targetAbstractSchemePortId.isVoid()
				|| !this.targetAbstractSchemePortId.equals(this.sourceAbstractSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;

		try {
			return this.targetAbstractSchemePortId.isVoid()
					? null
					: (AbstractSchemePort) SchemeStorableObjectPool.getStorableObject(this.targetAbstractSchemePortId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

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
	 * @see #opticalLength
	 */
	public final void setOpticalLength(final double opticalLength) {
		if (this.opticalLength == opticalLength)
			return;
		this.opticalLength = opticalLength;
		this.changed = true;
	}

	/**
	 * @see #physicalLength
	 */
	public final void setPhysicalLength(final double physicalLength) {
		if (this.physicalLength == physicalLength)
			return;
		this.physicalLength = physicalLength;
		this.changed = true;
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param sourceAbstractSchemePort
	 */
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.sourceAbstractSchemePortId.isVoid()
				|| !this.sourceAbstractSchemePortId.equals(this.targetAbstractSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		final Identifier newSourceAbstractSchemePortId = Identifier.possiblyVoid(sourceAbstractSchemePort);
		assert newSourceAbstractSchemePortId.isVoid()
				|| !newSourceAbstractSchemePortId.equals(this.targetAbstractSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		if (this.sourceAbstractSchemePortId.equals(newSourceAbstractSchemePortId))
			return;
		this.sourceAbstractSchemePortId = newSourceAbstractSchemePortId;
		super.changed = true;
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param targetAbstractSchemePort
	 */
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.targetAbstractSchemePortId.isVoid()
				|| !this.targetAbstractSchemePortId.equals(this.sourceAbstractSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		final Identifier newTargetAbstractSchemePortId = Identifier.possiblyVoid(targetAbstractSchemePort);
		assert newTargetAbstractSchemePortId.isVoid()
				|| !newTargetAbstractSchemePortId.equals(this.sourceAbstractSchemePortId): ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		if (this.targetAbstractSchemePortId.equals(newTargetAbstractSchemePortId))
			return;
		this.targetAbstractSchemePortId = newTargetAbstractSchemePortId;
		super.changed = true;
	}

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

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.abstractLinkTypeId != null && this.linkId != null
				&& this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.abstractLinkTypeId);
		dependencies.add(this.linkId);
		dependencies.add(this.sourceAbstractSchemePortId);
		dependencies.add(this.targetAbstractSchemePortId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @param header
	 * @param name
	 * @param description
	 * @param physicalLength1
	 * @param opticalLength1
	 * @param abstractLinkTypeId1
	 * @param linkId1
	 * @param sourceAbstractSchemePortId1
	 * @param targetAbstractSchemePortId1
	 * @param parentSchemeId1
	 * @param characteristicIds
	 * @throws CreateObjectException
	 */
	void fromTransferable(final StorableObject_Transferable header,
			final String name, final String description,
			final double physicalLength1, final double opticalLength1,
			final Identifier_Transferable abstractLinkTypeId1,
			final Identifier_Transferable linkId1,
			final Identifier_Transferable sourceAbstractSchemePortId1,
			final Identifier_Transferable targetAbstractSchemePortId1,
			final Identifier_Transferable parentSchemeId1,
			final Identifier_Transferable characteristicIds[])
			throws CreateObjectException {
		super.fromTransferable(header, name, description, parentSchemeId1, characteristicIds);
		this.physicalLength = physicalLength1;
		this.opticalLength = opticalLength1;
		this.abstractLinkTypeId = new Identifier(abstractLinkTypeId1);
		this.linkId = new Identifier(linkId1);
		this.sourceAbstractSchemePortId = new Identifier(sourceAbstractSchemePortId1);
		this.targetAbstractSchemePortId = new Identifier(targetAbstractSchemePortId1);
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
