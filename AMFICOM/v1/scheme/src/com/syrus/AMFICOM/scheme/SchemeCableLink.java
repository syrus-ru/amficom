/*
 * $Id: SchemeCableLink.java,v 1.3 2005/03/22 17:31:55 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/22 17:31:55 $
 * @module scheme_v1
 */
public final class SchemeCableLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3760847878314274867L;

	protected Identifier cableChannelingItemIds[] = null;

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

	public AbstractLinkType getAbstractLinkType() {
		throw new UnsupportedOperationException();
	}

	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	public CableChannelingItem[] cableChannelingItems() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableChannelingItems
	 * @see com.syrus.AMFICOM.scheme.SchemeCableLink#cableChannelingItems(com.syrus.AMFICOM.scheme.corba.CableChannelingItem[])
	 */
	public void cableChannelingItems(
			CableChannelingItem[] newCableChannelingItems) {
		throw new UnsupportedOperationException();
	}

	public CableLinkType getCableLinkType() {
		throw new UnsupportedOperationException();
	}

	public void setCableLinkType(final CableLinkType cableLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
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
	 * @param newLinkImpl
	 */
	public void setLink(Link newLinkImpl) {
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

	public SchemeCableThread[] schemeCableThreads() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCableThreads
	 * @see com.syrus.AMFICOM.scheme.SchemeCableLink#schemeCableThreads(com.syrus.AMFICOM.scheme.corba.SchemeCableThread[])
	 */
	public void schemeCableThreads(SchemeCableThread[] newSchemeCableThreads) {
		throw new UnsupportedOperationException();
	}

	public AbstractSchemePort sourceAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSourceAbstractSchemePort
	 * @see com.syrus.AMFICOM.scheme.AbstractSchemeLink#sourceAbstractSchemePort(com.syrus.AMFICOM.scheme.corba.AbstractSchemePort)
	 */
	public void sourceAbstractSchemePort(
			AbstractSchemePort newSourceAbstractSchemePort) {
		throw new UnsupportedOperationException();
	}

	public SchemeCablePort sourceSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSourceSchemeCablePort
	 * @see com.syrus.AMFICOM.scheme.SchemeCableLink#sourceSchemeCablePort(com.syrus.AMFICOM.scheme.corba.SchemeCablePort)
	 */
	public void sourceSchemeCablePort(
			SchemeCablePort newSourceSchemeCablePort) {
		throw new UnsupportedOperationException();
	}

	public AbstractSchemePort targetAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTargetAbstractSchemePort
	 * @see com.syrus.AMFICOM.scheme.AbstractSchemeLink#targetAbstractSchemePort(com.syrus.AMFICOM.scheme.corba.AbstractSchemePort)
	 */
	public void targetAbstractSchemePort(
			AbstractSchemePort newTargetAbstractSchemePort) {
		throw new UnsupportedOperationException();
	}

	public SchemeCablePort targetSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTargetSchemeCablePort
	 * @see com.syrus.AMFICOM.scheme.SchemeCableLink#targetSchemeCablePort(com.syrus.AMFICOM.scheme.corba.SchemeCablePort)
	 */
	public void targetSchemeCablePort(
			SchemeCablePort newTargetSchemeCablePort) {
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
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
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeCableLink createInstance() {
		throw new UnsupportedOperationException();
	}
}
