/*
 * $Id: SchemeCableThreadImpl.java,v 1.6 2004/12/17 15:58:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/12/17 15:58:57 $
 * @module schemecommon_v1
 */
final class SchemeCableThreadImpl extends SchemeCableThread implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeCableThreadImpl() {
	}

	public CableThreadType_Transferable cableThreadType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableThreadType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#cableThreadType(com.syrus.AMFICOM.configuration.corba.CableThreadType_Transferable)
	 */
	public void cableThreadType(CableThreadType_Transferable newCableThreadType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#cableThreadTypeImpl()
	 */
	public CableThreadType cableThreadTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCableThreadTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#cableThreadTypeImpl(com.syrus.AMFICOM.configuration.CableThreadType)
	 */
	public void cableThreadTypeImpl(CableThreadType newCableThreadTypeImpl) {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
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

	public SchemeCableThread cloneInstance() {
		try {
			return (SchemeCableThread) this.clone();
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

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
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

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
	 */
	public boolean isChanged() {
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

	public SchemeCableLink schemeCablelink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCablelink
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableThread#schemeCablelink(com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void schemeCablelink(SchemeCableLink newSchemeCablelink) {
		throw new UnsupportedOperationException();
	}

	public SchemePort schemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemePort
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeCableThread#schemePort(com.syrus.AMFICOM.scheme.corba.SchemePort)
	 */
	public void schemePort(SchemePort newSchemePort) {
		throw new UnsupportedOperationException();
	}

	public Link_Transferable thread() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newThread
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#thread(com.syrus.AMFICOM.configuration.corba.Link_Transferable)
	 */
	public void thread(Link_Transferable newThread) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#threadImpl()
	 */
	public Link threadImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newThreadImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeCableThread#threadImpl(com.syrus.AMFICOM.configuration.Link)
	 */
	public void threadImpl(Link newThreadImpl) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
