/*-
 * $Id: SchemeCableLink.java,v 1.6 2005/03/28 08:24:52 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * #11 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/03/28 08:24:52 $
 * @module scheme_v1
 */
public final class SchemeCableLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3760847878314274867L;

	protected Identifier schemeCableThreadIds[] = null;

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

	/**
	 * @deprecated
	 */
	public CableChannelingItem[] getCableChannelingItemsAsArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setCableChannelingItemsAsArray(final CableChannelingItem cableChannelingItems[]) {
		throw new UnsupportedOperationException();
	}

	public Collection getCableChannelingItems() {
		throw new UnsupportedOperationException();
	}

	public void setCableChannelingItems(final Collection cableChannelingItems) {
		throw new UnsupportedOperationException();
	}

	public void addCableChannelingItem(final CableChannelingItem cableChannelingItem) {
		throw new UnsupportedOperationException();
	}

	public void removeCableChannelingItem(final CableChannelingItem cableChannelingItem) {
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

	public AbstractLinkType getAbstractLinkType() {
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

	public Link getLink() {
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

	public double opticalLength() {
		throw new UnsupportedOperationException();
	}

	public void opticalLength(double opticalLength) {
		throw new UnsupportedOperationException();
	}

	public double physicalLength() {
		throw new UnsupportedOperationException();
	}

	public void physicalLength(double physicalLength) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemeCableThread[] getSchemeCableThreadsAsArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setSchemeCableThreadsAsArray(final SchemeCableThread schemeCableThreads[]) {
		throw new UnsupportedOperationException();
	}

	public Collection getSchemeCableThreads() {
		throw new UnsupportedOperationException();
	}

	public void setSchemeCableThreads(final Collection schemeCableThreads) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeCableThread(final SchemeCableThread schemeCableThread) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeCableThread(final SchemeCableThread schemeCableThread) {
		throw new UnsupportedOperationException();
	}

	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	public void setCableLinkType(final CableLinkType cableLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinkImpl
	 */
	public void setLink(Link newLinkImpl) {
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
