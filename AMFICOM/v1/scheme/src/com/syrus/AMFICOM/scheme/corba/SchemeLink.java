/*
 * $Id: SchemeLink.java,v 1.3 2005/03/11 17:26:59 bass Exp $
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
import com.syrus.AMFICOM.map.*;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/11 17:26:59 $
 * @module scheme_v1
 */
public final class SchemeLink extends AbstractSchemeLink implements
		ComSyrusAmficomConfigurationSchemeLink,
		ComSyrusAmficomMapSchemeLink {

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

	public AbstractLinkType_Transferable abstractLinkType() {
		throw new UnsupportedOperationException();
	}

	public void abstractLinkType(
			AbstractLinkType_Transferable abstractLinkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemeLink#abstractLinkTypeImpl()
	 */
	public AbstractLinkType abstractLinkTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newAbstractLinkTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemeLink#abstractLinkTypeImpl(com.syrus.AMFICOM.configuration.AbstractLinkType)
	 */
	public void abstractLinkTypeImpl(
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

	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

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
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationAbstractSchemeLink#linkImpl()
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

	public LinkType_Transferable linkType() {
		throw new UnsupportedOperationException();
	}

	public void linkType(LinkType_Transferable linkType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeLink#linkTypeImpl()
	 */
	public LinkType linkTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinkTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeLink#linkTypeImpl(com.syrus.AMFICOM.configuration.LinkType)
	 */
	public void linkTypeImpl(LinkType newLinkTypeImpl) {
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

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeLink#siteNode()
	 */
	public SiteNode_Transferable siteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param siteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeLink#siteNode(SiteNode_Transferable)
	 */
	public void siteNode(final SiteNode_Transferable siteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeLink#siteNodeImpl()
	 */
	public SiteNode siteNodeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param siteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeLink#siteNodeImpl(SiteNode)
	 */
	public void siteNodeImpl(final SiteNode siteNode) {
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

	public SchemePort sourceSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSourceSchemePort
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeLink#sourceSchemePort(com.syrus.AMFICOM.scheme.corba.SchemePort)
	 */
	public void sourceSchemePort(SchemePort newSourceSchemePort) {
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

	public SchemePort targetSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTargetSchemePort
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeLink#targetSchemePort(com.syrus.AMFICOM.scheme.corba.SchemePort)
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
