/*
 * $Id: SchemeLinkImpl.java,v 1.9 2004/12/22 11:07:40 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.scheme.CharacteristicSeqContainer;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2004/12/22 11:07:40 $
 * @module scheme_v1
 */
final class SchemeLinkImpl extends SchemeLink implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3256722862231728441L;

	SchemeLinkImpl() {
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
	public void abstractLinkTypeImpl(AbstractLinkType newAbstractLinkTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.configuration.Characterizable#addCharacteristic(Characteristic)
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
	 * @see StorableObject#changed()
	 */
	public boolean changed() {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(
			Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.Characterizable#characteristicsImpl()
	 */
	public CharacteristicSeqContainer characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.scheme.Characterizable#characteristicsImpl(CharacteristicSeqContainer)
	 */
	public void characteristicsImpl(final CharacteristicSeqContainer characteristics) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink cloneInstance() {
		try {
			return (SchemeLink) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#dependencies()
	 */
	public StorableObject[] dependencies() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
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

	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
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
	 * @see com.syrus.AMFICOM.configuration.Characterizable#removeCharacteristic(Characteristic)
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
	public void sourceAbstractSchemePort(AbstractSchemePort newSourceAbstractSchemePort) {
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
	public void targetAbstractSchemePort(AbstractSchemePort newTargetAbstractSchemePort) {
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

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeLinkImpl schemeLink = (SchemeLinkImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeLink;
	}
}
