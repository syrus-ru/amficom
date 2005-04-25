/*-
 * $Id: SchemeCableLink.java,v 1.24 2005/04/25 16:26:41 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink_Transferable;
import com.syrus.util.Log;

/**
 * #11 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.24 $, $Date: 2005/04/25 16:26:41 $
 * @module scheme_v1
 */
public final class SchemeCableLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3760847878314274867L;

	private SchemeCableLinkDatabase schemeCableLinkDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCableLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		this.schemeCableLinkDatabase = SchemeDatabaseContext.getSchemeCableLinkDatabase();
		try {
			this.schemeCableLinkDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	SchemeCableLink(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength,
			final CableLinkType cableLinkType,
			final Link link,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, physicalLength,
				opticalLength, cableLinkType, link,
				sourceSchemeCablePort, targetSchemeCablePort,
				parentScheme);

		this.schemeCableLinkDatabase = SchemeDatabaseContext.getSchemeCableLinkDatabase();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeCableLink(final SchemeCableLink_Transferable transferable) throws CreateObjectException {
		this.schemeCableLinkDatabase = SchemeDatabaseContext.getSchemeCableLinkDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, CableLinkType, Link, SchemeCablePort, SchemeCablePort, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeCableLink createInstance(final Identifier creatorId,
			final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null, //$NON-NLS-1$
				null, null, parentScheme);
	}

	
	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param cableLinkType
	 * @param link
	 * @param sourceSchemeCablePort
	 * @param targetSchemeCablePort
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeCableLink createInstance(final Identifier creatorId,
			final String name, final String description,
			final double physicalLength, final double opticalLength,
			final CableLinkType cableLinkType, final Link link,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentScheme != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCableLink schemeCableLink = new SchemeCableLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, physicalLength,
					opticalLength, cableLinkType, link,
					sourceSchemeCablePort,
					targetSchemeCablePort, parentScheme);
			schemeCableLink.changed = true;
			if (link != null || cableLinkType != null)
				schemeCableLink.abstractLinkTypeSet = true;
			return schemeCableLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeCableLink.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
		}
	}

	public void addCableChannelingItem(final CableChannelingItem cableChannelingItem) {
		assert cableChannelingItem != null: ErrorMessages.NON_NULL_EXPECTED;
		cableChannelingItem.setParentSchemeCableLink(this);
	}

	public void addSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeCableThread.setParentSchemeCableLink(this);
	}

	public Object clone() {
		final SchemeCableLink schemeCableLink = (SchemeCableLink) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableLink;
	}

	public Set getCableChannelingItems() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLELINK;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemeLink#getLink()
	 */
	public Link getLink() {
		final Link link = super.getLink();
		assert link == null || link.getSort().value() == LinkSort._LINKSORT_CABLELINK: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return link;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	public AbstractLinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof CableLinkType;
		return abstractLinkType;
	}

	public CableLinkType getCableLinkType() {
		return (CableLinkType) this.getAbstractLinkType();
	}

	/**
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	public Scheme getParentScheme() {
		assert super.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		return super.getParentScheme();
	}

	public Set getSchemeCableThreads() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	public AbstractSchemePort getSourceAbstractSchemePort() {
		return getSourceSchemeCablePort();
	}

	public SchemeCablePort getSourceSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	public AbstractSchemePort getTargetAbstractSchemePort() {
		return getTargetSchemeCablePort();
	}

	public SchemeCablePort getTargetSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	public void removeCableChannelingItem(final CableChannelingItem cableChannelingItem) {
		assert cableChannelingItem != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCableChannelingItems().contains(cableChannelingItem): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		cableChannelingItem.setParentSchemeCableLink(null);
	}

	public void removeSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeCableThreads().contains(schemeCableThread): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableThread.setParentSchemeCableLink(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param cableLinkTypeId
	 * @param linkId
	 * @param sourceSchemeCablePortId
	 * @param targetSchemeCablePortId
	 * @param parentSchemeId
	 * @see AbstractSchemeLink#setAttributes(Date, Date, Identifier, Identifier, long, String, String, double, double, Identifier, Identifier, Identifier, Identifier, Identifier)
	 */
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier cableLinkTypeId,
			final Identifier linkId,
			final Identifier sourceSchemeCablePortId,
			final Identifier targetSchemeCablePortId,			
			final Identifier parentSchemeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, name, description, physicalLength, opticalLength, cableLinkTypeId, linkId, sourceSchemeCablePortId, targetSchemeCablePortId, parentSchemeId);

		assert !parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
	}

	public void setCableChannelingItems(final Set cableChannelingItems) {
		assert cableChannelingItems != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldCableChannelingItemIterator = getCableChannelingItems().iterator(); oldCableChannelingItemIterator.hasNext();) {
			final CableChannelingItem oldCableChannelingItem = (CableChannelingItem) oldCableChannelingItemIterator.next();
			/*
			 * Check is made to prevent CableChannelingItems from
			 * permanently losing their parents.
			 */
			assert !cableChannelingItems.contains(oldCableChannelingItem);
			removeCableChannelingItem(oldCableChannelingItem);
		}
		for (final Iterator cableChannelingItemIterator = cableChannelingItems.iterator(); cableChannelingItemIterator.hasNext();)
			addCableChannelingItem((CableChannelingItem) cableChannelingItemIterator.next());
	}

	/**
	 * @param link
	 * @see AbstractSchemeLink#setLink(Link)
	 */
	public void setLink(final Link link) {
		assert link == null || link.getSort().value() == LinkSort._LINKSORT_CABLELINK: ErrorMessages.NATURE_INVALID;
		super.setLink(link);
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert abstractLinkType instanceof CableLinkType;
		super.setAbstractLinkType(abstractLinkType);
	}

	/**
	 * @param cableLinkType
	 */
	public void setCableLinkType(final CableLinkType cableLinkType) {
		this.setAbstractLinkType(cableLinkType);
	}

	/**
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	public void setParentScheme(final Scheme parentScheme) {
		assert super.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		super.setParentScheme(parentScheme);
	}

	public void setSchemeCableThreads(final Set schemeCableThreads) {
		assert schemeCableThreads != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeCableThreadIterator = getSchemeCableThreads().iterator(); oldSchemeCableThreadIterator.hasNext();) {
			final SchemeCableThread oldSchemeCableThread = (SchemeCableThread) oldSchemeCableThreadIterator.next();
			/*
			 * Check is made to prevent SchemeCableThreads from
			 * permanently losing their parents.
			 */
			assert !schemeCableThreads.contains(oldSchemeCableThread);
			removeSchemeCableThread(oldSchemeCableThread);
		}
		for (final Iterator schemeCableThreadIterator = schemeCableThreads.iterator(); schemeCableThreadIterator.hasNext();)
			addSchemeCableThread((SchemeCableThread) schemeCableThreadIterator.next());
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		setSourceSchemeCablePort((SchemeCablePort) sourceAbstractSchemePort);
	}

	public void setSourceSchemeCablePort(final SchemeCablePort sourceSchemeCablePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		setTargetSchemeCablePort((SchemeCablePort) targetAbstractSchemePort);
	}

	public void setTargetSchemeCablePort(final SchemeCablePort targetSchemeCablePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemeCableLink_Transferable schemeCableLink = (SchemeCableLink_Transferable) transferable;
		fromTransferable(schemeCableLink.header, schemeCableLink.name, schemeCableLink.description, schemeCableLink.parentSchemeId, schemeCableLink.characteristicIds);
		throw new UnsupportedOperationException();
	}
}
