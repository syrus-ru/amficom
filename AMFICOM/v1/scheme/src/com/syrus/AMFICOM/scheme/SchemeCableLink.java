/*-
 * $Id: SchemeCableLink.java,v 1.7 2005/03/28 12:01:27 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * #11 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/03/28 12:01:27 $
 * @module scheme_v1
 */
public final class SchemeCableLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3760847878314274867L;

	/**
	 * @param id
	 */
	SchemeCableLink(Identifier id) {
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
	SchemeCableLink(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeCableLink createInstance() {
		throw new UnsupportedOperationException();
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
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeCableLink.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public void addCableChannelingItem(final CableChannelingItem cableChannelingItem) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeCableThread(final SchemeCableThread schemeCableThread) {
		throw new UnsupportedOperationException();
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

	public Collection getCableChannelingItems() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public CableChannelingItem[] getCableChannelingItemsAsArray() {
		throw new UnsupportedOperationException();
	}

	public CableLinkType getCableLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLELINK;
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
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

	public Collection getSchemeCableThreads() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemeCableThread[] getSchemeCableThreadsAsArray() {
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
	public Object getTransferable() {
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

	public void setCableChannelingItems(final Collection cableChannelingItems) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setCableChannelingItemsAsArray(final CableChannelingItem cableChannelingItems[]) {
		throw new UnsupportedOperationException();
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

	public void setSchemeCableThreads(final Collection schemeCableThreads) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setSchemeCableThreadsAsArray(final SchemeCableThread schemeCableThreads[]) {
		throw new UnsupportedOperationException();
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
}
