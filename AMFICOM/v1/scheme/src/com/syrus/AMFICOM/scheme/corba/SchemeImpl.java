/*
 * $Id: SchemeImpl.java,v 1.3 2004/11/30 07:54:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.CORBA.Map.Map_Transferable;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.resource.ImageResource;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;
import com.syrus.util.logging.ErrorHandler;

import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/11/30 07:54:42 $
 * @module schemecommon_v1
 */
final class SchemeImpl extends Scheme implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeImpl() {
	}

	public Scheme cloneInstance() {
		try {
			return (Scheme) this.clone();
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

	public Domain_Transferable domain() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDomain
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationScheme#domain(com.syrus.AMFICOM.configuration.corba.Domain_Transferable)
	 */
	public void domain(Domain_Transferable newDomain) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationScheme#domainImpl()
	 */
	public Domain domainImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDomainImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationScheme#domainImpl(com.syrus.AMFICOM.configuration.Domain)
	 */
	public void domainImpl(Domain newDomainImpl) {
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

	public int height() {
		throw new UnsupportedOperationException();
	}

	public void height(int height) {
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

	public String label() {
		throw new UnsupportedOperationException();
	}

	public void label(String label) {
		throw new UnsupportedOperationException();
	}

	public Map_Transferable map() {
		throw new UnsupportedOperationException();
	}

	public void map(Map_Transferable map) {
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

	public User_Transferable owner() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newOwner
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationScheme#owner(com.syrus.AMFICOM.configuration.corba.User_Transferable)
	 */
	public void owner(User_Transferable newOwner) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationScheme#ownerImpl()
	 */
	public User ownerImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newOwnerImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationScheme#ownerImpl(com.syrus.AMFICOM.configuration.User)
	 */
	public void ownerImpl(User newOwnerImpl) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableLink[] schemeCableLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeCableLinks(SchemeCableLink[] schemeCableLinks) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable schemeCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCell
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable)
	 */
	public void schemeCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable newSchemeCell) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl()
	 */
	public ImageResource schemeCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl(com.syrus.AMFICOM.resource.ImageResource)
	 */
	public void schemeCellImpl(ImageResource newSchemeCellImpl) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement[] schemeElements() {
		throw new UnsupportedOperationException();
	}

	public void schemeElements(SchemeElement[] schemeElements) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink[] schemeLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeLinks(SchemeLink[] schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public SchemeMonitoringSolution schemeMonitoringSolution() {
		throw new UnsupportedOperationException();
	}

	public void schemeMonitoringSolution(
			SchemeMonitoringSolution schemeMonitoringSolution) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable symbol() {
		throw new UnsupportedOperationException();
	}

	public void symbol(ImageResource_Transferable symbol) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl()
	 */
	public ImageResource symbolImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSymbolImpl
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl(com.syrus.AMFICOM.resource.ImageResource)
	 */
	public void symbolImpl(ImageResource newSymbolImpl) {
		throw new UnsupportedOperationException();
	}

	public Type type() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newType
	 * @see com.syrus.AMFICOM.scheme.corba.Scheme#type(com.syrus.AMFICOM.scheme.corba.SchemePackage.Type)
	 */
	public void type(Type newType) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable ugoCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newUgoCell
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable)
	 */
	public void ugoCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable newUgoCell) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl()
	 */
	public ImageResource ugoCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newUgoCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl(com.syrus.AMFICOM.resource.ImageResource)
	 */
	public void ugoCellImpl(ImageResource newUgoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	public int width() {
		throw new UnsupportedOperationException();
	}

	public void width(int width) {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
