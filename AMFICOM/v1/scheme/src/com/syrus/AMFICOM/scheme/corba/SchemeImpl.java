/*
 * $Id: SchemeImpl.java,v 1.7 2004/12/17 11:57:12 bass Exp $
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
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;
import com.syrus.util.logging.ErrorHandler;

import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/12/17 11:57:12 $
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

	/**
	 * @see java.util.JavaUtilIStorableObject#createdImpl()
	 */
	public Date createdImpl() {
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
	 * @see java.util.JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
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

	/**
	 * @see java.util.JavaUtilIStorableObject#modifiedImpl()
	 */
	public Date modifiedImpl() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
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
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl()
	 */
	public SchemeImageResource schemeCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl(SchemeImageResource)
	 */
	public void schemeCellImpl(SchemeImageResource schemeCellImpl) {
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
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl()
	 */
	public BitmapImageResource symbolImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param symbolImpl
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl(BitmapImageResource)
	 */
	public void symbolImpl(BitmapImageResource symbolImpl) {
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
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl()
	 */
	public SchemeImageResource ugoCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ugoCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl(SchemeImageResource)
	 */
	public void ugoCellImpl(SchemeImageResource ugoCellImpl) {
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
