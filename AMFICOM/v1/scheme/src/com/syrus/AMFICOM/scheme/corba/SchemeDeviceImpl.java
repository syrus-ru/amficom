/*
 * $Id: SchemeDeviceImpl.java,v 1.6 2004/12/17 15:58:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/12/17 15:58:58 $
 * @module schemecommon_v1
 */
final class SchemeDeviceImpl extends SchemeDevice implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeDeviceImpl() {
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

	public SchemeDevice cloneInstance() {
		try {
			return (SchemeDevice) this.clone();
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

	public SchemeCablePort[] schemeCablePorts() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCablePorts
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeDevice#schemeCablePorts(com.syrus.AMFICOM.scheme.corba.SchemeCablePort[])
	 */
	public void schemeCablePorts(SchemeCablePort[] newSchemeCablePorts) {
		throw new UnsupportedOperationException();
	}

	public SchemePort[] schemePorts() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemePorts
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeDevice#schemePorts(com.syrus.AMFICOM.scheme.corba.SchemePort[])
	 */
	public void schemePorts(SchemePort[] newSchemePorts) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
