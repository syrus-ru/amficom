/*
 * $Id: SchemeLink.java,v 1.3 2005/03/17 12:52:55 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.SiteNode;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/17 12:52:55 $
 * @module scheme_v1
 */
public final class SchemeLink extends AbstractSchemeLink {
	private static final long serialVersionUID = 3834587703751947064L;

	protected Identifier siteId = null;

	/**
	 * @param id
	 */
	protected SchemeLink(Identifier id) {
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
	protected SchemeLink(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	public AbstractLinkType getAbstractLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newAbstractLinkTypeImpl
	 */
	public void setAbstractLinkType(
			AbstractLinkType newAbstractLinkTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public boolean alarmed() {
		throw new UnsupportedOperationException();
	}

	public void alarmed(boolean alarmed) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Collection getCharacteristics() {
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

	public LinkType getLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinkTypeImpl
	 */
	public void setLinkType(LinkType newLinkTypeImpl) {
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
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public void scheme(Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	public SiteNode getSiteNode() {
		throw new UnsupportedOperationException();
	}

	public void setSiteNode(final SiteNode siteNode) {
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

	public SchemePort sourceSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSourceSchemePort
	 * @see com.syrus.AMFICOM.scheme.SchemeLink#sourceSchemePort(com.syrus.AMFICOM.scheme.corba.SchemePort)
	 */
	public void sourceSchemePort(SchemePort newSourceSchemePort) {
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

	public SchemePort targetSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTargetSchemePort
	 * @see com.syrus.AMFICOM.scheme.SchemeLink#targetSchemePort(com.syrus.AMFICOM.scheme.corba.SchemePort)
	 */
	public void targetSchemePort(SchemePort newTargetSchemePort) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemeLink schemeLink = (SchemeLink) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeLink;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static SchemeLink createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_LINK_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeLink.changed = true;
			return schemeLink;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeLink.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeLink createInstance() {
		throw new UnsupportedOperationException();
	}
}
