/*-
 * $Id: SchemeCableLink.java,v 1.17 2005/04/18 16:00:30 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
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
 * @version $Revision: 1.17 $, $Date: 2005/04/18 16:00:30 $
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
	SchemeCableLink(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeCableLink(final SchemeCableLink_Transferable transferable) throws CreateObjectException {
		this.schemeCableLinkDatabase = SchemeDatabaseContext.getSchemeCableLinkDatabase();
		fromTransferable(transferable);
	}

	public static SchemeCableLink createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeCableLink schemeCableLink = new SchemeCableLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeCableLink.changed = true;
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

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	public AbstractLinkType getAbstractLinkType() {
		return getCableLinkType();
	}

	public Set getCableChannelingItems() {
		throw new UnsupportedOperationException();
	}

	public CableLinkType getCableLinkType() {
		throw new UnsupportedOperationException();
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
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	public Scheme getParentScheme() {
		assert this.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid(): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;

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
		throw new UnsupportedOperationException();
	}

	public void removeSchemeCableThread(final SchemeCableThread schemeCableThread) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		setCableLinkType((CableLinkType) abstractLinkType);
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

	public void setCableLinkType(final CableLinkType cableLinkType) {
		throw new UnsupportedOperationException();
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
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	public void setParentScheme(final Scheme parentScheme) {
		assert this.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid(): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
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
		throw new UnsupportedOperationException();
	}
}
