/*
 * $Id: SchemeProtoGroupImpl.java,v 1.3 2004/11/30 07:54:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.resource.ImageResource;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/11/30 07:54:42 $
 * @module schemecommon_v1
 */
final class SchemeProtoGroupImpl extends SchemeProtoGroup implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeProtoGroupImpl() {
	}

	public SchemeProtoGroup cloneInstance() {
		try {
			return (SchemeProtoGroup) this.clone();
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

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup parent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newParent
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#parent(com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup)
	 */
	public void parent(SchemeProtoGroup newParent) {
		throw new UnsupportedOperationException();
	}

	public String schemeProtoElementClass() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeProtoElementClass
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#schemeProtoElementClass(java.lang.String)
	 */
	public void schemeProtoElementClass(String newSchemeProtoElementClass) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoElement[] schemeProtoElements() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeProtoElements
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#schemeProtoElements(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement[])
	 */
	public void schemeProtoElements(SchemeProtoElement[] newSchemeProtoElements) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup[] schemeProtoGroups() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeProtoGroups
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#schemeProtoGroups(com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup[])
	 */
	public void schemeProtoGroups(SchemeProtoGroup[] newSchemeProtoGroups) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbol()
	 */
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

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
