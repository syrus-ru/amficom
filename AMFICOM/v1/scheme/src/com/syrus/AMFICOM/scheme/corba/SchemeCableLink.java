/*
 * $Id: SchemeCableLink.java,v 1.4 2005/03/15 17:47:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/15 17:47:57 $
 * @module scheme_v1
 */
public final class SchemeCableLink extends AbstractSchemeLink implements
		ComSyrusAmficomConfigurationSchemeCableLink {
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

	/**
	 * @see #abstractLinkTypeImpl()
	 */
	public AbstractLinkType_Transferable abstractLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #abstractLinkTypeImpl(AbstractLinkType)
	 */
	public void abstractLinkType(
			final AbstractLinkType_Transferable abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #abstractLinkType()
	 */
	public AbstractLinkType abstractLinkTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #abstractLinkType(AbstractLinkType_Transferable)
	 */
	public void abstractLinkTypeImpl(final AbstractLinkType abstractLinkType) {
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

	public CableChannelingItem[] cableChannelingItems() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableChannelingItems
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableLink#cableChannelingItems(com.syrus.AMFICOM.scheme.corba.CableChannelingItem[])
	 */
	public void cableChannelingItems(
			CableChannelingItem[] newCableChannelingItems) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkTypeImpl()
	 */
	public CableLinkType_Transferable cableLinkType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkTypeImpl(CableLinkType)
	 */
	public void cableLinkType(final CableLinkType_Transferable cableLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkType()
	 */
	public CableLinkType cableLinkTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #cableLinkType(CableLinkType_Transferable)
	 */
	public void cableLinkTypeImpl(final CableLinkType cableLinkType) {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
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

	/**
	 * @see #linkImpl()
	 */
	public Link_Transferable link() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLink
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemeLink#link(com.syrus.AMFICOM.configuration.corba.Link_Transferable)
	 */
	public void link(Link_Transferable newLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #link()
	 */
	public Link linkImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinkImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemeLink#linkImpl(com.syrus.AMFICOM.configuration.Link)
	 */
	public void linkImpl(Link newLinkImpl) {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
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

	public SchemeCableThread[] schemeCableThreads() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCableThreads
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableLink#schemeCableThreads(com.syrus.AMFICOM.scheme.corba.SchemeCableThread[])
	 */
	public void schemeCableThreads(SchemeCableThread[] newSchemeCableThreads) {
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

	public AbstractSchemePort sourceAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSourceAbstractSchemePort
	 * @see com.syrus.AMFICOM.scheme.corba.AbstractSchemeLink#sourceAbstractSchemePort(com.syrus.AMFICOM.scheme.corba.AbstractSchemePort)
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
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableLink#sourceSchemeCablePort(com.syrus.AMFICOM.scheme.corba.SchemeCablePort)
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
	 * @see com.syrus.AMFICOM.scheme.corba.AbstractSchemeLink#targetAbstractSchemePort(com.syrus.AMFICOM.scheme.corba.AbstractSchemePort)
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
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableLink#targetSchemeCablePort(com.syrus.AMFICOM.scheme.corba.SchemeCablePort)
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
