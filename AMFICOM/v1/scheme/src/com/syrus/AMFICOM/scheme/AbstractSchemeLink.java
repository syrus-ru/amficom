/*-
 * $Id: AbstractSchemeLink.java,v 1.49 2005/12/07 16:41:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemeLink;
import com.syrus.AMFICOM.scheme.xml.XmlAbstractSchemeLink;
import com.syrus.util.Log;
import com.syrus.util.XmlConversionException;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeLink}instead.
 *
 * @author $Author: bass $
 * @version $Revision: 1.49 $, $Date: 2005/12/07 16:41:54 $
 * @module scheme
 */
public abstract class AbstractSchemeLink<T extends AbstractSchemeLink<T>>
		extends AbstractSchemeElement<T> {
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
	 * Depending on implementation, may reference either
	 * {@link com.syrus.AMFICOM.configuration.LinkType} or
	 * {@link com.syrus.AMFICOM.configuration.CableLinkType}.
	 */
	Identifier abstractLinkTypeId;

	/**
	 * Depending on implementation, may reference either
	 * {@link com.syrus.AMFICOM.configuration.Link} or
	 * {@link com.syrus.AMFICOM.configuration.CableLink}.
	 */
	Identifier abstractLinkId;

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

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	boolean abstractLinkTypeSet = false;

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
	 * @param abstractLink
	 * @param sourceAbstractSchemePort
	 * @param targetAbstractSchemePort
	 * @param parentScheme
	 */
	AbstractSchemeLink(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final AbstractLinkType<?> abstractLinkType,
			final AbstractLink<?> abstractLink,
			final AbstractSchemePort<?> sourceAbstractSchemePort,
			final AbstractSchemePort<?> targetAbstractSchemePort,
			final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version, name, description, parentScheme);
		this.physicalLength = physicalLength;
		this.opticalLength = opticalLength;

		assert abstractLinkType == null || abstractLink == null;
		this.abstractLinkTypeId = Identifier.possiblyVoid(abstractLinkType);
		this.abstractLinkId = Identifier.possiblyVoid(abstractLink);

		this.sourceAbstractSchemePortId = Identifier.possiblyVoid(sourceAbstractSchemePort);
		this.targetAbstractSchemePortId = Identifier.possiblyVoid(targetAbstractSchemePort);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param entityCode
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	AbstractSchemeLink(final XmlIdentifier id,
			final String importType, final short entityCode,
			final Date created, final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, entityCode, created, creatorId);
	}

	/**
	 * Will transmute to the constructor from the corresponding
	 * transferable.
	 */
	AbstractSchemeLink(/*IdlAbstractSchemeLink*/) {
		// super();
	}

	Identifier getAbstractLinkId() {
		assert true || this.assertAbstractLinkTypeSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertAbstractLinkTypeSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		return this.abstractLinkId;
	}

	/**
	 * A wrapper around {@link #getAbstractLinkId()}.
	 *
	 * Overridden by descendants to add extra checks.
	 */
	public AbstractLink<?> getAbstractLink() {
		try {
			return StorableObjectPool.getStorableObject(this.getAbstractLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	Identifier getAbstractLinkTypeId() {
		assert true || this.assertAbstractLinkTypeSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertAbstractLinkTypeSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		return this.abstractLinkTypeId;
	}

	/**
	 * A wrapper around {@link #getAbstractLinkTypeId()}.
	 *
	 * Overridden by descendants to add extra checks.
	 */
	public AbstractLinkType<?> getAbstractLinkType() {
		try {
			return this.getAbstractLinkId().isVoid()
					? StorableObjectPool.<AbstractLinkType<?>>getStorableObject(this.getAbstractLinkTypeId(), true)
					: this.getAbstractLink().getType();
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
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

	Identifier getSourceAbstractSchemePortId() {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.sourceAbstractSchemePortId.isVoid()
				|| !this.sourceAbstractSchemePortId.equals(this.targetAbstractSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		return this.sourceAbstractSchemePortId;
	}

	/**
	 * A wrapper around {@link #getSourceAbstractSchemePortId()}.
	 *
	 * Overridden by descendants to add extra checks.
	 */
	public AbstractSchemePort<?> getSourceAbstractSchemePort() {
		try {
			return StorableObjectPool.getStorableObject(this.getSourceAbstractSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	Identifier getTargetAbstractSchemePortId() {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.targetAbstractSchemePortId.isVoid()
				|| !this.targetAbstractSchemePortId.equals(this.sourceAbstractSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		return this.targetAbstractSchemePortId;
	}

	/**
	 * A wrapper around {@link #getTargetAbstractSchemePortId()}.
	 *
	 * Overridden by descendants to add extra checks.
	 */
	public AbstractSchemePort<?> getTargetAbstractSchemePort() {
		try {
			return StorableObjectPool.getStorableObject(this.getTargetAbstractSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param abstractLink
	 */
	public void setAbstractLink(final AbstractLink<?> abstractLink) {
		assert this.assertAbstractLinkTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		final Identifier newLinkId = Identifier.possiblyVoid(abstractLink);
		if (this.abstractLinkId.equals(newLinkId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (this.abstractLinkId.isVoid()) {
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.abstractLinkTypeId = VOID_IDENTIFIER;
		} else if (newLinkId.isVoid()) {
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve).
			 */
			this.abstractLinkTypeId = this.getAbstractLink().getType().getId();
		}
		this.abstractLinkId = newLinkId;
		super.markAsChanged();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param abstractLinkType
	 */
	public void setAbstractLinkType(final AbstractLinkType<?> abstractLinkType) {
		assert this.assertAbstractLinkTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;
		assert abstractLinkType != null: NON_NULL_EXPECTED;

		if (this.abstractLinkId.isVoid()) {
			final Identifier newAbstractLinkTypeId = abstractLinkType.getId();
			if (this.abstractLinkTypeId.equals(newAbstractLinkTypeId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.abstractLinkTypeId = newAbstractLinkTypeId;
			super.markAsChanged();
		} else {
			this.getAbstractLink().setType(abstractLinkType);
		}
	}

	/**
	 * @see #opticalLength
	 */
	public final void setOpticalLength(final double opticalLength) {
		if (this.opticalLength == opticalLength) {
			return;
		}
		this.opticalLength = opticalLength;
		super.markAsChanged();
	}

	/**
	 * @see #physicalLength
	 */
	public final void setPhysicalLength(final double physicalLength) {
		if (this.physicalLength == physicalLength) {
			return;
		}
		this.physicalLength = physicalLength;
		super.markAsChanged();
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param sourceAbstractSchemePortId
	 */
	void setSourceAbstractSchemePortId(final Identifier sourceAbstractSchemePortId) {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.sourceAbstractSchemePortId.isVoid()
				|| !this.sourceAbstractSchemePortId.equals(this.targetAbstractSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		assert sourceAbstractSchemePortId.isVoid()
				|| !sourceAbstractSchemePortId.equals(this.targetAbstractSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		if (this.sourceAbstractSchemePortId.equals(sourceAbstractSchemePortId)) {
			return;
		}
		this.sourceAbstractSchemePortId = sourceAbstractSchemePortId;
		super.markAsChanged();
	}

	final void setAbstractLinkTypeId(final Identifier abstractLinkTypeId) {
//		TODO: inroduce additional sanity checks
		assert abstractLinkTypeId != null : NON_NULL_EXPECTED;
		assert abstractLinkTypeId.isVoid() || abstractLinkTypeId.getMajor() == LINK_TYPE_CODE || abstractLinkTypeId.getMajor() == CABLELINK_TYPE_CODE;
		this.abstractLinkTypeId = abstractLinkTypeId;
		super.markAsChanged();
	}

	final void setAbstractLinkId(final Identifier abstractLinkId) {
//		TODO: inroduce additional sanity checks
		assert abstractLinkId != null : NON_NULL_EXPECTED;
		assert abstractLinkId.isVoid() || abstractLinkId.getMajor() == LINK_CODE || abstractLinkId.getMajor() == CABLELINK_CODE;
		this.abstractLinkId = abstractLinkId;
		super.markAsChanged();
	}

	/**
	 * <p>
	 * A wrapper around {@link #setSourceAbstractSchemePortId(Identifier)}.
	 * </p>
	 *
	 * <p>
	 * Overridden by descendants to add extra checks.
	 * </p>
	 *
	 * @param sourceAbstractSchemePort
	 */
	public void setSourceAbstractSchemePort(final AbstractSchemePort<?> sourceAbstractSchemePort) {
		this.setSourceAbstractSchemePortId(Identifier.possiblyVoid(sourceAbstractSchemePort));
	}

	/**
	 * Overridden by descendants to add extra checks.
	 *
	 * @param targetAbstractSchemePortId
	 */
	void setTargetAbstractSchemePortId(final Identifier targetAbstractSchemePortId) {
		assert this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: OBJECT_NOT_INITIALIZED;
		assert this.targetAbstractSchemePortId.isVoid()
				|| !this.targetAbstractSchemePortId.equals(this.sourceAbstractSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		assert targetAbstractSchemePortId.isVoid()
				|| !targetAbstractSchemePortId.equals(this.sourceAbstractSchemePortId): CIRCULAR_DEPS_PROHIBITED;
		if (this.targetAbstractSchemePortId.equals(targetAbstractSchemePortId)) {
			return;
		}
		this.targetAbstractSchemePortId = targetAbstractSchemePortId;
		super.markAsChanged();
	}

	/**
	 * <p>
	 * A wrapper around {@link #setTargetAbstractSchemePortId(Identifier)}.
	 * </p>
	 * 
	 * <p>
	 * Overridden by descendants to add extra checks.
	 * </p>
	 *
	 * @param targetAbstractSchemePort
	 */
	public void setTargetAbstractSchemePort(final AbstractSchemePort<?> targetAbstractSchemePort) {
		this.setTargetAbstractSchemePortId(Identifier.possiblyVoid(targetAbstractSchemePort));
	}

	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier abstractLinkTypeId,
			final Identifier abstractLinkId,
			final Identifier sourceAbstractSchemePortId,
			final Identifier targetAbstractSchemePortId,
			final Identifier parentSchemeId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version, name, description, parentSchemeId);
	
			assert abstractLinkTypeId != null : NON_NULL_EXPECTED;
			assert abstractLinkId != null : NON_NULL_EXPECTED;
			assert abstractLinkTypeId.isVoid() ^ abstractLinkId.isVoid();
	
			assert sourceAbstractSchemePortId != null : NON_NULL_EXPECTED;
			assert targetAbstractSchemePortId != null : NON_NULL_EXPECTED;
	
			this.physicalLength = physicalLength;
			this.opticalLength = opticalLength;
			this.abstractLinkTypeId = abstractLinkTypeId;
			this.abstractLinkId = abstractLinkId;
			this.sourceAbstractSchemePortId = sourceAbstractSchemePortId;
			this.targetAbstractSchemePortId = targetAbstractSchemePortId;

			this.abstractLinkTypeSet = true;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.abstractLinkTypeId != null && this.abstractLinkId != null
				&& this.sourceAbstractSchemePortId != null
				&& this.targetAbstractSchemePortId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.abstractLinkTypeId);
		dependencies.add(this.abstractLinkId);
		dependencies.add(this.sourceAbstractSchemePortId);
		dependencies.add(this.targetAbstractSchemePortId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @param abstractSchemeLink
	 * @param abstractLinkTypeId1
	 * @param abstractLinkId1
	 * @param sourceAbstractSchemePortId1
	 * @param targetAbstractSchemePortId1
	 * @throws CreateObjectException
	 */
	final void fromTransferable(
			final IdlAbstractSchemeLink abstractSchemeLink,
			final IdlIdentifier abstractLinkTypeId1,
			final IdlIdentifier abstractLinkId1,
			final IdlIdentifier sourceAbstractSchemePortId1,
			final IdlIdentifier targetAbstractSchemePortId1)
	throws CreateObjectException {
		super.fromTransferable(abstractSchemeLink);
		this.physicalLength = abstractSchemeLink.physicalLength;
		this.opticalLength = abstractSchemeLink.opticalLength;
		this.abstractLinkTypeId = new Identifier(abstractLinkTypeId1);
		this.abstractLinkId = new Identifier(abstractLinkId1);
		this.sourceAbstractSchemePortId = new Identifier(sourceAbstractSchemePortId1);
		this.targetAbstractSchemePortId = new Identifier(targetAbstractSchemePortId1);

		this.abstractLinkTypeSet = true;
	}

	/**
	 * @param abstractSchemeLink
	 * @param importType
	 * @throws CreateObjectException
	 */
	final void fromXmlTransferable(
			final XmlAbstractSchemeLink abstractSchemeLink,
			final String importType)
	throws CreateObjectException {
		super.fromXmlTransferable(abstractSchemeLink, importType);

		this.physicalLength = abstractSchemeLink.getPhysicalLength();
		this.opticalLength = abstractSchemeLink.getOpticalLength();

		this.abstractLinkTypeSet = true;
	}

	/**
	 * @param abstractSchemeLink
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @throws ApplicationException
	 */
	final void getXmlTransferable(final XmlAbstractSchemeLink abstractSchemeLink,
			final String importType,
			final boolean usePool)
	throws XmlConversionException, ApplicationException {
		super.getXmlTransferable(abstractSchemeLink, importType, usePool);
		abstractSchemeLink.setPhysicalLength(this.physicalLength);
		abstractSchemeLink.setOpticalLength(this.opticalLength);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertAbstractLinkTypeSetNonStrict() {
		if (this.abstractLinkTypeSet) {
			return this.assertAbstractLinkTypeSetStrict();
		}
		this.abstractLinkTypeSet = true;
		return this.abstractLinkId != null
				&& this.abstractLinkTypeId != null
				&& this.abstractLinkId.isVoid()
				&& this.abstractLinkTypeId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertAbstractLinkTypeSetStrict() {
		return this.abstractLinkId != null
				&& this.abstractLinkTypeId != null
				&& (this.abstractLinkId.isVoid() ^ this.abstractLinkTypeId.isVoid());
	}
}
