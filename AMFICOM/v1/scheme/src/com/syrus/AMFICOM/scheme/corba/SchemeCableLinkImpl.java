/*
 * $Id: SchemeCableLinkImpl.java,v 1.10 2005/01/20 09:58:02 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.CharacteristicSeqContainer;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/01/20 09:58:02 $
 * @module scheme_v1
 */
final class SchemeCableLinkImpl extends SchemeCableLink implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3761131552347600181L;

	SchemeCableLinkImpl() {
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
	public void abstractLinkType(final AbstractLinkType_Transferable abstractLinkType) {
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
	public void cableChannelingItems(CableChannelingItem[] newCableChannelingItems) {
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

	/**
	 * @see StorableObject#changed()
	 */
	public boolean changed() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #characteristicsImpl()
	 */
	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see #characteristicsImpl(CharacteristicSeqContainer)
	 */
	public void characteristics(final Characteristic_Transferable characteristics[]) {
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

	public SchemeCableLink cloneInstance() {
		try {
			return (SchemeCableLink) this.clone();
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

	public SchemeCablePort sourceSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSourceSchemeCablePort
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableLink#sourceSchemeCablePort(com.syrus.AMFICOM.scheme.corba.SchemeCablePort)
	 */
	public void sourceSchemeCablePort(SchemeCablePort newSourceSchemeCablePort) {
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

	public SchemeCablePort targetSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTargetSchemeCablePort
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableLink#targetSchemeCablePort(com.syrus.AMFICOM.scheme.corba.SchemeCablePort)
	 */
	public void targetSchemeCablePort(SchemeCablePort newTargetSchemeCablePort) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeCableLinkImpl schemeCableLink = (SchemeCableLinkImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCableLink;
	}
}
