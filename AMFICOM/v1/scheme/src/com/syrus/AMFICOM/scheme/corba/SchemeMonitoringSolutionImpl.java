/*
 * $Id: SchemeMonitoringSolutionImpl.java,v 1.11 2005/03/10 15:06:08 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.logging.ErrorHandler;
import java.util.Date;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/03/10 15:06:08 $
 * @module scheme_v1
 */
final class SchemeMonitoringSolutionImpl extends SchemeMonitoringSolution implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3617859659659358515L;

	SchemeMonitoringSolutionImpl() {
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
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
	public Identifier[] getDependencies() {
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
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public double price() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPrice
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution#price(double)
	 */
	public void price(double newPrice) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newScheme
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution#scheme(com.syrus.AMFICOM.scheme.corba.Scheme)
	 */
	public void scheme(Scheme newScheme) {
		throw new UnsupportedOperationException();
	}

	public SchemePath[] schemePaths() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemePaths
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution#schemePaths(com.syrus.AMFICOM.scheme.corba.SchemePath[])
	 */
	public void schemePaths(SchemePath[] newSchemePaths) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeMonitoringSolutionImpl schemeMonitoringSolution = (SchemeMonitoringSolutionImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeMonitoringSolution;
	}
}
