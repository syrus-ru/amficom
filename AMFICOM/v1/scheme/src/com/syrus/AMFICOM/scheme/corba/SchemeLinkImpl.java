/*
 * $Id: SchemeLinkImpl.java,v 1.3 2004/11/30 07:54:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.CORBA.Map.MapSiteElement_Transferable;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/11/30 07:54:42 $
 * @module schemecommon_v1
 */
final class SchemeLinkImpl extends SchemeLink implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();
	
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
	 * @return
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

	public boolean alarmed() {
		throw new UnsupportedOperationException();
	}

	public void alarmed(boolean alarmed) {
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
	 * @return
	 * @see java.util.JavaUtilCharacterizable#characteristicsImpl()
	 */
	public List characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCharacteristicsImpl
	 * @see java.util.JavaUtilCharacterizable#characteristicsImpl(java.util.List)
	 */
	public void characteristicsImpl(List newCharacteristicsImpl) {
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

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getCreatorId()
	 */
	public com.syrus.AMFICOM.general.Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getId()
	 */
	public com.syrus.AMFICOM.general.Identifier getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getModifierId()
	 */
	public com.syrus.AMFICOM.general.Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
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
	 * @return
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
	 * @return
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

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public void scheme(Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	public MapSiteElement_Transferable site() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSite
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeLink#site(com.syrus.AMFICOM.CORBA.Map.MapSiteElement_Transferable)
	 */
	public void site(MapSiteElement_Transferable newSite) {
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
		throw new UnsupportedOperationException();
	}
}
